package booksinfobot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
//	Book book = new Book();
	private Long chat_id;
	String lastMessage = "";
	ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

	/*
	 * public void onUpdateReceived(Update update) { update.getUpdateId(); chat_id =
	 * update.getMessage().getChatId(); System.out.println("chat_id=" + chat_id +
	 * " input_text = " + update.getMessage().getText()); SendMessage sendMessage =
	 * new SendMessage().setChatId(update.getMessage().getChatId());
	 * sendMessage.setText(input(update.getMessage().getText())); try {
	 * execute(sendMessage); } catch (TelegramApiException e) { e.printStackTrace();
	 * } String text = update.getMessage().getText();
	 * 
	 * if(text.contains("person")){ text = text.replace("/person ", "");
	 * getPerson(text, update.getMessage().getChatId()); } }
	 */

	public void onUpdateReceived(Update update) {
		update.getUpdateId();
		chat_id = update.getMessage().getChatId();
		String text = update.getMessage().getText();
		System.out.println("ChatID=" + chat_id + "; Text: " + text);
		SendMessage sendMessage = new SendMessage().setChatId(chat_id);
		sendMessage.setReplyMarkup(replyKeyboardMarkup);

		if (text.contains("person")) {
			text = text.replace("/person ", "");
			getPerson(text, chat_id);
		}else {
			 try {
		            sendMessage.setText(getMessage(text));
		            execute(sendMessage);
		        } catch (TelegramApiException e) {
		            e.printStackTrace();
		        }
		}
	}

	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "@booksinfobot";
	}

//	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "1161419085:AAGb_c5WNql9jXAcCyvPR6RUS1JPncgs9ZE";
	}

	/*
	 * public String input(String msg) { String result = "I dont know what to do";
	 * if (msg.contains("book")) { System.out.println("msg.contains(\"book\")");
	 * result = getInfoBook(); System.out.println("getInfoBook(); DONE");
	 * System.out.println(result); } return result; }
	 */

	/*
	 * public String getInfoBook(){ try { URL url = new URL(book.getImg());
	 * System.out.println("URL url = new URL(book.getImg()); DONE"); // берем сслыку
	 * на изображение BufferedImage img = ImageIO.read(url); // качаем изображение в
	 * буфер File outputfile = new File("image.jpg"); //создаем новый файл в который
	 * поместим //скаченое изображение ImageIO.write(img, "jpg", outputfile);
	 * //преобразовуем наше буферное изображение //в новый файл SendPhoto sendPhoto
	 * = new SendPhoto().setChatId(chat_id); sendPhoto.setPhoto(outputfile);
	 * execute(sendPhoto); System.out.println("Photo was sended"); } catch
	 * (Exception e){ System.out.println("File not found"); e.printStackTrace(); }
	 */

	/*
	 * String title = book.getTitle(); System.out.println("Title= " + title); String
	 * author = book.getAutorName(); System.out.println("Author= " + author); String
	 * geners = book.getGeners(); System.out.println("Gener= " + geners);
	 */

	/*
	 * String info = book.getTitle() + "\nАвтор: " + book.getAutorName() +
	 * "\nЖанр: " + book.getGeners() + "\n\nОписание\n" + book.getDescription() +
	 * "\n\nКоличество лайков " + book.getLikes() + "\n\nПоследние комментарии\n" +
	 * book.getCommentList();
	 * System.out.println("String info = book.getTitle()  DONE");
	 * 
	 * return info; }
	 */

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
				System.out.println("Information that this site dosn't have  author " + author.name + " was send to chatID = " + chat_id);
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

//        if (message.equals("/person GGhe4ka")) {
//            String person = message.replace("/person ", "");
//            return getInfoPerson(person);
//        }
//
//        if (message.equals("Информация о книге \"Девушка с розовыми волосами.\"")) {
//            return getInfoBook(book);
//        }
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
            return "Выбарть...";
        }

        if (message.equals("Стихи") || message.equals("Книги\uD83D\uDCDA")) {
            lastMessage = message;
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

        boolean b = message.equals("Сегодня")
                || message.equals("За неделю")
                || message.equals("За месяц")
                || message.equals("За все время");

//        if (lastMessage != null && lastMessage.equals("Стихи") && b) {
//            String[] poems = top.getTopPoems(message);
//            return getTopPoem(poems);
//        }

//        if (lastMessage != null && lastMessage.equals("Книги\uD83D\uDCDA") && b) {
//            String[] books = top.getTopBook(message);
//            getTopBooks(books);
//            return "\uD83D\uDCDA";
//        }

        return message;
    }
}