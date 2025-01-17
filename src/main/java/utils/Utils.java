package utils;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {

    // Save File
    public static String saveFile(String fileName, String content, String directory) {
        // File chooser
        FileChooser fc = new FileChooser();

        // Set initial directory
        File initialDirectory = new File(directory);
        if (initialDirectory.exists()) {
            fc.setInitialDirectory(initialDirectory);
        }

        // Set initial file name
        fc.setInitialFileName(fileName);

        // Save file dialog
        File file = fc.showSaveDialog(new Stage());

        // File writer
        if (file != null) {
            // Write file
            try (FileWriter wr = new FileWriter(file)) {
                wr.write(content);
                return file.getName();
            } catch (IOException e) {
                return "File not saved";
            }
        } else {
            return "File not saved";
        }
    }

    // Open File
    public static File openFile(String title, String... extensions) {
        // File chooser
        FileChooser fc = new FileChooser();

        // File type filter
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(title, extensions));

        // Open File
        return fc.showOpenDialog(new Stage());
    }

    // Color to Hex
    public static String colorToHex(Color color) {
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        int alpha = (int) (color.getOpacity() * 255);
        return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
    }
}
