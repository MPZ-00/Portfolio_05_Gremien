import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IDatabaseObject {
    public List<Aufgabengebiete> getAufgabengebieteFromResultSet(ResultSet rs) throws SQLException;
    public List<Tagesordnung> getTagesordnungenFromResultSet(ResultSet rs) throws SQLException;
}
