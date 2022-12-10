import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tagesordnung extends AHauptklasse implements ITagesordnung {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    
    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
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

    public void setTitel(String Titel) {
        this.Titel = Titel;
    }
    public void setKurzbeschreibung(String Kurzbeschreibung) {
        this.Kurzbeschreibung = Kurzbeschreibung;
    }
    public void setProtokolltext(String Protokolltext) {
        this.Protokolltext = Protokolltext;
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
        if (!Gremien.objects.containsKey(this.GremiumID)) {
            throw new IllegalArgumentException("Kein Gremium mit ID " + this.GremiumID + " gefunden");
        }
        return Gremien.objects.get(this.GremiumID); // O(1)
    }

    public void addAntrag(Antrag antrag) {
        this.antraege.add(antrag);
    }
    public ArrayList<Antrag> getAntraege() {
        return this.antraege;
    }
}
