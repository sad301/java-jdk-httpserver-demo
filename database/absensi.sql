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
    id,
    deskripsi,
    primary key (id)
);

create table pegawai (
    nomor,
    id_jenis_nomor,
    nama,
    tempat_lahir,
    tanggal_lahir,
    jenis_kelamin,
    primary key (nomor)
);