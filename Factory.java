import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class Factory {
    private HashMap<String, HashSet<Integer>> objects;
    private static Factory instance;

    private Factory() {}
    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public Gremien createGremien(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        return new Gremien(getGremienID(), Name, offiziell, inoffiziell, Beginn, Ende);
    }
    public Antrag createAngtrag(String Titel, String Text, IAntrag.Ergebnis Ergebnis, boolean Angenommen) {
        return new Antrag(getAntragID(), Titel, Text, Ergebnis, Angenommen);
    }
    public Tagesordnung createTagesordnung(String Titel, String Kurzbeschreibung, String Protokolltext) {
        return new Tagesordnung(getTagesordnungID(), Titel, Kurzbeschreibung, Protokolltext);
    }
    public Aufgabengebiete createAufgabengebiete(int Ag_ID, String Aufgabengebiet) {
        return new Aufgabengebiete(getAufgabengebieteID(), Ag_ID, Aufgabengebiet);
    }
    public Sitzungen createSitzungen(Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        return new Sitzungen(getSitzungenID(), Beginn, Ende, Einladung_am, Oeffentlich, Ort, Protokoll);
    }
    
    private Integer getGremienID() {
        return objects.get(Gremien.class.getName()).size();
    }
    private Integer getAntragID() {
        return objects.get(Antrag.class.getName()).size();
    }
    private Integer getTagesordnungID() {
        return objects.get(Tagesordnung.class.getName()).size();
    }
    private Integer getAufgabengebieteID() {
        return objects.get(Aufgabengebiete.class.getName()).size();
    }
    private Integer getSitzungenID() {
        return objects.get(Sitzungen.class.getName()).size();
    }
}