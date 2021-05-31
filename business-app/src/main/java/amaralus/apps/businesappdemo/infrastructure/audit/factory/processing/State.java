package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

public abstract class State {

    protected final StateMachine stateMachine;

    protected State(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    abstract void update();
}
