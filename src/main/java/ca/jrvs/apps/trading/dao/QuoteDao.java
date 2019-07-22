package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuoteDao extends JdbcCrudDao<Quote, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);

    private final String TABLE_NAME = "quote";
    private final String ID_NAME = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public QuoteDao(DataSource dataSource) {
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
        return Quote.class;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleInsert() {
        return simpleInsert;
    }

    /**
     * Updates a single quote by passing in a singleton List<Quote>
     * @param quote
     */
    //TODO: Ask Edward why we pass a List of quotes if we use Collections.Singleton in quote controller
    public void update(List<Quote> quote){
        if (quote.isEmpty()){
            throw new IllegalArgumentException("Can't pass an empty quote");
        }
        String sql = "UPDATE " + TABLE_NAME + " SET last_price=?, bid_price=?, bid_size=?," +
                " ask_price=?, ask_size=? WHERE ticker=?";
        List<Object[]> batch = new ArrayList<>();

    }

    public List<Quote> findAll(){
        String sql = "SELECT * FROM " + TABLE_NAME;
        List<Quote> quotes = jdbcTemplate
                .query(sql, BeanPropertyRowMapper.newInstance(Quote.class));
        return quotes;
    }
}
