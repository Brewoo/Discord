package de.brewo.commands;

import de.brewo.core.checkPerms;
import de.brewo.utils.STATIC;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Created by Brewo on 13.05.2018.
 */
public class ChatclearCmd implements Command {

    private int getInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (checkPerms.hasPermission(event).equals("Admin")) {
            int numb = getInt(args[0]);

            if (args.length > 1) {
                STATIC.sendErrorMessage("Bitte gib die Anzahl der Nachrichten ein, die du löschen willst.", event, STATIC.embedBuilder);
            }

            if (numb > 1 && numb < 100) {

                try {
                    MessageHistory messageHistory = new MessageHistory(event.getTextChannel());


                    List<Message> messages;
                    event.getMessage().delete().queue();

                    messages = messageHistory.retrievePast(numb).complete();

                    event.getTextChannel().deleteMessages(messages).queue();

                } catch (Exception e) {
                    e.printStackTrace();
                    STATIC.sendErrorMessage("Fehler. Wahrscheinlich hat der Bot nicht genügend Permissions, um Messages zu löschen. Gib ihm dafür die Permission MESSAGE_MANAGE.", event, STATIC.embedBuilder);
                }

            } else {
                STATIC.sendErrorMessage("Die Zahl muss zwischen 2 und 99 liegen.", event, STATIC.embedBuilder);
            }


        } else {
            STATIC.sendErrorMessage("Du hast nicht genügend Rechte, um diesen Command auszuführen.", event, STATIC.embedBuilder);
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
