import java.sql.Connection;
import java.sql.ResultSet;

public interface IConnectionManager {
    public Connection getConnection();
    public void setConnection(String url, String db_name, String user, String pass, String port);
    public void showConnection();
    public void disconnect();
    public ResultSet executeStatement(String sql);
}