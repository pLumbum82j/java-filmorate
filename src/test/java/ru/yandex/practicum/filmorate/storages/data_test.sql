insert into GENRE(GENRE_ID, NAME)
values  (1, 'Комедия'),
        (2, 'Драма'),
        (3, 'Мультфильм'),
        (4, 'Триллер'),
        (5, 'Документальный'),
        (6, 'Боевик');
insert into MPA(MPA_ID, NAME)
values  (1, 'G'),
        (2, 'PG'),
        (3, 'PG-13'),
        (4, 'R'),
        (5, 'NC-17');

INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES  ('doritos@gmail.com', 'dorito', 'Mark', DATE '2021-12-12'),
        ('pizza@dodo,com','chiken', 'Theodore', DATE '2020-12-12'),
        ('zavtra@net.net','nofuture', 'Dead', DATE '2019-12-12');

INSERT INTO FRIENDS (FIRST_USER_ID, SECOND_USER_ID)
VALUES (1,2),
       (1,3),
       (2,3);

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_ID, LIKE_AMOUT)
VALUES  ('Titanic', 'Test description',  DATE '1997-1-27', 90, 1,0),
        ('Titanic2', 'Test description2',  DATE '1997-1-27', 90, 1,0),
        ('Titanic23', 'Test description23',  DATE '1997-1-27', 90, 1,0);

INSERT INTO LIKES (USER_ID, FILM_ID)
VALUES (2,1),
       (3,1),
       (3,2);