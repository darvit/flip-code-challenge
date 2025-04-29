CREATE TABLE users(
    id           uuid primary key    not null,
    username     varchar(50)         not null
);

CREATE TABLE urls(
    id           uuid primary key    not null,
    url          varchar(2048)       not null,
    shortUrl     varchar(255)        not null,
    userId       uuid                not null,
    clicks       int                 not null
);