import java.util.ArrayList;

public class Gremien implements IGremien {
    private String name;
    private String startTime;
    private String endTime;
    private ArrayList<Tagesordnungspunkte> TOPitem;

    public Gremien(String name, String startTime) {
        this.name = name;
        this.startTime = startTime;
        this.TOPitem = new ArrayList<Tagesordnungspunkte>();
    }

    public void addTOPitem(Tagesordnungspunkte item) {
        this.TOPitem.add(item);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}