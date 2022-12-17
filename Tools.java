import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Tools {
    private static Tools instance = null;
    
    public Tools() {}
    public static Tools getInstance() {
        if (instance == null) {
            instance = new Tools();
        }
        return instance;
    }

    public boolean frage_Ja_Nein(String frage) {
        String input;
        do {
            System.out.println(frage + " (ja/nein): ");
            input = Main.scanner.nextLine();
        } while (!input.equalsIgnoreCase("ja") && !input.equalsIgnoreCase("nein"));

        return (input.equalsIgnoreCase("ja"));
    }

    public Timestamp getTimestamp(String text) {
        String template = "yyyy-MM-dd HH:mm:ss";
        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        // Eingabeaufforderung ausgeben
        System.out.printf("%s (%s) ein: ", text, template);
        
        try {
            String input = Main.scanner.nextLine();
            if (!input.matches(regex)) {
                throw new IllegalArgumentException();
            }

            // Konvertiere den Eingabe-String in ein LocalDateTime-Objekt
            LocalDateTime dateTime = LocalDateTime.parse(input, DateTimeFormatter.ofPattern(template));

            // Konvertiere das LocalDateTime-Objekt in ein Timestamp-Objekt
            return Timestamp.from(dateTime.toInstant(ZoneOffset.of("+2"))); // offset to utc
        } catch (IllegalArgumentException e) {
            // Fehlermeldung ausgeben und erneut nach Timestamp fragen
            System.err.println("Ungültiges Datumsformat. Bitte versuchen Sie es erneut.");
            return getTimestamp(text);
        }
    }

    public LocalDate getLocalDate(String text) {
        int yy = 1964, mm = 5, dd = 14;
        try {
            System.out.printf("%s (%s) ein: ", text, "Jahr");
            while (!Main.scanner.hasNextInt()) {
                yy = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();
            
            System.out.printf("%s (%s) ein: ", text, "Monat");
            while (!Main.scanner.hasNextInt()) {
                mm = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();

            System.out.printf("%s (%s) ein: ", text, "Tag");
            while (!Main.scanner.hasNextInt()) {
                dd = Main.scanner.nextInt();
            }
            Main.scanner.nextLine();
            
            return LocalDate.of(yy, mm, dd);
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Eingabe. Bitte nur Zahlen eingeben.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Ungültiges Datumsformat");
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValidDateFormat(String input, String regex) {
        return input.matches(regex);
    }
}