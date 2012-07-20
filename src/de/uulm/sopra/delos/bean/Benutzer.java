package de.uulm.sopra.delos.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * Das Benutzerobjekt im System, wird aus der Datenbank befüllt. U.a. auch als Session Objekt benutzt um Zugriff auf die Daten des angemeldeten Benutzers zu
 * erhalten.
 * 
 */
public class Benutzer implements Serializable {

	private static final long	serialVersionUID	= 1038347322747229181L;

	public Benutzer() {}

	public Benutzer(final String vorname, final String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
	}

	private int		id					= 0;
	private int		gruppeId			= 0;
	private int		vertreterId			= 0;
	private String	vertreterName		= "";
	private int		institut			= 0;

	private String	email				= "";
	private String	nachname			= "";
	private String	vorname				= "";
	private String	passwort			= "";

	private String	telefon				= "";
	private String	raum				= "";
	private Date	geburtstag			= new Date(0);
	private int		geschlecht			= 0;
	private int		studiengang			= 0;
	private int		matrikelnummer		= 0;
	private int		nationalitaet		= 0;

	private String	strasse				= "";
	private String	hausnummer			= "";
	private String	postleitzahl		= "";
	private String	stadt				= "";
	private int		land				= 0;

	private String	strasseHeimat		= "";
	private String	hausnummerHeimat	= "";
	private String	postleitzahlHeimat	= "";
	private String	stadtHeimat			= "";
	private int		landHeimat			= 0;

	private Date	hinzugefuegt		= null;		// Datum
	private Date	bearbeitet			= null;		// Datum

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the gruppeId
	 */
	public int getGruppeId() {
		return this.gruppeId;
	}

	/**
	 * @return the vertreterId
	 */
	public int getVertreterId() {
		return this.vertreterId;
	}

