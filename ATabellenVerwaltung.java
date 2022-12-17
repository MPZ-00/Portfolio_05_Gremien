public abstract class ATabellenVerwaltung implements IATabellenVerwaltung {
    private int ID;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void Wahl() {}
    public void Erzeugen() {}
    public void Anzeigen() {}
}