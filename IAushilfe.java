import java.sql.Timestamp;
import java.util.Scanner;

public interface IAushilfe {
    public void Gremien_anzeigen();
    public boolean Sitzungen_anzeigen(Integer id);
    public void interne_DB_initialisieren();
    public boolean frage_Ja_Nein(Scanner scanner, String frage);
    public Timestamp getTimestamp(String text);
    public boolean isValidDateFormat(String input, String regex);
    public boolean Aufgabe1();
    public boolean Aufgabe2();
    public void Gremium_Wahl();
    public void Sitzung_Wahl();
    public void Tagesordnung_Wahl();
    public void Antrag_Wahl();
}