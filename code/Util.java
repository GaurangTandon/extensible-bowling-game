import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class that provides commonly used functionality
 * such as busyWait using a Thread, or getting the current
 * time stamp string.
 * Intended to replace equivalent pieces of code with
 * just one-line function calls of Util.
 */
final class Util {
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
        final LocalDate now = LocalDate.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:MM MM/dd/yyyy");
        return now.format(formatter);
    }

    static boolean containsString(final Iterable<String> container, final String target) {
        for (final String str : container) {
            if (str.equals(target))
                return true;
        }
        return false;
    }
}
