package ca.jrvs.apps.trading.model.domain;

import ca.jrvs.apps.trading.model.domain.Quote;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ticker",
        "position",
        "quote"
})
public class SecurityRow {

    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("position")
    private Position position;
    @JsonProperty("quote")
    private Quote quote;

    @JsonProperty("ticker")
    public String getTicker() {
        return ticker;
    }

    @JsonProperty("ticker")
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @JsonProperty("position")
    public Position getPosition() {
        return position;
    }

    @JsonProperty("position")
    public void setPosition(Position position) {
        this.position = position;
    }

    @JsonProperty("quote")
    public Quote getQuote() {
        return quote;
    }

    @JsonProperty("quote")
    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ticker", ticker).append("position", position).append("quote", quote).toString();
    }

}