	/**
	 * @return the fakultaet
	 */
	public int getInstitut() {
		return this.institut;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @return the nachname
	 */
	public String getNachname() {
		return this.nachname;
	}

	/**
	 * @return the vorname
	 */
	public String getVorname() {
		return this.vorname;
	}

	/**
	 * @return the passwort
	 */
	public String getPasswort() {
		return this.passwort;
	}

	/**
	 * @return the telefon
	 */
	public String getTelefon() {
		return this.telefon;
	}

	/**
	 * @return the geburtstag
	 */
	public Date getGeburtstag() {
		return this.geburtstag;
	}

	/**
	 * @return the geschlecht
	 */
	public int getGeschlecht() {
		return this.geschlecht;
	}

	/**
	 * @return the nationalitaet
	 */
	public int getNationalitaet() {
		return this.nationalitaet;
	}

	/**
	 * @return the strasse
	 */
	public String getStrasse() {
		return this.strasse;
	}

	/**
	 * @return the hausnummer
	 */
	public String getHausnummer() {
		return this.hausnummer;
	}

	/**
	 * @return the postleitzahl
	 */
	public String getPostleitzahl() {
		return this.postleitzahl;
	}

	/**
	 * @return the stadt
	 */
	public String getStadt() {
		return this.stadt;
	}

	/**
	 * @return the land
	 */
	public int getLand() {
		return this.land;
	}

	/**
	 * @return the strasseHeimat
	 */
	public String getStrasseHeimat() {
		return this.strasseHeimat;
	}

	/**
	 * @return the hausnummerHeimat
	 */
	public String getHausnummerHeimat() {
		return this.hausnummerHeimat;
	}

	/**
	 * @return the postleitzahlHeimat
	 */
	public String getPostleitzahlHeimat() {
		return this.postleitzahlHeimat;
	}

	/**
	 * @return the stadtHeimat
	 */
	public String getStadtHeimat() {
		return this.stadtHeimat;
	}

	/**
	 * @return the landHeimat
	 */
	public int getLandHeimat() {
		return this.landHeimat;
	}

	/**
	 * @return the hinzugefuegt
	 */
	public Date getHinzugefuegt() {
		return this.hinzugefuegt;
	}

	/**
	 * @return the bearbeitet
	 */
	public Date getBearbeitet() {
		return this.bearbeitet;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * @param gruppeId
	 *            the gruppeId to set
	 */
	public void setGruppeId(final int gruppeId) {
		this.gruppeId = gruppeId;
	}

	/**
	 * @param vertreterId
	 *            the vertreterId to set
	 */
	public void setVertreterId(final int vertreterId) {
		this.vertreterId = vertreterId;
	}

	/**
	 * @param institut
	 *            the institut to set
	 */
	public void setInstitut(final int institut) {
		this.institut = institut;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * @param nachname
	 *            the nachname to set
	 */
	public void setNachname(final String nachname) {
		this.nachname = nachname;
	}

	/**
	 * @param vorname
	 *            the vorname to set
	 */
	public void setVorname(final String vorname) {
		this.vorname = vorname;
	}

	/**
	 * @param passwort
	 *            the passwort to set
	 */
	public void setPasswort(final String passwort) {
		this.passwort = passwort;
	}

	/**
	 * @param telefon
	 *            the telefon to set
	 */
	public void setTelefon(final String telefon) {
		this.telefon = telefon;
	}

	/**
	 * @param geburtstag
	 *            the geburtstag to set
	 */
	public void setGeburtstag(final Date geburtstag) {
		this.geburtstag = geburtstag;
	}

	/**
	 * @param geschlecht
	 *            the geschlecht to set
	 */
	public void setGeschlecht(final int geschlecht) {
		this.geschlecht = geschlecht;
	}

	/**
	 * @param nationalitaet
	 *            the nationalitaet to set
	 */
	public void setNationalitaet(final int nationalitaet) {
		this.nationalitaet = nationalitaet;
	}

	/**
	 * @param strasse
	 *            the strasse to set
	 */
	public void setStrasse(final String strasse) {
		this.strasse = strasse;
	}

	/**
	 * @param hausnummer
	 *            the hausnummer to set
	 */
	public void setHausnummer(final String hausnummer) {
		this.hausnummer = hausnummer;
	}

	/**
	 * @param postleitzahl
	 *            the postleitzahl to set
	 */
	public void setPostleitzahl(final String postleitzahl) {
		this.postleitzahl = postleitzahl;
	}

	/**
	 * @param stadt
	 *            the stadt to set
	 */
	public void setStadt(final String stadt) {
		this.stadt = stadt;
	}

	/**
	 * @param land
	 *            the land to set
	 */
	public void setLand(final int land) {
		this.land = land;
	}

	/**
	 * @param strasseHeimat
	 *            the strasseHeimat to set
	 */
	public void setStrasseHeimat(final String strasseHeimat) {
		this.strasseHeimat = strasseHeimat;
	}

	/**
	 * @param hausnummerHeimat
	 *            the hausnummerHeimat to set
	 */
	public void setHausnummerHeimat(final String hausnummerHeimat) {
		this.hausnummerHeimat = hausnummerHeimat;
	}

	/**
	 * @param postleitzahlHeimat
	 *            the postleitzahlHeimat to set
	 */
	public void setPostleitzahlHeimat(final String postleitzahlHeimat) {
		this.postleitzahlHeimat = postleitzahlHeimat;
	}

	/**
	 * @param stadtHeimat
	 *            the stadtHeimat to set
	 */
	public void setStadtHeimat(final String stadtHeimat) {
		this.stadtHeimat = stadtHeimat;
	}

	/**
	 * @param landHeimat
	 *            the landHeimat to set
	 */
	public void setLandHeimat(final int landHeimat) {
		this.landHeimat = landHeimat;
	}

	/**
	 * @param hinzugefuegt
	 *            the hinzugefuegt to set
	 */
	public void setHinzugefuegt(final Date hinzugefuegt) {
		this.hinzugefuegt = hinzugefuegt;
	}

	/**
	 * @param bearbeitet
	 *            the bearbeitet to set
	 */
	public void setBearbeitet(final Date bearbeitet) {
		this.bearbeitet = bearbeitet;
	}

	/**
	 * @return the vertreterName
	 */
	public String getVertreterName() {
		return this.vertreterName;
	}

	/**
	 * @param vertreterName
	 *            the vertreterName to set
	 */
	public void setVertreterName(final String vertreterName) {
		this.vertreterName = vertreterName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = this.vorname + " " + this.nachname + " [id:" + this.id + "]";
		return str;
	}

	/**
	 * 
	 * @return den vollständigen Namen
	 */
	public String getName() {
		return this.vorname + " " + this.nachname;
	}

	/**
	 * @return the raum
	 */
	public String getRaum() {
		return raum;
	}

	/**
	 * @param raum
	 *            the raum to set
	 */
	public void setRaum(final String raum) {
		this.raum = raum;
	}

	/**
	 * @return the matrikelnummer
	 */
	public int getMatrikelnummer() {
		return matrikelnummer;
	}

	/**
	 * @param matrikelnummer
	 *            the matrikelnummer to set
	 */
	public void setMatrikelnummer(final int matrikelnummer) {
		this.matrikelnummer = matrikelnummer;
	}

	/**
	 * @return the studiengang
	 */
	public int getStudiengang() {
		return studiengang;
	}

	/**
	 * @param studiengang
	 *            the studiengang to set
	 */
	public void setStudiengang(final int studiengang) {
		this.studiengang = studiengang;
	}

}
