package de.uulm.sopra.delos.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Abfragekriterium speichert einzelne Bestandteile einer SQL Selectabfrage für sich zum automatischen generieren einer vollständigen, korrekten Abfrage
 * 
 * 
 */
public class Abfragekriterium implements Serializable {

	private static final long		serialVersionUID	= -1368107677019244288L;

	/**
	 * WHERE Bestandteil der SQL Abfrage
	 */
	private String					condition			= "";
	/**
	 * SELECT oder SELECT DISTINCT
	 */
	private boolean					distinct			= false;
	/**
	 * GROUP BY Bestandteil der SQL Abfrage
	 */
	private String					group				= "";
	/**
	 * HAVING Zusatzbestandteil, nur zusammen mit GROUP BY
	 */
	private String					having				= "";
	/**
	 * JOIN Bedingung für die Abfrage, z.B. LEFT JOIN users ON users.id=authorID
	 */
	private String					join				= "";
	/**
	 * Maximale Anzahl der wiederzugebenden Ergebnisse, limit < 0 heißt unendlich
	 */
	private int						limit;
	/**
	 * Startpunkt ab dem Ergebnisse zurückgegeben werden sollen
	 */
	private int						offset				= 0;
	/**
	 * ORDER BY Bestandteil der Abfrage
	 */
	private String					order				= "";
	/**
	 * SELECT Bestandteil der Abfrage, Liste der Spalten die auszugeben sind
	 */
	private String					select				= "*";

	/**
	 * Parameterübergabe wird bei Abfragen benutzt, die eine komplexe Wherebedingung mit Parametern von außen haben
	 */
	private Map<Integer, String>	parameter			= null;

	private final Logger			log					= Logger.getLogger(this.getClass().getName());	;

	/**
	 * Der Konstruktor, hier wird aus der Konfiguration das Standardlimit für die Seitenlänge geladen
	 */
	public Abfragekriterium() {
		Properties props = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("delosconfig.properties"); // per Classloader wegen Pfad
		try {
			props.load(is);
		} catch (IOException e) {
			log.fatal("Abfragekriterium-Konfiguration laden fehlgeschlagen", e);
		}

		limit = Integer.parseInt(props.getProperty("allgemein.eintraegeProSeite").trim());
	}

	/**
	 * @return the condition WHERE Bestandteil der SQL Abfrage
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @return the distinct SELECT oder SELECT DISTINCT
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * @return the group GROUP BY Bestandteil der SQL Abfrage
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return the having HAVING Zusatzbestandteil zusammen mit GROUP BY
	 */
	public String getHaving() {
		return having;
	}

	/**
	 * @return the join JOIN Bedingung für die Abfrage, z.B. LEFT JOIN users ON users.id=authorID
	 */
	public String getJoin() {
		return join;
	}

	/**
	 * @return the limit Maximale Anzahl der wiederzugebenden Ergebnisse
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @return the offset Startpunkt ab dem Ergebnisse zurückgegeben werden sollen
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @return the order ORDER BY Bestandteil der Abfrage
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @return the select SELECT Bestandteil der Abfrage, Liste der Spalten die auszugeben sind
	 */
	public String getSelect() {
		return select;
	}

	/**
	 * @return the parameter
	 */
	public Map<Integer, String> getParameter() {
		return parameter;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(final Map<Integer, String> parameter) {
		this.parameter = parameter;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(final String condition) {
		this.condition = condition;
	}

	/**
	 * Hängt zusätzliche Bedingungen an eine bestehende Bedingung an
	 * 
	 * @param condition
	 *            the condition to append to any existing condition
	 */
	public void appendCondition(final String condition) {
		if (0 < this.condition.length()) {
			this.condition = this.condition + " AND " + condition;
		} else {
			setCondition(condition);
		}
	}

	/**
	 * @param distinct
	 *            the distinct to set
	 */
	public void setDistinct(final boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(final String group) {
		this.group = group;
	}

	/**
	 * @param having
	 *            the having to set
	 */
	public void setHaving(final String having) {
		this.having = having;
	}

	/**
	 * @param join
	 *            the join to set
	 */
	public void setJoin(final String join) {
		this.join = join;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(final int limit) {
		this.limit = limit;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setOffset(final int offset) {
		this.offset = offset;
	}

	/**
	 * @param order
	 *            the order to set
	 */
	public void setOrder(final String order) {
		this.order = order;
	}

	/**
	 * @param select
	 *            the select to set
	 */
	public void setSelect(final String select) {
		this.select = select;
	}

}
