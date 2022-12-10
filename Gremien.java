import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Gremien implements IGremien {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    public static List<Gremien> objects = new ArrayList<Gremien>(); // Liste aller Objekte von Gremien
    
    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private int ID;
    private String Name;
    private Boolean Offiziell;
    private Boolean Inoffiziell;
    private Date Beginn;
    private Date Ende;

    public Gremien(String Name, String offiziell, String inoffiziell, Date Beginn, Date Ende) {
        setID(nextID++);
        setName(Name);
        setOffiziell(Offiziell);
        setInoffiziell(Inoffiziell);
        setBeginn(Beginn);
        setEnde(Ende);
        objects.add(this);
    }
    
    public void setID(int ID) {
        // Überprüfung, ob die angegebene ID bereits im Set für verwendete IDs vorhanden ist
        if (usedIDs.contains(ID)) {
            throw new IllegalArgumentException("ID(" + ID + ") ist bereits in verwendung");
        }
        
        // Wert des ID-Attributs ändern und neue ID im Set für verwendete IDs speichern
        usedIDs.add(this.ID);
        this.ID = ID;
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
    public void setBeginn(Date Beginn) {
        this.Beginn = Beginn;
    }
    public void setEnde(Date Ende) {
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
    public Date getBeginn() {
        return this.Beginn;
    }
    public Date getEnde() {
        return this.Ende;
    }
}