public class Drucken {
    private static Drucken instance;

    public Drucken() {}
    public static Drucken getInstance() {
        if (instance == null) {
            instance = new Drucken();
        }
        return instance;
    }

    public void print_Titel(String text) {
        System.out.println("[\033[35m" + text + "\033[0m]");
    }
    public void print_Warnung(String text) {
        System.out.println("[\033[33m" + text + "\033[0m]");
    }
}
