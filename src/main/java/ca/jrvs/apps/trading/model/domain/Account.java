package ca.jrvs.apps.trading.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "traderId",
        "amount"
})
public class Account implements Entity<String>{

    @JsonProperty("id")
    private String  id;
    @JsonProperty("traderId")
    private Integer traderId;
    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("traderId")
    public Integer getTraderId() {
        return traderId;
    }

    @JsonProperty("traderId")
    public void setTraderId(Integer traderId) {
        this.traderId = traderId;
    }

    @JsonProperty("amount")
    public Double getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("traderId", traderId).append("amount", amount).toString();
    }

}