import java.time.LocalDate;

public class Gremien extends ATabellenVerwaltung implements IGremien {
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
}