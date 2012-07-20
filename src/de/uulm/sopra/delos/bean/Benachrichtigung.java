package de.uulm.sopra.delos.bean;

import java.io.Serializable;

/**
 * BeanKlasse um Erfolgs- oder Misserfolgsmitteilungen im Benutzerinterface anzuzeigen
 * 
 */
public class Benachrichtigung implements Serializable {

	private static final long	serialVersionUID	= 2963199492820576004L;
	// Typ der Nachricht: success, error, danger, info
	private String				typ;
	// Titel der anzuzeigenden Nachricht
	private String				titel;
	// Ausführlicher Inhalt der anzuzeigenden Nachricht
	private String				nachricht;

	/**
	 * Erstellt eine leere Benachrichtigung
	 */
	public Benachrichtigung() {}

	/**
	 * Erstellt eine Benachrichtigung mit {@code typ} und {@code nachricht}
	 * 
	 * @param typ
	 * @param nachricht
	 */
	public Benachrichtigung(final String typ, final String nachricht) {
		this.typ = typ;
		this.nachricht = nachricht;
	}

	/**
	 * Erstellt eine Benachrichtigung mit {@code typ}, {@code titel} und {@code nachricht}
	 * 
	 * @param typ
	 * @param titel
	 * @param nachricht
	 */
	public Benachrichtigung(final String typ, final String titel, final String nachricht) {
		this.typ = typ;
		this.titel = titel;
		this.nachricht = nachricht;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = typ + ": " + titel + " " + nachricht;
		return str;
	}

	/**
	 * @return the typ
	 */
	public String getTyp() {
		return this.typ;
	}

	/**
	 * @param typ
	 *            the typ to set
	 */
	public void setTyp(final String typ) {
		this.typ = typ;
	}

	/**
	 * @return the titel
	 */
	public String getTitel() {
		return this.titel;
	}

	/**
	 * @param titel
	 *            the titel to set
	 */
	public void setTitel(final String titel) {
		this.titel = titel;
	}

	/**
	 * @return the nachricht
	 */
	public String getNachricht() {
		return this.nachricht;
	}

	/**
	 * @param nachricht
	 *            the nachricht to set
	 */
	public void setNachricht(final String nachricht) {
		this.nachricht = nachricht;
	}

}
