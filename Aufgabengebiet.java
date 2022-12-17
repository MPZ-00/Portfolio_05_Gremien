public class Aufgabengebiet extends APrimaryKey {
    private String Aufgabengebiet;

    public Aufgabengebiet(int ID, String Aufgabengebiet) {
        setID(ID);
        setAufgabengebiet(Aufgabengebiet);
    }

    public void setAufgabengebiet(String Aufgabengebiet) {
        this.Aufgabengebiet = Aufgabengebiet;
    }

    public String getAufgabengebiet() {
        return this.Aufgabengebiet;
    };
}
