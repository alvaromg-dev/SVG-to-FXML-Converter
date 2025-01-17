package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class SVGtoFXML {

    // #### VARIABLES #################
    // GUI
    @FXML
    public Label statusLabel;
    public Button openButton;
    public TextArea howToUseTextArea;
    public ColorPicker colorPicker;

    // Values
    private String svgFilePath;
    private String svgFileName;
    private String folderPath;

    // #### CONSTRUCTOR ###############
    public SVGtoFXML() {
        this.howToUseTextArea = new TextArea();
    }

    // #### METHODS ###################
    // Select file
    public void openFile() {

        // Open File
        File file = Utils.openFile("svg", "*.svg");

        // Get file data
        if (file != null && file.exists()) {
            // File data
            this.svgFilePath = file.getAbsolutePath();
            this.svgFileName = file.getName().replace(".svg", "");
            this.folderPath = file.getAbsolutePath().replace(svgFileName + ".svg", "");
            // Update color picker
            this.colorPicker.setValue(Color.web(this.extractSVGColor(this.svgFilePath)));
            // Update status
            this.statusLabel.setText("File selected.");
        } else {
            // Update status
            this.statusLabel.setText("File not selected.");
        }
    }

    // Save File
    private void saveFile(String content) {
        String fileName = Utils.saveFile(this.svgFileName + ".fxml", content, this.folderPath);

        // Update status and How to use
        if (fileName.equals("File not saved")) {
            this.statusLabel.setText("File not saved.");
        } else {
            this.statusLabel.setText("File saved.");
            this.updateHowToUse(fileName);
        }
    }

    // Convert SVG to FXML
    public void convertSVGToFXML() {

        // If the file is not selected, stop
        if (!(this.svgFilePath == null)) {
            // SVG path to FXML
            String svgPath = this.extractSVGpath(this.svgFilePath);
            String fxml = this.svgPathToFXML(svgPath);
            this.saveFile(fxml);
        } else {
            this.statusLabel.setText("File not selected.");
        }
    }

    // How to use
    private void updateHowToUse(String fileName) {
        this.howToUseTextArea.setText(String.format("""
                <Group>
                    <fx:include source="../icons/%s" />
                </Group>
                """, fileName));
    }

    // Add svg path to fxml file
    private String svgPathToFXML(String svgPath) {
        // Return null
        if (svgPath == null) {
            return null;
        } else {
            // Return FXML File content
            return String.format("""
                    <?xml version="1.0" encoding="UTF-8"?>
                    <?import javafx.scene.shape.SVGPath?>
                    
                    <SVGPath content="%s"
                             fill="%s"
                             scaleX="1"
                             scaleY="1"/>
                    """, svgPath, Utils.colorToHex(this.colorPicker.getValue()));
        }
    }

    // Extract SVG Path
    private String extractSVGpath(String svgFilePath) {
        StringBuilder concatenatedPaths = new StringBuilder();
        try {
            // DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(svgFilePath);

            // All path nodes
            NodeList pathNodes = document.getElementsByTagName("path");

            // Concat all paths
            for (int i = 0; i < pathNodes.getLength(); i++) {
                Node pathNode = pathNodes.item(i);
                if (pathNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element pathElement = (Element) pathNode;
                    String dAttribute = pathElement.getAttribute("d");
                    if (!dAttribute.isEmpty()) {
                        if (!concatenatedPaths.isEmpty()) {
                            concatenatedPaths.append(" ");
                        }
                        concatenatedPaths.append(dAttribute);
                    }
                }
            }
        } catch (
                Exception e) {
            this.statusLabel.setText("Error reading SVG file");
        }

        return concatenatedPaths.toString();
    }

    // Extract SVG Color
    private String extractSVGColor(String svgPath) {
        try {
            // Document builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(svgPath));

            // Get all nodes
            Element root = document.getDocumentElement();

            // Get color
            String fillColor = root.hasAttribute("fill") ? root.getAttribute("fill") : "#ffffff";
            String strokeColor = root.hasAttribute("stroke") ? root.getAttribute("stroke") : fillColor;

            // return the color
            return !fillColor.equals("#ffffff") ? fillColor : strokeColor;
        } catch (Exception e) {
            // Default color
            this.statusLabel.setText("Error reading SVG color");
            return "#ffffff";
        }
    }

    // Copy how to use to clipboard
    public void copyHowToUse() {
        ClipboardContent contenido = new ClipboardContent();
        contenido.putString(this.howToUseTextArea.getText());
        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(contenido);
    }

    // Fix color picker label
    public void colorPickerLabel(ActionEvent actionEvent) {
        Color color = this.colorPicker.getValue();
        String colorHexValue = Utils.colorToHex(color);
        Label label = (Label) this.colorPicker.lookup(".color-picker-label");
        label.setText(colorHexValue);
    }
}
