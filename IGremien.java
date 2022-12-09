public interface IGremien {
    public void addTOPitem(Tagesordnungspunkte item);
    public void setStartTime(String startTime);
    public void setEndTime(String endTime);
    public void setName(String name);
    public String getName();
    public String getStartTime();
    public String getEndTime();
}
