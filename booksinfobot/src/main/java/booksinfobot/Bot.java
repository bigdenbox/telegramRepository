package booksinfobot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
//	Book book = new Book();
	private Long chat_id;

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
		chat_id = update.getMessage().getChatId();
		String text = update.getMessage().getText();
		System.out.println("ChatID=" + chat_id + "; Text: " + text);

		if (text.contains("person")) {
			text = text.replace("/person ", "");
			getPerson(text, chat_id);
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
}