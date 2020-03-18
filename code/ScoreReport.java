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
import java.util.Iterator;
import java.util.Vector;

public class ScoreReport {

	private String content;
	
	public ScoreReport( Bowler bowler, int[] scores, int games ) {
		String nick = bowler.getNick();
		String full = bowler.getFullName();
		Vector v = null;
		try{
			v = ScoreHistoryFile.getScores(nick);
		} catch (Exception e){System.err.println("Error: " + e);}

		assert v != null;
		Iterator scoreIt = v.iterator();

		content = "";
		content += "--Lucky Strike Bowling Alley Score Report--\n";
		content += "\n";
		content += "Report for " + full + ", aka \"" + nick + "\":\n";
		content += "\n";
		content += "Final scores for this session: ";
		content += scores[0];
		for (int i = 1; i < games; i++){
			content = String.format("%s%s", content, ", " + scores[i]);
		}
		content += ".\n";
		content += "\n";
		content += "\n";
		content += "Previous scores by date: \n";
		while (scoreIt.hasNext()){
			Score score = (Score) scoreIt.next();
			content = String.format("%s%s", content, "  " + score.getDate() + " - " + score.getScore());
			content += "\n";
		}
		content += "\n\n";
		content += "Thank you for your continuing patronage.";

	}

	public void sendEmail(String recipient) {
		try {
			Socket s = new Socket("osfmail.rit.edu", 25);
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(s.getInputStream(), "8859_1"));
			BufferedWriter out =
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPrintout() {
		PrinterJob job = PrinterJob.getPrinterJob();

		PrintableText printobj = new PrintableText(content);

		job.setPrintable(printobj);

		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				System.err.println(e.getMessage());
			}
		}

	}

	private void sendLn(BufferedReader in, BufferedWriter out, String s) {
		try {
			out.write(s + "\r\n");
			out.flush();
			// System.out.println(s);
			in.readLine();
			// System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendLn(BufferedWriter out, String s) {
		try {
			out.write(s + "\r\n");
			out.flush();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
