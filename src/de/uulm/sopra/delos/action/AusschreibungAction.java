package de.uulm.sopra.delos.action;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;

import de.uulm.sopra.delos.bean.Ausschreibung;
import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.dao.AusschreibungDao;
import de.uulm.sopra.delos.dao.BenutzerDao;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDao;
import de.uulm.sopra.delos.system.ZugriffsException;

/**
 * In dieser Action-Klasse liegt die Logik für die Ausschreibungen von der Erstellung, Änderung und Löschung von Stellen, bis zur benutzergruppenindividuellen
 * Erstellung von Bewerbungsvorgangslisten
 * 
 */
public class AusschreibungAction extends BasisAction {

	private static final long									serialVersionUID		= 4284566569691754353L;

	// einzelenen Daos
	private final AusschreibungDao								ausschreibungDao		= new AusschreibungDao();
	private final BenutzerDao									benutzerDao				= new BenutzerDao();
	private final BewerbungsVorgangDao							bewerbungsVorgangDao	= new BewerbungsVorgangDao();

	private Ausschreibung										ausschreibung;
	// Abfragevariable für ID des Bewerbungsvorgangs
	private int													bewerbungID;
	private BewerbungsVorgang									bewerbung				= new BewerbungsVorgang();

	// Listen/Maps für Select-Buttons
	// alle Bearbeiter
	private LinkedList<Benutzer>								bearbeiterSelect		= new LinkedList<Benutzer>();

	// Liste für Tabellenabellen, die auf der Indexoperation erzeugt werden.
	private LinkedList<?>										listeDaten;

	// Liste für Bewerbungsvorgänge zur Abfrage beim Löschvorgang und beim vorzeitig beenden
	private LinkedList<BewerbungsVorgang>						bewerbungsvorgaenge		= new LinkedList<BewerbungsVorgang>();
	// Liste für alle Ausschreibungen
	private LinkedList<Ausschreibung>							ausschreibungen			= new LinkedList<Ausschreibung>();

	// Hash-Maps
	private final HashMap<String, HashMap<Integer, Benutzer>>	benutzerListenMap		= new HashMap<String, HashMap<Integer, Benutzer>>();

	// ID's
	private int													ausschreibungId;
	private int													benutzerId				= 0;

	// Zielangabe zum Abschicken des AusschreibungFormulars (entweder Ausschreibung_aktualisieren oder Ausschreibung_erstellen)
	private String												formularZiel;

	// String Array zum Eintragen der abzufragenden Spalten
	private String[]											spalten;

	boolean														loeschen				= false;
	boolean														beenden					= false;

	/**
	 * Definition der Spalten nach denen man sortieren darf
	 */
	@Override
	public void prepare() throws Exception {
		super.prepare();

		listeSortSpalten.put("id", "`id`");
		listeSortSpalten.put("name", "`name`");
		listeSortSpalten.put("institut", "`institut`");
		listeSortSpalten.put("stunden", "`stundenzahl`");
		listeSortSpalten.put("beginn", "`startet`");
		listeSortSpalten.put("ende", "`endet`");
		listeSortSpalten.put("hin", "`hinzugefuegt`");
		listeSortSpalten.put("mod", "`bearbeitet`");
		listeSortSpalten.put("frist", "`bewerbungsfrist`");
	}

