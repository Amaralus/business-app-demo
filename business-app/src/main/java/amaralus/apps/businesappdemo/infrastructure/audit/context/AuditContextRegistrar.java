package amaralus.apps.businesappdemo.infrastructure.audit.context;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardClassMetadata;

public class AuditContextRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        var basePackage = ((StandardClassMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName();
        var contextLoader = new AuditContextLoader(basePackage);
        contextLoader.loadAuditContextMetadata();

        var beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AuditContext.class);

        registry.registerBeanDefinition("auditContext", beanDefinition);
    }
}
