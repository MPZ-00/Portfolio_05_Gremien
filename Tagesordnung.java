public class Tagesordnung extends APrimaryKey {
    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private String Titel;
    private String Kurzbeschreibung;
    private String Protokolltext;

    private static Tagesordnung aktuellerTOP;

    public Tagesordnung(int ID, String Titel, String Kurzbeschreibung, String Protokolltext) {
        setID(ID);
        setTitel(Titel);
        setKurzbeschreibung(Kurzbeschreibung);
        setProtokolltext(Protokolltext);
    }

    public static void setAktuellenTOP(Tagesordnung TOP) {
        aktuellerTOP = TOP;
    }
    public static Tagesordnung getAktuellenTOP() {
        return aktuellerTOP;
    }

    public void setTitel(String Titel) {
        this.Titel = Titel;
    }
    public void setKurzbeschreibung(String Kurzbeschreibung) {
        this.Kurzbeschreibung = Kurzbeschreibung;
    }
    public void setProtokolltext(String Protokolltext) {
        this.Protokolltext = Protokolltext;
    }

    public String getTitel() {
        return this.Titel;
    }
    public String getKurzbeschreibung() {
        return this.Kurzbeschreibung;
    }
    public String getProtokolltext() {
        return this.Protokolltext;
    }

    @Override
    public void Wahl() {
        if (Anzeigen()) {
            String eingabe;
            do {
                System.out.print("\nWelche Tagesordnung soll es ein (Titel oder 'neu'): ");
                eingabe = Main.scanner.nextLine();
            } while (!TOP_mit_Titel(eingabe) && !eingabe.equalsIgnoreCase("neu"));

            if (eingabe.equalsIgnoreCase("neu")) {
                Erzeugen();
            }
        } else {
            if (Aushilfe.getInstance().frage_Ja_Nein("Neue Tagesordnung erstellen")) {
                Erzeugen();
            }
        }
    }
    private boolean TOP_mit_Titel(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (t.getTitel().equalsIgnoreCase(eingabe)) {
                Tagesordnung.setAktuellenTOP(t);
                return true;
            }
        }
        return false;
    }

    @Override
    public void Erzeugen() {
        System.out.print("TOP Titel eingeben: ");
        String titel = Main.scanner.nextLine();
        System.out.print("TOP Kurzbeschreibung eingeben: ");
        String kurzbeschreibung = Main.scanner.nextLine();
        System.out.print("TOP Protokolltext eingeben (oder null): ");
        String protokolltext = Main.scanner.nextLine();
        if (protokolltext.equals("null")) {
            protokolltext = "";
        }

        Tagesordnung.setAktuellenTOP(Factory.getInstance().createTagesordnung(titel, kurzbeschreibung, protokolltext));
        
        ConnectionManager.getInstance().executeStatement(
            "insert into tagesordnung values (" +
            Tagesordnung.getAktuellenTOP().getID() + ", " +
            Tagesordnung.getAktuellenTOP().getTitel() + ", " +
            Tagesordnung.getAktuellenTOP().getKurzbeschreibung() + ", " +
            Tagesordnung.getAktuellenTOP().getProtokolltext() + ")"
        );

        ConnectionManager.getInstance().executeStatement(
            "insert into top values (" +
            Sitzungen.getAktiveSitzung().getID() + ", " +
            Tagesordnung.getAktuellenTOP().getID() + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }

    @Override
    public boolean Anzeigen() {
        Aushilfe.getInstance().print_Titel("Tagesordnung");

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
            return false;
        }

        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (hs.getHS().contains(t.getID())) {
                System.out.printf("\nID: %d\nTitel: %s\nKurzbeschreibung: %s\nProtokolltext: %s", t.getID(), t.getTitel(), t.getKurzbeschreibung(), t.getProtokolltext());
            }
        }
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id) {
        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (t.getID() == id) {
                System.out.printf("\nID: %d\nTitel: %s\nKurzbeschreibung: %s\nProtokolltext: %s", t.getID(), t.getTitel(), t.getKurzbeschreibung(), t.getProtokolltext());
                return true;
            }
        }
        return false;
    }
}
