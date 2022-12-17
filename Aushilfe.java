import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Aushilfe implements IAushilfe {
    private static Aushilfe instance = null;
    
    public Aushilfe() {}
    public static Aushilfe getInstance() {
        if (instance == null) {
            instance = new Aushilfe();
        }
        return instance;
    }

    public boolean interne_DB_initialisieren() {
        Aushilfe.getInstance().print_Warnung("Interne DB wird initialisiert");
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
        Aushilfe.getInstance().print_Warnung("Interne DB initialisiert");
        return true;
    }

    private void init_Gremien_from_ResultSet() throws Exception {
        ResultSet rs_Gremien = ConnectionManager.getInstance().executeStatement("SELECT * FROM Gremien");
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
        ResultSet rs_Aufgabengebiete = ConnectionManager.getInstance().executeStatement("SELECT * FROM Aufgabengebiete");
        while (rs_Aufgabengebiete != null && rs_Aufgabengebiete.next()) {
            Aufgabengebiete au = new Aufgabengebiete(
                rs_Aufgabengebiete.getInt("ID"),
                rs_Aufgabengebiete.getString("Aufgabengebiet")
            );

            Factory.getInstance().addObject(Aufgabengebiete.class.toString(), au);
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

    public boolean frage_Ja_Nein(String frage) {
        String input;
        do {
            System.out.println(frage + " (ja/nein): ");
            input = Main.scanner.nextLine();
        } while (!input.equalsIgnoreCase("ja") && !input.equalsIgnoreCase("nein"));

        return (input.equalsIgnoreCase("ja"));
    }

    public Timestamp getTimestamp(String text) {
        String template = "yyyy-MM-dd HH:mm:ss";
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        // Eingabeaufforderung ausgeben
        System.out.printf("%s (%s) ein: ", text, template);
        
        try {
            String input = Main.scanner.nextLine();
            if (!input.matches(regex)) {
                throw new IllegalArgumentException();
            }

            // Konvertiere den Eingabe-String in ein LocalDateTime-Objekt
            LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern(template));

            // Konvertiere das LocalDateTime-Objekt in ein Timestamp-Objekt
            return Timestamp.from(dateTime.toInstant(ZoneOffset.of("+2"))); // offset to utc
        } catch (IllegalArgumentException e) {
            // Fehlermeldung ausgeben und erneut nach Timestamp fragen
            System.err.println("Ungültiges Datumsformat. Bitte versuchen Sie es erneut.");
            return getTimestamp(text);
        }
    }

    public LocalDate getLocalDate(String text) {
        int yy = 1964, mm = 5, dd = 14;
        try {
            System.out.printf("%s (%s) ein: ", text, "Jahr");
            while (!Main.scanner.hasNextInt()) {
                yy = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();
            
            System.out.printf("%s (%s) ein: ", text, "Monat");
            while (!Main.scanner.hasNextInt()) {
                mm = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();

            System.out.printf("%s (%s) ein: ", text, "Tag");
            while (!Main.scanner.hasNextInt()) {
                dd = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();
            
            return LocalDate.of(yy, mm, dd);
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Eingabe. Bitte nur Zahlen eingeben.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Ungültiges Datumsformat");
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValidDateFormat(String input, String regex) {
        return input.matches(regex);
    }

    public void print_Titel(String text) {
        System.out.println("[\033[35m" + text + "\033[0m]");
    }
    public void print_Warnung(String text) {
        System.out.println("[\033[33m" + text + "\033[0m]");
    }
}