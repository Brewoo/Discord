package de.brewo.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.EventListener;

/**
 * Created by Brewo on 28.04.2018.
 */
public class ReadyListener extends ListenerAdapter {

    public void onReady(ReadyEvent event) {

        String servers = "Der Bot wird momentan auf folgenden Servern ausgeführt: \n";

        for (Guild guild : event.getJDA().getGuilds()) {
            servers += guild.getName() + " (ID: " + guild.getId() + ") \n";
        }


        for(Guild guild : event.getJDA().getGuilds()) {

            guild.getTextChannels().get(0).sendMessage(
                    "Bot started."
            ).queue();
            guild.getTextChannels().get(0).sendMessage(
                    "" + servers
            ).queue();

        }



    }

}
