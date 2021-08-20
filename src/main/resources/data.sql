INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),    --1
       ('Admin', 'admin@gmail.com', '{noop}admin');     --2

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (name, location)
VALUES ('Burger King','Moscow'),
       ('Pho Bo','Moscow'),
       ('KFC','Moscow');

INSERT INTO dish (name, price, for_date, restaurant_id)
VALUES ('burger 1','100',  now()-INTERVAL 1 DAY ,1),
       ('burger 2','200',  now()-INTERVAL 1 DAY,1),
       ('burger 3','300',  now()-INTERVAL 1 DAY,1),
       ('pho bo 1','400',  now()-INTERVAL 1 DAY,2),
       ('pho bo 2','500',  now()-INTERVAL 1 DAY,2),
       ('pho bo 3','600',  now()-INTERVAL 1 DAY,2),
       ('chicken 1','700', now()-INTERVAL 1 DAY,3),
       ('chicken 2','800', now()-INTERVAL 1 DAY,3),
       ('chicken 3','900', now()-INTERVAL 1 DAY,3),
       ('burger 1','100',  now(),1),
       ('burger 2','200',  now(),1),
       ('burger 3','300',  now(),1),
       ('pho bo 1','400',  now(),2),
       ('pho bo 2','500',  now(),2),
       ('pho bo 3','600',  now(),2),
       ('chicken 1','700', now(),3),
       ('chicken 2','800', now(),3),
       ('chicken 3','900', now(),3);

INSERT INTO vote (user_id, restaurant_id, reg_date)
VALUES (1, 1, now()-INTERVAL 1 DAY),
       (2, 2, now()-INTERVAL 1 DAY),
       (1, 1, now()),
       (2, 2, now());