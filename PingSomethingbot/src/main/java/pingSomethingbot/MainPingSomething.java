package pingSomethingbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

//import booksinfobot.Bot;

public class MainPingSomething {
	public static void main(String[] args) {
		System.out.println("public class MainPingSomething is STARTED");
		ApiContextInitializer.init();
		TelegramBotsApi telegram = new TelegramBotsApi();
		Bot bot = new Bot();
		try {
			telegram.registerBot(bot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}
	}

}
