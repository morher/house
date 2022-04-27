package net.morher.house.wled.frontend;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.javalin.http.Context;
import net.morher.house.wled.WledControllerImpl;

public class UserHandler {
    private final WledControllerImpl controller;

    public UserHandler(WledControllerImpl controller) {
        this.controller = controller;
    }

    public void getUserProfile(@NotNull Context ctx) {
        String userId = null;

        UserTO user = new UserTO();
        user.setUserId(userId);
        user.setName(userId);
        user.setLedStrips(new ArrayList<>());
        List<WledStripTO> ledStrips = controller
                .getStrips()
                .stream()
                .map(WledStripTO::from)
                .collect(toList());
        user.setLedStrips(ledStrips);

        ctx.json(user);

    }

}
