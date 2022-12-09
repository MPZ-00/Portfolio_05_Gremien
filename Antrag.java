public class Antrag implements IAntrag {
    private String name;
    private String result;

    public Antrag(String name) {
        setName(name);
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getResult() {
        return this.result;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
}