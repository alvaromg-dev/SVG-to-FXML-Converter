package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AppLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppLauncher.class.getResource("/fxml/SVGtoFXML.fxml"));
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/window-icon.png")));
        Scene scene = new Scene(fxmlLoader.load(), 550, 350);
        stage.setTitle("SVG to FXML!");
        stage.getIcons().add(icon);
        stage.setMinWidth(550);
        stage.setMinHeight(350);
        stage.setScene(scene);
        stage.show();
    }
}