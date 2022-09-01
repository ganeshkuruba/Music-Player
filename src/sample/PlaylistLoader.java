package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.*;

public class PlaylistLoader extends ActionEvent {

    private static Connection connection;


    @FXML
    private Label lbl1;

    @FXML
    private JFXListView<Song> lst1;
    @FXML
    private TableView<Song> playlistTable;

    @FXML
    private  TableColumn<Song, String> playlistColumn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton doneBtn;

    @FXML
    private JFXButton createNewBtn;

    @FXML
    private HBox hbox;

    @FXML
    private JFXTextField textField;

    public String songName;
    public static int userId;

    public void initData(Song song,int userid){
        songName=song.getSongName();
        userId=userid;   //userId fetched from the main screen
        System.out.println("test 3 user id "+userId);

    }


    private Song selectedSong;




    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {

       playlistColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("playlistName"));
        ObservableList<Song> playlistData = FXCollections.observableArrayList();
        playlistData=getallPlaylists();
        playlistTable.refresh();
        playlistTable.setItems(playlistData);

    }

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

    public static ObservableList<Song> getallPlaylists() throws SQLException, ClassNotFoundException {

        setConnection();
        String query = String.format("SELECT playlist_name FROM playlists WHERE user_id=%d",Controller.userId);
        System.out.println(query);
        System.out.println("test userid "+userId);
        PreparedStatement prepmnt= null;
        try {

            prepmnt = connection.prepareStatement(query);
            ResultSet rs;

            rs = prepmnt.executeQuery(query);
            ObservableList<Song> allPlaylistData=getPlaylistObjects(rs);
            return allPlaylistData;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    private static ObservableList<Song> getPlaylistObjects(ResultSet rs) {
        try {
            ObservableList<Song> playlistData = FXCollections.observableArrayList();
            while (rs.next()) {
                Song song = new Song();
                song.setPlaylistName(rs.getString("playlist_name"));
                playlistData.add(song);
            }
            return playlistData;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @FXML
    private void createNewPlaylist(){
        doneBtn.setText("Create");
        hbox.setVisible(true);
    }
    @FXML
    private void doneMethod() throws SQLException, ClassNotFoundException {
        setConnection();
        if(doneBtn.getText().equals("Create")){
            String newPlaylist=textField.getText();
            if(newPlaylist.equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Message");
                alert.setHeaderText(null);
                alert.setContentText("Playlist name cannot be empty");
                alert.showAndWait();

            }else {
                long d = System.currentTimeMillis();
                Date datee = new Date(d);
                System.out.println(datee);
                String query = String.format("INSERT INTO playlists(user_id,playlist_name,date_added)" + " values(\"%d\",\"%s\",\"%s\")", Controller.userId, newPlaylist, datee);
                PreparedStatement statement = connection.prepareStatement(query);

                statement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Message");
                alert.setHeaderText(null);
                alert.setContentText("New playlist " + newPlaylist + " created.");
                alert.showAndWait();
                Stage stage = (Stage) doneBtn.getScene().getWindow();
                stage.close();
            }
        }else{
            Song curr_playlist = playlistTable.getSelectionModel().getSelectedItem();
            String playlistString=curr_playlist.getPlaylistName();
            System.out.println(playlistString);
            String query1=String.format("SELECT playlist_id FROM playlists WHERE playlist_name=\"%s\" AND user_id=\"%d\"",playlistString,Controller.userId);
            PreparedStatement statement1 = connection.prepareStatement(query1);
            ResultSet rs1=statement1.executeQuery();
            rs1.next();
            System.out.println(rs1.getString(1)+" playlist_id retreived");
            int playlistId=Integer.parseInt(rs1.getString(1));

            String query2="SELECT track_id FROM track WHERE track_name=?";
            PreparedStatement statement2 = connection.prepareStatement(query2);
            System.out.println(songName);
            statement2.setString(1,songName);
            ResultSet rs2=statement2.executeQuery();
            rs2.next();
            System.out.println(rs2.getString(1)+" track_id retreived");
            int trackId=Integer.parseInt(rs2.getString(1));

            String toCheckIfExistsInPlaylist=String.format("SELECT track_id FROM p_contains WHERE track_id=\"%d\" AND user_id=\"%d\" AND playlist_id=\"%d\"",trackId,Controller.userId,playlistId);
            PreparedStatement pstCheck=connection.prepareStatement(toCheckIfExistsInPlaylist);
            ResultSet rstcheck=pstCheck.executeQuery();
            if(rstcheck.next()){
                System.out.println(songName+" is already in "+playlistString+" playlist");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Song is already in "+playlistString+" playlist");
                alert.showAndWait();
            }
            else {
                String insertIntoPcontainsTable = String.format("INSERT INTO p_contains VALUES(%d,%d,%d)", trackId, playlistId, Controller.userId);

                PreparedStatement statement3 = connection.prepareStatement(insertIntoPcontainsTable);
                int affectedRows = statement3.executeUpdate();
                System.out.println("Affected rows are " + affectedRows);

                Stage stage = (Stage) doneBtn.getScene().getWindow();
                stage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("");
                alert.setHeaderText(null);
                alert.setContentText("Song added to " + playlistString);
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void quitMethod(){

        Stage stage= (Stage) doneBtn.getScene().getWindow();
        stage.close();

    }


    @FXML
    public void CreateNewDisappear(){

        hbox.setVisible(false);
        doneBtn.setText("Done");

    }

}
