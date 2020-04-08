import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class that provides commonly used functionality
 * such as busyWait using a Thread, or getting the current
 * time stamp string.
 * Intended to replace equivalent pieces of code with
 * just one-line function calls of Util.
 */
final class Util {
    static final String DELIMITER = ",";

    private Util() {
    }

    static void busyWait(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            System.err.println("Interrupted");
        }
    }

    static String getDateString() {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");
        return now.format(formatter);
    }

    static boolean containsString(final Iterable<String> container, final String target) {
        if (target == null) return false;
        for (final String str : container) {
            if (target.equals(str))
                return true;
        }
        return false;
    }

    static Bowler getPatronDetails(final String nickName) {
        Bowler patron = null;

        try {
            patron = BowlerFile.getBowlerInfo(nickName);
        } catch (final IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }
}
