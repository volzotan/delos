package de.uulm.sopra.delos.dao;

import org.apache.log4j.Logger;

/**
 * Das StandardDao, dass an alle anderen vererbt wird und dort die Tabellennamen bereitstellt, sowie eine Methode, um aus einem Abfragekriterium eine
 * SELECT-Query zu generieren
 */
public class StandardDao {

	protected Logger	log;
	// nachfolgend die tabellen namen die in der Datenbank benutzt werden
	protected String	tPrefix	= "delos_";
	protected String	tA		= "`" + this.tPrefix + "Ausschreibung`";
	protected String	tB		= "`" + this.tPrefix + "Benutzer`";
	protected String	tBV		= "`" + this.tPrefix + "BewerbungsVorgang`";
	protected String	tBVD	= "`" + this.tPrefix + "BewerbungsVorgangDokument`";
	protected String	tD		= "`" + this.tPrefix + "Dokument`";
	protected String	tN		= "`" + this.tPrefix + "Nachricht`";

	/**
	 * Default Konstruktor um den Logging Mechanismus einzubinden
	 */
	public StandardDao() {
		this.log = Logger.getLogger(this.getClass().getName());
	}

	/**
	 * Generiert einen SELECT query, der alle Eintraege aus der Tabelle mit dem als String uebergebenen Tabellennamen t selektieren wuerde (wenn er auf die
	 * Datenbank angewendet werden wuerde), die den Bedingungen, die in dem uebergebenen Abfragekriterium bedingungen enthalten sind. Dieser Query wird dann als
	 * String zurueckgegeben.
	 * 
	 * @param t
	 * @param bedingungen
	 * @return die fertige Query
	 */
	public final String queryGenerieren(final String t, final Abfragekriterium bedingungen) {
		String distinct = "", where = "", groupBy = "", having = "", orderBy = "", limit = "";

		if (bedingungen.isDistinct()) {
			distinct = "DISTINCT ";
		}
		if (0 < bedingungen.getCondition().length()) {
			where = " WHERE " + bedingungen.getCondition();
		}
		if (0 < bedingungen.getGroup().length()) {
			groupBy = " GROUP BY " + bedingungen.getGroup();
			if (0 < bedingungen.getHaving().length()) {
				having = " HAVING " + bedingungen.getHaving();
			}
		}
		if (0 < bedingungen.getOrder().length()) {
			orderBy = " ORDER BY " + bedingungen.getOrder();
		}
		if (0 < bedingungen.getOffset() || 0 < bedingungen.getLimit()) {
			limit = " LIMIT ";
			if (0 < bedingungen.getOffset()) {
				limit += bedingungen.getOffset();
			}
			if (0 < bedingungen.getLimit()) {
				if (0 < bedingungen.getOffset()) {
					limit += ",";
				}
				limit += bedingungen.getLimit();
			}
		}

		this.log.debug(("SELECT " + distinct + bedingungen.getSelect() + " FROM " + t + " " + bedingungen.getJoin() + where + groupBy + having + orderBy + limit)
				.trim() + ";");

		return ("SELECT " + distinct + bedingungen.getSelect() + " FROM " + t + " " + bedingungen.getJoin() + where + groupBy + having + orderBy + limit)
				.trim() + ";";
	}

}
