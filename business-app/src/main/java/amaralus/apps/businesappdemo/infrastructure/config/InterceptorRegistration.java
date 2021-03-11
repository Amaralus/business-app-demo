package amaralus.apps.businesappdemo.infrastructure.config;

import amaralus.apps.businesappdemo.infrastructure.ReplicationHibernateInterceptor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InterceptorRegistration implements HibernatePropertiesCustomizer {

    private final ReplicationHibernateInterceptor interceptor;

    public InterceptorRegistration(ReplicationHibernateInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", interceptor);
    }
}