	/*
	 * Actions
	 */
	/**
	 * Es wird für die Übersicht eine Liste mit allen aktiven Stellen erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String index() {
		// Alle Stellen anzeigen, die Status 0 haben (aktiv) sind
		try {
			listeDaten = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe("status", "0", seite, listeSortSpalten.get(spalte),
					sortierungen.get(richtung));
			listeAnzahlElemente = ausschreibungDao.getAnzahl("status", "0");
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Allgemeine Liste erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es wird für den Ausschreiber eine Liste mit allen seinen Stellen erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexMeine() {
		Benutzer b = indexBasic();
		try {
			switch (b.getGruppeId()) {
				case 3:
					// alle Ausschreibungen des Ausschreibers
					listeDaten = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe("ausschreiber_id", b.getId() + "", seite,
							listeSortSpalten.get(spalte), sortierungen.get(richtung));
					listeAnzahlElemente = ausschreibungDao.getAnzahl("ausschreiber_id", b.getId() + "");
					break;
			}
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es wird für den Ausschreiber eine Liste mit allen seinen aktiven Stellen erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexMeineAktiv() {
		Benutzer b = indexBasic();
		try {
			switch (b.getGruppeId()) {
				case 3:
					// alle Ausschreibungen des Ausschreibers, die Status 0 haben
					listeDaten = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe(new String[] { "ausschreiber_id", "status" },
							new String[] { b.getId() + "", "0" }, seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
					listeAnzahlElemente = ausschreibungDao.getAnzahl(new String[] { "ausschreiber_id", "status" }, new String[] { b.getId() + "", "0" });
					break;
			}
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es wird für den Ausschreiber eine Liste mit allen seinen Stellen erstellt, die beendet/vorzeitig beendet sind
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexMeineBeendet() {
		Benutzer b = indexBasic();
		try {
			switch (b.getGruppeId()) {
				case 3:
					// alle Ausschreibungen des Ausschreibers, die Status 1 haben
					listeDaten = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe(new String[] { "ausschreiber_id", "status" },
							new String[] { b.getId() + "", "1" }, seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
					listeAnzahlElemente = ausschreibungDao.getAnzahl(new String[] { "ausschreiber_id", "status" }, new String[] { b.getId() + "", "1" });
					break;
			}
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden alle Bewerbungsvorgänge der jeweiligen Benutzergruppe als Liste erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungen() {
		Benutzer b = indexBasic();
		try {
			switch (b.getGruppeId()) {
				case 2:
					// alle Bewerbungsvorgänge Bewerber
					listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe("bewerber_id", b.getId() + "", seite,
							listeSortSpalten.get(spalte), sortierungen.get(richtung));
					listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl("bewerber_id", b.getId() + "");
					break;
				case 3:
					// alle Bewerbungsvorgänge Ausschreiber
					listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe("ausschreiber_id", b.getId() + "", seite,
							listeSortSpalten.get(spalte), sortierungen.get(richtung));
					listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl("ausschreiber_id", b.getId() + "");
					break;
				// alle Bewerbungsvorgänge Bearbeiter
				case 4:
					listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListeNichtNeu(new String[] { "bearbeiter_id" },
							new String[] { b.getId() + "" }, seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
					listeAnzahlElemente = bewerbungsVorgangDao.getAnzahlNichtNeu("bearbeiter_id", b.getId() + "");
					break;
			}
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden nur die abgeschlossenen Bewerbungsvorgänge erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungenAbgeschlossen() {
		Benutzer b = indexBasic();
		try {
			// abgeschlossen
			listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(spalten, new String[] { b.getId() + "", "3" }, seite,
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl(spalten, new String[] { b.getId() + "", "3" });
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden nur die abgelehnten Bewerbungsvorgänge erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungenAbgelehnt() {
		Benutzer b = indexBasic();
		try {
			// abgelehnt
			listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(spalten, new String[] { b.getId() + "", "4" }, seite,
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl(spalten, new String[] { b.getId() + "", "4" });
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden nur die Bewerbungsvorgänge in Bearbeitung erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungenAktiv() {
		Benutzer b = indexBasic();
		try {
			// aktiv
			listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(spalten, new String[] { b.getId() + "", "2" }, seite,
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl(spalten, new String[] { b.getId() + "", "2" });
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden nur die neuen Bewerbungsvorgänge erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungenNeu() {
		Benutzer b = indexBasic();
		try {
			// neu
			listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(spalten, new String[] { b.getId() + "", "1" }, seite,
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl(spalten, new String[] { b.getId() + "", "1" });
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden nur die vom Bewerber selbst zurückgezogenen Bewerbungsvorgänge erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungenZurueckgezogen() {
		Benutzer b = indexBasic();
		try {
			// zurückgezogen
			listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(spalten, new String[] { b.getId() + "", "0" }, seite,
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl(spalten, new String[] { b.getId() + "", "0" });
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Es werden alle Bewerbungsvorgänge desselben Bearbeiterinstituts erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerbungenInstitut() {
		Benutzer b = indexBasic();
		try {
			// Institut
			listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListeNichtNeu(new String[] { "institut" },
					new String[] { b.getInstitut() + "" }, seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = bewerbungsVorgangDao.getAnzahl("institut", b.getInstitut() + "");
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Liste(n) erstellen fehlgeschlagen", e);
		}

		return SUCCESS;
	}

	/**
	 * Ausschreibungsliste nach der Benutzersuche erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String suchen() {
		if (null != ausschreibung) {
			try {
				listeDaten = (LinkedList<Ausschreibung>) ausschreibungDao.getSuchergebnisse(ausschreibung.getName(), ausschreibung.getInstitut(),
						ausschreibung.getStundenzahl(), seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
				listeAnzahlElemente = ausschreibungDao.getAnzahlSuchergebnisse(ausschreibung.getName(), ausschreibung.getInstitut(),
						ausschreibung.getStundenzahl());
			} catch (SQLException e) {
				log.fatal("Suchanfrage fehlgeschlagen", e);
				return ERROR;
			}
		} else {
			listeDaten = new LinkedList<Ausschreibung>();
			listeAnzahlElemente = 0;
		}
		return SUCCESS;
	}

	/**
	 * Hilfsmethode für Session und Spalten-Benennung
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	private Benutzer indexBasic() {
		// Benutzer Session holen
		Benutzer b = new Benutzer();

		if (0 == benutzerId) {
			b = sessionBenutzer;
		} else {
			try {
				b = benutzerDao.getEinzeln("id", benutzerId + "");
			} catch (SQLException e) {
				log.fatal("Session Benutzer kann nicht geholt werden", e);
			}
		}

		// abzufragende Spalten definieren für Bearbeiter, Ausschreiber, Bewerber
		if (b.getGruppeId() == 2) {
			spalten = new String[] { "bewerber_id", "status" };
		}
		if (b.getGruppeId() == 3) {
			spalten = new String[] { "ausschreiber_id", "status" };
		}
		if (b.getGruppeId() == 4) {
			spalten = new String[] { "bearbeiter_id", "status" };
		}

		return b;
	}

	/**
	 * Hier wird eine Ausschreibung erstellt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String erstellen() throws Exception {
		// Zugriff abfragen
		if (sessionBenutzer.getGruppeId() != 3) { throw new ZugriffsException(); }
		if (null == ausschreibung) {
			ausschreibung = new Ausschreibung();
			ausschreibung.setAusschreiberId(sessionBenutzer.getId());
			ausschreibung.setAusschreiberName(sessionBenutzer.getName());
			return INPUT;
		} else {
			try {
				ausschreibungDao.erstellen(ausschreibung);
				this.benachrichtigungErzeugen(SUCCESS, this.getText("Ausschreibung.speichern.erfolgreich"));
			} catch (SQLException e) {
				log.fatal("Ausschreibung speichern fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Ausschreibung.speichern.fehlgeschlagen"));
				return ERROR;
			}
			return SUCCESS;
		}
	}

	/**
	 * Hier wird eine Ausschreibung gelöscht
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String loeschen() throws Exception {
		// unerlaubten Zugriff abfragen - nur Ausschreiber darf das
		if (sessionBenutzer.getGruppeId() != 3) { throw new ZugriffsException(); }
		try {
			ausschreibung = ausschreibungDao.getEinzeln("id", ausschreibungId + "");
			// Abfragen, ob es zu der AusschreibungsID Bewerbungsvorgänge gibt
			bewerbungsvorgaenge = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe("ausschreibung_id", ausschreibung.getId() + "",
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
		} catch (SQLException e) {
			log.fatal("loeschen - Ausschreibung abfragen fehlgeschlagen oder bewerbungsvorgaenge", e);
		}
		// unerlaubten Zugriff abfragen - Benutzer muss Inhaber der Ausschreibung sein
		if (ausschreibung.getAusschreiberId() != sessionBenutzer.getId()) {
			throw new ZugriffsException();
		} else {
			// gibt es keine, kann die Ausschreibung komplett gelöscht werden
			if (bewerbungsvorgaenge.isEmpty()) {
				try {
					if (ausschreibung.getAusschreiberId() != sessionBenutzer.getId()) { throw new ZugriffsException(); }
					log.info("in Ausschreibung löschen bewerbungvorgaenge.isEmpty drin");
					ausschreibungDao.loeschen(ausschreibung);
					this.benachrichtigungErzeugen(SUCCESS, this.getText("Ausschreibung.loeschen.erfolgreich"));
				} catch (SQLException e) {
					log.fatal("Ausschreibung löschen fehlgeschlagen", e);
					this.benachrichtigungErzeugen(ERROR, this.getText("Ausschreibung.loeschen.fehlgeschlagen"));
				}
			} else {
				this.benachrichtigungErzeugen(ERROR, this.getText("Ausschreibung.loeschen.fehlgeschlagen"));
			}
		}
		return SUCCESS;
	}

	/**
	 * Hier kann eine Ausschreibung vorzeitig durch den Ausschreiber beendet werden.
	 * 
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String vorzeitigBeenden() throws Exception {
		// nur Ausschreiber dürfen dies
		if (sessionBenutzer.getGruppeId() != 3) {
			throw new ZugriffsException();
		}
		// Der Status wird auf 1 gestezt und die neue Stellenanzahl wird auf die Summe der momentan
		// aktiven und abschlossenen Bewerbungen gesetzt
		else {
			try {
				// Ausschreibung holen
				ausschreibung = ausschreibungDao.getEinzeln("id", ausschreibungId + "");
				// unerlaubten Zugriff abfragen - nur der Ausschreibungsinhaber darf
				if (ausschreibung.getAusschreiberId() != sessionBenutzer.getId()) { throw new ZugriffsException(); }
				// Alle aktiven Bewerbungen für entsprechende Ausschreibung abfragen
				bewerbungsvorgaenge = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreibung_id", "status" },
						new String[] { ausschreibung.getId() + "", "2" }, listeSortSpalten.get(spalte), sortierungen.get(richtung));
				int anzahlBewerbungen = bewerbungsvorgaenge.size();
				// Alle abgeschlossenen Bewerbungen für entsprechende Ausschreibung abfragen
				bewerbungsvorgaenge = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreibung_id", "status" },
						new String[] { ausschreibung.getId() + "", "3" }, listeSortSpalten.get(spalte), sortierungen.get(richtung));
				anzahlBewerbungen = anzahlBewerbungen + bewerbungsvorgaenge.size();
				// neue Anzahl setzen
				ausschreibung.setStellenzahl(anzahlBewerbungen);
				// Status der Ausschreibung auf 1: deaktiviert / abgeschlossen setzen
				ausschreibung.setStatus(1);
				// in der Datenbank aktualisieren
				ausschreibungDao.aktualisieren(ausschreibung);
			} catch (SQLException e) {
				log.fatal("vorzeitigBeenden fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Ausschreibung.vorzeitigBeenden.fehlgeschlagen"));
				return ERROR;
			}
		}
		this.benachrichtigungErzeugen(SUCCESS, this.getText("Ausschreibung.vorzeitigBeenden.erfolgreich"));
		return SUCCESS;
	}

	/**
	 * Die Ausschreibung wird automatisch beendet, wenn das heutige Datum nach dem Start der Ausschreibung liegt
	 * 
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String beendenAblauf() {
		try {
			java.util.Date heute = new java.util.Date();
			ausschreibungen = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe("status", "0", seite, listeSortSpalten.get(spalte),
					sortierungen.get(richtung));
			for (Ausschreibung a : ausschreibungen) {
				if (a.getStartet().after(new Date(heute.getTime()))) {
					a.setStatus(1);
					ausschreibungDao.aktualisieren(a);
				}
			}
		} catch (SQLException e) {
			log.fatal("beendenAblauf fehlgeschlagen", e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Hier wird eine Ausschreibung aktualisiert
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String aktualisieren() throws Exception {

		// nur Ausschreiber dürfen dies überhaupt
		if (sessionBenutzer.getGruppeId() != 3) { throw new ZugriffsException(); }
		if (null == ausschreibung) {
			try {
				ausschreibung = ausschreibungDao.getEinzeln("id", ausschreibungId + "");
				System.out.println("aktualisieren -if");
				System.out.println("BenutzerId" + sessionBenutzer.getId());
				System.out.println("Ausschreiber ID: " + ausschreibung.getAusschreiberId());
				// Exception, wenn Ausschreibung schon (vorzeitig) beendet
				if (ausschreibung.getStatus() == 1) { throw new ZugriffsException(); }
				// Exception, wenn Benutzer nicht der Ausschreibungsinhaber ist
				if (ausschreibung.getAusschreiberId() != sessionBenutzer.getId()) { throw new ZugriffsException(); }
			} catch (SQLException e) {
				log.fatal("Laden der Ausschreibung fehlgeschlagen", e);
			}
			return INPUT;
		} else {
			System.out.println("aktualisieren -else");
			System.out.println("BenutzerId" + sessionBenutzer.getId());
			System.out.println("Ausschreiber ID: " + ausschreibung.getAusschreiberId());
			// Exception, wenn Ausschreibung schon (vorzeitig) beendet
			if (ausschreibung.getStatus() == 1) { throw new ZugriffsException(); }
			// Exception, wenn Benutzer nicht der Ausschreibungsinhaber ist
			if (ausschreibung.getAusschreiberId() != sessionBenutzer.getId()) { throw new ZugriffsException(); }
			try {
				ausschreibungDao.aktualisieren(ausschreibung);
				this.benachrichtigungErzeugen(SUCCESS, this.getText("Ausschreibung.aktualisieren.erfolgreich"));
			} catch (SQLException e) {
				log.fatal("Ausschreibung aktualisieren fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Ausschreibung.aktualisieren.fehlgeschlagen"));
				return ERROR;
			}
		}

		return SUCCESS;
	}

	/**
	 * Methode um eine einzelne Ausschreibung mit allen Details anzeigen zu lassen
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String anzeigen() throws Exception {
		try {
			// Ausschreibung holen
			ausschreibung = ausschreibungDao.getEinzeln("id", ausschreibungId + "");

			if (ausschreibung.getStatus() == 0) {
				beenden = true;
			} else {
				beenden = false;
			}

			// Mit der AusschreibungsID entsprechenden BewerbungsVorgangID holen
			String[] spalteBewerbung = { "bewerber_id", "ausschreibung_id" };
			bewerbung = bewerbungsVorgangDao.getEinzeln(spalteBewerbung, new String[] { sessionBenutzer.getId() + "", ausschreibungId + "" });
			bewerbungID = bewerbung.getId();
			log.debug("Bewerbung:" + bewerbungID);

			// autorisierten Zugriff abfragen
			boolean unauthorisiert = true;
			if (sessionBenutzer.getGruppeId() == 2) {
				if (bewerbung.getBewerberId() == sessionBenutzer.getId()) {
					unauthorisiert = false;
				}
			}
			if (sessionBenutzer.getGruppeId() == 3) {
				if (ausschreibung.getAusschreiberId() == sessionBenutzer.getId()) {
					unauthorisiert = false;
					listeDaten = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreibung_id", "status" },
							new String[] { ausschreibung.getId() + "", "3" }, "name", "asc");
				}
			}
			if (sessionBenutzer.getGruppeId() == 4) {
				if (ausschreibung.getInstitut() == sessionBenutzer.getInstitut()) {
					unauthorisiert = false;
				}
			}
			// Alle Stellen mit Status 0 (aktiv) auf jeden Fall autorisieren!
			if (ausschreibung.getStatus() == 0) {
				unauthorisiert = false;
			}
			if (unauthorisiert) { throw new ZugriffsException(); }

			// Abfragen, ob es zu der AusschreibungsID Bewerbungsvorgänge gibt (benötigt, für Löschen-Button)
			bewerbungsvorgaenge = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe("ausschreibung_id", ausschreibung.getId() + "",
					listeSortSpalten.get(spalte), sortierungen.get(richtung));
			// Abfrage für Löschen-Button entsprechend setzen
			if (bewerbungsvorgaenge.isEmpty()) {
				loeschen = true;
			} else {
				loeschen = false;
			}
		} catch (SQLException e) {
			log.fatal("Anzeigen der Ausschreibung fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Ausschreibung.anzeigen.fehlgeschlagen"));
		}
		if (0 < ausschreibung.getId()) { return SUCCESS; }
		return ERROR;
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		if (null == spalte || null == listeSortSpalten.get(spalte)) {
			spalte = "hin";
			richtung = "desc";
		}

		fieldErrorsDuplikateEntfernen();
	}

	/**
	 * Validert die Formularfelder, wenn eine neue Ausschreibung erstellt wird
	 * 
	 * @throws Exception
	 */
	public void validateErstellen() throws Exception {
		formularZiel = "Ausschreibung_erstellen";
		log.debug("validateErstellen aufgerufen");

		// BearbeiterSelect erstellen
		try {
			bearbeiterSelect = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe(new String[] { "gruppe_id", "institut" },
					new String[] { "4", sessionBenutzer.getInstitut() + "" }, "`nachname`", sortierungen.get(richtung));
			bearbeiterSelect.add(new Benutzer("––––––––––––––––––", ""));
			bearbeiterSelect.addAll(benutzerDao.getAlleAlsListe("gruppe_id", "4", "`nachname`", sortierungen.get(richtung)));
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Benutzerliste (Bearbeiter) erstellen fehlgeschlagen", e);
		}

		if (null != ausschreibung) {
			log.debug("validateErstellen ausführen");
			java.util.Date heute = new java.util.Date();

			// Illegale Eingaben entfernen
			LinkedList<String> check = new LinkedList<String>();
			check.add("name");
			checkBeanHTML(ausschreibung, check);

			// Ausschreiber-Name und Id neu setzen
			ausschreibung.setAusschreiberId(sessionBenutzer.getId());
			ausschreibung.setAusschreiberName(sessionBenutzer.getName());
			ausschreibung.setStundenzahl(ausschreibung.getStundenzahl());
			ausschreibung.setStellenzahl(ausschreibung.getStellenzahl());

			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			ausschreibung.setBeschreibung(Jsoup.clean(ausschreibung.getBeschreibung(), "", Whitelist.basic(), d).trim());
			ausschreibung.setVoraussetzungen(Jsoup.clean(ausschreibung.getVoraussetzungen(), "", Whitelist.basic(), d).trim());

			if (ausschreibung.getBeschreibung().length() == 0) {
				addFieldError("ausschreibung.beschreibung", this.getText("Ausschreibung.speichern.beschreibung.benoetigt"));
			}
			if (ausschreibung.getStartet().before(ausschreibung.getBewerbungsfrist())) {
				addFieldError("ausschreibung.startet", this.getText("Ausschreibung.speichern.startet.vorfrist"));
			}
			if (ausschreibung.getEndet().before(ausschreibung.getStartet())) {
				addFieldError("ausschreibung.endet", this.getText("Ausschreibung.speichern.endet.vorstart"));
			}
			if (ausschreibung.getStartet().before(new Date(heute.getTime()))) {
				addFieldError("ausschreibung.startet", this.getText("Ausschreibung.speichern.startet.datum"));
			}
			if (ausschreibung.getBewerbungsfrist().before(new Date(heute.getTime()))) {
				addFieldError("ausschreibung.bewerbungsfrist", this.getText("Ausschreibung.speichern.bewerbungsfrist.datum"));
			}
			if (ausschreibung.getEndet().before(new Date(heute.getTime()))) {
				addFieldError("ausschreibung.endet", this.getText("Ausschreibung.speichern.endet.datum"));
			}
		}

		if (hasFieldErrors()) {
			Map<String, List<String>> fieldErrorMap = getFieldErrors();
			for (Map.Entry<String, List<String>> entry : fieldErrorMap.entrySet()) {
				log.debug("Validierung fehlgeschlagen: " + entry.getKey() + " : " + entry.getValue());
			}
		}
	}

