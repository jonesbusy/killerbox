SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

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

-- Pass 1234
INSERT INTO `joueur` (`pseudo`, `administrateur`, `hashConnexion`, `score`) VALUES
('killerbox', 1, 'ebafef48c44cbed390d90d9ff8c4840f', 0);

