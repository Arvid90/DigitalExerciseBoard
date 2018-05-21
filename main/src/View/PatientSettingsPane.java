
package View;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

import javafx.scene.text.Text;
import javafx.scene.text.Font;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.scene.shape.Rectangle;

import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author joakimstoor

* */

/* A pane used in the PatientGUIPane to show the patient name, number of active 
 * days, and a settingsbutton used to enter the settings view*/
public class PatientSettingsPane extends Pane {
    private final StackPane mainPane;
    private Button settingsButton;
    private BorderPane borderPane;
    private VBox vb;
    private AnchorPane ap;
    private PatientGUIPane parentPane;
    private Label idText, dateText;
        
    /* Initializes a new patient settings pane with a size of specified by the 
     * width (x) and height(y) fractions of the parentPane */
    public PatientSettingsPane(PatientGUIPane parentPane, double x, double y) {
        this.parentPane = parentPane; 
        borderPane = new BorderPane();
        mainPane = new MyStackPane(parentPane, x, y); // Using a MyStackPane
        initializeInformationView(); // The main info in this pane
        
        borderPane.setCenter(vb); 
        
        mainPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(borderPane);
        
        this.getChildren().add(mainPane);
    }
        
    /* Inits the button, the idtext and date activedaystext*/
    private void initializeInformationView() {
        initializeButtonPre();
        borderPane.setTop(settingsButton);
        
        
        // Calling a generic text function found below 
        idText = createText("",20);
        dateText = createText("Aktiva dagar: 1", 14); 
        
        // Storing texts in a vbox
        vb = new VBox();
        vb.spacingProperty().bind(this.heightProperty().divide(40));
        vb.getChildren().addAll(idText, dateText);
        vb.setAlignment(Pos.CENTER);  
    }
    
    // creates and styles labels with a specified text
    private Label createText(String s, int size) {
        Label tx  = new Label(s);
        tx.setFont(Font.font("Verdana", FontWeight.BOLD, size));
        tx.setTextFill(Color.ORANGE);
        tx.setPadding(new Insets(1));
        
        return tx;
    } 
    
    /* Called to change the size of the user name label, i.e. it depends on the 
     * length of the text in the label */
    private void setidTextSize(int size) {
        idText.setFont(Font.font("Verdana", FontWeight.BOLD, size));
    }
    
    private void initializeButtonPre() {
        settingsButton = initializeButton("Resources/settings.png");
    }
    
    /* Inits the settings button in the left corner*/
    private Button initializeButton(String s) {
        ImageView imageview = new ImageView(
                new Image(s));
        imageview.setPreserveRatio(true); 
        
        // translated a bit to come away from the border of the stage
        imageview.fitHeightProperty().bind(parentPane.heightProperty().divide(10));
        imageview.fitWidthProperty().bind(parentPane.widthProperty().divide(20));
        imageview.setSmooth(true);
        imageview.setCache(true);
        Button button = new Button("", imageview);
        button.setPrefHeight(imageview.fitHeightProperty().doubleValue());
        button.setPrefWidth(imageview.fitWidthProperty().doubleValue());
        // Show only the  image with no border
        button.setStyle("-fx-background-color: black; -fx-border-color: black;");
        
        translateButton(button, 48, 24);
        
        return button;
    }
    
    private void translateButton(Button button, double x, double y) {
        button.translateXProperty().bind(
                borderPane.heightProperty().divide(x));
        button.translateYProperty().bind(
                borderPane.heightProperty().divide(y));
    }
    
    /* Sets the size of the id (user name) text depeding on its lenght*/
    public void setIDText(String string) {

        if (string.length() <= 4) { setidTextSize(28);}
        else if (string.length() <= 8) {setidTextSize(24); } 
        else if (string.length() <= 12) { setidTextSize(18); }
        else {setidTextSize(15);}
            
        idText.setText(string);
    }
    
    /* sets the value of active days in the view*/
    public void setDateText(int i) {
        dateText.setText("Aktiva dagar: " + i);
    }

    /* Returns the settings button*/
    public Button getSettingsButton() {
        return settingsButton;
    }
    
    /* Returns the id (user name) text */
    public String getIDText() {
        return idText.getText();
    }
    
        
}
