import java.sql.Timestamp;
import java.time.LocalDate;

public class Sitzungen extends AHauptklasse implements ISitzungen {
    private static Sitzungen aktiveSitzung;

    // Attribute, die den Spalten der Tabelle Sitzungen entprechen
    private Timestamp Beginn;
    private Timestamp Ende;
    private LocalDate Einladung_am;
    private boolean Oeffentlich;
    private String Ort;
    private String Protokoll;

    // Konstruktor-Methode, die die Attriubute initialisiert
    public Sitzungen(Integer ID, Timestamp Beginn, Timestamp Ende, LocalDate Einladung_am, Boolean Oeffentlich, String Ort, String Protokoll) {
        setID(ID);
        setBeginn(Beginn);
        setEnde(Ende);
        setEinladung_am(Einladung_am);
        setOeffentlich(Oeffentlich);
        setOrt(Ort);
        setProtokoll(Protokoll);
    }

    public static void setAktiveSitzung(Sitzungen sitzung) {
        aktiveSitzung = sitzung;
    }
    public static Sitzungen getAktiveSitzung() {
        return aktiveSitzung;
    }

    public Timestamp getBeginn() {
        return this.Beginn;
    }
    public Timestamp getEnde() {
        return this.Ende;
    }
    public LocalDate getEinladung_am() {
        return this.Einladung_am;
    }
    public Boolean getOeffentlich() {
        return this.Oeffentlich;
    }
    public String getOrt() {
        return this.Ort;
    }
    public String getProtokoll() {
        return this.Protokoll;
    }

    public void setBeginn(Timestamp Beginn) {
        this.Beginn = Beginn;
    }
    public void setEnde(Timestamp Ende) {
        this.Ende = Ende;
    }
    public void setEinladung_am(LocalDate Einladung_am) {
        this.Einladung_am = Einladung_am;
    }
    public void setOeffentlich(Boolean Oeffentlich) {
        this.Oeffentlich = Oeffentlich;
    }
    public void setOrt(String Ort) {
        this.Ort = Ort;
    }
    public void setProtokoll(String Protokoll) {
        this.Protokoll = Protokoll;
    }
}
