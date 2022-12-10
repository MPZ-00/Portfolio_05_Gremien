import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Gremien extends AHauptklasse implements IGremien {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    // public static List<Gremien> objects = new ArrayList<Gremien>(); // Liste aller Objekte von Gremien, O(N)
    public static HashMap<Integer, Gremien> objects = new HashMap<>(); // Liste aller Objekte von Gremien, O(1)
    private static Gremien aktuellesGremium;

    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private int ID;
    private String Name;
    private Boolean Offiziell;
    private Boolean Inoffiziell;
    private LocalDate Beginn;
    private LocalDate Ende;

    public Gremien(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        setID(nextID++);
        setName(Name);
        setOffiziell(Offiziell);
        setInoffiziell(Inoffiziell);
        setBeginn(Beginn);
        setEnde(Ende);
        objects.put(getID(), this);
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

    public int getID() {
        return this.ID;
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