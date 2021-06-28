package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

public abstract class State {

    protected StateMachine stateMachine;

    public abstract void execute();

    void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }
}
