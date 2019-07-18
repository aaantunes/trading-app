package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.dao.MarketDataDao;
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

    private MarketDataDao marketDataDao;

    @Autowired
    public Application(MarketDataDao marketDataDao) {
        this.marketDataDao = marketDataDao;
    }

    @Override
    public void run(String... args) throws Exception {
        marketDataDao.findIexQuoteByTicker("aapl");
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);

        //Turn off web
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
