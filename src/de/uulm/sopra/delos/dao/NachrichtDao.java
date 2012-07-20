package de.uulm.sopra.delos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uulm.sopra.delos.bean.Nachricht;
import de.uulm.sopra.delos.system.Datenbank;

public class NachrichtDao extends StandardDao implements NachrichtDaoInterface {

	/**
	 * Liest alle Nachrichten aus der Datenbank aus, die den Bedingungen entsprechen und packt sie in eine LinkedList
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return int Anzahl der Treffer
	 * @throws SQLException
	 */
	private List<Nachricht> getAlleAlsListe(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tN, ak);

		List<Nachricht> l = new LinkedList<Nachricht>();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();

		// erzeugt für jeden Eintrag, den der SELECT query aus der Datenbanktabelle selektiert, ein neues Object vom Typ
		// Dokument und fügt dieses der Liste, die spater als Rückgabewert dieser Methode zurückgegeben wird, hinzu.
		while (result.next()) {
			Nachricht n = new Nachricht();

			n.setId(result.getInt("id"));
			n.setAbsenderId(result.getInt("absender_id"));
			n.setAbsenderName(result.getString("absender"));
			n.setEmpfaengerId(result.getInt("empfaenger_id"));
			n.setEmpfaengerName(result.getString("empfaenger"));
			n.setBetreff(result.getString("titel"));
			n.setText(result.getString("beschreibung"));
			n.setStatus(result.getInt("status"));
			n.setAbsenderLoeschStatus(result.getInt("absender_loeschStatus"));
			n.setEmpfaengerLoeschStatus(result.getInt("empfaenger_loeschStatus"));
			n.setVersanddatum(result.getTimestamp("versanddatum"));

			l.add(n);
		}
		result.close();
		stmt.close();

