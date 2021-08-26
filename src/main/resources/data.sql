INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),    --1
       ('Admin', 'admin@gmail.com', '{noop}admin'),     --2
       ('User2', 'user2@yandex.ru', '{noop}password');    --3

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3);

INSERT INTO restaurant (name, location)
VALUES ('Burger King','Moscow'), --1
       ('Pho Bo','Moscow'),      --2
       ('KFC','Moscow');         --3

INSERT INTO dish (name, price,restaurant_id)
VALUES ('burger 1','100', 1),
       ('burger 2','200', 1),
       ('burger 3','300', 1),
       ('pho bo 1','400', 2),
       ('pho bo 2','500', 2),
       ('pho bo 3','600', 2),
       ('chicken 1','700', 3),
       ('chicken 2','800', 3),
       ('chicken 3','900', 3);
       /*('burger 10','100'),
       ('burger 20','200'),
       ('burger 30','300'),
       ('pho bo 10','400'),
       ('pho bo 20','500'),
       ('pho bo 30','600'),
       ('chicken 10','700'),
       ('chicken 20','800'),
       ('chicken 30','900');*/

INSERT INTO menu (for_date, restaurant_id)
VALUES  (now()-INTERVAL 1 DAY, 1),
        (now()-INTERVAL 1 DAY, 2),
        (now()-INTERVAL 1 DAY, 3),
        (now(), 1),
        (now(), 2),
        (now(), 3);

INSERT INTO menu_dishes (menu_id, dishes_id)
VALUES  (1, 1), (1, 2), (1,3),
        (2, 4), (2, 5), (2,6),
        (3, 7), (3, 8), (3,9),
        (4, 1), (4, 2), (4,3),
        (5, 4), (5, 5), (5,6),
        (6, 7), (6, 8), (6,9);


/*INSERT INTO dish (name, price, for_date, restaurant_id)
VALUES ('burger 1','100',  now()-INTERVAL 1 DAY,1),
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
       ('chicken 3','900', now(),3);*/

INSERT INTO vote (user_id, restaurant_id, reg_date)
VALUES (1, 1, now()-INTERVAL 1 DAY),
       (2, 2, now()-INTERVAL 1 DAY),
       (1, 1, now()),
       (2, 2, now());