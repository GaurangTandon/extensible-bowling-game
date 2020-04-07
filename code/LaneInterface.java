abstract class LaneInterface extends Publisher implements Observer {

    protected final Pinsetter pinsetter;

    abstract void pauseGame(boolean state);

    LaneInterface() {
        pinsetter = new Pinsetter();
        pinsetter.subscribe(this);
    }
    final void subscribePinsetter(final PinSetterView psv) {
        pinsetter.subscribe(psv);
    }
}
