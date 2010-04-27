-- phpMyAdmin SQL Dump
-- version 3.2.5
-- http://www.phpmyadmin.net
--
-- Serveur: 127.0.0.1
-- Généré le : Mar 27 Avril 2010 à 14:28
-- Version du serveur: 5.1.43
-- Version de PHP: 5.3.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `killerbox`
--
CREATE DATABASE `killerbox` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `killerbox`;

-- --------------------------------------------------------

--
-- Structure de la table `joueur`
--

CREATE TABLE IF NOT EXISTS `joueur` (
  `pseudo` varchar(20) NOT NULL,
  `administrateur` tinyint(1) NOT NULL DEFAULT '0',
  `hashConnexion` varchar(32) NOT NULL,
  `score` bigint(20) NOT NULL,
  PRIMARY KEY (`pseudo`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `joueur`
--

INSERT INTO `joueur` (`pseudo`, `administrateur`, `hashConnexion`, `score`) VALUES
('THE_PRO', 0, '7c746ea42d70cf02742495780ca02146', 0),
('Test', 0, '8c1cea090cc1c1d33e5efe86482c84ad', 0),
('KING', 0, '39b2e135a3a84c8da50d2a262bee4404', 0),
('THE_BEST', 0, 'be75d87afca55f2c1ee88087e3d8c7fc', 0);
