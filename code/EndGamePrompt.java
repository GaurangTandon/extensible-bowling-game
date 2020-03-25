import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class EndGamePrompt implements ActionListener {

    private final Widget.WindowFrame win;
    private final Widget.ButtonPanel buttonPanel;
    private int result;

    private static final String BTN_YES = "Yes";
    private static final String BTN_NO = "No";

    EndGamePrompt(final String partyName) {
        result = 0;

        final Widget.ContainerPanel labelPanel = new Widget.ContainerPanel("")
                .put(new JLabel("Party " + partyName
                        + " has finished bowling.\nWould they like to bowl another game?"));
        buttonPanel = new Widget.ButtonPanel(1, 2, "")
                .put(BTN_YES, this)
                .put(BTN_NO, this);

        win = new Widget.WindowFrame(
                "Another Game for " + partyName + "?",
                new Widget.ContainerPanel(2, 1, "")
                        .put(labelPanel)
                        .put(buttonPanel)
        );
    }

    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

		if (source.equals(buttonPanel.get(BTN_YES))) {
            result = 1;
        }
        if (source.equals(buttonPanel.get(BTN_NO))) {
            result = 2;
        }

    }

    final int getResult() {
        while (result == 0) {
            Util.busyWait(10);
        }
        return result;
    }

    final void destroy() {
        win.setVisible(false);
    }

}

