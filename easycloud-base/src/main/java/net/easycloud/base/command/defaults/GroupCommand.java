package net.easycloud.base.command.defaults;

import net.easycloud.api.console.LogType;
import net.easycloud.api.group.Group;
import net.easycloud.api.group.misc.GroupType;
import net.easycloud.api.group.misc.GroupVersion;
import net.easycloud.base.command.CloudCommand;
import net.easycloud.base.command.Command;
import net.easycloud.base.Base;
import net.easycloud.base.service.SimpleServiceHandler;
import net.easycloud.base.setup.ConsoleSetup;
import net.easycloud.base.setup.SetupBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Command(name = "group", aliases = {"groups"})
public final class GroupCommand extends CloudCommand {

    @Override
    protected void execute(String[] args) {
        var logger = Base.getInstance().getLogger();

        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("setup")) {
                ConsoleSetup.subscribe(List.of(
                        SetupBuilder.<String>get()
                                .key("group.name")
                                .question("&7What is you group name?")
                                .build(),
                        SetupBuilder.<String>get()
                                .key("group.memory")
                                .question("&7What is you group memory?")
                                .build(),
                        SetupBuilder.<Integer>get()
                                .key("group.minOnline")
                                .question("&7What is you group minOnline count?")
                                .build(),
                        SetupBuilder.<String>get()
                                .key("group.maxOnline")
                                .question("&7What is you group maxOnline count? (-1 = unlimited)")
                                .build(),
                        SetupBuilder.<String>get()
                                .key("group.maxPlayers")
                                .question("&7What is you group maxPlayers?")
                                .build(),
                        SetupBuilder.<Boolean>get()
                                .key("group.static")
                                .question("&7Is your group static?")
                                .possibleResults(List.of(true, false))
                                .build(),
                        SetupBuilder.<String>get()
                                .key("group.type")
                                .question("&7What is you group type?")
                                .possibleResults(Arrays.stream(GroupType.values()).map(Enum::name).toList())
                                .build(),
                        SetupBuilder.<String>get()
                                .key("group.version")
                                .question("&7What is you group version?")
                                .possibleResults(Arrays.stream(GroupVersion.values()).map(Enum::name).toList())
                                .build()
                ), values -> {
                    Base.getInstance().groupProvider().getRepository().query().match("name", values.get("group.name"))
                            .find().stream().findFirst()
                            .ifPresentOrElse(group -> {
                                logger.log("Group " + values.get("group.name") + " already exists!", LogType.WARNING);
                            }, () -> {
                                Base.getInstance().groupProvider().create(new Group(
                                        UUID.randomUUID(),
                                        values.get("group.name"),
                                        Integer.parseInt(values.get("group.memory")),
                                        Integer.parseInt(values.get("group.minOnline")),
                                        Integer.parseInt(values.get("group.maxOnline")),
                                        Integer.parseInt(values.get("group.maxPlayers")),
                                        Boolean.parseBoolean(values.get("group.static")),
                                        "ANVIL",
                                        GroupType.valueOf(values.get("group.type")),
                                        GroupVersion.valueOf(values.get("group.version"))
                                ));
                            });
                    ((SimpleServiceHandler) Base.getInstance().serviceProvider()).update();
                });
                return;
            }
            if (args[1].equalsIgnoreCase("list")) {
                if (Base.getInstance().groupProvider().getRepository().query().find().isEmpty()) {
                    logger.log("There is currently no group!");
                    return;
                }

                Base.getInstance().groupProvider().getRepository().query().find().forEach(group -> {
                    logger.log(group.getName() + " " + argument(group.getMaxMemory() + "mb") + " §7| §f0 Online");
                });
                return;
            }
            if (args[1].equalsIgnoreCase("types")) {
                for (GroupType value : GroupType.values()) {
                    logger.log(value.name());
                }
                return;
            }
            if (args[1].equalsIgnoreCase("versions")) {
                for (GroupVersion version : GroupVersion.values()) {
                    logger.log(version.name() + " " + argument(version.getUrl()));
                }
                return;
            }
            if (args[1].equalsIgnoreCase("create")) {
                if (args.length == 7) {
                    try {
                        Base.getInstance().groupProvider().getRepository().query().match("name", args[2])
                                .find().stream().findFirst()
                                .ifPresentOrElse(group -> {
                                    logger.log("Group " + args[2] + " already exists!", LogType.WARNING);
                                }, () -> {
                                    Base.getInstance().groupProvider().create(new Group(UUID.randomUUID(), args[2], Integer.parseInt(args[3]), 1, -1, 50, Boolean.parseBoolean(args[6]), "ANVIL", GroupType.valueOf(args[4].toUpperCase()), GroupVersion.valueOf(args[5].toUpperCase())));
                                });
                        ((SimpleServiceHandler) Base.getInstance().serviceProvider()).update();
                    } catch (Exception exception) {
                        logger.log("Please provide valid values!", LogType.ERROR);
                    }
                    return;
                }
            }
            if (args[1].equalsIgnoreCase("delete")) {
                if (args.length == 2) {
                    try {
                        Base.getInstance().groupProvider().getRepository().query().match("name", args[2])
                                .find().stream().findFirst()
                                .ifPresentOrElse(group -> {
                                    //TODO
                                }, () -> {
                                    logger.log("Group " + args[2] + " does not exists!", LogType.WARNING);
                                });
                        ((SimpleServiceHandler) Base.getInstance().serviceProvider()).update();
                    } catch (Exception exception) {
                        logger.log("Please provide valid values!", LogType.ERROR);
                    }
                    return;
                }
            }
        }
        logger.log("");
        logger.log("group setup");
        logger.log("group create " + argument("name") + " " + argument("memory") + " " + argument("type") + " " + argument("version") + " " + argument("isStatic"));
        logger.log("group delete " + argument("name"));
        logger.log("group versions");
        logger.log("group list");
        logger.log("");
    }

    private String argument(String value) {
        return "§7[§f" + value + "§7]";
    }
}
