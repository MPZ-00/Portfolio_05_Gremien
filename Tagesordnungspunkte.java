public class Tagesordnungspunkte {
    private String name;
    private ArrayList<Antrag> antraege;

    public Tagesordnungspunkte(String name) {
        this.name = name;
        this.antraege = new ArrayList<Antrag>();
    }

    public void addTOP(Antrag antrag) {
        this.antraege.add(antrag);
    }
}
