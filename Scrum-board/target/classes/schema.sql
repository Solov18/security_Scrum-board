CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL

);

CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(50) NOT NULL,
    task_status VARCHAR(50) NOT NULL,
    date_create TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id INT NOT NULL,
    user_role VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

--task_status varchar(50) NOT NULL DEFAULT 'TO_DO',

INSERT INTO users (username, email, password, role)
VALUES
    ('user1', 'user1@example.com', 'password1', 'USER'),
    ('user2', 'user2@example.com', 'password2', 'MANAGER'),
    ('1', '1@mail', '1', 'ADMIN');

INSERT INTO tasks (title, description, task_status, date_create, user_id, user_role)
VALUES ('Задача 1', 'Описание задачи 1', 'В работе', NOW(), 1, 'USER');
INSERT INTO tasks (title, description, task_status, date_create, user_id, user_role)
VALUES ('Задача 1', 'Описание задачи 2', 'В работе', NOW(), 2, 'MANAGER');
INSERT INTO tasks (title, description, task_status, date_create, user_id, user_role)
VALUES ('Задача 1', 'Описание задачи 3', 'В работе', NOW(), 3, 'ADMIN');