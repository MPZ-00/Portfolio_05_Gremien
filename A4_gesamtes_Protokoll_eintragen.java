public class A4_gesamtes_Protokoll_eintragen {
    private static A4_gesamtes_Protokoll_eintragen instance = null;

    public A4_gesamtes_Protokoll_eintragen() {}

    public static A4_gesamtes_Protokoll_eintragen getInstance() {
        if (instance == null) {
            instance = new A4_gesamtes_Protokoll_eintragen();
        }
        return instance;
    }

    public boolean is_any_Protokolltext_null() {
        boolean is_Pt_null = false;
        hs_ids hs = new hs_ids(
        "select t.id " +
            "from tagesordnung t " +
            "inner join top on top.id_tagesordnung = t.id " +
            "inner join sitzungen s on s.id = top.id_sitzung " +
            "where s.id = " +
            Sitzungen.getInstance().getAktiveSitzung().getID()
        );

        if (hs.getHS().isEmpty()) {
            return false; // Keine Tagesordnungen der aktiven Sitzung gefunden
        }

        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung) object;

            if (hs.getHS().contains(t.getID()) && (t.getProtokolltext() == null || t.getProtokolltext().equalsIgnoreCase("null"))) {
                Tagesordnungen.getInstance().Anzeigen(t.getID());
                is_Pt_null = true;
            }
        }
        return is_Pt_null;
    }
}
