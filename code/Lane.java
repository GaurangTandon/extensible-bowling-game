import java.io.IOException;
import java.util.Vector;


public class Lane extends Publisher implements Runnable, LaneInterface, Observer {
    static final int FRAME_COUNT = 10;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;
    static final int LAST_FRAME = FRAME_COUNT - 1;

    private GeneralParty party;
    private final Pinsetter pinsetter;
    private final LaneScorer scorer;

    /**
     * Lane()
     * <p>
     * Constructs a new lane and starts its thread
     *
     * @pre none
     * @post a new lane has been created and its thread is executing
     */
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

    private void setFinalScoresOnGameEnd() {
        final int finalScore = scorer.finalizeCurrentBowlersGameScore();

        try {
            final String dateString = Util.getDateString();
            ScoreHistoryFile.addScore(getCurrentThrowerNick(), dateString, Integer.toString(finalScore));
        } catch (final IOException e) {
            System.err.println("Exception in addScore. " + e);
        }
    }

    private void bowlOneBowlerOneFrame() {
        while (scorer.canRollAgain()) {
            pinsetter.ballThrown();
        }
        if (scorer.isLastFrame()) {
            setFinalScoresOnGameEnd();
        }
        pinsetter.resetState();
    }

    private String getCurrentThrowerNick() {
        return party.getMemberNick(scorer.currentBowler());
    }

    /**
     * run()
     * <p>
     * entry point for execution of this lane
     */
    public final void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            // Since it is not guaranteed game is set
            // as soon as party got assigned, and this is
            // a multi-threaded environment
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

    /**
     * receivePinsetterEvent()
     * <p>
     * receives the thrown event from the pinsetter
     *
     * @param pev The pinsetter event that has been received.
     * @pre none
     * @post the event has been acted upon if desired
     */
    public final void receiveEvent(final Event pev) {
        final PinsetterEvent pe = (PinsetterEvent) pev;
        if (pe.isReset())
            return;

        final int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        scorer.roll(pinsDownOnThisThrow);
        publish();
    }

    /**
     * assignParty()
     * <p>
     * assigns a party to this lane
     *
     * @param theParty Party to be assigned
     * @pre none
     * @post the party has been assigned to the lane
     */
    final void assignParty(final GeneralParty theParty) {
        party = theParty;

        final Vector<GeneralBowler> members = party.getMembers();

        scorer.resetScores(members);
    }

    /**
     * lanePublish()
     * <p>
     * Method that creates and returns a newly created laneEvent
     *
     * @return The new lane event
     */
    Event createEvent() {
        return new LaneEvent(party.getMemberNicks(), party.getPartySize(), getCurrentThrowerNick(),
                scorer.getCumulativeScores(), scorer.getByBowlerByFramePartResult(), scorer.isHalted(),
                scorer.shouldResetGraphics(), getPinsDown());
    }

    private int getPinsDown() {
        return pinsetter == null ? 0 : pinsetter.totalPinsDown();
    }

    /**
     * isPartyAssigned()
     * <p>
     * checks if a party is assigned to this lane
     *
     * @return true if party assigned, false otherwise
     */
    final boolean isPartyAssigned() {
        return party != null;
    }

    /**
     * Accessor to get this Lane's pinsetter
     *
     * @return A reference to this lane's pinsetter
     */

    final Pinsetter getPinsetter() {
        return pinsetter;
    }

    /**
     * Pause the execution of this game
     */
    public final void pauseGame() {
        scorer.pause();
        publish();
    }

    /**
     * Resume the execution of this game
     */
    final void unPauseGame() {
        scorer.unpause();
        publish();
    }

}
