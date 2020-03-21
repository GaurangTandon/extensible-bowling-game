
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class Lane extends Thread implements PinsetterObserver, LaneInterface {
    private Party party;
    private final Pinsetter setter;
    private final HashMap<Bowler, int[]> scores;
    private final Vector<LaneObserver> subscribers;

    private boolean gameIsHalted;

    private boolean partyAssigned;
    private boolean gameFinished;
    private Iterator<Bowler> currentBowler;
    private int ball;
    private int currBowlerIndex;
    private int frameNumber;
    private boolean tenthFrameStrike;

    private int[] curScores;
    private int[][] cumulScores;
    private boolean canThrowAgain;

    private int[][] finalScores;
    private int gameNumber;

    private Bowler currentThrower;            // = the thrower who just took a throw
    private LaneScorer scorer;

    /**
     * Lane()
     * <p>
     * Constructs a new lane and starts its thread
     *
     * @pre none
     * @post a new lane has been created and its thread is executing
     */
    public Lane() {
        setter = new Pinsetter();
        scores = new HashMap<>(0);
        subscribers = new Vector<>(0);

        gameIsHalted = false;
        partyAssigned = false;

        gameNumber = 0;
        scorer = new LaneScorer();

        setter.subscribe(this);

        start(); // coming from Thread class, TODO: figure out if needed Thread extension?
    }

    private void exitGame(final String partyName) {
        final EndGameReport egr = new EndGameReport(partyName, party);
        final Vector<String> printVector = egr.getResult();
        partyAssigned = false;
        party = null;

        final LaneEvent event = lanePublish();
        publish(event);

        int myIndex = 0;
        for (final Bowler bowler : party.getMembers()) {
            final ScoreReport sr = new ScoreReport(bowler, finalScores[myIndex], gameNumber);
            myIndex++;

            final String email = bowler.getEmail();
            sr.sendEmail(email);

            final String nick = bowler.getNick();
            final boolean shouldPrint = Util.containsString(printVector, nick);
            if (shouldPrint) {
                System.out.println("Printing " + nick);
                sr.sendPrintout();
            }
        }
    }

    private void onGameFinish() {
        final String partyName = party.getName();

        final EndGamePrompt egp = new EndGamePrompt(partyName);
        final int result = egp.getResult();
        egp.destroy();

        System.out.println("Result was: " + result);

        // TODO: send record of scores to control desk
        if (result == 1) { // yes, want to play agian TODO: make this an enum
            scorer.resetScores();
            resetBowlerIterator();
        } else if (result == 2) {// no, dont want to play another game
            exitGame(partyName);
        }
    }

    private void frame9Settlement() {
        finalScores[currBowlerIndex][gameNumber] = cumulScores[currBowlerIndex][9];
        try {
            final String dateString = Util.getDateString();
            ScoreHistoryFile.addScore(currentThrower.getNick(), dateString,
                    Integer.toString(cumulScores[currBowlerIndex][9]));
        } catch (final Exception e) {
            System.err.println("Exception in addScore. " + e);
        }
    }

    private void bowlNextBowler() {
        currentThrower = (Bowler) currentBowler.next();

        canThrowAgain = true;
        tenthFrameStrike = false;
        ball = 0;
        while (canThrowAgain) {
            setter.ballThrown();
            ball++;
        }
        if (frameNumber == 9) {
            frame9Settlement();
        }
        setter.resetState();
        currBowlerIndex++;
    }

    private void continueGame() {
        if (currentBowler.hasNext()) {
            bowlNextBowler();
        } else {
            frameNumber++;
            resetBowlerIterator();
            currBowlerIndex = 0;
            if (frameNumber > 9) {
                gameFinished = true;
                gameNumber++;
            }
        }
    }

    /**
     * run()
     * <p>
     * entry point for execution of this lane
     */
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (partyAssigned && !gameFinished) {
                while (gameIsHalted) {
                    Util.busyWait(10);
                }

                continueGame();
            } else if (partyAssigned) onGameFinish();

            Util.busyWait(10);
        }
    }

    /**
     * recievePinsetterEvent()
     * <p>
     * recieves the thrown event from the pinsetter
     *
     * @param pe The pinsetter event that has been received.
     * @pre none
     * @post the event has been acted upon if desiered
     */
    public void receivePinsetterEvent(final PinsetterEvent pe) {
        if (pe.isReset())
            return;

        final int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        final int throwNumber = pe.getThrowNumber();
        // TODO: what is frameNumber + 1 ??
        scorer.markScore(currentThrower, frameNumber + 1, throwNumber, pinsDownOnThisThrow);

        // next logic handles the ?: what conditions dont allow them another throw?
        // handle the case of 10th frame first
        if (frameNumber != 9) { // TODO: verify this is actually the case? "its not the 10th frame"
            canThrowAgain = !(pinsDownOnThisThrow == 10 || throwNumber == 2);
            return;
        }
        final int totalPinsDown = pe.totalPinsDown();
        if (totalPinsDown == 10) {
            setter.resetPins();
            tenthFrameStrike = throwNumber == 1;
        }
        setCanThrowAgain(totalPinsDown, throwNumber);
    }

    private void setCanThrowAgain(final int totalPinsDown, final int throwNumber) {
        canThrowAgain = canThrowAgain
                && !((totalPinsDown != 10) && (throwNumber == 2 && !tenthFrameStrike))
                && !(throwNumber == 3);
    }

    /**
     * resetBowlerIterator()
     * <p>
     * sets the current bower iterator back to the first bowler
     *
     * @pre the party as been assigned
     * @post the iterator points to the first bowler in the party
     */
    private void resetBowlerIterator() {
        currentBowler = party.getMembers().iterator();
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
    void assignParty(final Party theParty) {
        party = theParty;
        resetBowlerIterator();
        partyAssigned = true;

        final Vector<Bowler> members = party.getMembers();
        final int size = members.size();

        curScores = new int[size];
        cumulScores = new int[size][10];
        finalScores = new int[size][128]; //Hardcoding a max of 128 games, bite me.
        gameNumber = 0;

        scorer.resetScores();
    }

    /**
     * lanePublish()
     * <p>
     * Method that creates and returns a newly created laneEvent
     *
     * @return The new lane event
     */
    private LaneEvent lanePublish() {
        return new LaneEvent(party, currBowlerIndex, currentThrower, cumulScores, scores,
                frameNumber + 1, curScores, ball, gameIsHalted);
    }

    /**
     * isPartyAssigned()
     * <p>
     * checks if a party is assigned to this lane
     *
     * @return true if party assigned, false otherwise
     */
    boolean isPartyAssigned() {
        return partyAssigned;
    }

    /**
     * subscribe
     * <p>
     * Method that will add a subscriber
     *
     * @param laneObserver Observer that is to be added
     */

    void subscribe(final LaneObserver laneObserver) {
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

    Pinsetter getPinsetter() {
        return setter;
    }

    /**
     * Pause the execution of this game
     */
    public void pauseGame() {
        gameIsHalted = true;
        publish(lanePublish());
    }

    /**
     * Resume the execution of this game
     */
    void unPauseGame() {
        gameIsHalted = false;
        publish(lanePublish());
    }

}
