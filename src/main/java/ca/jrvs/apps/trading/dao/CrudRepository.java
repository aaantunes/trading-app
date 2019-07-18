package ca.jrvs.apps.trading.dao;

public interface CrudRepository<T, Integer> {
    T save(T entity);

    T findById(Integer id);

    boolean existsById(Integer id);

    void deleteById(Integer id);
}
