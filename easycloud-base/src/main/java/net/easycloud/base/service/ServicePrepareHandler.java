package net.easycloud.base.service;

import com.google.gson.Gson;
import net.easycloud.api.CloudDriver;
import net.easycloud.api.group.Group;
import net.easycloud.api.misc.Reflections;
import net.easycloud.base.Base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ServicePrepareHandler {

    public void createFiles(Group group, String id) {
        Path files = Path.of(System.getProperty("user.dir") + File.separator + "template" + File.separator + group.getType() + File.separator + group.getName());
        Path tmp = Path.of(System.getProperty("user.dir") + File.separator + "tmp" + File.separator + group.getType() + File.separator + id);

        Reflections.createPath(tmp);
        Reflections.copy(files, tmp);

        Reflections.copy(Path.of(System.getProperty("user.dir") + File.separator + "template" + File.separator + "EVERY"), tmp);

        switch (group.getType()) {
            case SERVER -> {
                Reflections.copy(Path.of(System.getProperty("user.dir") + File.separator + "template" + File.separator + "EVERY_SERVER"), tmp);
                try {
                    Files.write(tmp.resolve("eula.txt"), Collections.singleton("eula=true"));
                    Files.write(tmp.resolve("server.properties"), List.of("max-players=50", "online-mode=false", ""));

                    Map<String, Object> data = new LinkedHashMap<>();
                    Map<String, Object> proxies = new LinkedHashMap<>();
                    Map<String, Object> velocity = new LinkedHashMap<>();

                    velocity.put("enabled", true);
                    velocity.put("online-mode", true);
                    velocity.put("secret", Base.getInstance().getVelocityProvider().getPrivateKey());
                    proxies.put("velocity", velocity);
                    data.put("proxies", proxies);

                    try {
                        Files.createDirectories(tmp.resolve("config"));
                        FileWriter writer = new FileWriter(tmp.resolve("config").resolve("paper-global.yml").toString());

                        /*DumperOptions options = new DumperOptions();
                        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                        Yaml yaml = new Yaml(options);
                        yaml.dump(data, writer);*/

                        Gson gson = new Gson();
                        writer.write(gson.toJson(data));
                        writer.close();
                    } catch (IOException e) {
                        System.err.println("Fehler beim Erstellen der Datei: " + e.getMessage());
                    }
                } catch (IOException exception) {
                    throw new RuntimeException(exception);

                }
            }
            case PROXY -> {
                Reflections.copy(Path.of(System.getProperty("user.dir") + File.separator + "template" + File.separator + "EVERY_PROXY"), tmp);
                try {
                    try {
                        URL url = new URL("https://t.vweb01.syncweb.de/server-icon.png");
                        InputStream in = url.openStream();
                        Path targetPath = tmp.resolve("server-icon.png");

                        Files.copy(in, targetPath);
                    } catch (IOException ignored) {
                    }

                    Files.write(tmp.resolve("velocity.toml"), Collections.singleton("player-info-forwarding-mode = \"modern\""));
                    Files.write(tmp.resolve("forwarding.secret"), List.of(CloudDriver.getInstance().getVelocityProvider().getPrivateKey()));
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
    }
}
