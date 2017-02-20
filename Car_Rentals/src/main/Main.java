package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.tools.CreateDataBase;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views.fxml"));
        primaryStage.setTitle("Car Rentals");
        primaryStage.setMinHeight(640);
        primaryStage.setMinWidth(1080);
        primaryStage.titleProperty();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
}


    public static void main(String[] args) {
        launch(args);
    }
}
