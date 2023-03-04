package net.morher.house.api.state;

import java.time.Instant;
import lombok.Data;

@Data
public class StateEvent<T> {
  private final T state;
  private final Instant eventTime;
}
