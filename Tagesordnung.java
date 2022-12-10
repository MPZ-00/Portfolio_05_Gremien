import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTML.Tag;
public class Tagesordnung implements ITagesordnung {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    
    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private int ID;
    private String Titel;
    private String Kurzbeschreibung;
    private String Protokolltext;
    private ArrayList<Antrag> antraege;

    private int GremiumID; // Referenz auf Gremium-Objekt
    private static Tagesordnung aktuellerTOP;

    public Tagesordnung(String Titel, String Kurzbeschreibung, String Protokolltext, int GremiumID) {
        setID(nextID++);
        setTitel(Titel);
        setKurzbeschreibung(Kurzbeschreibung);
        setProtokolltext(Protokolltext);
        setGremiumID(GremiumID); // TODO: Gremium hat Sitzung top Tagesordnung

        this.antraege = new ArrayList<Antrag>();
    }
    public Tagesordnung(int ID, String Titel, String Kurzbeschreibung, String Protokolltext) {
        setID(ID);
        setTitel(Titel);
        setKurzbeschreibung(Kurzbeschreibung);
        setProtokolltext(Protokolltext);

        this.antraege = new ArrayList<Antrag>();
    }

    public static void setAktuellenTOP(Tagesordnung TOP) {
        aktuellerTOP = TOP;
    }
    public static Tagesordnung getAktuellenTOP() {
        return aktuellerTOP;
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
    public void setKurzbeschreibung(String Kurzbeschreibung) {
        this.Kurzbeschreibung = Kurzbeschreibung;
    }
    public void setProtokolltext(String Protokolltext) {
        this.Protokolltext = Protokolltext;
    }

    public int getID() {
        return this.ID;
    }
    public String getTitel() {
        return this.Titel;
    }
    public String getKurzbeschreibung() {
        return this.Kurzbeschreibung;
    }
    public String getProtokolltext() {
        return this.Protokolltext;
    }

    public void setGremiumID(int GremiumID) {
        this.GremiumID = GremiumID;
    }
    public Gremien getGremium() {
        // Durchlaufe die Liste aller Objekte von Gremien
        for (Gremien obj : Gremien.objects) {
            // Überprüfe, ob die ID des aktuellen Objekts der gespeicherten ID in Gremien entspricht
            if (obj.getID() == this.GremiumID) {
                return obj;
            }
        }
        throw new IllegalArgumentException("Kein Gremium mit ID " + this.GremiumID + " gefunden");
    }

    public void addAntrag(Antrag antrag) {
        this.antraege.add(antrag);
    }
    public ArrayList<Antrag> getAntraege() {
        return this.antraege;
    }
}