	/**
	 * Validiert die Formularfelder, wenn eine Ausschreibung aktualisiert wird
	 * 
	 * @throws Exception
	 */
	public void validateAktualisieren() throws Exception {
		formularZiel = "Ausschreibung_aktualisieren";
		log.debug("validateAktualisieren aufgerufen");

		// Dies sorgt dafür, dass nach der Validierung die Select-Boxen auch wieder
		// ausgefüllt werden
		// BearbeiterSelect erstellen
		try {
			bearbeiterSelect = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe(new String[] { "gruppe_id", "institut" },
					new String[] { "4", sessionBenutzer.getInstitut() + "" }, "`nachname`", sortierungen.get(richtung));
			bearbeiterSelect.add(new Benutzer("––––––", ""));
			bearbeiterSelect.addAll(benutzerDao.getAlleAlsListe("gruppe_id", "4", "`nachname`", sortierungen.get(richtung)));
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Benutzerliste (Bearbeiter) erstellen fehlgeschlagen", e);
		}

		if (null != ausschreibung) {
			log.debug("validateAktualisieren ausführen");
			new java.util.Date();

			// illegalen Eingaben entfernen
			checkBeanHTML(ausschreibung);
			// und Beschreibung und Anforderungen aus der HTML-Validierung wieder rausnehmen
			fieldErrorsEntfernen("ausschreibung", new String[] { "beschreibung", "voraussetzungen" });

			// Ausschreiber-Name und Id neu setzen
			ausschreibung.setAusschreiberId(sessionBenutzer.getId());
			ausschreibung.setAusschreiberName(sessionBenutzer.getName());
			ausschreibung.setStundenzahl(ausschreibung.getStundenzahl());
			ausschreibung.setStellenzahl(ausschreibung.getStellenzahl());

			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			ausschreibung.setBeschreibung(Jsoup.clean(ausschreibung.getBeschreibung(), "", Whitelist.basic(), d).trim());
			ausschreibung.setVoraussetzungen(Jsoup.clean(ausschreibung.getVoraussetzungen(), "", Whitelist.basic(), d).trim());

			if (ausschreibung.getBeschreibung().length() == 0) {
				addFieldError("ausschreibung.beschreibung", this.getText("Ausschreibung.speichern.beschreibung.benoetigt"));
			}
			if (ausschreibung.getStartet().before(ausschreibung.getBewerbungsfrist())) {
				addFieldError("ausschreibung.startet", this.getText("Ausschreibung.speichern.startet.vorfrist"));
			}
			if (ausschreibung.getEndet().before(ausschreibung.getStartet())) {
				addFieldError("ausschreibung.endet", this.getText("Ausschreibung.speichern.endet.vorstart"));
			}

		}
	}

