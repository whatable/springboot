DROP TABLE IF EXISTS roles_permissions;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS hibernate_sequence;

CREATE SEQUENCE hibernate_sequence START WITH 10000 INCREMENT by 1;

CREATE TABLE IF NOT EXISTS roles_permissions(
	id bigint NOT NULL AUTO_INCREMENT,
	permission varchar(40) NOT NULL,
	role_id bigint NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS user_roles (
	id bigint NOT NULL AUTO_INCREMENT,
	role_name varchar(128) DEFAULT NULL,
	user_id bigint NOT NULL,
	PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS users(
	id bigint NOT NULL AUTO_INCREMENT,
	username varchar(40) UNIQUE NOT NULL,
	password varchar(128) NOT NULL,
	password_salt varchar(100) DEFAULT NULL,
	PRIMARY KEY (id)
);