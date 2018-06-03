package de.brewo.core;

import de.brewo.commands.Command;
import de.brewo.utils.STATIC;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Brewo on 30.04.2018.
 */
public class commandHandler {

    public static HashMap<String, Command> commands = new HashMap<>();

    public static void handleCommand(commandSplitter.commandContains cmd, MessageReceivedEvent event) {

        if (commands.containsKey(cmd.invoke)) {

            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if (!safe) {

                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(false, cmd.event);

            } else {

                commands.get(cmd.invoke).executed(true, cmd.event);

            }


        } else {
            STATIC.sendErrorMessage("Dieser Command existiert nicht. Benutze folgende Commands: -say, -ping, -clear <Zahl>", event, STATIC.embedBuilder);
        }

    }

}
