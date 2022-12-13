import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

public class Aushilfe implements IAushilfe {
    private static Aushilfe instance = null;
    
    public Aushilfe() {}
    public static Aushilfe getInstance() {
        if (instance == null) {
            instance = new Aushilfe();
        }
        return instance;
    }

    public void Gremium_Wahl() {
        Gremien_anzeigen();
        Scanner scanner = new Scanner(System.in);
        String eingabe;
        do {
            System.out.print("\nWelches Gremium soll es sein (Name): ");
            eingabe = scanner.nextLine();
        } while (!Gremien_enthaelt_Eingabe(eingabe));

        scanner.close();
    }
    private boolean Gremien_enthaelt_Eingabe(String eingabe) {
        for (AHauptklasse object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien) object;
            if (g.getName().equalsIgnoreCase(eingabe)) {
                Gremien.setAktuellesGremium(g);
                return true;
            }
        }
        return false;
    }

    public void Gremien_anzeigen() {
        System.out.println("[Gremien]");
        for (AHauptklasse object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien)object;
            System.out.printf("\nID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
        }
    }

    public void Sitzung_Wahl() {
        Sitzungen_anzeigen();
        try (Scanner scanner = new Scanner(System.in)) {
            String eingabe = null;
            do {
                System.out.print("\nWelche Sitzung soll es sein (Beginn): ");
                while (!scanner.hasNextLine()) {
                    // Warten, bis der Scanner Eingabebereit ist
                }
                System.out.println("nextLine: " + scanner.nextLine());
                eingabe = scanner.nextLine();
                if (eingabe.equals("")) {
                    continue;
                }
                // TODO: Fix this
                System.out.println("Ihre Eingabe: " + eingabe);
            } while (!Sitzungen_enthaelt_Eingabe(eingabe));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean Sitzungen_enthaelt_Eingabe(String eingabe) {
        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen) object;
            if (s.getBeginn().toString().equalsIgnoreCase(eingabe)) {
                Sitzungen.setAktiveSitzung(s);
                return true;
            }
        }
        return false;
    }
    public void Sitzungen_anzeigen() {
        System.out.println("[Sitzungen]");
        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen)object;
            System.out.printf("\nID: %d\nBeginn: %s\nEnde: %s\nEinladung_am: %s\noeffentlich: %b\nOrt: %s\nProtokoll: %s\n", s.getID(), s.getBeginn().toString(), s.getEnde().toString(), s.getEinladung_am().toString(), s.getOeffentlich(), s.getOrt(), "könnte zu lang sein");
        }
    }

    private ResultSet getRS(String sql) {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void interne_DB_testen() {
        try {
            System.out.println(ConnectionManager.getInstance().getConnection());

            // Verbindung zur Datenbank herstellen
            Connection conn = ConnectionManager.getInstance().getConnection();

            // SQL-Abfrage, um alle Tabellen anzuzeigen
            String sql = "select * from Gremien";

            // Erstelle ein Statement-Objekt, um die Abfrage auszuführen
            Statement stmt = conn.createStatement();

            // Führe die Abfrage aus und speichere das Ergebnis in einem ResultSet
            ResultSet rs = stmt.executeQuery(sql);

            // Iteriere durch das ResultSet und gebe die Namen der Tabellen auf der Konsole aus
            while (rs.next()) {
                System.out.println(rs.getString("Name"));
            }

            // Schließe die Verbindung zur Datenbank
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void interne_DB_initialisieren() {
        System.out.println("Interne DB wird initialisiert");
        try {
            init_Gremien_from_ResultSet();
            init_Antrag_from_ResultSet();
            init_Aufgabengebiete_from_ResultSet();
            init_Sitzungen_from_ResultSet();
            init_Tagesordnung_from_ResultSet();
        } catch (Exception e) {
            System.err.println("Interne DB konnte nicht initialisert werden,");
            e.printStackTrace();
        }
        System.out.println("Interne DB initialisiert.");
    }

    private void init_Gremien_from_ResultSet() throws SQLException {
        ResultSet rs_Gremien = getRS("SELECT * FROM Gremien");
        while (rs_Gremien.next()) {
            java.sql.Date beginn = rs_Gremien.getDate("Beginn");
            java.sql.Date ende = rs_Gremien.getDate("Ende");

            Gremien g = new Gremien(
                rs_Gremien.getInt("ID"),
                rs_Gremien.getString("Name"),
                rs_Gremien.getString("offiziell").matches("(?i)1|t|y"),
                rs_Gremien.getString("inoffiziell").matches("(?i)1|t|y"),
                beginn.toLocalDate(),
                ende.toLocalDate()
            );

            Factory.getInstance().addObject(Gremien.class.toString(), g);
        }
    }
    private void init_Antrag_from_ResultSet() throws SQLException {
        ResultSet rs_Antrag = getRS("SELECT * FROM Antrag");
        while (rs_Antrag.next()) {
            Antrag a = new Antrag(
                rs_Antrag.getInt("ID"),
                rs_Antrag.getString("Titel"),
                rs_Antrag.getString("Text"),
                IAntrag.Ergebnis.valueOf(rs_Antrag.getString("Ergebnis").toUpperCase()),
                Boolean.parseBoolean(rs_Antrag.getString("Angenommen"))
            );

            Factory.getInstance().addObject(Antrag.class.toString(), a);
        }
    }
    private void init_Sitzungen_from_ResultSet() throws SQLException {
        ResultSet rs_Sitzungen = getRS("SELECT * FROM Sitzungen");
        while (rs_Sitzungen.next()) {
            java.sql.Date Einladung_am = rs_Sitzungen.getDate("Einladung_am");
            Sitzungen s = new Sitzungen(
                rs_Sitzungen.getInt("ID"),
                rs_Sitzungen.getTimestamp("Beginn"),
                rs_Sitzungen.getTimestamp("Ende"),
                Einladung_am.toLocalDate(),
                Boolean.valueOf(rs_Sitzungen.getString("oeffentlich")),
                rs_Sitzungen.getString("Ort"),
                rs_Sitzungen.getString("Protokoll")
            );

            Factory.getInstance().addObject(Sitzungen.class.toString(), s);
        }
    }
    private void init_Aufgabengebiete_from_ResultSet() throws SQLException {
        ResultSet rs_Aufgabengebiete = getRS("SELECT * FROM Aufgabengebiete");
        while (rs_Aufgabengebiete.next()) {
            Aufgabengebiete au = new Aufgabengebiete(
                rs_Aufgabengebiete.getInt("ID"),
                rs_Aufgabengebiete.getInt("Ag_ID"),
                rs_Aufgabengebiete.getString("Aufgabengebiet")
            );

            Factory.getInstance().addObject(Aufgabengebiete.class.toString(), au);
        }
    }
    private void init_Tagesordnung_from_ResultSet() throws SQLException {
        ResultSet rs_Tagesordnung = getRS("SELECT * FROM Tagesordnung");
        while (rs_Tagesordnung.next()) {
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
