public class Tagesordnungen extends ATabellenVerwaltung {
    private static Tagesordnung aktuellerTOP;
    private static Tagesordnungen instance;

    public Tagesordnungen() {}
    public static Tagesordnungen getInstance() {
        if (instance == null) {
            instance = new Tagesordnungen();
        }
        return instance;
    }

    public void setAktuellenTOP(Tagesordnung TOP) {
        aktuellerTOP = TOP;
    }
    public Tagesordnung getAktuellenTOP() {
        return aktuellerTOP;
    }

    @Override
    public void Wahl() {
        if (Anzeigen()) {
            String eingabe;
            do {
                System.out.print("\nWelche Tagesordnung soll es ein (Titel oder 'neu'): ");
                eingabe = Main.scanner.nextLine();
            } while (!Enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));

            if (eingabe.equalsIgnoreCase("neu")) {
                Erzeugen();
            }
        } else {
            if (Aushilfe.getInstance().frage_Ja_Nein("Neue Tagesordnung erstellen")) {
                Erzeugen();
            }
        }
    }

    @Override
    public boolean Enthaelt_Eingabe(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (t.getTitel().equalsIgnoreCase(eingabe)) {
                setAktuellenTOP(t);
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

        setAktuellenTOP(Factory.getInstance().createTagesordnung(titel, kurzbeschreibung, protokolltext));
        
        ConnectionManager.getInstance().executeStatement(
            "insert into tagesordnung values (" +
            getAktuellenTOP().getID() + ", " +
            getAktuellenTOP().getTitel() + ", " +
            getAktuellenTOP().getKurzbeschreibung() + ", " +
            getAktuellenTOP().getProtokolltext() + ")"
        );

        ConnectionManager.getInstance().executeStatement(
            "insert into top values (" +
            Sitzungen.getInstance().getAktiveSitzung().getID() + ", " +
            getAktuellenTOP().getID() + ")"
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
            Sitzungen.getInstance().getAktiveSitzung().getID()
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
