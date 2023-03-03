--drop table GENRE cascade;

--drop table MPA cascade;

--drop table FILMS cascade;

--drop table GENRE_REG cascade;

--drop table USERS cascade;

--drop table FRIENDS cascade;

--drop table LIKES cascade;

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE
(
    GENRE_ID INTEGER auto_increment
        primary key,
    NAME     CHARACTER VARYING(20)
);

CREATE TABLE IF NOT EXISTS PUBLIC.MPA
(
    MPA_ID      INTEGER auto_increment,
    NAME        CHARACTER VARYING(20)  not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS
(
    FILM_ID     BIGINT auto_increment
        primary key,
    NAME        CHARACTER VARYING(200),
    DESCRIPTION CHARACTER VARYING(200),
    RELEASEDATE DATE,
    DURATION    BIGINT,
    RATE      INTEGER,
    MPA_ID      INTEGER,
    constraint FILMS_FK_MPA
        foreign key (MPA_ID) references PUBLIC.MPA
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE_REG
(
    GENRE_REG_ID BIGINT auto_increment
        primary key,
    GENRE_ID     INTEGER,
    FILM_ID      BIGINT,
    constraint GENRE_REG_FK_FILMS
        foreign key (FILM_ID) references PUBLIC.FILMS,
    constraint GENRE_REG_FK_GENRE
        foreign key (GENRE_ID) references PUBLIC.GENRE
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    USER_ID  BIGINT auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(50),
    LOGIN    CHARACTER VARYING(50),
    NAME     CHARACTER VARYING(50),
    BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS
(
    FRIENDS_ID      BIGINT auto_increment
        primary key,
    FIRST_USER_ID   BIGINT,
    SECOND_USER_ID  BIGINT,
    constraint FRIENDS_FK_USER_FIRST
        foreign key (FIRST_USER_ID) references PUBLIC.USERS,
    constraint FRIENDS_FK_USER_SECOND
        foreign key (SECOND_USER_ID) references PUBLIC.USERS
);

CREATE TABLE IF NOT EXISTS PUBLIC.LIKES
(
    LIKES_ID BIGINT auto_increment,
    USER_ID  BIGINT not null,
    FILM_ID  BIGINT not null,
    constraint "LIKES_pk"
        primary key (LIKES_ID),
    constraint LIKES_FK_FILS
        foreign key (FILM_ID) references PUBLIC.FILMS,
    constraint LIKES_FK_USER
        foreign key (USER_ID) references PUBLIC.USERS
);

