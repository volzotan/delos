package de.uulm.sopra.delos.bean;

import java.io.Serializable;

/**
 * 
 * Diese Bean kapselt alle Informationen die ein Dokument im Zusammenhang mit einem Bewerbungsvorgang repräsentieren.
 * 
 */
public class BewerbungsVorgangDokument implements Serializable {

	private static final long	serialVersionUID	= -2210876155011966267L;

	private int					id;											// success, error, info
	private int					dokumentId;
	private String				dokumentName;
	private int					bewerbungsVorgangId;
	private String				ausschreibungName;
	private int					status;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the dokumentId
	 */
	public int getDokumentId() {
		return dokumentId;
	}

	/**
	 * @return the bewerbungsVorgangId
	 */
	public int getBewerbungsVorgangId() {
		return bewerbungsVorgangId;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * @param dokumentId
	 *            the dokumentId to set
	 */
	public void setDokumentId(final int dokumentId) {
		this.dokumentId = dokumentId;
	}

	/**
	 * @param bewerbungsVorgangId
	 *            the bewerbungsVorgangId to set
	 */
	public void setBewerbungsVorgangId(final int bewerbungsVorgangId) {
		this.bewerbungsVorgangId = bewerbungsVorgangId;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final int status) {
		this.status = status;
	}

	/**
	 * @return the dokumentName
	 */
	public String getDokumentName() {
		return dokumentName;
	}

	/**
	 * @param dokumentName
	 *            the dokumentName to set
	 */
	public void setDokumentName(final String dokumentName) {
		this.dokumentName = dokumentName;
	}

	/**
	 * @return the ausschreibung
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

}
