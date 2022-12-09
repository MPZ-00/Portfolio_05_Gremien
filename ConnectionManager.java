import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    // JDBC-Treiber- und Datenbank-URL
    private static final String JDBC_DRIVER = "oracle.jdbc.pool.OracleDataSource";
    private static final String DB_URL = "fbe-neptun2.rwu.de";
    private static final String PREFIX = "jdbc:oracle:thin:@";

    // Datenbank-Zugangsdaten
    private static final String USER = "DABS_42";
    private static final String PASS = "DABS_42";
    private static final String DB_NAME = "DABS_42";
    private static final String PORT = "1521";

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
                String new_DB_URL = PREFIX + DB_URL + ":" + PORT + "/" + DB_NAME;
                connection = DriverManager.getConnection(new_DB_URL, USER, PASS);
            } catch (SQLException e) {
                System.out.println("Fehler beim Herstellen der Verbindung zur Datenbank");
                e.printStackTrace();
            }
        }

        return connection;
    }

    public void connect(String url, String db_name, String user, String pass, String port) {
        try {
            // Formatiere die URL, falls sie nicht mit "jdbc:oracle:thin:@" beginnt
            if (!url.startsWith(PREFIX)) {
                url = PREFIX + url;
            }

            // Füge den Port hinzu, falls er übergeben wurde
            if (port != null && !port.isEmpty()) {
                url += ":" + port;
            } else {
                url += ":" + PORT;
            }

            // Füge den Namen der Datenbank in die URL ein
            url += "/" + db_name;

            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Fehler beim Herstellen der Verbindung zur Datenbank");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.out.println("Fehler beim Schließen der Verbindung zur Datenbank");
                e.printStackTrace();
            }
        }
    }

    public ResultSet executeStatement(String sql) {
        if (connection == null) {
            System.out.println("Fehler: Keine Verbindung zur Datenbank");
            return null;
        }

        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Fehler beim Ausführen des Statements");
            e.printStackTrace();
            return null;
        }
    }
}
