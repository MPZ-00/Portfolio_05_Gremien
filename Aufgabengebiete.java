public class Aufgabengebiete extends APrimaryKey {
    private String Aufgabengebiet;
    private static Aufgabengebiete aktuellesAufgabengebiet;

    public Aufgabengebiete(int ID, String Aufgabengebiet) {
        setID(ID);
        setAufgabengebiet(Aufgabengebiet);
    }

    public void setAufgabengebiet(String Aufgabengebiet) {
        this.Aufgabengebiet = Aufgabengebiet;
    }

    public String getAufgabengebiet() {
        return this.Aufgabengebiet;
    };

    public static void setAktuellesAufgabengebiet(Aufgabengebiete aufgabengebiet) {
        aktuellesAufgabengebiet = aufgabengebiet;
    }

    public static Aufgabengebiete getAktuellesAufgabengebiet() {
        return aktuellesAufgabengebiet;
    }

    private boolean Aufgabengebiet_vorhanden(String aufgabengebiet) {
        return true;
    }

    private boolean Aufgabengebiet_enthaelt_Eingabe(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Aufgabengebiete.class.toString())) {
            Aufgabengebiete a = (Aufgabengebiete) object;
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
        } while (!Aufgabengebiet_enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));

        ConnectionManager.getInstance().executeStatement(
            "insert into aufgabengebiete values (" +
            getAktuellesAufgabengebiet().getID() + ", " +
            getAktuellesAufgabengebiet().getAufgabengebiet() + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }

    @Override
    public void Erzeugen() {
        System.out.println("Aufgabengebiet für das Gremium (" + Gremien.getAktuellesGremium().getID() + "): ");
        String Aufgabengebiet = Main.scanner.nextLine();
        setAktuellesAufgabengebiet(Factory.getInstance().createAufgabengebiete(Aufgabengebiet));
    }

    @Override
    public boolean Anzeigen() {
        Aushilfe.getInstance().print_Titel("Aufgabengebiete");
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id_gremium) {
        Aushilfe.getInstance().print_Titel("Aufgabengebiete für Gremium (" + Gremien.getAktuellesGremium().getID() + ")");

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

        for (APrimaryKey object : Factory.getInstance().getObject(Aufgabengebiete.class.toString())) {
            Aufgabengebiete a = (Aufgabengebiete) object;
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