/**
 * 
 */
package de.uulm.sopra.delos.dao;

import java.util.List;

import de.uulm.sopra.delos.bean.Ausschreibung;

public interface AusschreibungDaoInterface {

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und sortiert sie nach {@code sortierSpalte} in
	 * {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert wird
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen auf der abgefragten Seite mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getAlleAlsListe(int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datebank aus und sortiert sie nach {@code sortierSpale} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert wird
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getAlleAlsListe(String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalte} und {@code wert} mit den
	 * Parametern uebereinstimmen, nach {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert wird
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getAlleAlsListe(String spalte, String wert, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datenbank aus deren {@code spalte} und {@code wert} mit den uebergebenen Parametern uebereinstimmen, nach
	 * {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert auf den {@code spalte} zu ueberpruefen ist
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getAlleAlsListe(String spalte, String wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit
	 * den Parametern uebereinstimmen, nach {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalte
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param wert
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen auf der abgefragten Seite mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getAlleAlsListe(String[] spalte, String[] wert, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datenbank aus, deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen, nach
	 * {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalte
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param wert
	 *            abzufragende Werte für die Abfragebedingung
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getAlleAlsListe(String[] spalte, String[] wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Ausschreibungen als Liste aus der Datenbank aus die unter der {@code seite} zu finden sind, deren {@code institut} und {@code stunden} mit den
	 * übergebenen Parametern übereinstimmen und die {@code term} enthalten, nach {@code sortierSpalte} sortiert und in {@code sortierRichtung} Reihenfolge.
	 * Seitenlänge ist im Abfragekriterium definiert. Es werden Ausschreibername, sowie Titel und Beschreibung Oder-Verknüpft durchsucht und, sofern angebeben
	 * mit dem Institut und den Stunden verundet.
	 * 
	 * @param term
	 *            in der Ausschreibung enthaltener Text nach dem gesucht werden soll
	 * @param institut
	 *            Abzufragender Wert für die Spalte Institut
	 * @param stunden
	 *            Abzufragender Wert für die Spalte Stunden
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Ausschreibungen mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Ausschreibung> getSuchergebnisse(String term, int institut, int stunden, int seite, String sortierSpalte, String sortierRichtung)
			throws Exception;

	/**
	 * Liest die Ausschreibung aus der Datenbank als Ausschreibungsobjekt aus, dessen ID uebergeben wurde
	 * 
	 * @param id
	 *            ID der Ausschreibung die abgefragt werden soll
	 * @return Ausschreibungsobjekt, das der angegeben ID entspricht
	 * @throws Exception
	 */
	public Ausschreibung getEinzeln(int id) throws Exception;

	/**
	 * Liest die erste Ausschreibung aus der Datenbank als Ausschreibungsobjekt aus, die mit den Parametern {@code spalte} und {@code wert} uebereinstimmt
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @return Ausschreibungsobjekt, das den Kriterien entspricht
	 * @throws Exception
	 */
	public Ausschreibung getEinzeln(String spalte, String wert) throws Exception;

	/**
	 * Liest die erste Ausschreibung aus der Datenbank als Ausschreibungsobjekt aus, die mit den Parametern {@code spalten} und {@code werte} uebereinstimmt
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @return Ausschreibungsobjekt, das den Kriterien entspricht
	 * @throws Exception
	 */
	public Ausschreibung getEinzeln(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl der vorhandenen Datensätze aus
	 * 
	 * @return Anzahl der gefundenen Ausschreibungen
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
	 * @return anzahl der gefundenen Ausschreibungen
	 * @throws Exception
	 */
	public int getAnzahl(String spalte, String wert) throws Exception;

	/**
	 * Gibt die Anzahl auf Basis einer bestimmten SELECT Abfrage aus
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @return anzahl der gefundenen Ausschreibungen
	 * @throws Exception
	 */
	public int getAnzahl(String[] spalte, String[] wert) throws Exception;

	/**
	 * Gibt die Anzahl der Ergebnisse zurück, die Anhand der Suchanfrage {@code term}, {@code institut} und {@code stunden} gefunden wurden. Es werden
	 * Ausschreibername, sowie Titel und Beschreibung Oder-Verknüpft durchsucht und, sofern angebeben mit dem Institut und den Stunden verundet.
	 * 
	 * @param term
	 *            in der Ausschreibung enthaltener Text nach dem gesucht werden soll
	 * @param institut
	 *            Abzufragender Wert für die Spalte Institut
	 * @param stunden
	 *            Abzufragender Wert für die Spalte Stunden
	 * @return Die Anzahl der Suchergebnisse
	 * @throws Exception
	 */
	public int getAnzahlSuchergebnisse(String term, int institut, int stunden) throws Exception;

	/**
	 * Fuegt ein Ausschreibungsobjekt als neuen Datensatz in die Datenbank ein
	 * 
	 * @param daten
	 *            das Ausschreibungsobjekt, das in die Datenbank eingefuegt werden soll
	 * @throws Exception
	 */
	public void erstellen(Ausschreibung daten) throws Exception;

	/**
	 * Aktualisiert einen bestehenden Ausschreibungsdatensatz in der Datenbank
	 * 
	 * @param daten
	 *            das Ausschreibungsobjekt, das in der Datenbank aktualisiert werden soll
	 * @throws Exception
	 */
	public void aktualisieren(Ausschreibung daten) throws Exception;

	/**
	 * Loescht einen Ausschreibungsdatensatz aus der Datenbank
	 * 
	 * @param daten
	 *            der Ausschreibungsdatensatz, der aus der Datenbank geloescht werden soll
	 * @throws Exception
	 */
	public void loeschen(Ausschreibung daten) throws Exception;

}
