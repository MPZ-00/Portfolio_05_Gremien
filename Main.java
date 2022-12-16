import java.util.*;

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
                        Aushilfe.getInstance().Gremium_Wahl();
                        break;
                    case "Sitzung wählen":
                        Aushilfe.getInstance().Sitzung_Wahl();
                        break;
                    case "Tagesordnung wählen":
                        Aushilfe.getInstance().Tagesordnung_Wahl();
                        break;
                    case "Antrag wählen":
                        Aushilfe.getInstance().Antrag_Wahl();
                        break;
                }
            }
        } finally {
            ConnectionManager.getInstance().disconnect();
        }
    }

    private static void init(Scanner scanner) {
        boolean mit_Tunnel_verbunden = Aushilfe.getInstance().frage_Ja_Nein("Besteht über Putty ein Tunnel zur HS");

        if (!mit_Tunnel_verbunden && Aushilfe.getInstance().frage_Ja_Nein("Befinden Sie sich im HS Netz")) {
            Verbindung_selber_einrichten();
        } else {
            Verbindung_mit_Localhost();
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
            new Aufgabe1();
            
            /**
             * Aufgabe 2
             * select t.id
             * from sitzungen s
             * inner join top on top.id_sitzung = s.id
             * inner join tagesordnung t on t.id = top.id_tagesordnung
             * where s.id = <2>
             * 
             * select a.id
             * from tagesordnung t
             * inner join gehoert_zu on gehoert_zu.id_top = t.id
             * inner join antrag a on a.id = gehoert_zu.id_antrag
             * where t.id = <3>
             */
            new Aufgabe2();

            /**
             * Aufgabe 3
             * select t.id
             * from tagesordnung t
             * inner join top on top.id_tagesordnung = t.id
             * inner join sitzung s on s.id = top.id_sitzung
             * where s.id = 1
             */
            new Aufgabe3();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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