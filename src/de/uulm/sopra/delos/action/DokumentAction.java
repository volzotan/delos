package de.uulm.sopra.delos.action;

import java.sql.SQLException;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;

import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.bean.BewerbungsVorgangDokument;
import de.uulm.sopra.delos.bean.Dokument;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDao;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDao;
import de.uulm.sopra.delos.dao.DokumentDao;

/**
 * Jegliche Aktionen, die die Dokumente betreffen.
 */
public class DokumentAction extends BasisAction {

	private static final long					serialVersionUID					= -5766755091824939140L;
	// Dokumentobjekt zum Laden von Formulardaten beim abschicken bzw. anzeigen bei sonstigten Operationen
	private Dokument							dokument;
	// verschiedene weitere Objekte, deren Daten angezeigt werden
	private BewerbungsVorgang					bewerbungsVorgang					= new BewerbungsVorgang();
	private BewerbungsVorgangDokument			bewerbungsVorgangDokument			= new BewerbungsVorgangDokument();

	// Datenbankzugriffsobjekte für verschiedene Beans, deren Daten von den Methoden benötigt werden
	private final DokumentDao					dokumentDao							= new DokumentDao();
	private final BewerbungsVorgangDokumentDao	bewerbungsVorgangDokumentDao		= new BewerbungsVorgangDokumentDao();
	private final BewerbungsVorgangDao			bewerbungsVorgangDao				= new BewerbungsVorgangDao();

	// Id für den spezifischen Zugriff auf verschiedene Objekte
	private int									dokumentId;
	private int									bewerbungsVorgangId;
	private int									bewerbungsVorgangDokumentId;
	private int									bewerberId;
	private int									ausschreibungId;

	private LinkedList<?>						listeDaten;

	// Arrays zur mehrfachen Auswahl mittels Checkboxen
	private int[]								multiBewerbungsVorgangDokumentId	= new int[0];
	private int[]								multiDokumentId						= new int[0];

	// Zielangabe zum Abschicken des AusschreibungFormulars (entweder Dokument_aktualisieren oder Dokument_erstellen)
	private String								formularZiel;

	@Override
	public void prepare() throws Exception {
		super.prepare();

		listeSortSpalten.put("id", "`id`");
		listeSortSpalten.put("name", "`name`");
		listeSortSpalten.put("standard", "`standard`");
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		if (null == spalte || null == listeSortSpalten.get(spalte)) {
			spalte = "name";
		}

		fieldErrorsDuplikateEntfernen();
	}

	/**
	 * liest alle Dokumente aus der Datenbank, um diese in der Übersicht anzuzeigen
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String index() {
		try {
			listeDaten = (LinkedList<Dokument>) dokumentDao.getAlleAlsListe(seite, spalte, richtung);
			listeAnzahlElemente = dokumentDao.getAnzahl();
		} catch (SQLException e) {
			log.fatal("Dokument index fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Speichert die Daten eines neuen Dokumentes in die Datenbank
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String erstellen() {
		if (null == dokument) {
			return INPUT;
		} else {
			System.out.println(dokument.getName());
			try {
				dokumentDao.erstellen(dokument);
			} catch (SQLException e) {
				log.fatal("Dokument speichern fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.speichern.fehlgeschlagen"));
				return ERROR;
			}
		}

		this.benachrichtigungErzeugen(SUCCESS, this.getText("Dokument.erstellen.erfolgreich"));

		return SUCCESS;
	}

	/**
	 * validiert die Eingaben beim Erstellen eines neuen Dokumentes
	 * 
	 * @throws Exception
	 */
	public void validateErstellen() throws Exception {
		formularZiel = "Dokument_erstellen";
		if (null != dokument) {
			// Illegale Eingaben entfernen
			LinkedList<String> check = new LinkedList<String>();
			check.add("name");
			checkBeanHTML(dokument, check);

			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			dokument.setBeschreibung(Jsoup.clean(dokument.getBeschreibung(), "", Whitelist.basic(), d).trim());
			if (0 > dokument.getBeschreibung().length()) {
				addFieldError("dokument.beschreibung", this.getText("dokument.beschreibung.leer"));
			}
		}
	}

