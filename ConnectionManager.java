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
    private static final String SID = "namib";

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

    public void connect(String url, String db_name, String user, String pass, String port, String sid) {
        try {
            url = getValueOrDefault(url, DB_URL);
            // Formatiere die URL, falls sie nicht mit "jdbc:oracle:thin:@" beginnt
            if (!url.startsWith(PREFIX)) {
                url = PREFIX + url;
            }

            // Füge den Port hinzu, falls er übergeben wurde
            url += ":" + getValueOrDefault(port, PORT);

            // Füge die SID hinzu, falls diese übergeben wurde
            // url += ":" + getValueOrDefault(sid, SID);

            // Füge den Namen der Datenbank in die URL ein
            url += "/" + getValueOrDefault(db_name, DB_NAME);

            user = getValueOrDefault(user, USER);
            pass = getValueOrDefault(pass, PASS);

            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Fehler beim Herstellen der Verbindung zur Datenbank");
            System.out.println("URL: " + url + "\nUser: " + user + "\nPass: " + pass);
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

    private String getValueOrDefault(String value, String defaultValue) {
        return (value != null && !value.isEmpty() ? value : defaultValue);
    }

    public void directConnnect(String url, String user, String pass) throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
    }
}
