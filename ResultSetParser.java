import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetParser implements IResultSetParser {
    public List<Aufgabengebiete> getAufgabengebieteFromResultSet(ResultSet rs) throws SQLException {
        List<Aufgabengebiete> aufgabengebiete = new ArrayList<Aufgabengebiete>();

        while (rs.next()) {
            int ID = rs.getInt("ID");
            int Ag_ID = rs.getInt("Ag_ID");
            String aufgabengebiet = rs.getString("Aufgabengebiet");

            // Erstelle ein neues Aufgabengebiet-Objekt und füge es der Liste hinz
            aufgabengebiete.add(new Aufgabengebiete(ID, Ag_ID, aufgabengebiet));
        }

        return aufgabengebiete;
    }

    public List<Tagesordnung> getTagesordnungenFromResultSet(ResultSet rs) throws SQLException {
        List<Tagesordnung> Tagesordnung = new ArrayList<Tagesordnung>();

        while (rs.next()) {
            int ID = rs.getInt("ID");
            String Titel = rs.getString("Titel");
            String Kurzbeschreibung = rs.getString("Kurzbeschreibung");
            String Protokolltext = rs.getString("Protokolltext");

            Tagesordnung.add(new Tagesordnung(ID, Titel, Kurzbeschreibung, Protokolltext));
        }

        return Tagesordnung;
    }
}