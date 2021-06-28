package amaralus.apps.businesappdemo.infrastructure.audit;

import java.util.Arrays;
import java.util.List;

public enum SimpleAuditEvent {
    EXAMPLE_EVENT("OTHER", "EXAMPLE_EVENT"),
    EXAMPLE_EVENT_WITH_PARAMS("OTHER", "EXAMPLE_EVENT_WITH_PARAMS", "Param1", "Param2");

    private final String groupCode;
    private final String eventCode;
    private final List<String> params;

    SimpleAuditEvent(String groupCode, String eventCode, String... params) {
        this.groupCode = groupCode;
        this.eventCode = eventCode;
        this.params = Arrays.asList(params);
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getEventCode() {
        return eventCode;
    }

    public List<String> getParams() {
        return params;
    }

    public String getParam(int paramNumber) {
        try {
            return params.get(paramNumber);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Parameter with number " + paramNumber + "doesn't exist!");
        }
    }

    public String firstParam() {
        return getParam(0);
    }

    public String secondParam() {
        return getParam(1);
    }

    public String thirdParam() {
        return getParam(2);
    }
}
