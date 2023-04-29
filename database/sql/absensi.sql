pragma foreign_keys = on;

create table configuration (
    key varchar(32) not null,
    value text not null,
    primary key (key)
);

create table user (
    username varchar(16) not null,
    password text not null,
    level int not null,
    primary key (username)
);

create table user_token (
    username varchar(16) not null,
    token text not null,
    creation datetime not null default (datetime('now', 'localtime')),
    primary key (username),
    foreign key (username) references user (username)
);

create table jenis_nomor (
    id varchar(8) not null,
    deskripsi text,
    primary key (id)
);

create table pegawai (
    nomor varchar(16) not null,
    id_jenis_nomor varchar(8) not null,
    nama varchar(128) not null,
    tempat_lahir varchar(64) not null,
    tanggal_lahir date not null,
    jenis_kelamin varchar(1) check(jenis_kelamin in ('L', 'P')) not null,
    primary key (nomor),
    foreign key (id_jenis_nomor) references jenis_nomor (id)
);