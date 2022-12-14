import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
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

    private void Gremium_Wahl() {
        Gremien_anzeigen();
        String eingabe;
        do {
            System.out.print("\nWelches Gremium soll es sein (Name): ");
            eingabe = Main.scanner.nextLine();
        } while (!Gremien_enthaelt_Eingabe(eingabe));
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

    private void Sitzung_Wahl() {
        Sitzungen_anzeigen(Gremien.getAktuellesGremium().getID());
        try {
            Timestamp sitzungBeginn = getTimestamp("Geben Sie den Beginn der Sitzung ein", "yyyy-MM-dd HH:mm:ss");
            
            if (Sitzungen_enthaelt_Eingabe(sitzungBeginn)) {
                System.out.println("OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean Sitzungen_enthaelt_Eingabe(Timestamp beginn) {
        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen) object;
            if (s.getBeginn().equals(beginn)) {
                Sitzungen.setAktiveSitzung(s);
                return true;
            }
        }
        return false;
    }
    public void Sitzungen_anzeigen(Integer id) {
        System.out.println("[Sitzungen]");
        HashSet<Integer> s_ids = new HashSet<>();

        ResultSet rs = getRS(
            "select s.id " +
            "from sitzungen s " +
            "inner join hat on hat.id_sitzungen = s.id " +
            "inner join gremien g on g.id = hat.id_gremien where g.id = " +
            Gremien.getAktuellesGremium().getID()
        );
        try {
            while (rs.next()) {
                s_ids.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (s_ids.size() == 0) {
            System.out.println("Für dieses Gremium gibt es keine Sitzungen");
            return;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen)object;
            if (s_ids.contains(s.getID())) {
                System.out.printf("\nID: %d\nBeginn: %s\nEnde: %s\nEinladung_am: %s\noeffentlich: %b\nOrt: %s\nProtokoll: %s\n", s.getID(), s.getBeginn().toString(), s.getEnde().toString(), s.getEinladung_am().toString(), s.getOeffentlich(), s.getOrt(), s.getProtokoll());
            }
        }
    }

    public ResultSet getRS(String sql) {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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

    private void init_Gremien_from_ResultSet() throws Exception {
        ResultSet rs_Gremien = getRS("SELECT * FROM Gremien");
        while (rs_Gremien != null && rs_Gremien.next()) {
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
    private void init_Antrag_from_ResultSet() throws Exception {
        ResultSet rs_Antrag = getRS("SELECT * FROM Antrag");
        while (rs_Antrag != null && rs_Antrag.next()) {
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
        while (rs_Sitzungen != null && rs_Sitzungen.next()) {
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
        while (rs_Aufgabengebiete != null && rs_Aufgabengebiete.next()) {
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

    public boolean frage_Ja_Nein(Scanner scanner, String frage) {
        String input;
        do {
            System.out.println(frage + " (ja/nein): ");
            input = scanner.nextLine();
        } while (!input.equalsIgnoreCase("ja") && !input.equalsIgnoreCase("nein"));

        return (input.equalsIgnoreCase("ja"));
    }

    public Timestamp getTimestamp(String text, String pattern) {
        // Eingabeaufforderung ausgeben
        System.out.printf("%s (%s) ein: ", text, pattern);
        
        try {
            // Auf Bereitschaft vom Scanner warten
            if (!Main.scanner.hasNextLine()) {
                Main.scanner.reset();
            }

            // Nutzereingabe empfangen
            String input = Main.scanner.nextLine();

            // Konvertiere den Eingabe-String in ein LocalDateTime-Objekt
            LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern(pattern));

            // Konvertiere das LocalDateTime-Objekt in ein Timestamp-Objekt
            return Timestamp.from(dateTime.toInstant(ZoneOffset.UTC));
        } catch (IllegalArgumentException e) {
            // Fehlermeldung ausgeben und erneut nach Timestamp fragen
            System.err.println("Ungültiges Datumsformat. Bitte versuchen Sie es erneut.");
            return getTimestamp(text, pattern);
        }
    }

    public boolean Aufgabe1() {
        Aushilfe.getInstance().Gremium_Wahl();
        System.out.println("Ausgewähltes Gremium (ID/Name): " + Gremien.getAktuellesGremium().getID() + "/" + Gremien.getAktuellesGremium().getName());
        
        try {
            Aushilfe.getInstance().Sitzung_Wahl();
            System.out.println("Ausgewählte Sitzung (ID/Beginn): " + Sitzungen.getAktiveSitzung().getID() + "/" + Sitzungen.getAktiveSitzung().getBeginn());
        } catch (NullPointerException e) {
            if (Aushilfe.getInstance().frage_Ja_Nein(Main.scanner, "Jetzt neue Sitzung für dieses Gremium anlegen")) {
                // TODO: Code für neue Sitzung anlegen
            } else {
                System.err.println("Keine Sitzungen für dieses Gremium verfügbar, wähle ein anderes Gremium aus");
                return Aufgabe1();
            }
        }
        return true;
    }
    public void Aufgabe2() {
        System.out.println("[Sitzungen]");
        HashSet<Integer> TOP_ids = new HashSet<>();

        ResultSet rs = getRS(
            "select t.id " +
            "from sitzungen s " +
            "inner join top on top.id_sitzungen = s.id " +
            "inner join tagesordnung t on t.id = top.id_tagesordnung " +
            "where s.id = " +
            Sitzungen.getAktiveSitzung().getID()
        );
        try {
            while (rs.next()) {
                TOP_ids.add(rs.getInt("s.id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (TOP_ids.size() == 0) {
            System.out.println("Für dieses Gremium gibt es keine Sitzungen");
            return;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (TOP_ids.contains(t.getID())) {
                System.out.printf("\nID: %d\nTitel: %s\nKurzbeschreibung: %s\nProtokolltext: %s", t.getID(), t.getTitel(), t.getKurzbeschreibung(), t.getProtokolltext());
            }
        }
    }
}
