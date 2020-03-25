// this handles all the internal mechanics of a game happening on a Lane
class Game {

    private final int numberOfBowlers;
    private boolean halted;
    private boolean finished;
    private int currBowlerIndex;
    private int frameNumber;
    private int number;

    Game(final int partySize) {
        numberOfBowlers = partySize;
        currBowlerIndex = 0;
        number = 0;
        finished = false;
        halted = false;
        frameNumber = 0;
    }

    final void restartGame() {
        currBowlerIndex = 0;
        frameNumber = 0;
    }

    final void nextBowler() {
        currBowlerIndex++;
        if (currBowlerIndex == numberOfBowlers) {
            frameNumber++;
            currBowlerIndex = 0;
            if (frameNumber == Lane.FRAME_COUNT) {
                finished = true;
                number++;
            }
        }
    }

    final boolean isFinished() {
        return finished;
    }

    final boolean isHalted() {
        return halted;
    }

    final int getNumber() {
        return number;
    }

    final void pause() {
        halted = true;
    }

    final void unpause() {
        halted = false;
    }

    final int currentBowler() {
        return currBowlerIndex;
    }

    final int currentFrame() {
        return frameNumber;
    }

    final boolean isLastFrame() {
        return frameNumber == Lane.LAST_FRAME;
    }
}
