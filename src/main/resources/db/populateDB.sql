DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(dataTime, description, calories, userid)
VALUES  ('2022-02-19 09:19:55', 'Завтрак', 1000, 100000),
        ('2022-02-19 12:00:55', 'Обед', 1500, 100000),
        ('2022-02-19 20:00:55', 'Ужин', 900, 100000),
        ('2022-02-19 10:19:55', 'Завтрак', 1000, 100001),
        ('2022-02-19 13:00:55', 'Обед', 1500, 100001),
        ('2022-02-19 21:00:55', 'Ужин', 900, 100001);

