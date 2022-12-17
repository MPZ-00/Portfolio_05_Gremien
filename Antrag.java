public class Antrag extends APrimaryKey {
    public enum Ergebnis {
        JA, NEIN, ENTHALTUNG
    }
    private String Titel;
    private String Text;
    private Ergebnis Ergebnis;
    private boolean Angenommen;

    public Antrag(int ID, String Titel, String Text, Ergebnis Ergebnis, boolean Angenommen) {
        setID(ID);
        setTitel(Titel);
        setText(Text);
        setErgebnis(Ergebnis);
        setAngenommen(Angenommen);
    }
    
    public void setTitel(String Titel) {
        this.Titel = Titel;
    }
    public void setText(String Text) {
        this.Text = Text;
    }
    public void setAngenommen(Boolean Angenommen) {
        this.Angenommen = Angenommen;
    }

    public String getTitel() {
        return this.Titel;
    }
    public String getText() {
        return this.Text;
    }
    public Ergebnis getErgebnis() {
        return this.Ergebnis;
    }
    public Boolean isAngenommen() {
        return this.Angenommen;
    }

    public void setErgebnis(Ergebnis Ergebnis) {
        this.Ergebnis = Ergebnis;
    }
}