package de.uulm.sopra.delos.dao;

import java.io.File;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.system.Datenbank;

public class BewerbungsVorgangDao extends StandardDao implements BewerbungsVorgangDaoInterface {

	/**
	 * Liest alle Bewerbungen aus der Datenbank aus, die den Bedingungen entsprechen und packt sie in eine LinkedList
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return List<BewerbungsVorgang> der Bewerbungen
	 * @throws SQLException
	 */
	private List<BewerbungsVorgang> getAlleAlsListe(final Abfragekriterium ak) throws SQLException {

		String query = queryGenerieren(tBV, ak);

		List<BewerbungsVorgang> l = new LinkedList<BewerbungsVorgang>();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			BewerbungsVorgang bv = new BewerbungsVorgang();

			bv.setId(result.getInt("id"));
			bv.setBewerberId(result.getInt("bewerber_id"));
			bv.setBewerberName(result.getString("bewerber"));
			bv.setBearbeiterId(result.getInt("bearbeiter_id"));
			bv.setBearbeiterName(result.getString("bearbeiter"));
			bv.setAusschreiberId(result.getInt("ausschreiber_id"));
			bv.setAusschreiberName(result.getString("ausschreiber"));
			bv.setAusschreibungId(result.getInt("ausschreibung_id"));
			bv.setAusschreibungName(result.getString("ausschreibung"));
			bv.setInstitut(result.getInt("institut"));
			bv.setStundenzahl(result.getInt("stundenzahl"));
			bv.setStartet(result.getDate("startet"));
			bv.setEndet(result.getDate("endet"));
			bv.setStatus(result.getInt("status"));
			bv.setKommentar(result.getString("kommentar"));
			bv.setHinzugefuegt(result.getDate("hinzugefuegt"));
			bv.setBearbeitet(result.getDate("bearbeitet"));

			l.add(bv);
		}
		result.close();
		stmt.close();

