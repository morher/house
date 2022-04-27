package net.morher.house.wled.frontend;

import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.staticfiles.StaticFileConfig;
import io.javalin.plugin.json.JavalinJackson;
import net.morher.house.wled.WledControllerImpl;
import net.morher.house.wled.frontend.auth.UserManager;

/**
 * 
 * 
 * @author H139831
 */
public class WledFrontendServer {
    private final UserHandler userHandler;
    private final DeviceHandler deviceHandler;
    private final UserManager userManager;

    public WledFrontendServer(WledControllerImpl controller, UserManager userManager) {
        this.userHandler = new UserHandler(controller);
        this.deviceHandler = new DeviceHandler(controller);
        this.userManager = userManager;
    }

    public void run() {
        Javalin app = Javalin.create(this::config).start(7070);
        app.get("/d/<deviceName>/effects", deviceHandler::getDeviceEffects);
        app.get("/d/<deviceName>/palettes", deviceHandler::getDevicePalettes);
        app.get("/d/<deviceName>/state", deviceHandler::getDeviceState);
        app.post("/d/<deviceName>/state", deviceHandler::setDeviceState);
        app.get("/d/<deviceName>/", this::getDeviceControlPanel);
        app.get("/user/", userHandler::getUserProfile);
    }

    private void getDeviceControlPanel(@NotNull Context ctx) {
        String deviceName = ctx.pathParam("deviceName");
        InputStream controlPanelHtml = getClass().getResourceAsStream("/public/index.html");
        ctx.contentType(ContentType.HTML);
        ctx.result(controlPanelHtml);
    }

    private void config(JavalinConfig config) {
        config.addStaticFiles(this::config);
        config.accessManager(userManager);
        config.jsonMapper(new JavalinJackson(new ObjectMapper()));
    }

    private void config(StaticFileConfig config) {
        config.hostedPath = "/";
        config.directory = "/public";
        config.location = Location.CLASSPATH;

    }
}
