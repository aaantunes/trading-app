package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class QuoteDao extends JdbcCrudDao<Quote, String> {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);

    private final String TABLE_NAME = "quote";
    private final String ID_NAME = "ticker";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public QuoteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
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

    @Override
    public Quote save(Quote quote) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
        int row = getSimpleInsert().execute(parameterSource);
        if (row != 1) {
            throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
        }
        return quote;
    }

    public void update(List<Quote> quotes) {
        if (quotes.isEmpty()) {
            throw new IllegalArgumentException("Can't pass an empty quoteList");
        }
        String sql = "UPDATE " + TABLE_NAME + " SET last_price=?, bid_price=?, bid_size=?," +
                " ask_price=?, ask_size=? WHERE ticker=?";
        List<Object[]> batch = new ArrayList<>();
        quotes.forEach(quote -> {
            if (!existsById(quote.getTicker())) {
                throw new ResourceNotFoundException("Ticker not found:" + quote.getTicker());
            }
            Object[] values = new Object[]{quote.getLastPrice(), quote.getBidPrice(), quote.getBidSize(),
                    quote.getAskPrice(), quote.getAskSize(), quote.getTicker()};
            batch.add(values);
        });
        int[] rows = jdbcTemplate.batchUpdate(sql, batch);
        int totalRows = Arrays.stream(rows).sum();
        if (totalRows != quotes.size()) {
            throw new IncorrectResultSizeDataAccessException("Number of rows ", quotes.size(), totalRows);
        }
    }

    //make sure super.findAll works
//    public List<Quote> findAll() {
//        String sql = "SELECT * FROM " + TABLE_NAME;
//        List<Quote> quotes = jdbcTemplate
//                .query(sql, BeanPropertyRowMapper.newInstance(Quote.class));
//        return quotes;
//    }
}
