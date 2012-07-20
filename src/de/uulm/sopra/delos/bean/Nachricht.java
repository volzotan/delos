package de.uulm.sopra.delos.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Diese Bean kapselt alle Informationen einer systeminterne Nachricht.
 * 
 */

public class Nachricht implements Serializable {

	private static final long	serialVersionUID		= -1559338208960009698L;

	private int					id						= 0;
	private int					absenderId				= 0;
	private String				absenderName			= "";
	private int					empfaengerId			= 0;
	private String				empfaengerName			= "";
	private String				text					= "";
	private String				betreff					= "";
	// 0:Gelesen & Beantwortet, 1:Ungelesen & Beantwortet, 2:Gelesen, 3:Ungelesen
	private int					status					= 0;
	private int					absenderLoeschStatus	= 0;
	private int					empfaengerLoeschStatus	= 0;
	private Timestamp			versanddatum			= null;

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
	 * @return the absender_id
	 */
	public int getAbsenderId() {
		return absenderId;
	}

	/**
	 * @param absenderId
	 *            the absenderId to set
	 */
	public void setAbsenderId(final int absenderId) {
		this.absenderId = absenderId;
	}

	/**
	 * @return the absenderName
	 */
	public String getAbsenderName() {
		return absenderName;
	}

	/**
	 * @param absenderName
	 *            the absenderName to set
	 */
	public void setAbsenderName(final String absenderName) {
		this.absenderName = absenderName;
	}

	/**
	 * @return the empfaenger_id
	 */
	public int getEmpfaengerId() {
		return empfaengerId;
	}

	/**
	 * @param empfaengerId
	 *            the empfaengerId to set
	 */
	public void setEmpfaengerId(final int empfaengerId) {
		this.empfaengerId = empfaengerId;
	}

	/**
	 * @return the empfaengerName
	 */
	public String getEmpfaengerName() {
		return empfaengerName;
	}

	/**
	 * @param empfaengerName
	 *            the empfaengerName to set
	 */
	public void setEmpfaengerName(final String empfaengerName) {
		this.empfaengerName = empfaengerName;
	}

	/**
	 * @return the titel
	 */
	public String getBetreff() {
		return betreff;
	}

	/**
	 * @param betreff
	 *            the betreff to set
	 */
	public void setBetreff(final String betreff) {
		this.betreff = betreff;
	}

	/**
	 * @return the inhalt
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(final String text) {
		this.text = text;
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
	 * @return the versanddatum
	 */
	public Timestamp getVersanddatum() {
		return versanddatum;
	}

	/**
	 * @param date
	 *            the versanddatum to set
	 */
	public void setVersanddatum(final Timestamp date) {
		versanddatum = date;
	}

	/**
	 * @return the absenderLoeschStatus
	 */
	public int getAbsenderLoeschStatus() {
		return absenderLoeschStatus;
	}

	/**
	 * @return the empfaengerLoeschStatus
	 */
	public int getEmpfaengerLoeschStatus() {
		return empfaengerLoeschStatus;
	}

	/**
	 * @param absenderLoeschStatus
	 *            the absenderLoeschStatus to set
	 */
	public void setAbsenderLoeschStatus(final int absenderLoeschStatus) {
		this.absenderLoeschStatus = absenderLoeschStatus;
	}

	/**
	 * @param empfaengerLoeschStatus
	 *            the empfaengerLoeschStatus to set
	 */
	public void setEmpfaengerLoeschStatus(final int empfaengerLoeschStatus) {
		this.empfaengerLoeschStatus = empfaengerLoeschStatus;
	}
}