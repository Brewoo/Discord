package de.brewo.audioCoreClasses;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Brewo on 20.05.2018.
 */
public class TrackManager extends AudioEventAdapter {

    private final AudioPlayer PLAYER;
    private final Queue<AudioInformation> queue;


    public TrackManager(AudioPlayer player) {
        this.PLAYER = player;
        this.queue = new LinkedBlockingQueue<>();
    }


    public void queue(AudioTrack track, Member author) {
        AudioInformation audioInformation = new AudioInformation(track, author);
        queue.add(audioInformation);

        if(PLAYER.getPlayingTrack() == null) {
            PLAYER.playTrack(track);
        }
    }

    public Set<AudioInformation> getQueue() {
        return new LinkedHashSet<>(queue);
    }

    public AudioInformation getInformation(AudioTrack track) {
        return queue.stream()
                .filter(audioInformation -> audioInformation.getTRACK().equals(track))
                .findFirst().orElse(null);
    }

    public void clearQueue() {
        queue.clear();
    }

    public void randomiseQueue() {
        List<AudioInformation> cQueue = new ArrayList<>(getQueue());
        AudioInformation current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        clearQueue();
        queue.addAll(cQueue);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {

        AudioInformation audioInformation = queue.element();
        VoiceChannel voiceChannel = audioInformation.getAUTHOR().getVoiceState().getChannel();

        if(voiceChannel == null) {
            player.stopTrack();
            Objects.requireNonNull(audioInformation.getAUTHOR().getDefaultChannel()).sendMessage("Du befindest dich derzeit in keinem Voice Channel.").queue();
        }else {
            audioInformation.getAUTHOR().getGuild().getAudioManager().openAudioConnection(voiceChannel);
        }

    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        Guild guild = queue.poll().getAUTHOR().getGuild();

        if(queue.isEmpty()) {
            guild.getAudioManager().closeAudioConnection();
        }else {
            player.playTrack(queue.element().getTRACK());
        }

    }




}