	/**
	 * Holt das Dokument mit this.dokumentId als Id aus der Datenbank, damit dieses angezeigt werden kann.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String anzeigen() {
		try {
			dokument = dokumentDao.getEinzeln("id", dokumentId + "");
		} catch (SQLException e) {
			log.fatal("Dokument anzeigen fehlgeschlagen" + e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.anzeigen.fehlgeschlagen"));
			return ERROR;
		}

		if (0 < dokument.getId()) { return SUCCESS; }

		this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.anzeigen.fehlgeschlagen"));
		return ERROR;
	}

	/**
	 * Holt das Dokument mit this.dokumentId als Id aus der Datenbank, damit dieses im Bearbeitungsformular angezeigt werden kann.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String aktualisieren() {
		if (null == dokument) {
			try {
				dokument = dokumentDao.getEinzeln("id", dokumentId + "");
				if (0 < dokument.getId()) { return INPUT; }
			} catch (SQLException e) {
				log.fatal("Dokument laden (aktualisieren) fehlgeschlagen" + e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.aktualisieren.dokumentLadenFehlgeschlagen"));
				return ERROR;
			}
		} else {
			try {
				dokumentDao.aktualisieren(dokument);
			} catch (SQLException e) {
				log.fatal("Dokument speichern fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.speichern.fehlgeschlagen"));
				return ERROR;
			}
			return SUCCESS;
		}

		return ERROR;
	}

	/**
	 * validiert die Eingaben beim Aktualisieren eines bestehenden Dokumentes
	 * 
	 * @throws Exception
	 */
	public void validateAktualisieren() throws Exception {
		formularZiel = "Dokument_aktualisieren";
		if (null != dokument) {
			// Illegale Eingaben entfernen
			LinkedList<String> check = new LinkedList<String>();
			check.add("name");
			checkBeanHTML(dokument, check);

			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			dokument.setBeschreibung(Jsoup.clean(dokument.getBeschreibung(), "", Whitelist.basic(), d).trim());
			if (0 > dokument.getBeschreibung().length()) {
				addFieldError("dokument.beschreibung", this.getText("dokument.beschreibung.leer"));
			}
		}
	}

	/**
	 * Holt das Dokument mit this.dokumentId als Id aus der Datenbank, damit dieses gelöscht werden kann.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String loeschen() {
		try {
			dokument = dokumentDao.getEinzeln("id", dokumentId + "");
		} catch (SQLException e) {
			log.fatal("Dokument laden fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.loeschen.fehlgeschlagen"));
			return ERROR;
		}
		if (0 < dokument.getId()) {
			try {
				dokumentDao.loeschen(dokument);
			} catch (SQLException e) {
				log.fatal("Dokument loeschen fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.loeschen.fehlgeschlagen"));
				return ERROR;
			}
		}
		return index();
	}

	/**
	 * Löscht ein bestehendes BewerbungsVorgangDokument aus der Datenbank
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String bewerbungsVorgangDokumentLoeschen() {
		try {
			bewerbungsVorgangDokument = bewerbungsVorgangDokumentDao.getEinzeln("id", bewerbungsVorgangDokumentId + "");
		} catch (SQLException e) {
			log.fatal("BewerbungsVorgangDokument laden fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.loeschen.fehlgeschlagen"));
			return ERROR;
		}
		if (0 < bewerbungsVorgangDokument.getId()) {
			try {
				bewerbungsVorgangDokumentDao.loeschen(bewerbungsVorgangDokument);
			} catch (SQLException e) {
				log.fatal("BewerbungsVorgangDokument loeschen fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.loeschen.fehlgeschlagen"));
				return ERROR;
			}
		}
		return benoetigteDokumenteBearbeiter();
	}

	/**
	 * Lädt die benoetigten BewerbungsVorgangDokumente und die nicht benötigten Dokumente für einen bestimmten BewerbungsVorgang aus der Datenbank, damit diese
	 * einem Bearbeiter angezeigt werden können und von ihm bearbeitet werden können.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String benoetigteDokumenteBearbeiter() {
		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln("id", bewerbungsVorgangId + "");
		} catch (SQLException e) {
			log.fatal("BewerbungsVorgang laden fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.meinedokumenteBearbeiter.fehlgeschlagen"));
			return ERROR;
		}
		try {
			listeDaten = (LinkedList<BewerbungsVorgangDokument>) bewerbungsVorgangDokumentDao.getAlleAlsListe("bewerbungs_vorgang_id",
					bewerbungsVorgangId + "", seite, "`id`", richtung);
		} catch (SQLException e) {
			log.fatal("benoetigte Dokumente laden fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.meinedokumenteBearbeiter.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	public String unbenoetigteDokumenteBearbeiter() {
		try {
			bewerbungsVorgang = bewerbungsVorgangDao.getEinzeln("id", bewerbungsVorgangId + "");
		} catch (SQLException e) {
			log.fatal("BewerbungsVorgang laden fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.meinedokumenteBearbeiter.fehlgeschlagen"));
			return ERROR;
		}
		try {
			listeDaten = (LinkedList<Dokument>) dokumentDao.getNichtBenoetigteDokumenteAlsListe(bewerbungsVorgangId);
		} catch (SQLException e) {
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.meinedokumenteBearbeiter.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * ändert den Status der in den Checkboxen angeklickten BewerbungsVorgangDokumente
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String multiCheckBoxAuswertung() {
		try {
			bewerbungsVorgangDokumentDao.aktualisieren(multiBewerbungsVorgangDokumentId, bewerbungsVorgangId);
			this.benachrichtigungErzeugen(SUCCESS, this.getText("Dokument.speichern.erfolgreich"));
		} catch (SQLException e) {
			log.fatal("Dokument speichern fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.speichern.fehlgeschlagen"));
			return ERROR;
		}
		return benoetigteDokumenteBearbeiter();
	}

	/**
	 * Fügt neue BewerbungsVorgangDokumente entsprechend der Multicheckbox hinzu.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String multiCheckBoxAuswertungHinzufuegen() {
		try {
			for (int element : multiDokumentId) {
				BewerbungsVorgangDokument bvd = new BewerbungsVorgangDokument();
				bvd.setDokumentId(element);
				bvd.setBewerbungsVorgangId(bewerbungsVorgangId);
				bvd.setStatus(0);
				bewerbungsVorgangDokumentDao.erstellen(bvd);
			}
			this.benachrichtigungErzeugen(SUCCESS, this.getText("Dokument.speichern.erfolgreich"));
		} catch (SQLException e) {
			log.fatal("Dokument speichern fehlgeschlagen", e);
			this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.speichern.fehlgeschlagen"));
			return ERROR;
		}
		return benoetigteDokumenteBearbeiter();
	}

	/**
	 * Löscht Dokumente entsprechend der Multicheckbox
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String multiCheckBoxAuswertungLoeschen() {
		for (int element : multiDokumentId) {
			try {
				dokument = dokumentDao.getEinzeln("id", element + "");
			} catch (SQLException e) {
				log.fatal("Dokument laden fehlgeschlagen", e);
				this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.loeschen.fehlgeschlagen"));
				return ERROR;
			}
			if (0 < dokument.getId()) {
				try {
					dokumentDao.loeschen(dokument);
				} catch (SQLException e) {
					log.fatal("Dokument loeschen fehlgeschlagen", e);
					this.benachrichtigungErzeugen(ERROR, this.getText("Dokument.loeschen.fehlgeschlagen"));
					return ERROR;
				}
			}
		}
		return index();
	}

	/*
	 * Getters und Setters
	 */

