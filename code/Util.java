import java.lang.Thread;
import java.util.Calendar;

/**
 * Utility class that provides commonly used functionality
 * such as busyWait using a Thread, or getting the current
 * time stamp string.
 * Intended to replace equivalent pieces of code with
 * just one-line function calls of Util.
 */
public class Util {
    public static void busyWait(int duration) {
        try {
            Thread.sleep(duration);
        } catch (final InterruptedException ignored) {
        }
    }

    public static String getDateString() {
        final Calendar cal = Calendar.getInstance();
        final String dateString = "" + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE)
                + " " + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_WEEK) +
                "/" + (cal.get(Calendar.YEAR) + 1900);
        return dateString;
    }

}
