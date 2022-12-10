import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Sitzungen implements ISitzungen {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    
    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private int ID;
    private Timestamp Beginn;
    private Timestamp Ende;
    private LocalDate Einladung_am;
    private boolean Oeffentlich;
    private String Ort;
    private String Protokoll;

    // Konstruktor-Methode, die die Attriubute initialisiert
    public Sitzungen(Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        setID(nextID++);
        setBeginn(Beginn);
        setEnde(Ende);
        setEinladung_am(Einladung_am);
        setOeffentlich(Oeffentlich);
        setOrt(Ort);
        setProtokoll(Protokoll);
    }

    public int getID() {
        return this.ID;
    }
    public Timestamp getBeginn() {
        return this.Beginn;
    }
    public Timestamp getEnde() {
        return this.Ende;
    }
    public LocalDate getEinladung_am() {
        return this.Einladung_am;
    }
    public Boolean getOeffentlich() {
        return this.Oeffentlich;
    }
    public String getOrt() {
        return this.Ort;
    }
    public String getProtokoll() {
        return this.Protokoll;
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
    public void setBeginn(Timestamp Beginn) {
        this.Beginn = Beginn;
    }
    public void setEnde(Timestamp Ende) {
        this.Ende = Ende;
    }
    public void setEinladung_am(LocalDate Einladung_am) {
        this.Einladung_am = Einladung_am;
    }
    public void setOeffentlich(Boolean Oeffentlich) {
        this.Oeffentlich = Oeffentlich;
    }
    public void setOrt(String Ort) {
        this.Ort = Ort;
    }
    public void setProtokoll(String Protokoll) {
        this.Protokoll = Protokoll;
    }
}
