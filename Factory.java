import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;

public class Factory {
    private HashMap<String, HashSet<APrimaryKey>> objects = new HashMap<>();
    private static Factory instance;

    private Factory() {}

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public void addObject(String className, APrimaryKey obj) {
        if (!objects.containsKey(className)) {
            objects.put(className, new HashSet<>());
        }

        objects.get(className).add(obj);
    }

    public HashSet<APrimaryKey> getObject(String className) {
        return objects.get(className);
    }

    public Gremium createGremium(String Name, Boolean offiziell, Boolean inoffiziell, LocalDate Beginn, LocalDate Ende) {
        Integer id = getID(Gremium.class.toString());
        Gremium new_Gremium = new Gremium(id, Name, offiziell, inoffiziell, Beginn, Ende);
        addObject(Gremium.class.toString(), new_Gremium);
        return new_Gremium;
    }
    public Antrag createAntrag(String Titel, String Text, Antrag.Ergebnis Ergebnis, boolean Angenommen) {
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
    public Aufgabengebiet createAufgabengebiet(String Aufgabengebiet) {
        Integer id = getID(Aufgabengebiet.class.toString());
        Aufgabengebiet new_Aufgabengebiet = new Aufgabengebiet(id, Aufgabengebiet);
        addObject(Aufgabengebiet.class.toString(), new_Aufgabengebiet);
        return new_Aufgabengebiet;
    }
    public Sitzung createSitzung(Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        Integer id = getID(Sitzung.class.toString());
        Sitzung new_Sitzung = new Sitzung(id, Beginn, Ende, Einladung_am, Oeffentlich, Ort, Protokoll);
        addObject(Sitzung.class.toString(), new_Sitzung);
        return new_Sitzung;
    }

    private Integer getID(String className) {
        return objects.get(className) == null ? 1 : objects.get(className).size();
    }
}