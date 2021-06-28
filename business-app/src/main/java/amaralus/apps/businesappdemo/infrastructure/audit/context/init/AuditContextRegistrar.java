package amaralus.apps.businesappdemo.infrastructure.audit.context.init;

import amaralus.apps.businesappdemo.infrastructure.audit.EnableAuditEntityManagement;
import amaralus.apps.businesappdemo.infrastructure.audit.context.EntitySupportedLocalAuditContext;
import amaralus.apps.businesappdemo.infrastructure.audit.context.metamodel.AuditMetamodel;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.CreateEntityEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.DeleteEntityEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.EventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.factory.UpdateEntityEventFactory;
import amaralus.apps.businesappdemo.infrastructure.audit.metadata.EntityMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardClassMetadata;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
public class AuditContextRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("Loading audit entities metadata");
        var contextLoader = new AuditContextLoader(getPackageToScan(importingClassMetadata));
        contextLoader.loadAuditContextMetadata();

        var beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(EntitySupportedLocalAuditContext.class);
        beanDefinition.setPrimary(true);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, contextLoader.getEntitiesMetadata());

        registry.registerBeanDefinition(StringUtils.uncapitalize(EntitySupportedLocalAuditContext.class.getSimpleName()), beanDefinition);

        registerFactory(registry, CreateEntityEventFactory.class);
        registerFactory(registry, UpdateEntityEventFactory.class);
        registerFactory(registry, DeleteEntityEventFactory.class);

        registerMetamodel(registry, contextLoader.getEntitiesMetadata());
    }

    private void registerMetamodel(BeanDefinitionRegistry registry, Map<Class<?>, EntityMetadata> metadataMap) {
        var beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AuditMetamodel.class);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, metadataMap.values());

        registry.registerBeanDefinition(StringUtils.uncapitalize(AuditMetamodel.class.getSimpleName()), beanDefinition);
    }

    private void registerFactory(BeanDefinitionRegistry registry, Class<? extends EventFactory> factoryClass) {
        var factoryDefinition = new GenericBeanDefinition();

        factoryDefinition.setBeanClass(factoryClass);

        registry.registerBeanDefinition(StringUtils.uncapitalize(factoryClass.getSimpleName()), factoryDefinition);
    }

    private String getPackageToScan(AnnotationMetadata importingClassMetadata) {
        var basePackage = ((StandardClassMetadata) importingClassMetadata).getIntrospectedClass().getPackage().getName();
        var packageToScan = importingClassMetadata.getAnnotations().get(EnableAuditEntityManagement.class).getString("packageToScan");

        return packageToScan.isEmpty() ? basePackage : packageToScan;
    }
}
