import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lane extends LaneInterface implements Runnable {
    private ScorableParty scorer;
    private boolean paused;

    Lane() {
        scorer = null;
        paused = false;
    }

    private void exitGame(final String partyName) {
        final EndGameReport egr = new EndGameReport(partyName, scorer.getMemberNicks());
        egr.printer(scorer);
        publish();
        scorer = null;
    }

    void saveState(final FileWriter fw) throws IOException {
        scorer.saveState(fw);
    }

    void loadState(final BufferedReader fr) throws IOException {
        paused = true;
        scorer = new ScorableParty();
        scorer.loadState(fr);
        paused = false;
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

    public final void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (isPartyAssigned() && !paused) {
                if (scorer.isFinished()) onGameFinish();
                else {
                    while (scorer.isHalted()) Util.busyWait(10);

                    while (scorer.canRollAgain()) pinsetter.ballThrown();
                    scorer.setFinalScoresOnGameEnd();
                    pinsetter.resetState();
                    scorer.nextBowler();
                }
            }
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
        final int pinsDown = pinsetter == null ? 0 : pinsetter.totalPinsDown();
        return new LaneEvent(scorer, pinsDown);
    }

    final boolean isPartyAssigned() {
        return scorer != null;
    }

    public final void pauseGame(final boolean state) {
        scorer.setHalted(state);
        publish();
    }

    void pauseManual(final boolean state) {
        paused = state;
    }
}
