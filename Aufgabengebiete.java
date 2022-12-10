import java.util.HashSet;
import java.util.Set;

public class Aufgabengebiete implements IAufgabengebiete {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    
    // Attribute
    private int ID;
    private int Ag_ID; // Referenz auf Gremium
    private String Aufgabengebiet;

    public Aufgabengebiete(int ID, int Ag_ID, String Aufgabengebiet) {
        setID(nextID++);
        setAg_ID(Ag_ID);
        setAufgabengebiet(Aufgabengebiet);
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
    public void setAg_ID(int Ag_ID) {
        this.Ag_ID = Ag_ID;
    }
    public void setAufgabengebiet(String Aufgabengebiet) {
        this.Aufgabengebiet = Aufgabengebiet;
    }

    public int getID() {
        return this.ID;
    };
    public int getAg_ID() {
        return this.Ag_ID;
    };
    public String getAufgabengebiet() {
        return this.Aufgabengebiet;
    };
}
