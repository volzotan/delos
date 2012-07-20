package de.uulm.sopra.delos.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import de.uulm.sopra.delos.bean.Benachrichtigung;
import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.bean.Nachricht;
import de.uulm.sopra.delos.dao.BenutzerDao;
import de.uulm.sopra.delos.dao.NachrichtDao;

/**
 * Basisklasse für Actions, die eine Reihe von Methoden und Variablen für alle erbenden Actions bereitstellt. Hier werden u.a. das Benutzerobjekt aus der
 * Session geladen, die Logging-Routine bereitgestellt und Konfigurationsdaten geladen.
 */
public class BasisAction extends ActionSupport implements Preparable {

	private static final long				serialVersionUID				= -8050058912238071018L;
	// Vollständiges Benutzerobjekt, dass den Anmeldezustand und damit Verbundene Daten bereitstellt
	protected Benutzer						sessionBenutzer;
	// Enthält die session des Users
	protected Map<String, Object>			session;
	// Propoerties-Objekt das die gesamte Systemkonfiguration enthält
	protected Properties					konfiguration;
	// NachrichtenDAO um zu testen, ob neue Nachrichten vorliegen
	private final NachrichtDao				nachrichtDao					= new NachrichtDao();
	private final BenutzerDao				benutzerDao						= new BenutzerDao();
	// Systemnachricht (Statusmeldungen bei Aktionen)
	protected ArrayList<Benachrichtigung>	benachrichtigungListeSuccess	= new ArrayList<Benachrichtigung>();
	protected ArrayList<Benachrichtigung>	benachrichtigungListeInfo		= new ArrayList<Benachrichtigung>();
	protected ArrayList<Benachrichtigung>	benachrichtigungListeError		= new ArrayList<Benachrichtigung>();
	protected ArrayList<Benachrichtigung>	benachrichtigungListeDanger		= new ArrayList<Benachrichtigung>();
	// der Name der aufgerufenen Methode innerhalb einer *Action Klasse
	protected String						actionName;
	// Liste der möglichen Sortierungen
	protected HashMap<String, String>		sortierungen					= new HashMap<String, String>();
	// Liste der letzen 4 aufgerufenen URL's
	protected LinkedList<String>			actionHistoryStack;
	// Das Loggingobjekt, das Fehler und Debug-Informationen mitschreibt
	protected Logger						log;
	// Zeigt an, wie viele neue Nachrichten der Benutzer hat
	protected int							hatNachrichten					= 0;
	// Listet alle Vertretungsmöglichkeiten für Benutzer auf
	protected List<Benutzer>				vertreterListe					= new LinkedList<Benutzer>();
	// Seitenlänge aus Konfiguration
	protected int							seitenlaenge					= 0;
	// Seiten/Sortierung über URL Parameter
	protected int							seite							= 0;
	protected String						richtung;
	protected String						spalte;
	// Anzahl alle Elemente einer Liste/Tabelle in der Datenbank
	protected int							listeAnzahlElemente				= 0;
	// Spalten nach denen sich in Listen/Tabellen sortieren lässt
	protected final HashMap<String, String>	listeSortSpalten				= new HashMap<String, String>();
	// Liste der Institute
	protected HashMap<Integer, String>		instituteListe					= new HashMap<Integer, String>();
	// Liste der Benutzergruppen
	protected HashMap<Integer, String>		benutzergruppenListe			= new HashMap<Integer, String>();
	// Liste aller Anreden
	protected HashMap<Integer, String>		anredenListe					= new HashMap<Integer, String>();

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	@Override
	public void prepare() throws Exception {
		// Laden der Konfiguration
		konfiguration = getPropertiesObjekt("delosconfig");
		// Seitenlänge ausgliedern (für JSPs)
		seitenlaenge = Integer.parseInt(konfiguration.getProperty("allgemein.eintraegeProSeite").trim());
		// log = LogFactory.getLog(this.getClass().getName()); //commons logging
		// Laden der Logging Routine
		log = Logger.getLogger(this.getClass().getName());
		log.debug("rufe auf: " + this.getClass().getName());

		// weise die Sortiermöglichkeiten zu
		sortierungen.put("asc", "ASC");
		sortierungen.put("desc", "DESC");
		// globales validieren der Sortierrichtung
		if (null == richtung || null == sortierungen.get(richtung)) {
			richtung = "asc";
		}

		// Variable um festzustellen, ob ein Benutzer neue Nachrichten hat und erstellt Liste der Vertretungsmöglichkeiten
		if (sessionBenutzer != null && sessionBenutzer.getId() > 0) {
			hatNachrichten = nachrichtDao.getAnzahl(new String[] { "empfaenger_id", "status", "empfaenger_loeschStatus" },
					new String[] { sessionBenutzer.getId() + "", "3", "2" });
			if (sessionBenutzer.getGruppeId() == 3 || sessionBenutzer.getGruppeId() == 4) {
				vertreterListe = benutzerDao.getAlleAlsListe("vertreter_id", sessionBenutzer.getId() + "", "`nachname`", "asc");
			}
		}

		sessionLaden();
	}

