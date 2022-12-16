public class Aufgabe2 {
    public Aufgabe2() {
        Aushilfe.getInstance().print_Titel("Tagesordnung für Sitzung (" + Sitzungen.getAktiveSitzung().getID() + ")");
        
        hs_ids top = new hs_ids(
            "select t.id " +
            "from sitzungen s " +
            "inner join top on top.id_sitzung = s.id " +
            "inner join tagesordnung t on t.id = top.id_tagesordnung " +
            "where s.id = " +
            Sitzungen.getAktiveSitzung().getID()
        );

        if (top.getHS().size() == 0) {
            System.err.println("Für diese Sitzung gibt es keine Tagesordnungen");
            return;
        }

        for (AHauptklasse object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung)object;
            if (top.getHS().contains(t.getID())) {
                System.out.printf("\nID: %d\nTitel: %s\nKurzbeschreibung: %s\nProtokolltext: %s", t.getID(), t.getTitel(), t.getKurzbeschreibung(), t.getProtokolltext());
                for (AHauptklasse obj : Factory.getInstance().getObject(Antrag.class.toString())) {
                    Antrag a = (Antrag)obj;

                    hs_ids antraege = new hs_ids(
                        "select a.id " +
                        "from tagesordnung t " +
                        "inner join gehoert_zu on gehoert_zu.id_top = t.id " +
                        "inner join antrag a on a.id = gehoert_zu.id_antrag " +
                        "where t.id = " + t.getID()
                    );
                    if (antraege.getHS().size() > 0 && antraege.getHS().contains(a.getID())) {
                        System.out.printf("\nID: %d\nTitel: %s\nText: %s\nErgebnis: %s\nAngenommen: %b", a.getID(), a.getTitel(), a.getText(), a.getErgebnis().toString(), a.isAngenommen());
                    }
                }
            }
        }
    }
}
