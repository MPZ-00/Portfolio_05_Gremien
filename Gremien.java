import java.util.ArrayList;

public class Gremien implements IGremien {
    private String name;
    private String startTime;
    private String endTime;
    private ArrayList<Tagesordnungspunkte> TOPitem;

    public Gremien(String name, String startTime) {
        setName(name);
        setStartTime(startTime);
        this.TOPitem = new ArrayList<Tagesordnungspunkte>();
    }

    public Gremien() {
        this.TOPitem = new ArrayList<Tagesordnungspunkte>();
    };

    public void addTOPitem(Tagesordnungspunkte item) {
        this.TOPitem.add(item);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return this.startTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public String getName() {
        return this.name;
    }

    public ArrayList<Tagesordnungspunkte> getTOPitems() {
        return this.TOPitem;
    }
}