import java.util.List;
import java.util.Vector;

abstract class Publisher {
    private final List<Observer> subscribers;
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
