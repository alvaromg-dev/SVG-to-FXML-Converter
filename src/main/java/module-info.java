module launcher {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens launcher to javafx.fxml;
    opens controller to javafx.fxml;

    exports launcher;
    exports controller;
}