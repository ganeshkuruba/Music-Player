package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Song {

    private StringProperty songName;
    private StringProperty artistName;
    private StringProperty albumName;
    private StringProperty playlistName;
    private StringProperty noOfStreams;
    private JFXButton artist;


    public String getNoOfStreams() {
        return noOfStreams.get();
    }

    public void setNoOfStreams(String noOfStreams) {
        this.noOfStreams.set(noOfStreams);
    }

    public Song() {
        this.songName = new SimpleStringProperty();
        this.artistName = new SimpleStringProperty();
        this.albumName = new SimpleStringProperty();
        this.playlistName = new SimpleStringProperty();
        this.noOfStreams=new SimpleStringProperty();
        System.out.println(" song name " + songName);
    }


    public String getSongName() {
        return songName.get();
    }

    public StringProperty getSongName(StringProperty songProp) {
        return songName;
    }

    public void setSongName(String song_name) {
        this.songName.set(song_name);
    }

    public void setArtistName(String name) {
        this.artistName.set(name);
    }

    public String getArtist(int artist_id) {
        return artistName.get();
    }


    public String getArtistName() {
        return artistName.get();

    }

    public void setAlbumName(String name) {
        this.albumName.set(name);
    }


    public String getAlbumName() {
        return albumName.get();

    }

    public void setPlaylistName(String playlistName) {
        this.playlistName.set(playlistName);
    }

    public String getPlaylistName() {
        return playlistName.get();
    }

}