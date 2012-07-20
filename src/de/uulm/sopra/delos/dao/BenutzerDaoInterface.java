package de.uulm.sopra.delos.dao;

import java.util.List;

import de.uulm.sopra.delos.bean.Benutzer;

public interface BenutzerDaoInterface {

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus und sortiert diese nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getAlleAlsListe(String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer auf der abgefragten Seite mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getAlleAlsListe(int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalte} und {@code wert} mit den
	 * Parametern uebereinstimmen und sortiert diese nach {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getAlleAlsListe(String spalte, String wert, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus, deren {@code spalte} und {@code wert} mit den Parametern uebereinstimmen, nach {@code sortierSpalte}
	 * sortiert und in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getAlleAlsListe(String spalte, String wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit den
	 * Parametern uebereinstimmen, nach {@code sortierSpalte} sortiert und in {@code sortiertRichtung} Reihenfolge. Seitenlaenge ist im Abfragekriterium
	 * definiert.
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getAlleAlsListe(String[] spalten, String[] werte, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus, deren {@code spalten} und {@code werte} mit den Parametern uebereinstimmen und sortiert diese nach
	 * {@code sortierSpalte} in {@code sortierRichtung} Reihenfolge.
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getAlleAlsListe(String[] spalten, String[] werte, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Benutzer als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit dem
	 * Parameter {@code term} uebereinstimmen, nach {@code sortierSpalte} sortiert und in {@code sortiertRichtung} Reihenfolge. Seitenlaenge ist im
	 * Abfragekriterium definiert. Es werden der Vorname, der Nachname und die E-Mail Adresse verodert durchsucht.
	 * 
	 * @param term
	 *            die Suchanfrage
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            die spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            die richtung in die sortiert werden soll
	 * @return eine Liste aller Benutzer auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<Benutzer> getSuchergebnisseAlsListe(String term, int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest den Benutzer aus der Datenbank als Benutzerobjekt aus, dessen ID uebergeben wurde
	 * 
	 * @param id
	 *            ID des Benutzers der abgefragt werden soll
	 * @return Benutzerobjekt, dass der angegeben ID entspricht
	 * @throws Exception
	 */
	public Benutzer getEinzeln(int id) throws Exception;

	/**
	 * Liest den ersten Benutzer aus der Datenbank als Benutzerobjekt aus, der mit den Parametern {@code spalte} und {@code wert} uebereinstimmt
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @return Benutzerobjekt, dass den Kriterien entspricht
	 * @throws Exception
	 */
	public Benutzer getEinzeln(String spalte, String wert) throws Exception;

	/**
	 * Liest den ersten Benutzer aus der Datenbank als Benutzerobjekt aus, der mit den Parametern {@code spalten} und {@code werte} uebereinstimmt
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @return Benutzerobjekt, dass den Kriterien entspricht
	 * @throws Exception
	 */
	public Benutzer getEinzeln(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl der vorhandenen Datensätze aus
	 * 
	 * @return anzahl der gefundenen Benutzer
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
	 * @return anzahl der gefundenen Benutzer
	 * @throws Exception
	 */
	public int getAnzahl(String spalte, String wert) throws Exception;

	/**
	 * Gibt die Anzahl auf Basis einer bestimmten SELECT Abfrage aus
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @return anzahl der gefundenen Benutzer
	 * @throws Exception
	 */
	public int getAnzahl(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl der gefundenen Benutzerobjekte zurück, die im Namen oder der E-Mail Adresse {@code term} enthalten.
	 * 
	 * @param term
	 *            die Suchanfrage
	 * @return Anzahl der Suchergebnisse
	 * @throws Exception
	 */
	public int getAnzahlSuchergebnisse(String term) throws Exception;

	/**
	 * Fuegt ein Benutzerobjekt als neuen Datensatz in die Datenbank ein
	 * 
	 * @param daten
	 *            das Benutzerobjekt, dass in die Datenbank eingefuegt werden soll
	 * @throws Exception
	 */
	public void erstellen(Benutzer daten) throws Exception;

	/**
	 * Aktualisiert einen bestehenden Benutzerdatensatz in der Datenbank
	 * 
	 * @param daten
	 *            das Benutzerobjekt, dass in der Datenbank aktualisiert werden soll
	 * @throws Exception
	 */
	public void aktualisieren(Benutzer daten) throws Exception;

	/**
	 * Loescht einen Benutzerdatensatz aus der Datenbank
	 * 
	 * @param daten
	 *            das Benutzerobjekt, dass aus der Datenbank geloescht werden soll
	 * @throws Exception
	 */
	public void loeschen(Benutzer daten) throws Exception;

	/**
	 * Entfernt die Vertreterrechte des angegeben Benutzers
	 * 
	 * @param daten
	 * @throws Exception
	 */
	public void alsVertreterAustragen(Benutzer daten) throws Exception;

}
