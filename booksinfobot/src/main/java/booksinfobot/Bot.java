package booksinfobot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
	Book book = new Book("https://www.surgebook.com/GGhe4ka/book/devushka-s-rozovymi-volosami");
	private Long chat_id;
	String lastMessage = "";
	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

	public void onUpdateReceived(Update update) {
		update.getUpdateId();
		chat_id = update.getMessage().getChatId();
		String text = update.getMessage().getText();
		Date date = new Date();

		System.out.println(date.toString() + " ChatID=" + chat_id + "; Text: " + text);

		SendMessage sendMessage = new SendMessage().setChatId(chat_id);
		sendMessage.setReplyMarkup(replyKeyboardMarkup);

		if (text.contains("person_")) {
			text = text.replace("/person_", "");
			getPerson(text, chat_id);
		} else {
			if (text.contains("person")) {
				text = text.replace("/person ", "");
				getPerson(text, chat_id);
			} else {

				try {
					sendMessage.setText(getMessage(text));
					execute(sendMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getBotUsername() {
		return "@booksinfobot";
	}

//	@Override
	public String getBotToken() {
		return "1161419085:AAGb_c5WNql9jXAcCyvPR6RUS1JPncgs9ZE";
	}

	public String getAuthorNickName(Book book) {
		System.out.println("getAuthorNickName STARTED");
		String authorNickName = "";
		String authorHref = book.getHrefBook();
		System.out.println("book.getHrefBook() = " + book.getHrefBook());
		authorHref = authorHref.replaceAll("https://www.surgebook.com/", "");
		// https://www.surgebook.com/LilMiller/book/nezabudka

		int indexStringBook = authorHref.indexOf("/book");
		System.out.println("indexStringBook = " + indexStringBook);

		authorNickName = authorHref.substring(0, indexStringBook);

		System.out.println("authorNickName = " + authorNickName);

		return authorNickName;
	}

	public String getInfoBook(String href[]) {
		String info = "";
		for (int i = 0; i < href.length; i++) {
			info = "";
			Book book = new Book(href[i]);
			if (Files.exists(Paths.get("src\\main\\pictures")))
				;
			{
				System.out.println("File: src/main/pictures exists");
				try {
					Files.delete(Paths.get("src\\main\\pictures"));
					System.out.println("File: src/main/pictures was deleted");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			SendPhoto sendPhotoRequest = new SendPhoto();

			try (InputStream in = new URL(book.getImg()).openStream()) {
				System.out.println("\ntry (InputStream in = new URL(book.getImg()).openStream()) {\n");
				Files.copy(in, Paths.get("src\\main\\pictures"));
				sendPhotoRequest.setChatId(chat_id);
				sendPhotoRequest.setPhoto(new File("src\\main\\pictures"));
				try {
					execute(sendPhotoRequest);
					System.out.println("\nBook's photo # " + (i + 1) + " was send to User ChatId: " + chat_id
							+ ". Cover's href: " + book.getImg());
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
				if (Files.exists(Paths.get("src\\main\\pictures")))
					;

			} catch (IOException e) {
				System.out.println("File not found");
			}

			info = book.getTitle() + "\nАвтор " + book.getAutorName() + "\nЖанр " + book.getGeners() + "\nОписание\n"
					+ book.getDescription() + "\nКоличество лайков\n" + book.getLikes() + "\nПоследние комментарии\n"
					+ book.getCommentList();
			info = info + "\nСсылка на страницу книги:\n" + book.getHrefBook();

			try {
				Author author = new Author(getAuthorNickName(book));
				info = info + "\nИнформация об авторе /person_" + author.getNickName();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			SendMessage sendMessage = new SendMessage().setChatId(chat_id);
			sendMessage.setText(info);
			System.out.println("info sent to user ChatId" + chat_id + " :/n" + info);
			try {
				execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		return "\uD83D\uDC40";
	}

	private void getPerson(String name, long chat_id) {
		try {
			Author author = new Author(name);
			if (author.noName == "") {

				// Отправляем изображение
				URL url = new URL(author.getImg());

				// берем сслыку на изображение
				BufferedImage img = ImageIO.read(url);

				// качаем изображение в буфер
				File outputfile = new File("image.jpg");

				// создаем новый файл в который поместим
				// скаченое изображение
				ImageIO.write(img, "jpg", outputfile);

				// преобразовуем наше буферное изображение
				// в новый файл
				SendPhoto sendPhoto = new SendPhoto().setChatId(chat_id);
				sendPhoto.setPhoto(outputfile);
				execute(sendPhoto);
				System.out.println("Photo author " + author.name + " was send to chatID = " + chat_id);

				// Отправляем информацию о пользователе
				SendMessage sendMessage = new SendMessage().setChatId(chat_id);
				sendMessage.setText(author.getInfoPerson());
				execute(sendMessage);
				System.out.println("Information about author " + author.name + " was send to chatID = " + chat_id);
			} else {
				SendMessage sendMessage = new SendMessage().setChatId(chat_id);
				sendMessage.setText(author.noName);
				execute(sendMessage);
				System.out.println("Information that this site dosn't have  author " + author.name
						+ " was send to chatID = " + chat_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage(String message) {
		ArrayList<KeyboardRow> keyboard = new ArrayList<>();
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		KeyboardRow keyboardSecondRow = new KeyboardRow();

		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(false);

		if (message.equals("Привет") || message.equals("Меню")) {
			keyboard.clear();
			keyboardFirstRow.add("Популярное");
			keyboardFirstRow.add("Новости\uD83D\uDCF0");
			keyboardSecondRow.add("Полезная Информация");
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

		if (message.equals("Информация о книге \"Девушка с розовыми волосами.\"")) {
			String[] tempString = { book.getHrefBook(), book.getHrefBook() };
			return getInfoBook(tempString);
		}
		if (message.equals("Полезная Информация")) {
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardFirstRow.add("Информация о книге \"Девушка с розовыми волосами.\"");
			keyboardSecondRow.add("/person GGhe4ka");
			keyboardSecondRow.add("Меню");
			keyboard.add(keyboardFirstRow);
			keyboard.add(keyboardSecondRow);
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}

		if (message.equals("Популярное")) {
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardFirstRow.add("Стихи");
			keyboardFirstRow.add("Книги\uD83D\uDCDA");
			keyboardFirstRow.add("Меню");
			keyboard.add(keyboardFirstRow);
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}

		if (message.equals("Стихи") || message.equals("Книги\uD83D\uDCDA")) {
			lastMessage = message;
			System.out.println("lastMessage = " + lastMessage);
			keyboard.clear();
			keyboardFirstRow.clear();
			keyboardFirstRow.add("Сегодня");
			keyboardFirstRow.add("За неделю");
			keyboardFirstRow.add("За месяц");
			keyboardFirstRow.add("За все время");
			keyboardFirstRow.add("Меню");
			keyboard.add(keyboardFirstRow);
			replyKeyboardMarkup.setKeyboard(keyboard);
			return "Выбрать...";
		}
//
//		boolean b = message.equals("Сегодня") || message.equals("За неделю") || message.equals("За месяц")
//				|| message.equals("За все время");

		if (lastMessage.equals("Книги\uD83D\uDCDA")) {
			Top top = new Top();
			if (message.equals("Сегодня")) {
				System.out.println("Книги\uD83D\uDCDA" + " Сегодня");
				return getInfoBook(top.getTopBooks(message));
			}
			if (message.equals("За неделю")) {
				return getInfoBook(top.getTopBooks(message));
			}
			if (message.equals("За месяц")) {
				return getInfoBook(top.getTopBooks(message));
			}
			if (message.equals("За все время")) {
				return getInfoBook(top.getTopBooks(message));
			}
		} else if (lastMessage.equals("Стихи")) {
			Top top = new Top();
			if (message.equals("Сегодня")) {
				return getTopPoem(top.getTopPoems(message));
			}
			if (message.equals("За неделю")) {
				return getTopPoem(top.getTopPoems(message));
			}
			if (message.equals("За месяц")) {
				return getTopPoem(top.getTopPoems(message));
			}
			if (message.equals("За все время")) {
				return getTopPoem(top.getTopPoems(message));
			}
		}

		return message;
	}

	public String getTopPoem(String[] text) {
		SendMessage sendMessage = new SendMessage().setChatId(chat_id);
		for (int i = 0; i < text.length; i++) {
			try {
				sendMessage.setText(text[i]);
				execute(sendMessage);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		return "\uD83D\uDC40";
	}

//	public void getTopBooks(String[] hrefs) {
//        for (String href : hrefs) {
//            Book book = new Book(href);
//            SendMessage message = new SendMessage();
//            message.setChatId(chat_id);
//            message.setText(getInfoBook(book));
//            try {
//                execute(message);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
