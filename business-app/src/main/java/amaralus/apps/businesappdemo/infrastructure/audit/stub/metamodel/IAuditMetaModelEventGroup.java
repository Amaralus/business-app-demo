package amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel;

import java.util.List;

public interface IAuditMetaModelEventGroup extends IAuditLocalizedEntry {

    List<IAuditMetaModelEvent> getEvents();
}
