package de.uulm.sopra.delos.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import de.uulm.sopra.delos.action.BasisAction;
import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.dao.BenutzerDao;

/**
 * Sorgt im System für eine zulässiges Session für die Benutzer- und Rechtekontrolle. Außerdem wird hier die Sprachauswahl verifiziert. Darüberhinaus wird hier
 * die Datenbankverbindung für das ganze System gestartet und nach dem Ausführen der Anfrage beendet. Um Fehlanfragen bearbeiten zu können wird hier auch ein
 * Historystack angelegt und verwaltet, der vorhergehende Anfragen enthält.
 */
public class AuthentifizierungsInterceptor implements Interceptor {

	private static final long				serialVersionUID	= 7581731943623765042L;

	private Logger							log;
	private final HashMap<String, String>	locales				= new HashMap<String, String>();

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#destroy()
	 */
	@Override
	public void destroy() {}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#init()
	 */
	@Override
	public void init() {
		log = Logger.getLogger(this.getClass().getName());

		locales.put("de_DE", "German");
		locales.put("de_SW", "Swabian");
		locales.put("en_US", "English (United States)");
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
	@Override
	public String intercept(final ActionInvocation actionInvocation) throws Exception {
		// öffnet für die komplette Anwendung die Datenbankanwendung
		Datenbank.getInstance();

		// Sessionobjekt holen
		Map<String, Object> session = actionInvocation.getInvocationContext().getSession();

		// Lade den korrekten request locale für Internationalisierung und packe den für alles weitere in die session
		Locale locale = validiereLocale(actionInvocation.getInvocationContext().getLocale());
		actionInvocation.getInvocationContext().setLocale(locale);
		log.debug("Verwendete Sprache: " + actionInvocation.getInvocationContext().getLocale());

		// leeren Benutzer für Session anlegen
		Benutzer b;
		// fehlerhafte Benutzerobjekte aus Session löschen
		purgeStaleTokens(session);

		// Sorgt dafür, dass es immer ein korrektes Benutzerobjekt gibt in den Sessiondaten, egal ob neuer Besucher oder nicht.
		if (!session.containsKey("angemeldeterBenutzer")) {
			session.put("angemeldeterBenutzer", new Benutzer());
		} else {
			// Versucht ein aktuelles Benutzerobjekt zu erhalten (korrekte Rechte)
			if (0 < ((Benutzer) session.get("angemeldeterBenutzer")).getId()) {
				session.put("angemeldeterBenutzer", new BenutzerDao().getEinzeln(((Benutzer) session.get("angemeldeterBenutzer")).getId()));
			} else {
				session.put("angemeldeterBenutzer", new Benutzer());
			}
		}
		// korrektes Benutzerobjekt in session packen
		b = (Benutzer) session.get("angemeldeterBenutzer");

		// session mit weiteren Standardvariablen befüllen
		if (!session.containsKey("istVertreter")) {
			session.put("istVertreter", false);
		}

		/*
		 * BEGIN AUTH: Ab hier wird Zugriff auf die aufgerufene Action/Methode überprüft
		 */
		Action action = (Action) actionInvocation.getAction();
		// Erstelle ein Array mit ASPEKT / ACTION aufruf strings
		String[] actionName = actionInvocation.getInvocationContext().getName().toLowerCase().split("_");

		if (actionName.length < 2) { return Action.ERROR; }

		((BasisAction) action).setActionHistoryStack(actionHistoryStack(actionInvocation, action, session));

		// zugriffsrechte.properties laden
		Properties props = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("zugriffsrechte.properties"); // per Classloader wegen Pfad
		try {
			props.load(is);
		} catch (IOException e) {
			log.fatal("Zugriffsrechte laden fehlgeschlagen", e);
			throw new Exception();
		}

		// Lade die Zugriffsrechte aus der package.properties in *.action
		String actionRecht = "rechte." + b.getGruppeId() + "." + actionName[0];
		String methodenRecht = "rechte." + b.getGruppeId() + "." + actionName[0] + "." + actionName[1];
		String actionZugriff = props.getProperty(actionRecht);
		String methodenZugriff = props.getProperty(methodenRecht);

		// Zugriff testen, ggf. auf Login verweisen
		if (null == actionZugriff || (!actionZugriff.equals("true") && !actionZugriff.equals(actionRecht))) {
			if (session.get("loggedIn") != null) { throw new ZugriffsException(); }

			log.debug("verweigere Zugriff auf Action");

			return Action.LOGIN;
		}
		if (null != methodenZugriff && !methodenZugriff.equals("true") && !methodenZugriff.equals(methodenRecht)) {
			if (session.get("loggedIn") != null) { throw new ZugriffsException(); }

			log.debug("verweigere Zugriff auf Methode");

			return Action.LOGIN;
		}
		/*
		 * ENDE AUTH
		 */

		// Injiziere Methoden Namen und Session in die Action
		((BasisAction) action).setActionName(Character.toUpperCase(actionName[0].charAt(0)) + actionName[0].substring(1) + "_" + actionName[1]);
		((BasisAction) action).setSessionBenutzer(b);

		// macht mit der Aktion weiter und schließt die DB am ende
		String result = actionInvocation.invoke();
		Datenbank.getInstance().close();
		return result;
	}

	private LinkedList<String> loadHistoryStackFromSession(Map<String, Object> session) {
		LinkedList<String> actionHistoryStack = new LinkedList<String>();
		actionHistoryStack.add((String) session.get("actionHistoryStack1"));
		actionHistoryStack.add((String) session.get("actionHistoryStack2"));
		actionHistoryStack.add((String) session.get("actionHistoryStack3"));

		return actionHistoryStack;
	}

	private void saveActionHistoryStackToSession(Map<String, Object> session, LinkedList<String> actionHistoryStack) {
		session.put("actionHistoryStack1", actionHistoryStack.get(0));
		session.put("actionHistoryStack2", actionHistoryStack.get(1));
		session.put("actionHistoryStack3", actionHistoryStack.get(2));
	}

	/**
	 * Sorgt dafür, dass ein automatischer redirect auf zuvor besuchte Seiten möglich ist.
	 * 
	 * @param actionInvocation
	 * @param action
	 */
	private LinkedList<String> actionHistoryStack(final ActionInvocation actionInvocation, final Action action, Map<String, Object> session) throws Exception {
		LinkedList<String> actionHistoryStack;

		if (session.containsKey("actionHistoryStackActive") && session.get("actionHistoryStackActive") == "true") {
			actionHistoryStack = loadHistoryStackFromSession(session);

		} else {
			session.put("actionHistoryStackActive", "true");

			actionHistoryStack = new LinkedList<String>();

			actionHistoryStack.add("Seite_index.action");
			actionHistoryStack.add("Seite_index.action");
			actionHistoryStack.add("Seite_index.action");
		}

		// Hole aktuelle Query und Action
		HttpServletRequest request = ServletActionContext.getRequest();
		String currentQuery = request.getQueryString() != null ? "?" + request.getQueryString() : "";
		String currentAction = actionInvocation.getInvocationContext().getName();
		String currentActionQuery = currentAction + ".action" + currentQuery;

		// Falle neue Seite aufgerufen, wird die Älteste entfernt
		// und die Aktuelle chronologisch hinzugefügt
		if (!currentActionQuery.equals(actionHistoryStack.getLast())) {
			if (!(currentAction.equals("Benutzer_anmelden") || currentAction.equals("Benutzer_abmelden") || currentAction.equals("Benutzer_vertreterAnmelden")
					|| currentAction.equals("Benutzer_registrieren") || currentAction.equals("Benutzer_passwortAnfordern") || currentActionQuery
						.equals("Benutzer_aktualisieren.action"))) {
				actionHistoryStack.removeFirst();
				actionHistoryStack.add(currentActionQuery);
			}
		}

		saveActionHistoryStackToSession(session, actionHistoryStack);

		return actionHistoryStack;
	}

	/**
	 * Sorgt dafür, dass unzulässige Sessions gelöscht werden
	 * 
	 * @param session
	 */
	private void purgeStaleTokens(final Map<String, Object> session) {

		Object userToken = session.get("angemeldeterBenutzer");
		if (!(userToken instanceof Benutzer)) {
			session.remove("angemeldeterBenutzer");
		}

	}

	/**
	 * Sorgt dafür, dass nur im System vorhandene Sprachen ausgewählt werden können
	 * 
	 * @param session
	 */
	private Locale validiereLocale(final Locale locale) {

		if (null == locales.get(locale.toString())) {
			Locale.setDefault(new Locale("de", "DE"));
			return new Locale("de", "DE");
		}

		return locale;
	}
}