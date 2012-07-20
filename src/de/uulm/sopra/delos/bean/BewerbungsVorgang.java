package de.uulm.sopra.delos.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * Diese Bean kapselt alle einer Bewerbung zugeordneten Informationen.
 * 
 */
public class BewerbungsVorgang implements Serializable {

	private static final long	serialVersionUID	= -1044443983034903953L;

	private int					id					= 0;
	private int					bewerberId			= 0;
	private String				bewerberName		= "";
	private int					bearbeiterId		= 0;
	private String				bearbeiterName		= "";
	private int					ausschreiberId		= 0;
	private String				ausschreiberName	= "";

	private int					ausschreibungId		= 0;
	private String				ausschreibungName	= "";
	private int					institut			= 0;

	private int					stundenzahl			= 0;
	private Date				startet				= new Date(0);
	private Date				endet				= new Date(0);
	private int					status				= 0;
	private String				kommentar			= "";
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
	 * @return the kommentar
	 */
	public String getKommentar() {
		return kommentar;
	}

	/**
	 * @param kommentar
	 *            the kommentar to set
	 */
	public void setKommentar(final String kommentar) {
		this.kommentar = kommentar;
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
	 * @return the bewerberName
	 */
	public String getBewerberName() {
		return bewerberName;
	}

	/**
	 * @param bewerberName
	 *            the bewerberName to set
	 */
	public void setBewerberName(final String bewerberName) {
		this.bewerberName = bewerberName;
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
	 * @return the ausschreibungName
	 */
	public String getAusschreibungName() {
		return ausschreibungName;
	}

	/**
	 * @param ausschreibungName
	 *            the ausschreibungName to set
	 */
	public void setAusschreibungName(final String ausschreibungName) {
		this.ausschreibungName = ausschreibungName;
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

}
