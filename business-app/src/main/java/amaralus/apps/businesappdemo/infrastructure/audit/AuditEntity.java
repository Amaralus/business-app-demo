package amaralus.apps.businesappdemo.infrastructure.audit;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface AuditEntity {

    String groupCode() default "";

    int walkDepth() default 1;
}
