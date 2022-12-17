import java.sql.Timestamp;
import java.time.LocalDate;

public class Sitzungen extends ATabellenVerwaltung {
    private static Sitzung aktiveSitzung;
    private static Sitzungen instance;

    public Sitzungen() {}

    public static Sitzungen getInstance() {
        if (instance == null) {
            instance = new Sitzungen();
        }
        return instance;
    }
    

    public void setAktiveSitzung(Sitzung sitzung) {
        aktiveSitzung = sitzung;
    }
    public Sitzung getAktiveSitzung() {
        return aktiveSitzung;
    }

    @Override
    public void Wahl() {
        if (Anzeigen(Gremien.getInstance().getAktuellesGremium().getID())) {
            try {
                Timestamp sitzungBeginn = Tools.getInstance().getTimestamp("Geben Sie den Beginn der Sitzung ein");
                
                if (Enthaelt_Eingabe(sitzungBeginn)) {
                    System.out.println("\033[32mOK\033[0m");
                } else {
                    System.err.println("FALSCH");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean Enthaelt_Eingabe(Timestamp beginn) {
        for (APrimaryKey object : Factory.getInstance().getObject(Sitzung.class.toString())) {
            Sitzung s = (Sitzung) object;

            System.out.println(s.getBeginn() + ".equals(" + beginn + ")"); // Debug: warum läuft der Scheiß nicht immer?
            if (s.getBeginn().equals(beginn)) {
                Sitzungen.getInstance().setAktiveSitzung(s);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean Enthaelt_Eingabe(String eingabe) {return true;}

    @Override
    public boolean Anzeigen() {
        return true;
    }

    @Override
    public boolean Anzeigen(Integer id_gremium) {
        System.out.println("[\033[35mSitzungen für Gremium (" + id_gremium + ")\033[0m]");
        
        hs_ids hs = new hs_ids(
            "select s.id " +
            "from sitzungen s " +
            "inner join hat on hat.id_sitzungen = s.id " +
            "inner join gremien g on g.id = hat.id_gremien " +
            "where g.id = " +
            Gremien.getInstance().getAktuellesGremium().getID()
        );

        if (hs.getHS().size() == 0) {
            System.err.println("Für dieses Gremium gibt es keine Sitzungen");
            return false;
        }

        for (APrimaryKey object : Factory.getInstance().getObject(Sitzung.class.toString())) {
            Sitzung s = (Sitzung)object;
            if (hs.getHS().contains(s.getID())) {
                System.out.printf("\nID: %d\nBeginn: %s\nEnde: %s\nEinladung_am: %s\noeffentlich: %b\nOrt: %s\nProtokoll: %s\n", s.getID(), s.getBeginn().toString(), s.getEnde().toString(), s.getEinladung_am().toString(), s.getOeffentlich(), s.getOrt(), s.getProtokoll());
            }
        }

        return true;
    }

    @Override
    public void Erzeugen() {
        Timestamp beginn = Tools.getInstance().getTimestamp("Gib den Beginn der Sitzung");
        Timestamp ende = Tools.getInstance().getTimestamp("Gib das Ende der Sitzung");
        LocalDate einladung_am = Tools.getInstance().getLocalDate("Gib das Datum der Einladung");
        Boolean oeffentlich = Tools.getInstance().frage_Ja_Nein("Ist die Sitzung öffentlich");
        System.out.print("Gib den Ort der Sitzung ein: ");
        String ort = Main.scanner.nextLine();
        System.out.print("Gib das Protokoll der Sitzung ein (oder null): ");
        String protokoll = Main.scanner.nextLine();
        if (protokoll.matches("null")) {
            protokoll = "";
        }

        setAktiveSitzung(Factory.getInstance().createSitzung(beginn, ende, einladung_am, oeffentlich, ort, protokoll));
        
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
            Gremien.getInstance().getAktuellesGremium().getID() + ", " +
            getAktiveSitzung().getID() + ")"
        );

        ConnectionManager.getInstance().executeStatement("commit");
    }
}
