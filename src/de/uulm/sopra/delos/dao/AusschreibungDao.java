package de.uulm.sopra.delos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uulm.sopra.delos.bean.Ausschreibung;
import de.uulm.sopra.delos.system.Datenbank;

public class AusschreibungDao extends StandardDao implements AusschreibungDaoInterface {

	/**
	 * Liest alle Ausschreibungen aus der Datenbank aus, die den Bedingungen entsprechen und packt sie in eine LinkedList
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return LinkedList<Ausschreibung>
	 * @throws SQLException
	 */
	private List<Ausschreibung> getAlleAlsListe(final Abfragekriterium ak) throws SQLException {

		String query = this.queryGenerieren(this.tA, ak);

		List<Ausschreibung> l = new LinkedList<Ausschreibung>();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			Ausschreibung a = new Ausschreibung();

			a.setId(result.getInt("id"));
			a.setAusschreiberId(result.getInt("ausschreiber_id"));
			a.setAusschreiberName(result.getString("ausschreiber"));
			a.setBearbeiterId(result.getInt("bearbeiter_id"));
			a.setBearbeiterName(result.getString("bearbeiter"));
			a.setName(result.getString("name"));
			a.setBeschreibung(result.getString("beschreibung"));
			a.setInstitut(result.getInt("institut"));
			a.setStundenzahl(result.getInt("stundenzahl"));
			a.setStellenzahl(result.getInt("stellenzahl"));
			a.setBelegt(result.getInt("belegt"));
			a.setVoraussetzungen(result.getString("voraussetzungen"));
			a.setStartet(result.getDate("startet"));
			a.setEndet(result.getDate("endet"));
			a.setBewerbungsfrist(result.getDate("bewerbungsfrist"));
			a.setStatus(result.getInt("status"));
			a.setHinzugefuegt(result.getDate("hinzugefuegt"));
			a.setBearbeitet(result.getDate("bearbeitet"));

			l.add(a);
		}
		result.close();
		stmt.close();