		return l;
	}

	/**
	 * Liest eine Nachricht aus der Datenbank aus, die den Bedingungen entspricht
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return int Anzahl der Treffer
	 * @throws SQLException
	 */
	private Nachricht getEinzeln(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tN, ak);

		Nachricht n = new Nachricht();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();

		if (result.next()) {

			n.setId(result.getInt("id"));
			n.setAbsenderId(result.getInt("absender_id"));
			n.setAbsenderName(result.getString("absender"));
			n.setEmpfaengerId(result.getInt("empfaenger_id"));
			n.setEmpfaengerName(result.getString("empfaenger"));
			n.setBetreff(result.getString("titel"));
			n.setText(result.getString("beschreibung"));
			n.setStatus(result.getInt("status"));
			n.setAbsenderLoeschStatus(result.getInt("absender_loeschStatus"));
			n.setEmpfaengerLoeschStatus(result.getInt("empfaenger_loeschStatus"));
			n.setVersanddatum(result.getTimestamp("versanddatum"));
		}
		result.close();
		stmt.close();

		return n;
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
		String query = this.queryGenerieren(this.tN, ak);

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
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Nachricht> getAlleAlsListe(final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		ak.setLimit(0);
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Nachricht> getAlleAlsListe(final String spalte, final String wert, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		String where = this.tN + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public List<Nachricht> getAlleAlsListe(final String[] spalten, final String[] werte, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tN + ".`" + spalten[i] + "` = ? AND " : this.tN + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getPapierkorbAlsListe(int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Nachricht> getPapierkorbAlsListe(final int benutzerId, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");
		ak.setCondition("(" + this.tN + ".`absender_id` = " + benutzerId + " AND " + this.tN + ".`absender_loeschStatus` = 1) OR (" + this.tN
				+ ".`empfaenger_id` = " + benutzerId + " AND " + this.tN + ".`empfaenger_loeschStatus` = 1)");

		ak.setLimit(0);
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAlleAlsListe(int)
	 */
	@Override
	public List<Nachricht> getAlleAlsListe(final int seite, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<Nachricht> getAlleAlsListe(final String spalte, final String wert, final int seite, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		String where = this.tN + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], int)
	 */
	@Override
	public List<Nachricht> getAlleAlsListe(final String[] spalten, final String[] werte, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tN + ".`" + spalten[i] + "` = ? AND " : this.tN + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getPapierkorbAlsListe(int, int)
	 */
	@Override
	public List<Nachricht> getPapierkorbAlsListe(final int benutzerId, final int seite, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");
		ak.setCondition("(" + this.tN + ".`absender_id` = " + benutzerId + " AND " + this.tN + ".`absender_loeschStatus` = 1) OR (" + this.tN
				+ ".`empfaenger_id` = " + benutzerId + " AND " + this.tN + ".`empfaenger_loeschStatus` = 1)");

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tN + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getEinzeln(int)
	 */
	@Override
	public Nachricht getEinzeln(final int id) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		ak.setLimit(1);
		ak.appendCondition(this.tN + ".`id` = " + id);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getEinzeln(java.lang.String, java.lang.String)
	 */
	@Override
	public Nachricht getEinzeln(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		String where = this.tN + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getEinzeln(java.lang.String[], java.lang.String[])
	 */
	@Override
	public Nachricht getEinzeln(final String[] spalte, final String[] wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tN + ".*, CONCAT(`A`.`vorname`,' ',`A`.`nachname`) AS `absender`, CONCAT(`E`.`vorname`,' ',`E`.`nachname`) AS `empfaenger`");
		ak.setJoin("JOIN " + this.tB + " AS `A` ON " + this.tN + ".`absender_id` = `A`.`id` JOIN " + this.tB + " AS `E` ON " + this.tN
				+ ".`empfaenger_id` = `E`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalte.length; i++) {
			where += (i + 1 < spalte.length) ? this.tN + ".`" + spalte[i] + "` = ? AND " : this.tN + ".`" + spalte[i] + "` = ?";
			parameter.put(i + 1, wert[i] + "");
		}

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAnzahl()
	 */
	@Override
	public int getAnzahl() throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAnzahl(java.lang.String, java.lang.String)
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
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getAnzahl(java.lang.String[], java.lang.String[])
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
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#getPapierkorbAnzahl(int)
	 */
	@Override
	public int getPapierkorbAnzahl(final int benutzerId) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.setCondition("(" + this.tN + ".`absender_id` = " + benutzerId + " AND " + this.tN + ".`absender_loeschStatus` = 1) OR (" + this.tN
				+ ".`empfaenger_id` = " + benutzerId + " AND " + this.tN + ".`empfaenger_loeschStatus` = 1)");
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#erstellen(de.uulm.sopra.delos.bean.Nachricht)
	 */
	@Override
	public void erstellen(final Nachricht nachricht) throws SQLException {
		String query = "INSERT INTO " + this.tN
				+ " (`absender_id`, `empfaenger_id`, `titel`, `beschreibung`, `versanddatum`) VALUES (?, ?, TRIM(?), TRIM(?), NOW());";

		try {
			PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
			stmt.setInt(1, nachricht.getAbsenderId());
			stmt.setInt(2, nachricht.getEmpfaengerId());
			stmt.setString(3, nachricht.getBetreff());
			stmt.setString(4, nachricht.getText());
			stmt.execute();

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#aktualisiereStatus(int, int)
	 */
	@Override
	public void aktualisiereStatus(final int id, final int status) throws SQLException {
		String query = "UPDATE " + this.tN + " SET `status` = ? WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, status);
		stmt.setInt(2, id);
		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#aktualisiereLoeschStatus(de.uulm.sopra.delos.bean.Nachricht, boolean)
	 */
	@Override
	public void aktualisiereLoeschStatus(final Nachricht nachricht, final boolean absender) throws SQLException {
		String tS = absender ? "absender" : "empfaenger";
		String query = "UPDATE " + this.tN + " SET `" + tS + "_loeschStatus` = ? WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, (absender ? nachricht.getAbsenderLoeschStatus() : nachricht.getEmpfaengerLoeschStatus()) - 1);
		stmt.setInt(2, nachricht.getId());
		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#wiederherstellen(de.uulm.sopra.delos.bean.Nachricht, boolean)
	 */
	@Override
	public void wiederherstellen(final Nachricht nachricht, final boolean absender) throws SQLException {
		String tS = absender ? "absender" : "empfaenger";
		String query = "UPDATE " + this.tN + " SET `" + tS + "_loeschStatus` = ? WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, (absender ? nachricht.getAbsenderLoeschStatus() : nachricht.getEmpfaengerLoeschStatus()) + 1);
		stmt.setInt(2, nachricht.getId());
		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.NachrichtDaoInterface#loeschen(de.uulm.sopra.delos.bean.Nachricht)
	 */
	@Override
	public void loeschen(final Nachricht nachricht) throws SQLException {
		String query = "DELETE FROM " + this.tN + " WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, nachricht.getId());
		stmt.execute();

		stmt.close();

	}
}