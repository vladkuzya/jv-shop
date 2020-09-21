CREATE SCHEMA `jv-shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `jv-shop`.`products` (
  `product_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(225) NOT NULL,
  `price` DOUBLE NOT NULL,
  'deleted' boolean NOT NULL DEFAULT false,
  PRIMARY KEY (`product_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);
