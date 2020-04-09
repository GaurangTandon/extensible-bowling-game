import Widget.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class AdhocView extends WindowView implements ActionListener, ListSelectionListener {
    private final WindowFrame win;
    private JLabel statDisplay;
    private String selectedBowler;

    AdhocView() {
        final ButtonPanel buttonPanel = new ButtonPanel(4, 1, "")
                .put(ButtonNames.BTN_HIGHEST, this)
                .put(ButtonNames.BTN_LOWEST, this)
                .put(ButtonNames.BTN_BEST, this)
                .put(ButtonNames.BTN_FINISHED, this)
                .put(ButtonNames.BTN_HIGHLIGHTS, this);

        statDisplay = new JLabel();
        new ContainerPanel("Stat display").put(statDisplay);
        final ArrayList<String> bowlersList = BowlerFile.getBowlers();
        final ScrollablePanel bowlerPanel = drawScrollable(bowlersList, "Bowlers List", 8)
                .attachListener(this);
        selectedBowler = "";

        win = new WindowFrame(
                "Add Party",
                new ContainerPanel(1, 3, "")
                        .put(buttonPanel)
                        .put(bowlerPanel)
                        .put(statDisplay)
        );
    }

    private void displayLowest() {
        final Score currScore = ScoreHistoryFile.getLeastScore();
        final String str = "<html>Lowest Score achieved by bowler:<br/>" + currScore.getNick()
                + "<br/>with score " + currScore.getScore() + "</html>";
        statDisplay.setText(str);
    }

    private void displayHighest() {
        final Score currScore = ScoreHistoryFile.getBestScore();
        final String str = "<html>Highest Score achieved by bowler:<br/>" + currScore.getNick()
                + "<br/>with score " + currScore.getScore() + "</html>";
        statDisplay.setText(str);
    }

    private void displayBestPlayer() {
        final Score currScore = ScoreHistoryFile.getMaxCumulativeScore();
        final String str = "<html>Highest Overall Game Scores:<br/>" + currScore.getNick()
                + "<br/>with score " + currScore.getScore() + "</html>";
        statDisplay.setText(str);
    }

    private void displayTopGames() {
        if (selectedBowler.isEmpty()) {
            statDisplay.setText("No bowler selected!");
            return;
        }

        final StringBuilder stringBuilder = new StringBuilder("<html>Highest Overall Game Scores:<br/>by " + selectedBowler + " are:<br/>");
        final ArrayList<Score> scores = ScoreHistoryFile.getCareerHighlights(selectedBowler);
        for (final Score score : scores) {
            stringBuilder.append("&nbsp;").append(score.getScore()).append(" on ").append(score.getDate()).append("<br/>");
        }
        stringBuilder.append("</html>");
        statDisplay.setText(stringBuilder.toString());
    }

    public void buttonHandler(final String source) {
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
            case ButtonNames.BTN_HIGHLIGHTS:
                displayTopGames();
                break;
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent e) {
        selectedBowler = ((String) ((JList) e.getSource()).getSelectedValue());
    }
}
