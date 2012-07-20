package de.uulm.sopra.delos.action;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.EmailValidator;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.opensymphony.xwork2.ActionContext;

import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.bean.Nachricht;
import de.uulm.sopra.delos.dao.BenutzerDao;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDao;
import de.uulm.sopra.delos.dao.NachrichtDao;
import de.uulm.sopra.delos.system.BCrypt;
import de.uulm.sopra.delos.system.Mailer;
import de.uulm.sopra.delos.system.ZugriffsException;

/**
 * Jegliche Aktionen, die Benutzer betreffen.
 */
public class BenutzerAction extends BasisAction {

	private static final long			serialVersionUID		= 7805632989412532335L;
	// Benutzerobjekt zum Laden von Formulardaten beim abschicken bzw. anzeigen bei sonstigten Operationen
	private Benutzer					benutzer;
	// Datenbankzugriffsobjekt
	private final BenutzerDao			benutzerDao				= new BenutzerDao();
	private final NachrichtDao			nachrichtDao			= new NachrichtDao();
	private final BewerbungsVorgangDao	bewerbungsVorgangDao	= new BewerbungsVorgangDao();
	// URL parameter zum laden von spezifischen Benutzern
	private int							benutzerId;

	// Zweites Passwort zur Bestätigung von Passworteingaben im BenutzerFormular
	private String						passwortBestaetigen;

	private String						vertreterMail;

	// Listencontainer für Benutzerlisten, die auf der Indexoperation erzeugt werden.
	private LinkedList<Benutzer>		listeDaten				= new LinkedList<Benutzer>();
	private Map<Integer, String>		laenderMap				= new HashMap<Integer, String>();
	private Map<Integer, String>		nationalitaetenMap		= new HashMap<Integer, String>();
	private Map<Integer, String>		studiengaengeMap		= new HashMap<Integer, String>();

	// Checkbox beim Registrieren, notwendig, um einen Account anlegen zu dürfen
	private boolean						agbCheckbox;

	// suchmaske im index
	private String						suche;

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.action.BasisAction#prepare()
	 */
	@Override
	public void prepare() throws Exception {
		super.prepare();

		// Selectboxen für Länder und Nationalitäten vorbereiten
		mapProperties();

		// Legt die Spalten fest, nach denen sortiert werden darf
		listeSortSpalten.put("id", "`id`");
		listeSortSpalten.put("gruppe", "`gruppe_id`");
		listeSortSpalten.put("email", "`email`");
		listeSortSpalten.put("geschlecht", "`geschlecht`");
		listeSortSpalten.put("vname", "`vorname`");
		listeSortSpalten.put("name", "`nachname`");
		listeSortSpalten.put("hin", "`hinzugefuegt`");
		listeSortSpalten.put("mod", "`bearbeitet`");
	}

