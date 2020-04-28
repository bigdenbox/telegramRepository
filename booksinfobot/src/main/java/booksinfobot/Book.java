package booksinfobot;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Book {
	private Document document;
	private static String SITENAME1 = "https://www.surgebook.com/Kristina_Matsuk/book/losing-control-teryaya-kontrol";
	private static String SITENAME2 = "https://www.surgebook.com/ashly_dueal/book/nochnye-zhivotnye-ast";
	
	
	public Book() {
		connect(); 	
	}
	
	private void connect() {
		try {
			document = Jsoup.connect(SITENAME2).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTitle() {
		return document.title();
	}
	
	public String getLikes() {
		Element element = document.getElementById("likes");
		return element.text();
	}
	
	public String getDescription() {
		Element element = document.getElementById("description");
		return element.text();
	}
	
	public String getGeners() {
		System.out.println("getGeners() STARTED");
		Elements elements = document.getElementsByClass("genres d-block");
		System.out.println("getGeners() DONE = " + elements.text());
		return elements.text();
	}
	
	public String getImg() {
		Elements elements = document.getElementsByClass("cover-book");
        String url = elements.attr("style");
        System.out.println("URL_before= " + url);
        //чистим url от лишнего
        url = url.replace("background-image: url('", "");
        url = url.replace("');", "");
        System.out.println("URL_after= " + url);
        return url;
	}

    //имя автора
    public String getAutorName(){
    	System.out.println("getAutorName() STARTED");
        Elements elements = document.getElementsByClass("text-decoration-none column-author-name bold max-w-140 text-overflow-ellipsis");
        System.out.println("getAutorName() DONE = " + elements.text());
        
        return elements.text();
    }

    //последние комментарии
    public String getCommentList(){
        Elements elements = document.getElementsByClass("comment_mv1_item");

        String comment = elements.text();
        //чистим от ответить
        comment = comment.replaceAll("Ответить", "\n\n");
        //чистим от нравится
        comment = comment.replaceAll("Нравится", "");
        //чистим от дат
        comment = comment.replaceAll("\\d{4}-\\d{2}-\\d{2}", "");
        //чистим от времени
        comment = comment.replaceAll("\\d{4}-\\d{2}-\\d{2}", "");
        return comment;
    }
	
}