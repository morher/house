package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public abstract class BaseEntityConfig {

    private String name;
    @JsonProperty("unique_id")
    private String uniqueId;

    private String icon;

    @JsonProperty("avty_t")
    private String availabilityTopic;

    @JsonProperty("entity_category")
    private String entityCategory;

    private DeviceConfig device;

    @JsonIgnore
    public abstract String getEntityClass();
}