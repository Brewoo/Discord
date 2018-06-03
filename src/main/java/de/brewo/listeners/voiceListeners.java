package de.brewo.listeners;

import de.brewo.utils.STATIC;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

/**
 * Created by Brewo on 29.04.2018.
 */
public class voiceListeners extends ListenerAdapter {

    private static EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.BLUE);

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {

        event.getGuild().getTextChannels().get(0).sendMessage(
                embedBuilder.setDescription("User " + event.getVoiceState().getMember().getEffectiveName() + " joined Voice Channel " + event.getChannelJoined().getName() + ".").build()
        ).queue();
        //hello

    }

}
