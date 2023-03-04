package net.morher.house.api.hass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@JsonInclude(Include.NON_NULL)
public class DeviceConfig {
  public String name;
  public String manufacturer;
  public String model;

  @JsonProperty("suggested_area")
  public String area;

  @Setter(AccessLevel.NONE)
  public List<String> identifiers = new ArrayList<>();
}
