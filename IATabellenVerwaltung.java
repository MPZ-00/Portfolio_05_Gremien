import java.sql.Timestamp;

public interface IATabellenVerwaltung {
    public void Wahl();
    public void Erzeugen();
    public boolean Anzeigen();
    public boolean Anzeigen(Integer id);
    public boolean Enthaelt_Eingabe(String eingabe);
    public boolean Enthaelt_Eingabe(Timestamp beginn);
}
