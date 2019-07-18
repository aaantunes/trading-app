package ca.jrvs.apps.trading.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao {

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);

    @Autowired
    public QuoteDao() {

    }
}
