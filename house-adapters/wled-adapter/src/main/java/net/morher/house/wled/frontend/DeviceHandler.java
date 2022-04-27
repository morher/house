package net.morher.house.wled.frontend;

import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import net.morher.house.wled.LedStripState;
import net.morher.house.wled.WledControllerImpl;

public class DeviceHandler {
    private final WledControllerImpl controller;

    public DeviceHandler(WledControllerImpl controller) {
        this.controller = controller;
    }

    public void getDeviceEffects(@NotNull Context ctx) {
        InputStream effectsJson = getClass().getResourceAsStream("/effects.json");
        ctx.contentType(ContentType.JSON);
        ctx.result(effectsJson);
    }

    public void getDeviceState(@NotNull Context ctx) {
        String stripId = ctx.pathParam("deviceName");
        ctx.json(controller.getStrip(stripId).getState());
    }

    public void setDeviceState(@NotNull Context ctx) {
        String stripId = ctx.pathParam("deviceName");
        LedStripState state = ctx.bodyAsClass(LedStripState.class);
        controller.getStrip(stripId).setState(state);
        ctx.result("Ok");
    }

    public void getDevicePalettes(@NotNull Context ctx) {
        InputStream effectsJson = getClass().getResourceAsStream("/palettes.json");
        ctx.contentType(ContentType.JSON);
        ctx.result(effectsJson);
    }

}
