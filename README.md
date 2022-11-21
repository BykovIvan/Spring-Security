# insidetest
Приложение содержит авторизацию по jwt. Так же добавлен доступ по ролям. 

Приложение работает на порту **9090**. 
# EndPoints: 
# # Для регистрации:
http://localhost:9090/auth/signup
Метод POST
# # json
{
"name": "ivan2",
"email": "ivan2@yandex.ru",
"password": "password"
}

# # Для авторизации
http://localhost:9090/auth/signin
Метод POST
# # json
{
"name": "ivan2",
"password": "password"
}
В ответе придет сформированный token. Роль по умолчанию USER.

Для добавления сообщения пользователем
http://localhost:9090/message
Метод POST
Headers "**Authorization**": "**Bearer_**" + **token**
# # json
{
"name": "ivan2",
"message": "message from user"
}

Для получения истории сообщений:
http://localhost:9090/message
Метод GET
Headers "**Authorization**": "**Bearer_**" + **token**
{
"name": "ivan2",
"message": "history 10"
}

Для запуска приложения и бд в Docker необходимо необходимо:
mvn clean package
docker-compose up

