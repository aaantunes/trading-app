package ca.jrvs.apps.trading.model.config;

public class MarketDataConfig {

    private static String host = "https://cloud.iexapis.com/stable";
    private static String token = "pk_1a7cc2a4afb649efb1a13879a39cac97";

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        MarketDataConfig.host = host;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MarketDataConfig.token = token;
    }
}
