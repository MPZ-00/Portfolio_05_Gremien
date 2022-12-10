import java.util.HashSet;
import java.util.Set;

public abstract class AHauptklasse {
    protected static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    
    private int ID;

    public void setID(int ID) {
        // Überprüfung, ob die angegebene ID bereits im Set für verwendete IDs vorhanden ist
        if (usedIDs.contains(ID)) {
            throw new IllegalArgumentException("ID(" + ID + ") ist bereits in verwendung");
        }
        
        // Wert des ID-Attributs ändern und neue ID im Set für verwendete IDs speichern
        usedIDs.add(this.ID);
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }


}
