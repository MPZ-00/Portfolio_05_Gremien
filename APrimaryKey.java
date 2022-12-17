public abstract class APrimaryKey implements IAPrimaryKey {
    private int ID;

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }
}