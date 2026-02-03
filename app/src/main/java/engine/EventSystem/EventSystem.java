package engine.EventSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventSystem {
    public static class Event<T>{
        private final List<EventAction<T>> listeners = new ArrayList<>();

        public EventAction<T> connect(Consumer<T> cons) { // adds a function to this system wich allows connections of events
            EventAction<T> action = new EventAction<T>(this, cons);
            listeners.add(action);
            return action;
        }

        public void fire(T Payload) { // Fires all functions inside of the listeners list with a given payload
            for (EventAction<T> eventAction : listeners) {
                if (eventAction.disconnected == true) {
                    remove(eventAction);
                    continue;
                }

                eventAction.func.accept(Payload);
            }
        }

        public void fireNDC(T Payload) { // For those who are FPS maxing and dont want a single milisecond wasted
            for (EventAction<T> eventAction : listeners) {
                eventAction.func.accept(Payload);
            }
        }

        public void remove(EventAction<T> event) { // trys to find a given event and removes it if present
            listeners.remove(event);
        }
    }

    public static class EventAction<T> {
        private final Event<T> parent;
        private final Consumer<T> func;
        private boolean disconnected = false;

        public void disconnect() { // Removes the function from the event fire que and will never fire again
            disconnected = true;
            parent.remove(this);
        }

        private EventAction(Event<T> parent, Consumer<T> fn) {
            this.parent = parent;
            this.func = fn;
        }
    }
}
