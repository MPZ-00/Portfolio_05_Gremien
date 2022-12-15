import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class hs_ids {
    private HashSet<Integer> hs = new HashSet<>();

    public hs_ids(String sql) {
        ResultSet rs = ConnectionManager.getInstance().executeStatement(sql);
        try {
            while (rs.next()) {
                hs.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashSet<Integer> getHS() {
        return this.hs;
    }
}
