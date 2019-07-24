package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class QuoteService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    /**
     * Helper method. Map a IexQuote to a Quote entity.
     * Note: `iexQuote.getLatestPrice() == null` if the stock market is closed.
     * Make sure set a default value for number field(s).
     */
    public static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        //TODO: Implement buildQuoteFromIexQuote()
        if (iexQuote == null) {
            throw new IllegalArgumentException("Must pass iexQuote into buildQuoteFromIexQuote");
        }
        Quote quote = new Quote();
        quote.setAskPrice(Double.parseDouble(iexQuote.getIexAskPrice()));
        quote.setAskSize(Integer.parseInt(iexQuote.getIexAskSize()));
        quote.setBidPrice(Double.parseDouble(iexQuote.getIexBidPrice()));
        quote.setBidSize(Integer.parseInt(iexQuote.getIexBidSize()));
        quote.setLastPrice(Double.parseDouble(iexQuote.getLatestPrice()));
        //Add if lastPrice is null stock market is closed
        quote.setTicker(iexQuote.getSymbol());
        logger.info("buildQuoteFromIexQuote: " + quote.toString());
        return quote;
    }

    /**
     * Add a list of new tickers to the quote table. Skip existing ticker(s).
     *  - Get iexQuote(s)
     *  - convert each iexQuote to Quote entity
     *  - persist the quote to db
     *
     * @param tickers a list of tickers/symbols
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void initQuotes(List<String> tickers) {
        List<IexQuote> iexQuotes = marketDataDao.findIexQuoteByTicker(tickers);
        List<Quote> quotes = new ArrayList<>();

        for (int i = 0; i < iexQuotes.size(); i++) {
            if (!quoteDao.existsById(iexQuotes.get(i).getSymbol())) {
                quotes.add(buildQuoteFromIexQuote(iexQuotes.get(i)));
                logger.info("TESTING QuoteList: " + quotes.get(i).toString());
                quoteDao.save(quotes.get(i));
            }
        }
    }

    /**
     * Add a new ticker to the quote table. Skip existing ticker.
     *
     * @param ticker ticker/symbol
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void initQuote(String ticker) {
        initQuotes(Collections.singletonList(ticker));
    }


    /**
     * Update quote table against IEX source
     *  - get all quotes from the db
     *  - foreach ticker get iexQuote
     *  - convert iexQuote to quote entity
     *  - persist quote to db
     *
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve data
     * @throws IllegalArgumentException for invalid input
     */
    public void updateMarketData() {
        List<Quote> quotes = quoteDao.findAll();
        List<IexQuote> iexQuotes = new ArrayList<>();
        List<Quote> updatedQuotes = new ArrayList<>();

        for (Quote quote : quotes) {
            iexQuotes.add(marketDataDao.findIexQuoteByTicker(quote.getTicker()));
        }
        for (IexQuote iexQuote : iexQuotes) {
            updatedQuotes.add(buildQuoteFromIexQuote(iexQuote));
        }
        quoteDao.update(updatedQuotes);
    }

    public void updateQuote(Quote quote){
        quote.setTicker(quote.getTicker().toUpperCase());
        quoteDao.update(Collections.singletonList(quote));
    }
}
