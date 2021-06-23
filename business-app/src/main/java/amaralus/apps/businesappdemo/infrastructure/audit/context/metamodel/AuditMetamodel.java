package amaralus.apps.businesappdemo.infrastructure.audit.context.metamodel;

import amaralus.apps.businesappdemo.infrastructure.audit.SimpleAuditEvent;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.FieldMetadata;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModel;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEventGroup;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel.IAuditMetaModelEventParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static amaralus.apps.businesappdemo.infrastructure.audit.metadata.MetadataUtil.getEventCode;

@Slf4j
public class AuditMetamodel implements IAuditMetaModel {

    private final List<IAuditMetaModelEventGroup> eventGroups;

    public AuditMetamodel(Set<EntityMetadata> entityMetadata) {
        eventGroups = createGroups(entityMetadata);
        printMetamodel();
    }

    private List<IAuditMetaModelEventGroup> createGroups(Collection<EntityMetadata> entitiesMetadata) {
        var groups = new HashMap<String, MetamodelEventGroup>();

        for (var entityMetadata : entitiesMetadata) {
            var group = groups.computeIfAbsent(entityMetadata.getGroupCode(), MetamodelEventGroup::new);
            group.addEvent(createEvent("create", entityMetadata));
            group.addEvent(createEvent("update", entityMetadata));
            group.addEvent(createEvent("delete", entityMetadata));
        }

        for (var auditEvent : SimpleAuditEvent.values()) {
            var group = groups.computeIfAbsent(auditEvent.getGroupCode(), MetamodelEventGroup::new);
            group.addEvent(createEvent(auditEvent));
        }

        return new ArrayList<>(groups.values());
    }

    private MetamodelEvent createEvent(String codePrefix, EntityMetadata entityMetadata) {
        var event = new MetamodelEvent(getEventCode(codePrefix, entityMetadata.getEntityClass()));

        if (codePrefix.equals("delete"))
            event.addParam(createParam(entityMetadata.getIdFieldMetadata()));
        else
            for (var fieldMetadata : entityMetadata.getFieldsMetadata())
                event.addParam(createParam(fieldMetadata));

        return event;
    }

    private MetamodelEvent createEvent(SimpleAuditEvent auditEvent) {
        var event = new MetamodelEvent(auditEvent.getEventCode());

        for (var param : auditEvent.getParams())
            event.addParam(new MetamodelEventParam(param, false));

        return event;
    }

    private IAuditMetaModelEventParam createParam(FieldMetadata fieldMetadata) {
        return new MetamodelEventParam(fieldMetadata.getParamName(), fieldMetadata.isMandatory());
    }

    private void printMetamodel() {
        try {
            if (log.isDebugEnabled()) {
                var json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
                log.debug("Метамодель аудита в json формате:\n{}", json);
            }
        } catch (JsonProcessingException e) {
            log.warn("Неудалось отобразить метамодель аудита!", e);
        }
    }

    @Override
    public List<IAuditMetaModelEventGroup> getGroups() {
        return eventGroups;
    }
}
