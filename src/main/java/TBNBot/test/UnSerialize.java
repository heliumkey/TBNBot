package TBNBot.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UnSerialize {

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
    public static void outputDB() {
        for(Map.Entry<String, Date> entry : myMap.entrySet()){
            String key = entry.getKey();
            Date date = entry.getValue();

            System.out.println(key + " : " + date.toString());

        }
    }

    public static void main(String[] args){
        readDB();
        outputDB();
    }

}
