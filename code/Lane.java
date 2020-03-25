import java.io.IOException;
import java.util.Vector;


public class Lane extends Thread implements PinsetterObserver, LaneInterface {
    static final int FRAME_COUNT = 10;
    // two rolls for n - 1 frames, strike in first roll of last frame, then two more chances
    static final int MAX_ROLLS = FRAME_COUNT * 2 + 1;
    static final int LAST_FRAME = FRAME_COUNT - 1;

    private Party party;
    private final Pinsetter pinsetter;
    private final Vector<LaneObserver> subscribers;
    private Game game;

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
        subscribers = new Vector<>(0);

        scorer = new LaneScorer();

        pinsetter.subscribe(this);

        start(); // coming from Thread class, TODO: figure out if needed Thread extension?
    }

    private void exitGame(final String partyName) {
        final EndGameReport egr = new EndGameReport(partyName, party);
        final Vector<String> printVector = egr.getResult();

        int myIndex = 0;
        for (final Bowler bowler : party.getMembers()) {
            final ScoreReport sr = new ScoreReport(bowler, scorer.getFinalScores(myIndex), game.getNumber());
            myIndex++;

            final String email = bowler.getEmail();
            sr.sendEmail(email);

            final String nick = bowler.getNickName();
            final boolean shouldPrint = Util.containsString(printVector, nick);
            if (shouldPrint) {
                System.out.println("Printing " + nick);
                sr.sendPrintout();
            }
        }

        party = null;

        final LaneEvent event = lanePublish();
        publish(event);
    }

    private void onGameFinish() {
        final String partyName = party.getName();

        final EndGamePrompt egp = new EndGamePrompt(partyName);
        final int result = egp.getResult();
        egp.destroy();

        // TODO: send record of scores to control desk
        if (result == 1) { // yes, want to play again TODO: make this an enum
            scorer.resetScores();
            game.restartGame();
        } else if (result == 2) {// no, dont want to play another game
            exitGame(partyName);
        }
    }

    private void setFinalScoresOnGameEnd() {
        final int bowler = game.currentBowler();
        final int finalScore = scorer.getBowlersFinalScoreForCurrentGame(bowler);
        scorer.setFinalScores(bowler, game.getNumber(), finalScore);
        try {
            final String dateString = Util.getDateString();
            ScoreHistoryFile.addScore(getCurrentThrowerNick(), dateString, Integer.toString(finalScore));
        } catch (final IOException e) {
            System.err.println("Exception in addScore. " + e);
        }
    }

    private void bowlOneBowlerOneFrame() {
        while (scorer.canRollAgain(game.currentBowler(), game.currentFrame())) {
            pinsetter.ballThrown();
        }

        if (game.isLastFrame()) {
            setFinalScoresOnGameEnd();
        }

        pinsetter.resetState();
    }

    private String getCurrentThrowerNick() {
        return party.getMemberNick(game.currentBowler());
    }

    /**
     * run()
     * <p>
     * entry point for execution of this lane
     */
    public final void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (isPartyAssigned() && !game.isFinished()) {
                while (game.isHalted()) {
                    Util.busyWait(10);
                }

                bowlOneBowlerOneFrame();
                game.nextBowler();
            } else if (isPartyAssigned()) onGameFinish();

            Util.busyWait(10);
        }
    }

    /**
     * receivePinsetterEvent()
     * <p>
     * receives the thrown event from the pinsetter
     *
     * @param pe The pinsetter event that has been received.
     * @pre none
     * @post the event has been acted upon if desired
     */
    public final void receivePinsetterEvent(final PinsetterEvent pe) {
        if (pe.isReset())
            return;

        final int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        scorer.roll(game.currentBowler(), pinsDownOnThisThrow);
        final LaneEvent event = lanePublish();
        publish(event);
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
    final void assignParty(final Party theParty) {
        party = theParty;
        game = new Game(party.getPartySize());

        final Vector<Bowler> members = party.getMembers();

        scorer.resetScores(members);
    }

    /**
     * lanePublish()
     * <p>
     * Method that creates and returns a newly created laneEvent
     *
     * @return The new lane event
     */
    private LaneEvent lanePublish() {
        final int bowlerIndex = game.currentBowler();
        final boolean shouldSetupGraphics = bowlerIndex == 0 && scorer.isFirstRoll(bowlerIndex);

        return new LaneEvent(party.getMemberNicks(), party.getPartySize(), getCurrentThrowerNick(),
                scorer.getCumulativeScores(), scorer.getByBowlerByFramePartResult(), game.isHalted(),
                shouldSetupGraphics);
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
     * subscribe
     * <p>
     * Method that will add a subscriber
     *
     * @param laneObserver Observer that is to be added
     */

    final void subscribe(final LaneObserver laneObserver) {
        subscribers.add(laneObserver);
    }

    /**
     * publish
     * <p>
     * Method that publishes an event to subscribers
     *
     * @param event Event that is to be published
     */

    private void publish(final LaneEvent event) {
        for (final LaneObserver subscriber : subscribers) {
            subscriber.receiveLaneEvent(event);
        }
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
        game.pause();
        publish(lanePublish());
    }

    /**
     * Resume the execution of this game
     */
    final void unPauseGame() {
        game.unpause();
        publish(lanePublish());
    }

}
