package net.morher.house.api.device;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class DeviceInfo {
    private String name;
    private String manufacturer;
    private String model;
    private String configurationUrl;
    private String swVersion;
    private String suggestedArea;
    @Setter(AccessLevel.NONE)
    private List<String> identifiers = new ArrayList<>();

    public void addIdentifier(String identifier) {
        this.identifiers.add(identifier);
    }
}
