package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectProcessingStrategy extends FieldProcessingStrategy {

    protected final FieldMetadata fieldMetadata;
    protected final Object entity;

    public ObjectProcessingStrategy(FieldMetadata fieldMetadata, Object entity) {
        this.fieldMetadata = fieldMetadata;
        this.entity = entity;
    }

    @Override
    public void execute() {
        addParam(fieldMetadata.getParamName(), wrapNull(extractData(fieldMetadata, entity)));
        returnParams();
    }

    protected Object wrapNull(Object object) {
        return object == null ? "null": object;
    }
}
