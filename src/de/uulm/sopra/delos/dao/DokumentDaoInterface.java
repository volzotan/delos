package de.uulm.sopra.delos.dao;

import java.util.List;

import de.uulm.sopra.delos.bean.Dokument;

/**
 * @author Johannes
 * 
 */
public interface DokumentDaoInterface {

	/**
	 * Liest alle Dokumente als Liste aus der Datebank aus und sortiert sie nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return Liste aller Dokumente mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Dokument> getAlleAlsListe(String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datebank aus, die auf {@code seite} zu finden sind und sortiert sie nach {@code sortierSpalte} in
	 * {@code sortierRichtung} Reihenfolge. Seitenlänge ist im Abfragekriterium definiert.
	 * 
	 * @param seite
	 *            abzufragende Seite
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return Liste aller Dokumente auf der abgefragten Seite mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Dokument> getAlleAlsListe(int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen und sortiert sie
	 * nach {@code sortierSpalte} in {@code sortierRichtung}.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return Liste aller Dokumente die mit den Bedingungen übereinstimmen mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Dokument> getAlleAlsListe(String spalte, String wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus die auf {@code seite} zu finden sind und deren {@code spalte} und {@code wert} mit den übergebenen
	 * Parametern übereinstimmt, sortiert nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlänge ist im Abfragekriterium definiert.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @param seite
	 *            abzufragende Seite
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return Liste aller Dokumente die auf der abgefragten Seite zu finden sind, die mit den Bedingungen übereinstimmen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Dokument> getAlleAlsListe(String spalte, String wert, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus deren {@code spalten} und {@code werte} mit den übergebenen Parametern übereinstimmen, sortiert nach
	 * {@code sortierSpalte} in {@code sortierRichtung}.
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return Liste aller Dokumente die mit den Bedingungen übereinstimmen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Dokument> getAlleAlsListe(String[] spalten, String[] werte, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus die auf {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit den übergebenen
	 * Parametern übereinstimmen, sortiert nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlänge ist im Abfragekriterium definiert.
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @param seite
	 *            abzufragende Seite
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in die Daten ausgegeben werden sollen
	 * @return Liste aller Dokumente die auf der abgefragten Seite zu finden sind, die mit den Bedingungen übereinstimmen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Dokument> getAlleAlsListe(String[] spalten, String[] werte, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus, deren {@code bewerbungsVorgangId} mit dem übergebenem Parameter übereinstimmt
	 * 
	 * @param bewerbungsVorgangId
	 *            abzufragende Id für die Abfragebedingung
	 * @return Liste aller Dokumente mit der abgefragten Id
	 * @throws Exception
	 */
	public List<Dokument> getNichtBenoetigteDokumenteAlsListe(int bewerbungsVorgangId) throws Exception;

	/**
	 * Liest das Dokument als DokumentObjekt aus der Datenbank aus, dessen {@code id} mit dem übergebenem Parameter übereinstimmt
	 * 
	 * @param id
	 *            abzufragende Id für die Abfragebedingung
	 * @return DokumentObjekt mit der abgefragten Id
	 * @throws Exception
	 */
	public Dokument getEinzeln(int id) throws Exception;

	/**
	 * Liest das Dokument als DokumentObjekt aus der Datenbank aus, dessen {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmt
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @return DokumentObjekt das den Bedingungen entspricht
	 * @throws Exception
	 */
	public Dokument getEinzeln(String spalte, String wert) throws Exception;

	/**
	 * Liest das Dokument als DokumentObjekt aus der Datenbank aus, dessen {@code spalten} und {@code werte} mit den übergebenen Parametern übereinstimmen
	 * 
	 * @param spalten
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param werte
	 *            abzufragende Werte für die Abfragebedingung
	 * @return Dokumentobjekt das den Bedingungen entspricht
	 * @throws Exception
	 */
	public Dokument getEinzeln(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl aller vorhandenen Dokumente in der Datenbank aus
	 * 
	 * @return Anzahl der gefundenen Datensätze
	 * @throws Exception
	 */
	public int getAnzahl() throws Exception;

	/**
	 * Gibt die Anzahl aller Dokumente aus, deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @return Anzahl der gefundenen Datensätze
	 * @throws Exception
	 */
	public int getAnzahl(String spalte, String wert) throws Exception;

	/**
	 * Gibt die Anzahl aller Dokumente aus, deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen
	 * 
	 * @param spalte
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param wert
	 *            abzufragende Werte für die Abfragebedingung
	 * @return Anzahl der gefundenen Datensätze
	 * @throws Exception
	 */
	public int getAnzahl(String[] spalte, String[] wert) throws Exception;

	/**
	 * Erstellt einen neuen Eintrag in der Datenbank mit dem übergebenen DokumentObjekt
	 * 
	 * @param daten
	 *            DokumentObjekt das die benötigten Daten enthält
	 * @throws Exception
	 */
	public void erstellen(Dokument daten) throws Exception;

	/**
	 * Aktualisiert einen vorhandenen Eintrag in der Datenbank mit dem übergebenen DokumentObjekt
	 * 
	 * @param daten
	 *            DokuemtnObjekt das die benötigten Daten enthält
	 * @throws Exception
	 */
	public void aktualisieren(Dokument daten) throws Exception;

	/**
	 * Löscht einen Datensatz aus der Datenbank der dem übergebenen DokumentObjekt entspricht
	 * 
	 * @param daten
	 *            DokumentObjekt das gelöscht werden soll
	 * @throws Exception
	 */
	public void loeschen(Dokument daten) throws Exception;
}
