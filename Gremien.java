import java.time.LocalDate;

public class Gremien extends ATabellenVerwaltung {
    // public static List<Gremien> objects = new ArrayList<Gremien>(); // Liste aller Objekte von Gremien, O(N)
    private static Gremien aktuellesGremium;

    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private String Name;
    private Boolean Offiziell;
    private Boolean Inoffiziell;
    private LocalDate Beginn;
    private LocalDate Ende;

    public Gremien(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        setName(Name);
        setOffiziell(offiziell);
        setInoffiziell(inoffiziell);
        setBeginn(Beginn);
        setEnde(Ende);
    }
    public Gremien(Integer ID, String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        setID(ID);
        setName(Name);
        setOffiziell(offiziell);
        setInoffiziell(inoffiziell);
        setBeginn(Beginn);
        setEnde(Ende);
    }
    
    public static void setAktuellesGremium(Gremien gremium) {
        aktuellesGremium = gremium;
    }
    public static Gremien getAktuellesGremium() {
        return aktuellesGremium;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public void setOffiziell(Boolean Offiziell) {
        this.Offiziell = Offiziell;
    }
    public void setInoffiziell(Boolean Inoffiziell) {
        this.Inoffiziell = Inoffiziell;
    }
    public void setBeginn(LocalDate Beginn) {
        this.Beginn = Beginn;
    }
    public void setEnde(LocalDate Ende) {
        this.Ende = Ende;
    }

    public String getName() {
        return this.Name;
    }
    public Boolean getOffiziell() {
        return this.Offiziell;
    }
    public Boolean getInoffiziell() {
        return this.Inoffiziell;
    }
    public LocalDate getBeginn() {
        return this.Beginn;
    }
    public LocalDate getEnde() {
        return this.Ende;
    }

    @Override
    public void Wahl() {
        Anzeigen();
        String eingabe;
        Main.scanner.nextLine(); // Eingabepuffer löschen
        do {
            System.out.print("\nWelches Gremium soll es sein (Name oder 'neu'): ");
            eingabe = Main.scanner.nextLine();
        } while (!Gremien_enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));
        
        if (eingabe.equalsIgnoreCase("neu")) {
            Erzeugen();
        }
    }

    @Override
    public void Erzeugen() {
        System.out.print("Bezeichnung des Gremiums: ");
        String name = Main.scanner.nextLine();
        Boolean offiziell = Aushilfe.getInstance().frage_Ja_Nein("Ist das Gremium offiziell");
        LocalDate beginn = Aushilfe.getInstance().getLocalDate("Beginn des Gremiums");
        LocalDate ende = Aushilfe.getInstance().getLocalDate("Ende des Gremiums");

        setAktuellesGremium(Factory.getInstance().createGremien(name, offiziell, !offiziell, beginn, ende));

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
    private boolean Gremien_enthaelt_Eingabe(String eingabe) {
        for (ATabellenVerwaltung object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien) object;
            if (g.getName().equalsIgnoreCase(eingabe)) {
                setAktuellesGremium(g);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean Anzeigen() {
        Aushilfe.getInstance().print_Titel("Gremien");
        for (ATabellenVerwaltung object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien)object;
            System.out.printf("\nID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
        }
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id) {
        Aushilfe.getInstance().print_Titel("Gremien");
        for (ATabellenVerwaltung object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien)object;
            if (g.getID() == id) {
                System.out.printf("\nID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
                return true;
            }
        }
        return false;
    }
}