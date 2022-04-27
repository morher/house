package net.morher.house.wled.frontend.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.morher.house.wled.config.WledConfiguration;
import net.morher.house.wled.config.WledConfiguration.WledUserConfig;

public class SimpleUserManager implements UserManager {
    private final Map<String, User> users = new HashMap<>();

    public SimpleUserManager(WledConfiguration config) {
        for (WledUserConfig userConfig : config.getUsers()) {
            users.put(userConfig.getId(), new User(userConfig.getId(), userConfig.getPassword()));
        }
    }

    @Override
    public void manage(Handler handler, Context ctx, Set<RouteRole> routeRoles) throws Exception {
        if (!ctx.basicAuthCredentialsExist()) {
            ctx.status(401).header("WWW-Authenticate", "Basic realm=\"Access to staging site\"");
            return;
        }

        String username = ctx.basicAuthCredentials().getUsername();
        String password = ctx.basicAuthCredentials().getPassword();
        User user = users.get(username);

        if (user == null || !user.checkPassword(username, password)) {
            ctx.status(401).header("WWW-Authenticate", "Basic realm=\"Access to staging site\"");
            return;
        }

        handler.handle(ctx);
    }

    private static String hashPassword(String username, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(username.getBytes());
            md.update(password.getBytes());
            return new BigInteger(1, md.digest()).toString(16);

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @RequiredArgsConstructor
    private final class User {
        @Getter
        private final String id;
        private final String passwordHash;

        public boolean checkPassword(String username, String password) {
            return passwordHash.equals(hashPassword(username, password));
        }
    }
}