	/**
	 * Lädt Properties aus einer Datei {@code dateiname}
	 * 
	 * @param dateiname
	 * @return Angeforderte Properties
	 * @throws IOException
	 */
	private Properties getPropertiesObjekt(final String dateiname) throws IOException {
		Properties props = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(dateiname + ".properties"); // per Classloader wegen Pfad
		try {
			props.load(is);
		} catch (IOException e) {
			log.error("properties laden fehlgeschlagen", e);
			throw new IOException();
		}
		return props;
	}

	/**
	 * Überprüft, ob die im {@code beanproperties} übergebenen Array HTML enthalten und setzt in dem Fall entsprechende Formularfehler
	 * 
	 * @param bean
	 *            das zu überprüfende Bean
	 * @param beanproperties
	 *            die im bean zu testenden Properties
	 * @throws Exception
	 */
	protected void checkBeanHTML(final Object bean, final List<String> beanproperties) throws Exception {
		// Teste für jeden String aus dem Array
		for (String bp : beanproperties) {
			// ob eine entsprechende Methode dazu existiert
			String methodName = "get" + String.valueOf(bp.charAt(0)).toUpperCase() + bp.substring(1);
			java.lang.reflect.Method method;
			try {
				method = bean.getClass().getMethod(methodName);
			} catch (Exception e) {
				log.fatal("HTML-Check fehlgeschlagen", e);
				throw new Exception(e);
			}
			//
			String beanname = bean.getClass().getName().toLowerCase();
			beanname = beanname.substring(beanname.lastIndexOf(".") + 1);
			// und dann, ob das Property HTML enthält
			try {
				// wenn ja, dann wird ein entsprechender Fehler erstellt
				if (!Jsoup.isValid((String) method.invoke(bean), Whitelist.none())) {
					addFieldError(beanname + "." + bp, this.getText("Validierung.html"));
				}
			} catch (Exception e) {
				log.fatal("HTML-Check fehlgeschlagen", e);
				throw new Exception(e);
			}
		}
	}

	/**
	 * überprüft alle Klassenvariablen einer Bean auf HTML-Inhalte
	 * 
	 * @param bean
	 * @throws Exception
	 */
	protected void checkBeanHTML(final Object bean) throws Exception {
		Field[] klassenvariablen = bean.getClass().getDeclaredFields();

		LinkedList<String> varList = new LinkedList<String>();
		for (int i = 1; i < klassenvariablen.length; i++) { // beginn bei 1, [0] ist serialVersionUID
			if (klassenvariablen[i].toString().contains("String")) {
				varList.add(klassenvariablen[i].getName());
			}
		}

		checkBeanHTML(bean, varList);
	}

	/**
	 * Entfernt alle Fehlermeldungen zu Eingabe, die in der Validierung auftreten können.
	 * 
	 * @param bean
	 * @param str
	 */
	protected void fieldErrorsEntfernen(final String bean, final String[] str) {
		Map<String, List<String>> map = getFieldErrors();
		for (String fieldname : str) {
			map.remove(bean + "." + fieldname);
			map.remove(fieldname); // falls fielderror nicht zur beanproperty sondern zur Klassenvar gehörte
		}
		setFieldErrors(map);
	}

	/**
	 * Entfernt alle Fehlermeldungen zu Eingabe, die in der Validierung auftreten können.
	 */
	protected void fieldErrorsEntfernen() {
		Map<String, List<String>> map = getFieldErrors();
		map.clear();
		setFieldErrors(map);
	}

