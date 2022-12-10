import java.util.Scanner;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    private static ArrayList<Gremien> Gremien = null;
    
    public static void main(String[] args) {
        // Erstelle eine Liste aller Gremien
        Gremien = new ArrayList<Gremien>();

        // Erstelle einen Scanner, um die Eingabe vom Benutzer zu lesen
        Scanner scanner = new Scanner(System.in);
        int auswahl;
        boolean beenden = false;

        try {
            // Schleife, die solange läuft, bis der Benutzer das Programm beendet
            while (!beenden) {
                System.out.println("1. Gremium und Beginn der Sitzung auswählen");
                System.out.println("2. Tagesordnungspunkte anzeigen");
                System.out.println("3. Tagesordnungspunkt oder Antrag auswählen");
                System.out.println("4. Protokoll eintragen");
                System.out.println("5. Ende der Sitzung eintragen");
                System.out.println("6. Programm beenden");
                System.out.println("Auswahl: ");

                while (!scanner.hasNextInt() || (auswahl = scanner.nextInt()) < 1 || auswahl > 6) {
                    System.out.println("Bitte gültige Auswahl eingeben (1-6): ");
                    scanner.nextLine(); // Leere den Eingabepuffer
                }

                // Führe die entsprechnede Aktion aus
                switch (auswahl) {
                    case 1: Gremium_und_Beginn_der_Sitzung(); break;
                    case 2: Tagesordnungspunkte_anzeigen(); break;
                    case 3: Tagesordnungspunkt_oder_Antrag(); break;
                    case 4: Protokoll_eintragen(); break;
                    case 5: Ende_Sitzung_eintragen(); break;
                    case 6: beenden = true; break;
                }
            }
        } finally {
            scanner.close();
        }
    }

    static void Gremium_und_Beginn_der_Sitzung() {
        Scanner scanner = new Scanner(System.in);
        Gremien gremium = new Gremien();
        
        // Erstelle ein neues Gremium
        System.out.println("Geben Sie die Bezeichnung des Gremiums ein: ");
        gremium.setName(scanner.nextLine());

        System.out.println("Geben Sie den Beginn der Sitzung ein: ");
        gremium.setBeginn(Date.valueOf(scanner.nextLine()));
        
        // Füge Tagesordnungspunkte hinzu
        while (true) {
            System.out.println("Geben Sie den Namen des Tagesordnungspunks ein (oder 'ende', um die Eingabe zu beenden): ");
            String top = scanner.nextLine();
            
            if (top.equals("ende")) {break;}
            
            gremium.addTOPitem(new Tagesordnungspunkte(top));
        }
        
        Gremien.add(gremium);
        scanner.close();
    }

    static void Tagesordnungspunkte_anzeigen() {
        Gremien aktuellesGremium = Gremien.get(Gremien.size() - 1);
        ArrayList<Tagesordnungspunkte> TOPs = aktuellesGremium.getTOPitems();

        // Schleife durch alle TOPs
        for (Tagesordnungspunkte top : TOPs) {
            // Ausgabe der Namen und Anträge jedes Tagesordnungspunktes
            System.out.println("Name: " + top.getName());
            System.out.println("Anträge: ");

            // Schleife durch alle Anträge des Tagesordnungspunktes
            for (Antrag antrag : top.getAntraege()) {
                // Ausgabe des Titels, Textes, Ergebnisses und Angenommenen-Status des Antrags
                System.out.println("Titel: " + antrag.getTitel());
                System.out.println("Text: " + antrag.getText());
                System.out.println("Ergebnis: " + antrag.getErgebnis());
                System.out.println("Angenommen: " + (antrag.isAngenommen() ? "ja" : "nein"));
            }
        }
    }

    static void Tagesordnungspunkt_oder_Antrag() {

    }

    static void Protokoll_eintragen() {}
    
    static void Ende_Sitzung_eintragen() {}
}
