package amaralus.apps.businesappdemo.services;

import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReplicationService {

    public <D extends AbstractModel<?>> void replicate(D dao) {
        log.info("Выполняется репликация для таблицы [{}] id=[{}]", dao.getTableName(), dao.getId());
    }
}