	/**
	 * Entfernt doppelte Validierungsfehlermeldungen (hervorgerufen durch mehrfache field-validators)
	 */
	protected void fieldErrorsDuplikateEntfernen() {
		Map<String, List<String>> fieldErrorMap = getFieldErrors();
		for (Map.Entry<String, List<String>> entry : fieldErrorMap.entrySet()) {
			List<String> list = entry.getValue();
			for (int i = 0; i < list.size() - 1; i++) {
				for (int j = i + 1; j < list.size(); j++) {
					if (list.get(i).equals(list.get(j))) {
						list.remove(j);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @return gibt den Namen der gerade aufgerufenen Methode zurück
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * 
	 * @return sagt aus, ob die Session einen angemeldeten Benutzer enhält
	 */
	protected boolean benutzerAngemeldet() {
		if (sessionBenutzer.getId() == 0) { return false; }
		return true;
	}

	/**
	 * Liest ein Velocity Template aus, füllt alle zugehörigen {@code variablen} ein und gibt einen String mit dem Ergebnis zurück
	 * 
	 * @param variablen
	 * @param templateName
	 * @return Das ausgefüllte Velocity Template als String
	 */
	public String velocityTemplateAuslesen(final HashMap<?, ?> variablen, int sprache, final String templateName) {
		String locale = "";

		if (sprache != 1) {
			locale = "-EN";
		}

		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine ve = new VelocityEngine();
		ve.init(props);
		Template velocityTemplate = ve.getTemplate("../templates/" + templateName + locale + ".vm", "UTF-8");

		VelocityContext context = new VelocityContext();

		context.put("context", variablen);

		StringWriter writer = new StringWriter();
		velocityTemplate.merge(context, writer);

		return writer.toString();
	}

	public void versendeSystemnachricht(int empfaengerId, BewerbungsVorgang vorgang, String templateName, String betreff) throws SQLException {
		Benutzer empfaenger = benutzerDao.getEinzeln(empfaengerId);
		Nachricht nachricht = new Nachricht();

		HashMap<String, String> contextMap = new HashMap<String, String>();

		contextMap.put("ausschreiberName", vorgang.getAusschreiberName());
		contextMap.put("bewerberName", vorgang.getBewerberName());
		contextMap.put("bearbeiterName", vorgang.getBearbeiterName());
		contextMap.put("ausschreibungName", vorgang.getAusschreibungName());

		nachricht.setAbsenderId(0);
		nachricht.setEmpfaengerId(empfaengerId);
		nachricht.setBetreff(betreff);
		nachricht.setText(velocityTemplateAuslesen(contextMap, empfaenger.getNationalitaet(), templateName));
		nachrichtDao.erstellen(nachricht);
	}

	/**
	 * Fügt Systemnachrichten in die Session ein, um diese über Seitenwechsel hinweg zu erhalten
	 */
	private void benachrichtigungenInSessionAblegen() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (!benachrichtigungListeSuccess.isEmpty()) {
			session.put("benachrichtigungenSuccess", benachrichtigungListeSuccess);
		}
		if (!benachrichtigungListeInfo.isEmpty()) {
			session.put("benachrichtigungenInfo", benachrichtigungListeInfo);
		}
		if (!benachrichtigungListeError.isEmpty()) {
			session.put("benachrichtigungenError", benachrichtigungListeError);
		}
		if (!benachrichtigungListeDanger.isEmpty()) {
			session.put("benachrichtigungenDanger", benachrichtigungListeDanger);
		}
		log.debug("Benachrichtigungen in Session abgelegt.");
	}

	/**
	 * Erstellt eine Systembenachrichtigung vom Typ {@code typ} mit dem Inhalt {@code nachricht}. Zu beachten ist, dass diese neu erstellte Nachricht nicht
	 * automatisch zum Erhalt über einen Seitenwechsel hinweg in die Session abgelegt wird.
	 * 
	 * @param typ
	 *            der typ der Nachricht: success, error, danger, info
	 * @param nachricht
	 *            die Nachricht, die dem Benutzer angezeigt werden soll
	 */
	protected void benachrichtigungErzeugen(final String typ, final String nachricht) {
		Benachrichtigung b = new Benachrichtigung(typ, nachricht);
		if (typ.equals("success")) {
			benachrichtigungListeSuccess.add(b);
		}
		if (typ.equals("info")) {
			benachrichtigungListeInfo.add(b);
		}
		if (typ.equals("error")) {
			benachrichtigungListeError.add(b);
		}
		if (typ.equals("danger")) {
			benachrichtigungListeDanger.add(b);
		}
		benachrichtigungenInSessionAblegen();
	}

	/**
	 * Erstellt eine Systembenachrichtigung vom Typ {@code typ} mit dem Inhalt {@code nachricht} und dem Titel {@code titel}. Zu beachten ist, dass diese neu
	 * erstellte Nachricht nicht automatisch zum Erhalt über einen Seitenwechsel hinweg in die Session abgelegt wird.
	 * 
	 * @param typ
	 *            der typ der Nachricht: success, error, danger, info
	 * @param titel
	 *            der Titel der Nachricht, die dme Benutzer angezeigt werden soll
	 * @param nachricht
	 *            die Nachricht, die dem Benutzer angezeigt werden soll
	 */
	protected void benachrichtigungErzeugen(final String typ, final String titel, final String nachricht) {
		Benachrichtigung b = new Benachrichtigung(typ, titel, nachricht);
		if (typ.equals("success")) {
			benachrichtigungListeSuccess.add(b);
		}
		if (typ.equals("info")) {
			benachrichtigungListeInfo.add(b);
		}
		if (typ.equals("error")) {
			benachrichtigungListeError.add(b);
		}
		if (typ.equals("danger")) {
			benachrichtigungListeDanger.add(b);
		}
		benachrichtigungenInSessionAblegen();
	}

	/**
	 * Schreibt alle Feldfehler der Validierung in das Logfile
	 */
	protected void logFieldErrors() {
		if (hasFieldErrors()) {
			Map<String, List<String>> fieldErrorMap = getFieldErrors();
			for (Map.Entry<String, List<String>> entry : fieldErrorMap.entrySet()) {
				log.debug("Validierung fehlgeschlagen: " + entry.getKey() + " : " + entry.getValue());
			}
		}
	}

	/**
	 * Lädt die HTTP-Session in eine entsprechende Variable
	 */
	private void sessionLaden() {
		session = ActionContext.getContext().getSession();
	}

	/*
	 * Getter und Setter
	 */

	/**
	 * 
	 * @return gibt die Einstellungen zurück, die im {@code prepare()} Aufruf geladen wurden
	 */
	public Properties getKonfiguration() {
		return konfiguration;
	}

	/**
	 * Stellt die Konfigurationsdaten als Klassenvariable zur Verfügung
	 * 
	 * @param konfiguration
	 */
	public void setKonfiguration(final Properties konfiguration) {
		this.konfiguration = konfiguration;
	}

	/**
	 * 
	 * @return gibt das aus der Session geladene Benutzerobjekt zurück
	 */
	public Benutzer getSessionBenutzer() {
		return sessionBenutzer;
	}

	/**
	 * Stellt das Benutzerobjekt aus der Session als Klassenvariable zur Verfügung
	 * 
	 * @param sessionBenutzer
	 */
	public void setSessionBenutzer(final Benutzer sessionBenutzer) {
		this.sessionBenutzer = sessionBenutzer;
	}

	/**
	 * Stellt den Namen der aufgerufenen Methode als Klassenvariable zur Verfügung
	 * 
	 * @param actionName
	 */
	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return the actionHistoryStack
	 */
	public LinkedList<String> getActionHistoryStack() {
		return actionHistoryStack;
	}

	/**
	 * @param actionHistoryStack
	 *            the actionHistoryStack to set
	 */
	public void setActionHistoryStack(final LinkedList<String> actionHistoryStack) {
		this.actionHistoryStack = actionHistoryStack;
	}

	/**
	 * @return the hatNachrichten
	 */
	public int getHatNachrichten() {
		return hatNachrichten;
	}

	/**
	 * @return the seitenlaenge
	 */
	public int getSeitenlaenge() {
		return seitenlaenge;
	}

	/**
	 * @return the instituteListe
	 */
	public HashMap<Integer, String> getInstituteListe() {
		int i = 1;
		while (!getText("Institut." + i).equals("Institut." + i)) {
			instituteListe.put(i, getText("Institut." + i));
			i++;
		}
		return instituteListe;
	}

	/**
	 * @return the benutzergruppenListe
	 */
	public HashMap<Integer, String> getBenutzergruppenListe() {
		benutzergruppenListe.put(1, getText("Benutzer.gruppeId.1"));
		benutzergruppenListe.put(2, getText("Benutzer.gruppeId.2"));
		benutzergruppenListe.put(3, getText("Benutzer.gruppeId.3"));
		benutzergruppenListe.put(4, getText("Benutzer.gruppeId.4"));
		return benutzergruppenListe;
	}

	/**
	 * @return the anredenListe
	 */
	public HashMap<Integer, String> getAnredenListe() {
		anredenListe.put(1, getText("benutzer.anrede.mann"));
		anredenListe.put(2, getText("benutzer.anrede.frau"));
		return anredenListe;
	}

	/**
	 * @return the vertreterListe
	 */
	public List<Benutzer> getVertreterListe() {
		return vertreterListe;
	}

	/**
	 * @return the listeAnzahlElemente
	 */
	public int getListeAnzahlElemente() {
		return listeAnzahlElemente;
	}

	/**
	 * @return the listeSortSpalten
	 */
	public HashMap<String, String> getListeSortSpalten() {
		return listeSortSpalten;
	}

	/**
	 * @return the seite
	 */
	public int getSeite() {
		return seite;
	}

	/**
	 * @param seite
	 *            the seite to set
	 */
	public void setSeite(final int seite) {
		this.seite = seite;
	}

	/**
	 * @return the richtung
	 */
	public String getRichtung() {
		return richtung;
	}

	/**
	 * @param richtung
	 *            the richtung to set
	 */
	public void setRichtung(final String richtung) {
		this.richtung = richtung;
	}

	/**
	 * @return the spalte
	 */
	public String getSpalte() {
		return spalte;
	}

	/**
	 * @param spalte
	 *            the spalte to set
	 */
	public void setSpalte(final String spalte) {
		this.spalte = spalte;
	}
}