import java.util.HashSet;
import java.util.Set;

public class HelperClass {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs

    public static void setID(int ID) {
        // Überprüfung, ob die angegebene ID bereits im Set für verwendete IDs vorhanden ist
        if (usedIDs.contains(ID)) {
            throw new IllegalArgumentException("ID ist bereits in verwendung");
        }

        // Wert des ID-Attributs ändern und neue ID im Set für verwendete IDs speichern
        usedIDs.add(ID);
        // this.ID = ID; // this-Referenz ist in statischen Methoden nicht zulässig
    }

    public static int getNextID() { // statische Methode, um die nächste verfügbare ID zu erhalten und zu inkrementieren
        return nextID++;
    }
}
