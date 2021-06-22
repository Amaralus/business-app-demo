package amaralus.apps.businesappdemo.infrastructure.audit.context.metamodel;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEventGroup;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class MetamodelEventGroup implements IAuditMetaModelEventGroup {

    private final String code;
    private final List<IAuditMetaModelEvent> events;

    public MetamodelEventGroup(String code) {
        this.code = code;
        events = new ArrayList<>();
    }

    public void addEvent(IAuditMetaModelEvent event) {
        events.add(event);
    }


    @Override
    public Map<Lang, String> getLocalization() {
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public List<IAuditMetaModelEvent> getEvents() {
        return events;
    }
}
