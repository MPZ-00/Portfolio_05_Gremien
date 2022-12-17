public class Aufgabengebiete extends ATabellenVerwaltung implements IAufgabengebiete {
    // Attribute
    private int Ag_ID; // Referenz auf Gremium
    private String Aufgabengebiet;

    public Aufgabengebiete(int ID, int Ag_ID, String Aufgabengebiet) {
        setID(ID);
        setAg_ID(Ag_ID);
        setAufgabengebiet(Aufgabengebiet);
    }

    public void setAg_ID(int Ag_ID) {
        this.Ag_ID = Ag_ID;
    }
    public void setAufgabengebiet(String Aufgabengebiet) {
        this.Aufgabengebiet = Aufgabengebiet;
    }

    public int getAg_ID() {
        return this.Ag_ID;
    };
    public String getAufgabengebiet() {
        return this.Aufgabengebiet;
    };
}
