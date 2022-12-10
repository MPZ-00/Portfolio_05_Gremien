import java.util.HashSet;
import java.util.Set;

public class Antrag implements IAntrag {
    public enum Ergebnis {
        JA, NEIN, ENTHALTUNG
    }
    private int ID;
    private String Titel;
    private String Text;
    private IAntrag.Ergebnis Ergebnis;
    private boolean Angenommen;

    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    
    public Antrag(String Titel, String Text, IAntrag.Ergebnis Ergebnis, boolean Angenommen) {
        setID(nextID++);
        setTitel(Titel);
        setText(Text);
        setErgebnis(Ergebnis);
        setAngenommen(Angenommen);
    }
    public Antrag(int ID, String Titel, String Text, IAntrag.Ergebnis Ergebnis, boolean Angenommen) {
        setID(ID);
        setTitel(Titel);
        setText(Text);
        setErgebnis(Ergebnis);
        setAngenommen(Angenommen);
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
    public void setTitel(String Titel) {
        this.Titel = Titel;
    }
    public void setText(String Text) {
        this.Text = Text;
    }
    public void setAngenommen(Boolean Angenommen) {
        this.Angenommen = Angenommen;
    }

    public String getTitel() {
        return this.Titel;
    }
    public String getText() {
        return this.Text;
    }
    public IAntrag.Ergebnis getErgebnis() {
        return this.Ergebnis;
    }
    public Boolean isAngenommen() {
        return this.Angenommen;
    }

    @Override
    public void setErgebnis(IAntrag.Ergebnis Ergebnis) {
        this.Ergebnis = Ergebnis;
    }
}