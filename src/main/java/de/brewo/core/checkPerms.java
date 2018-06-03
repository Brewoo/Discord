package de.brewo.core;

import de.brewo.utils.STATIC;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

/**
 * Created by Brewo on 01.05.2018.
 */
public class checkPerms {

    public static String hasPermission(MessageReceivedEvent event) {

        List<Role> roles = event.getGuild().getMember(event.getAuthor()).getRoles();

        for (Role role : roles) {
            if(STATIC.FULLPERMS.contains(role.getName())) {
                return "Admin";
            }else

            if (STATIC.HALFPERMS.contains(role.getName())) {
                return "Mod";
            }
        }

        STATIC.sendErrorMessage(":warning: Sorry " + event.getAuthor().getAsMention() + ", you don't have permissions to perform this command.", event, STATIC.embedBuilder);

        return "Null";

    }

}
