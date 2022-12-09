import java.sql.Date;
import java.sql.Timestamp;

public interface ISitzungen {
    public void setID(int ID);
    public void setBeginn(Timestamp Beginn);
    public void setEnde(Timestamp Ende);
    public void setEinladung_am(Date Einladung_am);
    public void setOeffentlich(Boolean Oeffentlich);
    public void setOrt(String Ort);
    public void setProtokoll(String Protokoll);

    public int getID();
    public Timestamp getBeginn();
    public Timestamp getEnde();
    public Date getEinladung_am();
    public Boolean getOeffentlich();
    public String getOrt();
    public String getProtokoll();
}
