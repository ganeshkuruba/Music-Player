package sample;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application{

    static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        primaryStage.setTitle("Music player");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setMaximized(false);

        final double[] xOffset = new double[1];
        final double[] yOffset = new double[1];
        root.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              xOffset[0] = event.getSceneX();
              yOffset[0] = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset[0]);
                primaryStage.setY(event.getScreenY() - yOffset[0]);
            }
        });

        primaryStage.show();
    }

        public static void closPrgm(){
        window.close();
        }



    public static void main(String[] args) {
        launch(args);
    }
}