	/**
	 * Validiert die Suchfunktion
	 * 
	 * @throws Exception
	 */
	public void validateSuchen() throws Exception {
		log.debug("validateSuchen aufrufen");
		if (null != ausschreibung) {
			log.debug("validateSuchen ausführen");
			// illegalen Eingaben entfernen
			checkBeanHTML(ausschreibung);

			if (ausschreibung.getName().length() == 0) {
				addFieldError("ausschreibung.name", this.getText("Ausschreibung.speichern.name.benoetigt"));
			}
		}
	}

	/*
	 * Get/Set für Parameter
	 */
	/**
	 * @return the ausschreibung
	 */
	public Ausschreibung getAusschreibung() {
		return ausschreibung;
	}

	/**
	 * @return the ausschreibungId
	 */
	public int getAusschreibungId() {
		return ausschreibungId;
	}

	/**
	 * @return the benutzerId
	 */
	public int getBenutzerId() {
		return benutzerId;
	}

	/**
	 * @param benutzerId
	 *            the benutzerId to set
	 */
	public void setBenutzerId(final int benutzerId) {
		this.benutzerId = benutzerId;
	}

	/**
	 * @param ausschreibung
	 *            the ausschreibung to set
	 */
	public void setAusschreibung(final Ausschreibung ausschreibung) {
		this.ausschreibung = ausschreibung;
	}

