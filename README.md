🔐 Authorization Service
Сервис авторизации пользователей с ролевой моделью и поддержкой JWT.

📌 Возможности
Регистрация новых пользователей

Аутентификация с использованием JWT (access и refresh токены)

Хранение пароля в зашифрованном виде

Ролевая модель (ADMIN, PREMIUM_USER, GUEST)

Обновление access токена по refresh токену без повторного ввода пароля

Отзыв токена (logout)

Повышение роли GUEST до PREMIUM_USER


🧱 Архитектура и технологии
Java 17+

Spring Boot

Spring Security

JWT (JSON Web Token)

Hibernate / Spring Data JPA

H2 / PostgreSQL (настраиваемо)

Maven



👤 Структура пользователя
java
Копировать
Редактировать

public class User implements UserDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;       // логин
    private String password;    // шифруется BCrypt
    private Role role;          // ENUM: GUEST, PREMIUM_USER, ADMIN
}
🔐 Аутентификация и авторизация
JWT access token:

Краткоживущий (напр. 1 минуту)

Используется для доступа к защищённым ресурсам

JWT refresh token:

Долгоживущий (напр. 1 дней)

Позволяет получить новый access token

🔄 Обновление токена
Маршрут POST /api/v1/token/refresh принимает refreshToken и возвращает новый accessToken.


📥 Эндпоинты
Метод	Путь	Доступ	Описание
POST	/api/v1/register	Public	Регистрация пользователя
POST	/api/v1/login	Public	Аутентификация (логин)
POST	/api/v1/token/refresh	Public	Обновление access-токена
POST	/api/v1/logout	Авторизованные	Отзыв refresh-токена
POST	/api/v1/upgrade	Только GUEST	Повышение роли до PREMIUM и ADMIN
GET	/api/v1/dashboard	Пример	Пример защищённого ресурса. Только для ADMIN
