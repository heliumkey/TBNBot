package TBNBot;

import org.pircbotx.User;
import org.pircbotx.UserLevel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class kBotXListener extends ListenerAdapter {
    URLDetect detector;
    WolfQuery wQuery;
    sqlDBAdapter sqlAdapter;



    kBotXListener(URLDetect d, WolfQuery q, sqlDBAdapter s){
        detector = d;
        wQuery = q;
        sqlAdapter = s;
    }

    @Override
    public void onMessage(MessageEvent e){
        Date curDate = new Date(System.currentTimeMillis());
        //seenSerialize.updateDB(e.getUser().getNick());
        try {
            sqlAdapter.insertSeen(e.getUser().getNick(), e.getChannel().getName(), curDate.toString());
        }
        catch(SQLException ex){
            System.out.println(ex + " " + "onMessage insert");
        }

        WolfCommand wolf = new WolfCommand(wQuery);
        if(e.getMessage().startsWith("!wa")) {

            List results = wolf.command(e.getMessage().substring(4));
            String[] podTitles = (String[]) results.get(0);
            String[] podElements = (String[]) results.get(1);
            String url = (String) results.get(2);

            e.getBot().sendIRC().message(e.getChannel().getName(),"\u00034" + podTitles[0] + ": " + "\u000f" + podElements[0] + " | " + url);

            for(int i = 1; i < podTitles.length && i <= 5; i++){

                e.getBot().sendIRC().message(e.getChannel().getName(),i + ": " + podTitles[i]);

            }

        }
        if(e.getMessage().startsWith("!ws")){
            String[] msg = e.getMessage().split(" ");
            String builtMsg = new String();
            for(int i = 2; i < msg.length; i++){
                builtMsg = builtMsg.concat(msg[i] + " ");

            }

            for(String entry : wolf.lCommand(builtMsg, Integer.parseInt(msg[1])).split("\n") ){
                e.getBot().sendIRC().message(e.getChannel().getName(), entry);

            }


        }
        if(e.getMessage().startsWith("!q")){

            List results = wolf.command(e.getMessage().substring(3));
            String[] podTitles = (String[]) results.get(0);
            String[] podElements = (String[]) results.get(1);
            String url = (String) results.get(2);

            e.getBot().sendIRC().message(e.getChannel().getName(),"\u00034" + podTitles[0] + ": " + "\u000f" + podElements[0] + " | " + url);

            for(String entry : podElements[1].split("\n") ){
                e.getBot().sendIRC().message(e.getChannel().getName(), entry);
            }

        }
        URL[] list = URLDetect.urlList(detector.detect(e.getMessage()));
        for(URL url : list){
            pageTitle page = new pageTitle(url);
            if(pageTitle.pageTitle() != null){
                e.getBot().sendIRC().message(e.getChannel().getName(), "\u0002" + pageTitle.pageTitle());
            }
        }
        if(e.getMessage().startsWith("!help")){
            e.getBot().sendIRC().message(e.getUser().getNick(), "!q <query>: Fast query Wolfram Alpha");
            e.getBot().sendIRC().message(e.getUser().getNick(), "!wa <query>: List Wolfram Alpha entries");
            e.getBot().sendIRC().message(e.getUser().getNick(), "!ws <index> <query>: Select a Wolfram Alpha entry");
            e.getBot().sendIRC().message(e.getUser().getNick(), "!seen <nick>: Display time since user last seen");
            e.getBot().sendIRC().message(e.getUser().getNick(), "!weather <location>: Display weather");
            e.getBot().sendIRC().message(e.getUser().getNick(), "!foghorn <message>: Announce a message to all nicks in channel (channel ops only)");


        }

        if(e.getMessage().startsWith("!seen")){
            try {
                String msg = sqlAdapter.stringSeen(e.getMessage().substring(6).split(" ")[0], e.getChannel().getName());

                e.getBot().sendIRC().message(e.getChannel().getName(), msg);
            }
            catch(SQLException ex){
                System.out.println(ex + "!seen");
            }

        }
        if(e.getMessage().startsWith("!weather")){

            String location = e.getMessage().substring(9).toLowerCase();

            if(location.equals("moon")){
                String[] weather = Weather.grabWeather(location, 23);
                for(int i = 0; i <= weather.length; i++) {
                    System.out.println(weather[i]);
                    System.out.println(i);
                    e.getBot().sendIRC().message(e.getChannel().getName(), weather[i]);
                }
            }
            else{
                String[] weather = Weather.grabWeather(location, 7);
                for(int i = 0; i <= weather.length; i++){
                    System.out.println(weather[i]);
                    System.out.println(i);
                    e.getBot().sendIRC().message(e.getChannel().getName(), weather[i]);
                }
            }

        }
        if(e.getMessage().startsWith("!foghorn") && e.getUser().getUserLevels(e.getChannel()).contains(UserLevel.OP)){

            String entry = new String();

            int i = 1;
            if(!e.getMessage().substring(8).isEmpty()){
                entry = e.getMessage().substring(9);
                entry = entry.concat(": ");
            }

            for(User user : e.getChannel().getUsers()){
                if(!user.getNick().equals(e.getBot().getNick())){
                    if(i != e.getChannel().getUsers().size()){
                        entry = entry.concat(user.getNick() + ", ");
                    }
                    else{
                        entry = entry.concat(user.getNick());
                    }
                    i++;
                }
                else{
                    i++;
                }


            }
            e.getBot().sendIRC().message(e.getChannel().getName(), entry);
        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent e){
    }

    @Override
    public void onQuit(QuitEvent e){
        //cannot find e.getChannel to insert seen
    }
    @Override
    public void onJoin(JoinEvent e){
        try {
            Date curDate = new Date(System.currentTimeMillis());
            sqlAdapter.insertSeen(e.getUser().getNick(), e.getChannel().getName(), curDate.toString());
        }
        catch(SQLException ex){
            System.out.println(ex + " " + "onJoin insert");
        }

    }
}