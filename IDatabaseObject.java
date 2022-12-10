import java.sql.ResultSet;
import java.util.List;

public interface IDatabaseObject {
    public List<Aufgabengebiete> getAufgabengebieteFromResultSet(ResultSet rs);
}
