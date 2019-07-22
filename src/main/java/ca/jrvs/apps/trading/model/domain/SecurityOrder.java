package ca.jrvs.apps.trading.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "accountId",
        "status",
        "ticker",
        "size",
        "price",
        "notes"
})
public class SecurityOrder implements Entity<String> {

    @JsonProperty("id")
    private String id;
    @JsonProperty("accountId")
    private Integer accountId;
    @JsonProperty("status")
    private OrderStatus status;
    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("notes")
    private Object notes;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("accountId")
    public Integer getAccountId() {
        return accountId;
    }

    @JsonProperty("accountId")
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @JsonProperty("status")
    public OrderStatus getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @JsonProperty("ticker")
    public String getTicker() {
        return ticker;
    }

    @JsonProperty("ticker")
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("notes")
    public Object getNotes() {
        return notes;
    }

    @JsonProperty("notes")
    public void setNotes(Object notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("accountId", accountId).append("status", status).append("ticker", ticker).append("size", size).append("price", price).append("notes", notes).toString();
    }

}