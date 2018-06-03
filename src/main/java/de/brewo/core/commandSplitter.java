package de.brewo.core;

import de.brewo.utils.STATIC;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

/**
 * Created by Brewo on 30.04.2018.
 */
public class commandSplitter {

    public static commandContains splitter(String raw, MessageReceivedEvent event) {

        String beheaded = raw.replaceFirst(STATIC.PREFIX, "");
        String[] splitBeheaded = beheaded.split(" ");
        String invoke = splitBeheaded[0];

        ArrayList<String> split = new ArrayList<>();
        for (String string : splitBeheaded) {
            split.add(string);
        }

        String[] args = new String[split.size() - 1];
        split.subList(1, split.size()).toArray(args);

        return new commandContains(raw, beheaded, splitBeheaded, invoke, args, event);
    }


    public static class commandContains {

        public final String raw;
        public final String beheaded;
        public final String[] splitBeheaded;
        public final String invoke;
        public final String[] args;
        public final MessageReceivedEvent event;

        public commandContains(String raw, String beheaded, String[] splitBeheaded, String invoke, String[] args, MessageReceivedEvent event) {
            this.raw = raw;
            this.beheaded = beheaded;
            this.splitBeheaded = splitBeheaded;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }
    }

}
