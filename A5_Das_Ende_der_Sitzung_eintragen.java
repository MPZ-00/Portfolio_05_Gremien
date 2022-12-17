import java.sql.Timestamp;

public class A5_Das_Ende_der_Sitzung_eintragen {
    public A5_Das_Ende_der_Sitzung_eintragen() {
        // Ende der aktuellen Sitzung eintragen
        Aushilfe.getInstance().print_Titel("Aufgabe 5");

        Sitzungen.getAktiveSitzung().Anzeigen(Sitzungen.getAktiveSitzung().getID());

        if (Aushilfe.getInstance().frage_Ja_Nein("Ende der Sitzung eintragen")) {
            Timestamp ende = Aushilfe.getInstance().getTimestamp("Ende der Sitzung");
            Sitzungen.getAktiveSitzung().setEnde(ende);

            ConnectionManager.getInstance().executeStatement(
                "update sitzungen " +
                "set ende = to_timestamp('" + ende + "', 'dd.mm.YYYY HH24:MI:SS')" +
                " where id = " + Sitzungen.getAktiveSitzung().getID()
            );
        }

    }
}