	/**
	 * @param ausschreibungId
	 *            the ausschreibungId to set
	 */
	public void setAusschreibungId(final int ausschreibungId) {
		this.ausschreibungId = ausschreibungId;
	}

	/**
	 * @return the ausschreibungDao
	 */
	public AusschreibungDao getAusschreibungDao() {
		return ausschreibungDao;
	}

	/**
	 * @return the benutzerDao
	 */
	public BenutzerDao getBenutzerDao() {
		return benutzerDao;
	}

	/**
	 * @return the bewerbungsVorgangDao
	 */
	public BewerbungsVorgangDao getBewerbungsVorgangDao() {
		return bewerbungsVorgangDao;
	}

	/**
	 * @return the listeDaten
	 */
	public LinkedList<?> getListeDaten() {
		return listeDaten;
	}

	/**
	 * @return the benutzerListenMap
	 */
	public HashMap<String, HashMap<Integer, Benutzer>> getBenutzerListen() {
		return benutzerListenMap;
	}

	/**
	 * @return the benutzerListenMap
	 */
	public HashMap<String, HashMap<Integer, Benutzer>> getBenutzerListenMap() {
		return benutzerListenMap;
	}

	/**
	 * @return the bearbeiterSelect
	 */
	public LinkedList<Benutzer> getBearbeiterSelect() {
		return bearbeiterSelect;
	}

