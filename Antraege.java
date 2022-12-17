import java.util.EnumSet;

public class Antraege extends ATabellenVerwaltung {
    private static Antrag aktuellerAntrag;
    private static Antraege instance;

    public Antraege() {}

    public static Antraege getInstance() {
        if (instance == null) {
            instance = new Antraege();
        }
        return instance;
    }
    
    public void setAktuellenAntrag(Antrag antrag) {
        aktuellerAntrag = antrag;
    }

    public Antrag getAktuellenAntrag() {
        return aktuellerAntrag;
    }

    @Override
    public boolean Enthaelt_Eingabe(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Antrag.class.toString())) {
            Antrag a = (Antrag)object;
            if (a.getTitel().equalsIgnoreCase(eingabe)) {
                setAktuellenAntrag(a);
                return true;
            }
        }
        return false;
    }

    @Override
    public void Wahl() {
        if (Anzeigen()) {
            String eingabe;
            do {
                System.out.print("\nWelcher Antrag soll es sein (Titel oder 'neu'): ");
                eingabe = Main.scanner.nextLine();
            } while (!Enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));

            if (eingabe.equalsIgnoreCase("neu")) {
                Erzeugen();
            }
        } else if (Tools.getInstance().frage_Ja_Nein("Neuen Antrag erstellen")) {
            Erzeugen();
        }
    }

    @Override
    public boolean Anzeigen() {
        return Anzeigen(Tagesordnungen.getInstance().getAktuellenTOP().getID());
    }

    @Override
    public boolean Anzeigen(Integer id) {
        Drucken.getInstance().print_Titel("Antrag für Tagesordnung (" + id + ")");

        hs_ids hs = new hs_ids(
            "select a.id " +
            "from antrag a " +
            "inner join gehoert_zu on gehoert_zu.id_antrag = a.id " +
            "inner join tagesordnung t on t.id = gehoert_zu.id_top " +
            "where t.id = " +
            id
        );

        if (hs.getHS().size() == 0) {
            System.err.println("Für diese Tagesordnung gibt es keine Anträge");
            return false;
        }

        for (APrimaryKey object : Factory.getInstance().getObject(Antrag.class.toString())) {
            Antrag a = (Antrag)object;
            if (hs.getHS().contains(a.getID())) {
                System.out.println(
                    "\nID: " + a.getID() +
                    "\nTitel: " +a.getTitel() +
                    "\nText: " + a.getText() +
                    "\nErgebnis: " + a.getErgebnis() +
                    "\nAngenommen: " + a.isAngenommen()
                );
            }
        }
        return true;
    }

    @Override
    public void Erzeugen() {
        System.out.print("Antrag Titel eingeben: ");
        String titel = Main.scanner.nextLine();
        System.out.print("Antrag Text eingeben: ");
        String text = Main.scanner.nextLine();
        EnumSet<Antrag.Ergebnis> ergebnisse = EnumSet.allOf(Antrag.Ergebnis.class);
        
        String input;
        do {
            System.out.print("Antrag Ergebnis eingeben (" + ergebnisse.spliterator() + "): ");
            input = Main.scanner.nextLine().toUpperCase();
        } while (!ergebnisse.contains(Antrag.Ergebnis.valueOf(input)));

        Antrag.Ergebnis ergebnis = Antrag.Ergebnis.valueOf(input);
        boolean angenommen = Tools.getInstance().frage_Ja_Nein("Antrag angenommen");

        setAktuellenAntrag(Factory.getInstance().createAntrag(titel, text, ergebnis, angenommen));

        ConnectionManager.getInstance().executeStatement("insert into antrag values (" +
            getAktuellenAntrag().getID() + ", " +
            getAktuellenAntrag().getTitel() + ", " +
            getAktuellenAntrag().getText() + ", " +
            getAktuellenAntrag().getErgebnis().toString() + ", " +
            (getAktuellenAntrag().isAngenommen() ? "1" : "0") +")"
        );

        ConnectionManager.getInstance().executeStatement(
            "insert into gehoert_zu values (" +
            getAktuellenAntrag().getID() + ", " +
            Tagesordnungen.getInstance().getAktuellenTOP().getID() + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }
}
