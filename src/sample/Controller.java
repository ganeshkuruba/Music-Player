package sample;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import jaco.mp3.player.MP3Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller extends ActionEvent {

    static Connection connection;

    @FXML
    private  TableColumn<Song,String> songColumn;

    @FXML
    private TableColumn<Song,Void> goToArtist;

    @FXML
    TableColumn<Song, Void> addSongToPlaylistCol = new TableColumn("Add To Playlist");
    private boolean isaddSongToPlaylistColSet=false;

    @FXML
    TableColumn<Song, Void> addToDeleteSongCol = new TableColumn("Remove From Playlist");
    private boolean isaddToDeleteSongColSet=false;

    @FXML
    TableColumn<Song, Void> addToFavotiteCol = new TableColumn("Add To Favorite");
    private boolean isaddToFavotiteCol=false;

   // @FXML
    //TableColumn<Song, Void> addNoOfTracksCol = new TableColumn("No Of Tracks");
    //private boolean isaddNoOfTracksCol=false;

    @FXML
    private TableColumn<Song, String> addNoOfTracksCol;//=new TableColumn("No Of Tracks");;
    private boolean isaddNoOfTracksCol=false;

    @FXML
    private  TableView<Song> songTable;

    @FXML
    private JFXButton playBtn;
    @FXML
    private JFXButton allSongs;

    @FXML
    private JFXButton allArtists;
    @FXML
    private JFXButton allAlbums;
    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXTextField nowPlaying;

    @FXML
    private JFXTextField SRC;
    @FXML
    private JFXButton searchBtn;

    @FXML
    private JFXButton Favourites;

    @FXML
    private JFXButton recently_played;

    @FXML
    private JFXButton playlistMainBtn;

    @FXML
    private  Label HiUserLabel;


    @FXML
    private JFXButton newPlaylistMainBtn;

    @FXML
    private JFXTextField newPlaylistTextField;

    @FXML
    private JFXButton disappearTextFieldBtn;

    @FXML
    private AnchorPane anchorpaneCreatePlaylist;


    @FXML
    private JFXButton okBtn;



    MP3Player mp3;


    @FXML
    private JFXButton pauseBtn;

    @FXML
    private ImageView pauseImage;

    @FXML
    private ImageView playImage;

    @FXML
    private Label topLabel;


    @FXML
    private JFXButton logonIconBtn;

    @FXML
    private Label loginLabel;

    @FXML
    private ImageView loginIcon;

    @FXML
    private JFXButton trandingBtn;

    @FXML
    private JFXButton clrRecent;

    private static boolean isCredentialsCorrect=false;


    public static int count=0;

    public static String userEmailid;
    public static String userName;
    public static int userId;
     String songName="";
    public  MP3Player player;
    public static int playPause=0;

    private Image img=new Image("sample/icons/Add-icon.png");


    public static void initDataSignUp(String username,String email,String password,String secretkey) throws SQLException, ClassNotFoundException {

        setConnection();
        String toCheckUserRegisteredOrNot=String.format("SELECT user_id FROM users WHERE user_name=\"%S\" AND email_id=\"%s\"",username,email);
        PreparedStatement pst=connection.prepareStatement(toCheckUserRegisteredOrNot);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Hmmmm....You've already registered."+"\n"+"Just hop in and get some good music ;)");
            alert.showAndWait();

        }
        else {
            String query = "INSERT INTO users(user_name,password,email_id,secret_key) VALUES(?,?,?,?);";
            PreparedStatement statement1 = connection.prepareStatement(query);
            statement1.setString(1, username);
            statement1.setString(2, password);
            statement1.setString(3, email);
            statement1.setString(4, secretkey);

            int affectedRows = statement1.executeUpdate();
            System.out.println("Inserted new user to  the DB " + affectedRows);
        }
        userEmailid=email;
        userName=username;

    }

    public static  void initData(String useremail,String username){
        System.out.println(useremail+" this is the username got from the login screen");
        userEmailid=useremail;
        userName=username;
    }

    @FXML
   private void initialize() throws Exception {

        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
//        TODO :@phani , update db and uncomment this line String query = "SELECT track_name,artist_name from track_artists";
        String query = "SELECT track_name from track";
        ObservableList<Song> songList = getallSongs(query);
        songTable.refresh();
        songTable.setItems(songList);
        songColumn.setText("Track");
        goToArtist.setText("Go to Artist");
        goToArtist.setVisible(false);
        addButtonToTable();
        addSongToPlaylistBtn();
        addSongToPlaylistCol.setVisible(true); //this column should be visible only when thw tracks are displayed
        addDeleteSongInPlaylistButtonToTable();
        addToDeleteSongCol.setVisible(false);
        addToFavoriteButtonToTable();
        addToFavotiteCol.setVisible(true);
        topLabel.setText("All Songs");
       //addNoOfTracksToPlaylist();

        addNoOfTracksCol.setVisible(false);
        clrRecent.setVisible(false);



}