	/**
	 * @return the dokument
	 */
	public Dokument getDokument() {
		return dokument;
	}

	/**
	 * @param dokument
	 *            the dokument to set
	 */
	public void setDokument(final Dokument dokument) {
		this.dokument = dokument;
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
	 * @return the bewerbungsVorgangDokument
	 */
	public BewerbungsVorgangDokument getBewerbungsVorgangDokument() {
		return bewerbungsVorgangDokument;
	}

	/**
	 * @param bewerbungsVorgangDokument
	 *            the bewerbungsVorgangDokument to set
	 */
	public void setBewerbungsVorgangDokument(final BewerbungsVorgangDokument bewerbungsVorgangDokument) {
		this.bewerbungsVorgangDokument = bewerbungsVorgangDokument;
	}

	/**
	 * @return the dokumentId
	 */
	public int getDokumentId() {
		return dokumentId;
	}

	/**
	 * @param dokumentId
	 *            the dokumentId to set
	 */
	public void setDokumentId(final int dokumentId) {
		this.dokumentId = dokumentId;
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
	 * @return the bewerbungsVorgangDokumentId
	 */
	public int getBewerbungsVorgangDokumentId() {
		return bewerbungsVorgangDokumentId;
	}

	/**
	 * @param bewerbungsVorgangDokumentId
	 *            the bewerbungsVorgangDokumentId to set
	 */
	public void setBewerbungsVorgangDokumentId(final int bewerbungsVorgangDokumentId) {
		this.bewerbungsVorgangDokumentId = bewerbungsVorgangDokumentId;
	}

	/**
	 * @return the bewerberId
	 */
	public int getBewerberId() {
		return bewerberId;
	}

	/**
	 * @param bewerberId
	 *            the bewerberId to set
	 */
	public void setBewerberId(final int bewerberId) {
		this.bewerberId = bewerberId;
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
	 * @return the multiBewerbungsVorgangDokumentId
	 */
	public int[] getMultiBewerbungsVorgangDokumentId() {
		return multiBewerbungsVorgangDokumentId;
	}

	/**
	 * @param multiBewerbungsVorgangDokumentId
	 *            the multiBewerbungsVorgangDokumentId to set
	 */
	public void setMultiBewerbungsVorgangDokumentId(final int[] multiBewerbungsVorgangDokumentId) {
		this.multiBewerbungsVorgangDokumentId = multiBewerbungsVorgangDokumentId;
	}

	/**
	 * @return the multiDokumentId
	 */
	public int[] getMultiDokumentId() {
		return multiDokumentId;
	}

	/**
	 * @param multiDokumentId
	 *            the multiDokumentId to set
	 */
	public void setMultiDokumentId(final int[] multiDokumentId) {
		this.multiDokumentId = multiDokumentId;
	}

	/**
	 * @return the formularZiel
	 */
	public String getFormularZiel() {
		return formularZiel;
	}

	/**
	 * @param formularZiel
	 *            the formularZiel to set
	 */
	public void setFormularZiel(final String formularZiel) {
		this.formularZiel = formularZiel;
	}

	/**
	 * @return the listeDaten
	 */
	public LinkedList<?> getListeDaten() {
		return listeDaten;
	}

	/**
	 * @param listeDaten
	 *            the listeDaten to set
	 */
	public void setListeDaten(final LinkedList<?> listeDaten) {
		this.listeDaten = listeDaten;
	}
}