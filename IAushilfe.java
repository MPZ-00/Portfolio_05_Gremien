import java.sql.Timestamp;
import java.util.Scanner;

public interface IAushilfe {
    public void Gremien_anzeigen();
    public void Sitzungen_anzeigen(Integer id);
    public void interne_DB_initialisieren();
    public boolean frage_Ja_Nein(Scanner scanner, String frage);
    public Timestamp getTimestamp(String text, String pattern);
}