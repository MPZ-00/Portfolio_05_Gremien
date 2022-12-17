public class A1_Eingabe_Gremium_und_Sitzung {
    public A1_Eingabe_Gremium_und_Sitzung() {
        Gremien.getInstance().Wahl();
        System.out.println("Ausgewähltes Gremium (ID/Name): " + Gremien.getInstance().getAktuellesGremium().getID() + "/" + Gremien.getInstance().getAktuellesGremium().getName());

        if (Gremien.getInstance().getAktuellesGremium() == null) {
            System.err.println("Kein Gremium verfügbar, wähle ein anderes Gremium aus\n");
        }
        
        try {
            Sitzungen.getInstance().Wahl();
            System.out.println("Ausgewählte Sitzung (ID/Beginn): " + Sitzungen.getInstance().getAktiveSitzung().getID() + "/" + Sitzungen.getInstance().getAktiveSitzung().getBeginn());
        } catch (NullPointerException e) {
            if (Tools.getInstance().frage_Ja_Nein("Jetzt neue Sitzung für dieses Gremium anlegen")) {
                Sitzungen.getInstance().Erzeugen();
            }
        }
    }
}
