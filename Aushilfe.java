public class Aushilfe {
    private static Aushilfe instance = null;
    
    public Aushilfe() {}
    public static Aushilfe getInstance() {
        if (instance == null) {
            instance = new Aushilfe();
        }
        return instance;
    }

    public static Gremien Gremium_Wahl() {
        Gremien_anzeigen();
        return null;
    }
    public static void Gremien_anzeigen() {

    }

    public Sitzungen Sitzung_Wahl() {
        return null;
    }
    public void Sitzungen_anzeigen() {}
}
