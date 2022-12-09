public class Antrag implements IAntrag {
    public enum Ergebnis {
        JA, NEIN, ENTHALTUNG
    }

    private String Titel;
    private String Text;
    private IAntrag.Ergebnis Ergebnis;
    private boolean Angenommen;

    public Antrag(String Titel, String Text, IAntrag.Ergebnis Ergebnis, boolean Angenommen) {
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
    public IAntrag.Ergebnis getErgebnis() {
        return this.Ergebnis;
    }
    public Boolean isAngenommen() {
        return this.Angenommen;
    }

    @Override
    public void setErgebnis(IAntrag.Ergebnis Ergebnis) {
        setErgebnis(Ergebnis);
    }
}