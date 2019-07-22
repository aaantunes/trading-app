package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.util.StringUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

//    @Value("${iex.host}")
    private String iex_host = System.getenv("IEX_HOST");


    @Bean
    public MarketDataConfig marketDataConfig() {

        if (StringUtil.isEmpty(System.getenv("IEX_PUB_TOKEN")) || StringUtil.isEmpty(iex_host)) {
            throw new IllegalArgumentException("ENV:IEX_PUB_TOKEN or IEX_HOST is not set");
        }

        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost(iex_host);
        marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));
        return marketDataConfig;
    }

    @Bean
    public DataSource dataSource() {

        String jdbcUrl;
        String username;
        String password;

        jdbcUrl = System.getenv("PSQL_URL"); //may have to remove jdbc: part
        username = System.getenv("PSQL_USER");
        password = System.getenv("PSQL_PASSWORD");

        logger.error("JDBC:" + jdbcUrl);

        if (StringUtil.isEmpty(jdbcUrl, username, password)) {
            throw new IllegalArgumentException("Missing data source config env vars");
        }

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }

}
