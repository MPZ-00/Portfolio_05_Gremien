import java.sql.Date;
import java.util.ArrayList;

public interface IGremien {
    public void setID(int ID);
    public void setName(String Name);
    public void setOffiziell(Boolean Offiziell);
    public void setInoffiziell(Boolean Inoffiziell);
    public void setBeginn(Date Beginn);
    public void setEnde(Date Ende);

    public int getID();
    public String getName();
    public Boolean getOffiziell();
    public Boolean getInoffiziell();
    public Date getBeginn();
    public Date getEnde();

    public void addTOPitem(Tagesordnungspunkte item);
    public ArrayList<Tagesordnungspunkte> getTOPitems();
}
