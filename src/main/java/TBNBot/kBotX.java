package TBNBot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import java.nio.charset.Charset;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class kBotX {

    static URLDetect detector = new URLDetect();
    static WolfQuery wolf;
    static Properties defaultProp = new Properties();
    static ArrayList<String> channels = new ArrayList<String>();
    static String chanlist;
    static sqlDBAdapter sqlConnection = new sqlDBAdapter();

    static String nick;
    static boolean ssl;
    static int port;
    static String server;
    static String sslStr;

    static Scanner scan = new Scanner(System.in);

    static String wolframKey;

    static String versionString = "TBNBot v0.1";


    public static void main(String[] args) throws Exception {

        //attempt to load config file
        try{
            FileInputStream in = new FileInputStream("defaultProperties");  //create config file defaultProperties
            defaultProp.load(in); //load config file



        }
        // set all config information if no config exists
        catch(IOException e) {
            System.out.println("Config not found, please enter info:");

            System.out.print("Nick: ");
            nick = scan.next();
            System.out.println();
            defaultProp.setProperty("nick", nick);

            System.out.print("Server: ");
            server = scan.next();
            System.out.println();
            defaultProp.setProperty("server", server);

            System.out.print("SSL (Y/N): ");
            sslStr = scan.next();
            if (sslStr.equals("Y")) {
                ssl = true;
            } else {
                ssl = false;
            }
            System.out.println();
            defaultProp.setProperty("ssl", Boolean.toString(ssl));

            System.out.print("Port: ");
            port = scan.nextInt();
            System.out.println();

            defaultProp.setProperty("port", Integer.toString(port));
            System.out.print("Channels (separate by comma):");
            chanlist = scan.next();

            defaultProp.setProperty("channels", chanlist);
            System.out.println();
            System.out.println("Wolfram API key: ");
            wolframKey = scan.next();

            defaultProp.setProperty("apikey", wolframKey);


            FileOutputStream out = new FileOutputStream("defaultProperties");
            defaultProp.store(out, "Configuration");
            out.close();

        }

        chanlist = defaultProp.getProperty("channels");
        channels = new ArrayList<String>(Arrays.asList(chanlist.substring(0, chanlist.length()).split(", ")));
        nick = defaultProp.getProperty("nick");
        ssl = Boolean.getBoolean(defaultProp.getProperty("ssl"));
        server = defaultProp.getProperty("server");
        port =  Integer.parseInt(defaultProp.getProperty("port"));
        System.out.println(channels);
        wolf = new WolfQuery(wolframKey);



        @SuppressWarnings("unchecked")
        Configuration configuration = new Configuration.Builder()
                .setName(nick)
                .setRealName(nick)
                .setLogin(nick)
                .setVersion(versionString)
                .addServer(server, port)
                .addAutoJoinChannels(channels)
                .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates())
                .addListener(new kBotXListener(detector, wolf, sqlConnection))
                .setEncoding(Charset.forName("UTF-8"))
                .buildConfiguration();

        //read seenSerialize database
        //seenSerialize.readDB();

        sqlConnection.dbConnect();

        PircBotX bot = new PircBotX(configuration);
        //start bot instance
        bot.startBot();


    }
}