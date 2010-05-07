CREATE DATABASE `killerbox` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `killerbox`;

CREATE TABLE IF NOT EXISTS `joueur` (
  `pseudo` varchar(20) NOT NULL,
  `administrateur` tinyint(1) NOT NULL DEFAULT '0',
  `hashConnexion` varchar(32) NOT NULL,
  `score` bigint(20) NOT NULL,
  PRIMARY KEY (`pseudo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE USER 'killerbox'@'localhost'IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON killerbox.* TO 'killerbox'@'localhost';