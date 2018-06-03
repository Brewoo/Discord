package de.brewo.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brewo on 30.04.2018.
 */
public class STATIC {

    public static String PREFIX = "-";

    public static EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.RED);

    public static String VERSION = "1.0.0";

    public static List<String> HALFPERMS = Arrays.asList("Moderator", "Supporter", "Developer");

    public static List<String> FULLPERMS = Collections.singletonList("Administrator");


    public static void sendErrorMessage(String message, MessageReceivedEvent event, EmbedBuilder embedBuilder) {

        event.getTextChannel().sendMessage(
                embedBuilder.setDescription(message).build()
        ).queue();

    }

}