private void addNoOfTracksToPlaylist() throws SQLException, ClassNotFoundException {
        //String noOfStreams="";
    addNoOfTracksCol.setCellValueFactory(new PropertyValueFactory<Song, String>("noOfStreams"));
   // addNoOfTracksCol.setCellValueFactory(new PropertyValueFactory<Song, String>("noOfStreams"));
    //addNoOfTracksToPlaylist();
    String query = "SELECT no_of_tracks from playlists";
    setConnection();
    PreparedStatement pst=connection.prepareStatement(query);
    ResultSet rs=pst.executeQuery(query);

   ObservableList<Song> noOfTracksList =getNoOfStreams(query);//= FXCollections.observableArrayList();

   // System.out.println(noOfTracksList.get(0).getNoOfStreams());


    //songTable.refresh();

    //addNoOfTracksCol.add()
    songTable.setItems(noOfTracksList);
}

    private ObservableList<Song> getNoOfStreams(String query) throws SQLException, ClassNotFoundException {
        setConnection();
        PreparedStatement prepmnt= null;
        try {
            prepmnt = connection.prepareStatement(query);
            ResultSet rs;
            rs = prepmnt.executeQuery(query);
            ObservableList<Song> allSongData=getNoOfTracksObjects(rs);
            return allSongData;

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;

    }

    private ObservableList<Song> getNoOfTracksObjects(ResultSet rs) {
        try {
            ObservableList<Song> noOfTracksList = FXCollections.observableArrayList();
            while (rs.next()) {
                Song song = new Song();
                String not=rs.getInt("no_of_tracks")+"";
                song.setNoOfStreams(not);
                System.out.println(rs.getInt("no_of_tracks"));
                noOfTracksList.add(song);

            }
            return noOfTracksList;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;


    }


    private void addSongToPlaylistBtn(){

    Callback<TableColumn<Song, Void>, TableCell<Song, Void>> cellFactory = new Callback<TableColumn<Song, Void>, TableCell<Song, Void>>() {
        @Override
        public TableCell<Song, Void> call(final TableColumn<Song, Void> param) {
            final TableCell<Song, Void> cell = new TableCell<Song, Void>() {


                private ImageView addIconOnBtn=new ImageView(new Image("sample/icons/Add-icon.png"));


                @FXML
                private JFXButton btn = new JFXButton("");
                {

                    btn.setGraphic(addIconOnBtn);
                    btn.setOnAction((ActionEvent event) -> {
                        try {
                             FXMLLoader loader=new FXMLLoader();
                             loader.setLocation(getClass().getResource("playlistLoader.fxml"));
                             Parent root1=loader.load();
                            PlaylistLoader controller =loader.getController();
                            controller.initData(songTable.getSelectionModel().getSelectedItem(),userId);
                            Scene newScene=new Scene(root1);

                            System.out.println("test 2 userid "+userId);

                            Stage window=new Stage();
                            window.setScene(newScene);
                            window.initStyle(StageStyle.UNDECORATED);
                           window.initModality(Modality.APPLICATION_MODAL);
                            window.initOwner(btn.getScene().getWindow());
                            window.showAndWait();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("hello there listener!");
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        }
    };

    addSongToPlaylistCol.setCellFactory(cellFactory);
    if(!isaddSongToPlaylistColSet) {
        songTable.getColumns().add(addSongToPlaylistCol);
        isaddSongToPlaylistColSet=true;
    }


}

private void addButtonToTable() {

        Callback<TableColumn<Song, Void>, TableCell<Song, Void>> cellFactory = new Callback<TableColumn<Song, Void>, TableCell<Song, Void>>() {
            @Override
            public TableCell<Song, Void> call(final TableColumn<Song, Void> param) {
                final TableCell<Song, Void> cell = new TableCell<Song, Void>() {

                   private ImageView addIconOnBtn1=new ImageView(new Image("sample/icons/go-icon1.png"));
                   private ImageView addIconOnBtn2=new ImageView(new Image("sample/icons/artist-icon.png"));

                  @FXML
                  private JFXButton btn = new JFXButton("");

                    {
                            btn.setGraphic(addIconOnBtn1); //to add goto icon on a button

                        btn.setOnAction((ActionEvent event) -> {
                            Song data = getTableView().getItems().get(getIndex());
                            try {
                                if(goToArtist.getText().equals("Go to Artist")) {
                                    getArtistSongs();
                                    addToFavotiteCol.setVisible(false);
                                }
                                else if(goToArtist.getText().equals("Go to Album")){
                                    getSongsOfAlbum();
                                    goToArtist.setVisible(false);
                                    addSongToPlaylistCol.setVisible(true);
                                    addToFavotiteCol.setVisible(true);
                                }
                                else if(goToArtist.getText().equals("Go to Playlist")){
                                    getSongsInPlaylist();
                                    addSongToPlaylistCol.setVisible(false);
                                    addToDeleteSongCol.setVisible(true);
                                    addToFavotiteCol.setVisible(true);
                                }
                                else if(goToArtist.getText().equals("Show Profile")){
                                    getAllSongsOfArtist();
                                    addToFavotiteCol.setVisible(true);
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

    goToArtist.setCellFactory(cellFactory);

    }

    private void addDeleteSongInPlaylistButtonToTable() {

        Callback<TableColumn<Song, Void>, TableCell<Song, Void>> cellFactory = new Callback<TableColumn<Song, Void>, TableCell<Song, Void>>() {
            @Override
            public TableCell<Song, Void> call(final TableColumn<Song, Void> param) {
                final TableCell<Song, Void> cell = new TableCell<Song, Void>() {

                    private ImageView addIconOnBtn2=new ImageView(new Image("sample/icons/Delete-icon.png"));

                    @FXML
                    private JFXButton btn = new JFXButton("");

                    {
                        btn.setGraphic(addIconOnBtn2); //to add delete icon on a button

                        btn.setOnAction((ActionEvent event) -> {
                            Song data = getTableView().getItems().get(getIndex());
                            int trackId=999;
                            int playlistId=999;
                            try {
                                setConnection();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            String trackName=songTable.getSelectionModel().getSelectedItem().getSongName();
                            String query1="SELECT track_id FROM track WHERE track_name=?";
                            try {
                                PreparedStatement statement1=connection.prepareStatement(query1);
                                statement1.setString(1,trackName);
                                ResultSet rs=statement1.executeQuery();
                                rs.next();
                                trackId=rs.getInt(1);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            String query2="SELECT playlist_id from p_contains WHERE track_id=?";
                            try {
                                PreparedStatement statement2=connection.prepareStatement(query2);
                                statement2.setInt(1,trackId);
                                ResultSet rs=statement2.executeQuery();
                                rs.next();
                                playlistId=rs.getInt(1);

                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            String query3="DELETE FROM p_contains WHERE track_id=?";
                            try {
                                PreparedStatement statement3=connection.prepareStatement(query3);
                                statement3.setInt(1,trackId);
                                int rowsAffected=statement3.executeUpdate();
                                System.out.println("The rows deleted are "+ rowsAffected);
                                songColumn.setCellValueFactory(new PropertyValueFactory<>("playlistName"));
                                ObservableList<Song> albumList = PlaylistController.getallPlaylists(userId);
                                songTable.setItems(albumList);
                                songColumn.setText("Your Playlists");
                                goToArtist.setText("Go to Playlist");
                                goToArtist.setVisible(true);
                                addSongToPlaylistCol.setVisible(false);
                                addToDeleteSongCol.setVisible(false);
                                addNoOfTracksCol.setVisible(true);
                                addToFavotiteCol.setVisible(false);

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.initStyle(StageStyle.UTILITY);
                                alert.setTitle("");
                                alert.setHeaderText(null);
                                alert.setContentText( trackName+ " deleted from playlist.");
                                alert.showAndWait();

                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        addToDeleteSongCol.setCellFactory(cellFactory);
        if(!isaddToDeleteSongColSet) {
            songTable.getColumns().add(addToDeleteSongCol);
            isaddToDeleteSongColSet=true;
        }

    }

    private void addToFavoriteButtonToTable() {

        Callback<TableColumn<Song, Void>, TableCell<Song, Void>> cellFactory = new Callback<TableColumn<Song, Void>, TableCell<Song, Void>>() {
            @Override
            public TableCell<Song, Void> call(final TableColumn<Song, Void> param) {
                final TableCell<Song, Void> cell = new TableCell<Song, Void>() {

                    private ImageView addIconOnBtn3=new ImageView(new Image("sample/icons/fav-add-icon.png"));

                    @FXML
                    private JFXButton btn = new JFXButton("");

                    {

                        btn.setGraphic(addIconOnBtn3); //to add goto icon on a button

                        btn.setOnAction((ActionEvent event) -> {

                            String song=songTable.getSelectionModel().getSelectedItem().getSongName();
                            //int userId=1;
                          /*  String result="";
                            ResultSet rs=null;
                           String queryToCheckIfPresent="SELECT track_name FROM favourites f WHERE f.track_name=?";
                            try {

                                PreparedStatement statement=connection.prepareStatement(queryToCheckIfPresent);
                                statement.setString(1,song);
                                 rs=statement.executeQuery();
                                 rs.next();
                                 result=rs.getString(1);

                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            } */

                            try {
                           // if(result.equals("")){
                                setConnection();


                                String query=String.format("SELECT track_id FROM track WHERE track_name=\"%s\"",song);
                                PreparedStatement pst=connection.prepareStatement(query);
                                ResultSet rs=pst.executeQuery();
                                rs.next();
                                int trackId=rs.getInt(1);
                                String toCheckIfExists=String.format("SELECT track_id FROM favourites WHERE track_id=\"%d\" AND user_id=\"%d\"",trackId,userId);
                                PreparedStatement pst1=connection.prepareStatement(toCheckIfExists);
                                ResultSet rs1=pst1.executeQuery();
                              //  rs1.next();
                                if(rs1.next()){
                                    System.out.println("Already in playlist");
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.initStyle(StageStyle.UTILITY);
                                    alert.setTitle("");
                                    alert.setHeaderText(null);
                                    alert.setContentText(song+" already addded to favourites.");
                                    alert.showAndWait();
                                }else {


                                    CallableStatement c = connection.prepareCall("CALL addToFavs(?,?)");
                                    c.setInt(1, trackId);
                                    c.setInt(2, userId);
                                    c.executeUpdate();

                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.initStyle(StageStyle.UTILITY);
                                    alert.setTitle("");
                                    alert.setHeaderText(null);
                                    alert.setContentText(song + " added to favorites.");
                                    alert.showAndWait();
                                }
                            }
                           // else{
                            //    JOptionPane.showMessageDialog(null,"Song already in favorites");
                            //}
                         catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }});
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        addToFavotiteCol.setCellFactory(cellFactory);
        if(!isaddToFavotiteCol) {
            songTable.getColumns().add(addToFavotiteCol);
            isaddToFavotiteCol=true;
        }

    }


    public void getAllSongsOfArtist() throws SQLException, ClassNotFoundException {
        Song playlistSelected=songTable.getSelectionModel().getSelectedItem();
        String artistName=playlistSelected.getArtistName();
        String query1="SELECT artist_id FROM artist WHERE artist_name=?";
        PreparedStatement statement1=connection.prepareStatement(query1);
        statement1.setString(1,artistName);
        ResultSet rs1=statement1.executeQuery();
        rs1.next();
        int artistId=Integer.parseInt(rs1.getString(1));

        String query=String.format("SELECT track_name FROM track t INNER JOIN track_by tb ON t.track_id=tb.track_id AND tb.artist_id=\"%s\"",artistId);

        ObservableList<Song> allSongsOfArtist=getallSongs(query);
        songTable.refresh();//nyun
        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
        songTable.setItems(allSongsOfArtist);
        goToArtist.setVisible(false);
        addButtonToTable();
        addSongToPlaylistBtn();
        addSongToPlaylistCol.setVisible(true);
        songColumn.setText("Track");

    }

    public void getSongsInPlaylist() throws SQLException, ClassNotFoundException {
        Song playlistSelected=songTable.getSelectionModel().getSelectedItem();
        String playlist=playlistSelected.getPlaylistName();
        String query1="SELECT playlist_id FROM playlists WHERE playlist_name=?";

        PreparedStatement statement1 = connection.prepareStatement(query1);
        statement1.setString(1, playlist);
        ResultSet rs1=statement1.executeQuery();
        rs1.next();
        System.out.println(rs1.getString(1)+" playlist_id retreived");
        int playlistId=Integer.parseInt(rs1.getString(1));

        String query=String.format("SELECT track_name FROM track t INNER JOIN p_contains p ON t.track_id=p.track_id AND p.playlist_id=\"%s\"",playlistId);
        ObservableList<Song> playlistSongs=getallSongs(query);
        songTable.refresh();//yuju
        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
        songTable.setItems(playlistSongs);
        goToArtist.setVisible(false);
        addButtonToTable();
        addSongToPlaylistBtn();
        addSongToPlaylistCol.setVisible(true);
        addNoOfTracksCol.setVisible(false);
        songColumn.setText("Track");

    }


    @FXML
    private void Close(){
        closeBtn.setOnAction(a -> Main.closPrgm());
    }
    @FXML
    public  void getArtistSongs() throws SQLException, ClassNotFoundException {


        Song curr_artist = songTable.getSelectionModel().getSelectedItem();
        String artist = curr_artist.getArtistName();
        System.out.println(curr_artist.getSongName());
        String songSelected=curr_artist.getSongName();
        String query=String.format("SELECT track_name FROM track t WHERE t.track_id in (SELECT track_id FROM track_by tb WHERE  tb.artist_id=(SELECT artist_id FROM track_by tb WHERE tb.track_id=(SELECT track_id FROM track WHERE track_name=\"%s\")))",songSelected);
        System.out.println(query);
        ObservableList<Song> artistSongs=getallSongs(query);
        songTable.refresh();
        songTable.setItems(artistSongs);
        songColumn.setText("Track");

    }

    @FXML
    private void search() throws SQLException, ClassNotFoundException {

        try {
            String song = SRC.getText();
            String query = String.format("%s%s%s", "SELECT track_name from track where track_name like '%", song, "%';");
            System.out.println(query);
            ObservableList<Song> artistSongs = getallSongs(query);
            songTable.refresh();
            songTable.setItems(artistSongs);
        }catch(Exception e){

        }

    }

    @FXML
    private void addToFav() throws SQLException {
        Song curr_song = songTable.getSelectionModel().getSelectedItem();
        String song_name =curr_song.getSongName();

        CallableStatement cstmt=connection. prepareCall("CALL addToFavs(?,?)");
        cstmt.setString(1, song_name);
        cstmt.setInt(2,userId);
        cstmt.executeUpdate();
//bye
    }

    @FXML
    private void setFavourites() throws SQLException, ClassNotFoundException {
        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
        //String query = String.format("%s=%d","SELECT track_name from favourites where user_id",userId);
        String query = String.format("SELECT track_name FROM track WHERE track_id in (SELECT track_id  from favourites where user_id=\"%d\")",userId);
        ObservableList<Song> songList = getallSongs(query);
        songTable.refresh();
        songTable.setItems(songList);
        addToFavotiteCol.setVisible(false);
        topLabel.setText("Your Favorites");
        songColumn.setText("Track");
        addNoOfTracksCol.setVisible(false);
        addToDeleteSongCol.setVisible(false);
        goToArtist.setVisible(false);
        addSongToPlaylistCol.setVisible(true);
        clrRecent.setVisible(false);

    }

    @FXML
    private void recentlyPlayed() throws SQLException, ClassNotFoundException {
        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
        String query = String.format("SELECT track_name FROM track  WHERE  track_id in (SELECT track_id  from recently_Played where user_id=\"%d\" order by date_played) ",userId);//"%s=%d",
        String query1="";

        ObservableList<Song> songList = getallSongs(query);
        songTable.refresh();
        songTable.setItems(songList);
        goToArtist.setVisible(false);
        addToDeleteSongCol.setVisible(false);
        addToFavotiteCol.setVisible(true);
        addSongToPlaylistCol.setVisible(true);
        topLabel.setText("Recently Played");
        songColumn.setText("Track");
        addNoOfTracksCol.setVisible(false);
        clrRecent.setVisible(true);
    }


    public void getSongsOfAlbum() throws SQLException, ClassNotFoundException{
        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
        Song curr_album = songTable.getSelectionModel().getSelectedItem();
        String album = curr_album.getAlbumName();
        System.out.println(curr_album.getAlbumName());
        String query=String.format("SELECT track_name FROM track t WHERE t.album_id in (SELECT album_id FROM album a where album_name=\"%s\");",album);
        System.out.println(query);
        ObservableList<Song> artistSongs=getallSongs(query);
        goToArtist.setText("Go to Artist");
        songTable.refresh();
        songTable.setItems(artistSongs);
        songColumn.setText("Track");

    }

    @FXML
    private void artistInitialise() throws ClassNotFoundException, SQLException {
        songColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        ObservableList<Song> artistList = ArtistController.getallArtist();
        songTable.setItems(artistList);
        songColumn.setText("Artists");
        goToArtist.setText("Show Profile");
        goToArtist.setVisible(true);
        addSongToPlaylistCol.setVisible(false);
        addToDeleteSongCol.setVisible(false);
        addToFavotiteCol.setVisible(false);
        topLabel.setText("Artists");
        addNoOfTracksCol.setVisible(false);
        clrRecent.setVisible(false);

    }

    @FXML
    private void albumInitialise() throws ClassNotFoundException, SQLException {
        songColumn.setCellValueFactory(new PropertyValueFactory<>("albumName"));

        ObservableList<Song> albumList = AlbumController.getallAlbums();
        songTable.setItems(albumList);
        songColumn.setText("Albums");
        goToArtist.setText("Go to Album");
        goToArtist.setVisible(true);
        addSongToPlaylistCol.setVisible(false);
        addToDeleteSongCol.setVisible(false);
        addToFavotiteCol.setVisible(false);
        topLabel.setText("Albums");
        addNoOfTracksCol.setVisible(false);
        clrRecent.setVisible(false);

    }

    @FXML
    private void playlistInitialise() throws ClassNotFoundException, SQLException{
        songColumn.setCellValueFactory(new PropertyValueFactory<>("playlistName"));
        addNoOfTracksCol.setCellValueFactory(new PropertyValueFactory<>("noOfStreams"));
        addNoOfTracksToPlaylist();
        addNoOfTracksCol.setVisible(true);
        System.out.println("This is playlistInitializer and user id is "+userId);
        ObservableList<Song> albumList = PlaylistController.getallPlaylists(userId);

       // songTable.setItems(noOfTracksList);
        songTable.setItems(albumList);
        songColumn.setText("Your Playlists");
        goToArtist.setText("Go to Playlist");
        goToArtist.setVisible(true);
        addSongToPlaylistCol.setVisible(false);
        addToDeleteSongCol.setVisible(false);
        addToFavotiteCol.setVisible(false);
        topLabel.setText("Playlist");
        clrRecent.setVisible(false);
    }



    public void play() throws SQLException, ClassNotFoundException {

        if(userId==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("");
            alert.setHeaderText(null);
            alert.setContentText("Please login to enjoy the music.");
            alert.showAndWait();
        }
        else {


            Song curr_song = songTable.getSelectionModel().getSelectedItem();
            String song = curr_song.getSongName();
            nowPlaying.setText(song);
            String query = String.format("%s=\"%s\";", "SELECT path_ from track where track_name", song);
            System.out.println(query);

            CallableStatement cstmt = connection.prepareCall("CALL noOfStreams(?)");
            cstmt.setString(1, song);//noOfStreams
            cstmt.executeUpdate();
            System.out.println("yes");

            setConnection();
            String query1 = String.format("SELECT track_id FROM track WHERE track_name=\"%s\"", song);
            PreparedStatement pst1 = connection.prepareStatement(query1);
            ResultSet rs = pst1.executeQuery();
            rs.next();
            int trackId = rs.getInt(1);

            String CheckPesenceOfSongInRecentlyPlayed = String.format("SELECT track_id FROM recently_played WHERE track_id=\"%d\" AND user_id=\"%d\"", trackId, userId);
            PreparedStatement pst = connection.prepareStatement(CheckPesenceOfSongInRecentlyPlayed);
            ResultSet rss = pst.executeQuery();
            // rss.next();
            //!rss.getString(1).equals("")
            if (!rss.next()) {

                CallableStatement c = connection.prepareCall("CALL recently_played(?,?,?)");
                c.setInt(1, trackId);
                c.setInt(2, userId);   // @Phani TODO add user_id instead of 0
                c.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
               // DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                //LocalDateTime now = LocalDateTime.now();
               // c.setTimestamp(3, new Timestamp(System.currentTimeMillis());
               // SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
             //   Date date = new Date();
             //   c.setTimestamp(3, Timestamp.valueOf(formatter.format(date)));
                c.executeUpdate();
            }
            System.out.println("yes");


            setConnection();
            PreparedStatement prepmnt = null;
            try {
                prepmnt = connection.prepareStatement(query);
                ResultSet rs1;
                rs1 = prepmnt.executeQuery(query);

                if (rs1 == null)
                    System.out.println("nothing here");
                if (rs1.next()) {

                    String song_path = rs1.getString("path_");
                    System.out.println(song_path);
                    mp3 = new MP3Player(new File(song_path));
                    playy(mp3, song_path);
                    pauseBtn.setVisible(true);
                    pauseImage.setVisible(true);
                    playBtn.setVisible(false);
                    playImage.setVisible(false);

                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

    }

    public static void playy(MP3Player mp31,String path){
        mp31.play();

    }

    public  void pausee(){
        mp3.pause();
        pauseBtn.setVisible(false);
        pauseImage.setVisible(false);
        playBtn.setVisible(true);
        playImage.setVisible(true);

    }



    private static void setConnection() throws SQLException,ClassNotFoundException {
        String url = "jdbc:mysql://localhost/musicplayer";
        String uname = "root";
        //String pwd = "12Ccbu12!";
       String pwd = "phani@123";
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(url, uname, pwd);
            System.out.println("Connection succesful");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




        }


    public static ObservableList<Song> getallSongs(String query) throws SQLException, ClassNotFoundException {

                    setConnection();
                    PreparedStatement prepmnt= null;
        try {
                    prepmnt = connection.prepareStatement(query);
                    ResultSet rs;
                    rs = prepmnt.executeQuery(query);
                    ObservableList<Song> allSongData=getSongObjects(rs);
                    return allSongData;

        } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

    return null;
    }

    private static ObservableList<Song> getSongObjects(ResultSet rs) {
        try {
            ObservableList<Song> songData = FXCollections.observableArrayList();
            while (rs.next()) {
                Song song = new Song();
                song.setSongName(rs.getString("track_name"));
                System.out.println(rs.getString("track_name"));
                songData.add(song);
            }
            return songData;

    } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    return null;
    }

    @FXML
    public void CreateNewPlaylist(){                  //this method is called when New Playlist Button is clicked on the main screen.
        anchorpaneCreatePlaylist.setVisible(true);


    }
    @FXML
    public void okBtnActionHandler() throws SQLException, ClassNotFoundException {

        String newPlaylistName=newPlaylistTextField.getText();
        if(newPlaylistName.equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Playlist name cannot be empty");
            alert.showAndWait();

        }else {

            long d = System.currentTimeMillis();
            Date datee = new Date(d);

            String query = "INSERT INTO playlists(user_id,playlist_name,date_added) VALUES(?,?,?)";

            setConnection();
            long dd = System.currentTimeMillis();
            java.sql.Date dateee = new java.sql.Date(d);
            System.out.println(dateee);
            String query2 = String.format("INSERT INTO playlists(user_id,playlist_name,date_added)" + " values(\"%d\",\"%s\",\"%s\")", Controller.userId, newPlaylistName, dateee);
            PreparedStatement statement = connection.prepareStatement(query2);

            statement.executeUpdate();
          /*  PreparedStatement statement1 = connection.prepareStatement(query);
            statement1.setInt(1, userId);
            statement1.setString(2, newPlaylistName);
            statement1.setTimestamp(3, Timestamp.valueOf(String.valueOf(datee)));

            statement1.executeUpdate();*/
            anchorpaneCreatePlaylist.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("New playlist " + newPlaylistName + " created.");
            alert.showAndWait();

        }
    }


    @FXML
    public void disappearAnchorPane(){
        anchorpaneCreatePlaylist.setVisible(false);
    }

public static void UnlockFunctionalities(boolean bool){

    isCredentialsCorrect=bool;
}


    public  void hideFunctionalities() throws SQLException, ClassNotFoundException {

        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("loginScreen.fxml"));
            Parent root1=loader.load();
            //Parent root1= FXMLLoader.load(getClass().getResource("playlistLoader.fxml"));
            Scene newScene=new Scene(root1);
            //Stage window=(Stage)((Node)btn).getScene().getWindow();
            loginController controller =loader.getController();
            // controller.initData(songTable.getSelectionModel().getSelectedItem());
            count++;

            Stage window=new Stage();
            window.setScene(newScene);
            window.initStyle(StageStyle.UNDECORATED);
            window.initModality(Modality.APPLICATION_MODAL);
            window.initOwner(Main.window);
            window.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!userEmailid.equals("")) {  //this condition is for when a user forgets password and creates new password.then he'll be prompted to this window,then to handlwe this situation ,we put the if condition here

            setConnection();
            String queryToGetUserName = "SELECT * FROM users WHERE email_id=?";
            PreparedStatement statement = connection.prepareStatement(queryToGetUserName);
            statement.setString(1, userEmailid);
            System.out.println(userEmailid+" checking on email id passes");
            ResultSet rs = statement.executeQuery();
            rs.next();
            userName = rs.getString(2);
            userId=rs.getInt(1);
            HiUserLabel.setText("Hullow " + userName + " !");
        }else{
            setConnection();
            String queryToGetUserName = "SELECT * FROM users WHERE user_name=?";
            PreparedStatement statement = connection.prepareStatement(queryToGetUserName);
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();
            rs.next();
            userId=rs.getInt(1);

            HiUserLabel.setText("Hullow " + userName + " !");
        }

        if(isCredentialsCorrect){
            newPlaylistMainBtn.setVisible(true);
            recently_played.setVisible(true);
            Favourites.setVisible(true);
            playlistMainBtn.setVisible(true);
            trandingBtn.setVisible(true);
            loginIcon.setVisible(false);
            logonIconBtn.setVisible(false);
            loginLabel.setVisible(false);
        }

    }

    public void trending() throws SQLException, ClassNotFoundException {

        songColumn.setCellValueFactory(new PropertyValueFactory<Song,String>("songName"));
//        TODO :@phani , update db and uncomment this line String query = "SELECT track_name,artist_name from track_artists";
        String query = "SELECT track_name from track order by no_0f_streams desc";
        ObservableList<Song> songList = getallSongs(query);
        songTable.refresh();
        songTable.setItems(songList);
        songColumn.setText("Track");
        goToArtist.setText("Go to Artist");
        goToArtist.setVisible(false);
        addButtonToTable();
        addSongToPlaylistBtn();
        addSongToPlaylistCol.setVisible(true); //this column should be visible only when thw tracks are displayed
        addDeleteSongInPlaylistButtonToTable();
        addToDeleteSongCol.setVisible(false);
        addToFavoriteButtonToTable();
        addToFavotiteCol.setVisible(true);
        clrRecent.setVisible(false);
        topLabel.setText("Trending Songs");


    }

    public void clearRecentlyPlayed() throws SQLException, ClassNotFoundException {

        String query=String.format("DELETE FROM recently_played WHERE user_id=\"%d\"",userId);
        setConnection();
        PreparedStatement pst=connection.prepareStatement(query);
        int rows=pst.executeUpdate();
        System.out.println(rows);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("Your data of recently played has been cleared.");
        alert.showAndWait();

        recentlyPlayed();//to refresh the table.(empty table will be displayed)



    }



    }








