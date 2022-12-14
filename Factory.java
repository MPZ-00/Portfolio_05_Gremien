import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class Factory {
    private HashMap<String, HashSet<AHauptklasse>> objects = new HashMap<>();
    private static Factory instance;

    private Factory() {}

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public void addObject(String className, AHauptklasse obj) {
        if (!objects.containsKey(className)) {
            objects.put(className, new HashSet<AHauptklasse>());
        }

        objects.get(className).add(obj);
    }

    public HashSet<AHauptklasse> getObject(String className) {
        return objects.get(className);
    }

    public Gremien createGremien(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        Integer id = getGremienID();
        Gremien new_Gremien = new Gremien(id, Name, offiziell, inoffiziell, Beginn, Ende);
        addObject(Gremien.class.toString(), new_Gremien);
        return new_Gremien;
    }
    public Antrag createAngtrag(String Titel, String Text, IAntrag.Ergebnis Ergebnis, boolean Angenommen) {
        Integer id = getAntragID();
        Antrag new_Antrag = new Antrag(id, Titel, Text, Ergebnis, Angenommen);
        addObject(Antrag.class.toString(), new_Antrag);
        return new_Antrag;
    }
    public Tagesordnung createTagesordnung(String Titel, String Kurzbeschreibung, String Protokolltext) {
        Integer id = getTagesordnungID();
        Tagesordnung new_Tagesordnung = new Tagesordnung(id, Titel, Kurzbeschreibung, Protokolltext);
        addObject(Tagesordnung.class.toString(), new_Tagesordnung);
        return new_Tagesordnung;
    }
    public Aufgabengebiete createAufgabengebiete(int Ag_ID, String Aufgabengebiet) {
        Integer id = getAufgabengebieteID();
        Aufgabengebiete new_Aufgabengebiete = new Aufgabengebiete(id, Ag_ID, Aufgabengebiet);
        addObject(Aufgabengebiete.class.toString(), new_Aufgabengebiete);
        return new_Aufgabengebiete;
    }
    public Sitzungen createSitzungen(Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        Integer id = getSitzungenID();
        Sitzungen new_Sitzungen = new Sitzungen(id, Beginn, Ende, Einladung_am, Oeffentlich, Ort, Protokoll);
        addObject(Sitzungen.class.toString(), new_Sitzungen);
        return new_Sitzungen;
    }
    
    private Integer getGremienID() {
        return objects.get(Gremien.class.getName()) == null ? 1 : objects.get(Gremien.class.getName()).size();
    }
    private Integer getAntragID() {
        return objects.get(Antrag.class.getName()) == null ? 1 : objects.get(Antrag.class.getName()).size();
    }
    private Integer getTagesordnungID() {
        return objects.get(Tagesordnung.class.getName()) == null ? 1 : objects.get(Tagesordnung.class.getName()).size();
    }
    private Integer getAufgabengebieteID() {
        return objects.get(Aufgabengebiete.class.getName()) == null ? 1 : objects.get(Aufgabengebiete.class.getName()).size();
    }
    private Integer getSitzungenID() {
        return objects.get(Sitzungen.class.getName()) == null ? 1 : objects.get(Sitzungen.class.getName()).size();
    }
}