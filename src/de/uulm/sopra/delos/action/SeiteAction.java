package de.uulm.sopra.delos.action;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Locale;

import com.opensymphony.xwork2.ActionContext;

import de.uulm.sopra.delos.bean.Ausschreibung;
import de.uulm.sopra.delos.bean.BewerbungsVorgang;
import de.uulm.sopra.delos.dao.AusschreibungDao;
import de.uulm.sopra.delos.dao.BewerbungsVorgangDao;

public class SeiteAction extends BasisAction {

	private static final long				serialVersionUID		= 4415445274771854763L;

	// Abfragevariable für die Anzahl von diversen Objekte
	private int								anzahl					= 0;

	// benötigte Daos
	private final BewerbungsVorgangDao		bewerbungsVorgangDao	= new BewerbungsVorgangDao();
	private final AusschreibungDao			ausschreibungDao		= new AusschreibungDao();

	// Listen für abgefragte Objekte um diese auf der Startseite anzuzeigen
	private LinkedList<BewerbungsVorgang>	bewerbungsListe			= new LinkedList<BewerbungsVorgang>();
	private LinkedList<Ausschreibung>		ausschreibungsListe		= new LinkedList<Ausschreibung>();

	// Aufruf für die Startseite um diese mit benutzerspezifischem Inhalt zu füllen
	public String index() {
		// Inhalte für Startseite werden, abhängig von der Benutzergruppe, aus der Datebank abgefragt und in einer Liste abgelegt
		switch (sessionBenutzer.getGruppeId()) {
			case 1:
				return "indexAdmin";
			case 2:
				try {
					// Bewerbungen des eingeloggten Benutzers werden aus der Datenbank ausgelesen

					bewerbungsListe = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(new String[] { "bewerber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "1" }, 0, "bearbeitet", "desc");
					bewerbungsListe.addAll(bewerbungsVorgangDao.getAlleAlsListe(new String[] { "bewerber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "-1" }, 0, "bearbeitet", "desc"));
					bewerbungsListe.addAll(bewerbungsVorgangDao.getAlleAlsListe(new String[] { "bewerber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "2" }, 0, "bearbeitet", "desc"));
					bewerbungsListe.addAll(bewerbungsVorgangDao.getAlleAlsListe(new String[] { "bewerber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "-2" }, 0, "bearbeitet", "desc"));

					for (BewerbungsVorgang vorgang : bewerbungsListe) {
						if (vorgang.getAusschreibungName().length() > 50) {
							String kurzname = vorgang.getAusschreibungName().substring(0, 50);
							vorgang.setAusschreibungName(vorgang.getAusschreibungName().substring(0, kurzname.lastIndexOf(" ")) + " ...");
						}
					}

					ausschreibungsListe = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe(new String[] { "status" }, new String[] { "0" }, 0,
							"hinzugefuegt", "desc");

					while (ausschreibungsListe.size() > 6) {
						ausschreibungsListe.removeLast();
					}

					for (Ausschreibung ausschreibung : ausschreibungsListe) {
						if (ausschreibung.getName().length() > 30) {
							String kurzname = ausschreibung.getName().substring(0, 30);
							ausschreibung.setName(ausschreibung.getName().substring(0, kurzname.lastIndexOf(" ")) + " ...");
						}
					}

				} catch (SQLException e) {
					log.fatal("Abfragen der Bewerbungsvorgänge fehlgeschlagen" + e);
					return ERROR;
				}
				return "indexBewerber";
			case 3:
				// Anzahl der Ausschreibungen des eingeleogten Benutzers werden aus der Datenbank ausgelesen
				try {
					bewerbungsListe = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreiber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "1" }, 0, "bearbeitet", "desc");
					bewerbungsListe.addAll(bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreiber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "-1" }, 0, "bearbeitet", "desc"));
					bewerbungsListe.addAll(bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreiber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "2" }, 0, "bearbeitet", "desc"));
					bewerbungsListe.addAll(bewerbungsVorgangDao.getAlleAlsListe(new String[] { "ausschreiber_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "-2" }, 0, "bearbeitet", "desc"));

					for (BewerbungsVorgang vorgang : bewerbungsListe) {
						if (vorgang.getAusschreibungName().length() > 50) {
							String kurzname = vorgang.getAusschreibungName().substring(0, 50);
							vorgang.setAusschreibungName(vorgang.getAusschreibungName().substring(0, kurzname.lastIndexOf(" ")) + " ...");
						}
					}

					ausschreibungsListe = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe(new String[] { "status" }, new String[] { "0" }, 0,
							"hinzugefuegt", "desc");

					while (ausschreibungsListe.size() > 6) {
						ausschreibungsListe.removeLast();
					}

					for (Ausschreibung ausschreibung : ausschreibungsListe) {
						if (ausschreibung.getName().length() > 30) {
							String kurzname = ausschreibung.getName().substring(0, 30);
							ausschreibung.setName(ausschreibung.getName().substring(0, kurzname.lastIndexOf(" ")) + " ...");
						}
					}

				} catch (SQLException e) {
					log.fatal("Abfragen der Ausschreibungen fehlgeschlagen" + e);
					return ERROR;
				}
				return "indexAusschreiber";
			case 4:
				try {
					bewerbungsListe = (LinkedList<BewerbungsVorgang>) bewerbungsVorgangDao.getAlleAlsListe(new String[] { "bearbeiter_id", "status" },
							new String[] { Integer.toString(sessionBenutzer.getId()), "2" }, 0, "bearbeitet", "desc");

				} catch (SQLException e) {
					log.fatal("Abfragen der Ausschreibungen fehlgeschlagen" + e);
					return ERROR;
				}

				return "indexBearbeiter";

			default:
				try {
					ausschreibungsListe = (LinkedList<Ausschreibung>) ausschreibungDao.getAlleAlsListe(new String[] { "status" }, new String[] { "0" }, 0,
							"hinzugefuegt", "desc");

					while (ausschreibungsListe.size() > 6) {
						ausschreibungsListe.removeLast();
					}

					for (Ausschreibung ausschreibung : ausschreibungsListe) {
						if (ausschreibung.getName().length() > 45) {
							String kurzname = ausschreibung.getName().substring(0, 45);
							ausschreibung.setName(ausschreibung.getName().substring(0, kurzname.lastIndexOf(" ")) + "...");
						}
					}
				} catch (SQLException e) {
					log.fatal("Abfragen der Ausschreibungen fehlgeschlagen" + e);
					return ERROR;
				}
				return "index";
		}
	}

	// Aufruf für die Hilfeseite
	public String hilfe() {

		Locale locale = ActionContext.getContext().getLocale();
		// sorgt dafür, dass Hilfe in der ausgewählten Sprache angezeigt wird
		if (locale.getDisplayLanguage().equals("Englisch")) {
			return "hilfeEN";
		} else {
			return "hilfeDE";
		}
	}

	// Aufruf für die Administrationsseite
	public String administration() {
		return SUCCESS;
	}

	// Aufruf für die Kontaktseite
	public String kontakt() {
		return SUCCESS;
	}

	// Aufruf für die Über-Seite
	public String ueber() {
		return SUCCESS;
	}

	// Aufruf für die 404-Seite
	public String viernullvier() {
		return SUCCESS;
	}

	/**
	 * 
	 * @return anzahl
	 */
	public int getAnzahl() {
		return anzahl;
	}

	/**
	 * @param anzahl
	 *            the anzahl to set
	 */
	public void setAnzahl(final int anzahl) {
		this.anzahl = anzahl;
	}

	/**
	 * 
	 * @return bewerbungsListe
	 */
	public LinkedList<BewerbungsVorgang> getBewerbungsListe() {
		return bewerbungsListe;
	}

	/**
	 * @param bewerbungsListe
	 *            the bewerbungsListe to set
	 */
	public void setBewerbungsListe(final LinkedList<BewerbungsVorgang> bewerbungsListe) {
		this.bewerbungsListe = bewerbungsListe;
	}

	/**
	 * 
	 * @return ausschreibungsListe
	 */
	public LinkedList<Ausschreibung> getAusschreibungsListe() {
		return ausschreibungsListe;
	}

	/**
	 * @param ausschreibungsListe
	 *            the ausschreibungsListe to set
	 */
	public void setAusschreibungsListe(final LinkedList<Ausschreibung> ausschreibungsListe) {
		this.ausschreibungsListe = ausschreibungsListe;
	}

}