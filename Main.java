import java.util.*;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Main extends Aushilfe {
    private static final List<String> options = Arrays.asList(
        "Gremium und Beginn der Sitzung auswählen",
        "Tagesordnung anzeigen",
        "Tagesordnungspunkt oder Antrag auswählen",
        "Protokoll eintragen",
        "Ende der Sitzung eintragen",
        "Verbindung selber einrichten",
        "Verbindung mit Localhost",
        "Programm beenden"
    );
    
    public static void main(String[] args) {
        // Erstelle einen Scanner, um die Eingabe vom Benutzer zu lesen

        try (Scanner scanner = new Scanner(System.in)) {
            int auswahl;
            boolean beenden = false;
            
            init(scanner);

            while (!beenden) {
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i));
                }
                System.out.print("Auswahl: ");

                while (!scanner.hasNextInt() || (auswahl = scanner.nextInt()) < 1 || auswahl > options.size()) {
                    System.out.println("Bitte gültige Auswahl eingeben (1-" + options.size() + "): ");
                    scanner.nextLine(); // Leere den Eingabepuffer
                }

                // Verwende options.get() anstatt feste Werte im switch-Statement
                switch (options.get(auswahl - 1)) {
                    case "Verbindung selber einrichten":
                        Verbindung_selber_einrichten();
                        ConnectionManager.getInstance().showConnection();
                        break;
                    case "Gremium und Beginn der Sitzung auswählen":
                        Gremium_und_Beginn_der_Sitzung();
                        break;
                    case "Tagesordnung anzeigen":
                        Tagesordnung_anzeigen();
                        break;
                    case "Tagesordnungspunkt oder Antrag auswählen":
                        Tagesordnungspunkt_oder_Antrag();
                        break;
                    case "Protokoll eintragen":
                        Protokoll_eintragen();
                        break;
                    case "Ende der Sitzung eintragen":
                        Ende_Sitzung_eintragen();
                        break;
                    case "Programm beenden":
                        beenden = true;
                        break;
                    case "Verbindung mit Localhost":
                        Verbindung_mit_Localhost();
                        break;
                    case "X_nachher":
                        X_nachher();
                        break;
                    case "Interne DB initialisieren":
                        Aushilfe.getInstance().interne_DB_initialisieren();
                        break;
                    case "Verbindung anzeigen":
                        ConnectionManager.getInstance().showConnection();
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
    }

    private static void Verbindung_mit_Localhost() {
        ConnectionManager.getInstance().setConnection("localhost", "namib", "DABS_42", "DABS_42", "10111");
    }

    static void Gremium_und_Beginn_der_Sitzung() {
        Aushilfe.getInstance().Gremium_Wahl();
        System.out.println("Ausgewähltes Gremium (ID/Name): " + Gremien.getAktuellesGremium().getID() + "/" + Gremien.getAktuellesGremium().getName());
        Aushilfe.getInstance().Sitzung_Wahl();
        System.out.println("Ausgewählte Sitzung (ID/Beginn): " + Sitzungen.getAktiveSitzung().getID() + "/" + Sitzungen.getAktiveSitzung().getBeginn());
    }

    private static void X_nachher() {
        Scanner scanner = new Scanner(System.in);
        
        // Erstelle ein neues Gremium
        System.out.println("Geben Sie die Bezeichnung des Gremiums ein: ");
        String gremiumName = scanner.nextLine();

        Boolean gremiumOffiziell = Aushilfe.getInstance().frage_Ja_Nein(scanner, "Ist das Gremium offiziell");

        Timestamp sitzungBeginn = Aushilfe.getInstance().getTimestamp(scanner, "Geben Sie den Beginn der Sitzung ein", "dd-MM-YYYY HH:mm:ss");
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
            ResultSet rs = getRS("SELECT Antrag.* FROM Antrag JOIN gehoert_zu ON Antrag.ID = gehoert_zu.ID_Antrag WHERE gehoert_zu.ID_TOP = " + Tagesordnung.getAktuellenTOP().getID());
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

        ConnectionManager.getInstance().setConnection(url, db_name, user, pass, port);
        input.close();
    }
}