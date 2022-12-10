import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Aufgabengebiete extends ResultSetParser implements IAufgabengebiete {
    private static int nextID = 1; // statischer Attributwert für die nächste ID
    private static Set<Integer> usedIDs = new HashSet<>(); // statisches Set für verwendete IDs
    
    // Attribute
    private int ID;
    private int Ag_ID; // Referenz auf Gremium
    private String Aufgabengebiet;

    public Aufgabengebiete(int ID, int Ag_ID, String Aufgabengebiet) {
        setID(ID);
        setAg_ID(Ag_ID);
        setAufgabengebiet(Aufgabengebiet);
    }
    public Aufgabengebiete(int Ag_ID, String Aufgabengebiet) {
        setID(nextID++);
        setAg_ID(Ag_ID);
        setAufgabengebiet(Aufgabengebiet);
    }

    public void setID(int ID) {
        // Überprüfung, ob die angegebene ID bereits im Set für verwendete IDs vorhanden ist
        if (usedIDs.contains(ID)) {
            throw new IllegalArgumentException("ID(" + ID + ") ist bereits in verwendung");
        }
        
        // Wert des ID-Attributs ändern und neue ID im Set für verwendete IDs speichern
        usedIDs.add(this.ID);
        this.ID = ID;
    }
    public void setAg_ID(int Ag_ID) {
        this.Ag_ID = Ag_ID;
    }
    public void setAufgabengebiet(String Aufgabengebiet) {
        this.Aufgabengebiet = Aufgabengebiet;
    }

    public int getID() {
        return this.ID;
    };
    public int getAg_ID() {
        return this.Ag_ID;
    };
    public String getAufgabengebiet() {
        return this.Aufgabengebiet;
    };

    public List<Aufgabengebiete> getAllAufgabengebiete() {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Aufgabengebiete");

            return getAufgabengebieteFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
