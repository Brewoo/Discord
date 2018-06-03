package de.brewo.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sun.java.accessibility.util.GUIInitializedListener;
import de.brewo.audioCoreClasses.AudioInformation;
import de.brewo.audioCoreClasses.PlayerSendHandler;
import de.brewo.audioCoreClasses.TrackManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Brewo on 22.05.2018.
 */
public class MusicCmd implements Command {

    private static final int PLAYLIST_LIMIT = 1000;
    private static Guild guild;
    private static final AudioPlayerManager AUDIO_PLAYER_MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

    public MusicCmd() {
        AudioSourceManagers.registerRemoteSources(AUDIO_PLAYER_MANAGER);

    }

    private AudioPlayer createPlayer(Guild guild) {
        AudioPlayer player = AUDIO_PLAYER_MANAGER.createPlayer();
        TrackManager trackManager = new TrackManager(player);
        player.addListener(trackManager);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));

        PLAYERS.put(guild, new AbstractMap.SimpleEntry<>(player, trackManager));

        return player;
    }

    private boolean hasPlayer(Guild guild) {
        return PLAYERS.containsKey(guild);
    }

    private AudioPlayer getPlayer(Guild guild) {

        if (hasPlayer(guild))
            return PLAYERS.get(guild).getKey();
        else
            return createPlayer(guild);

    }

    private TrackManager getManager(Guild guild) {
        return PLAYERS.get(guild).getValue();
    }

    private boolean isIdle(Guild guild) {
        return !hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null;
    }

    private void loadTrack(String identifier, Member author, Message message) {

        Guild guild = author.getGuild();
        getPlayer(guild);

        AUDIO_PLAYER_MANAGER.setFrameBufferDuration(2000);
        AUDIO_PLAYER_MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });

    }

    private void skip(Guild guild) {
        getPlayer(guild).stopTrack();
    }

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    private String buildQueueMessage(AudioInformation audioInformation) {

        AudioTrackInfo audioTrackInfo = audioInformation.getTRACK().getInfo();
        String title = audioTrackInfo.title;
        long length = audioTrackInfo.length;
        return "'[ " + getTimestamp(length) + " ]' " + title + "\n";

    }

    private void sendErrorMsg(MessageReceivedEvent event, String message) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder().setColor(Color.RED).setDescription(message).build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        guild = event.getGuild();
        if (args.length < 1) {
            sendErrorMsg(event, help());
        }

        switch (args[0].toLowerCase()) {

            case "play":
            case "p":

                if (args.length < 2) {
                    sendErrorMsg(event, "Bitte gib eine verfügbare Quelle an.");
                }


                if (!isIdle(guild)) {

                    getManager(guild).clearQueue();
                    skip(guild);

                    String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                    if (!(input.startsWith("http://") || input.startsWith("https://")))
                        input = "ytsearch: " + input;


                    loadTrack(input, event.getMember(), event.getMessage());

                } else {
                    String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                    if (!(input.startsWith("http://") || input.startsWith("https://")))
                        input = "ytsearch: " + input;


                    loadTrack(input, event.getMember(), event.getMessage());
                }

                break;

            case "skip":

                if (isIdle(guild)) return;

                for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i == 1; i--) {
                    skip(guild);
                }

                break;

            case "stop":

                if (isIdle(guild)) return;

                getManager(guild).clearQueue();
                skip(guild);
                guild.getAudioManager().closeAudioConnection();

                sendErrorMsg(event, "Die Playlist wurde erfolgreich gestoppt.");

                break;

            case "shuffle":

                if (isIdle(guild)) return;

                getManager(guild).randomiseQueue();

                sendErrorMsg(event, "Die Playlist wurde erfolgreich geshuffled.");

                break;

            case "volume":
            case "v":

                if(isIdle(guild)) sendErrorMsg(event, "Momentan wird kein Track abgespielt.");

                if(args.length < 2) {
                    sendErrorMsg(event, "Bitte gib einen Wert zwischen 0 und 100 an.");
                }else {

                    try {
                        String volume = args[1];
                        getPlayer(guild).setVolume(Integer.parseInt(volume));

                        event.getTextChannel().sendMessage(
                                new EmbedBuilder().setColor(Color.CYAN).setDescription("Volume: " + Integer.parseInt(volume)).build()
                        ).queue();

                    }catch (Exception e) {
                        sendErrorMsg(event, "Bitte gib einen Wert zwischen 0 und 100 an.");
                    }

                }

                break;

            case "now":
            case "info":

                if (isIdle(guild)) sendErrorMsg(event, "Momentan wird kein Track abgespielt.");

                AudioTrack track = getPlayer(guild).getPlayingTrack();
                AudioTrackInfo audioTrackInfo = track.getInfo();

                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription("--CURRENT TRACK INFO--")
                                .addField("Title: ", audioTrackInfo.title, false)
                                .addField("Duration: ", "[" + getTimestamp(track.getPosition()) + "/" +
                                        getTimestamp(track.getDuration()) + "]", false)
                                .addField("Author: ", audioTrackInfo.author, false)
                                .addField("Volume: ", String.valueOf(getPlayer(guild).getVolume()), false)
                                .build()
                ).queue();


                break;

        }

    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
