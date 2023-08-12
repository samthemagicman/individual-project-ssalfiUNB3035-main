package ca.unb.cs3035.project;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainApplication extends Application {
    public static TasksModel tasksModel;
    public static Scene scene;
    @Override
    public void start(Stage stage) throws IOException {
        tasksModel = new TasksModel();

//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
//        scene = new Scene(fxmlLoader.load(), 800, 400);
//        stage.setTitle("Task App!");
//        stage.setScene(scene);
//        stage.show();

        try {
            //Load splash screen view FXML
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("splash-screen.fxml"));
            VBox pane = fxmlLoader.load();
            scene = new Scene(pane, 800, 400);
            stage.setTitle("Welcome!");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);

            //Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            //Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            //After fade in, start fade out
            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            //After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
                try {
                    FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
                    scene = new Scene(loader.load(), 800, 400);
                    stage.setResizable(true);
                    stage.setTitle("Task App!");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            });
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}