/**
 * This class is supposed to handle all the scoring happening on a particular lane
 */
class LaneScorer {
    void resetCumulAtBowlIndex() {
        for (int i = 0; i < Pinsetter.PIN_COUNT; i++) {
            cumulScores[bowlIndex][i] = 0;
        }
    }

    int getCurrent(final int frame) {
        return 2 * (frame - 1) + ball - 1;
    }

    /**
     * getScore()
     * <p>
     * Method that calculates a bowlers score
     *
     * @param currentBowler The bowler that is currently up
     * @param frame         The frame the current bowler is on
     */
    void getScore(final Bowler currentBowler, final int frame) {
        final int[] curScore = (int[]) scores.get(currentBowler);
        resetCumulAtBowlIndex();

        final int current = getCurrent(frame);

        //Iterate through each ball until the current one.
        for (int frameChance = 0; frameChance < current + 2; frameChance++) {
            // TODO: what is the && condition?
            if (LaneUtil.wasSpare(frameChance, curScore) && frameChance < Math.min(current - 1, 19)) {
                getScoreSpare(frameChance, curScore);
            } else if (frameChance < Math.min(current, 18) && frameChance % 2 == 0 && curScore[frameChance] == 10) {
                if (getScoreSubCase2(frameChance, curScore) != 2) break;
            } else {
                getScoreSubCase3(frameChance, curScore);
            }
        }
    }

    void getScoreSpare(final int i, final int[] curScore) {
        // This ball was a the second of a spare.
        // Also, we're not on the current ball. TODO: what does this mean??
        // Add the next ball to the ith one in cumul.
        cumulScores[bowlIndex][(i / 2)] += curScore[i + 1] + curScore[i];
    }

    int getScoreSubCase2(final int i, final int[] curScore) {
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

    void getScoreSubCase3(final int i, final int[] curScore) {
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
     * markScore()
     * <p>
     * Method that marks a bowlers score on the board.
     *
     * @param Cur   The current bowler
     * @param frame The frame that bowler is on
     * @param ball  The ball the bowler is on
     * @param score The bowler's score
     */
    void markScore(final Bowler Cur, final int frame, final int ball, final int score) {
        final int index = ((frame - 1) * 2 + ball);

        final int[] curScore = (int[]) scores.get(Cur);
        curScore[index - 1] = score;

        scores.put(Cur, curScore);
        getScore(Cur, frame);
        final LaneEvent event = lanePublish();
        publish(event);
    }

    /**
     * resetScores()
     * <p>
     * resets the scoring mechanism, must be called before scoring starts
     *
     * @pre the party has been assigned
     * @post scoring system is initialized
     */
    void resetScores() {
        for (final Bowler o : party.getMembers()) {
            // TODO: 25 what?
            final int[] toPut = new int[25];
            for (int i = 0; i != 25; i++) {
                toPut[i] = -1;
            }
            scores.put(o, toPut);
        }

        gameFinished = false;
        frameNumber = 0;
    }
}
