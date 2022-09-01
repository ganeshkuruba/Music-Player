package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Artist {
    private StringProperty artistName;


    public Artist() {
        this.artistName = new SimpleStringProperty();
    }

    public String getArtistName() {
        return artistName.get();
    }

    public StringProperty getArtistName(StringProperty artistName) {
        return artistName;
    }

}