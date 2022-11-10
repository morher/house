package net.morher.house.api.entity.cover;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.morher.house.api.entity.common.EntityOptions;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoverOptions extends EntityOptions {

    /**
     * Normally open means up and closed means down. With {@code invertDirection} set to {@code true} open means down and closed
     * means up.
     */
    private boolean invertDirection;

    private boolean position;

    private boolean tilt;
}
