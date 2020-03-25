interface GeneralPinsetter {
    void ballThrown();

    void resetState();

    void subscribe(PinsetterObserver subscriber);
}
