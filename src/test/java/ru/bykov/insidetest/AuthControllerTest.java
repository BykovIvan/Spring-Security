package ru.bykov.insidetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.bykov.insidetest.config.SecurityConfig;
import ru.bykov.insidetest.controller.AuthController;
import ru.bykov.insidetest.controller.MessageController;
import ru.bykov.insidetest.model.Message;
import ru.bykov.insidetest.model.User;
import ru.bykov.insidetest.model.dto.MessageDto;
import ru.bykov.insidetest.payload.LoginDto;
import ru.bykov.insidetest.payload.SignUpDto;
import ru.bykov.insidetest.repository.UserRepository;
import ru.bykov.insidetest.service.CustomUserDetailsService;
import ru.bykov.insidetest.service.UserService;
import ru.bykov.insidetest.service.impl.UserServiceImpl;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        mvc.perform(get("/message"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    void createUserResponseOKTest() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("ivan");
        signUpDto.setPassword("password");
        signUpDto.setUsername("ivan");
        signUpDto.setRole("");
        signUpDto.setEmail("ivan@yandex.ru");
        String body = mapper.writeValueAsString(signUpDto);
        mvc.perform(post("/auth/signup").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("ivan");
        signUpDto.setPassword("password");
        signUpDto.setUsername("ivan");
        signUpDto.setRole("");
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

        String token = result.getResponse().getContentAsString();

        MessageDto messageDto = new MessageDto();
        messageDto.setName(signUpDto.getName());
        messageDto.setMessage("Hello?");
        mvc.perform(get("/message")
                        .content(body)

                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer_" + token))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void rootWithMockUserStatusIsOK() throws Exception {
        this.mvc.perform(get("/")).andExpect(status().isOk());
    }
}
