package net.morher.house.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EntityCategory {
  @JsonProperty("config")
  CONFIG,

  @JsonProperty("diagnostic")
  DIAGNOSTIC,

  @JsonProperty("system")
  SYSTEM;
}
