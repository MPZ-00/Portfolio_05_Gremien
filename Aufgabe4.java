import javax.swing.text.html.HTML.Tag;

public class Aufgabe4 {
    private static Aufgabe4 instance = null;

    public Aufgabe4() {}

    public static Aufgabe4 getInstance() {
        if (instance == null) {
            instance = new Aufgabe4();
        }
        return instance;
    }

    public boolean is_any_Protokolltext_null() {
        boolean is_Pt_null = false;
        for (AHauptklasse object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung) object;
            if (t.getProtokolltext() == null || t.getProtokolltext().equalsIgnoreCase("null")) {
                is_Pt_null = true;
            }
        }
        return is_Pt_null;
    }
}
