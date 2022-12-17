public class Aufgabengebiete extends ATabellenVerwaltung {
    private static Aufgabengebiet aktuellesAufgabengebiet;
    private static Aufgabengebiete instance;

    public Aufgabengebiete() {}
    public static Aufgabengebiete getInstance() {
        if (instance == null) {
            instance = new Aufgabengebiete();
        }
        return instance;
    }

    public void setAktuellesAufgabengebiet(Aufgabengebiet aufgabengebiet) {
        aktuellesAufgabengebiet = aufgabengebiet;
    }

    public Aufgabengebiet getAktuellesAufgabengebiet() {
        return aktuellesAufgabengebiet;
    }

    private boolean Aufgabengebiet_vorhanden(String aufgabengebiet) {
        // TODO: diese Funktion hier einbauen
        return true;
    }

    @Override
    public boolean Enthaelt_Eingabe(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Aufgabengebiet.class.toString())) {
            Aufgabengebiet a = (Aufgabengebiet) object;
            if (a.getAufgabengebiet().equalsIgnoreCase(eingabe)) {
                setAktuellesAufgabengebiet(a);
                return true;
            }
        }
        return false;
    }

    @Override
    public void Wahl() {
        Anzeigen();
        String eingabe;
        Main.scanner.nextLine(); // Eingabepuffer löschen
        do {
            System.out.print("\nWelches Aufgabengebiet soll es sein: ");
            eingabe = Main.scanner.nextLine();
        } while (!Enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));

        ConnectionManager.getInstance().executeStatement(
            "insert into aufgabengebiete values (" +
            getAktuellesAufgabengebiet().getID() + ", " +
            getAktuellesAufgabengebiet().getAufgabengebiet() + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }

    @Override
    public void Erzeugen() {
        System.out.println("Aufgabengebiet für das Gremium (" + Gremien.getInstance().getAktuellesGremium().getID() + "): ");
        String Aufgabengebiet = Main.scanner.nextLine();
        setAktuellesAufgabengebiet(Factory.getInstance().createAufgabengebiet(Aufgabengebiet));
    }

    @Override
    public boolean Anzeigen() {
        Drucken.getInstance().print_Titel("Aufgabengebiete");
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id_gremium) {
        Drucken.getInstance().print_Titel("Aufgabengebiete für Gremium (" + Gremien.getInstance().getAktuellesGremium().getID() + ")");

        hs_ids hs = new hs_ids(
            "select a.id " +
            "from aufgabengebiete a " +
            "inner join aufgaben_gremien ag on ag.id_aufgabe = a.id " +
            "where ag.id_gremium = " + id_gremium
        );

        if (hs.getHS().isEmpty()) {
            System.err.println("Für das Gremium (" + id_gremium + ") gibt es keine Aufgabengebiete");
            return false;
        }

        for (APrimaryKey object : Factory.getInstance().getObject(Aufgabengebiet.class.toString())) {
            Aufgabengebiet a = (Aufgabengebiet) object;
            if (hs.getHS().contains(a.getID())) {
                System.out.println(
                    "ID: " + a.getID() +
                    "Aufgabengebiet: " + a.getAufgabengebiet()
                );
            }
        }
        return true;
    }
}