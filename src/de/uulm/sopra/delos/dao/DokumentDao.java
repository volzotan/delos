package de.uulm.sopra.delos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uulm.sopra.delos.bean.Dokument;
import de.uulm.sopra.delos.system.Datenbank;

public class DokumentDao extends StandardDao implements DokumentDaoInterface {

	/**
	 * Liest alle Dokumente aus der Datenbank aus, die den Bedingungen entsprechen und packt sie in eine LinkedList
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return
	 * @throws SQLException
	 */
	private List<Dokument> getAlleAlsListe(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tD, ak);

		List<Dokument> l = new LinkedList<Dokument>();

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
			Dokument d = new Dokument();

			d.setId(result.getInt("id"));
			d.setName(result.getString("name"));
			d.setBeschreibung(result.getString("beschreibung"));
			d.setStandard(result.getInt("standard"));

			l.add(d);
		}
		result.close();
		stmt.close();

		return l;
	}

	/**
	 * Liest ein Dokument aus der Datenbank aus, das den Bedingungen entspricht
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return
	 * @throws SQLException
	 */
	private Dokument getEinzeln(final Abfragekriterium ak) throws SQLException {
		String query = this.queryGenerieren(this.tD, ak);

		Dokument d = new Dokument();

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		if (null != ak.getParameter() && 0 < ak.getParameter().size()) {
			Map<Integer, String> parameter = ak.getParameter();
			for (int i = 1; i <= ak.getParameter().size(); i++) {
				stmt.setString(i, parameter.get(i));
			}
		}
		ResultSet result = stmt.executeQuery();

		if (result.next()) {

			d.setId(result.getInt("id"));
			d.setName(result.getString("name"));
			d.setBeschreibung(result.getString("beschreibung"));
			d.setStandard(result.getInt("standard"));

		}
		result.close();
		stmt.close();

		return d;
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
		String query = this.queryGenerieren(this.tD, ak);

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
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAlleAlsListe()
	 */
	@Override
	public List<Dokument> getAlleAlsListe(final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setLimit(0);
		ak.setOrder(this.tD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Dokument> getAlleAlsListe(final String spalte, final String wert, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = this.tD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[])
	 */
	@Override
	public List<Dokument> getAlleAlsListe(final String[] spalten, final String[] werte, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tD + ".`" + spalten[i] + "` = ? AND " : this.tD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAlleAlsListe(int)
	 */
	@Override
	public List<Dokument> getAlleAlsListe(final int seite, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<Dokument> getAlleAlsListe(final String spalte, final String wert, final int seite, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = this.tD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], int)
	 */
	@Override
	public List<Dokument> getAlleAlsListe(final String[] spalten, final String[] werte, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tD + ".`" + spalten[i] + "` = ? AND " : this.tD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tD + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getNichtBenoetigteDokumenteAlsListe(int)
	 */
	@Override
	public List<Dokument> getNichtBenoetigteDokumenteAlsListe(final int bewerbungsVorgangId) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.appendCondition("`id` NOT IN (SELECT `dokument_id` FROM delos_BewerbungsVorgangDokument WHERE `bewerbungs_vorgang_id`= " + bewerbungsVorgangId + ")");
		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getEinzeln(int)
	 */
	@Override
	public Dokument getEinzeln(final int id) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.appendCondition(this.tD + ".`id` = " + id);
		ak.setLimit(1);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getEinzeln(java.lang.String, java.lang.String)
	 */
	@Override
	public Dokument getEinzeln(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = this.tD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(1);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getEinzeln(java.lang.String[], java.lang.String[])
	 */
	@Override
	public Dokument getEinzeln(final String[] spalten, final String[] werte) throws SQLException {

		Abfragekriterium ak = new Abfragekriterium();

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tD + ".`" + spalten[i] + "` = ? AND " : this.tD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(1);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAnzahl()
	 */
	@Override
	public int getAnzahl() throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect("COUNT(`id`) AS `anzahl`");

		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAnzahl(java.lang.String, java.lang.String)
	 */
	@Override
	public int getAnzahl(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = this.tD + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#getAnzahl(java.lang.String[], java.lang.String[])
	 */
	@Override
	public int getAnzahl(final String[] spalten, final String[] werte) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tD + ".`" + spalten[i] + "` = ? AND " : this.tD + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#erstellen(de.uulm.sopra.delos.bean.Dokument)
	 */
	@Override
	public void erstellen(final Dokument daten) throws SQLException {
		String query = "INSERT INTO " + this.tD + " (`name`, `beschreibung`, `standard`) VALUES(TRIM(?),TRIM(?), ?);";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setString(1, daten.getName());
		stmt.setString(2, daten.getBeschreibung());
		stmt.setInt(3, daten.getStandard());

		stmt.execute();

		stmt.close();

	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#aktualisieren(de.uulm.sopra.delos.bean.Dokument)
	 */
	@Override
	public void aktualisieren(final Dokument daten) throws SQLException {
		String query = "UPDATE " + this.tD + " SET `name` = TRIM(?), `beschreibung` = TRIM(?), `standard` = ? WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setString(1, daten.getName());
		stmt.setString(2, daten.getBeschreibung());
		stmt.setInt(3, daten.getStandard());
		stmt.setInt(4, daten.getId());

		stmt.execute();

		stmt.close();

	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.DokumentDaoInterface#loeschen(de.uulm.sopra.delos.bean.Dokument)
	 */
	@Override
	public void loeschen(final Dokument daten) throws SQLException {
		String query = "DELETE FROM " + this.tD + " WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getId());
		stmt.execute();

		stmt.close();
	}
}
