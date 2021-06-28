package amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel;

import java.util.List;

public interface IAuditMetaModelEvent extends IAuditLocalizedEntry {

    String getCategory();

    List<IAuditMetaModelEventParam> getParams();
}
