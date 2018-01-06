CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto-generated primary key',
  username varchar(200) NOT NULL UNIQUE COMMENT 'natural key; the user identifier',
  password varchar(200) NOT NULL COMMENT 'the users identifier; hash of the user password',
  PRIMARY KEY (`id`)
)
