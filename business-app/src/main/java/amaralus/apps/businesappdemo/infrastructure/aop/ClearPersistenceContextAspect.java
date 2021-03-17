package amaralus.apps.businesappdemo.infrastructure.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Aspect
@Component
@Slf4j
public class ClearPersistenceContextAspect {

    private final EntityManager entityManager;

    public ClearPersistenceContextAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @After("execution(* org.springframework.data.jpa.repository.JpaRepository.save(..)) " +
            "|| execution(* org.springframework.data.jpa.repository.JpaRepository.saveAndFlush(..))")
    public void afterSave() {
        log.trace("clear persistence context...");
        entityManager.flush();
        entityManager.clear();
    }
}
