package de.uulm.sopra.delos.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uulm.sopra.delos.bean.Benutzer;
import de.uulm.sopra.delos.system.Datenbank;

public class BenutzerDao extends StandardDao implements BenutzerDaoInterface {

	/*
	 * Standard Methoden zum Abfragen der Datensätze
	 */

	/**
	 * Liest alle Benutzer aus der Datenbank aus, die den Bedingungen entsprechen und packt sie in eine LinkedList
	 * 
	 * @param ak
	 *            Abfragekriterium aus erstellt in einer der Interfacemethoden
	 * @return LinkedList<Benutzer> der Benutzer
	 * @throws SQLException
	 */
	private List<Benutzer> getAlleAlsListe(final Abfragekriterium ak) throws SQLException {
		// ignoriert gelöschte Benutzer und Accounts die dem System gehören und in öffentlichen Listen etc. nichts zu suchen haben
		ak.appendCondition(this.tB + ".`gruppe_id` <> 5 AND " + this.tB + ".`email` <> ''");
		String query = this.queryGenerieren(this.tB, ak);

		List<Benutzer> l = new LinkedList<Benutzer>();

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

		// erzeugt für jeden Eintrag, den der SELECT query aus der Datenbanktabelle selektiert, ein neues Object vom Typ
		// Benutzer und fügt dieses der Liste, die spater als Rückgabewert dieser Methode zurückgegeben wird, hinzu.
		while (result.next()) {
			Benutzer b = new Benutzer();

			b.setId(result.getInt("id"));
			b.setGruppeId(result.getInt("gruppe_id"));
			b.setVertreterId(result.getInt("vertreter_id"));
			b.setVertreterName(result.getString("vertreter"));
			b.setInstitut(result.getInt("institut"));
			b.setEmail(result.getString("email"));
			b.setNachname(result.getString("nachname"));
			b.setVorname(result.getString("vorname"));
			b.setPasswort(result.getString("passwort"));
			b.setTelefon(result.getString("telefon"));
			b.setRaum(result.getString("raum"));
			b.setGeburtstag(result.getDate("geburtstag"));
			b.setGeschlecht(result.getInt("geschlecht"));
			b.setMatrikelnummer(result.getInt("matrikelnummer"));
			b.setStudiengang(result.getInt("studiengang"));
			b.setNationalitaet(result.getInt("nationalitaet"));
			b.setStrasse(result.getString("strasse"));
			b.setHausnummer(result.getString("hausnummer"));
			b.setPostleitzahl(result.getString("postleitzahl"));
			b.setStadt(result.getString("stadt"));
			b.setLand(result.getInt("land"));
			b.setStrasseHeimat(result.getString("strasseHeimat"));
			b.setHausnummerHeimat(result.getString("hausnummerHeimat"));
			b.setPostleitzahlHeimat(result.getString("postleitzahlHeimat"));
			b.setStadtHeimat(result.getString("stadtHeimat"));
			b.setLandHeimat(result.getInt("landHeimat"));
			b.setHinzugefuegt(result.getDate("hinzugefuegt"));
			b.setBearbeitet(result.getDate("bearbeitet"));

			l.add(b);
		}
		result.close();
		stmt.close();

		return l;
	}

