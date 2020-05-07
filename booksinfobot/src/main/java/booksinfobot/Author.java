package booksinfobot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Author {
	Document document = null;
	String name = null;
	String noName = "";

//    public Author(String name) throws IOException {
//        document = Jsoup.connect("https://www.surgebook.com/" + name).get();
//        this.name = name;
//    }

	public Author(String name) throws IOException {
		try {
			document = Jsoup.connect("https://www.surgebook.com/" + name).get();
		} catch (Exception e) {
				this.noName = "There are no any author with name: " + name;
				System.out.println("Author. " + noName);
		}
		this.name = name;
	}

	public String getNickName() {
		return this.name;
	}
	
	public String getName() {
		Elements namePerson = document.getElementsByClass("author-name bold");
		return namePerson.text();
	}

	public String getBio() {
		Elements bioPerson = document.getElementsByClass("author-bio");
		return bioPerson.text();
	}

	public String getImg() {
		Elements elements = document.getElementsByClass("user-avatar");
		String url = elements.attr("style");
		// чистим url от лишнего
		url = url.replace("background-image: url('", "");
		url = url.replace("');", "");
		return url;
	}

	public String getInfoPerson() {
		String info = null;
		info = "Имя: " + getName() + "\n";
		info += "Стастус: " + getBio() + "\n";

		Elements names = document.getElementsByClass("info-stats-name");
		Elements values = document.getElementsByClass("info-stats-num");

		for (int i = 0; i < names.size(); i++) {
			info += names.get(i).text() + " : " + values.get(i).text() + "\n";
		}

		return info + getBooks() + "/n" + getFollowers();
	}

	public String getBooks() {
		try {
			document = Jsoup.connect("https://www.surgebook.com/" + name + "/books/all").get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String text = "\nСписок книг: \n";
		ArrayList<String> listUrlBooks = new ArrayList<String>();

		Elements booksName = document.getElementsByClass("book_view_mv1v2_title");
		Elements booksUrl = document.getElementsByClass("book_view_mv1v2_cover");

		System.out.println("This author has " + booksName.size() + " books");
		for (int i = 0; i < booksUrl.size(); i++) {
			// System.out.println(booksName.get(i).text());
			text += booksName.get(i).text() + "\n";
			listUrlBooks.add(booksUrl.get(i).attr("href"));
		}

		HashMap<String, Integer> info = getStats(listUrlBooks);

		text += "\n\nКоличество лайков на книгах: " + info.get("Лайки") + "\n";
		text += "Количество просмотров на книгах: " + info.get("Просмотры") + "\n";
		text += "Количество комментариев на книгах: " + info.get("Комментарии");
		return text;
	}

	private HashMap<String, Integer> getStats(ArrayList<String> listURL) {
		HashMap<String, Integer> info = new HashMap<String, Integer>();
		info.put("Лайки", 0);
		info.put("Комментарии", 0);
		info.put("Просмотры", 0);

		for (String url : listURL) {
			try {
				document = Jsoup.connect(url).get();

				Elements elements = document.getElementsByClass("font-size-14 color-white ml-5");
				info.put("Лайки", info.get("Лайки") + Integer.valueOf(elements.get(0).text()));
				info.put("Комментарии", info.get("Комментарии") + Integer.valueOf(elements.get(1).text()));
				info.put("Просмотры", info.get("Просмотры") + Integer.valueOf(elements.get(2).text()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return info;
	}

	public String getFollowers() {
		try {
			document = Jsoup.connect("https://www.surgebook.com/" + name + "/followers").get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String text = "\nИмена подписчиков: \n";
		ArrayList<String> listUrlFollowers = new ArrayList<String>();

		Elements followersName = document.getElementsByClass("user_view_mv3_name");
		Elements followersUrl = document.getElementsByClass("user_view_mv3_avatar");

		System.out.println("This author has " + followersName.size() + " followers");
		for (int i = 0; i < followersUrl.size(); i++) {
			System.out.println(followersName.get(i).text());
			text += followersName.get(i).text() + " " + followersUrl.get(i).attr("href") + "\n";
			listUrlFollowers.add(followersUrl.get(i).attr("href"));
		}

		HashMap<String, Integer> info = getFollowersStats(listUrlFollowers);

		text += "\n\nСуммарное количество книг у подписчиков: " + info.get("Книги") + "\n";
		text += "Суммарное количество блогов у подписчиков: " + info.get("Блог") + "\n";
		text += "Суммарное количество стихов у подписчиков: " + info.get("Стихи") + "\n";
		text += "Суммарное количество подписчиков у подписчиков: " + info.get("Подписчиков") + "\n";
		return text;
	}

	private HashMap<String, Integer> getFollowersStats(ArrayList<String> listURL) {
		HashMap<String, Integer> info = new HashMap<String, Integer>();
		info.put("Книги", 0);
		info.put("Блог", 0);
		info.put("Стихи", 0);
		info.put("Подписчиков", 0);

		for (String url : listURL) {
			try {
				document = Jsoup.connect(url).get();

				Elements elements = document.getElementsByClass("info-stats-num");
				info.put("Книги", info.get("Книги") + Integer.valueOf(elements.get(0).text()));
				info.put("Блог", info.get("Блог") + Integer.valueOf(elements.get(1).text()));
				info.put("Стихи", info.get("Стихи") + Integer.valueOf(elements.get(2).text()));
				info.put("Подписчиков", info.get("Подписчиков") + Integer.valueOf(elements.get(3).text()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return info;
	}
}