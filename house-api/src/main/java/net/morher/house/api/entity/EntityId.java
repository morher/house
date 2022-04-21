package net.morher.house.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.morher.house.api.device.DeviceId;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class EntityId {
    private final DeviceId device;
    private final String entity;
}
