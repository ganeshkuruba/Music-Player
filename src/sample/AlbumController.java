package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AlbumController {

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



    private static ObservableList<Song> getAlbumObjects(ResultSet rs) {
        try {
            ObservableList<Song> albumData = FXCollections.observableArrayList();
            while (rs.next()) {
                Song song = new Song();
//                song.setArtistBtn();
                song.setAlbumName(rs.getString("album_name"));
                albumData.add(song);
            }
            return albumData;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static ObservableList<Song> getallAlbums() throws SQLException, ClassNotFoundException {

        setConnection();
        String query = "SELECT album_name from album;";
        PreparedStatement prepmnt= null;
        try {
            prepmnt = connection.prepareStatement(query);
            ResultSet rs;
            rs = prepmnt.executeQuery(query);

            ObservableList<Song> allAlbumData=getAlbumObjects(rs);
            return allAlbumData;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }



}
