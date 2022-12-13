import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager implements IConnectionManager {
    // JDBC-Treiber- und Datenbank-URL
    private static final String JDBC_DRIVER = "oracle.jdbc.pool.OracleDataSource";
    private String DB_URL = "fbe-neptun.hs-weingarten.de"; // localhost
    private String PREFIX = "jdbc:oracle:thin:@";

    // Datenbank-Zugangsdaten
    private String USER = "DABS_42";
    private String PASS = "DABS_42";
    private String DB_NAME = "namib";
    private String PORT = "1521"; // 10111

    private static ConnectionManager instance = null;
    private Connection connection = null;

    private ConnectionManager() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                String new_DB_URL = PREFIX + DB_URL + ":" + PORT + ":" + DB_NAME;
                connection = DriverManager.getConnection(new_DB_URL, USER, PASS);
            } catch (SQLException e) {
                System.out.println("Fehler beim Herstellen der Verbindung zur Datenbank");
                e.printStackTrace();
            }
        }

        return connection;
    }

    public void setConnection(String url, String db_name, String user, String pass, String port) {
        if (url.startsWith("@//")) {
            // Entferne das "//" aus dem String
            url = url.replace("@//", "@");
        }
        if (url.startsWith("@")) {
            // Teile den String in "prefix" bis und mit zum @-Zeichen und "url" auf
            setPrefix(url.substring(0, url.indexOf("@") + 1));
            setDB_Url(url.substring(url.indexOf("@") + 1));
        } else {
            setDB_Url(url);
        }

        setDB_Name(db_name);
        setUser(user);
        setPass(pass);
        setPort(port);

        getConnection();
    }

    public void showConnection() {
        System.out.printf("[Verbindung zur Datenbank]\n%s%s:%s:%s\n", this.PREFIX, this.DB_URL, this.PORT, this.DB_NAME);
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Verbindung zur Datenbank");
                e.printStackTrace();
            }
        }
    }

    public ResultSet executeStatement(String sql) {
        if (connection == null) {
            System.err.println("Fehler: Keine Verbindung zur Datenbank");
            return null;
        }

        try {
            Statement statement = connection.prepareStatement(sql);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Fehler beim Ausführen des Statements");
            e.printStackTrace();
            return null;
        }
    }

    private String getValueOrDefault(String value, String defaultValue) {
        return (value != null && !value.isEmpty() ? value : defaultValue);
    }

    private void setUser(String user) {
        this.USER = getValueOrDefault(user, USER);
    }
    private void setPass(String pass) {
        this.PASS = getValueOrDefault(pass, PASS);
    }
    private void setDB_Name(String db_name) {
        this.DB_NAME = getValueOrDefault(db_name, DB_NAME);
    }
    private void setDB_Url(String url) {
        this.DB_URL = getValueOrDefault(url, DB_URL);
    }
    private void setPort(String port) {
        this.PORT = getValueOrDefault(port, PORT);
    }
    private void setPrefix(String prefix) {
        this.PREFIX = getValueOrDefault(prefix, PREFIX);
    }
}
