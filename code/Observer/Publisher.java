package Observer;

import java.util.List;
import java.util.Vector;

public abstract class Publisher {
    private final List<Observer> subscribers;

    public Publisher(){
        subscribers = new Vector<>(0);
    }

    public abstract Event createEvent();

    public void subscribe(final Observer observer) {
        subscribers.add(observer);
    }

    public void publish() {
        final Event event = createEvent();
        for (final Observer subscriber : subscribers) {
            subscriber.receiveEvent(event);
        }
    }
}
