package de.uulm.sopra.delos.system;

/**
 * 
 * Allgemeine Zugriffsexception für unzulässige Aufrufe (Fehlende Benutzerrechte)
 * 
 */
public class ZugriffsException extends Exception {

	private static final long	serialVersionUID	= 5349374855948751338L;

	/**
	 * Konstruktor ZugriffsException
	 * 
	 */
	public ZugriffsException() {

	}

	/**
	 * Konstruktor ZugriffsException
	 * 
	 * @param msg
	 */
	public ZugriffsException(final String msg) {
		super(msg);
	}
}
