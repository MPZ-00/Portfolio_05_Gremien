import java.util.EnumSet;

public class Antrag extends APrimaryKey {
    public enum Ergebnis {
        JA, NEIN, ENTHALTUNG
    }
    private String Titel;
    private String Text;
    private Ergebnis Ergebnis;
    private boolean Angenommen;

    private static Antrag aktuellerAntrag;

    public Antrag(int ID, String Titel, String Text, Ergebnis Ergebnis, boolean Angenommen) {
        setID(ID);
        setTitel(Titel);
        setText(Text);
        setErgebnis(Ergebnis);
        setAngenommen(Angenommen);
    }

    public static void setAktuellenAntrag(Antrag antrag) {
        aktuellerAntrag = antrag;
    }
    public static Antrag getAktuellenAntrag() {
        return aktuellerAntrag;
    }
    
    public void setTitel(String Titel) {
        this.Titel = Titel;
    }
    public void setText(String Text) {
        this.Text = Text;
    }
    public void setAngenommen(Boolean Angenommen) {
        this.Angenommen = Angenommen;
    }

    public String getTitel() {
        return this.Titel;
    }
    public String getText() {
        return this.Text;
    }
    public Ergebnis getErgebnis() {
        return this.Ergebnis;
    }
    public Boolean isAngenommen() {
        return this.Angenommen;
    }

    public void setErgebnis(Ergebnis Ergebnis) {
        this.Ergebnis = Ergebnis;
    }

    private boolean Antrag_mit_Titel(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Antrag.class.toString())) {
            Antrag a = (Antrag)object;
            if (a.getTitel().equalsIgnoreCase(eingabe)) {
                Antrag.setAktuellenAntrag(a);
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
            } while (!Antrag_mit_Titel(eingabe) && !eingabe.equalsIgnoreCase("neu"));

            if (eingabe.equalsIgnoreCase("neu")) {
                Erzeugen();
            }
        } else if (Aushilfe.getInstance().frage_Ja_Nein("Neuen Antrag erstellen")) {
            Erzeugen();
        }
    }

    @Override
    public boolean Anzeigen() {
        return Anzeigen(Tagesordnung.getAktuellenTOP().getID());
    }

    @Override
    public boolean Anzeigen(Integer id) {
        Aushilfe.getInstance().print_Titel("Antrag für Tagesordnung (" + id + ")");

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
        } while (!ergebnisse.contains(Ergebnis.valueOf(input)));
        Ergebnis ergebnis = Ergebnis.valueOf(input);
        boolean angenommen = Aushilfe.getInstance().frage_Ja_Nein("Antrag angenommen");

        Antrag.setAktuellenAntrag(Factory.getInstance().createAngtrag(titel, text, ergebnis, angenommen));

        ConnectionManager.getInstance().executeStatement("insert into antrag values (" +
                Antrag.getAktuellenAntrag().getID() + ", " +
                Antrag.getAktuellenAntrag().getTitel() + ", " +
                Antrag.getAktuellenAntrag().getText() + ", " +
                Antrag.getAktuellenAntrag().getErgebnis().toString() + ", " +
                (Antrag.getAktuellenAntrag().isAngenommen() == true ? "1" : "0") +
                ")");
        ConnectionManager.getInstance().executeStatement(
                "insert into gehoert_zu values (" +
                    Antrag.getAktuellenAntrag().getID() + ", " +
                    Tagesordnung.getAktuellenTOP().getID() + ")");
        ConnectionManager.getInstance().executeStatement("commit");
    }
}