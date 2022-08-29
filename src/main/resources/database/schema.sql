CREATE DATABASE familyDB;

CREATE TABLE member(
                       id bigint not null auto_increment primary key,
                       name varchar(255),
                       family_name varchar(255),
                       birthday date,
                       died_on date,
                       father_id bigint,
                       mother_id bigint,
                       partner_id bigint,
                       user_id bigint,
                       gender int
);

CREATE TABLE family(
                       id bigint not null auto_increment primary key,
                       family_name varchar(255),
                       user_id bigint
);

CREATE TABLE family_members(
                               family_id bigint not null,
                               members_id bigint not null
);

CREATE TABLE user(
                     id bigint not null auto_increment primary key,
                     username varchar(255),
                     password varchar(255),
                     my_family_nr bigint,
                     doihave_family bit(1)
);