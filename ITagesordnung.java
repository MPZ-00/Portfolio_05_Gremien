import java.util.ArrayList;

public interface ITagesordnung {
    public void setID(int ID);
    public void setTitel(String Titel);
    public void setKurzbeschreibung(String Kurzbeschreibung);
    public void setProtokolltext(String Protokolltext);

    public int getID();
    public String getTitel();
    public String getKurzbeschreibung();
    public String getProtokolltext();

    public void setGremiumID(int Gremium);
    public Gremien getGremium();

    public void addAntrag(Antrag antrag);
    public ArrayList<Antrag> getAntraege();
}
