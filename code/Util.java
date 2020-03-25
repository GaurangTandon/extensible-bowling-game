import java.util.Calendar;
import java.util.Vector;

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
        final Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + " " + cal.get(Calendar.MONTH) +
                "/" + cal.get(Calendar.DAY_OF_WEEK) + "/" + (cal.get(Calendar.YEAR));
    }

    static boolean containsString(final Iterable<String> container, final String target) {
        for (final String str : container) {
            if (str.equals(target))
                return true;
        }
        return false;
    }
}
