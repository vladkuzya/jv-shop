CREATE SCHEMA `jv-shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `jv-shop`.`products` (
  `product_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(225) NOT NULL,
  `price` DOUBLE NOT NULL,
  'deleted' boolean NOT NULL DEFAULT false,
  PRIMARY KEY (`product_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);

CREATE TABLE `jv-shop`.`roles` (
  `role_id` BIGINT(2) NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`role_id`));

CREATE TABLE `jv-shop`.`users_roles` (
  `user_id` BIGINT(11) NOT NULL,
  `role_id` BIGINT(11) NOT NULL);

ALTER TABLE `jv-shop`.`users_roles`
ADD INDEX `users_fk_idx` (`user_id` ASC) VISIBLE,
ADD INDEX `roles_fk_idx` (`role_id` ASC) VISIBLE;
;
ALTER TABLE `jv-shop`.`users_roles`
ADD CONSTRAINT `users_fk`
  FOREIGN KEY (`user_id`)
  REFERENCES `jv-shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `roles_fk`
  FOREIGN KEY (`role_id`)
  REFERENCES `jv-shop`.`roles` (`role_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


CREATE TABLE `jv-shop`.`users` (
  `user_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(256) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`user_id`));

ALTER TABLE `jv-shop`.`users`
    ADD COLUMN `deleted` TINYINT NOT NULL DEFAULT 0 AFTER `password`;

CREATE TABLE `jv-shop`.`shopping_carts` (
  `cart_id` BIGINT(11) NOT NULL,
  `user_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`cart_id`));

ALTER TABLE `jv-shop`.`shopping_carts`
ADD INDEX `shopping_carts_users_fk_idx` (`user_id` ASC) VISIBLE;
;
ALTER TABLE `jv-shop`.`shopping_carts`
ADD CONSTRAINT `shopping_carts_users_fk`
  FOREIGN KEY (`user_id`)
  REFERENCES `jv-shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `jv-shop`.`shopping_carts`
    ADD COLUMN `deleted` TINYINT NOT NULL DEFAULT 0 AFTER `user_id`;

CREATE TABLE `jv-shop`.`orders` (
  `order_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `orders_users_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `orders_users_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `jv-shop`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

ALTER TABLE `jv-shop`.`orders`
    ADD COLUMN `deleted` TINYINT NOT NULL DEFAULT 0 AFTER `user_id`;


CREATE TABLE `jv-shop`.`shopping_carts_products` (
  `cart_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
  INDEX `carts_fk_idx` (`cart_id` ASC) VISIBLE,
  INDEX `products_fk_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `carts_fk`
    FOREIGN KEY (`cart_id`)
    REFERENCES `jv-shop`.`shopping_carts` (`cart_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `products_fk`
    FOREIGN KEY (`product_id`)
    REFERENCES `jv-shop`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `jv-shop`.`orders_products` (
  `order_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
  INDEX `orders_fk_idx` (`order_id` ASC) VISIBLE,
  INDEX `products_fk_idx` (`product_id` ASC) VISIBLE,
  CONSTRAINT `orders_fk`
    FOREIGN KEY (`order_id`)
    REFERENCES `jv-shop`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `order_products_fk`
    FOREIGN KEY (`product_id`)
    REFERENCES `jv-shop`.`products` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

