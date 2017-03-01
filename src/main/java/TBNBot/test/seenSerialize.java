package TBNBot.test;

import java.io.*;
import java.util.Date;
import java.util.HashMap;

public class seenSerialize {
    
    static String absPath;
    static HashMap<String, Date> myMap = new HashMap<String, Date>();
    static File seenDB = new File("seenDB.ser");


    public static void readDB(){

        absPath = seenDB.getAbsolutePath();
        if(seenDB.exists()){
            try{
                FileInputStream dbIn = new FileInputStream(seenDB);
                ObjectInputStream dbInStream = new ObjectInputStream(dbIn);
                myMap = (HashMap) dbInStream.readObject();
                System.out.println(myMap);
            }
            catch(IOException e){
                System.out.println(e);
            }
            catch(ClassNotFoundException c){
                System.out.println(c);
            }
        }
    }

    public static void writeDB(){

        try {
            FileOutputStream dbOut = new FileOutputStream(seenDB);
            ObjectOutputStream dbOutStream = new ObjectOutputStream(dbOut);
            dbOutStream.writeObject(myMap);
        }
        catch(FileNotFoundException e){
            System.out.println(e);
        }
        catch(IOException e){
            System.out.println(e);
        }

    }

    public static void updateDB(String user){
        Date date = new Date(System.currentTimeMillis());
        myMap.put(user, date);
        writeDB();

    }

    public static String seen(String name) throws FileNotFoundException{

        if(myMap.containsKey(name)){
            Date seenDate = myMap.get(name);
            Date currentDate = new Date(System.currentTimeMillis());
            long diffSecs = (currentDate.getTime() - seenDate.getTime()) / 1000;
            long diffMins = diffSecs / 60;
            long diffHours = diffMins / 60;
            long diffDays = diffHours / 24;

            long remDiffMins = diffSecs % 60;
            long remDiffHours = diffMins % 60;
            /*if(diffDays == 0){
                if(diffHours == 0){
                    return (name + " was last seenSerialize " + diffMins + " minutes ago.");
                }
                else{
                    return (name + " was last seenSerialize " + diffHours + " hours, " + remDiffMins + " minutes ago.");
                }
            }
            else {
                return (name + " was last seenSerialize " + diffDays + " days, " + remDiffHours + " hours, " + remDiffMins + " minutes ago.");
            }
            */
            return (name + " was last seenSerialize on " + seenDate + ".");
        }
        else{
            return ("I haven't seenSerialize " + name + ".");
        }
    }
}
