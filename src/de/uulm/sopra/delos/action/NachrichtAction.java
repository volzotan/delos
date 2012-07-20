package de.uulm.sopra.delos.action;

import java.sql.Timestamp;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;

import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.bean.Nachricht;
import de.uulm.sopra.delos.dao.BenutzerDao;
import de.uulm.sopra.delos.dao.NachrichtDao;

/**
 * 
 * Implementierung des Nachrichtensystems
 * 
 */
public class NachrichtAction extends BasisAction {

	private static final long		serialVersionUID	= 1612792435888305734L;

	private Nachricht				nachricht;
	private final NachrichtDao		nachrichtDao		= new NachrichtDao();
	private LinkedList<Nachricht>	listeDaten			= new LinkedList<Nachricht>();

	private int						nachrichtId;
	private int						empfaengerId;
	private int						absenderId;
	private int						sessionId;
	private int[]					multiNachrichtId;
	private int						multiSubmitButton;
	// 0: Posteingang, 1: Postausgang, 2: Papierkorb
	private int						nachrichtTyp;
	// Antwort auf Nachricht oder neue Nachricht?
	private String					formularZiel;
	private String					currentDay;

	private final BenutzerDao		benutzerDao			= new BenutzerDao();

	@Override
	public void prepare() throws Exception {
		super.prepare();

		listeSortSpalten.put("titel", "`titel`");
		listeSortSpalten.put("datum", "`versanddatum`");
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#validate()
	 */
	@Override
	public void validate() {
		if (null == spalte || null == listeSortSpalten.get(spalte)) {
			spalte = "datum";
			richtung = "desc";
		}

		fieldErrorsDuplikateEntfernen();
	}

	/**
	 * Listet Posteingang auf.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexPosteingang() {
		try {
			// Aktueller Timestamp (Tag Monat Jahr) für Abgleich in den JSP's
			currentDay = new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
			// Auflistung des Posteingangs
			listeDaten = (LinkedList<Nachricht>) nachrichtDao.getAlleAlsListe(new String[] { "empfaenger_id", "empfaenger_loeschStatus" }, new String[] {
					sessionBenutzer.getId() + "", "2" }, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = nachrichtDao.getAnzahl(new String[] { "empfaenger_id", "empfaenger_loeschStatus" }, new String[] {
					sessionBenutzer.getId() + "", "2" });
		} catch (Exception e) {
			e.printStackTrace();
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.index.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Listet Postausgang auf.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexPostausgang() {
		try {
			// Aktueller Timestamp (Tag Monat Jahr) für Abgleich in den JSP's
			currentDay = new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
			// Auflistung des Postausgangs
			listeDaten = (LinkedList<Nachricht>) nachrichtDao.getAlleAlsListe(new String[] { "absender_id", "absender_loeschStatus" }, new String[] {
					sessionBenutzer.getId() + "", "2" }, listeSortSpalten.get(spalte), sortierungen.get(richtung));
			listeAnzahlElemente = nachrichtDao.getAnzahl(new String[] { "absender_id", "absender_loeschStatus" }, new String[] { sessionBenutzer.getId() + "",
					"2" });
		} catch (Exception e) {
			e.printStackTrace();
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.index.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Listet Papierkorb auf.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String indexPapierkorb() {
		try {
			// Aktueller Timestamp (Tag Monat Jahr) für Abgleich in den JSP's
			currentDay = new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
			// Auflistung des Papierkorbs
			listeDaten = (LinkedList<Nachricht>) nachrichtDao.getPapierkorbAlsListe(sessionBenutzer.getId(), seite, listeSortSpalten.get(spalte),
					sortierungen.get(richtung));
			listeAnzahlElemente = nachrichtDao.getPapierkorbAnzahl(sessionBenutzer.getId());
		} catch (Exception e) {
			e.printStackTrace();
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.index.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Zeigt einzelne Nachricht im Detail an.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String anzeigen() {
		try {
			nachricht = nachrichtDao.getEinzeln("id", nachrichtId + "");
			sessionId = sessionBenutzer.getId();
			absenderId = nachricht.getAbsenderId();
			empfaengerId = nachricht.getEmpfaengerId();

			// Hat der Nutzer das Recht die Nachricht zu öffnen?
			// Und aus welchem Fach stammt die Nachricht?
			if (null != nachricht) {
				if (sessionId == absenderId) {
					if (nachricht.getAbsenderLoeschStatus() < 2) {
						// Papierkorb (Ursprung: Postausgang)
						nachrichtTyp = 2;
					} else {
						// Postausgang
						nachrichtTyp = 1;
					}
				} else if (sessionId == empfaengerId) {
					if (nachricht.getEmpfaengerLoeschStatus() < 2) {
						// Papierkorb (Ursprung: Posteingang)
						nachrichtTyp = 2;
					} else {
						// Posteingang
						nachrichtTyp = 0;
					}
					if (nachricht.getStatus() == 3 || nachricht.getStatus() == 1) {
						// Setze Nachricht auf Status "Angesehen"
						nachrichtDao.aktualisiereStatus(nachrichtId, nachricht.getStatus() - 1);
					}
				} else {
					this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.anzeigen.keinRecht"));
					return ERROR;
				}
			} else {
				this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.anzeigen.nichtGeladen"));
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.anzeigen.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Legt neues Nachrichtenobjekt an, um eine Nachricht schreiben zu können.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String erstellen() {
		if (null != nachricht) {
			try {
				// Nachricht in DB ablegen
				nachrichtDao.erstellen(nachricht);
			} catch (Exception e) {
				e.printStackTrace();
				this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.abschicken.fehlgeschlagen"));
				return ERROR;
			}
			this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.abschicken.erfolgreich"));
			return SUCCESS;
		}
		// Befüllen der Nachricht mit nötigen Parametern
		if (empfaengerId > 0 && empfaengerId != sessionBenutzer.getId()) {
			try {
				nachricht = new Nachricht();
				nachricht.setEmpfaengerId(empfaengerId);
				nachricht.setEmpfaengerName(benutzerDao.getEinzeln(empfaengerId).getName());
				nachricht.setAbsenderId(sessionBenutzer.getId());
				return INPUT;
			} catch (Exception e) {
				e.printStackTrace();
				this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.erstellen.fehlgeschlagen"));
				return ERROR;
			}
		} else {
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.erstellen.unzulaessigerEmpfaenger"));
			return ERROR;
		}
	}

	/**
	 * Validiere Input des Erstellen-Formulars
	 * 
	 * @throws Exception
	 */
	public void validateErstellen() throws Exception {
		formularZiel = "Nachricht_erstellen";
		log.debug("validateEstellen aufgerufen");
		if (null != nachricht) {
			log.debug("validateErstellen ausführen");

			// Empfaenger validieren
			Benutzer empfaenger = benutzerDao.getEinzeln(nachricht.getEmpfaengerId());
			if (null == empfaenger || 0 >= empfaenger.getId()) {
				addFieldError("nachricht.empfaengerId", this.getText("Nachricht.fielderror.empfaenger.unzulaessigerEmpfaenger"));
				addFieldError("nachricht.empfaengerName", this.getText("Nachricht.fielderror.empfaenger.unzulaessigerEmpfaenger"));
			} else {
				nachricht.setEmpfaengerName(empfaenger.getName());
			}
			// Illegale Eingaben entfernen
			LinkedList<String> check = new LinkedList<String>();
			check.add("betreff");
			this.checkBeanHTML(nachricht, check);

			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			nachricht.setText(Jsoup.clean(nachricht.getText(), "", Whitelist.basic(), d).trim());
			if (0 > nachricht.getText().length()) {
				addFieldError("nachricht.text", this.getText("Nachricht.fielderror.text.leer"));
			}
		}
	}

	/**
	 * Antworten auf eine bereits vorhandene Nachricht.
	 * 
	 * @return Erfolgs- bzw. Misserfolgsstatus der Abfrage gekoppelt an das Ziel für die Ausgabe
	 */
	public String antworten() {
		if (null != nachricht) {
			try {
				// Nachricht in DB ablegen
				nachrichtDao.erstellen(nachricht);
				nachrichtId = nachricht.getId();
				// Status auf "beantwortet" setzten (Status wird im Formular mit übergeben)
				if (nachricht.getStatus() > 1) {
					nachrichtDao.aktualisiereStatus(nachrichtId, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.abschicken.fehlgeschlagen"));
				return ERROR;
			}
			this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.abschicken.erfolgreich"));
			return SUCCESS;
		}

		try {
			nachricht = nachrichtDao.getEinzeln("id", nachrichtId + "");
			sessionId = sessionBenutzer.getId();

			// Hat der Nutzer das Recht die Nachricht zu beantworten?
			if (null != nachricht && nachricht.getAbsenderId() > 0 && (sessionId == nachricht.getAbsenderId() || sessionId == nachricht.getEmpfaengerId())) {
				String betreffReplies = this.getText("Nachricht.betreff.replies");
				String betreffInput = nachricht.getBetreff();

				// Absender- und EmpfaengerID's setzen
				if (sessionId != nachricht.getAbsenderId()) {
					// Antwort auf Nachricht aus Posteingang
					nachricht.setEmpfaengerName(nachricht.getAbsenderName());
					nachricht.setEmpfaengerId(nachricht.getAbsenderId());
				}
				nachricht.setAbsenderId(sessionId);
				// log.debug("ACTIONAME: " + getActionName());

				// Füge das jeweilige Replies-Kürzel ein, wenn nötig
				if (!betreffInput.substring(0, betreffReplies.length()).trim().equals(betreffReplies)) {
					nachricht.setBetreff(betreffReplies + ' ' + betreffInput);
				}
				// Einfügen des Nachrichtenverlaufs
				if (!nachricht.getText().trim().equals("")) {
					LinkedList<Timestamp> timestamp = new LinkedList<Timestamp>();
					timestamp.add(nachricht.getVersanddatum());
					nachricht.setText(this.getText("Nachricht.text.verlauf", new String[] { this.getText("nachricht.versanddatum.komplett", timestamp),
							nachricht.getEmpfaengerName(), nachricht.getText() }));
				}
			} else {
				this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.antworten.keinRecht"));
				return ERROR;
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Daten zur Nachricht ID: " + nachrichtId + " sind fehlerhaft / nicht vorhanden");
			return ERROR;
		}
	}

	/**
	 * Validiere Inputs des Antworten-Formulars
	 * 
	 * @throws Exception
	 */
	public void validateAntworten() throws Exception {
		formularZiel = "Nachricht_antworten";
		log.debug("validateAntworten aufgerufen");
		if (null != nachricht) {
			log.debug("validateAntworten ausführen");

			// Empfaenger validieren
			Benutzer empfaenger = benutzerDao.getEinzeln(nachricht.getEmpfaengerId());
			if (null == empfaenger || 0 >= empfaenger.getId()) {
				addFieldError("nachricht.empfaengerId", this.getText("Nachricht.fielderror.empfaenger.unzulaessigerEmpfaenger"));
				addFieldError("nachricht.empfaengerName", this.getText("Nachricht.fielderror.empfaenger.unzulaessigerEmpfaenger"));
			} else {
				nachricht.setEmpfaengerName(empfaenger.getName());
			}
			// Betreff validieren
			// Illegale Eingaben entfernen
			LinkedList<String> check = new LinkedList<String>();
			check.add("betreff");
			this.checkBeanHTML(nachricht, check);

			Document.OutputSettings d = new Document.OutputSettings();
			d.escapeMode(Entities.EscapeMode.valueOf("xhtml"));
			nachricht.setText(Jsoup.clean(nachricht.getText(), "", Whitelist.basic(), d).trim());
			if (0 > nachricht.getText().length()) {
				addFieldError("nachricht.text", this.getText("Nachricht.fielderror.text.leer"));
			}
		}
	}

	/**
	 * Löschen/Paierkorb einer einzelnen Nachricht
	 * 
	 * @return //
	 */
	public String einzelnLoeschen() {
		try {
			nachricht = nachrichtDao.getEinzeln("id", nachrichtId + "");
			if (nachricht != null) {
				int absenderLoeschStatus = nachricht.getAbsenderLoeschStatus();
				int empfaengerLoeschStatus = nachricht.getEmpfaengerLoeschStatus();
				sessionId = sessionBenutzer.getId();
				absenderId = nachricht.getAbsenderId();
				empfaengerId = nachricht.getEmpfaengerId();
				// Hat der aktive Nutzer das Recht?
				// Wenn ja, evaluiere derzeitigen Status der Nachricht
				// und führe passende Methode aus.
				if (sessionId == absenderId) {
					if (absenderLoeschStatus > 0 && empfaengerLoeschStatus > 0) {
						nachrichtDao.aktualisiereLoeschStatus(nachricht, true);
						if (absenderLoeschStatus == 2) {
							this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.papierkorb.erfolgreich"));
						} else {
							this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.loeschen.erfolgreich"));
						}
						log.debug("BenutzerId " + sessionId + " - Erfolg beim Löschen folgender Nachricht: " + nachrichtId);
						return SUCCESS;
					}
					if (absenderLoeschStatus == 1 && empfaengerLoeschStatus == 0) {
						nachrichtDao.loeschen(nachricht);
						log.debug("BenutzerId " + sessionId + " - Erfolg beim Löschen folgender Nachricht: " + nachrichtId);
						this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.loeschen.erfolgreich"));
						return SUCCESS;
					}
				} else if (sessionId == empfaengerId) {
					if (empfaengerLoeschStatus > 0 && absenderLoeschStatus > 0) {
						nachrichtDao.aktualisiereLoeschStatus(nachricht, false);
						if (empfaengerLoeschStatus == 2) {
							this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.papierkorb.erfolgreich"));
						} else {
							this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.loeschen.erfolgreich"));
						}
						log.debug("BenutzerId " + sessionId + " - Erfolg beim Löschen folgender Nachricht: " + nachrichtId);
						return SUCCESS;
					}
					if (empfaengerLoeschStatus == 1 && absenderLoeschStatus == 0) {
						nachrichtDao.loeschen(nachricht);
						log.debug("BenutzerId " + sessionId + " - Erfolg beim Löschen folgender Nachricht: " + nachrichtId);
						this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.loeschen.erfolgreich"));
						return SUCCESS;
					}
				} else {
					this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.loeschen.keinRecht"));
					return ERROR;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.loeschen.fehlgeschlagen"));
			log.debug("BenutzerId " + sessionId + " - Fehler beim Löschen folgender Nachricht: " + nachrichtId);
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Wiederherstellen einer einzelnen Nachricht
	 * 
	 * @return //
	 */
	public String einzelnWiederherstellen() {
		try {
			nachricht = nachrichtDao.getEinzeln("id", nachrichtId + "");
			// Hat der aktive Nutzer das Recht?
			// Wenn ja, stelle Nachricht wieder her.
			if (nachricht != null) {
				sessionId = sessionBenutzer.getId();
				if (sessionId == nachricht.getAbsenderId()) {
					if (nachricht.getAbsenderLoeschStatus() > 0) {
						// true -> Absenderstatus wird aktualisiert
						nachrichtDao.wiederherstellen(nachricht, true);
						log.debug("BenutzerId " + sessionId + " - Erfolg beim Wiederherstellen folgender Nachricht: " + nachrichtId);
						this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.wiederherstellen.erfolgreich"));
						return SUCCESS;
					}
				} else if (sessionId == nachricht.getEmpfaengerId()) {
					if (nachricht.getEmpfaengerLoeschStatus() > 0) {
						// false -> Empfaengerstatus wird aktualisiert
						nachrichtDao.wiederherstellen(nachricht, false);
						log.debug("BenutzerId " + sessionId + " - Erfolg beim Wiederherstellen folgender Nachricht: " + nachrichtId);
						this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.wiederherstellen.erfolgreich"));
						return SUCCESS;
					}
				} else {
					this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.wiederherstellen.keinRecht"));
					return ERROR;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("BenutzerId " + sessionBenutzer.getId() + " - Fehler beim Wiederherstellen folgender Nachricht: " + nachrichtId);
			this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.wiederherstellen.fehlgeschlagen"));
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Evaluation der auszuführenden Funktion in Formularen mit Checkboxen
	 * 
	 * @return //
	 */
	public String multiCheckBoxAuswertung() {
		if (multiNachrichtId != null) {
			LinkedList<Integer> multiErrorCatch = new LinkedList<Integer>();
			LinkedList<Integer> multiSuccessCatch = new LinkedList<Integer>();
			// 0: Löschen
			if (multiSubmitButton == 0) {
				try {
					for (int id : multiNachrichtId) {
						if (multiLoeschen(id) > -1) {
							multiErrorCatch.add(id);
						} else {
							multiSuccessCatch.add(id);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("BenutzerId " + sessionBenutzer.getId() + " - Fehler beim Löschen folgender Nachricht/en: " + multiErrorCatch.toString());
					if (null != multiErrorCatch && 1 < multiErrorCatch.size()) {
						this.benachrichtigungErzeugen(ERROR,
								this.getText("Nachricht.error.multiLoeschen.fehlgeschlagen", new String[] { multiErrorCatch.size() + "" }));
					} else {
						this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.loeschen.fehlgeschlagen"));
					}
					return ERROR;
				}
				log.debug("BenutzerId " + sessionBenutzer.getId() + " - Erfolg beim Löschen folgender Nachricht/en: " + multiSuccessCatch.toString());
				if (null != multiSuccessCatch && 1 < multiSuccessCatch.size()) {
					this.benachrichtigungErzeugen(SUCCESS,
							this.getText("Nachricht.success.multiLoeschen.erfolgreich", new String[] { multiSuccessCatch.size() + "" }));
				} else {
					this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.loeschen.erfolgreich"));
				}
			}
			// 1: Wiederherstellen
			if (multiSubmitButton == 1) {
				try {
					for (int id : multiNachrichtId) {
						if (multiWiederherstellen(id) > -1) {
							multiErrorCatch.add(id);
						} else {
							multiSuccessCatch.add(id);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("BenutzerId " + sessionBenutzer.getId() + " - Fehler beim Wiederherstellen folgender Nachricht/en: " + multiErrorCatch.toString());
					if (null != multiErrorCatch && 1 < multiErrorCatch.size()) {
						this.benachrichtigungErzeugen(ERROR,
								this.getText("Nachricht.error.multiWiederherstellen.fehlgeschlagen", new String[] { multiErrorCatch.size() + "" }));
					} else {
						this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.wiederherstellen.fehlgeschlagen"));
					}
					return ERROR;
				}
				log.debug("BenutzerId " + sessionBenutzer.getId() + " - Erfolg beim Wiederherstellen folgender Nachricht/en: " + multiSuccessCatch.toString());
				if (null != multiSuccessCatch && 1 < multiSuccessCatch.size()) {
					this.benachrichtigungErzeugen(SUCCESS,
							this.getText("Nachricht.success.multiWiederherstellen.erfolgreich", new String[] { multiSuccessCatch.size() + "" }));
				} else {
					this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.wiederherstellen.erfolgreich"));
				}
			}
			// 2: Als gelesen markieren
			if (multiSubmitButton == 2) {
				try {
					for (int id : multiNachrichtId) {
						int markStatus = multiAlsGelesenMarkieren(id);
						if (markStatus > -1) {
							multiErrorCatch.add(id);
						} else if (markStatus == -1) {
							multiSuccessCatch.add(id);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("BenutzerId " + sessionBenutzer.getId() + " - Fehler beim Markieren folgender Nachricht/en: " + multiErrorCatch.toString());
					if (null != multiErrorCatch && 1 < multiErrorCatch.size()) {
						this.benachrichtigungErzeugen(ERROR,
								this.getText("Nachricht.error.multiAlsGelesenMarkieren.fehlgeschlagen", new String[] { multiErrorCatch.size() + "" }));
					} else {
						this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.alsGelesenMarkieren.fehlgeschlagen"));
					}
					return ERROR;
				}
				log.debug("BenutzerId " + sessionBenutzer.getId() + " - Erfolg beim Markieren folgender Nachricht/en: " + multiSuccessCatch.toString());
				if (null != multiSuccessCatch && 1 < multiSuccessCatch.size()) {
					this.benachrichtigungErzeugen(SUCCESS,
							this.getText("Nachricht.success.multiAlsGelesenMarkieren.erfolgreich", new String[] { multiSuccessCatch.size() + "" }));
				} else {
					this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.alsGelesenMarkieren.erfolgreich"));
				}
			}
			// 2: Als gelesen markieren
			if (multiSubmitButton == 3) {
				try {
					for (int id : multiNachrichtId) {
						int markStatus = multiAlsUngelesenMarkieren(id);
						if (markStatus > -1) {
							multiErrorCatch.add(id);
						} else if (markStatus == -1) {
							multiSuccessCatch.add(id);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("BenutzerId " + sessionBenutzer.getId() + " - Fehler beim Markieren folgender Nachricht/en: " + multiErrorCatch.toString());
					if (null != multiErrorCatch && 1 < multiErrorCatch.size()) {
						this.benachrichtigungErzeugen(ERROR,
								this.getText("Nachricht.error.multiAlsUngelesenMarkieren.fehlgeschlagen", new String[] { multiErrorCatch.size() + "" }));
					} else {
						this.benachrichtigungErzeugen(ERROR, this.getText("Nachricht.error.alsUngelesenMarkieren.fehlgeschlagen"));
					}
					return ERROR;
				}
				log.debug("BenutzerId " + sessionBenutzer.getId() + " - Erfolg beim Markieren folgender Nachricht/en: " + multiSuccessCatch.toString());
				if (null != multiSuccessCatch && 1 < multiSuccessCatch.size()) {
					this.benachrichtigungErzeugen(SUCCESS,
							this.getText("Nachricht.success.multiAlsUngelesenMarkieren.erfolgreich", new String[] { multiSuccessCatch.size() + "" }));
				} else {
					this.benachrichtigungErzeugen(SUCCESS, this.getText("Nachricht.success.alsUngelesenMarkiren.erfolgreich"));
				}
			}
			return SUCCESS;
		} else {
			return NONE;
		}
	}

	/**
	 * Löschvorgang für jede Nachricht im Zusammenhang mit multiCheckBoxAuswertung()
	 * 
	 * @return //
	 */
	private int multiLoeschen(final int id) {
		try {
			nachricht = nachrichtDao.getEinzeln("id", id + "");
			if (nachricht != null) {
				int absenderLoeschStatus = nachricht.getAbsenderLoeschStatus();
				int empfaengerLoeschStatus = nachricht.getEmpfaengerLoeschStatus();
				sessionId = sessionBenutzer.getId();
				absenderId = nachricht.getAbsenderId();
				empfaengerId = nachricht.getEmpfaengerId();
				// Hat der aktive Nutzer das Recht?
				// Wenn ja, evaluiere derzeitigen Status der Nachricht
				// und führe passende Methode aus.
				if (sessionId == absenderId) {
					if (absenderLoeschStatus > 0 && empfaengerLoeschStatus > 0) {
						nachrichtDao.aktualisiereLoeschStatus(nachricht, true);
					}
					if (absenderLoeschStatus == 1 && empfaengerLoeschStatus == 0) {
						nachrichtDao.loeschen(nachricht);
					}
				} else if (sessionId == empfaengerId) {
					if (empfaengerLoeschStatus > 0 && absenderLoeschStatus > 0) {
						nachrichtDao.aktualisiereLoeschStatus(nachricht, false);
					}
					if (empfaengerLoeschStatus == 1 && absenderLoeschStatus == 0) {
						nachrichtDao.loeschen(nachricht);
					}
				} else {
					// Benutzer hat kein Zugriff/Recht
					return id;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Löschen fehlgeschlagen
			return id;
		}
		// Erfolgreich gelöscht/Papierkorb
		return -1;
	}

	/**
	 * Wiederherstellungsvorgang für jede Nachricht im Zusammenhang mit multiCheckBoxAuswertung()
	 * 
	 * @return //
	 */
	private int multiWiederherstellen(final int id) {
		try {
			nachricht = nachrichtDao.getEinzeln("id", id + "");
			// Hat der aktive Nutzer das Recht?
			// Wenn ja, stelle Nachricht wieder her.
			if (nachricht != null) {
				sessionId = sessionBenutzer.getId();
				if (sessionId == nachricht.getAbsenderId()) {
					if (nachricht.getAbsenderLoeschStatus() > 0) {
						nachrichtDao.wiederherstellen(nachricht, true);
					}
				} else if (sessionId == nachricht.getEmpfaengerId()) {
					if (nachricht.getEmpfaengerLoeschStatus() > 0) {
						nachrichtDao.wiederherstellen(nachricht, false);
					}
				} else {
					// Benutzer hat kein Zugriff/Recht
					return id;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Wiederherstellung fehlgeschlagen
			return id;
		}
		// Erfolgreich wiederhergestellt
		return -1;
	}

	/**
	 * Markierung von Nachrichten als gelesen im Zusammenhang mit multiCheckBoxAuswertung()
	 * 
	 * @return //
	 */
	private int multiAlsGelesenMarkieren(final int id) {
		try {
			nachricht = nachrichtDao.getEinzeln("id", id + "");
			// Hat der aktive Nutzer das Recht?
			// Wenn ja, markiere Nachricht als gelesen.
			if (nachricht != null) {
				sessionId = sessionBenutzer.getId();
				if (sessionId == nachricht.getEmpfaengerId()) {
					if (nachricht.getStatus() == 3 || nachricht.getStatus() == 1) {
						nachrichtDao.aktualisiereStatus(id, nachricht.getStatus() - 1);
					} else {
						return -2;
					}
				} else {
					// Benutzer hat kein Zugriff/Recht
					return id;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Markiren fehlgeschlagen
			return id;
		}
		// Erfolgreich markiert
		return -1;
	}

	/**
	 * Markierung von Nachrichten als ungelesen im Zusammenhang mit multiCheckBoxAuswertung()
	 * 
	 * @return //
	 */
	private int multiAlsUngelesenMarkieren(final int id) {
		try {
			nachricht = nachrichtDao.getEinzeln("id", id + "");
			// Hat der aktive Nutzer das Recht?
			// Wenn ja, markiere Nachricht als ungelesen.
			if (nachricht != null) {
				sessionId = sessionBenutzer.getId();
				if (sessionId == nachricht.getEmpfaengerId()) {
					if (nachricht.getStatus() == 2 || nachricht.getStatus() == 0) {
						nachrichtDao.aktualisiereStatus(id, nachricht.getStatus() + 1);
					} else {
						return -2;
					}
				} else {
					// Benutzer hat kein Zugriff/Recht
					return id;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Markiren fehlgeschlagen
			return id;
		}
		// Erfolgreich markiert
		return -1;
	}

	/**
	 * @return the listeDaten
	 */
	public LinkedList<Nachricht> getListeDaten() {
		return listeDaten;
	}

	/**
	 * @param listeDaten
	 *            the listeDaten to set
	 */
	public void setListeDaten(final LinkedList<Nachricht> listeDaten) {
		this.listeDaten = listeDaten;
	}

	/**
	 * 
	 * @return the nachrichtId
	 */
	public int getNachrichtId() {
		return nachrichtId;
	}

	/**
	 * 
	 * @param nachrichtId
	 */
	public void setNachrichtId(final int nachrichtId) {
		this.nachrichtId = nachrichtId;
	}

	/**
	 * @return the multiNachrichtId
	 */
	public int[] getMultiNachrichtId() {
		return multiNachrichtId;
	}

	/**
	 * @param multiNachrichtId
	 *            the multiNachrichtId to set
	 */
	public void setMultiNachrichtId(final int[] multiNachrichtId) {
		this.multiNachrichtId = multiNachrichtId;
	}

	/**
	 * @return the multiSubmitButton
	 */
	public int getMultiSubmitButton() {
		return multiSubmitButton;
	}

	/**
	 * @param multiSubmitButton
	 *            the multiSubmitButton to set
	 */
	public void setMultiSubmitButton(final int multiSubmitButton) {
		this.multiSubmitButton = multiSubmitButton;
	}

	/**
	 * 
	 * @return the EmpfaengerId
	 */
	public int getEmpfaengerId() {
		return empfaengerId;
	}

	/**
	 * 
	 * @param empfaengerId
	 */
	public void setEmpfaengerId(final int empfaengerId) {
		this.empfaengerId = empfaengerId;
	}

	/**
	 * 
	 * @return the AbsenderId
	 */
	public int getAbsenderId() {
		return absenderId;
	}

	/**
	 * 
	 * @param absenderId
	 */
	public void setAbsenderId(final int absenderId) {
		this.absenderId = absenderId;
	}

	/**
	 * @return the nachrichtTyp
	 */
	public int getNachrichtTyp() {
		return nachrichtTyp;
	}

	/**
	 * @return the formularZiel
	 */
	public String getFormularZiel() {
		return formularZiel;
	}

	/**
	 * @return the currentDate
	 */
	public String getCurrentDay() {
		return currentDay;
	}

	/**
	 * @return the nachricht
	 */
	public Nachricht getNachricht() {
		return nachricht;
	}

	/**
	 * @param nachricht
	 *            the nachricht to set
	 */
	public void setNachricht(final Nachricht nachricht) {
		this.nachricht = nachricht;
	}
}