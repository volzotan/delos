package de.uulm.sopra.delos.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;

import de.uulm.sopra.delos.bean.Ausschreibung;
import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.bean.BewerbungsVorgangDokument;
import de.uulm.sopra.delos.bean.Dokument;
import de.uulm.sopra.delos.bean.Nachricht;
import de.uulm.sopra.delos.dao.AusschreibungDao;
import de.uulm.sopra.delos.dao.BenutzerDao;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDao;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDao;
import de.uulm.sopra.delos.dao.DokumentDao;
import de.uulm.sopra.delos.dao.NachrichtDao;
import de.uulm.sopra.delos.system.ZugriffsException;

/**
 * Jegliche Aktionen die einen Bewerbungsvorgang betreffen
 */
public class BewerbungsVorgangAction extends BasisAction {

	private static final long												serialVersionUID							= 4274885782753302966L;
	// Per URL Parameter übergebene ID einer Ausschreibung, z.B, zum Erstellen des Bewerbungsvorgangs benutzt
	private int																ausschreibungId;
	// DAO zum Abrufen der Ausschreibungsdaten
	private final AusschreibungDao											ausschreibungDao							= new AusschreibungDao();
	// DAO zum Abrufen von Benutzerdaten
	private final BenutzerDao												benutzerDao									= new BenutzerDao();
	// DAO zum Verschicken von Nachrichten bei Aktionen
	private final NachrichtDao												nachrichtDao								= new NachrichtDao();
	// DAO um Dokumente zu laden, die abzugeben sind etc.
	private final DokumentDao												dokumentDao									= new DokumentDao();
	// DAO um bewerbungsspezifische Dokumente zu speichern/laden
	private final BewerbungsVorgangDokumentDao								bewerbungsVorgangDokumentDao				= new BewerbungsVorgangDokumentDao();
	// Das interne Bewerbungsvorgangsobjekt zur Ein- und Ausgabe
	private BewerbungsVorgang												bewerbungsVorgang;
	// DAO zum Abrufen und Speichern von Bewerbungsvorgangsdaten
	private final BewerbungsVorgangDao										bewerbungsVorgangDao						= new BewerbungsVorgangDao();
	// Per URL Parameter übergebene ID eines Bewerbungsvorgangs
	private int																bewerbungsVorgangId;
	// Ausgabelisten von Bewerbungsvorgängen und Anzahlen für in DB vorhandener Vorgänge
	private final HashMap<String, LinkedList<BewerbungsVorgang>>			bewerbungsVorgangListen						= new HashMap<String, LinkedList<BewerbungsVorgang>>();
	private final HashMap<String, Integer>									bewerbungsVorgangListenAnzahlen				= new HashMap<String, Integer>();
	// Ausgabelisten von BewerbungsVorgangDokument für Bewerbungsvorgänge
	private final HashMap<String, LinkedList<BewerbungsVorgangDokument>>	bewerbungsVorgangDokumenteListen			= new HashMap<String, LinkedList<BewerbungsVorgangDokument>>();
	private final HashMap<String, Integer>									bewerbungsVorgangDokumenteListenAnzahlen	= new HashMap<String, Integer>();

	private FileInputStream													fileInputStream;

	private final HashMap<String, String>									bewerbungsVorgangSpalten					= new HashMap<String, String>();
	private LinkedList<Benutzer>											bearbeiterSelect;

	private int[]															multiBewerbungsVorgangDokumentId			= new int[0];

	@Override
	public void prepare() throws Exception {
		super.prepare();

		bewerbungsVorgangSpalten.put("id", "`id`");
		bewerbungsVorgangSpalten.put("status", "`status`");
		bewerbungsVorgangSpalten.put("ausschreibung", "`ausschreibung_id`");
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		if (null == spalte || null == bewerbungsVorgangSpalten.get(spalte)) {
			spalte = "ausschreibung";
		}

		fieldErrorsDuplikateEntfernen();
	}

	/**
	 * Überprüft Zugriffsrechte für Benutzergruppen, zugeschnitten auf den Bewerbungsvorgang.
	 * 
	 * @param gruppen
	 * @throws ZugriffsException
	 * @throws SQLException
	 */
	private void checkZugriffsrechte(final List<String> gruppen) throws ZugriffsException, SQLException {
		boolean unauthorisiert = true;

		if (gruppen.contains("bewerber")) {
			if (bewerbungsVorgang.getBewerberId() == sessionBenutzer.getId()) {
				unauthorisiert = false;
			}
		}
		if (gruppen.contains("ausschreiber")) {
			if (bewerbungsVorgang.getAusschreiberId() == sessionBenutzer.getId()) {
				unauthorisiert = false;
			}
		}
		if (gruppen.contains("bearbeiterGleichesInstitut")) { // schließt den ausgewählten Bearbeiter selbst mit ein
			Benutzer ben = benutzerDao.getEinzeln(bewerbungsVorgang.getBearbeiterId());
			if (sessionBenutzer.getId() != 2 && ben != null && sessionBenutzer.getInstitut() == ben.getInstitut()) {
				unauthorisiert = false;
			}
		}
		if (gruppen.contains("bearbeiter")) {
			if (bewerbungsVorgang.getBearbeiterId() == sessionBenutzer.getId()) {
				unauthorisiert = false;
			}
		}
		if (gruppen.contains("admin")) {
			if (sessionBenutzer.getGruppeId() == 1) {
				unauthorisiert = false;
			}
		}

		if (unauthorisiert) {
			log.debug("BewerbungsVorgang Zugriff verweigert");
			throw new ZugriffsException();
		}
	}

