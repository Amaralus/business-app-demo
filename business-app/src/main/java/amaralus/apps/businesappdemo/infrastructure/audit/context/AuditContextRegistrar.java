package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.EnableAuditManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardClassMetadata;

@Slf4j
public class AuditContextRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("Loading audit metadata");
        var contextLoader = new AuditContextLoader(getPackageToScan(importingClassMetadata));
        contextLoader.loadAuditContextMetadata();

        var beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AuditContext.class);
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(contextLoader.getEntitiesMetadata());

        registry.registerBeanDefinition("auditContext", beanDefinition);
    }

    private String getPackageToScan(AnnotationMetadata importingClassMetadata) {
        var basePackage = ((StandardClassMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName();
        var packageToScan = importingClassMetadata.getAnnotations().get(EnableAuditManagement.class).getString("packageToScan");

        return packageToScan.isEmpty() ? basePackage : packageToScan;
    }
}
