package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

public class ObjectProcessingStrategy extends FieldProcessingStrategy {

    private final FieldMetadata fieldMetadata;
    private final Object entity;

    public ObjectProcessingStrategy() {
        this(null, null, null);
    }

    public ObjectProcessingStrategy(StateMachine stateMachine, FieldMetadata fieldMetadata, Object entity) {
        super(stateMachine);
        this.fieldMetadata = fieldMetadata;
        this.entity = entity;
    }

    @Override
    public void update() {
        if (stateMachine == null)
            throw new UnsupportedOperationException("state machine updating is unsupported");

        addParam(fieldMetadata.getParamName(), extractData(fieldMetadata, entity));
        stateMachine.removeState();

        var currentState = stateMachine.getCurrent();
        if (currentState != null)
            ((FieldProcessingStrategy) currentState).addParams(params);
    }

    public Object process(FieldMetadata fieldMetadata, Object entity) {
        return extractData(fieldMetadata, entity);
    }
}
