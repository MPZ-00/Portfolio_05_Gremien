import java.sql.ResultSet;
import java.sql.SQLException;

public class DBInitialisierung {
    private static DBInitialisierung instance;

    public DBInitialisierung() {}

    public static DBInitialisierung getInstance() {
        if (instance == null) {
            instance = new DBInitialisierung();
        }
        return instance;
    }

    public boolean interne_DB_initialisieren() {
        Drucken.getInstance().print_Warnung("Interne DB wird initialisiert");
        try {
            init_Gremien_from_ResultSet();
            init_Antrag_from_ResultSet();
            init_Aufgabengebiete_from_ResultSet();
            init_Sitzungen_from_ResultSet();
            init_Tagesordnung_from_ResultSet();
        } catch (Exception e) {
            System.err.println("Interne DB konnte nicht initialisert werden,");
            e.printStackTrace();
            return false;
        }
        Drucken.getInstance().print_Warnung("Interne DB initialisiert");
        return true;
    }

    private void init_Gremien_from_ResultSet() throws Exception {
        ResultSet rs_Gremien = ConnectionManager.getInstance().executeStatement("SELECT * FROM Gremien");
        while (rs_Gremien != null && rs_Gremien.next()) {
            java.sql.Date beginn = rs_Gremien.getDate("Beginn");
            java.sql.Date ende = rs_Gremien.getDate("Ende");

            Gremium g = new Gremium(
                rs_Gremien.getInt("ID"),
                rs_Gremien.getString("Name"),
                rs_Gremien.getString("offiziell").matches("(?i)1|t|y"),
                rs_Gremien.getString("inoffiziell").matches("(?i)1|t|y"),
                beginn.toLocalDate(),
                ende.toLocalDate()
            );

            Factory.getInstance().addObject(Gremium.class.toString(), g);
        }
    }
    private void init_Antrag_from_ResultSet() throws Exception {
        ResultSet rs_Antrag = ConnectionManager.getInstance().executeStatement("SELECT * FROM Antrag");
        while (rs_Antrag != null && rs_Antrag.next()) {
            Antrag a = new Antrag(
                rs_Antrag.getInt("ID"),
                rs_Antrag.getString("Titel"),
                rs_Antrag.getString("Text"),
                Antrag.Ergebnis.valueOf(rs_Antrag.getString("Ergebnis").toUpperCase()),
                Boolean.parseBoolean(rs_Antrag.getString("Angenommen"))
            );

            Factory.getInstance().addObject(Antrag.class.toString(), a);
        }
    }
    private void init_Sitzungen_from_ResultSet() throws SQLException {
        ResultSet rs_Sitzungen = ConnectionManager.getInstance().executeStatement("SELECT * FROM Sitzungen");
        while (rs_Sitzungen != null && rs_Sitzungen.next()) {
            java.sql.Date Einladung_am = rs_Sitzungen.getDate("Einladung_am");
            Sitzung s = new Sitzung(
                rs_Sitzungen.getInt("ID"),
                rs_Sitzungen.getTimestamp("Beginn"),
                rs_Sitzungen.getTimestamp("Ende"),
                Einladung_am.toLocalDate(),
                Boolean.valueOf(rs_Sitzungen.getString("oeffentlich")),
                rs_Sitzungen.getString("Ort"),
                rs_Sitzungen.getString("Protokoll")
            );

            Factory.getInstance().addObject(Sitzung.class.toString(), s);
        }
    }
    private void init_Aufgabengebiete_from_ResultSet() throws SQLException {
        ResultSet rs_Aufgabengebiete = ConnectionManager.getInstance().executeStatement("SELECT * FROM Aufgabengebiete");
        while (rs_Aufgabengebiete != null && rs_Aufgabengebiete.next()) {
            Aufgabengebiet au = new Aufgabengebiet(
                rs_Aufgabengebiete.getInt("ID"),
                rs_Aufgabengebiete.getString("Aufgabengebiet")
            );

            Factory.getInstance().addObject(Aufgabengebiet.class.toString(), au);
        }
    }
    private void init_Tagesordnung_from_ResultSet() throws SQLException {
        ResultSet rs_Tagesordnung = ConnectionManager.getInstance().executeStatement("SELECT * FROM Tagesordnung");
        while (rs_Tagesordnung != null && rs_Tagesordnung.next()) {
            Tagesordnung t = new Tagesordnung(
                rs_Tagesordnung.getInt("ID"),
                rs_Tagesordnung.getString("Titel"),
                rs_Tagesordnung.getString("Kurzbeschreibung"),
                rs_Tagesordnung.getString("Protokolltext")
            );

            Factory.getInstance().addObject(Tagesordnung.class.toString(), t);
        }
    }
}
