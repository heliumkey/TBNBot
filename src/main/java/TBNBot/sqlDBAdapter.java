package TBNBot;

import java.sql.*;


public class sqlDBAdapter {

    static String dbURL = "jdbc:sqlite:seen.db";
    Connection connection;

    sqlDBAdapter(){
        try{

            this.connection = dbConnect();
            createTable();
        }
        catch(SQLException e){

           System.out.println(e);
        }
    }

    public Connection dbConnect() throws SQLException {

        connection = DriverManager.getConnection(dbURL);

        DatabaseMetaData meta = connection.getMetaData();

        //System.out.println(meta.getDriverName());

        return connection;

    }

    public void createTable() throws SQLException {

        String seenTableString = "CREATE TABLE IF NOT EXISTS seenTable (\n"
                + " user_id integer PRIMARY KEY, \n"
                + " user_name text NOT NULL, \n"
                + " channel_name text NOT NULL, \n"
                + " last_seen_date text NOT NULL\n"
                + ");";

        Statement statement = this.connection.createStatement();

        statement.execute(seenTableString);
    }
    public void listTables() throws SQLException {

        String listStatement = "SELECT name FROM sqlite_master WHERE type='table'";

        Statement statement = this.connection.createStatement();
        ResultSet results = statement.executeQuery(listStatement);

        while(results.next()){
            System.out.println(results.getString(1));
        }

    }
    public void listColumns(String table) throws SQLException {



        String query = "SELECT * FROM seenTable";

        Statement statement = this.connection.createStatement();
        //statement.setString(1, table);
        ResultSet rs = statement.executeQuery(query);



        while(rs.next()) {
            System.out.println(rs.getString("user_id") + " " + rs.getString("user_name") + " " + rs.getString("channel_name") + " " + rs.getString("last_seen_date"));
        }
    }

    public void insertSeen(String userName, String channelName, String seenDate) throws SQLException {

        int searchIndex = searchSeen(userName.toLowerCase(), channelName);

        if(searchIndex == 0){
            String statement = "INSERT INTO seenTable(user_name, channel_name, last_seen_date) VALUES(?,?,?)";

            PreparedStatement pstatement = this.connection.prepareStatement(statement);

            pstatement.setString(1, userName.toLowerCase());
            pstatement.setString(2, channelName);
            pstatement.setString(3, seenDate);

            pstatement.executeUpdate();

        }
        else{
            String sqlUpdateStr = "UPDATE seenTable SET user_name = ?, \n"
                    + " channel_name = ?, \n"
                    + " last_seen_date = ?\n"
                    + " WHERE user_id = ?";

            PreparedStatement pstatement = this.connection.prepareStatement(sqlUpdateStr);

            pstatement.setString(1, userName.toLowerCase());
            pstatement.setString(2, channelName);
            pstatement.setString(3, seenDate);
            pstatement.setInt(4, searchIndex);


            pstatement.executeUpdate();
        }

    }
    //searchSeen returns index if username from channel is found; otherwise, returns 0
    public int searchSeen(String username, String channel) throws SQLException {

        String pstatementStr = "SELECT user_id FROM seenTable WHERE user_name = ? AND channel_name = ?";

        PreparedStatement pstatement = this.connection.prepareStatement(pstatementStr);
        pstatement.setString(1, username);
        pstatement.setString(2, channel);
        int result = 0;

        ResultSet rs = pstatement.executeQuery();
        if(rs.next()){
            result = Integer.parseInt(rs.getString(1));
        }
        return result;
    }

    public String stringSeen(String username, String channel) throws SQLException{

        String dateSelect = "SELECT last_seen_date FROM seenTable where user_name = ? AND channel_name = ?";
        PreparedStatement pstatement = this.connection.prepareStatement(dateSelect);

        pstatement.setString(1, username.toLowerCase());
        pstatement.setString(2, channel);

        ResultSet rs = pstatement.executeQuery();
        if(rs.next()) {
            String lastSeen = rs.getString(1);
            return lastSeen;
        }
        else {
            return "I haven't seen " + username + ".";
        }
    }
}









