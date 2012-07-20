/**
 * 
 */
package de.uulm.sopra.delos.system;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * Datenbank singleton für persistente Datenhaltung und Zugriff
 * 
 */
public class Datenbank {

	// Die Datenbankverbindung
	public Connection			con		= null;

	Logger						log		= Logger.getLogger(this.getClass().getName());

	private final String		url;													// localhost:3306/Sopra
	final static private String	DRIVER	= "com.mysql.jdbc.Driver";
	private final String		user;													// root
	private final String		password;												// root

	/**
	 * Default-Konstruktor, der nicht außerhalb dieser Klasse aufgerufen werden kann
	 * 
	 * @throws IOException
	 */
	private Datenbank() {
		Properties props = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("delosconfig.properties"); // per Classloader wegen Pfad
		try {
			props.load(is);
		} catch (IOException e) {
			log.fatal("Datenbank-Konfiguration laden fehlgeschlagen", e);
		}

		url = props.getProperty("datenbank.url");
		user = props.getProperty("datenbank.benutzer");
		password = props.getProperty("datenbank.passwort");

		open();
	}

	/**
	 * Öffnet die Datenbankverbindung
	 */
	private void open() {
		if (con == null) {
			try {
				// Treiber laden.
				Class.forName(DRIVER);
			} catch (ClassNotFoundException e) {
				log.fatal("Datenbank: Treiberklasse nicht gefunden", e);
			}

			try {
				// Verbindung Aufbauen
				con = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				log.fatal("Datenbank: Öffnen der Verbindung fehlgeschlagen", e);
			}
		}
	}

	/**
	 * Schließt die Datenbankverbindung
	 */
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			log.error("Datenbank: Schließen der Verbindung fehlgeschlagen", e);
		}
	}

	/**
	 * Statische Methode, liefert die einzige Instanz dieser Klasse zurück
	 */
	public static synchronized Datenbank getInstance() {
		return new Datenbank();

	}
}
