/*
  SMTP implementation based on code by Ral Gagnon mailto:real@rgagnon.com
 */


import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

class ScoreReport {

    private String content;

    ScoreReport(final Bowler bowler, final int[] scores, final int games) {
        final String nick = bowler.getNick();
        final String full = bowler.getFullName();
        Vector<Score> v = null;
        try {
            v = ScoreHistoryFile.getScores(nick);
        } catch (final Exception e) {
            System.err.println("Error: " + e);
        }

        assert v != null;

        content = "";
        content += "--Lucky Strike Bowling Alley Score Report--\n";
        content += "\n";
        content += "Report for " + full + ", aka \"" + nick + "\":\n";
        content += "\n";
        content += "Final scores for this session: ";
        content += scores[0];
        for (int i = 1; i < games; i++) {
            content = String.format("%s%s", content, ", " + scores[i]);
        }
        content += ".\n";
        content += "\n";
        content += "\n";
        content += "Previous scores by date: \n";
        for (final Score score : v) {
            content = String.format("%s%s", content, "  " + score.getDate() + " - " + score.getScore());
            content += "\n";
        }
        content += "\n\n";
        content += "Thank you for your continuing patronage.";

    }

    void sendEmail(final String recipient) {
        try {
            final Socket s = new Socket("osfmail.rit.edu", 25);
            final BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(s.getInputStream(), "8859_1"));
            final BufferedWriter out =
                    new BufferedWriter(
                            new OutputStreamWriter(s.getOutputStream(), "8859_1"));

            // here you are supposed to send your username
            sendLn(in, out, "HELO world");
            sendLn(in, out, "MAIL FROM: <mda2376@rit.edu>");
            sendLn(in, out, "RCPT TO: <" + recipient + ">");
            sendLn(in, out, "DATA");
            sendLn(out, "Subject: Bowling Score Report ");
            sendLn(out, "From: <Lucky Strikes Bowling Club>");

            sendLn(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
            sendLn(out, content + "\n\n");
            sendLn(out, "\r\n");

            sendLn(in, out, ".");
            sendLn(in, out, "QUIT");
            s.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    void sendPrintout() {
        final PrinterJob job = PrinterJob.getPrinterJob();

        final PrintableText printobj = new PrintableText(content);

        job.setPrintable(printobj);

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
            // System.out.println(s);
            in.readLine();
            // System.out.println(s);
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