	/**
	 * Erzeugt die Übersichtsseite über alle Benutzer
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String index() {
		benutzer = new Benutzer();

		try {
			// Liste aller Benutzer
			listeDaten = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe(seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = benutzerDao.getAnzahl();
		} catch (SQLException e) {
			log.fatal("Benutzer index fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Erzeugt die Übersichtsseite für die Suchergebnisse
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String suche() {

		try {
			// Liste der Suchergebnisse
			if (null != suche && 0 < suche.length()) {
				listeDaten = (LinkedList<Benutzer>) benutzerDao.getSuchergebnisseAlsListe(suche, seite, listeSortSpalten.get(spalte),
						sortierungen.get(richtung));
				listeAnzahlElemente = benutzerDao.getAnzahlSuchergebnisse(suche);
			}
		} catch (SQLException e) {
			log.fatal("Benutzer index fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Erzeugt die Übersichtsseite für Admins
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexAdministratoren() {
		benutzer = new Benutzer();

		try {
			// Liste der Admins
			listeDaten = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe("gruppe_id", "1", seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = benutzerDao.getAnzahl("gruppe_id", "1");
		} catch (SQLException e) {
			log.fatal("Benutzer index Admin fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Erzeugt die Übersichtsseite für Ausschreiber
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexAusschreiber() {
		benutzer = new Benutzer();

		try {
			// Liste der Ausschreiber
			listeDaten = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe("gruppe_id", "3", seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = benutzerDao.getAnzahl("gruppe_id", "3");
		} catch (SQLException e) {
			log.fatal("Benutzer index Ausschreiber fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Erzeugt die Übersichtsseite für Bearbeiter
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBearbeiter() {
		benutzer = new Benutzer();

		try {
			// Liste der Bearbeiter
			listeDaten = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe("gruppe_id", "4", seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = benutzerDao.getAnzahl("gruppe_id", "4");
		} catch (SQLException e) {
			log.fatal("Benutzer index Bearbeiter fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Erzeugt die Übersichtsseite für Bewerber
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexBewerber() {
		benutzer = new Benutzer();

		try {
			// Liste der Bewerber
			listeDaten = (LinkedList<Benutzer>) benutzerDao.getAlleAlsListe("gruppe_id", "2", seite, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = benutzerDao.getAnzahl("gruppe_id", "2");
		} catch (SQLException e) {
			log.fatal("Benutzer index Bewerber fehlgeschlagen" + e);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Zeigt das Registrierungsformular für Gäste (und Admins) an und speichert zulässige Eingaben im Formular als neue Benutzer
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String registrieren() {
		// Wenn Benutzer eingeloggt, zurück zur Startseite
		if (sessionBenutzer.getGruppeId() > 1) { // Admin muss neue Benutzer registrieren können
			benachrichtigungErzeugen("info", getText("Benutzer.registrieren.bereitsEingeloggt"));
			return ERROR;
		}
		if (benutzer == null) {
			benutzer = new Benutzer();
			log.debug("Keine Formulardaten gefunden -> Rückgabe Registrierungsformular");

			return INPUT;
		}

		try {
			if (sessionBenutzer.getGruppeId() == 1) { // wenn Admin Benutzer anlegt, Passwort generieren lassen
				Mailer mailer = new Mailer();
				String neuesPasswort = mailer.passwortGenerieren();
				benutzer.setPasswort(passwortHashen(neuesPasswort));

				// passwort schicken
				mailer.passwortVerschicken(benutzer.getEmail(), neuesPasswort);
			} else {
				benutzer.setPasswort(passwortHashen(benutzer.getPasswort()));
			}
			benutzerDao.erstellen(benutzer);
			benutzer = benutzerDao.getEinzeln("email", benutzer.getEmail());
		} catch (Exception e) {
			log.fatal("Benutzer Registrierung fehlgeschlagen", e);
			benachrichtigungErzeugen(ERROR, getText("Benutzer.registrieren.fehlgeschlagen"));
			return INPUT;
		}

		log.info("Benutzer " + benutzer + " erfolgreich registriert");

		// Wenn ein Admin den Benutzer angelegt hat, einfach fertig
		if (sessionBenutzer.getId() != 0) {
			benachrichtigungErzeugen(SUCCESS, getText("Benutzer.registrieren.erfolgreichAdmin"));
			return "benutzerIndex";
		}

		// Wenn kein Admin den Benutzer angelegt hat, muss das Benutzerobjekt in die Session damit man direkt angemeldet wird

		benachrichtigungErzeugen(SUCCESS, getText("Benutzer.registrieren.erfolgreich"));

		// Benutzer einloggen
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.put("angemeldeterBenutzer", benutzer);
		session.put("loggedIn", true);
		log.info("Benutzer angemeldet: " + sessionBenutzer);

		return "startseite";
	}

	/**
	 * Lädt den Benutzer, der über den {@code benutzerId} URL-Parameter angegeben wurde und zeigt diesen, sofern er exisiert, an.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 *             bei aufruf mit unzureichenden Zugriffsrechten
	 */
	public String anzeigen() throws ZugriffsException {
		try {
			benutzer = benutzerDao.getEinzeln(benutzerId);

			// falls gelöschter Benutzer
			if (benutzer.getEmail().equals("") || benutzer.getEmail().equals(" ")) {
				benachrichtigungErzeugen(ERROR, getText("Benutzer.geloescht"));
				return ERROR;
			}

			if (sessionBenutzer.getGruppeId() == 2 && benutzer != null && benutzer.getGruppeId() == 2 && benutzer.getId() != sessionBenutzer.getId()) { throw new ZugriffsException(); }

			if (benutzer.getGruppeId() >= 3 && benutzer.getVertreterId() > 0 && benutzerDao.getEinzeln(benutzer.getVertreterId()) != null) {
				if (!benutzerDao.getEinzeln(benutzer.getVertreterId()).getEmail().equals("")) {
					vertreterMail = benutzerDao.getEinzeln(benutzer.getVertreterId()).getEmail();
				}
			}

			if (benutzer == null) {
				benachrichtigungErzeugen(ERROR, getText("Benutzer.anzeigen.fehlgeschlagen"));
				return ERROR;
			}
		} catch (SQLException e) {
			log.fatal("Benutzer anzeigen fehlgeschlagen" + e);
			benachrichtigungErzeugen(ERROR, getText("Benutzer.anzeigen.fehlgeschlagen"));
			// zurück zur aufrufenden Seite wenn vorhanden (sonst Startseite)
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Zeigt das Formular zum Bearbeiten bzw. Aktualisieren des Benutzers {@code benutzerId} an und speichert anschließend nach dem Abschicken des Formulars die
	 * Eingaben in der Datenbank
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 *             bei aufruf mit unzureichenden Zugriffsrechten
	 */
	public String aktualisieren() throws ZugriffsException {
		// wenn aktualisierung von Admin durchgenommen (und nicht auf sich selbst ausgeführt)
		// muss benutzer.gruppeId in var geschrieben und benutzerobjekt neu aus DB geladen werden

		/*
		 * Zugriffskontrolle
		 */

		if (sessionBenutzer.getGruppeId() != 1) {
			if (benutzer == null) {
				if ((sessionBenutzer.getId() != benutzerId)) { throw new ZugriffsException(); }
			} else {
				if (sessionBenutzer.getId() != benutzer.getId()) { throw new ZugriffsException(); }
			}
		}

		// Vertreter darf keine Profildaten ändern

		if (((Boolean) session.get("istVertreter"))) {
			benachrichtigungErzeugen("info", getText("Benutzer.aktualisieren.vertreterZugriffsverbot"));
			throw new ZugriffsException();

		}

		// falls Benutzer_aktualisieren ohne URL-Parameter aufgerufen wird
		if ((benutzerId <= 0) && (benutzer == null)) {
			log.fatal("Benutzer aktualisieren fehlgeschlagen (URL-Parameter fehlt)");
			benachrichtigungErzeugen(ERROR, getText("Benutzer.aktualisieren.keinBenutzerAngegeben"));
			return "404";
		}

		// benutzer aus DB laden und damit Formular ausfüllen
		if (benutzer == null) {
			try {
				benutzer = benutzerDao.getEinzeln(benutzerId);

				if (benutzer.getGruppeId() >= 3 && benutzer.getVertreterId() > 0 && benutzerDao.getEinzeln(benutzer.getVertreterId()) != null) {
					vertreterMail = benutzerDao.getEinzeln(benutzer.getVertreterId()).getEmail();
				}
			} catch (Exception e) {
				log.fatal("Benutzer laden (aktualisieren) fehlgeschlagen" + e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.aktualisieren.benutzerLadenFehlgeschlagen"));
				return ERROR;
			}

			if (benutzer == null) {
				log.fatal("Zu aktualisierenden Benutzer gibt es nicht");
				benachrichtigungErzeugen(ERROR, getText("Benutzer.aktualisieren.benutzerNichtGefunden"));
				return ERROR;
			}

			// falls gelöschter Benutzer
			if (benutzer.getEmail().equals("") || benutzer.getEmail().equals(" ")) {
				benachrichtigungErzeugen(ERROR, getText("Benutzer.geloescht"));
				return ERROR;
			}

			return INPUT;

		}

		// abgeschickten benutzer speichern

		// falls gelöschter Benutzer
		if (benutzer.getEmail().equals("") || benutzer.getEmail().equals(" ")) {
			benachrichtigungErzeugen(ERROR, getText("Benutzer.geloescht"));
			return ERROR;
		}

		// falls Passwort nicht erneut eingeben wurde, kein leeres abspeichern
		if (benutzer.getPasswort() == null || benutzer.getPasswort().equals("")) {
			try {
				Benutzer abgespeicherterBenutzer;
				abgespeicherterBenutzer = benutzerDao.getEinzeln(benutzer.getId());
				benutzer.setPasswort(abgespeicherterBenutzer.getPasswort());
			} catch (SQLException e) {
				log.fatal("Benutzer aktualisieren fehlgeschlagen" + e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.aktualisieren.aktualisierenFehlgeschlagen"));
				return ERROR;
			}
		} else {
			benutzer.setPasswort(passwortHashen(benutzer.getPasswort()));
		}

		// Fall: Admin aktualisiert fremdes Benutzerkonto
		if (sessionBenutzer.getId() == 1 && sessionBenutzer.getId() != benutzer.getId()) {
			try {
				int gruppeId = benutzer.getGruppeId();
				benutzer = benutzerDao.getEinzeln(benutzer.getId());
				benutzer.setGruppeId(gruppeId);
				benutzerDao.aktualisieren(benutzer);
				benachrichtigungErzeugen(SUCCESS, getText("Benutzer.aktualisieren.aktualisierenErfolgreich"));
				log.info("Benutzer " + benutzer + " aktualisiert");

				// fertig
			} catch (Exception e) {
				log.fatal("Benutzer aktualisieren fehlgeschlagen" + e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.aktualisieren.aktualisierenFehlgeschlagen"));
				return ERROR;
			}
		} else {
			try {
				// Vertreter
				if (benutzer.getGruppeId() >= 3) {
					Benutzer abgespeicherterBenutzer;
					abgespeicherterBenutzer = benutzerDao.getEinzeln(benutzer.getId());

					Benutzer vertreter = benutzerDao.getEinzeln("email", vertreterMail);
					// nur bei Änderung des Vertreters gegenüber dem vorherigen Zustand die Nachricht verschicken
					if (null != vertreter && abgespeicherterBenutzer.getVertreterId() != vertreter.getId()) {
						benutzer.setVertreterId(vertreter.getId());

						// Nachricht an Vertreter schicken
						Nachricht nachrichtVertreter = new Nachricht();
						HashMap<String, String> contextMap = new HashMap<String, String>();

						nachrichtVertreter.setAbsenderId(0);
						nachrichtVertreter.setEmpfaengerId(vertreter.getId());
						nachrichtVertreter.setBetreff(getText("VelocityTemplate.betreff.Benutzer_aktualisieren_Vertreter") + benutzer.getName());
						contextMap.put("vertreterName", vertreter.getName());
						contextMap.put("benutzerName", benutzer.getName());
						contextMap.put("benutzerEmail", benutzer.getEmail());
						nachrichtVertreter.setText(velocityTemplateAuslesen(contextMap, vertreter.getNationalitaet(), "Benutzer_aktualisieren_Vertreter"));
						nachrichtDao.erstellen(nachrichtVertreter);
					}
				}

				benutzerDao.aktualisieren(benutzer);
				benachrichtigungErzeugen(SUCCESS, getText("Benutzer.aktualisieren.aktualisierenErfolgreich"));
			} catch (SQLException e) {
				log.fatal("Benutzer aktualisieren fehlgeschlagen" + e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.aktualisieren.aktualisierenFehlgeschlagen"));
				return ERROR;
			}
		}
		benutzerId = benutzer.getId();
		return SUCCESS;

	}

	/**
	 * Loescht einen Benutzer {@code benutzerId} aus der Datenbank
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 * @throws ZugriffsException
	 *             bei aufruf mit unzureichenden Zugriffsrechten
	 */
	public String loeschen() throws ZugriffsException {
		// Muss Admin oder Benutzer selbst sein
		if (benutzerId != sessionBenutzer.getId() && sessionBenutzer.getGruppeId() != 1) { throw new ZugriffsException(); }

		// ( Zu löschender Benutzer muss existieren )
		try {
			benutzer = benutzerDao.getEinzeln(benutzerId);
		} catch (SQLException e) {
			log.fatal("Benutzer abrufen fehlgeschlagen" + e);
			benachrichtigungErzeugen(ERROR, getText("Benutzer.loeschen.fehlgeschlagen"));
			return ERROR;
		}

		try {
			benutzerDao.loeschen(benutzer);
		} catch (SQLException e) {
			if (e.getErrorCode() == 1451) { // MySQLIntegrityConstraintViolationException

				Benutzer leererBenutzer = new Benutzer();
				leererBenutzer.setVorname("gelöschter");
				leererBenutzer.setNachname("Benutzer");
				leererBenutzer.setEmail("");
				leererBenutzer.setId(benutzer.getId());
				leererBenutzer.setGruppeId(benutzer.getGruppeId());

				try {
					benutzerDao.aktualisieren(leererBenutzer);
				} catch (SQLException e1) {
					log.fatal("Benutzer loeschen fehlgeschlagen", e);
					benachrichtigungErzeugen(ERROR, getText("Benutzer.loeschen.fehlgeschlagen"));
					return ERROR;
				}

				if (benutzer.getGruppeId() == 2) {
					try {
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
									Nachricht nachrichtBearbeiter = new Nachricht();
									Benutzer bearbeiter = benutzerDao.getEinzeln(vorgang.getBearbeiterId());

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
					} catch (SQLException e2) {
						log.fatal("Benutzer loeschen fehlgeschlagen", e2);
						benachrichtigungErzeugen(ERROR, getText("Benutzer.loeschen.fehlgeschlagen"));
						return ERROR;
					}
				} else { // Ausschreiber, Bearbeiter
					benachrichtigungErzeugen(ERROR, getText("Benutzer.loeschen.nichterlaubt." + benutzer.getGruppeId()));
					return ERROR;
				}
			} else {
				log.fatal("Benutzer loeschen fehlgeschlagen", e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.loeschen.fehlgeschlagen"));
				return ERROR;
			}
		}

		try {
			benutzerDao.alsVertreterAustragen(benutzer);
		} catch (SQLException e) {
			log.fatal("Benutzer löschen: als Vertreter austragen fehlgeschlagen", e);
			benachrichtigungErzeugen(ERROR, getText("Benutzer.loeschen.AlsVertreterAustragenfehlgeschlagen"));
			return ERROR;
		}

		benachrichtigungErzeugen(SUCCESS, getText("Benutzer.loeschen.accountGeloescht"));
		log.info("Benutzer gelöscht: " + benutzer);

		if (benutzerId == sessionBenutzer.getId()) {
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.remove("angemeldeterBenutzer");
			session.remove("loggedIn");
			return "startseite";
		}

		return SUCCESS;
	}

	/**
	 * Meldet einen Benutzer anhand der abgeschickten Formulardaten im System an und schriebt das Benutzerobjekt in die Session.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String anmelden() {
		if (benutzerAngemeldet()) {
			benachrichtigungErzeugen("info", getText("Benutzer.anmelden.bereitsAngemeldet"));
			return ERROR;
		}

		if (benutzer == null) {
			log.debug("Keine Formulardaten gefunden -> Rückgabe Loginformular");
			return INPUT;
		}

		try {
			Benutzer abgespeicherterBenutzer = benutzerDao.getEinzeln("email", benutzer.getEmail());

			sessionBenutzer = abgespeicherterBenutzer;
			Map<String, Object> session = ActionContext.getContext().getSession();
			session.put("angemeldeterBenutzer", abgespeicherterBenutzer);
			session.put("loggedIn", true);
			log.info("Benutzer angemeldet: " + sessionBenutzer);
			// benachrichtigungErzeugen(SUCCESS, getText("Benutzer.anmelden.erfolgreich"));

			return SUCCESS;
		} catch (SQLException e) {
			log.fatal("Fehlgeschlagener Login", e);
			benachrichtigungErzeugen(ERROR, getText("Benutzer.anmelden.fehlgeschlagen"));
			return INPUT;
		}
	}

	/**
	 * Meldet einen Benutzer vom System ab und löscht das zugehörige Benutzerobjekt aus der Session
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String abmelden() {
		log.debug("Benutzer abgemeldet: " + sessionBenutzer);
		session.remove("angemeldeterBenutzer");
		session.remove("loggedIn");
		session.put("istVertreter", false);
		session.put("actionHistoryStackActive", "false");
		benachrichtigungErzeugen(SUCCESS, getText("Benutzer.abmelden.erfolgreich"));
		return SUCCESS;
	}

	/**
	 * Schickt einem Benutzer (Abfrage der Formulardaten) ein neues Passwort zu
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String passwortAnfordern() {
		if (benutzer == null) {
			log.debug("Keine Formulardaten gefunden -> Rückgabe PasswortVergessenformular");
			return INPUT;
		}

		try {
			Mailer mailer = new Mailer();
			benutzer = benutzerDao.getEinzeln("email", benutzer.getEmail());
			String neuesPasswort = mailer.passwortGenerieren();
			benutzer.setPasswort(passwortHashen(neuesPasswort));
			benutzerDao.aktualisieren(benutzer);
			mailer.passwortAnfordernMail(benutzer.getEmail(), neuesPasswort);

			log.info("neues Passwort angefordert für " + benutzer);

			benachrichtigungErzeugen(SUCCESS, getText("Benutzer.passwortAnfordern.erfolgreich"));
			return SUCCESS;
		} catch (Exception e) {
			log.fatal("Passwort Anfordern fehlgeschlagen", e);
			benachrichtigungErzeugen(ERROR, getText("Benutzer.passwortAnfordern.fehlgeschlagen.titel"), getText("Benutzer.passwortAnfordern.fehlgeschlagen"));
			return INPUT;
		}
	}

	/**
	 * Meldet einen Benutzer im System als Vertreter um. Dabei wird der eigentlich angemeldete Benutzer fortan als Vertreter eines anderen Benutzers gehandhabt
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String vertreterAnmelden() {

		if (!((Boolean) session.get("istVertreter"))) {
			try {
				// benutzerId = Id des Benutzers der vertreten werden soll
				Benutzer benutzer = benutzerDao.getEinzeln(benutzerId);

				if (benutzer == null || benutzer.getVertreterId() != sessionBenutzer.getId()) {
					log.fatal("Fehlgeschlagener Vertreter-Login");
					benachrichtigungErzeugen(ERROR, getText("Benutzer.anmelden.vertreter.fehlgeschlagen"));
					return "startseite";
				}

				log.info("Benutzer " + sessionBenutzer + " als Verteter angemeldet für " + benutzer);
				sessionBenutzer = benutzer;

				session.put("angemeldeterBenutzer", benutzer);
				session.put("istVertreter", true);

				// benachrichtigungErzeugen(SUCCESS, getText("Benutzer.anmelden.vertreter.erfolgreich"));

				return SUCCESS;
			} catch (SQLException e) {
				log.fatal("Fehlgeschlagener Vertreter-Login", e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.anmelden.vertreter.fehlgeschlagen"));
				return "startseite";
			}
		} else {
			try {
				benutzer = benutzerDao.getEinzeln(sessionBenutzer.getId());
				Benutzer originalBenutzer = benutzerDao.getEinzeln(benutzer.getVertreterId());
				if (originalBenutzer == null) {
					log.fatal("Vertreter Modus beenden fehlgeschlagen");
					benachrichtigungErzeugen(ERROR, getText("Benutzer.anmelden.vertreter.beenden.fehlgeschlagen"));
					return "startseite";
				}

				log.info("Benutzer " + sessionBenutzer + " beendet VertreterModus für " + benutzer);
				sessionBenutzer = originalBenutzer;

				session.put("angemeldeterBenutzer", originalBenutzer);
				session.put("istVertreter", false);

				// benachrichtigungErzeugen(SUCCESS, getText("Benutzer.anmelden.vertreter.beenden.erfolgreich"));

				return SUCCESS;
			} catch (SQLException e) {
				log.fatal("Vertreter Modus beenden fehlgeschlagen", e);
				benachrichtigungErzeugen(ERROR, getText("Benutzer.anmelden.vertreter.beenden.fehlgeschlagen"));
				return "startseite";
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		// teste ob die 'alle' seite korrekt sortiert wird, sonst standard zuweisen
		if (null == spalte || null == listeSortSpalten.get(spalte)) {
			spalte = "name";
		}

		fieldErrorsDuplikateEntfernen();
	}

	/**
	 * Validiert die Eingaben bei der Suche
	 * 
	 * @throws Exception
	 */
	public void validateSuche() throws Exception {
		log.debug("validateSuche aufgerufen");
		if (suche != null) {
			log.debug("validateSuche begonnen");
			if (!Jsoup.isValid(suche, Whitelist.none())) {
				addFieldError("suche", this.getText("Validierung.html"));
			}
		} else {
			fieldErrorsEntfernen();
		}

		logFieldErrors();
	}

	/**
	 * Validiert die Eingaben bei der Registrierung
	 * 
	 * @throws Exception
	 */
	public void validateRegistrieren() throws Exception {
		// ohne mitgesendete Formulardaten, Validierung abbrechen
		log.debug("validateRegistrieren aufgerufen");
		if (benutzer != null) {
			log.debug("validateRegistrieren begonnen");

			// Felder auf Inhalte prüfen -> validation.xml

			// Wenn keine zulässige Benutzergruppe ausgewählt, mach einen bewerber daraus
			if (0 >= benutzer.getGruppeId() || 4 < benutzer.getGruppeId()) {
				benutzer.setGruppeId(2);
			}

			// nicht zu validierende Felder
			if (benutzer.getGruppeId() != 2) {
				String[] adressen = { "strasse", "hausnummer", "postleitzahl", "stadt", "land", "strasseHeimat", "hausnummerHeimat", "postleitzahlHeimat",
						"stadtHeimat", "landHeimat", "nationalitaet", "matrikelnummer", "studiengang", "fachsemester" };
				fieldErrorsEntfernen("benutzer", adressen);
			} else {
				String[] institut = { "institut" };
				fieldErrorsEntfernen("benutzer", institut);
			}

			// Auf HTML prüfen
			checkBeanHTML(benutzer);

			// AGBs akzeptiert?
			if (sessionBenutzer.getGruppeId() != 1 && !agbCheckbox) {
				addFieldError("agbCheckbox", getText("Benutzer.registrieren.agb.dsb.nichtAkzeptiert"));
			}

			// Email bereits vorhanden
			if (benutzerDao.getEinzeln("email", benutzer.getEmail()) != null) {
				addFieldError("benutzer.email", getText("Benutzer.email.bereitsvorhanden"));
			}

			// Geburtstag bei Bewerber valid?
			if (!(benutzer.getGeburtstag() instanceof Date)) {
				addFieldError("benutzer.geburtstag", getText("Benutzer.geburtstag.ungueltigesDatum"));
			} else if (benutzer.getGeburtstag().after(Calendar.getInstance().getTime())) {
				addFieldError("benutzer.geburtstag", getText("Benutzer.geburtstag.zuspaet"));
			}

			// Passwörter gleich?
			if (!benutzer.getPasswort().equals(passwortBestaetigen)) {
				addFieldError("passwortBestaetigen", getText("Benutzer.passwort.nichtidentisch"));
			}

			// Falls Admin, Passwortfehler entfernen
			if (sessionBenutzer.getGruppeId() == 1) {
				fieldErrorsEntfernen("benutzer", new String[] { "passwort", "passwortBestaetigen" });
			}

			// Bei Fehlschlag der Validierung, Benachrichtigung Erzeugen
			if (!getFieldErrors().isEmpty()) {
				benachrichtigungErzeugen(ERROR, getText("Benutzer.registrieren.validierungFehlgeschlagen"));
			}
		} else {
			fieldErrorsEntfernen();
		}

		logFieldErrors();
	}

	/**
	 * Validiert die Eingaben beim Aktualisieren
	 * 
	 * @throws Exception
	 */
	public void validateAktualisieren() throws Exception {
		log.debug("validateAktualisieren aufgerufen");

		if (benutzer != null) {
			log.debug("validateAktualisieren begonnen");

			// Felder auf Inhalte prüfen -> validation.xml

			// nicht zu validierende Felder
			if (benutzer.getGruppeId() != 2) {
				String[] adressen = { "strasse", "hausnummer", "postleitzahl", "stadt", "land", "strasseHeimat", "hausnummerHeimat", "postleitzahlHeimat",
						"stadtHeimat", "landHeimat", "nationalitaet", "matrikelnummer", "studiengang", "fachsemester" };
				fieldErrorsEntfernen("benutzer", adressen);
			} else {
				String[] institut = { "institut" };
				fieldErrorsEntfernen("benutzer", institut);
			}

			// Geburtstag bei Bewerber valid?
			if (!(benutzer.getGeburtstag() instanceof Date)) {
				addFieldError("benutzer.geburtstag", getText("Benutzer.geburtstag.ungueltigesDatum"));
			} else if (benutzer.getGeburtstag().after(Calendar.getInstance().getTime())) {
				addFieldError("benutzer.geburtstag", getText("Benutzer.geburtstag.zuspaet"));
			}

			// Passwörter gleich oder gleichleer?
			if (!benutzer.getPasswort().equals(passwortBestaetigen)) {
				addFieldError("passwortBestaetigen", getText("Benutzer.passwort.nichtidentisch"));
			} else if (benutzer.getPasswort() == null || benutzer.getPasswort().equals("")) {
				// FieldErrors bzgl. Passwort entfernen
				Map<String, List<String>> map = getFieldErrors();
				map.remove("benutzer.passwort");
				setFieldErrors(map);
			}

			// Wenn Admin fremden Benutzer aktualisiert, FieldErrors verwerfen, gruppeId validieren und Daten auffüllen
			if (sessionBenutzer.getGruppeId() == 1 && sessionBenutzer.getId() != benutzer.getId()) {
				Map<String, List<String>> map = getFieldErrors();
				map.clear();
				setFieldErrors(map);

				if (benutzer.getGruppeId() < 0 || benutzer.getGruppeId() > 4) {
					addFieldError("benutzer.gruppeId", getText("Benutzer.gruppeId.ungueltig"));
				}

				int temp = benutzer.getGruppeId();
				benutzer = benutzerDao.getEinzeln(benutzer.getId());
				benutzer.setGruppeId(temp);

				benutzer.setPasswort(""); // sorgt dafür, dass am ende das alte PW erhalten bleibt
			}

			// EMail bereits vorhanden? wenn mail != Datenbankbenutzer-Mail
			if (benutzerDao.getEinzeln("email", benutzer.getEmail()) != null
					&& benutzerDao.getEinzeln("email", benutzer.getEmail()).getId() != benutzer.getId()) {
				addFieldError("benutzer.email", getText("Benutzer.aktualisieren.email.bereitsvorhanden"));
			}

			// Vertreter validieren
			if ((vertreterMail != null && !vertreterMail.equals(""))) {
				Benutzer vertreter = benutzerDao.getEinzeln("email", vertreterMail);
				if (vertreter == null || vertreter.getGruppeId() != benutzer.getGruppeId() || benutzer.getId() == vertreter.getId()) {
					addFieldError("vertreterMail", getText("Benutzer.vertreterFalsch"));
				}
			}

			// Auf HTML prüfen
			// Falls Admin fremden Benutzer aktualisiert, nicht prüfen
			if (!(sessionBenutzer.getGruppeId() == 1 && sessionBenutzer.getId() != benutzer.getId())) {
				checkBeanHTML(benutzer);
			}

		} else {
			fieldErrorsEntfernen();
		}

		logFieldErrors();
	}

	/**
	 * Validiert die Eingaben beim Löschen
	 * 
	 * @throws ZugriffsException
	 */
	public void validateLoeschen() throws ZugriffsException {
		log.debug("validateLoeschen aufgerufen");

		logFieldErrors();
	}

	/**
	 * Validiert die Eingaben beim Anmelden
	 * 
	 * @throws Exception
	 */
	public void validateAnmelden() throws Exception {
		log.debug("validateAnmelden aufgerufen");

		if (benutzer != null) {

			// eMail eingabe keine e-mail?
			if (!EmailValidator.getInstance().isValid(benutzer.getEmail())) {
				addFieldError("benutzer.email", getText("Benutzer.anmelden.email.keineKorrekteEMail"));
			} else {

				Benutzer abgespeicherterBenutzer = benutzerDao.getEinzeln("email", benutzer.getEmail());
				// Email gefunden?
				if (abgespeicherterBenutzer == null) { // TEST AUF NULL, NICHT AUF LEER
					log.info("Fehlgeschlagener Loginversuch: Account nicht gefunden");
					addFieldError("benutzer.email", getText("Benutzer.anmelden.email.falsch"));
				} else if (!passwortAbgleichen(benutzer.getPasswort(), abgespeicherterBenutzer)) { // Passwort korrekt?
					log.info("Fehlgeschlagener Loginversuch: Passwort nicht korrekt");
					addFieldError("benutzer.passwort", getText("Benutzer.anmelden.passwort.falsch"));
				} else {
					Map<String, Object> session = ActionContext.getContext().getSession();
					session.put("istVertreter", false);
				}
			}
		}

		logFieldErrors();
	}

	/**
	 * Validiert die Eingaben bei der Passwortanfrage
	 * 
	 * @throws Exception
	 */
	public void validatePasswortAnfordern() throws Exception {
		log.debug("validatePasswortAnfordern aufgerufen");

		if (benutzer != null) {
			log.debug("validatePasswortAnfordern begonnen");

			// eMail eingabe keine e-mail?
			if (!EmailValidator.getInstance().isValid(benutzer.getEmail())) {
				addFieldError("benutzer.email", getText("Benutzer.passwortAnfordern.email.keineKorrekteEMail"));
			} else {
				if (benutzerDao.getEinzeln("email", benutzer.getEmail()) == null) {
					addFieldError("benutzer.email", getText("Benutzer.passwortAnfordern.email.nichtvorhanden"));
				}
			}
		}

		logFieldErrors();
	}

	/*
	 * Hilfsmethoden für Passwörter und Selectboxen
	 */

	/**
	 * Hasht ein Passwort mit {@code BCrypt}
	 * 
	 * @param passwort
	 * @return
	 */
	private String passwortHashen(final String passwort) {
		return BCrypt.hashpw(passwort, BCrypt.gensalt());
	}

	/**
	 * Vergleicht einen eingebenen String {@code passwort} mit einem gehashten Passwort aus einem {@code Benutzerobjekt}
	 * 
	 * @param passwort
	 * @param abgespeicherterBenutzer
	 * @return
	 */
	private boolean passwortAbgleichen(final String passwort, final Benutzer abgespeicherterBenutzer) {
		return BCrypt.checkpw(passwort, abgespeicherterBenutzer.getPasswort());
	}

	/**
	 * Befüllt die Länder und Nationalitäten Maps mit entsprechenden Einträgen für die Selextboxgenerierung
	 */
	public void mapProperties() {

		int i = 1;
		while (!getText("Benutzer.land." + i).equals("Benutzer.land." + i)) {
			laenderMap.put(i, getText("Benutzer.land." + i));
			i++;
		}

		i = 1;
		while (!getText("Benutzer.nationalitaet." + i).equals("Benutzer.nationalitaet." + i)) {
			nationalitaetenMap.put(i, getText("Benutzer.nationalitaet." + i));
			i++;
		}

		i = 1;
		while (!getText("Studiengang." + i).equals("Studiengang." + i)) {
			studiengaengeMap.put(i, getText("Studiengang." + i));
			i++;
		}
	}

	/*
	 * Getters und Setters für Properties
	 */

	/**
	 * 
	 * @return the benutzer
	 */
	public Benutzer getBenutzer() {
		return benutzer;
	}

	/**
	 * 
	 * @param benutzer
	 */
	public void setBenutzer(final Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	/**
	 * 
	 * @return the benutzerId
	 */
	public int getBenutzerId() {
		return benutzerId;
	}

	/**
	 * 
	 * @param benutzerId
	 */
	public void setBenutzerId(final int benutzerId) {
		this.benutzerId = benutzerId;
	}

	/**
	 * 
	 * @return the passwortBestaetigen
	 */
	public String getPasswortBestaetigen() {
		return passwortBestaetigen;
	}

	/**
	 * 
	 * @param passwortBestaetigen
	 */
	public void setPasswortBestaetigen(final String passwortBestaetigen) {
		this.passwortBestaetigen = passwortBestaetigen;
	}

	/**
	 * @return the vertreterMail
	 */
	public String getVertreterMail() {
		return vertreterMail;
	}

	/**
	 * @return the listeDaten
	 */
	public LinkedList<Benutzer> getListeDaten() {
		return listeDaten;
	}

	/**
	 * @param vertreterMail
	 *            the vertreterMail to set
	 */
	public void setVertreterMail(final String vertreterMail) {
		this.vertreterMail = vertreterMail;
	}

	/**
	 * @return the agbCheckbox
	 */
	public boolean isAgbCheckbox() {
		return agbCheckbox;
	}

	/**
	 * @param agbCheckbox
	 *            the agbCheckbox to set
	 */
	public void setAgbCheckbox(final boolean agbCheckbox) {
		this.agbCheckbox = agbCheckbox;
	}

	/**
	 * @return the laenderMap
	 */
	public Map<Integer, String> getLaenderMap() {
		return laenderMap;
	}

	/**
	 * @param laenderMap
	 *            the laenderMap to set
	 */
	public void setLaenderMap(final Map<Integer, String> laenderMap) {
		this.laenderMap = laenderMap;
	}

	/**
	 * @return the nationalitaetenMap
	 */
	public Map<Integer, String> getNationalitaetenMap() {
		return nationalitaetenMap;
	}

	/**
	 * @param nationalitaetenMap
	 *            the nationalitaetenMap to set
	 */
	public void setNationalitaetenMap(final Map<Integer, String> nationalitaetenMap) {
		this.nationalitaetenMap = nationalitaetenMap;
	}

	/**
	 * @return the studiengaengeMap
	 */
	public Map<Integer, String> getStudiengaengeMap() {
		return studiengaengeMap;
	}

	/**
	 * @param studiengaengeMap
	 *            the studiengaengeMap to set
	 */
	public void setStudiengaengeMap(final Map<Integer, String> studiengaengeMap) {
		this.studiengaengeMap = studiengaengeMap;
	}

	/**
	 * @return the suche
	 */
	public String getSuche() {
		return suche;
	}

	/**
	 * @param suche
	 *            the suche to set
	 */
	public void setSuche(final String suche) {
		this.suche = suche;
	}

}
