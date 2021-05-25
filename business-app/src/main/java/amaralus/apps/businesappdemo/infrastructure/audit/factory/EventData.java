package amaralus.apps.businesappdemo.infrastructure.audit.factory;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class EventData {

    private boolean success;
    private String groupCode;
    private String eventCode;
    private Map<String, String> params = new HashMap<>();
    private Object oldAuditEntity;
    private Object newAuditEntity;
    private EntityMetadata entityMetadata;

    public void addParam(String name, String value) {
        params.put(name, value);
    }
}
