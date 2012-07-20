package de.uulm.sopra.delos.dao;

import java.util.List;

import de.uulm.sopra.delos.bean.BewerbungsVorgangDokument;

public interface BewerbungsVorgangDokumentDaoInterface {

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und sortiert diese nach {@code sortierSpalte} in
	 * {@code sortierRichtung}. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Dokumente auf der abgefragten Seite mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgangDokument> getAlleAlsListe(int seite, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus und sortiert diese nach {@code sortierSpalte} in {@code sortierRichtung}.
	 * 
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Dokumente mit der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgangDokument> getAlleAlsListe(String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalte} und {@code wert} mit den
	 * Parametern uebereinstimmen und sortiert diese nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Dokumente auf der abgefragten Seite mit den ensprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgangDokument> getAlleAlsListe(String spalte, String wert, int seite, String sortierSpalte, String sortierRichtung)
			throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus deren {@code spalte} und {@code wert} mit den übergebenen Parametern übereinstimmen und sortiert
	 * diese nach {@code sortierSpalte} in {@code sortierRichtung}.
	 * 
	 * @param spalte
	 *            abzufragende Spalte für die Abfragebedingung
	 * @param wert
	 *            abzufragender Wert für die Abfragebedingung
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Dokumente mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgangDokument> getAlleAlsListe(String spalte, String wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus, die unter der {@code seite} zu finden sind und deren {@code spalten} und {@code werte} mit den
	 * Parametern uebereinstimmen und sortiert sie nach {@code sortierSpalte} in {@code sortierRichtung}. Seitenlaenge ist im Abfragekriterium definiert.
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @param seite
	 *            die aufzurufende Seite
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Dokumente auf der abgefragten Seite mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgangDokument> getAlleAlsListe(String[] spalten, String[] werte, int seite, String sortierSpalte, String sortierRichtung)
			throws Exception;

	/**
	 * Liest alle Dokumente als Liste aus der Datenbank aus deren {@code spalte} und {@code wert} mit den Parametern übereinstimmen und sortiert sie nach
	 * {@code sortierSpalte} in {@code sortierRichtung}.
	 * 
	 * @param spalte
	 *            abzufragende Spalten für die Abfragebedingung
	 * @param wert
	 *            abzufragende Werte für die Abfragebedingung
	 * @param sortierSpalte
	 *            Spalte nach der sortiert werden soll
	 * @param sortierRichtung
	 *            Reihenfolge in der die Daten ausgegeben werden sollen
	 * @return eine Liste aller Dokumente auf der abgefragten Seite mit den entsprechenden Bedingungen und der gewünschten Sortierung
	 * @throws Exception
	 */
	public List<BewerbungsVorgangDokument> getAlleAlsListe(String[] spalte, String[] wert, String sortierSpalte, String sortierRichtung) throws Exception;

	/**
	 * Liest das Dokument aus der Datenbank als Dokumentobjekt aus, dessen ID uebergeben wurde
	 * 
	 * @param id
	 *            ID des Dokuments das abgefragt werden soll
	 * @return Dokumentobjekt, das der angegeben ID entspricht
	 * @throws Exception
	 */
	public BewerbungsVorgangDokument getEinzeln(int id) throws Exception;

	/**
	 * Liest das erste Dokument aus der Datenbank als Dokumentobjekt aus, das mit den Parametern {@code spalte} und {@code wert} uebereinstimmt
	 * 
	 * @param spalte
	 *            abzufragende spalte fuer die abfragebedingung
	 * @param wert
	 *            abzufragender wert auf den die {@code spalte} zu ueberpruefen ist
	 * @return Dokumentobjekt, das den Kriterien entspricht
	 * @throws Exception
	 */
	public BewerbungsVorgangDokument getEinzeln(String spalte, String wert) throws Exception;

	/**
	 * Liest das erste Dokument aus der Datenbank als Dokumentobjekt aus, das mit den Parametern {@code spalten} und {@code werte} uebereinstimmt
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalten} zu ueberpruefen ist
	 * @return Dokumentobjekt, das den Kriterien entspricht
	 * @throws Exception
	 */
	public BewerbungsVorgangDokument getEinzeln(String[] spalten, String[] werte) throws Exception;

	/**
	 * Gibt die Anzahl der vorhandenen Datensätze aus
	 * 
	 * @return Anzahl der gefundenen Dokumente
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
	 * @return anzahl der gefundenen Dokumente
	 * @throws Exception
	 */
	public int getAnzahl(String spalte, String wert) throws Exception;

	/**
	 * Gibt die Anzahl auf Basis einer bestimmten SELECT Abfrage aus
	 * 
	 * @param spalten
	 *            abzufragende spalten fuer die abfragebedingung
	 * @param werte
	 *            abzufragender werte auf den die {@code spalte} zu ueberpruefen ist
	 * @return anzahl der gefundenen Dokumente
	 * @throws Exception
	 */
	public int getAnzahl(String[] spalten, String[] werte) throws Exception;

	/**
	 * Fuegt ein Dokumentobjekt als neuen Datensatz in die Datenbank ein
	 * 
	 * @param daten
	 *            das Dokumentobjekt, das in die Datenbank eingefuegt werden soll
	 * @throws Exception
	 */
	public void erstellen(BewerbungsVorgangDokument daten) throws Exception;

	/**
	 * Aktualisiert einen bestehenden Dokumentdatensatz in der Datenbank
	 * 
	 * @param daten
	 *            das Dokumentobjekt, das in der Datenbank aktualisiert werden soll
	 * @throws Exception
	 */
	public void aktualisieren(BewerbungsVorgangDokument daten) throws Exception;

	/**
	 * Aktualisiert den Status eines BewerbungsVorgangDokumentes in der Datenbank
	 * 
	 * @param multiBewerbungsVorgangDokumentId
	 *            id der Bewerbungsvorgaenge (alle Einträge mit dieser BewerbungsVorgangId werden zunächst auf 0 gesetzt)
	 * @param bewerbungsVorgangId
	 *            id der BewerbungsVorgangDokumente, deren Status auf 1 gesetzt werden soll.
	 * @throws Exception
	 */
	public void aktualisieren(int[] multiBewerbungsVorgangDokumentId, int bewerbungsVorgangId) throws Exception;

	/**
	 * Loescht einen Dokumentdatensatz aus der Datenbank
	 * 
	 * @param daten
	 *            der Dokumentdatensatz, der aus der Datenbank geloescht werden soll
	 * @throws Exception
	 */
	public void loeschen(BewerbungsVorgangDokument daten) throws Exception;

}
