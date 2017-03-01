package TBNBot;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

public class URLDetect {

    //Not as good
    public static URL detectAlt(String s) {
        String string = s;

        String [] parts = string.split("\\s+");

        for( String item : parts ) try {
            URL url = new URL(item);

            return url;

        } catch (MalformedURLException e) {

        }

        return null;
    }

    public ArrayList detect(String text) {
        ArrayList links = new ArrayList();

        String regex = "\\(?\\b((http|https)://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while(m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }

        return links;
    }

    public static URL[] urlList(ArrayList urlStr){
        URL[] list = new URL[urlStr.size()];
        String prefix = "http://";

        for(Object item : urlStr){
            int i = 0;
            System.out.println("readURLloop");
            try{
                list[i] = new URL(item.toString());
            }
            catch(MalformedURLException me){
                System.out.println("BAD URL, TRYING PREFIX");
                try{
                    list[i] = new URL(prefix + item.toString());
                }
                catch(MalformedURLException e){
                    System.out.println("NOPE BAD URL");
                }
            }
            System.out.println(list[i]);
            i++;
        }
        return list;
    }
    public static String readURL(URL url){
        String content = "";
        try {
            Scanner scan = new Scanner(url.openStream());


            while (scan.hasNext())
                content += scan.nextLine();
            scan.close();

        }
        catch(IOException e){
            System.out.println("readURL IOException! :" + e);

        }
        return content;
    }

    public static String findTitle(String str){


        System.out.println("findTitle");
        System.out.println("Printing page content before search...:" + "\n");

        String tagOpen = "<title>";
        String tagClose = "</title>";

        int begin = str.indexOf(tagOpen) + tagOpen.length();
        int end = str.indexOf(tagClose);

        return str.substring(begin, end);
    }

}