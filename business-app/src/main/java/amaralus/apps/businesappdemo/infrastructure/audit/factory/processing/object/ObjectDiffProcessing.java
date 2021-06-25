package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.object;

import amaralus.apps.businesappdemo.infrastructure.audit.factory.processing.DiffProcessing;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectDiffProcessing extends ObjectProcessing implements DiffProcessing {

    private final Object oldEntity;

    public ObjectDiffProcessing(FieldMetadata fieldMetadata, Object oldEntity, Object newEntity) {
        super(fieldMetadata, newEntity);
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
}
