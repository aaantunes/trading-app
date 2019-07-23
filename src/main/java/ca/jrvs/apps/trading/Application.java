package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.util.Collections;
import java.util.List;


@SpringBootApplication(exclude = {
        JdbcTemplateAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class},
        scanBasePackages = "ca.jrvs.apps.trading")
public class Application implements CommandLineRunner {

    private QuoteController quoteController;
    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public Application(QuoteController quoteController, QuoteService quoteService, QuoteDao quoteDao, MarketDataDao marketDataDao) {
        this.quoteController = quoteController;
        this.quoteService = quoteService;
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    @Override
    public void run(String... args) throws Exception {
//        marketDataDao.findIexQuoteByTicker("aapl");
//        quoteController.createQuote("tsla");
        Quote quote = new Quote();
        quote.setAskPrice(200.0);
        quote.setAskSize(3);
        quote.setBidPrice(202.0);
        quote.setLastPrice(201.99);
        quote.setBidSize(4);
        quote.setId("1000");
        quote.setTicker("AAPL");

        quoteController.putQuote(quote);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);



        //Turn off web
//        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
