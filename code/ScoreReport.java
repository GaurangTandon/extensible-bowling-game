import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class ScoreReport {
    private String content;

    ScoreReport(final BowlerInfo bowler, final int[] scores, final int games) {
        final String nick = bowler.getNickName();
        final String full = bowler.getFullName();

        Iterable<Score> v = null;
        try {
            v = ScoreHistoryFile.getScores(nick);
        } catch (final Exception e) {
            System.err.println("Error: " + e);
        }

        assert v != null;
        content = "--Lucky Strike Bowling Alley Score Report--\n\n"
                + "Report for " + full + ", aka \"" + nick + "\":\n\n"
                + "Final scores for this session: " + scores[0];

        for (int i = 1; i < games; i++) {
            content = String.format("%s%s", content, ", " + scores[i]);
        }
        content += ".\n\n\nPrevious scores by date: \n";

        for (final Score score : v) {
            content = String.format("%s%s\n", content, "  " + score.getDate() + " - " + score.getScore());
        }
        content += "\n\nThank you for your continuing patronage.";

        sendEmail(bowler.getEmail());
    }

    private void sendEmail(final String recipient) {
        try {
            final Socket socket = new Socket("osfmail.rit.edu", 25);
            final BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), "8859_1"));
            final BufferedWriter out =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), "8859_1"));

            String linesToSend = "HELLO world\nMAIL FROM: <mda2376@rit.edu>\nRCPT TO: <" + recipient + ">\nDATA";
            sendLn(in, out, linesToSend);

            linesToSend = "Subject: Bowling Score Report\nFrom: <Lucky Strikes Bowling Club>\nContent-Type: text/plain; charset=\"us-ascii\"\r\n" + content + "\n\n\n\r\n";
            sendLn(out, linesToSend);

            sendLn(in, out, ".\nQUIT");
            socket.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    void sendPrintout() {
        final PrinterJob job = PrinterJob.getPrinterJob();
        final Printable printObj = new PrintableText(content);

        job.setPrintable(printObj);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (final PrinterException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void sendLn(final BufferedReader in, final BufferedWriter out, final String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            in.readLine();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLn(final BufferedWriter out, final String s) {
        try {
            out.write(s + "\r\n");
            out.flush();
            System.out.println(s);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
