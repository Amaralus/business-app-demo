package amaralus.apps.businesappdemo.datasource.models.link;

import amaralus.apps.businesappdemo.datasource.IdGenerator;
import amaralus.apps.businesappdemo.datasource.models.AbstractModel;
import amaralus.apps.businesappdemo.datasource.models.CompositeKey;

import java.util.HashSet;
import java.util.Set;

public interface ManyToManyLinkedModel {

    default <E extends AbstractModel<String> & CompositeKey> void replaceLinks(Set<E> oldLinks, Set<E> newLinks) {
        oldLinks.forEach(AbstractModel::delete);
        newLinks.forEach(IdGenerator::generateId);
        // хэшсет не обновляет хэшкод после foeEach и после генерации айдишников хэшкоды сущностей и в хэштаблице не будут совпадать
        newLinks = new HashSet<>(newLinks);

        // чтобы persistence context контекст не ругался на добавление сущетвующей сущности
        // нужно убрать из новых существующие и восстановить старык на случай если они удалены
        // в итоге будут добавлятся только новые сущноти
        for (var oldLink : oldLinks)
            for (var newLink : new HashSet<>(newLinks))
                if (oldLink.getId().equals(newLink.getId())) {
                    newLinks.remove(newLink);
                    oldLink.restore();
                }

        oldLinks.addAll(newLinks);
    }
}
