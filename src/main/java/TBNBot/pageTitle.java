package TBNBot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;


public class pageTitle {
    static URL url;
    pageTitle(URL url){
        pageTitle.url = url;
    }

    public static String pageTitle(){

        try{
            Document doc = Jsoup.connect(url.toString())
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .timeout(10000)
                    .get();

            return doc.title();
        }
        catch(IOException e){
            System.out.println("pageTitle IOException! :" + e);
        }
        return null;
    }

}
