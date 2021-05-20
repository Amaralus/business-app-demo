package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryEvent;

import java.util.Map;

public interface EventFactory {

    EventFactory success(boolean success);

    EventFactory eventGroup(String eventGroup);

    EventFactory eventCode(String eventCode);

    EventFactory params(Map<String, String> params);

    EventFactory oldAuditEntity(Object oldAuditEntity);

    EventFactory newAuditEntity(Object newAuditEntity);

    EventFactory entityMetadata(EntityMetadata entityMetadata);

    AuditLibraryEvent produce();
}
