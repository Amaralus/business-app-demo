package amaralus.apps.businesappdemo.infrastructure;

import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import amaralus.apps.businesappdemo.services.ReplicationService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Slf4j
public class ReplicationHibernateInterceptor extends EmptyInterceptor {


    private final transient ReplicationService replicationService;

    public ReplicationHibernateInterceptor(ReplicationService replicationService) {
        this.replicationService = replicationService;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        replicationIntercept(entity);
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        replicationIntercept(entity);
        super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        replicationIntercept(entity);
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }


    private void replicationIntercept(Object entity) {
        if (entity instanceof AbstractModel)
            replicationService.replicate((AbstractModel<?>) entity);
    }
}
