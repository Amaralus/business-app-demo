package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

public abstract class State {

    protected StateMachine stateMachine;

    abstract void update();

    void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }
}
