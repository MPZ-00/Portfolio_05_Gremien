import java.sql.Timestamp;

public interface IAushilfe {
    public void Gremien_anzeigen();
    public boolean Sitzungen_anzeigen(Integer id);
    public boolean interne_DB_initialisieren();
    public boolean frage_Ja_Nein(String frage);
    public Timestamp getTimestamp(String text);
    public boolean isValidDateFormat(String input, String regex);
    public void Gremium_Wahl();
    public void Sitzung_Wahl();
    public void Tagesordnung_Wahl();
    public void Antrag_Wahl();
}