		return l;
	}

	/**
	 * Liest die Ausschreibung aus der Datenbank aus, die der Bedingungen entspricht
	 * 
	 * @param ak
	 * @return Ausschreiber
	 * @throws SQLException
	 */
	private Ausschreibung getEinzeln(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tA, ak);

		Ausschreibung a = new Ausschreibung();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();

		// erzeugt für jeden Eintrag, den der SELECT query aus der Datenbanktabelle selektiert, ein neues Object vom Typ
		// Benutzer und fügt dieses der Liste, die spater als Rückgabewert dieser Methode zurückgegeben wird, hinzu.
		if (result.next()) {

			a.setId(result.getInt("id"));
			a.setAusschreiberId(result.getInt("ausschreiber_id"));
			a.setAusschreiberName(result.getString("ausschreiber"));
			a.setBearbeiterId(result.getInt("bearbeiter_id"));
			a.setBearbeiterName(result.getString("bearbeiter"));
			a.setName(result.getString("name"));
			a.setBeschreibung(result.getString("beschreibung"));
			a.setInstitut(result.getInt("institut"));
			a.setStundenzahl(result.getInt("stundenzahl"));
			a.setStellenzahl(result.getInt("stellenzahl"));
			a.setBelegt(result.getInt("belegt"));
			a.setVoraussetzungen(result.getString("voraussetzungen"));
			a.setStartet(result.getDate("startet"));
			a.setEndet(result.getDate("endet"));
			a.setBewerbungsfrist(result.getDate("bewerbungsfrist"));
			a.setStatus(result.getInt("status"));
			a.setHinzugefuegt(result.getDate("hinzugefuegt"));
			a.setBearbeitet(result.getDate("bearbeitet"));

		}
		result.close();
		stmt.close();

		return a;

	}

	/**
	 * Liest die Anzahl der Treffer aus, die eine SELECT Abfrage erreichen würde
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return int Anzahl der Treffer
	 * @throws SQLException
	 */
	private int getAnzahl(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tA, ak);

		int r = 0;

		// Die Select Abfrage wird erstellt
		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		// Sollte es Parameter gegeben, die in die Query eingefügt werden müssen, werden die hier eingebaut
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();

		if (result.next()) {
			r = result.getInt("anzahl");
		}

		return r;
	}

	/*
	 * Nachfolgend die Interface Methoden
	 */

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Ausschreibung> getAlleAlsListe(final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		ak.setLimit(0);
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Ausschreibung> getAlleAlsListe(final String spalte, final String wert, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		String where = this.tA + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public List<Ausschreibung> getAlleAlsListe(final String[] spalten, final String[] werte, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tA + ".`" + spalten[i] + "` = ? AND " : this.tA + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAlleAlsListe(int)
	 */
	@Override
	public List<Ausschreibung> getAlleAlsListe(final int seite, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<Ausschreibung> getAlleAlsListe(final String spalte, final String wert, final int seite, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		String where = this.tA + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], int)
	 */
	@Override
	public List<Ausschreibung> getAlleAlsListe(final String[] spalten, final String[] werte, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tA + ".`" + spalten[i] + "` = ? AND " : this.tA + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getSuchergebnisse(java.lang.String, int, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Ausschreibung> getSuchergebnisse(final String term, final int institut, final int stunden, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		HashMap<Integer, String> parameter = new HashMap<Integer, String>();

		String[] terme = term.split("[\\s\\p{Punct}]");

		ArrayList<String> list = new ArrayList<String>();
		for (String s : terme) { // terme aussortieren die leer sind
			if (!s.equals("")) {
				list.add(s);
			}
		}

		String where = "(";
		int j = 0;
		for (int i = 0; i < list.size(); i++) {
			where += "LOWER(" + this.tA + ".`name`) LIKE LOWER(?) OR LOWER(" + this.tA
					+ ".`beschreibung`) LIKE LOWER(?) OR LOWER(CONCAT(`A`.`vorname`,' ',`A`.`nachname`)) LIKE LOWER(?)";
			where += (i + 1 < list.size()) ? " OR " : "";

			parameter.put(++j, "%" + list.get(i) + "%");
			parameter.put(++j, "%" + list.get(i) + "%");
			parameter.put(++j, "%" + list.get(i) + "%");
		}
		where += ")";

		if (0 < institut) {
			where += " AND " + this.tA + ".`institut` =" + institut;
		}

		if (0 < stunden) {
			int start = 0, end = 40;
			switch (stunden) {
				case 1:
					end = 10;
					break;
				case 2:
					start = 10;
					end = 20;
					break;
				case 3:
					start = 20;
					end = 30;
					break;
				case 4:
					start = 30;
					end = 40;
					break;
				case 5:
					start = 40;
					end = 99999;
					break;
			}
			where += " AND (" + this.tA + ".`stundenzahl` BETWEEN " + start + " AND " + end + " )";
		}

		where += " AND " + this.tA + ".`status` = 0 ";

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		// ak.setOffset(seite * ak.getLimit());
		ak.setLimit(0);
		ak.setOrder(this.tA + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getEinzeln(int)
	 */
	@Override
	public Ausschreibung getEinzeln(final int id) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		ak.setLimit(1);
		ak.appendCondition(this.tA + ".`id` = " + id);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getEinzeln(java.lang.String, java.lang.String)
	 */
	@Override
	public Ausschreibung getEinzeln(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		String where = this.tA + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getEinzeln(java.lang.String[], java.lang.String[])
	 */
	@Override
	public Ausschreibung getEinzeln(final String[] spalten, final String[] werte) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tA
				+ ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `ausschreiber`, CONCAT(`B`.`vorname`,' ',`B`.`nachname`) AS `bearbeiter`, (SELECT COUNT(`id`) FROM "
				+ this.tBV + " WHERE `ausschreibung_id` = " + this.tA + ".`id` AND `status` = 3) AS `belegt`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tA + ".`" + spalten[i] + "` = ? AND " : this.tA + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAnzahl()
	 */
	@Override
	public int getAnzahl() throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAnzahl(java.lang.String, java.lang.String)
	 */
	@Override
	public int getAnzahl(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		String where = "`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAnzahl(java.lang.String[], java.lang.String[])
	 */
	@Override
	public int getAnzahl(final String[] spalten, final String[] werte) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? "`" + spalten[i] + "` = ? AND " : "`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#getAnzahlSuchergebnisse(java.lang.String, int, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public int getAnzahlSuchergebnisse(final String term, final int institut, final int stunden) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect("COUNT(" + this.tA + ".`id`) AS `anzahl`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tA + ".`ausschreiber_id`=`A`.`id` JOIN " + this.tB + " AS `B` ON " + this.tA
				+ ".`bearbeiter_id`=`B`.`id`");

		HashMap<Integer, String> parameter = new HashMap<Integer, String>();

		String[] terme = term.split("[\\s\\p{Punct}]");

		ArrayList<String> list = new ArrayList<String>();
		for (String s : terme) { // terme aussortieren die leer sind
			if (!s.equals("")) {
				list.add(s);
			}
		}

		String where = "(";
		int j = 0;
		for (int i = 0; i < list.size(); i++) {
			where += "LOWER(" + this.tA + ".`name`) LIKE LOWER(?) OR LOWER(" + this.tA
					+ ".`beschreibung`) LIKE LOWER(?) OR LOWER(CONCAT(`A`.`vorname`,' ',`A`.`nachname`)) LIKE LOWER(?)";
			where += (i + 1 < list.size()) ? " OR " : "";

			parameter.put(++j, "%" + list.get(i) + "%");
			parameter.put(++j, "%" + list.get(i) + "%");
			parameter.put(++j, "%" + list.get(i) + "%");
		}
		where += ")";

		if (0 < institut) {
			where += " AND " + this.tA + ".`institut` =" + institut;
		}

		if (0 < stunden) {
			int start = 0, end = 40;
			switch (stunden) {
				case 1:
					end = 10;
					break;
				case 2:
					start = 10;
					end = 20;
					break;
				case 3:
					start = 20;
					end = 30;
					break;
				case 4:
					start = 30;
					end = 40;
					break;
				case 5:
					start = 40;
					end = 99999;
					break;
			}
			where += " AND (" + this.tA + ".`stundenzahl` BETWEEN " + start + " AND " + end + " )";
		}

		where += " AND " + this.tA + ".`status` = 0 ";

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#erstellen(de.uulm.sopra.delos.bean.Ausschreibung)
	 */
	@Override
	public void erstellen(final Ausschreibung daten) throws SQLException {
		String query = "INSERT INTO "
				+ this.tA
				+ " (`ausschreiber_id`,`bearbeiter_id`,`name`,`beschreibung`,`institut`,`stundenzahl`,`stellenzahl`,`voraussetzungen`,`startet`,`endet`,`bewerbungsfrist`,`status`,`hinzugefuegt`,`bearbeitet`) "
				+ "VALUES (?,?,TRIM(?),TRIM(?),?,?,?,TRIM(?),?,?,?,?,NOW(),NOW());";

		try {
			PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
			stmt.setInt(1, daten.getAusschreiberId());
			stmt.setInt(2, daten.getBearbeiterId());
			stmt.setString(3, daten.getName());
			stmt.setString(4, daten.getBeschreibung());
			stmt.setInt(5, daten.getInstitut());
			stmt.setInt(6, daten.getStundenzahl());
			stmt.setInt(7, daten.getStellenzahl());
			stmt.setString(8, daten.getVoraussetzungen());
			stmt.setDate(9, daten.getStartet());
			stmt.setDate(10, daten.getEndet());
			stmt.setDate(11, daten.getBewerbungsfrist());
			stmt.setInt(12, daten.getStatus());
			stmt.execute();

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#aktualisieren(de.uulm.sopra.delos.bean.Ausschreibung)
	 */
	@Override
	public void aktualisieren(final Ausschreibung daten) throws SQLException {

		String query = "UPDATE "
				+ this.tA
				+ " SET `ausschreiber_id` = ?,`bearbeiter_id` = ?,`name` = TRIM(?),`beschreibung` = TRIM(?),`institut` = ?,`stundenzahl` = ?,`stellenzahl` = ?, "
				+ "`voraussetzungen` = TRIM(?), `startet` = ?, `endet` = ?, `bewerbungsfrist` = ?, `status` = ?, `bearbeitet` = NOW() WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getAusschreiberId());
		stmt.setInt(2, daten.getBearbeiterId());
		stmt.setString(3, daten.getName());
		stmt.setString(4, daten.getBeschreibung());
		stmt.setInt(5, daten.getInstitut());
		stmt.setInt(6, daten.getStundenzahl());
		stmt.setInt(7, daten.getStellenzahl());
		stmt.setString(8, daten.getVoraussetzungen());
		stmt.setDate(9, daten.getStartet());
		stmt.setDate(10, daten.getEndet());
		stmt.setDate(11, daten.getBewerbungsfrist());
		stmt.setInt(12, daten.getStatus());
		stmt.setInt(13, daten.getId());
		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.AusschreibungDaoInterface#loeschen(de.uulm.sopra.delos.bean.Ausschreibung)
	 */
	@Override
	public void loeschen(final Ausschreibung daten) throws SQLException {

		String query = "DELETE FROM " + this.tA + " WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getId());
		stmt.execute();

		stmt.close();
	}

}
