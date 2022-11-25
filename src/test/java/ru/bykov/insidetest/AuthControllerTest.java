package ru.bykov.insidetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.bykov.insidetest.controller.AuthController;
import ru.bykov.insidetest.model.dto.MessageDto;
import ru.bykov.insidetest.model.dto.JWTAuthResponse;
import ru.bykov.insidetest.model.dto.LoginDto;
import ru.bykov.insidetest.model.dto.SignUpDto;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void rootWhenNoAuthenticated() throws Exception {
        mvc.perform(get("/message"))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/message"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="ivan", roles={"USER","ADMIN"})
    void AuthenticatedUserWithMessage() throws Exception {

        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("ivan");
        signUpDto.setPassword("password");
        signUpDto.setRoles(new ArrayList<>());
        signUpDto.setEmail("ivan@yandex.ru");
        String body = mapper.writeValueAsString(signUpDto);
        mvc.perform(post("/auth/signup").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        LoginDto loginDto = new LoginDto();
        loginDto.setName(signUpDto.getName());
        loginDto.setPassword(signUpDto.getPassword());
        MvcResult result = mvc.perform(post("/auth/signin").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String tokenObjStr = result.getResponse().getContentAsString();

        JWTAuthResponse tokenJwt = objectMapper.readValue(tokenObjStr, JWTAuthResponse.class);

        MessageDto messageDto = new MessageDto();
        messageDto.setName(signUpDto.getName());
        messageDto.setMessage("history 10");
        String body2 = mapper.writeValueAsString(messageDto);
        mvc.perform(get("/message")
                        .content(body2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer_" + tokenJwt.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        MessageDto messageDtoNew = new MessageDto();
        messageDtoNew.setName(signUpDto.getName());
        messageDtoNew.setMessage("Hello Ivan");
        String body3 = mapper.writeValueAsString(messageDtoNew);
        mvc.perform(post("/message")
                        .content(body3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer_" + tokenJwt.getToken()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(messageDtoNew.getName()), String.class))
                .andExpect(jsonPath("$.message", is(messageDtoNew.getMessage()), String.class));


        MessageDto messageDtoGet = new MessageDto();
        messageDtoGet.setName(signUpDto.getName());
        messageDtoGet.setMessage("history 10");
        String body4 = mapper.writeValueAsString(messageDtoGet);
        mvc.perform(get("/message")
                        .content(body4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer_" + tokenJwt.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(messageDtoNew.getName()), String.class))
                .andExpect(jsonPath("$[0].message", is(messageDtoNew.getMessage()), String.class));
    }

}
