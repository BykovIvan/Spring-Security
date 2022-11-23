# Inside-Test
Приложение содержит авторизацию по jwt. Так же добавлен доступ по ролям.
<br/> Ссылка на DockerHub https://hub.docker.com/repository/docker/bykovivan/inside-test
***
Приложение работает на порту **9090**. 
***
# EndPoints: 
## Для регистрации: ##
http://localhost:9090/auth/signup
<br/>Метод **POST**
### json: ###
{
"name": "ivan2",
"email": "ivan2@yandex.ru",
"password": "password"
}
### **curl**:
curl -d '{"name":"ivan2", "email":"ivan2@yandex.ru", "password":"password"}' -H "Content-Type: application/json" -X POST http://localhost:9090/auth/signup
***

## Для авторизации ##
http://localhost:9090/auth/signin
<br/>Метод **POST**
### json: ###
{
"name": "ivan2",
"password": "password"
}
<br/>В ответе придет сформированный token. Роль по умолчанию USER.
### **curl**:
curl -d '{"name":"ivan2", "password":"password"}' -H "Content-Type: application/json" -X POST http://localhost:9090/auth/signin
***

## Для добавления сообщения пользователем ##
http://localhost:9090/message
<br/>Метод **POST**
<br/>Headers "**Authorization**": "**Bearer_**" + **token**
### json ###
{
"name": "ivan2",
"message": "message from user"
}
### **curl**:
curl -d '{"name":"ivan2", "message":"message from user"}' -H "Content-Type: application/json" -H "Authorization: Bearer_ + **token**" -X POST http://localhost:9090/message
***

## Для получения истории сообщений: ##
http://localhost:9090/message
<br/>Метод **GET**
<br/>Headers "**Authorization**": "**Bearer_**" + **token**
### json ###
{
"name": "ivan2",
"message": "history 10"
}
### **curl**:
curl -d '{"name":"ivan2", "message":"history 10"}' -H "Content-Type: application/json" -H "Authorization: Bearer_ + **token**" -X GET http://localhost:9090/message
***

## Для запуска приложения и бд в Docker необходимо: ##
-mvn clean package
<br/>-docker-compose up

