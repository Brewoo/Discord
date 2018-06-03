package de.brewo.core;

import de.brewo.commands.ChatclearCmd;
import de.brewo.commands.MusicCmd;
import de.brewo.commands.cmdPing;
import de.brewo.commands.cmdSay;
import de.brewo.listeners.ReadyListener;
import de.brewo.listeners.commandListener;
import de.brewo.listeners.voiceListeners;
import de.brewo.utils.STATIC;
import de.brewo.utils.Secrets;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

/**
 * Created by Brewo on 28.04.2018.
 */
public class Main {

    public static void main(String[] args) {

        JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT);

        jdaBuilder.setToken(Secrets.TOKEN);
        jdaBuilder.setAutoReconnect(true);

        jdaBuilder.setStatus(OnlineStatus.ONLINE);

        jdaBuilder.setGame(Game.playing("v." + STATIC.VERSION));

        registerListeners(jdaBuilder);
        registerCommands();

        try {
            jdaBuilder.buildBlocking();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void registerCommands() {

        commandHandler.commands.put("ping", new cmdPing());
        commandHandler.commands.put("say", new cmdSay());
        commandHandler.commands.put("clear", new ChatclearCmd());
        commandHandler.commands.put("m", new MusicCmd());

    }

    private static void registerListeners(JDABuilder jdaBuilder) {

        jdaBuilder.addEventListener(new ReadyListener());
        jdaBuilder.addEventListener(new voiceListeners());
        jdaBuilder.addEventListener(new commandListener());

    }

}
