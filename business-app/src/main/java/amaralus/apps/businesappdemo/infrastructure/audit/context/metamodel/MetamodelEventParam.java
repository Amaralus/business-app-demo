package amaralus.apps.businesappdemo.infrastructure.audit.context.metamodel;

import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEventParam;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class MetamodelEventParam implements IAuditMetaModelEventParam {

    private final String code;
    private final boolean mandatory;

    public MetamodelEventParam(String code, boolean mandatory) {
        this.code = code;
        this.mandatory = mandatory;
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
    public boolean isMandatory() {
        return mandatory;
    }

    @Override
    public Integer getOrder() {
        return null;
    }
}
