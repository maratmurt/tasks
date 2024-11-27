# Tasks
### Приложение для управления задачами
Поднимаем Docker
```shell
docker-compose up
```
Собираем проект
```shell
./mvnw clean package -DskipTests
```
Запускаем приложение
```shell
./mvnw spring-boot:run
```
Для ручных тестов в базе сохранён пользователь `admin@mail.ru` с паролем `admin`.

Swagger: http://localhost:8080/swagger-ui/index.html