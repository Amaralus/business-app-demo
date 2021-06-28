package amaralus.apps.businesappdemo.infrastructure.audit.stub.metamodel;

public interface IAuditMetaModelEventParam extends IAuditLocalizedEntry {

    boolean isMandatory();

    Integer getOrder();
}
