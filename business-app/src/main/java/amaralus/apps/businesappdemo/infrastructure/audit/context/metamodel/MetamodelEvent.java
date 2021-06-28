package amaralus.apps.businesappdemo.infrastructure.audit.context.metamodel;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEventParam;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class MetamodelEvent implements IAuditMetaModelEvent {

    private final String code;
    private final List<IAuditMetaModelEventParam> params;

    public MetamodelEvent(String code) {
        this.code = code;
        params = new ArrayList<>();
    }

    public void addParam(IAuditMetaModelEventParam param) {
        params.add(param);
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
    public String getCategory() {
        return null;
    }

    @Override
    public List<IAuditMetaModelEventParam> getParams() {
        return params;
    }
}
