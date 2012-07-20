package de.uulm.sopra.delos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uulm.sopra.delos.bean.BewerbungsVorgangDokument;
import de.uulm.sopra.delos.system.Datenbank;

public class BewerbungsVorgangDokumentDao extends StandardDao implements BewerbungsVorgangDokumentDaoInterface {

	/**
	 * Liest alle zu einer Bewerbung gehörigen Dokumente aus der Datenbank aus, die den Bedingungen entsprechen und packt sie in eine LinkedList
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return
	 * @throws SQLException
	 */
	private List<BewerbungsVorgangDokument> getAlleAlsListe(final Abfragekriterium ak) throws SQLException {

		String query = this.queryGenerieren(this.tBVD, ak);

		List<BewerbungsVorgangDokument> l = new LinkedList<BewerbungsVorgangDokument>();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			BewerbungsVorgangDokument bvd = new BewerbungsVorgangDokument();

			bvd.setId(result.getInt("id"));
			bvd.setDokumentId(result.getInt("dokument_id"));
			bvd.setDokumentName(result.getString("dokument"));
			bvd.setBewerbungsVorgangId(result.getInt("bewerbungs_vorgang_id"));
			bvd.setAusschreibungName(result.getString("ausschreibung"));
			bvd.setStatus(result.getInt("status"));

			l.add(bvd);
		}
		result.close();
		stmt.close();

