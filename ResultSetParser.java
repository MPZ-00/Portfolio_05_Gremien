import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetParser implements IDatabaseObject {
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
}