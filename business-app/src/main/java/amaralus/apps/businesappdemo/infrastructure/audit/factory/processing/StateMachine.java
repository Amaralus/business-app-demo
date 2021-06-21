package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class StateMachine {

    private final Deque<State> stateStack = new ArrayDeque<>();

    public void addState(State state) {
        if (state != null) {
            state.setStateMachine(this);
            stateStack.push(state);
        }
    }

    public State getCurrent() {
        try {
            return stateStack.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void removeState() {
        stateStack.pop();
    }

    public void executeAll() {
        while (!stateStack.isEmpty()) {
            getCurrent().execute();
        }
    }
}
