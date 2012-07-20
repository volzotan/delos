package de.uulm.sopra.delos.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * Dies ist die Bean zu den Ausschreibungen. Hier werden die für eine Ausschreibung relevanten Variablen mittels getter und setter geschrieben und gelesen
 */
public class Ausschreibung implements Serializable {

	private static final long	serialVersionUID	= 8163819362700457923L;

	private int					id					= 0;
	private int					ausschreiberId		= 0;
	private String				ausschreiberName	= "";
	private int					bearbeiterId		= -1;
	private String				bearbeiterName		= "";
	private String				name				= "";
	private String				beschreibung		= "";
	private int					institut			= 0;
	private int					stundenzahl			= 40;
	private int					stellenzahl			= 0;
	private int					belegt				= 0;
	private String				voraussetzungen		= "";
	private Date				startet				= new Date(new java.util.Date().getTime());
	private Date				endet				= new Date(new java.util.Date().getTime());
	private Date				bewerbungsfrist		= new Date(new java.util.Date().getTime());
	private int					status				= 0;
	private Date				hinzugefuegt		= null;
	private Date				bearbeitet			= null;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * @return the ausschreiberId
	 */
	public int getAusschreiberId() {
		return ausschreiberId;
	}

	/**
	 * @param ausschreiberId
	 *            the ausschreiberId to set
	 */
	public void setAusschreiberId(final int ausschreiberId) {
		this.ausschreiberId = ausschreiberId;
	}

	/**
	 * @return the bearbeiterId
	 */
	public int getBearbeiterId() {
		return bearbeiterId;
	}

	/**
	 * @param bearbeiterId
	 *            the bearbeiterId to set
	 */
	public void setBearbeiterId(final int bearbeiterId) {
		this.bearbeiterId = bearbeiterId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * @param beschreibung
	 *            the beschreibung to set
	 */
	public void setBeschreibung(final String beschreibung) {
		this.beschreibung = beschreibung;
	}

	/**
	 * @return the institut
	 */
	public int getInstitut() {
		return institut;
	}

	/**
	 * @param institut
	 *            the institut to set
	 */
	public void setInstitut(final int institut) {
		this.institut = institut;
	}

	/**
	 * @return the stundenzahl
	 */
	public int getStundenzahl() {
		return stundenzahl;
	}

	/**
	 * @param stundenzahl
	 *            the stundenzahl to set
	 */
	public void setStundenzahl(final int stundenzahl) {
		this.stundenzahl = stundenzahl;
	}

	/**
	 * @return the stellenzahl
	 */
	public int getStellenzahl() {
		return stellenzahl;
	}

	/**
	 * @param stellenzahl
	 *            the stellenzahl to set
	 */
	public void setStellenzahl(final int stellenzahl) {
		this.stellenzahl = stellenzahl;
	}

	/**
	 * @return the voraussetzungen
	 */
	public String getVoraussetzungen() {
		return voraussetzungen;
	}

	/**
	 * @param voraussetzungen
	 *            the voraussetzungen to set
	 */
	public void setVoraussetzungen(final String voraussetzungen) {
		this.voraussetzungen = voraussetzungen;
	}

	/**
	 * @return the startet
	 */
	public Date getStartet() {
		return startet;
	}

	/**
	 * @param startet
	 *            the startet to set
	 */
	public void setStartet(final Date startet) {
		this.startet = startet;
	}

	/**
	 * @return the endet
	 */
	public Date getEndet() {
		return endet;
	}

	/**
	 * @param endet
	 *            the endet to set
	 */
	public void setEndet(final Date endet) {
		this.endet = endet;
	}

	/**
	 * @return the hinzugefuegt
	 */
	public Date getHinzugefuegt() {
		return hinzugefuegt;
	}

	/**
	 * @param hinzugefuegt
	 *            the hinzugefuegt to set
	 */
	public void setHinzugefuegt(final Date hinzugefuegt) {
		this.hinzugefuegt = hinzugefuegt;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final int status) {
		this.status = status;
	}

	/**
	 * @return the bearbeitet
	 */
	public Date getBearbeitet() {
		return bearbeitet;
	}

	/**
	 * @param bearbeitet
	 *            the bearbeitet to set
	 */
	public void setBearbeitet(final Date bearbeitet) {
		this.bearbeitet = bearbeitet;
	}

	/**
	 * @return the ausschreiberName
	 */
	public String getAusschreiberName() {
		return ausschreiberName;
	}

	/**
	 * @param ausschreiberName
	 *            the ausschreiberName to set
	 */
	public void setAusschreiberName(final String ausschreiberName) {
		this.ausschreiberName = ausschreiberName;
	}

	/**
	 * @return the bearbeiterName
	 */
	public String getBearbeiterName() {
		return bearbeiterName;
	}

	/**
	 * @param bearbeiterName
	 *            the bearbeiterName to set
	 */
	public void setBearbeiterName(final String bearbeiterName) {
		this.bearbeiterName = bearbeiterName;
	}

	/**
	 * @return the bewerbungsfrist
	 */
	public Date getBewerbungsfrist() {
		return bewerbungsfrist;
	}

	/**
	 * @param bewerbungsfrist
	 *            the bewerbungsfrist to set
	 */
	public void setBewerbungsfrist(final Date bewerbungsfrist) {
		this.bewerbungsfrist = bewerbungsfrist;
	}

	/**
	 * @return the belegt
	 */
	public int getBelegt() {
		return belegt;
	}

	/**
	 * @param belegt
	 *            the belegt to set
	 */
	public void setBelegt(final int belegt) {
		this.belegt = belegt;
	}

	/**
	 * @return Anzahl der freien Stellen
	 */
	public int getFrei() {
		return stellenzahl - belegt;
	}
}
