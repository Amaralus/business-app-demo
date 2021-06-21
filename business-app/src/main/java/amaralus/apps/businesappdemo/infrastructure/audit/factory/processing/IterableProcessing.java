package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import java.util.Iterator;
import java.util.function.Supplier;

public abstract class IterableProcessing<O> extends ProcessingStrategy {

    private final Iterator<O> iterator;

    protected IterableProcessing(Supplier<Iterator<O>> iteratorSupplier) {
        this.iterator = iteratorSupplier.get();
    }

    protected int currentIterationNumber;

    @Override
    public void execute() {
        if (iterator.hasNext()) {
            stateMachine.addState(getNextState(iterator.next()));
            ++currentIterationNumber;
        } else
            onIterationEnd();
    }

    protected abstract State getNextState(O object);

    protected void onIterationEnd() {
        returnParams();
    }
}
