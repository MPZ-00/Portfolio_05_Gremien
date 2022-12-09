public interface IAntrag {
    public enum Ergebnis {
        JA, NEIN, ENTHALTUNG
    }

    public void setTitel(String Titel);
    public void setText(String Text);
    public void setErgebnis(Ergebnis Ergebnis);
    public void setAngenommen(Boolean Angenommen);

    public String getTitel();
    public String getText();
    public Ergebnis getErgebnis();
    public Boolean isAngenommen();
}
