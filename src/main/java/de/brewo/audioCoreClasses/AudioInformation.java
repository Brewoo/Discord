package de.brewo.audioCoreClasses;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;

/**
 * Created by Brewo on 20.05.2018.
 */
public class AudioInformation {

    private final AudioTrack TRACK;
    private final Member AUTHOR;

    public AudioInformation(AudioTrack track, Member author) {
        this.TRACK = track;
        this.AUTHOR = author;
    }

    public AudioTrack getTRACK() {
        return TRACK;
    }

    public Member getAUTHOR() {
        return AUTHOR;
    }



}
