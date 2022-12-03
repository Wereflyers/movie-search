create table IF NOT EXISTS FILM_GENRE
(
    GENRE_ID INTEGER auto_increment,
    GENRE    CHARACTER VARYING,
    constraint "FILM_GENRE_pk"
        primary key (GENRE_ID)
);

create table IF NOT EXISTS RATING_MPA
(
    RATING_ID INTEGER auto_increment,
    RATING    CHARACTER VARYING,
    constraint "RATING_MPA_pk"
        primary key (RATING_ID)
);

create table IF NOT EXISTS FILMORATE_USER
(
    USER_ID   INTEGER auto_increment,
    USER_NAME CHARACTER VARYING,
    LOGIN     CHARACTER VARYING not null
        unique,
    EMAIL     CHARACTER VARYING not null,
    BIRTHDAY  DATE,
    constraint "FILMORATE_USER_pk"
        primary key (USER_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment
        primary key,
    GENRE_ID     INTEGER,
    FILM_NAME    CHARACTER VARYING not null,
    DESCRIPTION  CHARACTER VARYING(200),
    DURATION     INTEGER,
    RATING_ID    INTEGER,
    RELEASE_DATE DATE,
    constraint FILMS_FILM_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references FILM_GENRE,
    constraint FILMS_RATING_MPA_RATING_ID_FK
        foreign key (RATING_ID) references RATING_MPA
);

create table FRIENDS_LIST
(
    USER_ID           INTEGER,
    FRIEND_ID         INTEGER not null,
    FRIENDSHIP_STATUS BOOLEAN default FALSE,
    constraint FRIENDS_LIST_FILMORATE_USER_FRIEND_ID_FK
        foreign key (FRIEND_ID) references FILMORATE_USER,
    constraint "FRIENDS_LIST_FILMORATE_USER_null_fk"
        foreign key (USER_ID) references FILMORATE_USER
            on update cascade on delete cascade
);

create table IF NOT EXISTS LIKES
(
    USER_ID INTEGER,
    FILM_ID INTEGER,
    constraint "LIKES_FILMORATE_USER_null_fk"
        foreign key (USER_ID) references FILMORATE_USER,
    constraint "LIKES_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS
);


