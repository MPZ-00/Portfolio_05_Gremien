import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Main extends Aushilfe {
    private static final String PATTERN = "dd-MM-YYYY HH:mm:ss";
    private static final List<String> options = Arrays.asList(
        "Verbindung selber einrichten",
        "Gremium und Beginn der Sitzung auswählen",
        "Tagesordnung anzeigen",
        "Tagesordnungspunkt oder Antrag auswählen",
        "Protokoll eintragen",
        "Ende der Sitzung eintragen",
        "Verbindung mit Localhost",
        "Programm beenden"
    );
    
    public static void main(String[] args) {
        // Erstelle einen Scanner, um die Eingabe vom Benutzer zu lesen
        Scanner scanner = new Scanner(System.in);
        int auswahl;
        boolean beenden = false;

        interne_DB_initialisieren();

        try {
            while (!beenden) {
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i));
                }
                System.out.println("Auswahl: ");

                while (!scanner.hasNextInt() || (auswahl = scanner.nextInt()) < 1 || auswahl > options.size()) {
                    System.out.println("Bitte gültige Auswahl eingeben (1-" + options.size() + "): ");
                    scanner.nextLine(); // Leere den Eingabepuffer
                }

                // Verwende options.get() anstatt feste Werte im switch-Statement
                switch (options.get(auswahl - 1)) {
                    case "Verbindung selber einrichten": Verbindung_selber_einrichten(); break;
                    case "Gremium und Beginn der Sitzung auswählen": Gremium_und_Beginn_der_Sitzung(); break;
                    case "Tagesordnung anzeigen": Tagesordnung_anzeigen(); break; // Alle Tagesordnungspunkte einer Sitzung werden in der richtigen Reihenfolge angezeigt. Zu jedem Tagesordnungspunkt werden die zugehörigen Anträge angezeigt.
                    case "Tagesordnungspunkt oder Antrag auswählen": Tagesordnungspunkt_oder_Antrag(); break;
                    case "Protokoll eintragen": Protokoll_eintragen(); break;
                    case "Ende der Sitzung eintragen": Ende_Sitzung_eintragen(); break;
                    case "Programm beenden": beenden = true; break;
                    case "Verbindung mit Localhost": Verbindung_mit_Localhost(); break;
                }
            }
        } finally {
            scanner.close();
            ConnectionManager.getInstance().disconnect();
        }
    }

    static void Gremium_und_Beginn_der_Sitzung() {
        Scanner scanner = new Scanner(System.in);

        // TODO: verfügbare Gremien anzeigen, dann entscheiden lassen, ob vorhandenes oder neues verwendet werden soll
        Aushilfe.getInstance().Gremium_Wahl();
        // 2. nach Name absteigend sortieren
        // 3. Auswahl erfolgt durch Eingabe des Namens
        // 4. aktuelles Gremium setzen

        // TODO: vorhandene oder neue Sitzung auswählen
        
        // Erstelle ein neues Gremium
        System.out.println("Geben Sie die Bezeichnung des Gremiums ein: ");
        String gremiumName = scanner.nextLine();

        Boolean gremiumOffiziell = ist_Offiziell(scanner);

        System.out.println("Geben Sie den Beginn der Sitzung ein (dd-MM-YYYY HH:mm:ss): ");
        Timestamp sitzungBeginn = getTimestamp(scanner);
        // Date sitzungBeginn = Date.valueOf(scanner.nextLine());
        // Sitzungen(Timestamp Beginn, Timestamp Ende, Date Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll)

        Gremien gremium = Factory.getInstance().createGremien(gremiumName, gremiumOffiziell, !gremiumOffiziell, LocalDate.now(), LocalDate.now().plusYears(50));
        Gremien.setAktuellesGremium(gremium);

        // Timestamp sitzungEnde = Timestamp.valueOf((LocalDateTime)sitzungBeginn.toLocalDateTime().plus(Duration.ofHours(2)));
        Timestamp sitzungEnde = new Timestamp(sitzungBeginn.getTime() + TimeUnit.HOURS.toMillis(2));
        Sitzungen sitzung = Factory.getInstance().createSitzungen(sitzungBeginn, sitzungEnde, LocalDate.now(), true, "", "");
        Sitzungen.setAktiveSitzung(sitzung);

        // Füge Tagesordnung hinzu
        while (true) {
            System.out.println("Geben Sie den Namen des Tagesordnungspunks ein (oder 'ende', um die Eingabe zu beenden): ");
            String top = scanner.nextLine();
            
            if (top.equals("ende")) {break;}
            
        }
        
        scanner.close();
    }

    private static Timestamp getTimestamp(Scanner scanner) {
        // Eingabeaufforderung ausgeben
        System.out.println("Bitte geben Sie das Timestamp im Format dd-MM-YYYY HH:mm:ss ein: ");

        // Nutzereingabe empfangen
        String input = scanner.nextLine();

        try {
            // Konvertiere den Eingabe-String in ein LocalDateTime-Objekt
            LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern(PATTERN));

            // Konvertiere das LocalDateTime-Objekt in ein Timestamp-Objekt
            return Timestamp.from(dateTime.toInstant(ZoneOffset.UTC));
        } catch (IllegalArgumentException e) {
            // Fehlermeldung ausgeben und erneut nach Timestamp fragen
            System.out.println("Ungültiges Datumsformat. Bitte versuchen Sie es erneut.");
            return getTimestamp(scanner);
        }
    }

    private static boolean ist_Offiziell(Scanner scanner) {
        String input;
        do {
            System.out.println("Ist das Gremium offiziell (ja/nein): ");
            input = scanner.nextLine();
        } while (!input.equalsIgnoreCase("ja") && !input.equalsIgnoreCase("nein"));
        
        return (input.equalsIgnoreCase("ja"));
    }

    private static void Tagesordnung_anzeigen() {
        try {
            // Alle Tagesordnung einer Sitzung sortiert nach Titel ausgeben
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Tagesordnung.Titel FROM top INNER JOIN Tagesordnung ON top.ID_Tagesordnung = Tagesordnung.ID WHERE top.ID_Sitzung = " + Sitzungen.getAktiveSitzung().getID() + " ORDER BY Tagesordnung.Titel ASC");
    
            List<String> titel = new ArrayList<>();
            while (rs.next()) {
                titel.add(rs.getString("Titel"));
            }
    
            // Gib die Liste mit den Titeln der Tagesordnung aus
            System.out.println(titel);
    
            ResultSet rs2 = statement.executeQuery("SELECT Antrag.* FROM Antrag JOIN gehoert_zu ON Antrag.ID = gehoert_zu.ID_Antrag WHERE gehoert_zu.ID_TOP = " + Tagesordnung.getAktuellenTOP().getID());
            List<Antrag> antraege = new ArrayList<Antrag>();
            while (rs2.next()) {
                antraege.add(new Antrag(rs2.getInt("ID"), rs2.getString("Titel"), rs2.getString("Text"), IAntrag.Ergebnis.valueOf(rs2.getString("Ergebnis")), Boolean.parseBoolean(rs2.getString("Angenommen"))));
            }

            Antraege_fuer_TOP_anzeigen();
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private static void Antraege_fuer_TOP_anzeigen() {
        try {
            ResultSet rs = getRS("SELECT Antrag.* FROM Antrag JOIN gehoert_zu ON Antrag.ID = gehoert_zu.ID_Antrag WHERE gehoert_zu.ID_TOP = " + Tagesordnung.getAktuellenTOP().getID());
            while (rs.next()) {
                System.out.println("Antrag ID: " + rs.getInt("Antrag.ID"));
                System.out.println("Antrag Titel: " + rs.getString("Antrag.Titel"));
                System.out.println("Antrag Text: " + rs.getString("Antrag.Text"));
                System.out.println("Antrag Ergebnis: " + rs.getString("Antrag.Ergebnis"));
                System.out.println("Antrag Angenommen: " + Boolean.valueOf(rs.getString("Antrag.Angenommen")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static ResultSet getRS(String sql) {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void Tagesordnungspunkt_oder_Antrag() {

    }

    private static void Protokoll_eintragen() {}
    
    private static void Ende_Sitzung_eintragen() {}

    private static void Verbindung_selber_einrichten() {
        Scanner input = new Scanner(System.in);

        System.out.println("Gib 'null' ein, wenn ein Standardwert verwendet werden soll.");

        // Eingabeaufforderungen für die Verbindungsparameter
        System.out.print("URL der Datenbank: ");
        String url = input.nextLine();
        System.out.print("Name der Datenbank: ");
        String db_name = input.nextLine();
        System.out.print("Benutzername: ");
        String user = input.nextLine();
        System.out.print("Passwort: ");
        String pass = input.nextLine();
        System.out.print("Port: ");
        String port = input.nextLine();
        System.out.print("SID: ");
        String sid = input.nextLine();

        ConnectionManager.getInstance().connect(
            get_Value_Or_Null(url),
            get_Value_Or_Null(db_name),
            get_Value_Or_Null(user),
            get_Value_Or_Null(pass),
            get_Value_Or_Null(port),
            get_Value_Or_Null(sid)
        );
        input.close();
    }

    private static void Verbindung_mit_Localhost() {
        try {
            ConnectionManager.getInstance().directConnnect("jdbc:oracle:thin:@localhost:10111:namib", "DABS_42", "DABS_42");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static String get_Value_Or_Null(String value) {
        if (value.equalsIgnoreCase("null")) {
            return null;
        }
        return value;
    }

    private static void interne_DB_initialisieren() {
        try {
            ResultSet rs_Gremien = getRS("SELECT * FROM Gremien");
            while (rs_Gremien.next()) {
                Date beginn = rs_Gremien.getDate("Beginn");
                Date ende = rs_Gremien.getDate("Ende");

                Gremien g = new Gremien(
                    rs_Gremien.getInt("ID"),
                    rs_Gremien.getString("Name"),
                    Boolean.valueOf(rs_Gremien.getString("offiziell")),
                    Boolean.valueOf(rs_Gremien.getString("inoffiziell")),
                    LocalDate.ofInstant(beginn.toInstant(), ZoneId.systemDefault()),
                    LocalDate.ofInstant(ende.toInstant(), ZoneId.systemDefault())
                );

                Factory.getInstance().addID(Gremien.class.toString(), g.getID());
            }

            ResultSet rs_Antrag = getRS("SELECT * FROM Antrag");
            while (rs_Antrag.next()) {
                Antrag a = new Antrag(
                    rs_Antrag.getInt("ID"),
                    rs_Antrag.getString("Titel"),
                    rs_Antrag.getString("Text"),
                    IAntrag.Ergebnis.valueOf(rs_Antrag.getString("Ergebnis").toUpperCase()),
                    Boolean.valueOf(rs_Antrag.getString("Angenommen"))
                );

                Factory.getInstance().addID(Antrag.class.toString(), a.getID());
            }

            ResultSet rs_Sitzungen = getRS("SELECT * FROM Sitzungen");
            while (rs_Sitzungen.next()) {
                Date Einladung_am = rs_Sitzungen.getDate("Einladung_am");
                Sitzungen s = new Sitzungen(
                    rs_Sitzungen.getInt("ID"),
                    rs_Sitzungen.getTimestamp("Beginn"),
                    rs_Sitzungen.getTimestamp("Ende"),
                    LocalDate.ofInstant(Einladung_am.toInstant(), ZoneId.systemDefault()),
                    Boolean.valueOf(rs_Sitzungen.getString("oeffentlich")),
                    rs_Sitzungen.getString("Ort"),
                    rs_Sitzungen.getString("Protokoll")
                );

                Factory.getInstance().addID(Sitzungen.class.toString(), s.getID());
            }

            ResultSet rs_Tagesordnung = getRS("SELECT * FROM Tagesordnung");
            while (rs_Tagesordnung.next()) {
                Tagesordnung t = new Tagesordnung(
                    rs_Tagesordnung.getInt("ID"),
                    rs_Tagesordnung.getString("Titel"),
                    rs_Tagesordnung.getString("Kurzbeschreibung"),
                    rs_Tagesordnung.getString("Protokolltext")
                );

                Factory.getInstance().addID(Tagesordnung.class.toString(), t.getID());
            }

            ResultSet rs_Aufgabengebiete = getRS("SELECT * FROM Aufgabengebiete");
            while (rs_Aufgabengebiete.next()) {
                Aufgabengebiete au = new Aufgabengebiete(
                    rs_Aufgabengebiete.getInt("ID"),
                    rs_Aufgabengebiete.getInt("Ag_ID"),
                    rs_Aufgabengebiete.getString("Aufgabengebiet")
                );

                Factory.getInstance().addID(Aufgabengebiete.class.toString(), au.getID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}