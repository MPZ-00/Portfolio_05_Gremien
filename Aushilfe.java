import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);
        String eingabe;
        do {
            System.out.print("Welches Gremium soll es sein (Name): ");
            eingabe = scanner.nextLine();
        } while (!Gremien_enthaelt_Eingabe(eingabe));
        
        scanner.close();
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
        for (AHauptklasse object : Factory.getInstance().getObject(Gremien.class.toString())) {
            Gremien g = (Gremien)object;
            System.out.printf("ID: %d\nName: %s\noffiziell: %b\ninoffiziell: %b\nBeginn: %s\nEnde: %s\n", g.getID(), g.getName(), g.getOffiziell(), g.getInoffiziell(), g.getBeginn().toString(), g.getEnde().toString());
        }
    }

    public void Sitzung_Wahl() {
        Sitzungen_anzeigen();

        Scanner scanner = new Scanner(System.in);
        String eingabe;
        do {
            System.out.print("Welche Sitzung soll es sein (Beginn): ");
            eingabe = scanner.nextLine();
        } while (!Sitzungen_enthaelt_Eingabe(eingabe));

        scanner.close();
    }
    private boolean Sitzungen_enthaelt_Eingabe(String eingabe) {
        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen) object;
            if (s.getBeginn().toString().equalsIgnoreCase(eingabe)) {
                Sitzungen.setAktiveSitzung(s);
                return true;
            }
        }
        return false;
    }
    public void Sitzungen_anzeigen() {
        for (AHauptklasse object : Factory.getInstance().getObject(Sitzungen.class.toString())) {
            Sitzungen s = (Sitzungen)object;
            System.out.printf("ID: %d\nBeginn: %s\nEnde: %s\nEinladung_am: %s\noeffentlich: %b\nOrt: %s\nProtokoll: %s\n", s.getID(), s.getBeginn().toString(), s.getEnde().toString(), s.getEinladung_am().toString(), s.getOeffentlich(), s.getOrt(), "könnte zu lang sein");
        }
    }

    private ResultSet getRS(String sql) {
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
        try {
            init_Gremien_from_ResultSet();
            init_Antrag_from_ResultSet();
            init_Aufgabengebiete_from_ResultSet();
            init_Sitzungen_from_ResultSet();
            init_Tagesordnung_from_ResultSet();
        } catch (NullPointerException e) {
            System.out.println("Tabelle oder View nicht vorhanden");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init_Gremien_from_ResultSet() throws SQLException {
        ResultSet rs_Gremien = getRS("SELECT * FROM Gremien");
        while (rs_Gremien.next()) {
            Date beginn = rs_Gremien.getDate("Beginn");
            Date ende = rs_Gremien.getDate("Ende");

            Gremien g = new Gremien(
                rs_Gremien.getInt("ID"),
                rs_Gremien.getString("Name"),
                Boolean.valueOf(rs_Gremien.getString("offiziell")),
                Boolean.valueOf(rs_Gremien.getString("inoffiziell")),
                LocalDate.ofInstant(beginn.toInstant(), ZoneId.systemDefault()),
                LocalDate.ofInstant(ende.toInstant(), ZoneId.systemDefault())
            );

            Factory.getInstance().addObject(Gremien.class.toString(), g);
        }
    }
    private void init_Antrag_from_ResultSet() throws SQLException {
        ResultSet rs_Antrag = getRS("SELECT * FROM Antrag");
        while (rs_Antrag.next()) {
            Antrag a = new Antrag(
                rs_Antrag.getInt("ID"),
                rs_Antrag.getString("Titel"),
                rs_Antrag.getString("Text"),
                IAntrag.Ergebnis.valueOf(rs_Antrag.getString("Ergebnis").toUpperCase()),
                Boolean.valueOf(rs_Antrag.getString("Angenommen"))
            );

            Factory.getInstance().addObject(Antrag.class.toString(), a);
        }
    }
    private void init_Sitzungen_from_ResultSet() throws SQLException {
        ResultSet rs_Sitzungen = getRS("SELECT * FROM Sitzungen");
        while (rs_Sitzungen.next()) {
            Date Einladung_am = rs_Sitzungen.getDate("Einladung_am");
            Sitzungen s = new Sitzungen(
                rs_Sitzungen.getInt("ID"),
                rs_Sitzungen.getTimestamp("Beginn"),
                rs_Sitzungen.getTimestamp("Ende"),
                LocalDate.ofInstant(Einladung_am.toInstant(), ZoneId.systemDefault()),
                Boolean.valueOf(rs_Sitzungen.getString("oeffentlich")),
                rs_Sitzungen.getString("Ort"),
                rs_Sitzungen.getString("Protokoll")
            );

            Factory.getInstance().addObject(Sitzungen.class.toString(), s);
        }
    }
    private void init_Aufgabengebiete_from_ResultSet() throws SQLException {
        ResultSet rs_Aufgabengebiete = getRS("SELECT * FROM Aufgabengebiete");
        while (rs_Aufgabengebiete.next()) {
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
        while (rs_Tagesordnung.next()) {
            Tagesordnung t = new Tagesordnung(
                rs_Tagesordnung.getInt("ID"),
                rs_Tagesordnung.getString("Titel"),
                rs_Tagesordnung.getString("Kurzbeschreibung"),
                rs_Tagesordnung.getString("Protokolltext")
            );

            Factory.getInstance().addObject(Tagesordnung.class.toString(), t);
        }
    }
}
