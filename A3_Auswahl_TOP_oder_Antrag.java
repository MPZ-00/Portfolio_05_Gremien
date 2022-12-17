import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A3_Auswahl_TOP_oder_Antrag {
    private boolean is_TOP = true;
    private Integer ID;

    public A3_Auswahl_TOP_oder_Antrag() {
        // Auswahl eines TOPs oder eines Antrags einer Sitzung
        System.out.println();
        Aushilfe.getInstance().print_Titel("Aufgabe 3");

        hs_ids hs = new hs_ids(
            "select t.id " +
            "from tagesordnung t " +
            "inner join top on top.id_tagesordnung = t.id " +
            "inner join sitzungen s on s.id = top.id_sitzung " +
            "where s.id = " +
            Sitzungen.getAktiveSitzung().getID()
        );

        if (hs.getHS().size() == 0) {
            System.err.println("Für diese Sitzung gibt es keine Tagesordnungen");
            return;
        }
        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (hs.getHS().contains(t.getID())) {
                System.out.println(
                    "[Tagesordnung]" +
                    "\nID: " + t.getID() +
                    "\nTitel: " + t.getTitel() +
                    "\nKurzbeschreibung: " + t.getKurzbeschreibung() +
                    "\nProtokolltext: " + t.getProtokolltext()
                );
                Antrag.getAktuellenAntrag().Anzeigen(t.getID());
            }
        }

        String eingabe;
        do {
            System.out.print("\nWelcher Tagesordnungspunkt ('TOP ID') oder Antrag ('A ID') soll es sein: ");
            eingabe = Main.scanner.nextLine();
        } while (!test_auf_TOP_oder_Antrag(eingabe));
        
        int max_length_protokolltext = 4000;
        if (this.is_TOP) {
            do {
                System.out.println("Sie können jetzt das Protokoll für den TOP (" + this.ID + ") eintragen:");
            
                eingabe = Main.scanner.nextLine();
                if (eingabe.length() > max_length_protokolltext) {
                    Aushilfe.getInstance().print_Warnung("Bitte beachten Sie, dass das Protokoll eine Gesamtlänge von maximal " + max_length_protokolltext + " Zeichen haben darf:");
                }
            } while (eingabe.length() > max_length_protokolltext);

            ConnectionManager.getInstance().executeStatement(
                "update Tagesordnung " +
                "set protokolltext = '" + eingabe + "'" +
                " where id = " + this.ID
            );
        } else {
            do {
                System.out.println("Sie können jetzt das Ergebnis (ja, nein, enthaltung) für den Antrag (" + this.ID + ") eintragen:");
                eingabe = Main.scanner.nextLine();
            } while (!eingabe.toLowerCase().contains("ja") && eingabe.toLowerCase().contains("nein") && eingabe.toLowerCase().contains("enthaltung"));

            ConnectionManager.getInstance().executeStatement(
                "update antrag " +
                "set ergebnis = '" + eingabe + "'" +
                " where id = " + this.ID
            );
        }
    }

    private boolean ist_TOP(String eingabe) {
        return eingabe.toLowerCase().contains("top");
    }

    private boolean ist_Antrag(String eingabe) {
        return eingabe.toLowerCase().contains("a") || eingabe.toLowerCase().contains("antrag");
    }

    private Integer extrahiere_ID(String eingabe) {
        Pattern pattern = Pattern.compile("(\\w+ +)(\\d+)");
        Matcher matcher = pattern.matcher(eingabe);
        return Integer.parseInt(matcher.replaceAll("$2"));
    }

    private boolean test_auf_TOP_oder_Antrag(String eingabe) {
        this.ID = extrahiere_ID(eingabe);
        this.is_TOP = ist_TOP(eingabe);
        return this.is_TOP || ist_Antrag(eingabe);
    }
}
