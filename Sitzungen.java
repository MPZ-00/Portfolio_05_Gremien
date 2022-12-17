import java.sql.Timestamp;
import java.time.LocalDate;

public class Sitzungen extends ATabellenVerwaltung {
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

    @Override
    public void Wahl() {
        if (Anzeigen(Gremien.getAktuellesGremium().getID())) {
            try {
                Timestamp sitzungBeginn = Aushilfe.getInstance().getTimestamp("Geben Sie den Beginn der Sitzung ein");
                
                if (Sitzungen_enthaelt_Eingabe(sitzungBeginn)) {
                    System.out.println("\033[32mOK\033[0m");
                } else {
                    System.err.println("FALSCH");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private boolean Sitzungen_enthaelt_Eingabe(Timestamp beginn) {
        for (ATabellenVerwaltung object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen) object;

            System.out.println(s.getBeginn() + ".equals(" + beginn + ")"); // Debug: warum läuft der Scheiß nicht immer?
            if (s.getBeginn().equals(beginn)) {
                Sitzungen.setAktiveSitzung(s);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean Anzeigen() {
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id_gremium) {
        System.out.println("\033[35m[Sitzungen für Gremium (" + id_gremium + ")\033[0m]");
        
        hs_ids hs = new hs_ids(
            "select s.id " +
            "from sitzungen s " +
            "inner join hat on hat.id_sitzungen = s.id " +
            "inner join gremien g on g.id = hat.id_gremien " +
            "where g.id = " +
            Gremien.getAktuellesGremium().getID()
        );

        if (hs.getHS().size() == 0) {
            System.err.println("Für dieses Gremium gibt es keine Sitzungen");
            return false;
        }

        for (ATabellenVerwaltung object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen)object;
            if (hs.getHS().contains(s.getID())) {
                System.out.printf("\nID: %d\nBeginn: %s\nEnde: %s\nEinladung_am: %s\noeffentlich: %b\nOrt: %s\nProtokoll: %s\n", s.getID(), s.getBeginn().toString(), s.getEnde().toString(), s.getEinladung_am().toString(), s.getOeffentlich(), s.getOrt(), s.getProtokoll());
            }
        }

        return true;
    }

    @Override
    public void Erzeugen() {
        Timestamp beginn = Aushilfe.getInstance().getTimestamp("Gib den Beginn der Sitzung");
        Timestamp ende = Aushilfe.getInstance().getTimestamp("Gib das Ende der Sitzung");
        LocalDate einladung_am = Aushilfe.getInstance().getLocalDate("Gib das Datum der Einladung");
        Boolean oeffentlich = Aushilfe.getInstance().frage_Ja_Nein("Ist die Sitzung öffentlich");
        System.out.print("Gib den Ort der Sitzung ein: ");
        String ort = Main.scanner.nextLine();
        System.out.print("Gib das Protokoll der Sitzung ein (oder null): ");
        String protokoll = Main.scanner.nextLine();
        if (protokoll.matches("null")) {
            protokoll = "";
        }

        setAktiveSitzung(Factory.getInstance().createSitzungen(beginn, ende, einladung_am, oeffentlich, ort, protokoll));
        
        ConnectionManager.getInstance().executeStatement(
            "insert into sitzungen values (" +
            getAktiveSitzung().getID() + ", " +
            getAktiveSitzung().getBeginn() + ", " +
            getAktiveSitzung().getEnde() + ", " +
            getAktiveSitzung().getEinladung_am() + ", " +
            (getAktiveSitzung().getOeffentlich() ? "1" : "0") + ", " +
            getAktiveSitzung().getOrt() + ", " +
            getAktiveSitzung().getProtokoll() + ")"
        );

        ConnectionManager.getInstance().executeStatement(
            "insert into hat values (" +
            Gremien.getAktuellesGremium().getID() + ", " +
            getAktiveSitzung().getID() + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }
}