		return l;
	}

	/**
	 * Liest ein zu einer Bewerbung gehöriges Dokument aus der Datenbank aus, das den Bedingungen entspricht
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return
	 * @throws SQLException
	 */
	private BewerbungsVorgangDokument getEinzeln(final Abfragekriterium ak) throws SQLException {

		String query = this.queryGenerieren(this.tBVD, ak);

		BewerbungsVorgangDokument bvd = new BewerbungsVorgangDokument();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();
		while (result.next()) {
			bvd.setId(result.getInt("id"));
			bvd.setDokumentId(result.getInt("dokument_id"));
			bvd.setDokumentName(result.getString("dokument"));
			bvd.setBewerbungsVorgangId(result.getInt("bewerbungs_vorgang_id"));
			bvd.setAusschreibungName(result.getString("ausschreibung"));
			bvd.setStatus(result.getInt("status"));
		}
		result.close();
		stmt.close();

		return bvd;
	}

	/**
	 * Liest die Anzahl der Treffer aus, die eine SELECT Abfrage erreichen würde
	 * 
	 * @param ak
	 * @return int Anzahl der Treffer
	 * @throws SQLException
	 */
	private int getAnzahl(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tBVD, ak);

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
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgangDokument> getAlleAlsListe(final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		ak.setLimit(0);
		ak.setOrder(this.tBVD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgangDokument> getAlleAlsListe(final String spalte, final String wert, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		String where = this.tBVD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tBVD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BewerbungsVorgangDokument> getAlleAlsListe(final String[] spalten, final String[] werte, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tBVD + ".`" + spalten[i] + "` = ? AND " : this.tBVD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tBVD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAlleAlsListe(int)
	 */
	@Override
	public List<BewerbungsVorgangDokument> getAlleAlsListe(final int seite, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tBVD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<BewerbungsVorgangDokument> getAlleAlsListe(final String spalte, final String wert, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		String where = this.tBVD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tBVD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], int)
	 */
	@Override
	public List<BewerbungsVorgangDokument> getAlleAlsListe(final String[] spalten, final String[] werte, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tBVD + ".`" + spalten[i] + "` = ? AND " : this.tBVD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tBVD + "." + sortierSpalte + " " + sortierRichtung);
		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getEinzeln(int)
	 */
	@Override
	public BewerbungsVorgangDokument getEinzeln(final int id) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		ak.appendCondition(this.tBVD + ".`id` = " + id);
		ak.setLimit(1);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getEinzeln(java.lang.String, java.lang.String)
	 */
	@Override
	public BewerbungsVorgangDokument getEinzeln(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		String where = this.tBVD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(1);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getEinzeln(java.lang.String[], java.lang.String[])
	 */
	@Override
	public BewerbungsVorgangDokument getEinzeln(final String[] spalten, final String[] werte) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tBVD + ".*, `D`.`name` AS `dokument`, `A`.`name` AS `ausschreibung`");
		ak.setJoin("JOIN " + this.tD + " AS `D` ON " + this.tBVD + ".`dokument_id` = `D`.`id` JOIN " + this.tBV + " ON " + this.tBVD
				+ ".`bewerbungs_vorgang_id`=" + this.tBV + ".`id` JOIN " + this.tA + " AS A ON " + this.tBV + ".`ausschreibung_id`=`A`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tBVD + ".`" + spalten[i] + "` = ? AND " : this.tBVD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(1);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAnzahl()
	 */
	@Override
	public int getAnzahl() throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAnzahl(java.lang.String, java.lang.String)
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
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#getAnzahl(java.lang.String[], java.lang.String[])
	 */
	@Override
	public int getAnzahl(final String[] spalte, final String[] wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalte.length; i++) {
			where += (i + 1 < spalte.length) ? "`" + spalte[i] + "` = ? AND " : "`" + spalte[i] + "` = ?";
			parameter.put(i + 1, wert[i] + "");
		}
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#erstellen(de.uulm.sopra.delos.bean.BewerbungsVorgangDokument)
	 */
	@Override
	public void erstellen(final BewerbungsVorgangDokument daten) throws SQLException {
		String query = "INSERT INTO " + this.tBVD + " (`dokument_id`,`bewerbungs_vorgang_id`,`status`) VALUES (?,?,?);";

		PreparedStatement insert = Datenbank.getInstance().con.prepareStatement(query);

		insert.setInt(1, daten.getDokumentId());
		insert.setInt(2, daten.getBewerbungsVorgangId());
		insert.setInt(3, daten.getStatus());
		insert.execute();

		insert.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#aktualisieren(de.uulm.sopra.delos.bean.BewerbungsVorgangDokument)
	 */
	@Override
	public void aktualisieren(final BewerbungsVorgangDokument daten) throws SQLException {
		String query = "UPDATE " + this.tBVD + " SET `dokument_Id` = ?,`bewerbungs_vorgang_id` = ?,`status` = ? WHERE `id` = ?;";

		PreparedStatement update = Datenbank.getInstance().con.prepareStatement(query);

		update.setInt(1, daten.getDokumentId());
		update.setInt(2, daten.getBewerbungsVorgangId());
		update.setInt(3, daten.getStatus());
		update.setInt(4, daten.getId());
		update.execute();

		update.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#aktualisieren(int[], int)
	 */
	@Override
	public void aktualisieren(final int[] multiBewerbungsVorgangDokumentId, final int bewerbungsVorgangId) throws SQLException {
		String query = "UPDATE " + this.tBVD + " SET `status` = 0 WHERE `bewerbungs_vorgang_id` = ?;";

		PreparedStatement update = Datenbank.getInstance().con.prepareStatement(query);

		update.setInt(1, bewerbungsVorgangId);
		update.execute();

		update.close();

		query = "UPDATE " + this.tBVD + " SET `status` = 1 WHERE `id` = ?;";

		update = Datenbank.getInstance().con.prepareStatement(query);

		for (int element : multiBewerbungsVorgangDokumentId) {
			update.setInt(1, element);
			update.execute();
		}
		update.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BewerbungsVorgangDokumentDaoInterface#loeschen(de.uulm.sopra.delos.bean.BewerbungsVorgangDokument)
	 */
	@Override
	public void loeschen(final BewerbungsVorgangDokument daten) throws SQLException {
		String query = "DELETE FROM " + this.tBVD + " WHERE `id` = ?;";

		PreparedStatement delete = Datenbank.getInstance().con.prepareStatement(query);
		delete.setInt(1, daten.getId());
		delete.execute();

		delete.close();
	}

}
