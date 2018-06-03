package de.brewo.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Brewo on 30.04.2018.
 */
public interface Command {

    boolean called(String[] args, MessageReceivedEvent event);
    void action(String[] args, MessageReceivedEvent event);
    void executed(boolean sucess, MessageReceivedEvent event);
    String help();

}
