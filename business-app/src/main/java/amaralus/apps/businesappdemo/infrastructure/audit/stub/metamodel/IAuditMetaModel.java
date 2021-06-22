package amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel;

import java.util.List;

public interface IAuditMetaModel {

    List<IAuditMetaModelEventGroup> getGroups();
}
