package de.uulm.sopra.delos.dao;

import java.io.File;
import java.util.List;

import de.uulm.sopra.delos.bean.BewerbungsVorgang;

public interface BewerbungsVorgangDaoInterface {

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind, nach {@code sortierSpalte} sortiert und in
	 * {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge auf der abgefragten Seite und mit der gewuenschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListe(int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datenbank aus, nach {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge mit der gewuenschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListe(String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalte} und {@code wert} mit
	 * den Parametern uebereinstimmen, nach {@code sortierSpalte} sortiert in {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListe(String spalte, String wert, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datenbank aus, deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen, nach
	 * {@code sortierSpalte} sortiert und in {@code soriterRichtung} Reihenfolge.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListe(String spalte, String wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit
	 * den Parametern uebereinstimmen, nach {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge auf der abgefragten Seite mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListe(String[] spalten, String[] werte, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datebank aus deren {@code spalten} und {@code werte} mit den übergebenen Parametern übereinstimmen, nach
	 * {@code sortierSpalte} sortiert in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListe(String[] spalten, String[] werte, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datebank aus die auf {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit den
	 * übergebenen Parametern übereinstimmen, die nicht neu sind, und sortiert diese nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * Seitenlänge ist im Abfragekriterium definiert.
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
	 * @return eine Liste aller Bewerbungsvorgänge auf der abgefragten Seite, mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListeNichtNeu(String[] spalten, String[] werte, int seite, String sortierSpalte, String sortierRichtung)
			throws Exception;

	/**
	 * Liest alle Bewerbungsvorgänge als Liste aus der Datebank aus deren {@code spalten} und {@code werte} mit den übergebenen Parametern übereinstimmen, die
	 * nicht neu sind, und sortiert diese nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Bewerbungsvorgänge mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgang> getAlleAlsListeNichtNeu(String[] spalten, String[] werte, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest den Bewerbungsvorgänge aus der Datenbank als Bewerbungsvorgangobjekt aus, dessen ID uebergeben wurde
	 * 
	 * @param id
	 *            ID des Bewerbungsvorgangs der abgefragt werden soll
	 * @return Bewerbungsvorgangobjekt, das der angegeben ID entspricht
	 * @throws Exception
	 */
	public BewerbungsVorgang getEinzeln(int id) throws Exception;

	/**
	 * Liest den ersten Bewerbungsvorgang aus der Datenbank als Bewerbungsvorgangobjekt aus, das mit den Parametern {@code spalte} und {@code wert}
	 * uebereinstimmt
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @return Bewerbungsvorgangobjekt, das den Kriterien entspricht
	 * @throws Exception
	 */
	public BewerbungsVorgang getEinzeln(String spalte, String wert) throws Exception;

	/**
	 * Liest den ersten Bewerbungsvorgang aus der Datenbank als Bewerbungsvorgangobjekt aus, das mit den Parametern {@code spalten} und {@code werte}
	 * uebereinstimmt
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @return Bewerbungsvorgangobjekt, das den Kriterien entspricht
	 * @throws Exception
	 */
	public BewerbungsVorgang getEinzeln(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl der vorhandenen Datensätze aus
	 * 
	 * @return anzahl der gefundenen Bewerbungsvorgänge
	 * @throws Exception
	 */
	public int getAnzahl() throws Exception;

	/**
	 * Gibt die Anzahl auf Basis einer bestimmten SELECT Abfrage aus
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @return anzahl der gefundenen Bewerbungsvorgaenge
	 * @throws Exception
	 */
	public int getAnzahl(String spalte, String wert) throws Exception;

	/**
	 * Gibt die Anzahl auf Basis einer bestimmten SELECT Abfrage aus
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragende werte auf den die {@code spalten} zu ueberpruefen ist
	 * @return anzahl der gefundenen Bewerbungsvorgaenge
	 * @throws Exception
	 */
	public int getAnzahl(String[] spalten, String[] werte) throws Exception;

	/**
	 * Liest die Anzahl der Bewerbungsvorgänge aus der Datebank aus deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen, die
	 * nicht neu sind.
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalten} zu ueberpruefen ist
	 * @return anzahl der gefundenen Bewerbungsvorgaenge
	 * @throws Exception
	 * @throws Exception
	 */
	public int getAnzahlNichtNeu(String spalte, String wert) throws Exception;

	/**
	 * Liest die Anzahl der Bewerbungsvorgänge aus der Datebank aus deren {@code spalten} und {@code werte} mit den übergebenen Parametern übereinstimmen, die
	 * nicht neu sind.
	 * 
	 * @param spalten
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param werte
	 *            abzufragender wert auf den die {@code spalten} zu ueberpruefen ist
	 * @return anzahl der gefundenen Bewerbungsvorgaenge
	 * @throws Exception
	 */
	public int getAnzahlNichtNeu(String[] spalten, String[] werte) throws Exception;

	/**
	 * Fuegt ein Bewerbungsvorgangobjekt als neuen Datensatz in die Datenbank ein
	 * 
	 * @param daten
	 *            das Bewerbungsvorgangobjekt, dass in die Datenbank eingefuegt werden soll
	 * @throws Exception
	 */
	public void erstellen(BewerbungsVorgang daten) throws Exception;

	/**
	 * Aktualisiert einen bestehenden Bewerbungsvorgangdatensatz in der Datenbank
	 * 
	 * @param daten
	 *            das Bewerbungsvorgangobjekt, dass in der Datenbank aktualisiert werden soll
	 * @throws Exception
	 */
	public void aktualisieren(BewerbungsVorgang daten) throws Exception;

	/**
	 * Loescht einen Bewerbungsvorgangdatensatz aus der Datenbank
	 * 
	 * @param daten
	 *            das Bewerbungsvorgangobjekt, dass aus der Datenbank geloescht werden soll
	 * @throws Exception
	 */
	public void loeschen(BewerbungsVorgang daten) throws Exception;

	/**
	 * Exportiert alle abgeschlossenen Bewerbungsvorgänge eines Bearbeiters als CSV Datei
	 * 
	 * @param bearbeiterId
	 * @return CSV Dump der abgeschlossenen Bewerbungsvorgänge
	 * @throws Exception
	 */
	public File export(final int bearbeiterId) throws Exception;

}
