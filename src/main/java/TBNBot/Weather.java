package TBNBot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;


public class Weather {

    static String url = "http://wttr.in/";

    public static String[] grabWeather(String location, int size){

        try {

            Document wittr = Jsoup.connect(url + location).get();
            Elements wittrBody = wittr.select("body");
            String wittrLines = wittrBody.text();
            BufferedReader bufReader = new BufferedReader(new StringReader(wittrLines));
            String[]  lines = new String[size];    //create array to store text lines
            for(int i = 0 ; i < size ; i++){       //iterate over first 7 lines of webpage
                lines[i] = bufReader.readLine();
            }
                                                //TODO: grab first weather stuff from weather text in a non-terrible way

            return lines;

        }
        catch(IOException e){
            return null;
        }
    }
}
