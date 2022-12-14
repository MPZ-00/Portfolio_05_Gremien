import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashSet;

public class Aushilfe implements IAushilfe {
    private static Aushilfe instance = null;
    
    public Aushilfe() {}
    public static Aushilfe getInstance() {
        if (instance == null) {
            instance = new Aushilfe();
        }
        return instance;
    }

    public void Gremium_Wahl() {
        Gremien_anzeigen();
        String eingabe;
        Main.scanner.nextLine(); // Eingabepuffer löschen
        do {
            System.out.print("\nWelches Gremium soll es sein (Name oder 'neu'): ");
            eingabe = Main.scanner.nextLine();
        } while (!Gremien_enthaelt_Eingabe(eingabe) && !eingabe.equalsIgnoreCase("neu"));
        
        if (eingabe.equalsIgnoreCase("neu")) {
            Gremium_erzeugen();
        }
    }
    private void Gremium_erzeugen() {
        System.out.print("Bezeichnung des Gremiums: ");
        String name = Main.scanner.nextLine();
        Boolean offiziell = frage_Ja_Nein("Ist das Gremium offiziell");
        LocalDate beginn = getLocalDate("Beginn des Gremiums");
        LocalDate ende = getLocalDate("Ende des Gremiums");

        Gremien.setAktuellesGremium(Factory.getInstance().createGremien(name, offiziell, !offiziell, beginn, ende));
    }
    private boolean Gremien_enthaelt_Eingabe(String eingabe) {
        for (AHauptklasse object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien) object;
            if (g.getName().equalsIgnoreCase(eingabe)) {
                Gremien.setAktuellesGremium(g);
                return true;
            }
        }
        return false;
    }
    public void Gremien_anzeigen() {
        System.out.println("[Gremien]");
        for (AHauptklasse object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien)object;
            System.out.printf("\nID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
        }
    }

    public void Sitzung_Wahl() {
        if (Sitzungen_anzeigen(Gremien.getAktuellesGremium().getID())) {
            try {
                Timestamp sitzungBeginn = getTimestamp("Geben Sie den Beginn der Sitzung ein");
                
                if (Sitzungen_enthaelt_Eingabe(sitzungBeginn)) {
                    System.out.println("OK");
                } else {
                    System.err.println("FALSCH");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private boolean Sitzungen_enthaelt_Eingabe(Timestamp beginn) {
        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen) object;
            
            if (s.getBeginn().equals(beginn)) {
                Sitzungen.setAktiveSitzung(s);
                return true;
            }
        }
        return false;
    }
    public boolean Sitzungen_anzeigen(Integer id) {
        System.out.println("[Sitzungen]");
        HashSet<Integer> s_ids = new HashSet<>();

        ResultSet rs = getRS(
            "select s.id " +
            "from sitzungen s " +
            "inner join hat on hat.id_sitzungen = s.id " +
            "inner join gremien g on g.id = hat.id_gremien where g.id = " +
            Gremien.getAktuellesGremium().getID()
        );
        try {
            while (rs.next()) {
                s_ids.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (s_ids.size() == 0) {
            System.err.println("Für dieses Gremium gibt es keine Sitzungen");
            return false;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen)object;
            if (s_ids.contains(s.getID())) {
                System.out.printf("\nID: %d\nBeginn: %s\nEnde: %s\nEinladung_am: %s\noeffentlich: %b\nOrt: %s\nProtokoll: %s\n", s.getID(), s.getBeginn().toString(), s.getEnde().toString(), s.getEinladung_am().toString(), s.getOeffentlich(), s.getOrt(), s.getProtokoll());
            }
        }

        return true;
    }

    public ResultSet getRS(String sql) {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void interne_DB_initialisieren() {
        System.out.println("Interne DB wird initialisiert");
        try {
            init_Gremien_from_ResultSet();
            init_Antrag_from_ResultSet();
            init_Aufgabengebiete_from_ResultSet();
            init_Sitzungen_from_ResultSet();
            init_Tagesordnung_from_ResultSet();
        } catch (Exception e) {
            System.err.println("Interne DB konnte nicht initialisert werden,");
            e.printStackTrace();
        }
        System.out.println("Interne DB initialisiert.");
    }

    private void init_Gremien_from_ResultSet() throws Exception {
        ResultSet rs_Gremien = getRS("SELECT * FROM Gremien");
        while (rs_Gremien != null && rs_Gremien.next()) {
            java.sql.Date beginn = rs_Gremien.getDate("Beginn");
            java.sql.Date ende = rs_Gremien.getDate("Ende");

            Gremien g = new Gremien(
                    rs_Gremien.getInt("ID"),
                    rs_Gremien.getString("Name"),
                    rs_Gremien.getString("offiziell").matches("(?i)1|t|y"),
                    rs_Gremien.getString("inoffiziell").matches("(?i)1|t|y"),
                    beginn.toLocalDate(),
                    ende.toLocalDate()
            );

            Factory.getInstance().addObject(Gremien.class.toString(), g);
        }
    }
    private void init_Antrag_from_ResultSet() throws Exception {
        ResultSet rs_Antrag = getRS("SELECT * FROM Antrag");
        while (rs_Antrag != null && rs_Antrag.next()) {
            Antrag a = new Antrag(
                rs_Antrag.getInt("ID"),
                rs_Antrag.getString("Titel"),
                rs_Antrag.getString("Text"),
                IAntrag.Ergebnis.valueOf(rs_Antrag.getString("Ergebnis").toUpperCase()),
                Boolean.parseBoolean(rs_Antrag.getString("Angenommen"))
            );

            Factory.getInstance().addObject(Antrag.class.toString(), a);
        }
    }
    private void init_Sitzungen_from_ResultSet() throws SQLException {
        ResultSet rs_Sitzungen = getRS("SELECT * FROM Sitzungen");
        while (rs_Sitzungen != null && rs_Sitzungen.next()) {
            java.sql.Date Einladung_am = rs_Sitzungen.getDate("Einladung_am");
            Sitzungen s = new Sitzungen(
                rs_Sitzungen.getInt("ID"),
                rs_Sitzungen.getTimestamp("Beginn"),
                rs_Sitzungen.getTimestamp("Ende"),
                Einladung_am.toLocalDate(),
                Boolean.valueOf(rs_Sitzungen.getString("oeffentlich")),
                rs_Sitzungen.getString("Ort"),
                rs_Sitzungen.getString("Protokoll")
            );

            Factory.getInstance().addObject(Sitzungen.class.toString(), s);
        }
    }
    private void init_Aufgabengebiete_from_ResultSet() throws SQLException {
        ResultSet rs_Aufgabengebiete = getRS("SELECT * FROM Aufgabengebiete");
        while (rs_Aufgabengebiete != null && rs_Aufgabengebiete.next()) {
            Aufgabengebiete au = new Aufgabengebiete(
                rs_Aufgabengebiete.getInt("ID"),
                rs_Aufgabengebiete.getInt("Ag_ID"),
                rs_Aufgabengebiete.getString("Aufgabengebiet")
            );

            Factory.getInstance().addObject(Aufgabengebiete.class.toString(), au);
        }
    }
    private void init_Tagesordnung_from_ResultSet() throws SQLException {
        ResultSet rs_Tagesordnung = getRS("SELECT * FROM Tagesordnung");
        while (rs_Tagesordnung != null && rs_Tagesordnung.next()) {
            Tagesordnung t = new Tagesordnung(
                rs_Tagesordnung.getInt("ID"),
                rs_Tagesordnung.getString("Titel"),
                rs_Tagesordnung.getString("Kurzbeschreibung"),
                rs_Tagesordnung.getString("Protokolltext")
            );

            Factory.getInstance().addObject(Tagesordnung.class.toString(), t);
        }
    }

    public boolean frage_Ja_Nein(String frage) {
        String input;
        do {
            System.out.println(frage + " (ja/nein): ");
            input = Main.scanner.nextLine();
        } while (!input.equalsIgnoreCase("ja") && !input.equalsIgnoreCase("nein"));

        return (input.equalsIgnoreCase("ja"));
    }

    public Timestamp getTimestamp(String text) {
        String template = "yyyy-MM-dd HH:mm:ss";
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        // Eingabeaufforderung ausgeben
        System.out.printf("%s (%s) ein: ", text, template);
        
        try {
            String input = Main.scanner.nextLine();
            if (!input.matches(regex)) {
                throw new IllegalArgumentException();
            }

            // Konvertiere den Eingabe-String in ein LocalDateTime-Objekt
            LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern(template));

            // Konvertiere das LocalDateTime-Objekt in ein Timestamp-Objekt
            return Timestamp.from(dateTime.toInstant(ZoneOffset.of("+1")));
        } catch (IllegalArgumentException e) {
            // Fehlermeldung ausgeben und erneut nach Timestamp fragen
            System.err.println("Ungültiges Datumsformat. Bitte versuchen Sie es erneut.");
            return getTimestamp(text);
        }
    }

    public LocalDate getLocalDate(String text) {
        int yy = 1964, mm = 5, dd = 14;
        try {
            System.out.printf("%s (%s) ein: ", text, "Jahr");
            while (!Main.scanner.hasNextInt()) {
                yy = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();
            
            System.out.printf("%s (%s) ein: ", text, "Monat");
            while (!Main.scanner.hasNextInt()) {
                mm = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();

            System.out.printf("%s (%s) ein: ", text, "Tag");
            while (!Main.scanner.hasNextInt()) {
                dd = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();
            
            return LocalDate.of(yy, mm, dd);
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Eingabe. Bitte nur Zahlen eingeben.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Ungültiges Datumsformat");
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValidDateFormat(String input, String regex) {
        return input.matches(regex);
    }

    public boolean Aufgabe1() {
        Aushilfe.getInstance().Gremium_Wahl();
        System.out.println("Ausgewähltes Gremium (ID/Name): " + Gremien.getAktuellesGremium().getID() + "/" + Gremien.getAktuellesGremium().getName());
        
        try {
            Aushilfe.getInstance().Sitzung_Wahl();
            System.out.println("Ausgewählte Sitzung (ID/Beginn): " + Sitzungen.getAktiveSitzung().getID() + "/" + Sitzungen.getAktiveSitzung().getBeginn());
        } catch (NullPointerException e) {
            if (Aushilfe.getInstance().frage_Ja_Nein("Jetzt neue Sitzung für dieses Gremium anlegen")) {
                Sitzung_erzeugen();
            } else {
                System.err.println("Keine Sitzungen für dieses Gremium verfügbar, wähle ein anderes Gremium aus\n");
                return Aufgabe1();
            }
        }
        return true;
    }
    public boolean Aufgabe2() {
        System.out.println("[Tagesordnung]");
        
        hs_ids top = new hs_ids(
            "select t.id " +
            "from sitzungen s " +
            "inner join top on top.id_sitzung = s.id " +
            "inner join tagesordnung t on t.id = top.id_tagesordnung " +
            "where s.id = " +
            Sitzungen.getAktiveSitzung().getID()
        );

        if (top.getHS().size() == 0) {
            System.err.println("Für diese Sitzung gibt es keine Tagesordnungen");
            return false;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (top.getHS().contains(t.getID())) {
                System.out.printf("\nID: %d\nTitel: %s\nKurzbeschreibung: %s\nProtokolltext: %s", t.getID(), t.getTitel(), t.getKurzbeschreibung(), t.getProtokolltext());
                for (AHauptklasse obj : Factory.getInstance().getObject(Antrag.class.toString())) {
                    Antrag a = (Antrag)obj;

                    hs_ids antraege = new hs_ids(
                        "select a.id " +
                        "from tagesordnung t " +
                        "inner join gehoert_zu on gehoert_zu.id_top = t.id " +
                        "inner join antrag a on a.id = gehoert_zu.id_antrag " +
                        "where t.id = " + t.getID()
                    );
                    if (antraege.getHS().size() > 0 && antraege.getHS().contains(a.getID())) {
                        System.out.printf("\nID: %d\nTitel: %s\nText: %s\nErgebnis: %s\nAngenommen: %b", a.getID(), a.getTitel(), a.getText(), a.getErgebnis().toString(), a.isAngenommen());
                    }
                }
            }
        }
        return true;
    }

    public boolean Aufgabe3() {
        // Auswahl eines TOPs oder eines Antrags einer Sitzung
        System.out.println("\n[Tagesordnung|Antrag]");
        
        Tagesordnung_Wahl();
        Antrag_Wahl();
        
        return true;
    }

    private void Sitzung_erzeugen() {
        Timestamp beginn = getTimestamp("Gib den Beginn der Sitzung");
        Timestamp ende = getTimestamp("Gib das Ende der Sitzung");
        LocalDate einladung_am = getLocalDate("Gib das Datum der Einladung");
        Boolean oeffentlich = frage_Ja_Nein("Ist die Sitzung öffentlich");
        System.out.print("Gib den Ort der Sitzung ein: ");
        String ort = Main.scanner.nextLine();
        System.out.print("Gib das Protokoll der Sitzung ein (oder null): ");
        String protokoll = Main.scanner.nextLine();
        if (protokoll.matches("null")) {
            protokoll = "";
        }

        Sitzungen.setAktiveSitzung(Factory.getInstance().createSitzungen(beginn, ende, einladung_am, oeffentlich, ort, protokoll));
    }

    public void Tagesordnung_Wahl() {
        if (Tagesordnung_anzeigen()) {
            String eingabe;
            do {
                System.out.print("\nWelche Tagesordnung soll es ein (Titel oder 'neu'): ");
                eingabe = Main.scanner.nextLine();
            } while (!TOP_mit_Titel(eingabe) && !eingabe.equalsIgnoreCase("neu"));

            if (eingabe.equalsIgnoreCase("neu")) {
                Tagesordnung_erzeugen();
            }
        } else {
            if (frage_Ja_Nein("Neue Tagesordnung erstellen")) {
                Tagesordnung_erzeugen();
            }
        }
    }
    private boolean TOP_mit_Titel(String eingabe) {
        for (AHauptklasse object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (t.getTitel().equalsIgnoreCase(eingabe)) {
                Tagesordnung.setAktuellenTOP(t);
                return true;
            }
        }
        return false;
    }
    private void Tagesordnung_erzeugen() {
        System.out.print("TOP Titel eingeben: ");
        String titel = Main.scanner.nextLine();
        System.out.print("TOP Kurzbeschreibung eingeben: ");
        String kurzbeschreibung = Main.scanner.nextLine();
        System.out.print("TOP Protokolltext eingeben (oder null): ");
        String protokolltext = Main.scanner.nextLine();
        if (protokolltext == "null") {
            protokolltext = "";
        }

        Tagesordnung.setAktuellenTOP(Factory.getInstance().createTagesordnung(titel, kurzbeschreibung, protokolltext));
    }
    public boolean Tagesordnung_anzeigen() {
        System.out.println("[Tagesordnung]");

        hs_ids hs = new hs_ids(
            "select t.id " +
            "from tagesordnung t " +
            "inner join top on top.id_tagesordnung = t.id " +
            "inner join sitzungen s on s.id = top.id_sitzung " +
            "where s.id = " +
            Sitzungen.getAktiveSitzung().getID()
        );

        if (hs.getHS().size() == 0) {
            System.err.println("Für diese Sitzung gibt es keine Tagesordnungen");
            return false;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (hs.getHS().contains(t.getID())) {
                System.out.printf("\nID: %d\nTitel: %s\nKurzbeschreibung: %s\nProtokolltext: %s", t.getID(), t.getTitel(), t.getKurzbeschreibung(), t.getProtokolltext());
            }
        }
        return true;
    }

    public void Antrag_Wahl() {
        if (Antrag_anzeigen()) {
            String eingabe;
            do {
                System.out.print("\nWelcher Antrag soll es sein (Titel oder 'neu'): ");
                eingabe = Main.scanner.nextLine();
            } while (!Antrag_mit_Titel(eingabe) && !eingabe.equalsIgnoreCase("neu"));
        } else if (frage_Ja_Nein("Neuen Antrag erstellen")) {
            Antrag_erzeugen();
        }
    }
    private boolean Antrag_mit_Titel(String eingabe) {
        for (AHauptklasse object : Factory.getInstance().getObject(Antrag.class.toString())) {
            Antrag a = (Antrag)object;
            if (a.getTitel().equalsIgnoreCase(eingabe)) {
                Antrag.setAktuellenAntrag(a);
                return true;
            }
        }
        return false;
    }
    private void Antrag_erzeugen() {
        System.out.print("Antrag Titel eingeben: ");
        String titel = Main.scanner.nextLine();
        System.out.print("Antrag Text eingeben: ");
        String text = Main.scanner.nextLine();
        EnumSet<IAntrag.Ergebnis> ergebnisse = EnumSet.allOf(IAntrag.Ergebnis.class);
        String input;
        do {
            System.out.print("Antrag Ergebnis eingeben (" + ergebnisse.spliterator() + "): ");
            input = Main.scanner.nextLine().toUpperCase();
        } while (!ergebnisse.contains(IAntrag.Ergebnis.valueOf(input)));
        IAntrag.Ergebnis ergebnis = IAntrag.Ergebnis.valueOf(input);
        boolean angenommen = frage_Ja_Nein("Antrag angenommen");

        Antrag.setAktuellenAntrag(Factory.getInstance().createAngtrag(titel, text, ergebnis, angenommen));

        ConnectionManager.getInstance().executeStatement("insert into antrag values (" +
                Antrag.getAktuellenAngtrag().getID() + ", " +
                Antrag.getAktuellenAngtrag().getTitel() + ", " +
                Antrag.getAktuellenAngtrag().getText() + ", " +
                Antrag.getAktuellenAngtrag().getErgebnis().toString() + ", " +
                (Antrag.getAktuellenAngtrag().isAngenommen() == true ? "1" : "0") +
                ")");
        ConnectionManager.getInstance().executeStatement(
                "insert into gehoert_zu values (" +
                    Antrag.getAktuellenAngtrag().getID() + ", " +
                    Tagesordnung.getAktuellenTOP().getID() + ")");
        ConnectionManager.getInstance().executeStatement("commit");
    }
    public boolean Antrag_anzeigen() {
        System.out.println("[Antrag]");

        hs_ids hs = new hs_ids(
            "select a.id " +
            "from antrag a " +
            "inner join gehoert_zu on gehoert_zu.id_antrag = a.id " +
            "inner join tagesordnung t on t.id = gehoert_zu.id_top " +
            "where t.id = " +
            Tagesordnung.getAktuellenTOP().getID()
        );

        if (hs.getHS().size() == 0) {
            System.err.println("Für diese Tagesordnung gibt es keine Anträge");
            return false;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Antrag.class.toString())) {
            Antrag a = (Antrag)object;
            if (hs.getHS().contains(a.getID())) {
                System.out.printf("\nID: %d\nTitel: %s\nText: %s\nErgebnis: %s\nAngenommen: %b", a.getID(), a.getTitel(), a.getText(), a.getErgebnis().toString(), a.isAngenommen());
            }
        }
        return true;
    }
}
