package amaralus.apps.businesappdemo.datasource.models;

import java.io.Serializable;

public interface Cleanable<M extends AbstractModel<? extends Serializable>> {

    M clearDeleted();
}
