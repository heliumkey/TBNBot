package TBNBot;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;
import org.apache.http.auth.AUTH;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.json.*;


import java.io.IOException;
import java.net.Authenticator;
import java.time.Duration;
import java.util.List;

public class YTSearch {

    private static String query;
    private static YouTube yt;
    private static String apiKey;




    YTSearch(String key){
        apiKey = key;

    }
    public String search(String q){
        query = q;
        try{

            yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest httpRequest) throws IOException {

                }
            }).setApplicationName("Genard-IRCbot").build();

            YouTube.Search.List search = yt.search().list("id,snippet");
            search.setKey(apiKey);
            search.setQ(query);
            search.setType("video");
            //search.setMaxResults(1L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            if(!searchResultList.isEmpty()){

                String yturl = "https://www.youtube.com/watch?v=";
                String resultID = searchResultList.get(0).getId().getVideoId();

                yturl = yturl.concat(resultID);

                String resultURL = "\u0002" + "\u00039";
                resultURL = resultURL.concat(searchResultList.get(0).getSnippet().getTitle());


                YouTube.Videos.List videoDetailList = yt.videos().list("snippet,contentDetails,statistics");
                videoDetailList.setId(resultID);
                videoDetailList.setKey(apiKey);

                VideoListResponse videoDetailListResponse = videoDetailList.execute();

                JSONObject json = new JSONObject(videoDetailListResponse);
                String d = json.getJSONArray("items").getJSONObject(0).getJSONObject("contentDetails").getString("duration");
                Duration duration = Duration.parse(d);
                PeriodFormatter formatter = new PeriodFormatterBuilder()
                        .appendDays()
                        .appendSuffix("d")
                        .appendHours()
                        .appendSuffix("h")
                        .appendMinutes()
                        .appendSuffix("m")
                        .appendSeconds()
                        .appendSuffix("s")
                        .toFormatter();
                String formattedDuration = formatter.print(new Period(duration.toMillis()));

                resultURL = resultURL.concat("\u0002" + "\u0003" + " - " + formattedDuration);




                resultURL = resultURL.concat(" | " + yturl);

                return resultURL;

            }
            else{
                return "No results!";

            }

        }
        catch(Exception e){
            System.out.println(e.toString());
            return null;

        }

    }





}
