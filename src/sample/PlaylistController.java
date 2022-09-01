package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Song;

import java.sql.*;

public class PlaylistController {
    String playlistName;
    Date dateAdded;

    private PlaylistController(){
        //empty constructor
    }
    static Connection connection;




    private static void setConnection() throws SQLException,ClassNotFoundException {
        String url = "jdbc:mysql://localhost/musicplayer";
        String uname = "root";
        String pwd = "phani@123";
        //String pwd = "12Ccbu12!";
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(url, uname, pwd);
            System.out.println("Connection succesful");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private static ObservableList<Song> getPlaylistObjects(ResultSet rs) {
        try {
            ObservableList<Song> playlistData = FXCollections.observableArrayList();
            while (rs.next()) {
                Song song = new Song();
                song.setPlaylistName(rs.getString("playlist_name"));
                String not=rs.getInt("no_of_tracks")+"";
                song.setNoOfStreams(not);
                playlistData.add(song);
            }
            return playlistData;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static ObservableList<Song> getallPlaylists(int userid) throws SQLException, ClassNotFoundException {

        setConnection();


        try {
            String query =String.format("SELECT playlist_name,no_of_tracks FROM playlists p WHERE user_id=%d",userid);

            System.out.println(query);
            PreparedStatement prepmnt= connection.prepareStatement(query);

            ResultSet rs;

            rs = prepmnt.executeQuery(query);
            ObservableList<Song> allPlaylistData=getPlaylistObjects(rs);
            return allPlaylistData;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }


}
