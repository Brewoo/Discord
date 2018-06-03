package de.brewo.commands;

import de.brewo.core.checkPerms;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Brewo on 06.05.2018.
 */
public class cmdSay implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (checkPerms.hasPermission(event).equals("Mod") || checkPerms.hasPermission(event).equals("Admin")) {

            StringBuilder stringBuilder = new StringBuilder();
            for (String string : args) {

                stringBuilder.append(string).append(" ");


            }
            sendMessage(stringBuilder.toString(), event);


        }


    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    private static void sendMessage(String message, MessageReceivedEvent event) {

        event.getTextChannel().sendMessage(message).queue();

    }
}
