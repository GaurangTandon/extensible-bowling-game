import Widget.ButtonPanel;
import Widget.ContainerPanel;
import Widget.WindowFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class AdhocView implements ActionListener {
    private static final String BTN_HIGHEST = "Highest score";
    private static final String BTN_LOWEST = "Lowest score";
    private static final String BTN_BEST = "Best player";
    private static final String BTN_FINISHED = "Finished";
    private final ButtonPanel buttonPanel;
    private final WindowFrame win;

    AdhocView() {
        buttonPanel = new ButtonPanel(4, 1, "")
                .put(BTN_HIGHEST, this)
                .put(BTN_LOWEST, this)
                .put(BTN_BEST, this)
                .put(BTN_FINISHED, this);

        // Window
        win = new WindowFrame(
                "Add Party",
                new ContainerPanel(1, 3, "")
                        .put(buttonPanel));
    }

    void displayLowest() {
        try {
            Score score = ScoreHistoryFile.getLeastScore();


        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    void displayHighest() {
        try {
            Score score = ScoreHistoryFile.getBestScore();


        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    void displayBestPlayer() {
        try {
            Score score = ScoreHistoryFile.getMaxCumulativeScore();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();

        if (source.equals(buttonPanel.get(BTN_HIGHEST))) {
            displayHighest();
        }
        if (source.equals(buttonPanel.get(BTN_LOWEST))) {
            displayLowest();
        }
        if (source.equals(buttonPanel.get(BTN_FINISHED))) {
            win.destroy();
        }
        if (source.equals(buttonPanel.get(BTN_BEST))) {
            displayBestPlayer();
        }
    }
}
