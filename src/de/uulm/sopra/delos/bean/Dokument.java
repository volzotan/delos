package de.uulm.sopra.delos.bean;

/**
 * 
 * Diese Bean kapselt alle Informationen, die ein Dokument repräsentieren.
 * 
 */
public class Dokument {

	private int		id				= 0;
	private String	name			= "";
	private String	beschreibung	= "";
	private int		standard		= 0;

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
	 * @return the standard
	 */
	public int getStandard() {
		return standard;
	}

	/**
	 * @param standard
	 *            the standard to set
	 */
	public void setStandard(final int standard) {
		this.standard = standard;
	}

}