	/**
	 * @param bearbeiterSelect
	 *            the bearbeiterSelect to set
	 */
	public void setBearbeiterSelect(final LinkedList<Benutzer> bearbeiterSelect) {
		this.bearbeiterSelect = bearbeiterSelect;
	}

	/**
	 * @return the formularZiel
	 */
	public String getFormularZiel() {
		return formularZiel;
	}

	/**
	 * @return the bewerbung
	 */
	public BewerbungsVorgang getBewerbung() {
		return bewerbung;
	}

	/**
	 * @param bewerbung
	 *            the bewerbung to set
	 */
	public void setBewerbung(final BewerbungsVorgang bewerbung) {
		this.bewerbung = bewerbung;
	}

	/**
	 * @return the spalten
	 */
	public String[] getSpalten() {
		return spalten;
	}

	/**
	 * @param spalten
	 *            the spalten to set
	 */
	public void setSpalten(final String[] spalten) {
		this.spalten = spalten;
	}

	/**
	 * @return the bewerbungsvorgaenge
	 */
	public LinkedList<BewerbungsVorgang> getBewerbungsvorgaenge() {
		return bewerbungsvorgaenge;
	}

	/**
	 * @param bewerbungsvorgaenge
	 *            the bewerbungsvorgaenge to set
	 */
	public void setBewerbungsvorgaenge(final LinkedList<BewerbungsVorgang> bewerbungsvorgaenge) {
		this.bewerbungsvorgaenge = bewerbungsvorgaenge;
	}

	/**
	 * @return the loeschen
	 */
	public boolean isLoeschen() {
		return loeschen;
	}

	/**
	 * @param loeschen
	 *            the loeschen to set
	 */
	public void setLoeschen(final boolean loeschen) {
		this.loeschen = loeschen;
	}

	/**
	 * @return the bewerbungID
	 */
	public int getBewerbungID() {
		return bewerbungID;
	}

	/**
	 * @param bewerbungID
	 *            the bewerbungID to set
	 */
	public void setBewerbungID(final int bewerbungID) {
		this.bewerbungID = bewerbungID;
	}

	/**
	 * @return the beenden
	 */
	public boolean isBeenden() {
		return beenden;
	}

	/**
	 * @param beenden
	 *            the beenden to set
	 */
	public void setBeenden(final boolean beenden) {
		this.beenden = beenden;
	}

	/**
	 * @return the ausschreibungen
	 */
	public LinkedList<Ausschreibung> getAusschreibungen() {
		return ausschreibungen;
	}

	/**
	 * @param ausschreibungen
	 *            the ausschreibungen to set
	 */
	public void setAusschreibungen(final LinkedList<Ausschreibung> ausschreibungen) {
		this.ausschreibungen = ausschreibungen;
	}

}