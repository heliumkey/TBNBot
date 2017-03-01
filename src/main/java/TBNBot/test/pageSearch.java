package TBNBot.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class pageSearch {

    //god damnit change this to the google API
    public static void youtube(String searchStr){
        try {
            Document doc = Jsoup.connect("http://www.youtube.com/results?search_query=" + searchStr).get();
            System.out.println(doc.location());
        }
        catch(IOException e){
            System.out.println("pageSearch.youtube() IOException!: " + e);
        }
    }
}
