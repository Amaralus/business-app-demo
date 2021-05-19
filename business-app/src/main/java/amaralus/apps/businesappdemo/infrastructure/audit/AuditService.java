package amaralus.apps.businesappdemo.infrastructure.audit;

import amaralus.apps.businesappdemo.infrastructure.audit.context.AuditContext;
import amaralus.apps.businesappdemo.infrastructure.audit.stub.AuditLibraryServiceStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    private final AuditContext auditContext;
    private final AuditLibraryServiceStub auditLibraryServiceStub;

    public AuditService(AuditContext auditContext, AuditLibraryServiceStub auditLibraryServiceStub) {
        this.auditContext = auditContext;
        this.auditLibraryServiceStub = auditLibraryServiceStub;
    }

    public class EventBuilder {

        public void send() {
            try {
               auditLibraryServiceStub.send(null);
            } catch (Exception e) {
                log.error("Ошибка во время отправки собития аудита");
            }
        }
    }
}
