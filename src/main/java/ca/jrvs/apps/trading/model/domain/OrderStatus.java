package ca.jrvs.apps.trading.model.domain;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "enum"
})
public class OrderStatus {

    @JsonProperty("type")
    private String type;
    @JsonProperty("enum")
    private List<String> _enum = null;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("enum")
    public List<String> getEnum() {
        return _enum;
    }

    @JsonProperty("enum")
    public void setEnum(List<String> _enum) {
        this._enum = _enum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("type", type).append("_enum", _enum).toString();
    }

}