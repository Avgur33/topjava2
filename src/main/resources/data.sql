INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 1);

INSERT INTO poll (name, start_date, end_date, description)
VALUES ('pol1', now()-INTERVAL 1 DAY, now()+INTERVAL 1 DAY, 'first poll'),
       ('pol2', now()-INTERVAL 1 DAY, now()+INTERVAL 1 DAY, 'second poll');

INSERT INTO question(text,type,poll_id)
VALUES ('Poll1. What is your name?','TEXT',1),
       ('Poll2. What is your name?','TEXT',2);

INSERT INTO answer(user_id, text, type, poll_id, question_id)
VALUES (1,'answer 1 user 1','TEXT',1, 1);




