
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


public class Lane extends Thread implements PinsetterObserver {
    private Party party;
    private final Pinsetter setter;
    private final HashMap scores;
    private final Vector<LaneObserver> subscribers;

    private boolean gameIsHalted;

    private boolean partyAssigned;
    private boolean gameFinished;
    private Iterator bowlerIterator;
    private int ball;
    private int bowlIndex;
    private int frameNumber;
    private boolean tenthFrameStrike;

    private int[] curScores;
    private int[][] cumulScores;
    private boolean canThrowAgain;

    private int[][] finalScores;
    private int gameNumber;

    private Bowler currentThrower;            // = the thrower who just took a throw

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
        scores = new HashMap();
        subscribers = new Vector<>();

        gameIsHalted = false;
        partyAssigned = false;

        gameNumber = 0;

        setter.subscribe(this);

        start(); // coming from Thread class, TODO: figure out if needed Thread extension?
    }

    private void exitGame(String partyName) {
        final Vector<String> printVector;
        final EndGameReport egr = new EndGameReport(partyName, party);
        printVector = egr.getResult();
        partyAssigned = false;
        party = null;

        final LaneEvent event = lanePublish();
        publish(event);

        int myIndex = 0;
        for (final Object bowler : party.getMembers()) {
            final Bowler thisBowler = (Bowler) bowler;
            final ScoreReport sr = new ScoreReport(thisBowler, finalScores[myIndex], gameNumber);
            myIndex++;

            final String email = thisBowler.getEmail();
            sr.sendEmail(email);

            final String nick = thisBowler.getNick();

            // TODO: write utility to check if an element belongs to a vector
            // and use it here
            for (final String o : printVector) {
                if (nick.equals(o)) {
                    System.out.println("Printing " + nick);
                    sr.sendPrintout();
                }
            }
        }
    }

    private void onGameFinish() {
        Bowler firstBowler = (Bowler) party.getMembers().get(0);
        String partyName = firstBowler.getPartyName();

        final EndGamePrompt egp = new EndGamePrompt(partyName);
        final int result = egp.getResult();
        egp.destroy();

        System.out.println("result was: " + result);

        // TODO: send record of scores to control desk
        if (result == 1) { // yes, want to play agian TODO: make this an enum
            resetScores();
            resetBowlerIterator();
        } else if (result == 2) {// no, dont want to play another game
            exitGame(partyName);
        }
    }

    private void frame9Settlement() {
        finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][9];
        try {
            String dateString = Util.getDateString();
            ScoreHistoryFile.addScore(currentThrower.getNick(), dateString,
                    Integer.toString(cumulScores[bowlIndex][9]));
        } catch (final Exception e) {
            System.err.println("Exception in addScore. " + e);
        }
    }

    private void bowlNextBowler() {
        currentThrower = (Bowler) bowlerIterator.next();

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
        setter.reset();
        bowlIndex++;
    }

    private void continueGame() {
        if (bowlerIterator.hasNext()) {
            bowlNextBowler();
        } else {
            frameNumber++;
            resetBowlerIterator();
            bowlIndex = 0;
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
        int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        if (pinsDownOnThisThrow < 0)
            // else this is not a real throw, probably a reset
            return;
        //TODO: "this is a real throw" what does this mean?

        int throwNumber = pe.getThrowNumber();
        markScore(currentThrower, frameNumber + 1, throwNumber, pinsDownOnThisThrow);

        // next logic handles the ?: what conditions dont allow them another throw?
        // handle the case of 10th frame first
        if (frameNumber != 9) { // TODO: verify this is actually the case? "its not the 10th frame"
            canThrowAgain = !(pinsDownOnThisThrow == 10 || throwNumber == 2);
            return;
        }
        int totalPinsDown = pe.totalPinsDown();
        if (totalPinsDown == 10) {
            setter.resetPins();
            tenthFrameStrike = throwNumber == 1;
        }
        canThrowAgain(totalPinsDown, throwNumber);
    }

    private void canThrowAgain(int totalPinsDown, int throwNumber) {
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
        bowlerIterator = (party.getMembers()).iterator();
    }

    /**
     * resetScores()
     * <p>
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    private void resetScores() {

        for (final Object o : (party.getMembers())) {
            final int[] toPut = new int[25];
            for (int i = 0; i != 25; i++) {
                toPut[i] = -1;
            }
            scores.put(o, toPut);
        }


        gameFinished = false;
        frameNumber = 0;
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

        final Vector members = party.getMembers();
        final int size = members.size();

        curScores = new int[size];
        cumulScores = new int[size][10];
        finalScores = new int[size][128]; //Hardcoding a max of 128 games, bite me.
        gameNumber = 0;

        resetScores();
    }

    /**
     * markScore()
     * <p>
     * Method that marks a bowlers score on the board.
     *
     * @param Cur   The current bowler
     * @param frame The frame that bowler is on
     * @param ball  The ball the bowler is on
     * @param score The bowler's score
     */
    private void markScore(final Bowler Cur, final int frame, final int ball, final int score) {
        final int[] curScore;
        final int index = ((frame - 1) * 2 + ball);

        curScore = (int[]) scores.get(Cur);


        curScore[index - 1] = score;
        scores.put(Cur, curScore);
        getScore(Cur, frame);
        final LaneEvent event = lanePublish();
        publish(event);
    }

    /**
     * lanePublish()
     * <p>
     * Method that creates and returns a newly created laneEvent
     *
     * @return The new lane event
     */
    private LaneEvent lanePublish() {
        return new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores,
                frameNumber + 1, curScores, ball, gameIsHalted);
    }

    public void resetCumulAtBowlIndex() {
        for (int i = 0; i != 10; i++) {
            cumulScores[bowlIndex][i] = 0;
        }
    }

    public int getCurrent(final int frame) {
        return 2 * (frame - 1) + ball - 1;
    }

    public boolean wasSpare(final int frameChance, final int[] curScore, final int current) {
        assert frameChance >= 0;

        return frameChance % 2 == 1 && curScore[frameChance - 1] + curScore[frameChance] == 10 && frameChance < Math.min(current - 1, 19);
    }

    /**
     * getScore()
     * <p>
     * Method that calculates a bowlers score
     *
     * @param currentBowler   The bowler that is currently up
     * @param frame The frame the current bowler is on
     */
    private void getScore(final Bowler currentBowler, final int frame) {
        final int[] curScore = (int[]) scores.get(currentBowler);
        resetCumulAtBowlIndex();

        final int current = getCurrent(frame);

        //Iterate through each ball until the current one.
        for (int frameChance = 0; frameChance < current + 2; frameChance++) {
            if (wasSpare(frameChance, curScore, current)) {
                getScoreSpare(frameChance, curScore);
            } else if (frameChance < Math.min(current, 18) && frameChance % 2 == 0 && curScore[frameChance] == 10) {
                if (getScoreSubCase2(frameChance, curScore) != 2) break;
            } else {
                getScoreSubCase3(frameChance, curScore);
            }
        }
    }

    private void getScoreSpare(int i, int[] curScore) {
        // This ball was a the second of a spare.
        // Also, we're not on the current ball.
        // Add the next ball to the ith one in cumul.
        cumulScores[bowlIndex][(i / 2)] += curScore[i + 1] + curScore[i];
    }

    private int getScoreSubCase2(int i, int[] curScore) {
        int strikeBalls = 0;
        //This ball is the first ball, and was a strike.
        //If we can get 2 balls after it, good add them to cumul.
        if (curScore[i + 2] != -1) {
            strikeBalls = 1;
            if (curScore[i + 3] != -1) {
                //Still got em.
                strikeBalls = 2;
            } else if (curScore[i + 4] != -1) {
                //Ok, got it.
                strikeBalls = 2;
            }
        }
        if (strikeBalls == 2) {
            //Add up the strike.
            //Add the next two balls to the current cumulscore.
            cumulScores[bowlIndex][i / 2] += 10;
            if (curScore[i + 1] != -1) {
                cumulScores[bowlIndex][i / 2] += curScore[i + 1] + cumulScores[bowlIndex][(i / 2) - 1];
                if (curScore[i + 2] != -1) {
                    if (curScore[i + 2] != -2) {
                        cumulScores[bowlIndex][(i / 2)] += curScore[i + 2];
                    }
                } else {
                    if (curScore[i + 3] != -2) {
                        cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
                    }
                }
            } else {
                if (i / 2 > 0) {
                    cumulScores[bowlIndex][i / 2] += curScore[i + 2] + cumulScores[bowlIndex][(i / 2) - 1];
                } else {
                    cumulScores[bowlIndex][i / 2] += curScore[i + 2];
                }
                if (curScore[i + 3] != -1) {
                    if (curScore[i + 3] != -2) {
                        cumulScores[bowlIndex][(i / 2)] += curScore[i + 3];
                    }
                } else {
                    cumulScores[bowlIndex][(i / 2)] += curScore[i + 4];
                }
            }
        }
        return strikeBalls;
    }

    private void getScoreSubCase3(int i, int[] curScore) {
        //We're dealing with a normal throw, add it and be on our way.
        if (i % 2 == 0 && i < 18) {
            if (i / 2 == 0) {
                //First frame, first ball.  Set his cumul score to the first ball
                if (curScore[i] != -2) {
                    cumulScores[bowlIndex][i / 2] += curScore[i];
                }
            } else {
                //add his last frame's cumul to this ball, make it this frame's cumul.
                if (curScore[i] != -2) {
                    cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1] + curScore[i];
                } else {
                    cumulScores[bowlIndex][i / 2] += cumulScores[bowlIndex][i / 2 - 1];
                }
            }
        } else if (i < 18) {
            if (curScore[i] != -1 && i > 2) {
                if (curScore[i] != -2) {
                    cumulScores[bowlIndex][i / 2] += curScore[i];
                }
            }
        }
        if (i / 2 == 9) {
            if (i == 18) {
                cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];
            }
            if (curScore[i] != -2) {
                cumulScores[bowlIndex][9] += curScore[i];
            }
        } else if (i / 2 == 10) {
            if (curScore[i] != -2) {
                cumulScores[bowlIndex][9] += curScore[i];
            }
        }
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
     * @param adding Observer that is to be added
     */

    public void subscribe(final LaneObserver adding) {
        subscribers.add(adding);
    }

    /**
     * unsubscribe
     * <p>
     * Method that unsubscribes an observer from this object
     *
     * @param removing The observer to be removed
     */

    public void unsubscribe(final LaneObserver removing) {
        subscribers.remove(removing);
    }

    /**
     * publish
     * <p>
     * Method that publishes an event to subscribers
     *
     * @param event Event that is to be published
     */

    private void publish(final LaneEvent event) {
        if (subscribers.size() > 0) {

            for (final LaneObserver subscriber : subscribers) {
                (subscriber).receiveLaneEvent(event);
            }
        }
    }

    /**
     * Accessor to get this Lane's pinsetter
     *
     * @return A reference to this lane's pinsetter
     */

    public Pinsetter getPinsetter() {
        return setter;
    }

    /**
     * Pause the execution of this game
     */
    void pauseGame() {
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
