public class Tagesordnung extends ATabellenVerwaltung {
    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private String Titel;
    private String Kurzbeschreibung;
    private String Protokolltext;

    private static Tagesordnung aktuellerTOP;

    public Tagesordnung(int ID, String Titel, String Kurzbeschreibung, String Protokolltext) {
        setID(ID);
        setTitel(Titel);
        setKurzbeschreibung(Kurzbeschreibung);
        setProtokolltext(Protokolltext);
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
}
