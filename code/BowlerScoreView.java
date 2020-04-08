import javax.swing.*;
import java.awt.*;

class BowlerScoreView {
    private final Component panel;
    private final Widget.GridPanel gridPanel;

    BowlerScoreView(final String bowlerNick) {
        final int maxBalls = Frame.MAX_ROLLS + 2;
        gridPanel = new Widget.GridPanel(maxBalls, Frame.FRAME_COUNT, bowlerNick);
        panel = gridPanel.getPanel();
    }

    private static String getCharToShow(final int currScore) {
        final String textToSet;
        switch (currScore) {
            case Frame.STRIKE:
                textToSet = "X";
                break;
            case Frame.SPARE:
                textToSet = "/";
                break;
            default:
                textToSet = Integer.toString(currScore);
        }
        return textToSet;
    }

    private void setBoxLabels(final int[] scores) {
        for (int i = 0; i < Frame.MAX_ROLLS; i++) {
            final int bowlScore = scores[i];
            final JLabel ballLabel = gridPanel.getItemLabel(i);

            // it means that the particular roll was skipped due to a strike
            final String textToSet = bowlScore == -1 ? "" : getCharToShow(bowlScore);

            ballLabel.setText(textToSet);
        }
    }

    private void setScoreLabels(final int[] bowlerScores) {
        for (int frameIdx = 0; frameIdx < Frame.FRAME_COUNT; frameIdx++) {
            final JLabel blockLabel = gridPanel.getBlockLabel(frameIdx);
            final String textToSet = bowlerScores[frameIdx] == -1 ? "" : Integer.toString(bowlerScores[frameIdx]);

            blockLabel.setText(textToSet);
        }
    }

    void update(final int[] bowlerScores, final int[] scores) {
        setScoreLabels(bowlerScores);
        setBoxLabels(scores);
    }

    Component getPanel() {
        //noinspection ReturnPrivateMutableField
        return panel;
    }
}
