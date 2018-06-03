package de.brewo.commands;

import de.brewo.core.checkPerms;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Brewo on 30.04.2018.
 */
public class cmdPing implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {


        if(checkPerms.hasPermission(event).equals("Mod")) {
            event.getTextChannel().sendMessage(":heavy_check_mark: Pong!").queue();
        }

        if(checkPerms.hasPermission(event).equals("Admin")) {
            event.getTextChannel().sendMessage(":white_check_mark: Administrator - Pong!").queue();
        }




    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {
        System.out.println("INFO: Command -ping wurde von " + event.getMessage().getAuthor().getName() + " ausgeführt!");
    }

    @Override
    public String help() {
        return null;
    }
}
