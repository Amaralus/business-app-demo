package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectProcessingStrategy extends FieldProcessingStrategy {

    private final FieldMetadata fieldMetadata;
    private final Object entity;

    public ObjectProcessingStrategy() {
        this(null, null);
    }

    public ObjectProcessingStrategy(FieldMetadata fieldMetadata, Object entity) {
        this.fieldMetadata = fieldMetadata;
        this.entity = entity;
    }

    @Override
    public void update() {
        if (stateMachine == null)
            throw new UnsupportedOperationException("state machine updating is unsupported");

        addParam(fieldMetadata.getParamName(), wrapNull(extractData(fieldMetadata, entity)));
        stateMachine.removeState();

        var currentState = stateMachine.getCurrent();
        if (currentState != null)
            ((FieldProcessingStrategy) currentState).addParams(params);
    }

    public Object process(FieldMetadata fieldMetadata, Object entity) {
        return wrapNull(extractData(fieldMetadata, entity));
    }

    private Object wrapNull(Object object) {
        return object == null ? "null": object;
    }
}
