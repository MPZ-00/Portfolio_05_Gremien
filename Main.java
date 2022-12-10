import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Main {
    private static String PATTERN = "dd-MM-YYYY HH:mm:ss";
    
    public static void main(String[] args) {
        // Erstelle einen Scanner, um die Eingabe vom Benutzer zu lesen
        Scanner scanner = new Scanner(System.in);
        int auswahl;
        boolean beenden = false;

        try {
            // Schleife, die solange läuft, bis der Benutzer das Programm beendet
            while (!beenden) {
                System.out.println("1. Gremium und Beginn der Sitzung auswählen");
                System.out.println("2. Tagesordnung anzeigen");
                System.out.println("3. Tagesordnungspunkt oder Antrag auswählen");
                System.out.println("4. Protokoll eintragen");
                System.out.println("5. Ende der Sitzung eintragen");
                System.out.println("6. Programm beenden");
                System.out.println("Auswahl: ");

                while (!scanner.hasNextInt() || (auswahl = scanner.nextInt()) < 1 || auswahl > 6) {
                    System.out.println("Bitte gültige Auswahl eingeben (1-6): ");
                    scanner.nextLine(); // Leere den Eingabepuffer
                }

                switch (auswahl) {
                    case 1: Gremium_und_Beginn_der_Sitzung(); break;
                    case 2: Tagesordnung_anzeigen(); break;
                    case 3: Tagesordnungspunkt_oder_Antrag(); break;
                    case 4: Protokoll_eintragen(); break;
                    case 5: Ende_Sitzung_eintragen(); break;
                    case 6: beenden = true; break;
                }
            }
        } finally {
            scanner.close();
            ConnectionManager.getInstance().disconnect();
        }
    }

    static void Gremium_und_Beginn_der_Sitzung() {
        Scanner scanner = new Scanner(System.in);
        
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
        Sitzungen sitzung = new Sitzungen(sitzungBeginn, sitzungEnde, LocalDate.now(), true, "", "");
        Sitzungen.setAktiveSitzung(sitzung);

        // Füge Tagesordnung hinzu
        while (true) {
            System.out.println("Geben Sie den Namen des Tagesordnungspunks ein (oder 'ende', um die Eingabe zu beenden): ");
            String top = scanner.nextLine();
            
            if (top.equals("ende")) {break;}
            
        }
        
        scanner.close();
    }

    public static Timestamp getTimestamp(Scanner scanner) {
        // Eingabeaufforderung ausgeben
        System.out.println("Bitte geben Sie das Tiemstamp im Format dd-MM-YYYY HH:mm:ss ein: ");

        // Nutzereingabe empfangen
        String input = scanner.nextLine();

        try {
            // Konvertiere den Eingabe-String in ein LocalDateTime-Objekt
            LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern(PATTERN));

            // Konvertiere das LocalDateTime-Objekt in ein Timestamp-Objekt
            Timestamp timestamp = Timestamp.from(dateTime.toInstant(ZoneOffset.UTC));

            return timestamp;
        } catch (IllegalArgumentException e) {
            // Fehlermeldung ausgeben und erneut nach Timestamp fragen
            System.out.println("Ungültiges Datumsformat. Bitte versuchen Sie es erneut.");
            return getTimestamp(scanner);
        }
    }

    static boolean ist_Offiziell(Scanner scanner) {
        String input;
        do {
            System.out.println("Ist das Gremium offiziell (ja/nein): ");
            input = scanner.nextLine();
        } while (!input.equalsIgnoreCase("ja") && !input.equalsIgnoreCase("nein"));
        
        if (input.equalsIgnoreCase("ja")) {
            return true;
        }
        return false;
    }

    static void Tagesordnung_anzeigen() {
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
        } catch (SQLException e) {
            e.getStackTrace();
        }
        // System.println(antraege); // TODO: fix this
        /*
        Gremien aktuellesGremium = Gremien.get(Gremien.size() - 1);
        ArrayList<Tagesordnung> TOPs = aktuellesGremium.getTOPitems();

        // Schleife durch alle TOPs
        for (Tagesordnung top : TOPs) {
            // Ausgabe der Namen und Anträge jedes Tagesordnungs
            System.out.println("Name: " + top.getName());
            System.out.println("Anträge: ");

            // Schleife durch alle Anträge des Tagesordnungs
            for (Antrag antrag : top.getAntraege()) {
                // Ausgabe des Titels, Textes, Ergebnisses und Angenommenen-Status des Antrags
                System.out.println("Titel: " + antrag.getTitel());
                System.out.println("Text: " + antrag.getText());
                System.out.println("Ergebnis: " + antrag.getErgebnis());
                System.out.println("Angenommen: " + (antrag.isAngenommen() ? "ja" : "nein"));
            }
        }*/
    }

    static void Tagesordnungspunkt_oder_Antrag() {

    }

    static void Protokoll_eintragen() {}
    
    static void Ende_Sitzung_eintragen() {}
}
