import java.util.*;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Main extends Aushilfe {
    public static final Scanner scanner = new Scanner(System.in);
    private static final List<String> options = Arrays.asList(
        "Prozess starten",
        "Gremium wählen",
        "Sitzung wählen",
        "Tagesordnung wählen",
        "Antrag wählen",
        "Verbindung selber einrichten",
        "Verbindung mit Localhost",
        "Programm beenden"
    );
    
    public static void main(String[] args) {
        // Erstelle einen Scanner, um die Eingabe vom Benutzer zu lesen

        try {
            int auswahl;
            boolean beenden = false;
            
            init(scanner);

            while (!beenden) {
                System.out.println("\n[Menü]");
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i));
                }
                System.out.print("Auswahl: ");

                while (!scanner.hasNextInt() || (auswahl = scanner.nextInt()) < 1 || auswahl > options.size()) {
                    System.out.println("Bitte gültige Auswahl eingeben (1-" + options.size() + "): ");
                    scanner.nextLine(); // Leere den Eingabepuffer bei falscher Eingabe
                }

                // Verwende options.get() anstatt feste Werte im switch-Statement
                switch (options.get(auswahl - 1)) {
                    case "Verbindung selber einrichten":
                        Verbindung_selber_einrichten();
                        ConnectionManager.getInstance().showConnection();
                        break;
                    case "Verbindung mit Localhost":
                        Verbindung_mit_Localhost();
                        break;
                    case "Verbindung anzeigen":
                        ConnectionManager.getInstance().showConnection();
                        break;
                    case "Prozess starten":
                        Prozessschritte();
                        break;
                    case "Programm beenden":
                        beenden = true;
                        break;
                    case "Gremium wählen":
                        // TODO: Gremium wählen
                        break;
                    case "Sitzung wählen":
                        // TODO: Sitzung wählen
                        break;
                    case "Tagesordnung wählen":
                        // TODO: Tagesordnung wählen
                        break;
                    case "Antrag wählen":
                        // TODO: Antrag wählen
                        break;
                }
            }
        } finally {
            ConnectionManager.getInstance().disconnect();
        }
    }

    private static void init(Scanner scanner) {
        boolean mit_HS_verbunden = Aushilfe.getInstance().frage_Ja_Nein(scanner, "Befinden Sie sich im HS Netz");

        if (!mit_HS_verbunden && Aushilfe.getInstance().frage_Ja_Nein(scanner, "Besteht über Putty ein Tunnel zur HS")) {
            Verbindung_mit_Localhost();
        } else {
            Verbindung_selber_einrichten();
        }

        Aushilfe.getInstance().interne_DB_initialisieren();
        System.out.println();
    }

    private static void Verbindung_mit_Localhost() {
        ConnectionManager.getInstance().setConnection("localhost", "namib", "DABS_42", "DABS_42", "10111");
    }

    private static void Prozessschritte() {
        try {
            /**
             * Aufgabe 1
             * select s.id
             * from sitzungen s
             * inner join hat on hat.id_sitzungen = s.id
             * inner join gremien g on g.id = hat.id_gremien
             * where g.id = <1>
             */
            Aushilfe.getInstance().Aufgabe1();
            
            /**
             * Aufgabe 2
             * select t.id
             * from sitzungen s
             * inner join top on top.id_sitzung = s.id
             * inner join tagesordnung t on t.id = top.id_tagesordnung
             * where s.id = <2>
             */
            Aushilfe.getInstance().Aufgabe2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void X_nachher() {
        // Erstelle ein neues Gremium
        System.out.println("Geben Sie die Bezeichnung des Gremiums ein: ");
        String gremiumName = scanner.nextLine();

        Boolean gremiumOffiziell = Aushilfe.getInstance().frage_Ja_Nein(scanner, "Ist das Gremium offiziell");

        Timestamp sitzungBeginn = Aushilfe.getInstance().getTimestamp("Geben Sie den Beginn der Sitzung ein");
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
            List<Antrag> antraege = new ArrayList<>();
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
            ResultSet rs = Aushilfe.getInstance().getRS("SELECT Antrag.* FROM Antrag JOIN gehoert_zu ON Antrag.ID = gehoert_zu.ID_Antrag WHERE gehoert_zu.ID_TOP = " + Tagesordnung.getAktuellenTOP().getID());
            while (rs.next()) {
                System.out.println("Antrag ID: " + rs.getInt("Antrag.ID"));
                System.out.println("Antrag Titel: " + rs.getString("Antrag.Titel"));
                System.out.println("Antrag Text: " + rs.getString("Antrag.Text"));
                System.out.println("Antrag Ergebnis: " + rs.getString("Antrag.Ergebnis"));
                System.out.println("Antrag Angenommen: " + Boolean.valueOf(rs.getString("Antrag.Angenommen")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Tagesordnungspunkt_oder_Antrag() {

    }

    private static void Protokoll_eintragen() {}
    
    private static void Ende_Sitzung_eintragen() {}

    private static void Verbindung_selber_einrichten() {
        System.out.println("Gib 'null' ein, wenn ein Standardwert verwendet werden soll.");

        // Eingabeaufforderungen für die Verbindungsparameter
        System.out.print("URL der Datenbank: ");
        String url = scanner.nextLine();
        System.out.print("Name der Datenbank: ");
        String db_name = scanner.nextLine();
        System.out.print("Benutzername: ");
        String user = scanner.nextLine();
        System.out.print("Passwort: ");
        String pass = scanner.nextLine();
        System.out.print("Port: ");
        String port = scanner.nextLine();

        ConnectionManager.getInstance().setConnection(url, db_name, user, pass, port);
    }
}