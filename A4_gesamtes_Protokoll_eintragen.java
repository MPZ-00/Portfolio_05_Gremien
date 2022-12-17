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
        for (APrimaryKey object : Factory.getInstance().getObject(Tagesordnung.class.toString())) {
            Tagesordnung t = (Tagesordnung) object;
            if (t.getProtokolltext() == null || t.getProtokolltext().equalsIgnoreCase("null")) {
                is_Pt_null = true;
            }
        }
        return is_Pt_null;
    }
}
