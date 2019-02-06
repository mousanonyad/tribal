package com.mousanony.telegram.bot.consolegame;

import com.mousanony.telegram.bot.consolegame.handlers.LongPolling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author mousanonyad
 */
public class Application {
    private static final String BOT_NAME = "TribalLeaderBot";
    private static final String BOT_TOKEN = "";
//    private static final String BOT_URL = "t.me/TribalLeaderBot";

    private static String PROXY_HOST = "212.224.113.209" /* proxy host */;
    private static Integer PROXY_PORT = 1080 /* proxy port */;
    private static String PROXY_USER = "proxy" /* proxy user */;
    private static String PROXY_PASSWORD = "quah1Iej" /* proxy password */;

    //пока всё здесь, рефакторинг потом
    private static boolean useProxy = true;

    public static void main(String[] args) {
        try {
            ApiContextInitializer.init();

            // Create the TelegramBotsApi object to register your bots
            TelegramBotsApi botsApi = new TelegramBotsApi();

            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            // Set up Http proxy
            if (useProxy){
                // Create the Authenticator that will return auth's parameters for proxy authentication
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
                    }
                });

                botOptions.setProxyHost(PROXY_HOST);
                botOptions.setProxyPort(PROXY_PORT);
                // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
                botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            }

            // Register your newly created AbilityBot
            LongPolling bot = new LongPolling(BOT_TOKEN, BOT_NAME, botOptions);

            botsApi.registerBot(bot);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
