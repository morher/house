package net.morher.house.wled.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WledSegment {

    private Integer id;
    private Integer start;
    private Integer stop;

    @JsonProperty("len")
    private Integer length;

    @JsonProperty("on")
    private Boolean powerOn;

    @JsonProperty("bri")
    private Integer brightness;

    @JsonProperty("col")
    private int[][] colors;

    @JsonProperty("fx")
    private Integer effect;

    @JsonProperty("sx")
    private Integer speed;

    @JsonProperty("ix")
    private Integer intensity;

    @JsonProperty("pal")
    private Integer palette;

    @JsonProperty("sel")
    private Boolean selected;
}
