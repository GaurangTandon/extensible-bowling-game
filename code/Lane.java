import java.io.IOException;
import java.util.Vector;


public class Lane extends Publisher implements Runnable, LaneInterface, Observer {
    private GeneralParty party;
    private final Pinsetter pinsetter;
    private final LaneScorer scorer;

    public Lane() {
        pinsetter = new Pinsetter();
        scorer = new LaneScorer();
        pinsetter.subscribe(this);
    }

    private void exitGame(final String partyName) {
        final EndGameReport egr = new EndGameReport(partyName, party.getMemberNicks());

        final int gameNumber = scorer.getGameNumber();
        int myIndex = 0;

        for (final GeneralBowler bowler : party.getMembers()) {
            final ScoreReport sr = new ScoreReport(bowler, scorer.getFinalScores(myIndex), gameNumber);
            myIndex++;

            final String nick = bowler.getNickName();
            if (egr.shouldPrint(nick)) {
                System.out.println("Printing " + nick);
                sr.sendPrintout();
            }
        }

        publish();
        party = null;
    }

    private void onGameFinish() {
        final String partyName = party.getName();

        final EndGamePrompt egp = new EndGamePrompt(partyName);
        final int result = egp.getResult();
        egp.destroy();

        // TODO: send record of scores to control desk
        if (result == 1) { // yes, want to play again TODO: make this an enum
            scorer.onGameFinish();
        } else if (result == 2) {// no, dont want to play another game
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

    private String getCurrentThrowerNick() {
        return party.getMemberNick(scorer.getCurrentBowler());
    }

    public final void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (isPartyAssigned() && !scorer.isFinished()) {
                waitWhilePaused();

                bowlOneBowlerOneFrame();
                scorer.nextBowler();
            } else if (isPartyAssigned()) onGameFinish();

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

    final void assignParty(final GeneralParty theParty) {
        party = theParty;

        final Vector<GeneralBowler> members = party.getMembers();

        scorer.resetScores(members);
    }

    Event createEvent() {
        return new LaneEvent(party.getMemberNicks(), party.getPartySize(), getCurrentThrowerNick(),
                scorer.getCumulativeScores(), scorer.getByBowlerByFramePartResult(), scorer.isHalted(),
                scorer.shouldResetGraphics(), getPinsDown());
    }

    private int getPinsDown() {
        return pinsetter == null ? 0 : pinsetter.totalPinsDown();
    }

    final boolean isPartyAssigned() {
        return party != null;
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