	/**
	 * Liest den Benutzer aus der Datenbank aus, der den Bedingungen entspricht
	 * 
	 * @param ak
	 * @return Benutzer
	 * @throws SQLException
	 */
	private Benutzer getEinzeln(final Abfragekriterium ak) throws SQLException {
		// ignoriert gelöschte Benutzer und Accounts die dem System gehören und in öffentlichen Listen etc. nichts zu suchen haben
		ak.appendCondition(this.tB + ".`gruppe_id` <> 5 AND " + this.tB + ".`email` <> ''");
		String query = this.queryGenerieren(this.tB, ak);

		Benutzer b = null;

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

		// erzeugt für den ersten Eintrag, den der SELECT query aus der Datenbanktabelle selektiert, ein neues Object vom Typ Benutzer
		if (result.next()) {
			b = new Benutzer();

			b.setId(result.getInt("id"));
			b.setGruppeId(result.getInt("gruppe_id"));
			b.setVertreterId(result.getInt("vertreter_id"));
			b.setVertreterName(result.getString("vertreter"));
			b.setInstitut(result.getInt("institut"));
			b.setEmail(result.getString("email"));
			b.setNachname(result.getString("nachname"));
			b.setVorname(result.getString("vorname"));
			b.setPasswort(result.getString("passwort"));
			b.setTelefon(result.getString("telefon"));
			b.setRaum(result.getString("raum"));
			b.setGeburtstag(result.getDate("geburtstag"));
			b.setGeschlecht(result.getInt("geschlecht"));
			b.setMatrikelnummer(result.getInt("matrikelnummer"));
			b.setStudiengang(result.getInt("studiengang"));
			b.setNationalitaet(result.getInt("nationalitaet"));
			b.setStrasse(result.getString("strasse"));
			b.setHausnummer(result.getString("hausnummer"));
			b.setPostleitzahl(result.getString("postleitzahl"));
			b.setStadt(result.getString("stadt"));
			b.setLand(result.getInt("land"));
			b.setStrasseHeimat(result.getString("strasseHeimat"));
			b.setHausnummerHeimat(result.getString("hausnummerHeimat"));
			b.setPostleitzahlHeimat(result.getString("postleitzahlHeimat"));
			b.setStadtHeimat(result.getString("stadtHeimat"));
			b.setLandHeimat(result.getInt("landHeimat"));
			b.setHinzugefuegt(result.getDate("hinzugefuegt"));
			b.setBearbeitet(result.getDate("bearbeitet"));
		}
		result.close();
		stmt.close();

		return b;
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
		// ignoriert gelöschte Benutzer und Accounts die dem System gehören und in öffentlichen Listen etc. nichts zu suchen haben
		ak.appendCondition(this.tB + ".`gruppe_id` <> 5 AND " + this.tB + ".`email` <> ''");
		String query = this.queryGenerieren(this.tB, ak);

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
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Benutzer> getAlleAlsListe(final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		ak.setLimit(0);
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Benutzer> getAlleAlsListe(final String spalte, final String wert, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		// Hier werden die Spalten in das WHERE des Abfragekriterium eingebaut und die entsprechenden Parameter als HashMap weitergereicht
		String where = this.tB + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public List<Benutzer> getAlleAlsListe(final String[] spalten, final String[] werte, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		// Hier werden die Spalten in das WHERE des Abfragekriterium eingebaut und die entsprechenden Parameter als HashMap weitergereicht
		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tB + ".`" + spalten[i] + "` = ? AND " : this.tB + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setLimit(0);
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAlleAlsListe(int)
	 */
	@Override
	public List<Benutzer> getAlleAlsListe(final int seite, final String sortierSpalte, final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAlleAlsListe(java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<Benutzer> getAlleAlsListe(final String spalte, final String wert, final int seite, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		// Hier werden die Spalten in das WHERE des Abfragekriterium eingebaut und die entsprechenden Parameter als HashMap weitergereicht
		String where = this.tB + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAlleAlsListe(java.lang.String[], java.lang.String[], int)
	 */
	@Override
	public List<Benutzer> getAlleAlsListe(final String[] spalten, final String[] werte, final int seite, final String sortierSpalte,
			final String sortierRichtung) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		// Hier werden die Spalten in das WHERE des Abfragekriterium eingebaut und die entsprechenden Parameter als HashMap weitergereicht
		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tB + ".`" + spalten[i] + "` = ? AND " : this.tB + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, werte[i] + "");
		}

		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getSuchergebnisseAlsListe(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Benutzer> getSuchergebnisseAlsListe(final String term, final int seite, final String sortierSpalte, final String sortierRichtung)
			throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, "%" + term + "%");
		parameter.put(2, "%" + term + "%");
		parameter.put(3, "%" + term + "%");

		ak.appendCondition("(LOWER(" + this.tB + ".`vorname`) LIKE LOWER(?) OR LOWER(" + this.tB + ".`nachname`) LIKE LOWER(?) OR LOWER(" + this.tB
				+ ".`email`) LIKE LOWER(?))");
		ak.setParameter(parameter);
		ak.setOffset(seite * ak.getLimit());
		ak.setOrder(this.tB + "." + sortierSpalte + " " + sortierRichtung);

		return this.getAlleAlsListe(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getEinzeln(int)
	 */
	@Override
	public Benutzer getEinzeln(final int id) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		ak.setLimit(1);
		ak.appendCondition(this.tB + ".`id` = " + id);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getEinzeln(java.lang.String, java.lang.String)
	 */
	@Override
	public Benutzer getEinzeln(final String spalte, final String wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		String where = this.tB + ".`" + spalte + "` = ?";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, wert + "");

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getEinzeln(java.lang.String[], java.lang.String[])
	 */
	@Override
	public Benutzer getEinzeln(final String[] spalten, final String[] wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();

		ak.setSelect(this.tB + ".*, CONCAT(`V`.`vorname`,' ',`V`.`nachname`) AS `vertreter`");
		ak.setJoin("LEFT JOIN " + this.tB + " AS `V` ON " + this.tB + ".`vertreter_id` = `V`.`id`");

		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? this.tB + ".`" + spalten[i] + "` = ? AND " : this.tB + ".`" + spalten[i] + "` = ?";
			parameter.put(i + 1, wert[i] + "");
		}

		ak.setLimit(1);
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);

		return this.getEinzeln(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAnzahl()
	 */
	@Override
	public int getAnzahl() throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAnzahl(java.lang.String, java.lang.String)
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
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAnzahl(java.lang.String[], java.lang.String[])
	 */
	@Override
	public int getAnzahl(final String[] spalten, final String[] wert) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		String where = "";
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		for (int i = 0; i < spalten.length; i++) {
			where += (i + 1 < spalten.length) ? "`" + spalten[i] + "` = ? AND " : "`" + spalten[i] + "` = ?";
			parameter.put(i + 1, wert[i] + "");
		}
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		ak.appendCondition(where.trim());
		ak.setParameter(parameter);
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#getAnzahlSuchergebnisse(java.lang.String)
	 */
	@Override
	public int getAnzahlSuchergebnisse(final String term) throws SQLException {
		Abfragekriterium ak = new Abfragekriterium();
		ak.setSelect("COUNT(`id`) AS `anzahl`");
		HashMap<Integer, String> parameter = new HashMap<Integer, String>();
		parameter.put(1, "%" + term + "%");
		parameter.put(2, "%" + term + "%");
		parameter.put(3, "%" + term + "%");

		ak.appendCondition("(LOWER(" + this.tB + ".`vorname`) LIKE LOWER(?) OR LOWER(" + this.tB + ".`nachname`) LIKE LOWER(?) OR LOWER(" + this.tB
				+ ".`email`) LIKE LOWER(?))");
		ak.setParameter(parameter);
		return this.getAnzahl(ak);
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#erstellen(de.uulm.sopra.delos.bean.Benutzer)
	 */
	@Override
	public void erstellen(final Benutzer daten) throws SQLException {
		String query = "INSERT INTO "
				+ this.tB
				+ " (`gruppe_id`, `vertreter_id`, `institut`, `email`, `nachname`, `vorname`, `passwort`, `telefon`, `raum`, `geburtstag`, `geschlecht`, `matrikelnummer`, `studiengang`, `nationalitaet`, "
				+ "`strasse`, `hausnummer`, `postleitzahl`, `stadt`, `land`, `strasseHeimat`, `hausnummerHeimat`, `postleitzahlHeimat`, `stadtHeimat`, `landHeimat`, `hinzugefuegt`, `bearbeitet`) "
				+ "VALUES(?,?,?,TRIM(?),TRIM(?),TRIM(?),?,TRIM(?),TRIM(?),?,?,?,?,?,TRIM(?),TRIM(?),TRIM(?),TRIM(?),?,TRIM(?),TRIM(?),TRIM(?),TRIM(?),?,NOW(),NOW());";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getGruppeId());
		stmt.setInt(2, daten.getVertreterId());
		stmt.setInt(3, daten.getInstitut());
		stmt.setString(4, daten.getEmail());
		stmt.setString(5, daten.getNachname());
		stmt.setString(6, daten.getVorname());
		stmt.setString(7, daten.getPasswort());
		stmt.setString(8, daten.getTelefon());
		stmt.setString(9, daten.getRaum());
		stmt.setDate(10, daten.getGeburtstag());
		stmt.setInt(11, daten.getGeschlecht());
		stmt.setInt(12, daten.getMatrikelnummer());
		stmt.setInt(13, daten.getStudiengang());
		stmt.setInt(14, daten.getNationalitaet());
		stmt.setString(15, daten.getStrasse());
		stmt.setString(16, daten.getHausnummer());
		stmt.setString(17, daten.getPostleitzahl());
		stmt.setString(18, daten.getStadt());
		stmt.setInt(19, daten.getLand());
		stmt.setString(20, daten.getStrasseHeimat());
		stmt.setString(21, daten.getHausnummerHeimat());
		stmt.setString(22, daten.getPostleitzahlHeimat());
		stmt.setString(23, daten.getStadtHeimat());
		stmt.setInt(24, daten.getLandHeimat());

		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#aktualisieren(de.uulm.sopra.delos.bean.Benutzer)
	 */
	@Override
	public void aktualisieren(final Benutzer daten) throws SQLException {
		String query = "UPDATE " + this.tB
				+ " SET `gruppe_id` = ?, `vertreter_id` = ?, `institut` = ?, `email` = TRIM(?), `nachname` = TRIM(?), `vorname` = TRIM(?), `passwort` = ?, "
				+ "`telefon` = TRIM(?), `raum` = TRIM(?), `geburtstag` = ?, `geschlecht` = ?, `matrikelnummer` = ?, `studiengang` = ?, "
				+ "`nationalitaet` = ?, `strasse` = TRIM(?), `hausnummer` = TRIM(?), "
				+ "`postleitzahl` = TRIM(?), `stadt` = TRIM(?), `land` = ?, `strasseHeimat` = TRIM(?), `hausnummerHeimat` = TRIM(?), "
				+ "`postleitzahlHeimat` = TRIM(?), `stadtHeimat` = TRIM(?), `landHeimat` = ?, `bearbeitet` = NOW() WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getGruppeId());
		stmt.setInt(2, daten.getVertreterId());
		stmt.setInt(3, daten.getInstitut());
		stmt.setString(4, daten.getEmail());
		stmt.setString(5, daten.getNachname());
		stmt.setString(6, daten.getVorname());
		stmt.setString(7, daten.getPasswort());
		stmt.setString(8, daten.getTelefon());
		stmt.setString(9, daten.getRaum());
		stmt.setDate(10, daten.getGeburtstag());
		stmt.setInt(11, daten.getGeschlecht());
		stmt.setInt(12, daten.getMatrikelnummer());
		stmt.setInt(13, daten.getStudiengang());
		stmt.setInt(14, daten.getNationalitaet());
		stmt.setString(15, daten.getStrasse());
		stmt.setString(16, daten.getHausnummer());
		stmt.setString(17, daten.getPostleitzahl());
		stmt.setString(18, daten.getStadt());
		stmt.setInt(19, daten.getLand());
		stmt.setString(20, daten.getStrasseHeimat());
		stmt.setString(21, daten.getHausnummerHeimat());
		stmt.setString(22, daten.getPostleitzahlHeimat());
		stmt.setString(23, daten.getStadtHeimat());
		stmt.setInt(24, daten.getLandHeimat());
		stmt.setInt(25, daten.getId());

		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#loeschen(de.uulm.sopra.delos.bean.Benutzer)
	 */
	@Override
	public void loeschen(final Benutzer daten) throws SQLException {
		String query = "DELETE FROM " + this.tB + " WHERE `id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getId());
		stmt.execute();

		stmt.close();
	}

	/*
	 * (non-Javadoc)
	 * @see de.uulm.sopra.delos.dao.BenutzerDaoInterface#alsVertreterAustragen(de.uulm.sopra.delos.bean.Benutzer)
	 */
	@Override
	public void alsVertreterAustragen(final Benutzer daten) throws SQLException {
		String query = "UPDATE " + this.tB + " SET `vertreter_id` = 0  WHERE `vertreter_id` = ?;";

		PreparedStatement stmt = Datenbank.getInstance().con.prepareStatement(query);
		stmt.setInt(1, daten.getId());
		stmt.execute();

		stmt.close();
	}
}
