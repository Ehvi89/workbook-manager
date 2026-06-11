package ci.gouv.guce.workbookmanager.service;

import java.util.List;

public interface CrudService <C, T> {
    C save(C d);

    C findOne(T t);

    C update(C d);

    void delete(T t);

    List<C> findAll();

    C partialUpdate(C d);

    Boolean existById(T t);
}
