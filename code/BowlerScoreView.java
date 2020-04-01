import javax.swing.*;
import java.awt.*;

public class BowlerScoreView {
    private Component panel;
    private Widget.GridPanel gridPanel;

    BowlerScoreView(final String bowlerNick){
        final int maxBalls = Lane.MAX_ROLLS + 2;
        gridPanel = new Widget.GridPanel(maxBalls, Lane.FRAME_COUNT, bowlerNick);
        panel = gridPanel.getPanel();
    }

    private static String getCharToShow(final int currScore) {
        final String textToSet;
        switch (currScore) {
            case BowlerScorer.STRIKE:
                textToSet = "X";
                break;
            case BowlerScorer.SPARE:
                textToSet = "/";
                break;
            default:
                textToSet = Integer.toString(currScore);
        }
        return textToSet;
    }

    void setBoxLabels(final int[] scores) {
        for (int i = 0; i < Lane.MAX_ROLLS; i++) {
            final int bowlScore = scores[i];

            // it means that the particular roll was skipped due to a strike
            if (bowlScore != -1) {
                final String textToSet = getCharToShow(bowlScore);
                final JLabel ballLabel = gridPanel.getItemLabel(i);

                ballLabel.setText(textToSet);
            }
        }
    }

    void setScoreLabels(final int[] bowlerScores) {
        for (int frameIdx = 0; frameIdx < Lane.FRAME_COUNT; frameIdx++) {
            if (bowlerScores[frameIdx] != -1)
                gridPanel.getBlockLabel(frameIdx).setText(Integer.toString(bowlerScores[frameIdx]));
        }
    }

    void update(final int[] bowlerScores, final int[] scores) {
        setScoreLabels(bowlerScores);
        setBoxLabels(scores);
    }

    Component getPanel(){
        //noinspection ReturnPrivateMutableField
        return panel;
    }
}
