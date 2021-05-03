ALTER DATABASE chess DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS pieces
(
    room_id    bigint,
    piece_name char(1),
    position   char(2)
);
CREATE TABLE IF NOT EXISTS room
(
    id           bigint NOT NULL AUTO_INCREMENT,
    title        varchar(64) DEFAULT NULL UNIQUE,
    turn         char(5)     DEFAULT NULL,
    playing_flag tinyint(1) DEFAULT NULL,
    PRIMARY KEY (id)
);

