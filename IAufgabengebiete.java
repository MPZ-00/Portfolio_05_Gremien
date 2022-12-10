import java.util.List;

public interface IAufgabengebiete {
    public void setID(int ID);
    public void setAg_ID(int Ag_ID);
    public void setAufgabengebiet(String Aufgabengebiet);

    public int getID();
    public int getAg_ID();
    public String getAufgabengebiet();

    public List<Aufgabengebiete> getAllAufgabengebiete();
}
