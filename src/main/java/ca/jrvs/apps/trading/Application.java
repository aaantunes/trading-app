package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.controller.QuoteController;
import ca.jrvs.apps.trading.controller.TraderContoller;
import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.service.FundTransferService;
import ca.jrvs.apps.trading.service.QuoteService;
import ca.jrvs.apps.trading.service.RegisterService;
import ca.jrvs.apps.trading.util.ParametersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


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

    private TraderContoller traderContoller;
    private FundTransferService fundTransferService;
    private RegisterService registerService;
    private TraderDao traderDao;
    private AccountDao accountDao;
    private SecurityOrderDao securityOrderDao;
    private PositionDao positionDao;

    @Autowired
    public Application(QuoteController quoteController, QuoteService quoteService, QuoteDao quoteDao, MarketDataDao marketDataDao) {
        this.quoteController = quoteController;
        this.quoteService = quoteService;
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    @Override
    public void run(String... args) throws Exception {

        /*TESTING QUOTE_CONTROLLER!!!*/
        /*Testing getIexQuoteByTicker*/
//        marketDataDao.findIexQuoteByTicker("aapl");

        /*Testing CreateQuote given ticker*/
//        quoteController.createQuote("tsla");

//        Quote quote = new Quote();
//        quote.setAskPrice(200.0);
//        quote.setAskSize(3);
//        quote.setBidPrice(202.0);
//        quote.setLastPrice(201.99);
//        quote.setBidSize(4);
//        quote.setId("1000");
//        quote.setTicker("AAPL");
//
//        quoteController.putQuote(quote);

        /*Testing Display Daily List and Update Market Data*/
//        quoteController.getDailyList();
//        quoteController.updateMarketData();
//        quoteController.getDailyList();

        /*TESTING TRADER_CONTROLLER!!!*/
        /*testing createTraderAndAccount()*/
        Trader trader = new Trader();
        trader = traderDao.createTrader("Andre", "Antunes",
                "1997-10-29", "Canada", "aaantune@ryerson.ca");
        traderContoller.createTraderAndAccount(trader);

        /*testing deleteTrader()*/
        traderContoller.deleteTrader(trader.getId());

        /*testing createTraderAndAccount(,,,,)*/
        traderContoller.createTraderAndAccount("Andre", "Antunes",
                "1997-10-29", "Canada", "aaantune@ryerson.ca");

        /*testing depositFund()*/
        traderContoller.depositFund(trader.getId(), 1000.0);

        /*testing withdrawFund()*/
        traderContoller.withdrawFund(trader.getId(), 100.0);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);

        Quote quote = new Quote();
        quote.setTicker("test");
        quote.setLastPrice(200.0);

       if (!ParametersUtil.checkIfNullsInObject(quote).isEmpty()) {
           System.out.println(ParametersUtil.checkIfNullsInObject(quote));
       }
        //Turn off web
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
