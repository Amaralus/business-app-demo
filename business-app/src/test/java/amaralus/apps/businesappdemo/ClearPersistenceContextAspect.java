package amaralus.apps.businesappdemo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Aspect
@Component
@Slf4j
// нужен только для адекватного тестирования
public class ClearPersistenceContextAspect {

    private final EntityManager entityManager;

    public ClearPersistenceContextAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @After("execution(* org.springframework.data.jpa.repository.JpaRepository.*(..))")
    public void afterSave(JoinPoint jp) {
        try {
            log.debug("clearing persistence context after {}()...", jp.getSignature().getName());
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            log.error("Clearing persistence context error", e);
        }
    }
}
