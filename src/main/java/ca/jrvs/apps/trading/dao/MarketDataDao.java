package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import jdk.internal.loader.Resource;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MarketDataDao {

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);

    private final String BATCH_QUOTE_URL;
    private HttpClientConnectionManager httpClientConnectionManager;

    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig){
        this.httpClientConnectionManager = httpClientConnectionManager;

        BATCH_QUOTE_URL = marketDataConfig.getHost() +
                "/stock/market/batch?symbols=%s&types=quote&token=" +
                marketDataConfig.getToken();
    }

    /**
     *
     * @param tickerList
     * @throws org.springframework.dao.DataRetrievalFailureException if unable to get http response
     * @return
     */
    public List<IexQuote> findIexQuoteByTicker(List<String> tickerList){
        //convert list into comma seperated string
        URI uri = null;
        try {
            uri = getIexQuoteByTickerURI(tickerList);
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
        logger.info("Get URI: " + uri);
        //Get HTTP response body in String
        String response = executeHttpGet(uri);
        //Iex will skip invalid symbols/ticker..we need to check it
        if (IexQuotesJson.length() != tickerList.size()) {
            throw new IllegalArgumentException("Invalid ticker/symbol");
        }
        //Unmarhsal JSON object
        return iexQuotes;
    }

    protected String executeHttpGet(String url) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpGet httpGet = new HttpGet((url));
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                switch (response.getStatusLine().getStatusCode()) {
                    case 200:
                        //EntityUtils toString will also close inputStream in Entity
                        String body = EntityUtils.toString(response.getEntity());
                        return Optional.ofNullable(body).orElseThrow(
                                () -> new IOException("Unexpected empty http response body"));
                    case 404:
                        throw new ResourceNotFoundException("Not found");
                    default:
                        throw new DataRetrievalFailureException("Unexpected status:" + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Unable Http execution error", e);
        }
    }

    public IexQuote findIexQuoteByTicker(String ticker){
        List<IexQuote> quotes = findIexQuoteByTicker(Arrays.asList(ticker));
        if (quotes == null || quotes.size() != 1) {
            throw new DataRetrievalFailureException("Unable to get data");
        }
        return quotes.get(0);
    }

    private static URI getIexQuoteByTickerURI(List<String> tickerList) throws URISyntaxException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tickerList.size() - 1; i++){
            sb.append(tickerList.get(i)).append(",");
        }
        sb.append(tickerList.get(tickerList.size()));

        return new URI(sb.toString());
    }


}
