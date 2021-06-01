package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class ObjectDiffProcessingStrategy extends ObjectProcessingStrategy {

    private final Object oldEntity;

    public ObjectDiffProcessingStrategy(FieldMetadata fieldMetadata, Object oldEntity, Object entity) {
        super(fieldMetadata, entity);
        this.oldEntity = oldEntity;
    }

    @Override
    public void execute() {
        var diff = getDiff(extractData(fieldMetadata, oldEntity), extractData(fieldMetadata, entity));

        if (diff != null) {
            addParam(fieldMetadata.getParamName(), diff);
            returnParams();
        } else if (fieldMetadata.isIdField())
            // айдишник нужно показать всегда
            super.execute();
        else
            // обычные поля пропускаются
            stateMachine.removeState();
    }

    private String getDiff(Object oldValue, Object newValue) {
        if (Objects.equals(oldValue, newValue))
            return null;
        else
            return oldValue + " -> " + newValue;
    }
}
