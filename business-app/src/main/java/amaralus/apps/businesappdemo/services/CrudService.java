package amaralus.apps.businesappdemo.services;

public interface CrudService<E, I> {

    E save(E entity);

    E getById(I id);

    void delete(I id);
}
