import Widget.ButtonPanel;
import Widget.ContainerPanel;
import Widget.WindowFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class AdhocView implements ActionListener {
    private final WindowFrame win;
    private final ContainerPanel statDisplay;
    private Score currScore;

    AdhocView() {
        final ButtonPanel buttonPanel = new ButtonPanel(4, 1, "")
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
        currScore = ScoreHistoryFile.getLeastScore();

        setDisplayLabel(ButtonNames.BTN_LOWEST);
    }

    private void displayHighest() {
        currScore = ScoreHistoryFile.getBestScore();

        setDisplayLabel(ButtonNames.BTN_HIGHEST);
    }

    private void displayBestPlayer() {
        currScore = ScoreHistoryFile.getMaxCumulativeScore();

        setDisplayLabel("Highest overall games score");
    }

    public void actionPerformed(final ActionEvent e) {
        final String source = ((AbstractButton) e.getSource()).getText();
        switch (source) {
            case ButtonNames.BTN_HIGHEST:
                displayHighest();
                break;
            case ButtonNames.BTN_LOWEST:
                displayLowest();
                break;
            case ButtonNames.BTN_FINISHED:
                win.destroy();
                break;
            case ButtonNames.BTN_BEST:
                displayBestPlayer();
                break;
        }
    }
}
