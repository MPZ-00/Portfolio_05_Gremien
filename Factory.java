import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class Factory {
    private HashMap<String, HashSet<ATabellenVerwaltung>> objects = new HashMap<>();
    private static Factory instance;

    private Factory() {}

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public void addObject(String className, ATabellenVerwaltung obj) {
        if (!objects.containsKey(className)) {
            objects.put(className, new HashSet<ATabellenVerwaltung>());
        }

        objects.get(className).add(obj);
    }

    public HashSet<ATabellenVerwaltung> getObject(String className) {
        return objects.get(className);
    }

    public Gremien createGremien(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        Integer id = getID(Gremien.class.toString());
        Gremien new_Gremien = new Gremien(id, Name, offiziell, inoffiziell, Beginn, Ende);
        addObject(Gremien.class.toString(), new_Gremien);
        return new_Gremien;
    }
    public Antrag createAngtrag(String Titel, String Text, Antrag.Ergebnis Ergebnis, boolean Angenommen) {
        Integer id = getID(Antrag.class.toString());
        Antrag new_Antrag = new Antrag(id, Titel, Text, Ergebnis, Angenommen);
        addObject(Antrag.class.toString(), new_Antrag);
        return new_Antrag;
    }
    public Tagesordnung createTagesordnung(String Titel, String Kurzbeschreibung, String Protokolltext) {
        Integer id = getID(Tagesordnung.class.toString());
        Tagesordnung new_Tagesordnung = new Tagesordnung(id, Titel, Kurzbeschreibung, Protokolltext);
        addObject(Tagesordnung.class.toString(), new_Tagesordnung);
        return new_Tagesordnung;
    }
    public Aufgabengebiete createAufgabengebiete(String Aufgabengebiet) {
        Integer id = getID(Aufgabengebiete.class.toString());
        Aufgabengebiete new_Aufgabengebiete = new Aufgabengebiete(id, Aufgabengebiet);
        addObject(Aufgabengebiete.class.toString(), new_Aufgabengebiete);
        return new_Aufgabengebiete;
    }
    public Sitzungen createSitzungen(Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        Integer id = getID(Sitzungen.class.toString());
        Sitzungen new_Sitzungen = new Sitzungen(id, Beginn, Ende, Einladung_am, Oeffentlich, Ort, Protokoll);
        addObject(Sitzungen.class.toString(), new_Sitzungen);
        return new_Sitzungen;
    }

    private Integer getID(String className) {
        return objects.get(className) == null ? 1 : objects.get(className).size();
    }
}