package amaralus.apps.businesappdemo.infrastructure.audit.stub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditLibraryServiceStub {

    public void send(AuditLibraryEvent auditLibraryEvent) throws AuditSendException {
        log.debug("Отправка события аудита: {}", auditLibraryEvent);
    }
}
