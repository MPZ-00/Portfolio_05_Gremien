import java.util.*;

public class Main extends Tools {
    public static final Scanner scanner = new Scanner(System.in);
    private static final List<String> options = Arrays.asList(
        "Prozess starten",
        "Verbindung selber einrichten",
        "Verbindung mit Localhost",
        "Programm beenden"
    );
    
    public static void main(String[] args) {
        try {
            int auswahl;
            boolean beenden = false;
            
            if (!init_DB()) {
                return;
            }

            while (!beenden) {
                System.out.println();
                Drucken.getInstance().print_Titel("Menü");

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
                }
            }
        } finally {
            ConnectionManager.getInstance().disconnect();
        }
    }

    private static boolean init_DB() {
        boolean mit_Tunnel_verbunden = Tools.getInstance().frage_Ja_Nein("Besteht über Putty ein Tunnel zur HS");

        if (mit_Tunnel_verbunden) {
            Verbindung_mit_Localhost();
        } else if (Tools.getInstance().frage_Ja_Nein("Befinden Sie sich im HS Netz")) {
            Verbindung_mit_Neptun();
        } else {
            Verbindung_selber_einrichten();
        }

        if (!DBInitialisierung.getInstance().interne_DB_initialisieren()) {
            System.err.println("Fehler beim Initialisieren der DB");
            return false;
        }
        System.out.println();
        return true;
    }

    private static void Verbindung_mit_Localhost() {
        ConnectionManager.getInstance().setConnection("localhost", "namib", "DABS_42", "DABS_42", "10111");
    }

    private static void Verbindung_mit_Neptun() {
        ConnectionManager.getInstance().setConnection("fbe-neptun.hs-weingarten.de", "namib", "DABS_42", "DABS_42", "1521");
    }

    private static void Prozessschritte() {
        try {
            new A1_Eingabe_Gremium_und_Sitzung();
            
            new A2_TOPs_und_Antraege_anzeigen();

            do {
                System.out.println();
                new A3_Auswahl_TOP_oder_Antrag();

                if (A4_gesamtes_Protokoll_eintragen.getInstance().is_any_Protokolltext_null()) {
                    Drucken.getInstance().print_Warnung("Es gibt noch einen TOP, bei dem der Protokolltext 'null' ist!");
                }
            } while (A4_gesamtes_Protokoll_eintragen.getInstance().is_any_Protokolltext_null());

            new A5_Das_Ende_der_Sitzung_eintragen();
        } catch (Exception e) {
            System.err.println("In Prozessschritte ist ein Fehler aufgetreten");
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