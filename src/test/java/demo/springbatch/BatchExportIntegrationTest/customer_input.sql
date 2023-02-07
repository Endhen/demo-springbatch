
DROP TABLE IF EXISTS `customer`; 
CREATE TABLE IF NOT EXISTS `customer` (
    `id` bigint NOT NULL,
    `first_name` varchar(255) DEFAULT NULL,
    `last_name` varchar(255) DEFAULT NULL,
    `birthday` varchar(255) DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `contact_no` varchar(255) DEFAULT NULL,
    `country` varchar(255) DEFAULT NULL,
    `gender` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);