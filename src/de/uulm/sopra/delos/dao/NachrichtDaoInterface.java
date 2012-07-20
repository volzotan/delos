package de.uulm.sopra.delos.dao;

import java.util.List;

import de.uulm.sopra.delos.bean.Nachricht;

public interface NachrichtDaoInterface {

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus und sortiert sie nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Nachrichten mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Nachricht> getAlleAlsListe(String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus die unter {@code seite} zu finden sind und sortiert sie nach {@code sortierSpalte} in
	 * {@code soriterRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Nachrichten auf der gewünschten Seite mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Nachricht> getAlleAlsListe(int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus deren {@code spalte} und {@code wert} mit den uebergebenen Parametern uebereinstimmen und sortiert
	 * sie nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Nachrichten deren Parameter mit den übergebenen Werten uebereinstimmen mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Nachricht> getAlleAlsListe(String spalte, String wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus die unter {@code seite} zu finden sind und deren {@code spalte} und {@code wert} mit den
	 * übergebenen Parametern übereinstimmen Diese werden nach {@code sortierSpalte} in {@code sortierRichtung} sortiert. Seitenlänge ist im Abfragekriterium
	 * definiert
	 * 
	 * @param spalte
	 *            abzufragende Spalte für Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für Abfragebedingung
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen.
	 * @return eine Liste aller Nachrichten die auf der gewünschten Seite zu finden sind und deren Werte mit den übergebenen Parametern übereinstimmen mit
	 *         gewünschter Sortierung
	 * @throws Exception
	 */
	public List<Nachricht> getAlleAlsListe(String spalte, String wert, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus deren {@code spalte} und {@code wert} mit den übergebenen Parametern uebereinstimmen und sortiert
	 * sie nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlänge ist im Abfragekriterium definiert.
	 * 
	 * @param spalte
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param wert
	 *            abzufragende Werte für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen.
	 * @return eine Liste aller Nachrichten deren Werte mit den übergebenen Parametern übereinstimmen mit der gewünschten Sortierung.
	 * @throws Exception
	 */
	public List<Nachricht> getAlleAlsListe(String[] spalte, String[] wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus die auf {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit den
	 * übergebenen Parametern übereinstimmen und sortiert sie nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlänge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Nachrichten die auf der gewünschten Seite zu finden sind und deren Werte mit den übergebenen Parametern uebereinstimmen mit der
	 *         gewünschten Sortierung.
	 * @throws Exception
	 */
	public List<Nachricht> getAlleAlsListe(String[] spalten, String[] werte, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus deren Empfaenger oder Absender ID mit {@code benutzerId} übereinstimmt und sortiert sie nach
	 * {@code sortierSpalte} in {@code sortierRichtung}. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param benutzerId
	 *            abzufragende ID für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Nachrichten deren Absender/Empfänger mit dem übergebenen Wert übereinstimmen mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Nachricht> getPapierkorbAlsListe(int benutzerId, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Nachrichten als Liste aus der Datenbank aus die auf {@code seite} zu finden sind deren Absender/Empfänger ID mit {@code benutzerID}
	 * übereinstimmt und sortiert sie nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlänge ist im Abfragekriterium definiert.
	 * 
	 * @param benutzerId
	 *            abzufragende ID für die Abfragebedingung
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Nachrichten auf der gewünschten Seite deren Absender/Empfänger mit dem übergebenen Wert übereinstimmen mit der gewünschten
	 *         Sortierung
	 * @throws Exception
	 */
	public List<Nachricht> getPapierkorbAlsListe(final int benutzerId, final int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest die Nachricht als Nachrichtobjekt aus der Datebank aus deren ID mit {@code id} übereinstimmt.
	 * 
	 * @param id
	 *            abzufragende ID für die Abfragebedingung
	 * @return Nachrichtenobjekt das der übergebenen ID entspricht
	 * @throws Exception
	 */
	public Nachricht getEinzeln(int id) throws Exception;

	/**
	 * Liest die erste Nachricht als Nachrichtenobjekt aus der Datenbank aus deren {@code spalte} und {@code wert} den übergebenen Parametern entspricht.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @return Nachrichtenobjekt das den angegebenen Kriterin entspricht
	 * @throws Exception
	 */
	public Nachricht getEinzeln(String spalte, String wert) throws Exception;

	/**
	 * Liest die erste Nachricht als Nachrichtenobjekt aus der Datebank aus deren {@code spalten} und {@code werte} mit den übergebenen Parametern
	 * übereinstimmen.
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @return Nachrichtenobjekt das den angegebenen Kriterin entspricht
	 * @throws Exception
	 */
	public Nachricht getEinzeln(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl der in der Datenbank vorhandenen Datensätze aus
	 * 
	 * @return Anzahl der Nachrichten
	 * @throws Exception
	 */
	public int getAnzahl() throws Exception;

	/**
	 * Gibt die Anzahl der Nachrichten in der Datenbank aus deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @return Anzahl der gefundenen Nachrichten
	 * @throws Exception
	 */
	public int getAnzahl(String spalte, String wert) throws Exception;

	/**
	 * Gibt die Anzahl der Nachrichten in der Datenbank aus deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen.
	 * 
	 * @param spalte
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param wert
	 *            abzufragende Werte für die Abfragebedingung
	 * @return Anzahl der gefundenen Nachrichten
	 * @throws Exception
	 */
	public int getAnzahl(String[] spalte, String[] wert) throws Exception;

	/**
	 * Gibt die Anzahl der Nachrichten, die sich im Papierkorb befinden, in der Datenbank aus deren {@code benutzer ID} der übergebenen ID entspricht.
	 * 
	 * @param benutzerId
	 *            abzufragende ID für die Abfragebedingung
	 * @return Anzahl der gefundenen Nachrichten
	 * @throws Exception
	 */
	public int getPapierkorbAnzahl(final int benutzerId) throws Exception;

	/**
	 * Erstellen eine neue Nachricht in der Datenbank mit der übergebenen {@code nachricht}
	 * 
	 * @param nachricht
	 *            Nachrichtenobjekt dass in die Datebank übertragen werden soll
	 * @throws Exception
	 */
	public void erstellen(final Nachricht nachricht) throws Exception;

	/**
	 * Aktualisiert einen vorhandenen Nachrichtendatensatz in der Datebank
	 * 
	 * @param id
	 *            ID der Nachricht die aktualisiert werden soll
	 * @param status
	 *            neuer Status der aktualisiert werden soll
	 * @throws Exception
	 */
	public void aktualisiereStatus(final int id, int status) throws Exception;

	/**
	 * Aktualisiert einen vorhandenen Nachrichtendatensatz
	 * 
	 * @param nachricht
	 *            Nachrichtenobjekt womit der alte Datensatz überschrieben werden soll
	 * @param absender
	 *            Boolean der angibt ob fuer welchen Benutzer der Löschstatus aktualisiert werden soll
	 * @throws Exception
	 */
	public void aktualisiereLoeschStatus(final Nachricht nachricht, final boolean absender) throws Exception;

	/**
	 * Stellt eine Nachricht aus dem Papierkorb wieder her
	 * 
	 * @param nachricht
	 *            Nachrichtenobjekt das wieder hergestellt werden soll
	 * @param absender
	 *            Boolean der angibt für welchen Benutzer die Nachricht wieder hergestellt werden soll
	 * @throws Exception
	 */
	public void wiederherstellen(final Nachricht nachricht, final boolean absender) throws Exception;

	/**
	 * Löscht eine Nachricht (verschiebt Nachricht in Papierkorb oder löscht sie aus der Datenbank abhängig vom Löschstatus)
	 * 
	 * @param nachricht
	 *            Nachrichtenobjekt dass geloescht werden soll
	 * @throws Exception
	 */
	public void loeschen(final Nachricht nachricht) throws Exception;

}
