package net.morher.house.api.entity.light;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;
import net.morher.house.api.entity.common.EntityOptions;

@Data
@With
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class LightOptions extends EntityOptions {
    private final boolean dimmable;
    private final List<String> effects;
}
