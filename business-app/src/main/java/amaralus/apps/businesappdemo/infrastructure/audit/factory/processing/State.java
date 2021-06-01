package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

public abstract class State {

    protected StateMachine stateMachine;

    abstract void execute();

    void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }
}
