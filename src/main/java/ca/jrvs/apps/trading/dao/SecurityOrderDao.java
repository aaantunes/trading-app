package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(SecurityOrderDao.class);

    private final String TABLE_NAME = "security_order";
    private final String ID_NAME = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public SecurityOrderDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_NAME);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdName() {
        return ID_NAME;
    }

    @Override
    Class getEntityClass() {
        return SecurityOrder.class;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleInsert() {
        return simpleInsert;
    }

    //TODO: may need to write specific overriden methods
}
