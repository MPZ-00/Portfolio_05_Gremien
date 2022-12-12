import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class Factory {
    private HashMap<String, HashSet<Integer>> objects = new HashMap<>();
    private static Factory instance;

    private Factory() {}

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public void addID(String className, Integer id) {
        if (!objects.containsKey(className)) {
            objects.put(className, new HashSet<Integer>());
        }

        objects.get(className).add(id);
    }

    public Gremien createGremien(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        Integer id = getGremienID();
        Gremien new_Gremien = new Gremien(id, Name, offiziell, inoffiziell, Beginn, Ende);
        addID(Gremien.class.toString(), id);
        return new_Gremien;
    }
    public Antrag createAngtrag(String Titel, String Text, IAntrag.Ergebnis Ergebnis, boolean Angenommen) {
        Integer id = getAntragID();
        addID(Antrag.class.toString(), id);
        Antrag new_Antrag = new Antrag(id, Titel, Text, Ergebnis, Angenommen);
        return new_Antrag;
    }
    public Tagesordnung createTagesordnung(String Titel, String Kurzbeschreibung, String Protokolltext) {
        Integer id = getTagesordnungID();
        addID(Tagesordnung.class.toString(), id);
        Tagesordnung new_Tagesordnung = new Tagesordnung(id, Titel, Kurzbeschreibung, Protokolltext);
        return new_Tagesordnung;
    }
    public Aufgabengebiete createAufgabengebiete(int Ag_ID, String Aufgabengebiet) {
        Integer id = getAufgabengebieteID();
        addID(Aufgabengebiete.class.toString(), id);
        Aufgabengebiete new_Aufgabengebiete = new Aufgabengebiete(id, Ag_ID, Aufgabengebiet);
        return new_Aufgabengebiete;
    }
    public Sitzungen createSitzungen(Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        Integer id = getSitzungenID();
        addID(Sitzungen.class.toString(), id);
        Sitzungen new_Sitzungen = new Sitzungen(getSitzungenID(), Beginn, Ende, Einladung_am, Oeffentlich, Ort, Protokoll);
        return new_Sitzungen;
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