public class Aufgabe1 {
    public Aufgabe1() {
        Gremien.getAktuellesGremium().Wahl();
        System.out.println("Ausgewähltes Gremium (ID/Name): " + Gremien.getAktuellesGremium().getID() + "/" + Gremien.getAktuellesGremium().getName());

        if (Gremien.getAktuellesGremium() == null) {
            System.err.println("Kein Gremium verfügbar, wähle ein anderes Gremium aus\n");
        }
        
        try {
            Sitzungen.getAktiveSitzung().Wahl();
            System.out.println("Ausgewählte Sitzung (ID/Beginn): " + Sitzungen.getAktiveSitzung().getID() + "/" + Sitzungen.getAktiveSitzung().getBeginn());
        } catch (NullPointerException e) {
            if (Aushilfe.getInstance().frage_Ja_Nein("Jetzt neue Sitzung für dieses Gremium anlegen")) {
                Sitzungen.getAktiveSitzung().Erzeugen();
            }
        }
    }
}
