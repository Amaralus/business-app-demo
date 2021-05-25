package amaralus.apps.businesappdemo.infrastructure.audit;

import amaralus.apps.businesappdemo.infrastructure.audit.context.init.AuditContextRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Import(AuditContextRegistrar.class)
public @interface EnableAuditEntityManagement {

    String packageToScan() default "";
}
