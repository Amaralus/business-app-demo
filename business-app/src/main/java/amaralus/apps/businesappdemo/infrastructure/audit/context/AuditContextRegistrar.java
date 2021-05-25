package amaralus.apps.businesappdemo.infrastructure.audit.context;

import amaralus.apps.businesappdemo.infrastructure.audit.EnableAuditManagement;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.CreateEntityEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.DeleteEntityEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.UpdateEntityEventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardClassMetadata;
import org.springframework.util.StringUtils;

@Slf4j
public class AuditContextRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("Loading audit metadata");
        var contextLoader = new AuditContextLoader(getPackageToScan(importingClassMetadata));
        contextLoader.loadAuditContextMetadata();

        var beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(EntitySupportedAuditContext.class);
        beanDefinition.setPrimary(true);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, contextLoader.getEntitiesMetadata());

        registry.registerBeanDefinition(StringUtils.uncapitalize(EntitySupportedAuditContext.class.getSimpleName()), beanDefinition);

        registerFactory(registry, CreateEntityEventFactory.class);
        registerFactory(registry, UpdateEntityEventFactory.class);
        registerFactory(registry, DeleteEntityEventFactory.class);
    }

    private void registerFactory(BeanDefinitionRegistry registry, Class<? extends EventFactory> factoryClass) {
        var factoryDefinition = new GenericBeanDefinition();

        factoryDefinition.setBeanClass(factoryClass);

        registry.registerBeanDefinition(StringUtils.uncapitalize(factoryClass.getSimpleName()), factoryDefinition);
    }

    private String getPackageToScan(AnnotationMetadata importingClassMetadata) {
        var basePackage = ((StandardClassMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName();
        var packageToScan = importingClassMetadata.getAnnotations().get(EnableAuditManagement.class).getString("packageToScan");

        return packageToScan.isEmpty() ? basePackage : packageToScan;
    }
}
