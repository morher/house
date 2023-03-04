package net.morher.house.api.entity.number;

import net.morher.house.api.entity.common.EntityOptions;

public class NumberOptions extends EntityOptions {
  private Double min;
  private Double max;
  private Double step;

  public NumberOptions() {}

  public NumberOptions(Double min, Double max, Double step) {
    this.min = min;
    this.max = max;
    this.step = step;
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  public Double getStep() {
    return step;
  }

  public void setStep(Double step) {
    this.step = step;
  }
}
