package pingSomethingbot;

import java.util.ArrayList;
import java.util.Date;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//import booksinfobot.Top;

public class Bot extends TelegramLongPollingBot {
	private Long chat_id;
	String lastMessage = "";
	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

	@Override
	public void onUpdateReceived(Update update) {
		update.getUpdateId();
		chat_id = update.getMessage().getChatId();
		String text = update.getMessage().getText();
		Date date = new Date();
		

		System.out.println(date.toString() + " ChatID=" + chat_id + "; Text: " + text);

		SendMessage sendMessage = new SendMessage().setChatId(chat_id);
		sendMessage.setReplyMarkup(replyKeyboardMarkup);

//		if (text.contains("login")) {
//			text = text.replace("/login", "");
//			getUser(text, chat_id);
//		} else {
//			if (text.contains("person")) {
//				text = text.replace("/person ", "");
//				getUser(text, chat_id);
//			} else {
//
			try {
					sendMessage.setText(getMessage(text));
					execute(sendMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
//			}
//		}

	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "@PingSomethingbot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "1248961696:AAFJaTrlWV3sBTpsORfHBxncBj_trwLNen8";
	}
	

	public String getMessage(String message) {
//		System.out.println("String getMessage(String message) STARTED");
		SendMessage sendMessage = new SendMessage().setChatId(chat_id);
		ArrayList<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		KeyboardRow keyboardSecondRow = new KeyboardRow();

		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		if (message.equals("Привет") || message.equals("Menu")) {
			keyboard.clear();
			keyboardFirstRow.add("Информация");
			keyboardFirstRow.add("Stop");
			keyboardSecondRow.add("Login");
			keyboard.add(keyboardFirstRow);
			keyboard.add(keyboardSecondRow);
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}

//		if (message.contains("person")) {
//			System.out.println("message = " + message);
//			String person = message.replace("/person_", "");
//			System.out.println("message after replace /person_ = " + person);
//			Author author;
//			try {
//				author = new Author(person);
//				return author.getInfoPerson();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

//		if (message.equals("Информация о книге \"Девушка с розовыми волосами.\"")) {
//			String[] tempString = { book.getHrefBook(), book.getHrefBook() };
//			return getInfoBook(tempString);
//		}
		
		if (message.equals("Login")) {
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardSecondRow.clear();
			keyboardFirstRow.add("Send my Name");
			keyboardFirstRow.add("Ping 8.8.8.7");
			keyboardSecondRow.add("Ping 8.8.8.8");
			keyboardSecondRow.add("Menu");
			keyboard.add(keyboardFirstRow);
			keyboard.add(keyboardSecondRow);
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}

		if (message.equals("Send my Name")) {
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardFirstRow.add("Test 1");
			keyboardFirstRow.add("Test 2");
			keyboardFirstRow.add("Menu");
			keyboard.add(keyboardFirstRow);
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}

		if (message.equals("Ping 8.8.8.8") || message.equals("ping ")) {
			lastMessage = message;
			System.out.println("lastMessage = " + lastMessage);
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardFirstRow.add("Repeat " + lastMessage);
			keyboardFirstRow.add("Ping 8.8.8.8");
	//		keyboardFirstRow.add("За месяц");
	//		keyboardFirstRow.add("За все время");
			keyboardFirstRow.add("Menu");
			keyboard.add(keyboardFirstRow);
			Ip ip = new Ip("8.8.8.8");
			try {
				sendMessage.setText(ip.getIpAnswer());
				execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}
		
		if (message.equals("Ping 8.8.8.7")) {
			lastMessage = message;
			System.out.println("lastMessage = " + lastMessage);
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardFirstRow.add("Repeat " + lastMessage);
			keyboardFirstRow.add("Ping 8.8.8.7");
	//		keyboardFirstRow.add("За месяц");
	//		keyboardFirstRow.add("За все время");
			keyboardFirstRow.add("Menu");
			keyboard.add(keyboardFirstRow);
			Ip ip = new Ip("8.8.8.7");
			try {
				sendMessage.setText(ip.getIpAnswer());
				execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}
		
//
//		boolean b = message.equals("Сегодня") || message.equals("За неделю") || message.equals("За месяц")
//				|| message.equals("За все время");

		if (lastMessage.equals("Ping 8.8.8.8")) {
			System.out.println("Ping 8.8.8.8");
				return null;
//				return pingIP(lastMessage);
			}
		

		return message;
	}


}
