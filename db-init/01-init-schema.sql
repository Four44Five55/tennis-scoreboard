
create table players
(
    id   integer generated always as identity
        constraint players_pk
            primary key,
    name varchar not null
        constraint players_pk_2
            unique
);

alter table players
    owner to tennis_user;

create table matches
(
    id      integer generated always as identity
        constraint matches_pk
            primary key,
    player1 integer not null
        constraint matches_players_id_fk
            references players,
    player2 integer not null
        constraint matches_players_id_fk_2
            references players,
    winner  integer
        constraint matches_players_id_fk_3
            references players
);

alter table matches
    owner to tennis_user;

