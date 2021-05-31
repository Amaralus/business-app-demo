package amaralus.apps.businesappdemo.infrastructure.audit.factory.processing;

import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class FieldProcessingStrategy extends State {

    protected Map<String, Object> params = new HashMap<>();
    protected String paramNamePrefix;

    protected Object extractData(FieldMetadata fieldMetadata, Object targetObject) {
        try {
            if (fieldMetadata.getGetterMethod() != null)
                return fieldMetadata.getGetterMethod().invoke(targetObject);
            else {
                var field = fieldMetadata.getEntityClass().getDeclaredField(fieldMetadata.getName());
                field.setAccessible(true);
                return field.get(targetObject);
            }
        } catch (Exception e) {
            log.warn("Unsuccessful data extraction from field [" + fieldMetadata.getName() + "]", e);
            return "DATA EXTRACTION ERROR";
        }
    }

    protected void addParam(String name, Object value) {
        params.put(updateName(name), value);
    }

    protected void returnParams() {
        stateMachine.removeState();

        var currentState = stateMachine.getCurrent();
        if (currentState != null)
            ((FieldProcessingStrategy) currentState).addParams(params);
    }

    private String updateName(String name) {
        return paramNamePrefix != null? paramNamePrefix + " | " + name : name;
    }

    public void addParams(Map<String , Object> params) {
        this.params.putAll(params);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParamNamePrefix(String paramNamePrefix) {
        this.paramNamePrefix = paramNamePrefix;
    }
}
