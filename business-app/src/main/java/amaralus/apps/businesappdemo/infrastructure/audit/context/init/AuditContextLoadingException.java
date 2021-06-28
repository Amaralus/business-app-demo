package amaralus.apps.businesappdemo.infrastructure.audit.context.init;

public class AuditContextLoadingException extends RuntimeException {

    public AuditContextLoadingException(Throwable cause) {
        super("Audit context loading failed", cause);
    }
}
