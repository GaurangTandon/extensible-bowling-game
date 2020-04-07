import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lane extends Publisher implements Runnable, LaneInterface, Observer {
    private final Pinsetter pinsetter;
    private ScorableParty scorer;
    private boolean paused;

    Lane() {
        pinsetter = new Pinsetter();
        scorer = new ScorableParty();
        pinsetter.subscribe(this);
        paused = false;
    }

    private void exitGame(final String partyName) {
        final EndGameReport egr = new EndGameReport(partyName, scorer.getMemberNicks());

        final int gameNumber = scorer.getGameNumber();
        int myIndex = 0;

        for (final Bowler bowler : scorer.getMembers()) {
            final ScoreReport sr = new ScoreReport(bowler, scorer.getFinalScores(myIndex), gameNumber);
            myIndex++;

            final String nick = bowler.getNickName();
            if (egr.shouldPrint(nick)) {
                System.out.println("Printing " + nick);
                sr.sendPrintout();
            }
        }

        publish();
        scorer = null;
    }

    void saveState(final FileWriter fw) throws IOException {
        scorer.saveState(fw);
    }

    void loadState(final BufferedReader fr) throws IOException {
        paused = true;
        scorer = new ScorableParty();
        scorer.resetScoresHard();
        scorer.loadState(fr);
        paused = false;
    }

    void setPauseState(final boolean state) {
        paused = state;
    }

    private void onGameFinish() {
        final String partyName = scorer.getName();

        final EndGamePrompt egp = new EndGamePrompt(partyName);
        final int result = egp.getResult();
        egp.destroy();

        if (result == 1) {
            scorer.onGameFinish();
        } else if (result == 2) {
            exitGame(partyName);
        }
    }

    private void bowlOneBowlerOneFrame() {
        while (scorer.canRollAgain()) {
            pinsetter.ballThrown();
        }

        scorer.setFinalScoresOnGameEnd();
        pinsetter.resetState();
    }

    public final void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (isPartyAssigned() && !paused) {
                if (scorer.isFinished()) onGameFinish();
                else {
                    waitWhilePaused();

                    bowlOneBowlerOneFrame();
                    scorer.nextBowler();
                }
            }

            Util.busyWait(10);
        }
    }

    private void waitWhilePaused() {
        while (scorer.isHalted()) {
            Util.busyWait(10);
        }
    }

    public final void receiveEvent(final Event pev) {
        final PinsetterEvent pe = (PinsetterEvent) pev;
        if (pe.isReset())
            return;

        final int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        scorer.roll(pinsDownOnThisThrow);
        publish();
    }

    final void assignParty(final ScorableParty theParty) {
        scorer = theParty;
        scorer.resetScoresHard();
    }

    Event createEvent() {
        return new LaneEvent(scorer.getMemberNicks(), scorer.getPartySize(), scorer.getCurrentThrowerNick(),
                scorer.getCumulativeScores(), scorer.getByBowlerByFramePartResult(), scorer.isHalted(),
                getPinsDown());
    }

    private int getPinsDown() {
        return pinsetter == null ? 0 : pinsetter.totalPinsDown();
    }

    final boolean isPartyAssigned() {
        return scorer != null;
    }

    final void subscribePinsetter(final PinSetterView psv) {
        pinsetter.subscribe(psv);
    }

    public final void pauseGame() {
        scorer.pause();
        publish();
    }

    final void unPauseGame() {
        scorer.unpause();
        publish();
    }
}
