package net.morher.house.api.entity.trigger;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.morher.house.api.entity.common.EntityOptions;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TriggerOptions extends EntityOptions {
  private final List<String> availableEvents = new ArrayList<>();
}
