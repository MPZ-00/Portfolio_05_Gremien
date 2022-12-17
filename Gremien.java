import java.time.LocalDate;

public class Gremien extends ATabellenVerwaltung {
    private static Gremium aktuellesGremium = null;
    private static Gremien instance = null;

    public Gremien() {}

    public static Gremien getInstance() {
        if (instance == null) {
            instance = new Gremien();
        }
        return instance;
    }

    public void setAktuellesGremium(Gremium gremium) {
        aktuellesGremium = gremium;
    }
    public Gremium getAktuellesGremium() {
        return aktuellesGremium;
    }

    @Override
    public void Wahl() {
        Anzeigen();
        String eingabe;
        Main.scanner.nextLine(); // Eingabepuffer löschen
        do {
            System.out.print("\nWelches Gremium soll es sein (Name oder 'neu'): ");
            eingabe = Main.scanner.nextLine();
        } while (!Enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));
        
        if (eingabe.equalsIgnoreCase("neu")) {
            Erzeugen();
        }
    }

    @Override
    public void Erzeugen() {
        System.out.print("Bezeichnung des Gremiums: ");
        String name = Main.scanner.nextLine();
        Boolean offiziell = Tools.getInstance().frage_Ja_Nein("Ist das Gremium offiziell");
        LocalDate beginn = Tools.getInstance().getLocalDate("Beginn des Gremiums");
        LocalDate ende = Tools.getInstance().getLocalDate("Ende des Gremiums");

        setAktuellesGremium(Factory.getInstance().createGremium(name, offiziell, !offiziell, beginn, ende));

        ConnectionManager.getInstance().executeStatement(
            "insert into Gremien values (" +
            getAktuellesGremium().getID() + ", " +
            getAktuellesGremium().getName() + ", " +
            (getAktuellesGremium().getOffiziell() ? "1" : "0") + ", " +
            (getAktuellesGremium().getInoffiziell() ? "1" : "0") + ", " +
            java.sql.Date.valueOf(getAktuellesGremium().getBeginn()) + ", " +
            java.sql.Date.valueOf(getAktuellesGremium().getEnde()) + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }

    @Override
    public boolean Enthaelt_Eingabe(String eingabe) {
        for (APrimaryKey object : Factory.getInstance().getObject(Gremium.class.toString())) {
            Gremium g = (Gremium) object;
            if (g.getName().equalsIgnoreCase(eingabe)) {
                setAktuellesGremium(g);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean Anzeigen() {
        Drucken.getInstance().print_Titel("Gremium");
        for (APrimaryKey object : Factory.getInstance().getObject(Gremium.class.toString())) {
            Gremium g = (Gremium)object;
            System.out.printf("\nID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
        }
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id) {
        Drucken.getInstance().print_Titel("Gremium");
        for (APrimaryKey object : Factory.getInstance().getObject(Gremium.class.toString())) {
            Gremium g = (Gremium)object;
            if (g.getID() == id) {
                System.out.printf("\nID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
                return true;
            }
        }
        return false;
    }
}