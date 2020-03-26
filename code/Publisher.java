import java.util.Vector;

abstract class Publisher {
    Vector<Observer> subscribers;
    Publisher(){
        subscribers = new Vector<>(0);
    }

    abstract Event createEvent();

    void subscribe(final Observer observer) {
        subscribers.add(observer);
    }

    void publish() {
        final Event event = createEvent();
        for (final Observer subscriber : subscribers) {
            subscriber.receiveEvent(event);
        }
    }
}