	/**
	 * Erstellt neuen Bewerbungsvorgang, wird ausgelöst durch Bewerbung eines Bewerbers auf Ausschreibung.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 */
	public String erstellen() throws ZugriffsException {
		/*
		 * Zugriffsrechte : erstellen dürfen nur Bewerber (AuthInterceptor) ohne Bewerbungsvorgang in entsprechender Ausschreibung
		 */

		try {
			// Darf nicht schon einen Bewerbungsvorgang für die Ausschreibung laufen haben
			if (0 < bewerbungsVorgangDao.getEinzeln(new String[] { "bewerber_id", "ausschreibung_id" },
					new String[] { sessionBenutzer.getId() + "", ausschreibungId + "" }).getId()) {
				benachrichtigungErzeugen("info", getText("BewerbungsVorgang.erstellen.existiertBereits"));
				return ERROR;
			}

			// Ausschreibung muss aktiv sein
			if (ausschreibungDao.getEinzeln(ausschreibungId).getStatus() != 0) {
				benachrichtigungErzeugen(ERROR, getText("BewerbungsVorgang.erstellen.ausschreibungBeendet"));
				return ERROR;
			}
		} catch (SQLException e) {
			log.fatal("BewerbungsVorgang erstellen fehlgeschlagen" + e);
			benachrichtigungErzeugen(ERROR, getText("BewerbungsVorgang.erstellen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		Ausschreibung template;
		try {
			template = ausschreibungDao.getEinzeln(ausschreibungId);
			if (0 == template.getId()) {
				template = ausschreibungDao.getEinzeln(bewerbungsVorgang.getAusschreibungId());
			}
			if (0 == template.getId()) {
				log.fatal("Ausschreibung laden fehlgeschlagen");
				this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.erstellen.ausschreibungLadenFehlgeschlagen"));
				return ERROR;
			}
		} catch (SQLException e) {
			log.fatal("Ausschreibung laden fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.erstellen.ausschreibungLadenFehlgeschlagen"));
			return ERROR;
		}

		if (template.getStellenzahl() == template.getBelegt()) {
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.erstellen.alleStellenBelegt"));
			return ERROR;
		}

		if (bewerbungsVorgang == null) {
			bewerbungsVorgang = new BewerbungsVorgang();
			bewerbungsVorgang.setBewerberId(sessionBenutzer.getId());
			bewerbungsVorgang.setBewerberName(sessionBenutzer.getName());
			bewerbungsVorgang.setAusschreiberId(template.getAusschreiberId());
			bewerbungsVorgang.setInstitut(template.getInstitut());
			bewerbungsVorgang.setAusschreiberName(template.getAusschreiberName());
			bewerbungsVorgang.setBearbeiterId(template.getBearbeiterId());
			bewerbungsVorgang.setBearbeiterName(template.getBearbeiterName());
			bewerbungsVorgang.setAusschreibungId(template.getId());
			bewerbungsVorgang.setAusschreibungName(template.getName());
			bewerbungsVorgang.setStundenzahl(template.getStundenzahl());
			bewerbungsVorgang.setStartet(template.getStartet());
			bewerbungsVorgang.setEndet(template.getEndet());
			bewerbungsVorgang.setStatus(1);
			return INPUT;
		} else {
			template.getAusschreiberId();
			try {
				bewerbungsVorgangDao.erstellen(bewerbungsVorgang);
				benachrichtigungErzeugen(SUCCESS, getText("BewerbungsVorgang.erstellen.erfolgreich"));

				// Nachrichten an beteiligte Personen verschicken

				versendeSystemnachricht(bewerbungsVorgang.getAusschreiberId(), bewerbungsVorgang, "BewerbungsVorgang_erstellen_Ausschreiber",
						getText("VelocityTemplate.betreff.BewerbungsVorgang_erstellen_Ausschreiber") + bewerbungsVorgang.getAusschreibungName());

				versendeSystemnachricht(bewerbungsVorgang.getBewerberId(), bewerbungsVorgang, "BewerbungsVorgang_erstellen_Bewerber",
						getText("VelocityTemplate.betreff.BewerbungsVorgang_erstellen_Bewerber") + bewerbungsVorgang.getAusschreibungName());

				// bewerbungsVorgangId setzen zum Aufruf der Bewerbungsvorgangsdetails
				String[] spaltennamen = { "bewerber_id", "ausschreiber_id", "ausschreibung_id" };
				String[] werte = { Integer.toString(bewerbungsVorgang.getBewerberId()), Integer.toString(bewerbungsVorgang.getAusschreiberId()),
						Integer.toString(bewerbungsVorgang.getAusschreibungId()) };
				bewerbungsVorgangId = bewerbungsVorgangDao.getEinzeln(spaltennamen, werte).getId();

				return SUCCESS;
			} catch (SQLException e) {
				log.fatal("Bewerbungsvorgang erstellen fehlgeschlagen " + e);
				this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.erstellen.fehlgeschlagen"));
				return ERROR;
			}
		}
	}

	/**
	 * Holt einen Bewerbungsvorgang aus der DB und stellt diesen dar.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String anzeigen() throws Exception {

		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang anzeigen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.anzeigen.fehlgeschlagen"));
			return ERROR;
		}

		/*
		 * Zugriffsrechte : anzeigen darf jeder der betroffenen Benutzer
		 */
		try {
			List<String> zugriffsrechte = new LinkedList<String>();
			zugriffsrechte.add("bewerber");
			zugriffsrechte.add("ausschreiber");
			zugriffsrechte.add("bearbeiterGleichesInstitut");
			checkZugriffsrechte(zugriffsrechte);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang anzeigen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.anzeigen.fehlgeschlagen"));
			return ERROR;
		}

		// Lade zugehörige Dokumente
		try {
			bewerbungsVorgangDokumenteListen.put("alle", (LinkedList<BewerbungsVorgangDokument>) bewerbungsVorgangDokumentDao.getAlleAlsListe(
					"bewerbungs_vorgang_id", bewerbungsVorgangId + "", "`id`", richtung));
			bewerbungsVorgangDokumenteListenAnzahlen.put("alle", bewerbungsVorgangDokumentDao.getAnzahl("bewerbungs_vorgang_id", bewerbungsVorgangId + ""));
			bewerbungsVorgangDokumenteListen.put("abgegebene", (LinkedList<BewerbungsVorgangDokument>) bewerbungsVorgangDokumentDao.getAlleAlsListe(
					new String[] { "status", "bewerbungs_vorgang_id" }, new String[] { "1", bewerbungsVorgangId + "" }, "`id`", richtung));
			bewerbungsVorgangDokumenteListenAnzahlen.put("abgegebene",
					bewerbungsVorgangDokumentDao.getAnzahl(new String[] { "status", "bewerbungs_vorgang_id" }, new String[] { "1", bewerbungsVorgangId + "" }));
			bewerbungsVorgangDokumenteListen.put("abzugebene", (LinkedList<BewerbungsVorgangDokument>) bewerbungsVorgangDokumentDao.getAlleAlsListe(
					new String[] { "status", "bewerbungs_vorgang_id" }, new String[] { "0", bewerbungsVorgangId + "" }, "`id`", richtung));
			bewerbungsVorgangDokumenteListenAnzahlen.put("abzugebene",
					bewerbungsVorgangDokumentDao.getAnzahl(new String[] { "status", "bewerbungs_vorgang_id" }, new String[] { "0", bewerbungsVorgangId + "" }));
		} catch (SQLException e) {
			log.fatal("Dokumente laden fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.anzeigen.dokumenteLaden.fehlgeschlagen"));
			return ERROR;
		}

		if (0 < bewerbungsVorgang.getBewerberId()) { return SUCCESS; }
		return ERROR;
	}

