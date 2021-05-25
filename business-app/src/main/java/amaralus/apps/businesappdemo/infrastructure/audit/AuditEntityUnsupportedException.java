package amaralus.apps.businesappdemo.infrastructure.audit;

public class AuditEntityUnsupportedException extends UnsupportedOperationException {

    public AuditEntityUnsupportedException() {
        super("Audit entities processing is unsupported!");
    }
}
