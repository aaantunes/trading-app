package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PositionDao {

    private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);

    private final String TABLE_NAME = "position";
    private final String ID_NAME = "account_id";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PositionDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @SuppressWarnings("unchecked")
    public List<Position> findByAccountId(Integer accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("AccountId cannot be null");
        }
        List<Position> trader = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_NAME + " =?";
        logger.info(sql);

        try {
            trader = (List<Position>) jdbcTemplate.query(sql,
                    BeanPropertyRowMapper.newInstance(Position.class), accountId);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("Can't find trader id: " + accountId, e);
        }

        if (trader == null) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return trader;
    }
}
