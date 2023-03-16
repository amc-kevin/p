
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black;");
        Gameboard game = new Gameboard();
        pane.getChildren().add(game);
        var scene = new Scene(pane, 950, 1050);
        stage.setTitle("PacmanObligTest");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}