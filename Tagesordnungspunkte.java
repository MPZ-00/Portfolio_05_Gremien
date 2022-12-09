import java.util.ArrayList;
public class Tagesordnungspunkte implements ITagesordnungspunkte {
    private String name;
    private ArrayList<Antrag> antraege;

    public Tagesordnungspunkte(String name) {
        this.name = name;
        this.antraege = new ArrayList<Antrag>();
    }

    public void addTOP(Antrag antrag) {
        this.antraege.add(antrag);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Antrag> getAntraege() {
        return this.antraege;
    }
}
