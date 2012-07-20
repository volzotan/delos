package de.uulm.sopra.delos.system;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.uulm.sopra.delos.action.BasisAction;
import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.dao.BenutzerDao;

/**
 * ist eigentlich keine Action, erbt aber BasisAction wegen der Implementation des TextProvider-Interfaces
 * 
 */
public class Mailer extends BasisAction {

	private static final long	serialVersionUID	= 327080891546296395L;
	private final BenutzerDao	benutzerDao			= new BenutzerDao();

	/**
	 * Konstruktur, prepare wird aus der BasisAction aufgerufen, um die notwendigen Daten zur Verfügung zu stellen
	 * 
	 * @throws Exception
	 */
	public Mailer() throws Exception {
		prepare();
	}

	/**
	 * Verschickt die E-Mail mit einem neu generierten Passwort, falls ein neues angefordert wurde
	 * 
	 * @param empfaengerAdresse
	 * @param passwort
	 * @throws Exception
	 */
	public void passwortAnfordernMail(final String empfaengerAdresse, final String passwort) throws Exception {
		Benutzer benutzer = benutzerDao.getEinzeln("email", empfaengerAdresse);

		HashMap<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("passwort", passwort);

		Session session = Session.getDefaultInstance(konfiguration, new Authenticator());
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(konfiguration.getProperty("mail.absender.adresse")));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfaengerAdresse));
		message.setSubject(this.getText("Mailer.passwortVergessen.betreff"));
		message.setContent(velocityTemplateAuslesen(contextMap, benutzer.getNationalitaet(), "Mailer_passwortVergessen"), "text/html");
		Transport.send(message);
		log.debug("email gesendet an " + empfaengerAdresse);

	}

	/**
	 * Generiert ein zufälliges passwort
	 * 
	 * @return zufällig gewähltes passwort
	 */
	public String passwortGenerieren() {

		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}

	/**
	 * Verschickt das zufällig generierte Passwort für einen vom Admin erstellen Benutzer an diesen
	 * 
	 * @param empfaengerAdresse
	 * @param passwort
	 * @throws Exception
	 */
	public void passwortVerschicken(final String empfaengerAdresse, final String passwort) throws Exception {

		HashMap<String, String> contextMap = new HashMap<String, String>();
		contextMap.put("passwort", passwort);

		Session session = Session.getDefaultInstance(konfiguration, new Authenticator());
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(konfiguration.getProperty("mail.absender.adresse")));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfaengerAdresse));
		message.setSubject(this.getText("Mailer.passwortVerschicken.betreff"));
		message.setContent(velocityTemplateAuslesen(contextMap, 1, "Mailer_passwortVerschicken"), "text/html");
		Transport.send(message);
		log.debug("email gesendet an " + empfaengerAdresse);

	}

	/**
	 * Verbindung zum Mailserver herstellen
	 * 
	 */
	private class Authenticator extends javax.mail.Authenticator {

		private PasswordAuthentication	authentication;

		/**
		 * Verbindet delos mit dem Mailserver zum Verschicken der E-mails
		 */
		public Authenticator() {
			try {
				// private class kann vererbte konfigurationsProperties nicht nutzen, muss sie sich selbst holen
				Properties konfiguration = new Properties();
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("delosconfig.properties");
				konfiguration.load(is);

				authentication = new PasswordAuthentication(konfiguration.getProperty("mail.absender.adresse"),
						konfiguration.getProperty("mail.absender.passwort"));
			} catch (Exception e) {
				// nichts tun. Aufrufende Methode verarbeitet Exception
				Mailer.this.log.fatal("Email Authenticator erzeugen fehlgeschlagen", e);
			}
		}

		/*
		 * (non-Javadoc)
		 * @see javax.mail.Authenticator#getPasswordAuthentication()
		 */
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}

}
