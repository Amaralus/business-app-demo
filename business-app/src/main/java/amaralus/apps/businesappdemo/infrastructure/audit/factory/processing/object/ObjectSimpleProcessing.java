package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;

public class ObjectSimpleProcessing extends ObjectProcessing {

    public ObjectSimpleProcessing() {
        super(null, null);
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("State machine execution is unsupported!");
    }

    public Object process(FieldMetadata fieldMetadata, Object entity) {
        return wrapNull(extractData(fieldMetadata, entity));
    }
}
