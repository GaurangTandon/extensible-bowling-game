import Widget.ButtonPanel;
import Widget.ContainerPanel;
import Widget.WindowFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class AdhocView implements ActionListener {
    private final ButtonPanel buttonPanel;
    private final WindowFrame win;
    private final ContainerPanel statDisplay;
    private Score currScore;

    AdhocView() {
        buttonPanel = new ButtonPanel(4, 1, "")
                .put(ButtonNames.BTN_HIGHEST, this)
                .put(ButtonNames.BTN_LOWEST, this)
                .put(ButtonNames.BTN_BEST, this)
                .put(ButtonNames.BTN_FINISHED, this);

        statDisplay = new ContainerPanel("Stat display");

        // Window
        win = new WindowFrame(
                "Add Party",
                new ContainerPanel(1, 3, "")
                        .put(buttonPanel)
                        .put(statDisplay)
        );
        currScore = new Score();
    }

    private void setDisplayLabel(final String pre) {
        final String str = pre + " achieved by bowler " + currScore.getNick() + " with score " + currScore.getScore();
        final JLabel jl = new JLabel(str);
        statDisplay.clear();
        statDisplay.put(jl);
        statDisplay.getPanel().revalidate();
    }

    private void displayLowest() {
        try {
            currScore = ScoreHistoryFile.getLeastScore();

            setDisplayLabel("Lowest score");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void displayHighest() {
        try {
            currScore = ScoreHistoryFile.getBestScore();

            setDisplayLabel("Highest score");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void displayBestPlayer() {
        try {
            currScore = ScoreHistoryFile.getMaxCumulativeScore();

            setDisplayLabel("Highest overall games score");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

        if (source.equals(buttonPanel.get(ButtonNames.BTN_HIGHEST))) {
            displayHighest();
        }
        if (source.equals(buttonPanel.get(ButtonNames.BTN_LOWEST))) {
            displayLowest();
        }
        if (source.equals(buttonPanel.get(ButtonNames.BTN_FINISHED))) {
            win.destroy();
        }
        if (source.equals(buttonPanel.get(ButtonNames.BTN_BEST))) {
            displayBestPlayer();
        }
    }
}
