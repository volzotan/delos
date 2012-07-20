-- Create syntax for TABLE 'delos_Ausschreibung'
CREATE TABLE `delos_Ausschreibung` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ausschreiber_id` int(11) unsigned NOT NULL,
  `bearbeiter_id` int(11) unsigned NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT '',
  `beschreibung` text NOT NULL,
  `institut` int(11) unsigned NOT NULL,
  `stundenzahl` int(11) unsigned NOT NULL,
  `stellenzahl` int(11) unsigned NOT NULL,
  `voraussetzungen` text,
  `startet` date NOT NULL,
  `endet` date NOT NULL,
  `bewerbungsfrist` date NOT NULL,
  `status` int(11) unsigned NOT NULL DEFAULT '0',
  `hinzugefuegt` datetime NOT NULL,
  `bearbeitet` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ausschreiber_id` (`ausschreiber_id`),
  KEY `bearbeiter_id` (`bearbeiter_id`),
  CONSTRAINT `delos_Ausschreibung_ibfk_1` FOREIGN KEY (`ausschreiber_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `delos_Ausschreibung_ibfk_2` FOREIGN KEY (`bearbeiter_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'delos_Benutzer'
CREATE TABLE `delos_Benutzer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `gruppe_id` int(11) unsigned NOT NULL,
  `vertreter_id` int(11) unsigned NOT NULL DEFAULT '0',
  `institut` int(11) unsigned NOT NULL,
  `email` varchar(128) NOT NULL DEFAULT '',
  `nachname` varchar(128) NOT NULL DEFAULT '',
  `vorname` varchar(128) NOT NULL DEFAULT '',
  `passwort` varchar(128) NOT NULL DEFAULT '',
  `telefon` varchar(128) NOT NULL DEFAULT '',
  `raum` varchar(128) DEFAULT NULL,
  `geburtstag` date DEFAULT NULL,
  `geschlecht` tinyint(1) unsigned NOT NULL,
  `matrikelnummer` int(10) unsigned NOT NULL DEFAULT '0',
  `studiengang` int(11) unsigned NOT NULL DEFAULT '0',
  `nationalitaet` int(11) unsigned NOT NULL,
  `strasse` varchar(128) NOT NULL DEFAULT '',
  `hausnummer` varchar(6) NOT NULL DEFAULT '',
  `postleitzahl` varchar(11) NOT NULL DEFAULT '',
  `stadt` varchar(128) NOT NULL DEFAULT '',
  `land` int(11) unsigned NOT NULL,
  `strasseHeimat` varchar(128) DEFAULT NULL,
  `hausnummerHeimat` varchar(6) DEFAULT NULL,
  `postleitzahlHeimat` varchar(11) DEFAULT NULL,
  `stadtHeimat` varchar(128) DEFAULT NULL,
  `landHeimat` int(11) unsigned DEFAULT NULL,
  `hinzugefuegt` datetime NOT NULL,
  `bearbeitet` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Standardbenutzer notwendig für ein korrektes Verhalten des Systems
INSERT INTO `delos_Benutzer` (`id`, `gruppe_id`, `vertreter_id`, `institut`, `email`, `nachname`, `vorname`, `passwort`, `telefon`, `raum`, `geburtstag`, `geschlecht`, `matrikelnummer`, `nationalitaet`, `strasse`, `hausnummer`, `postleitzahl`, `stadt`, `land`, `strasseHeimat`, `hausnummerHeimat`, `postleitzahlHeimat`, `stadtHeimat`, `landHeimat`, `hinzugefuegt`, `bearbeitet`)
VALUES
        (0, 5, 0, 2, 'sys@sys', '', 'System', '-', '', NULL, '1970-01-01', 1, 0, 0, '', '', '', '', 0, '', '', '', '', 0, NOW(), NOW());
-- Standardadmin mit PW 123456 für Systemverwaltung direkt nach der Installation
INSERT INTO `delos_Benutzer` (`id`, `gruppe_id`, `vertreter_id`, `institut`, `email`, `nachname`, `vorname`, `passwort`, `telefon`, `raum`, `geburtstag`, `geschlecht`, `matrikelnummer`, `nationalitaet`, `strasse`, `hausnummer`, `postleitzahl`, `stadt`, `land`, `strasseHeimat`, `hausnummerHeimat`, `postleitzahlHeimat`, `stadtHeimat`, `landHeimat`, `hinzugefuegt`, `bearbeitet`)
VALUES
        (1, 1, 0, 1, 'admin@uni-ulm.de', 'Uni', 'Admin', '$2a$10$RkxDuKjadlgEkxquPuVUL.24bW3vVTsb1bAz1r25lrCtsZJk9bPUa', '', NULL, '1970-01-01', 1, 0, 0, '', '', '', '', 0, '', '', '', '', 0, NOW(), NOW());

-- Create syntax for TABLE 'delos_BewerbungsVorgang'
CREATE TABLE `delos_BewerbungsVorgang` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bewerber_id` int(11) unsigned NOT NULL,
  `bearbeiter_id` int(11) unsigned DEFAULT NULL,
  `ausschreiber_id` int(11) unsigned NOT NULL,
  `ausschreibung_id` int(11) unsigned NOT NULL,
  `institut` int(11) unsigned NOT NULL,
  `stundenzahl` int(11) unsigned NOT NULL,
  `startet` date NOT NULL,
  `endet` date NOT NULL,
  `status` int(11) NOT NULL,
  `kommentar` text,
  `hinzugefuegt` datetime NOT NULL,
  `bearbeitet` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `bewerber_id` (`bewerber_id`),
  KEY `ausschreiber_id` (`ausschreiber_id`),
  KEY `bearbeiter_id` (`bearbeiter_id`),
  KEY `ausschreibung_id` (`ausschreibung_id`),
  CONSTRAINT `delos_BewerbungsVorgang_ibfk_1` FOREIGN KEY (`ausschreibung_id`) REFERENCES `delos_Ausschreibung` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `delos_BewerbungsVorgang_ibfk_2` FOREIGN KEY (`bewerber_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `delos_BewerbungsVorgang_ibfk_3` FOREIGN KEY (`bearbeiter_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `delos_BewerbungsVorgang_ibfk_4` FOREIGN KEY (`ausschreiber_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'delos_BewerbungsVorgangDokument'
CREATE TABLE `delos_BewerbungsVorgangDokument` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dokument_id` int(11) unsigned NOT NULL,
  `bewerbungs_vorgang_id` int(11) unsigned NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `bewerbungs_vorgang_id` (`bewerbungs_vorgang_id`),
  KEY `dokument_id` (`dokument_id`),
  CONSTRAINT `delos_BewerbungsVorgangDokument_ibfk_1` FOREIGN KEY (`bewerbungs_vorgang_id`) REFERENCES `delos_BewerbungsVorgang` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `delos_BewerbungsVorgangDokument_ibfk_2` FOREIGN KEY (`dokument_id`) REFERENCES `delos_Dokument` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'delos_Dokument'
CREATE TABLE `delos_Dokument` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `beschreibung` text,
  `standard` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'delos_Nachricht'
CREATE TABLE `delos_Nachricht` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `absender_id` int(11) unsigned NOT NULL,
  `empfaenger_id` int(11) unsigned NOT NULL,
  `titel` varchar(128) NOT NULL DEFAULT '',
  `beschreibung` text NOT NULL,
  `status` tinyint(11) NOT NULL DEFAULT '3',
  `absender_loeschStatus` tinyint(11) NOT NULL DEFAULT '2',
  `empfaenger_loeschStatus` tinyint(11) NOT NULL DEFAULT '2',
  `versanddatum` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `absender_id` (`absender_id`),
  KEY `empfaenger_id` (`empfaenger_id`),
  CONSTRAINT `delos_Nachricht_ibfk_1` FOREIGN KEY (`absender_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `delos_Nachricht_ibfk_2` FOREIGN KEY (`empfaenger_id`) REFERENCES `delos_Benutzer` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;