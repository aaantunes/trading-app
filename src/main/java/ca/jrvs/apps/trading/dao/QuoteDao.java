package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuoteDao {

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);

    @Autowired
    public QuoteDao() {

    }

    public void update(){

    }

    public List<Quote> findAll(){

        return null;
    }
}