		return l;
	}

	/**
	 * Liest einen Bewerbungsvorgang aus der Datenbank aus, der den Angaben im Abfragekriterium entspricht
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return Bewerbungsvorgang
	 * @throws SQLException
	 */
	private BewerbungsVorgang getEinzeln(final Abfragekriterium ak) throws SQLException {

		String query = queryGenerieren(tBV, ak);

		BewerbungsVorgang bv = new BewerbungsVorgang();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();
		if (result.next()) {

			bv.setId(result.getInt("id"));
			bv.setBewerberId(result.getInt("bewerber_id"));
			bv.setBewerberName(result.getString("bewerber"));
			bv.setBearbeiterId(result.getInt("bearbeiter_id"));
			bv.setBearbeiterName(result.getString("bearbeiter"));
			bv.setAusschreiberId(result.getInt("ausschreiber_id"));
			bv.setAusschreiberName(result.getString("ausschreiber"));
			bv.setAusschreibungId(result.getInt("ausschreibung_id"));
			bv.setAusschreibungName(result.getString("ausschreibung"));
			bv.setInstitut(result.getInt("institut"));
			bv.setStundenzahl(result.getInt("stundenzahl"));
			bv.setStartet(result.getDate("startet"));
			bv.setEndet(result.getDate("endet"));
			bv.setStatus(result.getInt("status"));
			bv.setKommentar(result.getString("kommentar"));
			bv.setHinzugefuegt(result.getDate("hinzugefuegt"));
			bv.setBearbeitet(result.getDate("bearbeitet"));

		}
		result.close();
		stmt.close();

		return bv;
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
		String query = queryGenerieren(tBV, ak);

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
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListe(final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		ak.setLimit(0);
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListe(final String spalte, final String wert, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = tBV + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListe(final String[] spalte, final String[] wert, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalte.length; i++) {
			where += (i + 1 < spalte.length) ? tBV + ".`" + spalte[i] + "` = ? AND " : tBV + ".`" + spalte[i] + "` = ?";
			parameter.put(i + 1, wert[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListe(int)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListe(final int seite, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListe(final String spalte, final String wert, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = tBV + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], int)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListe(final String[] spalte, final String[] wert, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalte.length; i++) {
			where += (i + 1 < spalte.length) ? tBV + ".`" + spalte[i] + "` = ? AND " : tBV + ".`" + spalte[i] + "` = ?";
			parameter.put(i + 1, wert[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListeNichtNeu(java.lang.String[], java.lang.String[], java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListeNichtNeu(final String[] spalte, final String[] wert, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalte.length; i++) {
			where += tBV + ".`" + spalte[i] + "` = ? AND ";
			parameter.put(i + 1, wert[i] + "");
		}

		where += tBV + ".`status` <> 1";

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAlleAlsListeNichtNeu(java.lang.String[], java.lang.String[], int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgang> getAlleAlsListeNichtNeu(final String[] spalte, final String[] wert, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalte.length; i++) {
			where += tBV + ".`" + spalte[i] + "` = ? AND ";
			parameter.put(i + 1, wert[i] + "");
		}

		where += tBV + ".`status` <> 1";

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder("`AG`." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getEinzeln(int)
	 */
	@Override
	public BewerbungsVorgang getEinzeln(final int id) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		ak.setLimit(1);
		ak.appendCondition(tBV + ".`id` = " + id);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getEinzeln(java.lang.String, java.lang.String)
	 */
	@Override
	public BewerbungsVorgang getEinzeln(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = tBV + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getEinzeln(java.lang.String[], java.lang.String[])
	 */
	@Override
	public BewerbungsVorgang getEinzeln(final String[] spalten, final String[] werte) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(tBV + ".*, CONCAT(`BW`.`vorname`,' ',`BW`.`nachname`) AS `bewerber`, CONCAT(`BA`.`vorname`,' ',`BA`.`nachname`) AS `bearbeiter`, "
				+ "CONCAT(`AR`.`vorname`,' ',`AR`.`nachname`) AS `ausschreiber`, " + "`AG`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + tB + " AS `BW` ON " + tBV + ".`bewerber_id`=`BW`.`id` JOIN " + tB + " AS `BA` ON " + tBV + ".`bearbeiter_id`=`BA`.`id` JOIN " + tB
				+ " AS `AR` ON " + tBV + ".`ausschreiber_id`=`AR`.`id` JOIN " + tA + " AS `AG` ON " + tBV + ".`ausschreibung_id`=`AG`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? tBV + ".`" + spalten[i] + "` = ? AND " : tBV + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAnzahl()
	 */
	@Override
	public int getAnzahl() throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAnzahl(java.lang.String, java.lang.String)
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
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAnzahl(java.lang.String[], java.lang.String[])
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
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAnzahlNichtNeu(java.lang.String, java.lang.String)
	 */
	@Override
	public int getAnzahlNichtNeu(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		String where = "`" + spalte + "` = ? AND `status` <> 1";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#getAnzahlNichtNeu(java.lang.String[], java.lang.String[])
	 */
	@Override
	public int getAnzahlNichtNeu(final String[] spalten, final String[] werte) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += "`" + spalten[i] + "` = ? AND ";
			parameter.put(i + 1, werte[i] + "");
		}
		where += "`status` <> 1";
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#erstellen(de.uulm.sopra.delos.bean.BewerbungsVorgang)
	 */
	@Override
	public void erstellen(final BewerbungsVorgang daten) throws SQLException {
		String query = "INSERT INTO "
				+ tBV
				+ " (`bewerber_id`,`bearbeiter_id`,`ausschreiber_id`,`ausschreibung_id`,`institut`,`stundenzahl`,`startet`,`endet`, `status`, `kommentar`, `hinzugefuegt`,`bearbeitet`) VALUES (?,?,?,?,?,?,?,?,?,TRIM(?),NOW(),NOW());";

		PreparedStatement insert = Datenbank.getInstance().con.prepareStatement(query);

		insert.setInt(1, daten.getBewerberId());
		insert.setInt(2, daten.getBearbeiterId());
		insert.setInt(3, daten.getAusschreiberId());
		insert.setInt(4, daten.getAusschreibungId());
		insert.setInt(5, daten.getInstitut());
		insert.setInt(6, daten.getStundenzahl());
		insert.setDate(7, daten.getStartet());
		insert.setDate(8, daten.getEndet());
		insert.setInt(9, daten.getStatus());
		insert.setString(10, daten.getKommentar());

		insert.execute();

		insert.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#aktualisieren(de.uulm.sopra.delos.bean.BewerbungsVorgang)
	 */
	@Override
	public void aktualisieren(final BewerbungsVorgang daten) throws SQLException {
		String query = "UPDATE "
				+ tBV
				+ " SET `ausschreiber_id` = ?, `bearbeiter_id` = ?, `institut` = ?, `stundenzahl` = ?, `startet` = ?, `endet` = ?, `status` = ?, `kommentar` = TRIM(?), `bearbeitet` = NOW() WHERE `id` = ?;";

		PreparedStatement update = Datenbank.getInstance().con.prepareStatement(query);

		update.setInt(1, daten.getAusschreiberId());
		update.setInt(2, daten.getBearbeiterId());
		update.setInt(3, daten.getInstitut());
		update.setInt(4, daten.getStundenzahl());
		update.setDate(5, daten.getStartet());
		update.setDate(6, daten.getEndet());
		update.setInt(7, daten.getStatus());
		update.setString(8, daten.getKommentar());
		update.setInt(9, daten.getId());
		update.execute();

		update.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#loeschen(de.uulm.sopra.delos.bean.BewerbungsVorgang)
	 */
	@Override
	public void loeschen(final BewerbungsVorgang daten) throws SQLException {
		String query = "DELETE FROM " + tBV + " WHERE `id` = ?;";

		PreparedStatement delete = Datenbank.getInstance().con.prepareStatement(query);
		delete.setInt(1, daten.getId());
		delete.execute();

		delete.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDaoInterface#export(int)
	 */
	@Override
	public File export(final int bearbeiterId) throws Exception {
		// DAO kann konfigurationsProperties aus der BasisAction nicht nutzen, muss sie sich selbst holen
		Properties konfiguration = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("delosconfig.properties");
		konfiguration.load(is);

		String outfilePfad = konfiguration.getProperty("outfile.pfad");
		String dateiname = "export_" + Long.toString(System.currentTimeMillis() / 1000) + ".csv";

		String query = "SELECT `AR`.`nachname` AS `AusschreiberNachname`, `AR`.`vorname` AS `AusschreiberVorname`,"
				+ "`BW`.`nachname` AS `BewerberNachname`, `BW`.`vorname` AS `BewerberVorname`, `BW`.`matrikelnummer` AS `Matrikelnummer`,"
				+ "`VO`.`startet` AS `Beginn`, `VO`.`endet` AS `Ende`," + "`VO`.`stundenzahl` AS `Std/Monat`, `VO`.`bearbeitet` AS `abgeschlossen` " + "FROM "
				+ tBV + " AS `VO` JOIN " + tB + " AS `BW` ON `VO`.`bewerber_id`=`BW`.`id` " + "JOIN " + tB + " AS `BA` ON `VO`.`bearbeiter_id`=`BA`.`id` "
				+ "JOIN " + tB + " AS `AR` ON `VO`.`ausschreiber_id`=`AR`.`id` " + "JOIN " + tA + " AS `AG` ON `VO`.`ausschreibung_id`=`AG`.`id` "
				+ "WHERE `VO`.`bearbeiter_id` = ? AND `VO`.`status` = 3 ORDER BY `AG`.`name` ASC " + "INTO OUTFILE ? " + "FIELDS TERMINATED BY ',' "
				+ "ENCLOSED BY '' " + "LINES TERMINATED BY '\n' ";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);

		stmt.setInt(1, bearbeiterId);
		stmt.setString(2, outfilePfad + dateiname);

		stmt.executeQuery();

		File csvDatei = new File(outfilePfad + dateiname);

		return csvDatei;
	}
}
