package net.morher.house.api.utils.color;

import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Brightness {
  public static final Brightness FULL = new Brightness(1.0);
  @Getter private final double level;

  public static Optional<Brightness> ofNullable(Double level) {
    return level != null ? Optional.of(new Brightness(level)) : Optional.empty();
  }

  public static Optional<Brightness> ofNullable(Integer level, int min, int max) {
    return level != null ? Optional.of(new Brightness(level, min, max)) : Optional.empty();
  }

  public static Optional<Brightness> ofNullable(Double level, double min, double max) {
    return level != null ? Optional.of(new Brightness(level, min, max)) : Optional.empty();
  }

  public Brightness(double brightness) {
    if (brightness < 0.0) {
      this.level = 0.0;
    } else if (brightness > 1.0) {
      this.level = 1.0;
    } else {
      this.level = brightness;
    }
  }

  public Brightness(int level, int min, int max) {
    this((double) level, (double) min, (double) max);
  }

  public Brightness(double level, double min, double max) {
    this((level - min) / (max - min));
  }

  public Brightness multiply(Double multiplier) {
    return new Brightness(level * multiplier);
  }

  public int rebase(int min, int max) {
    return ((int) (level * (max - min))) + min;
  }
}
