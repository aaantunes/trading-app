package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MarketDataDaoTest {

    @Test
    public void test() {
        MarketDataConfig config = new MarketDataConfig();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        MarketDataDao dao = new MarketDataDao(cm, config);

        List<String> tickers = new ArrayList<>();
        tickers.add("AAPL");
        tickers.add("TSLA");

        dao.findIexQuoteByTicker(tickers);
    }


}