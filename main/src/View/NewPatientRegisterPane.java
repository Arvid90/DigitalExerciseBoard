package View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author joakimstoor
 */

/* Pane used to regster a new patient, shows a register popup stage if 
 * a new register button is pressed*/
public class NewPatientRegisterPane extends Pane {
    
    private GridPane gridPane;
    private VBox vbox;
    private MyBorderPane borderPane;
    private Label instructionLabel;
    private Label userLabel;
    private TextField textField;
    private Button registerButton;
    
    /* Initializes a new instantiation of this class with a generic add 
     * new user button */
    public NewPatientRegisterPane(MainClass mainClass, Stage primaryStage) {
        borderPane = new MyBorderPane(mainClass, true, false);        
        initiailizeButton(primaryStage, mainClass);
        borderPane.setCenter(registerButton); 
        this.setStyle("-fx-background-color: black;");
        this.getChildren().add(borderPane);           
    }
    
    /* initializes the add new user button*/
    private void initiailizeButton(Stage primaryStage, MainClass mainClass) {
        registerButton = new Button("Registrera ny användare");
        registerButton.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        // Retains the same fraction of the parent mybordepane all the time
        registerButton.setMinSize(ProgramConstants.WINDOWWIDTH/2,
                ProgramConstants.WINDOWHEIGHT/6);
        registerButton.setMaxSize(ProgramConstants.WINDOWWIDTH/2,
                ProgramConstants.WINDOWHEIGHT/6);
        registerButton.setStyle
                ("-fx-background-color: black; -fx-border-color: white; "
                        + " -fx-border-radius: 3px;  -fx-text-align: center;"
                        + "-fx-font-family: Times; -fx-font-size: " +
                        Integer.toString(ProgramConstants.TEXT_SIZE));
        registerButton.setPadding(new Insets(10));
        registerButton.setAlignment(Pos.CENTER);
        // New stage to enter a username and register, register button is setOnAction
        registerButton.setOnAction(e -> {
            newStage(primaryStage, borderPane, mainClass);
        });
    }
    
    /* Autosizes the width of this pane when the stage size is changed*/
    public void setThisWidth(double value) {
        borderPane.setMinWidth(value);
        borderPane.setMaxWidth(value);
    }
    
    /* Autosizes the height of this pane when the stage size is changed*/
    public void setThisHeight(double value) {
        borderPane.setMinHeight(value);
        borderPane.setMaxHeight(value);
    }

    /* A self-mad dialog that shows up on top of the primary stage*/
    public void newStage(Stage primaryStage, BorderPane headPane, MainClass mainClass) {

        final Stage dialog = new Stage();
        // Blocks events to any other window than this
        dialog.initModality(Modality.APPLICATION_MODAL);  
        // Has to specified to bind its position to the primary stage
        dialog.initOwner(primaryStage); 
        dialog.setTitle("Notering");
        
        // Position the stage
        dialog.setX(primaryStage.getX() + (borderPane.getWidth()-350)/2);
        dialog.setY(primaryStage.getY() + 30);
        
        // Textfield to enter a new username
        TextField textField = new TextField(); 
        textField.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        textField.setMaxWidth(200);
        textField.setMinWidth(200);
        
        // Textfield with at maximum 14 char defined in program constants
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than TEXT_LIMIT
                    if (textField.getText().length() >= ProgramConstants.TEXT_LIMIT) {

                        // if it's 15th character then just setText to previous
                        // one
                        textField.setText(textField.getText().substring(0, 
                                ProgramConstants.TEXT_LIMIT));
                    }
                }
            }
        });
        
        
        Label lm = new Label("Ny registrering!");
        Label l = new Label("Användarnamn:");
        HBox hbox_1 = new HBox(10);
        hbox_1.getChildren().addAll(l, textField); // Stores 2nd label and textField
        hbox_1.setAlignment(Pos.CENTER);
        
        // Fonts have to be specified
        lm.setFont(Font.font("Times New Roman", 18));
        l.setFont(Font.font("Times New Roman", 15));
  
        Button b1 = createButton("OK");
        Button b2 = createButton("Avbryt");
        
        HBox hbox_2 = new HBox(20);
        
        // Buttons in their own hbox, positioned and translated to be directly
        // below the textField
        hbox_2.getChildren().addAll(b2, b1);
        hbox_2.setAlignment(Pos.CENTER);
        hbox_2.translateXProperty().setValue(54);
        
        // For styling purposes only, functions as a line to separate into text
        // from textfield and button
        Rectangle rect = new Rectangle(350, 1);
        rect.setFill(Color.GREY);
        
        // Different spacing than for the line and intro text, need their own vbox
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(hbox_1, hbox_2); // label2, textfield and button
        vbox.setAlignment(Pos.CENTER);
        
        VBox vb =new VBox(15);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(lm, rect, vbox); // Here, everything is stored
        
        
        // Handle button clicks
        b1.setOnAction(e -> { 
            // First trim it from whitespaces
            if (textField.getText().trim().length()==0) {
                myAlertDialog(primaryStage);
            }  else { 
                mainClass.createNewPatient(textField.getText().trim());
                dialog.close();
            }
        });
        b2.setOnAction(e -> { dialog.close(); });
        
        dialog.setResizable(false);
        Scene dialogScene = new Scene(vb, 350, 130);
        if (ProgramConstants.isPI) { dialogScene.setCursor(Cursor.NONE);}
        dialog.setScene(dialogScene);
        dialog.show();       
    }
    
    // Creates a generic button for the newStage and sets its size
    private Button createButton(String s) {
        Button button = new Button(s);
        button.setMaxWidth(90);
        button.setMinWidth(90);
        return button;
    }
    
    /* Alerts the user if they try to enter with an empty user name, with or
     * without whitespaces*/
    private void myAlertDialog(Stage stage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(stage);
        alert.setX(stage.getX() + (borderPane.getWidth()-350)/2);
        alert.setY(stage.getY() + 30);
        alert.setWidth(350);
        alert.setTitle("Felmedelande");
        alert.setHeaderText("Felaktigt användarnamn");
        alert.setContentText("Inget giltigt användarnamn har angivits.\n"
                + "Kolla dina whitespaces!");  
        alert.showAndWait();
    }
}