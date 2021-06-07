package amaralus.apps.businesappdemo.datasource.models;

import java.io.Serializable;

/**
 * Расширение для моделей, необходимо для отчистки сущностей помеченых как удаленные.
 * Так как хочется использовать стандартные средства Spring JPA нужно так же дополнительно
 * подчищать удаленные сущиности когда это надо.
 * @param <M>
 */
public interface Cleanable<M extends AbstractModel<? extends Serializable>> {

    M clearDeleted();
}