	/**
	 * Aktualisiert eine Reihe von zur Bewerbung gehörenden Dokumentenstatusangaben
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws Exception
	 */
	public String dokumentMultiCheckboxAuswertung() throws Exception {
		try {
			bewerbungsVorgangDokumentDao.aktualisieren(multiBewerbungsVorgangDokumentId, bewerbungsVorgangId);
			this.benachrichtigungErzeugen(SUCCESS, this.getText("Dokument.speichern.erfolgreich"));
		} catch (SQLException e) {
			log.fatal("Dokument speichern fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.speichern.fehlgeschlagen"));
			return ERROR;
		}
		return anzeigen();
	}

	/**
	 * Ändert Details eines Bewerbungsvorgangs.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws Exception
	 */
	public String aktualisieren() throws Exception {
		/*
		 * Zugriffsrechte : aktualisieren darf der Bewerber, Ausschreiber & Vertreter
		 */
		List<String> zugriffsrechte = new LinkedList<String>();
		zugriffsrechte.add("bewerber");
		zugriffsrechte.add("ausschreiber");

		// überprüfen ob existiert
		if (bewerbungsVorgang == null) {

			try {
				bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);
			} catch (SQLException e) {
				log.fatal("Bewerbungsvorgang laden (aktualisieren) fehlgeschlagen" + e);
				this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.aktualiseren.vorgangLadenFehlgeschlagen"));
				// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
				return ERROR;
			}

			checkZugriffsrechte(zugriffsrechte);
			return INPUT;
		}

		try {
			// falls Bewerber, alle nicht zu aktualisierenden Daten aus der DB holen
			if (sessionBenutzer.getGruppeId() == 2) {
				BewerbungsVorgang dbBewVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgang.getId());
				dbBewVorgang.setKommentar(bewerbungsVorgang.getKommentar());
				bewerbungsVorgang = dbBewVorgang;
			}
			// falls Ausschreiber, alle nicht aktualisierbaren Daten aus der DB holen
			if (sessionBenutzer.getGruppeId() == 3) {
				BewerbungsVorgang dbBewVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgang.getId());
				dbBewVorgang.setBearbeiterId(bewerbungsVorgang.getBearbeiterId());
				dbBewVorgang.setStundenzahl(bewerbungsVorgang.getStundenzahl());
				dbBewVorgang.setStartet(bewerbungsVorgang.getStartet());
				dbBewVorgang.setEndet(bewerbungsVorgang.getEndet());
				bewerbungsVorgang = dbBewVorgang;
			}

			try {
				checkZugriffsrechte(zugriffsrechte);
			} catch (SQLException e) {
				log.fatal("Bewerbungsvorgang aktualisieren fehlgeschlagen" + e);
				this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.aktualisieren.fehlgeschlagen"));
				return ERROR;
			}
			// falls Ausschreiber, Status nach dem Aktualisieren ändern
			if (sessionBenutzer.getGruppeId() == 3) {
				if (bewerbungsVorgang.getStatus() == 1 || bewerbungsVorgang.getStatus() == 2) { // Wenn Status Neu/Laufend
					bewerbungsVorgang.setStatus(bewerbungsVorgang.getStatus() * -1); // auf Status geändert setzen

					// Nachricht an den Bewerber wegen Zustimmung

					versendeSystemnachricht(bewerbungsVorgang.getBewerberId(), bewerbungsVorgang, "BewerbungsVorgang_aktualisieren_Bewerber",
							getText("VelocityTemplate.betreff.BewerbungsVorgang_aktualisieren_Bewerber") + bewerbungsVorgang.getAusschreibungName());
				}
			}

			bewerbungsVorgangDao.aktualisieren(bewerbungsVorgang);
			benachrichtigungErzeugen(SUCCESS, getText("BewerbungsVorgang.aktualisieren.erfolgreich"));
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang aktualisieren fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.aktualisieren.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		bewerbungsVorgangId = bewerbungsVorgang.getId();
		return SUCCESS;
	}

	/**
	 * Ausschreiber akzeptieren Bewerbungsvorgänge.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 */
	public String akzeptieren() throws ZugriffsException {
		/*
		 * Zugriffsrechte : akzeptieren dürfen nur Ausschreiber & Vertreter
		 */

		List<String> zugriffsrechte = new LinkedList<String>();
		zugriffsrechte.add("ausschreiber");

		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang laden (akzeptieren) fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.vorgangLadenFehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		try {
			checkZugriffsrechte(zugriffsrechte);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang akzeptieren fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.akzeptieren.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		// überprüfen ob existiert
		if (bewerbungsVorgang.getBewerberId() <= 0) {
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.vorgangExistiertNicht"));
			return ERROR;
		}

		// überprüfen ob die Bewerbungsbedingungen akzeptiert wurden
		if (bewerbungsVorgang.getStatus() != 1) {
			if (bewerbungsVorgang.getStatus() < 0) {
				this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.aenderungenMuessenAkzeptiertWerden"));
			} else {
				this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.akzeptieren.fehlgeschlagen.status"));
			}
			return ERROR;
		}

		try {
			bewerbungsVorgang.setStatus(2);
			bewerbungsVorgangDao.aktualisieren(bewerbungsVorgang);

			// Anzahl der abgeschlossenen Bewerbungsvorgänge laden
			int bewerbungen = bewerbungsVorgangDao.getAnzahl(new String[] { "bewerber_id", "status" }, new String[] { bewerbungsVorgang.getBewerberId() + "",
					"3" });

			// Liste aller Standard Dokumente holen
			LinkedList<Dokument> stdDokumente = (LinkedList<Dokument>) dokumentDao.getAlleAlsListe("standard", "1", "`id`", "ASC");
			for (Dokument dokument : stdDokumente) {
				BewerbungsVorgangDokument bvd = new BewerbungsVorgangDokument();
				bvd.setDokumentId(dokument.getId());
				bvd.setBewerbungsVorgangId(bewerbungsVorgang.getId());
				bewerbungsVorgangDokumentDao.erstellen(bvd);
			}

			// Sonderdokumente laden
			LinkedList<Dokument> extraDokumente;
			if (0 == bewerbungen) {
				// Für erstbewerber
				extraDokumente = (LinkedList<Dokument>) dokumentDao.getAlleAlsListe("standard", "2", "`id`", "ASC");
			} else {
				// Für bewerber die schon stellen hatten
				extraDokumente = (LinkedList<Dokument>) dokumentDao.getAlleAlsListe("standard", "3", "`id`", "ASC");
			}

			// Extra Dokumente speichern
			for (Dokument dokument : extraDokumente) {
				BewerbungsVorgangDokument bvd = new BewerbungsVorgangDokument();
				bvd.setDokumentId(dokument.getId());
				bvd.setBewerbungsVorgangId(bewerbungsVorgang.getId());
				bewerbungsVorgangDokumentDao.erstellen(bvd);
			}

			benachrichtigungErzeugen(SUCCESS, getText("BewerbungsVorgang.akzeptieren.erfolgreich"));

			// Nachrichten an beteiligte Personen verschicken

			versendeSystemnachricht(bewerbungsVorgang.getBearbeiterId(), bewerbungsVorgang, "BewerbungsVorgang_akzeptieren_Bearbeiter",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_akzeptieren_Bearbeiter") + bewerbungsVorgang.getAusschreibungName());

			versendeSystemnachricht(bewerbungsVorgang.getBewerberId(), bewerbungsVorgang, "BewerbungsVorgang_akzeptieren_Bewerber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_akzeptieren_Bearbeiter") + bewerbungsVorgang.getAusschreibungName());

		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang akzeptieren fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.akzeptieren.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * Bearbeiter schließt nach Abgabe aller Unterlagen den Bewerbungsvorgang ab.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String abschliessen() throws Exception {
		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang laden (aktualisieren) fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.abschliessen.vorgangLadenFehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		/*
		 * Zugriffsrechte : abschliessen darf nur der Bearbeiter & Vertreter
		 */

		try {
			List<String> zugriffsrechte = new LinkedList<String>();
			zugriffsrechte.add("bearbeiterGleichesInstitut");

			checkZugriffsrechte(zugriffsrechte);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang abschliessen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.abschliessen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		// überprüfen ob existiert
		if (0 >= bewerbungsVorgang.getBewerberId()) {
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.abschliessen.vorgangExistiertNicht"));
			return ERROR;
		}

		try {
			bewerbungsVorgang.setStatus(3);
			bewerbungsVorgangDao.aktualisieren(bewerbungsVorgang);
			benachrichtigungErzeugen(SUCCESS, getText("BewerbungsVorgang.abschliessen.erfolgreich"));

			// Überprüfen ob damit die letzte offene Stelle der Ausschreibung besetzt wurde
			Ausschreibung ausschreibung = ausschreibungDao.getEinzeln(bewerbungsVorgang.getAusschreibungId());
			if (ausschreibung.getBelegt() == ausschreibung.getStellenzahl()) {
				ausschreibung.setStatus(1);
				ausschreibungDao.aktualisieren(ausschreibung);
			}

			// Nachrichten an beteiligte Personen verschicken

			versendeSystemnachricht(bewerbungsVorgang.getAusschreiberId(), bewerbungsVorgang, "BewerbungsVorgang_abschliessen_Ausschreiber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_abschliessen_Ausschreiber") + bewerbungsVorgang.getAusschreibungName());

			versendeSystemnachricht(bewerbungsVorgang.getBewerberId(), bewerbungsVorgang, "BewerbungsVorgang_abschliessen_Bewerber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_abschliessen_Bewerber") + bewerbungsVorgang.getAusschreibungName());

		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang löschen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.abschliessen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * Aktion um eine Bewerbung als Bearbeiter oder Ausschreiber abzulehnen
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 */
	public String ablehnen() throws ZugriffsException {
		/*
		 * Zugriffsrechte : ablehnen darf nur der Ausschreiber & Vertreter
		 */

		List<String> zugriffsrechte = new LinkedList<String>();
		zugriffsrechte.add("ausschreiber");

		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang laden (ablehnen) fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.ablehnen.vorgangLadenFehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		try {
			checkZugriffsrechte(zugriffsrechte);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang ablehnen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.ablehnen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		// überprüfen ob existiert
		if (0 >= bewerbungsVorgang.getBewerberId()) {
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.ablehnen.vorgangExistiertNicht"));
			return ERROR;
		}

		try {
			bewerbungsVorgang.setStatus(4);
			bewerbungsVorgangDao.aktualisieren(bewerbungsVorgang);
			benachrichtigungErzeugen("info", getText("BewerbungsVorgang.ablehnen.erfolgreich"));

			// Nachrichten an beteiligte Personen verschicken

			versendeSystemnachricht(bewerbungsVorgang.getAusschreiberId(), bewerbungsVorgang, "BewerbungsVorgang_ablehnen_Ausschreiber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_ablehnen_Ausschreiber") + bewerbungsVorgang.getAusschreibungName());

			versendeSystemnachricht(bewerbungsVorgang.getBewerberId(), bewerbungsVorgang, "BewerbungsVorgang_ablehnen_Bewerber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_ablehnen_Bewerber") + bewerbungsVorgang.getAusschreibungName());

		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang ablehnen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.ablehnen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * Aktion um eine Bewerbung als Bewerber zurückzuziehen
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 */
	public String zurueckziehen() throws ZugriffsException {
		/*
		 * Zugriffsrechte : zurueckziehen darf nur der Bewerber
		 */
		List<String> zugriffsrechte = new LinkedList<String>();
		zugriffsrechte.add("bewerber");

		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang laden (zurueckziehen) fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.zurueckziehen.vorangLadenFehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		try {
			checkZugriffsrechte(zugriffsrechte);
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang zurueckziehen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.zurueckziehen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}

		// überprüfen ob existiert
		if (0 >= bewerbungsVorgang.getBewerberId()) {
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.zurueckziehen.vorgangExistiertNicht"));
			return ERROR;
		}

		try {
			bewerbungsVorgang.setStatus(0);
			bewerbungsVorgangDao.aktualisieren(bewerbungsVorgang);
			benachrichtigungErzeugen("info", getText("BewerbungsVorgang.zurueckziehen.erfolgreich"));

			// Nachrichten an beteiligte Personen verschicken

			versendeSystemnachricht(bewerbungsVorgang.getAusschreiberId(), bewerbungsVorgang, "BewerbungsVorgang_zurueckziehen_Ausschreiber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_zurueckziehen_Ausschreiber") + bewerbungsVorgang.getAusschreibungName());

			versendeSystemnachricht(bewerbungsVorgang.getBewerberId(), bewerbungsVorgang, "BewerbungsVorgang_zurueckziehen_Bewerber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_zurueckziehen_Bewerber") + bewerbungsVorgang.getAusschreibungName());

			// Sofern Rückzug bei Status 2 (oder -2), auch Bearbeiter benachrichtigen
			if (Math.abs(bewerbungsVorgang.getStatus()) == 2) {
				versendeSystemnachricht(bewerbungsVorgang.getBearbeiterId(), bewerbungsVorgang, "BewerbungsVorgang_zurueckziehen_Bearbeiter",
						getText("VelocityTemplate.betreff.BewerbungsVorgang_zurueckziehen_Bearbeiter") + bewerbungsVorgang.getAusschreibungName());
			}
		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang zurueckziehen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.zurueckziehen.fehlgeschlagen"));
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * Sollte eine Bewerbung durch einen Bearbeiter oder Ausschreiber aktualisiert werden (Änderungen von Daten), dann muss das durch den Bewerber akzeptiert
	 * werden bevor mit dem Vorgang weiter gemacht werden kann
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 */
	public String aenderungenAkzeptieren() throws ZugriffsException {
		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln(bewerbungsVorgangId);

			List<String> zugriffsrechte = new LinkedList<String>();
			zugriffsrechte.add("bewerber");
			checkZugriffsrechte(zugriffsrechte);

			bewerbungsVorgang.setStatus(bewerbungsVorgang.getStatus() * -1);
			bewerbungsVorgangDao.aktualisieren(bewerbungsVorgang);

			versendeSystemnachricht(bewerbungsVorgang.getAusschreiberId(), bewerbungsVorgang, "BewerbungsVorgang_aenderungenAkzeptieren_Ausschreiber",
					getText("VelocityTemplate.betreff.BewerbungsVorgang_aenderungenAkzeptieren_Ausschreiber") + bewerbungsVorgang.getAusschreibungName());

		} catch (SQLException e) {
			log.fatal("Bewerbungsvorgang aenderungenAkzeptieren fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.anderungenAkzeptieren.fehlgeschlagen"));
			return ERROR;
		}

		benachrichtigungErzeugen(SUCCESS, getText("BewerbungsVorgang.aktualisieren.erfolgreich"));

		return ERROR;
	}

	/**
	 * Zieht alle neuen bzw. laufenden Bewerbungen eines Bewerbers zurück, die Beteiligten werden darüber per Systemnachricht informiert
	 * 
	 * @param benutzerId
	 * @throws SQLException
	 */
	public void alleBewerbungsVorgaengeZurueckziehen(final int benutzerId) throws SQLException {
		List<BewerbungsVorgang> liste = bewerbungsVorgangDao.getAlleAlsListe("bewerber_id", benutzerId + "", "`id`", "ASC");

		for (BewerbungsVorgang vorgang : liste) {
			if (vorgang.getStatus() <= 2 && 0 != vorgang.getStatus()) { // alle laufenden bzw neuen vorgänge (-) (1, 2), - heißt bewerbung hat
				// zu bestätigende änderungen

				// Nachrichten an beteiligte Personen verschicken
				HashMap<String, String> contextMap = new HashMap<String, String>();

				contextMap.put("ausschreiberName", vorgang.getAusschreiberName());
				contextMap.put("bewerberName", vorgang.getBewerberName());
				contextMap.put("bearbeiterName", vorgang.getBearbeiterName());
				contextMap.put("ausschreibungName", vorgang.getAusschreibungName());

				Benutzer ausschreiber = benutzerDao.getEinzeln(vorgang.getAusschreiberId());

				Nachricht nachrichtAusschreiber = new Nachricht();
				nachrichtAusschreiber.setAbsenderId(0);
				nachrichtAusschreiber.setEmpfaengerId(vorgang.getAusschreiberId());
				nachrichtAusschreiber.setBetreff(getText("VelocityTemplate.betreff.BewerbungsVorgang_zurueckziehenAutomatisch_Ausschreiber")
						+ vorgang.getAusschreibungName());
				nachrichtAusschreiber.setText(velocityTemplateAuslesen(contextMap, ausschreiber.getNationalitaet(),
						"BewerbungsVorgang_zurueckziehenAutomatisch_Ausschreiber"));
				nachrichtDao.erstellen(nachrichtAusschreiber);

				// Sofern Rückzug bei Status 2 (oder -2), auch Bearbeiter benachrichtigen
				if (Math.abs(vorgang.getStatus()) == 2) {
					Benutzer bearbeiter = benutzerDao.getEinzeln(vorgang.getBearbeiterId());

					Nachricht nachrichtBearbeiter = new Nachricht();
					nachrichtBearbeiter.setAbsenderId(0);
					nachrichtBearbeiter.setEmpfaengerId(vorgang.getBearbeiterId());
					nachrichtBearbeiter.setBetreff(getText("VelocityTemplate.betreff.BewerbungsVorgang_zurueckziehenAutomatisch_Bearbeiter")
							+ vorgang.getAusschreibungName());
					nachrichtBearbeiter.setText(velocityTemplateAuslesen(contextMap, bearbeiter.getNationalitaet(),
							"BewerbungsVorgang_zurueckziehenAutomatisch_Bearbeiter"));
					nachrichtDao.erstellen(nachrichtBearbeiter);
				}

				vorgang.setStatus(0);
				bewerbungsVorgangDao.aktualisieren(vorgang);
			}
		}
	}

	/**
	 * Exportiert die Bewerbungsvorgänge als CSV-Datei
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String exportieren() {
		try {

			// Funktioniert nicht auf localhost

			File csvDatei = bewerbungsVorgangDao.export(sessionBenutzer.getId());

			String dateiName = csvDatei.getAbsolutePath();
			dateiName = dateiName.substring(0, dateiName.lastIndexOf(".")) + "-download.csv";

			FileReader fr = new FileReader(csvDatei.getAbsolutePath());
			BufferedReader br = new BufferedReader(fr);

			BufferedWriter bw = new BufferedWriter(new FileWriter(dateiName));

			String line = "Ausschreiber Nachname,Ausschreiber Vorname,Bewerber Nachname,Bewerber Vorname,Matrikelnummer,Beginn,Ende,Std/Monat,Abgeschlossen";
			do {
				bw.write(line);
				bw.newLine();

				line = br.readLine();
			} while (line != null);

			bw.close();

			csvDatei = new File(dateiName);

			fileInputStream = new FileInputStream(csvDatei);

		} catch (Exception e) {
			log.fatal("Bewerbungsvorgang exportieren fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("BewerbungsVorgang.exportieren.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Validiert die Eingaben beim Erstellen einer Bewerbung
	 * 
	 * @throws Exception
	 */
	public void validateErstellen() throws Exception {
		log.debug("validateErstellen aufgerufen");
		if (bewerbungsVorgang != null) {
			Ausschreibung template = ausschreibungDao.getEinzeln(bewerbungsVorgang.getAusschreibungId());

			bewerbungsVorgang.setBewerberId(sessionBenutzer.getId());
			bewerbungsVorgang.setBewerberName(sessionBenutzer.getName());
			bewerbungsVorgang.setAusschreiberId(template.getAusschreiberId());
			bewerbungsVorgang.setInstitut(template.getInstitut());
			bewerbungsVorgang.setAusschreiberName(template.getAusschreiberName());
			bewerbungsVorgang.setBearbeiterId(template.getBearbeiterId());
			bewerbungsVorgang.setBearbeiterName(template.getBearbeiterName());
			bewerbungsVorgang.setAusschreibungId(template.getId());
			bewerbungsVorgang.setAusschreibungName(template.getName());
			bewerbungsVorgang.setStundenzahl(template.getStundenzahl());
			bewerbungsVorgang.setStartet(template.getStartet());
			bewerbungsVorgang.setEndet(template.getEndet());
			bewerbungsVorgang.setStatus(1);

			log.debug("validateErstellen begonnen");
			// alles andere ist hier unnötig, da das aus der DB direkt kopiert wird
			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			bewerbungsVorgang.setKommentar(Jsoup.clean(bewerbungsVorgang.getKommentar(), "", Whitelist.basic(), d).trim());
		}

		logFieldErrors();
	}

	/**
	 * Validiert die Eingaben beim Aktualisieren einer Bewerbung
	 * 
	 * @throws Exception
	 */
	public void validateAktualisieren() throws Exception {
		log.debug("validateAktualisieren aufgerufen");

		// Die Bearbeiter werden nur nach dem Institut des Ausschreibers angezeigt
		// sollte in unserer Umgebung also nicht mehr als 50 pro Liste entstehen
		try {
			String[] spalteBearbeiterSelect = { "gruppe_id", "institut" };
			int institut = sessionBenutzer.getInstitut();
			setBearbeiterSelect((LinkedList<Benutzer>) benutzerDao.getAlleAlsListe(spalteBearbeiterSelect, new String[] { "4", institut + "" }, "`nachname`",
					richtung));
		} catch (SQLException e) {
			log.fatal("Ausschreibung Index: Benutzerliste (Bearbeiter) erstellen fehlgeschlagen", e);
		}

		if (bewerbungsVorgang != null) {
			log.debug("validateAktualisieren begonnen");

			if (sessionBenutzer.getGruppeId() == 2) {
				fieldErrorsEntfernen();
				Document.OutputSettings d = new Document.OutputSettings();
				d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
				bewerbungsVorgang.setKommentar(Jsoup.clean(bewerbungsVorgang.getKommentar(), "", Whitelist.basic(), d).trim());
			} else {
				fieldErrorsEntfernen("bewerbungsVorgang", new String[] { "ausschreibungId", "bewerberId", "ausschreiberId", "institut" });

				Benutzer bearbeiter = benutzerDao.getEinzeln(bewerbungsVorgang.getBearbeiterId());
				if (null == bearbeiter || 4 != benutzerDao.getEinzeln(bewerbungsVorgang.getBearbeiterId()).getGruppeId()) {
					addFieldError("bewerbungsVorgang.bearbeiterId",
							this.getText("BewerbungsVorgang.aktualisieren.bearbeiterId.keinenZulaessigenBearbeiterAusgewaehlt"));
				}

				if (!(bewerbungsVorgang.getEndet() instanceof Date)) {
					addFieldError("bewerbungsVorgang.endet", this.getText("bewerbungsVorgang.endet.ungueltig"));
				}
			}
		}

		logFieldErrors();
	}

	/**
	 * @return the bewerbungsVorgang
	 */
	public BewerbungsVorgang getBewerbungsVorgang() {
		return bewerbungsVorgang;
	}

	/**
	 * @param bewerbungsVorgang
	 *            the bewerbungsVorgang to set
	 */
	public void setBewerbungsVorgang(final BewerbungsVorgang bewerbungsVorgang) {
		this.bewerbungsVorgang = bewerbungsVorgang;
	}

	/**
	 * @return the bewerbungsVorgangId
	 */
	public int getBewerbungsVorgangId() {
		return bewerbungsVorgangId;
	}

	/**
	 * @param bewerbungsVorgangId
	 *            the bewerbungsVorgangId to set
	 */
	public void setBewerbungsVorgangId(final int bewerbungsVorgangId) {
		this.bewerbungsVorgangId = bewerbungsVorgangId;
	}

	/**
	 * @return the bewerbungsVorgangListen
	 */
	public HashMap<String, LinkedList<BewerbungsVorgang>> getBewerbungsVorgangListen() {
		return bewerbungsVorgangListen;
	}

	/**
	 * @return the bewerbungsVorgangListenAnzahlen
	 */
	public HashMap<String, Integer> getBewerbungsVorgangListenAnzahlen() {
		return bewerbungsVorgangListenAnzahlen;
	}

	/**
	 * @return the ausschreibungId
	 */
	public int getAusschreibungId() {
		return ausschreibungId;
	}

	/**
	 * @param ausschreibungId
	 *            the ausschreibungId to set
	 */
	public void setAusschreibungId(final int ausschreibungId) {
		this.ausschreibungId = ausschreibungId;
	}

	/**
	 * @return the bewerbungsVorgangDokumenteListen
	 */
	public HashMap<String, LinkedList<BewerbungsVorgangDokument>> getBewerbungsVorgangDokumenteListen() {
		return bewerbungsVorgangDokumenteListen;
	}

	/**
	 * @return the bewerbungsVorgangDokumenteListenAnzahlen
	 */
	public HashMap<String, Integer> getBewerbungsVorgangDokumenteListenAnzahlen() {
		return bewerbungsVorgangDokumenteListenAnzahlen;
	}

	public FileInputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(final FileInputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
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

	public int[] getMultiBewerbungsVorgangDokumentId() {
		return multiBewerbungsVorgangDokumentId;
	}

	public void setMultiBewerbungsVorgangDokumentId(final int[] multiBewerbungsVorgangDokumentId) {
		this.multiBewerbungsVorgangDokumentId = multiBewerbungsVorgangDokumentId;
	}

}
