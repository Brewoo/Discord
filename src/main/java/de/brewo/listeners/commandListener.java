package de.brewo.listeners;

import de.brewo.core.commandHandler;
import de.brewo.core.commandSplitter;
import de.brewo.utils.STATIC;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Brewo on 30.04.2018.
 */
public class commandListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().startsWith(STATIC.PREFIX) && event.getMessage().getAuthor().getId() !=
                event.getJDA().getSelfUser().getId()) {

            commandHandler.handleCommand(commandSplitter.splitter(event.getMessage().getContentRaw(), event), event);

        }

    }


}
