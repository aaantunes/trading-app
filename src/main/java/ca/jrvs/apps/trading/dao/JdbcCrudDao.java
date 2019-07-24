package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class JdbcCrudDao<E extends Entity, ID> implements CrudRepository<E, ID> {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

    abstract public String getTableName();

    abstract public String getIdName();

    abstract Class getEntityClass();

    abstract public JdbcTemplate getJdbcTemplate();

    abstract public SimpleJdbcInsert getSimpleInsert();

    @SuppressWarnings("unchecked")
    @Override
    public E save(E entity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        Number newId = getSimpleInsert().executeAndReturnKey(parameterSource);
        entity.setId(newId.intValue());
        return entity;
    }

    @Override
    public E findById(ID id) {
        return findById(getIdName(), id, false, getEntityClass());
    }

    public E findByIdForUpdate(ID id) {
        return findById(getIdName(), id, true, getEntityClass());
    }

    @SuppressWarnings("unchecked")
    public E findById(String idName, ID id, boolean forUpdate, Class clazz) {
        E t = null;
        String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + idName + " =?";

        //handle reaad + update race condition
        if (forUpdate) {
            selectSql += " for update";
        }
        logger.info(selectSql);

        try {
            t = (E) getJdbcTemplate()
                    .queryForObject(selectSql, BeanPropertyRowMapper.newInstance(clazz), id);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("Can't find trader id:" + id, e);
        }

        if (t == null) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public List<E> findAll() {
        String sql = "SELECT * FROM " + getTableName();
        return getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(getEntityClass()));
    }

    @Override
    public boolean existsById(ID id) {
        return existsById(getIdName(), id);
    }

    public boolean existsById(String idName, ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        String selectSql = "SELECT count(*) FROM " + getTableName() + " WHERE " + idName + " =?";
        logger.info("SQL Statement: " + selectSql);

        Integer count = getJdbcTemplate()
                .queryForObject(selectSql, Integer.class, id);
        return count != 0;
    }

    @Override
    public void deleteById(ID id) {
        deleteById(getIdName(), id);
    }

    public void deleteById(String idName, ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }

        String deleteSql = "DELETE FROM " + getTableName() + " WHERE " + idName + "=?";
        logger.info(deleteSql);
        getJdbcTemplate().update(deleteSql, id);
    }

}
