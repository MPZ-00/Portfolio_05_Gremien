import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Erstelle eine Liste aller Gremien
        ArrayList<Gremien> Gremien = new ArrayList<Gremien>();

        // Erstelle einen Scanner, um die Eingabe vom Benutzer zu lesen
        Scanner scanner = new Scanner(System.in);

        // Schleife, die solange läuft, bis der Benutzer das Programm beendet
        while (true) {
            System.out.println("1. Gremium und Beginn der Sitzung auswählen");
            System.out.println("2. Tagesordnungspunkte anzeigen");
            System.out.println("3. Tagesordnungspunkt oder Antrag auswählen");
            System.out.println("4. Protokoll eintragen1");
            System.out.println("5. Ende der Sitzung eintragen");
            System.out.println("6. Programm beenden");
            System.out.println("Auswahl: ");
        }
    }
}
