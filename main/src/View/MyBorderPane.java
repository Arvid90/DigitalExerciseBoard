package View;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author joakimstoor
 */

/* A border pane with one or two buttons in the top right and/or left corner */
public class MyBorderPane extends BorderPane {
    
    private Button settingsReturnButton, exitButton, removeExButton;
    private boolean canBeExited, hasReturnButton;
    private MainClass mainGUI;
    
    /* Intializes the border pane with an exit button or with a delete exercise 
     * button, and with or without a return button, all specified by the booleans*/
    public MyBorderPane(MainClass mainGUI, boolean canBeExited, boolean hasReturnButton) {
        this.mainGUI = mainGUI;
        this.hasReturnButton = hasReturnButton;
        this.canBeExited = canBeExited; 
        this.setStyle("-fx-java-color: black");
        
        // setMinHeight and setMaxHeight of the extended BorderPane class are 
        // called from the classes that uses object of this class to autosize it
        this.setMinHeight(ProgramConstants.WINDOWHEIGHT);
        this.setMinWidth(ProgramConstants.WINDOWWIDTH);
        this.setMaxHeight(ProgramConstants.WINDOWHEIGHT);
        this.setMaxWidth(ProgramConstants.WINDOWWIDTH);
        
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: black");
        
        // Creates and positions a return button if true
        if (hasReturnButton) {
            initializeReturnButton();
            anchorPane.getChildren().add(settingsReturnButton);
            AnchorPane.setLeftAnchor(settingsReturnButton, 0.0);
        }
        
        /* Creates and positions an exit button if true. If not, creates and 
         * positions a remove exercise button in its stead */
        if (canBeExited) {
            initializeExitButton();
            anchorPane.getChildren().add(exitButton);
            AnchorPane.setRightAnchor(exitButton, 0.0);
            
            exitButton.setOnAction(e -> mainGUI.exitAlert());
            
        } else {
            initializeRemoveExercise();
            
            anchorPane.getChildren().add(removeExButton);
            AnchorPane.setRightAnchor(removeExButton, 0.0);
        }

        this.setTop(anchorPane);
    }
    
    /* Initializes the remove exercise button*/
    private void initializeRemoveExercise() {
        // Uses an URL for the image and some translation integers, and a name string
        removeExButton = initializeGeneralButton(
                "Resources/removeExercise.png", 40, -80, "removeExButton"); 
    }
    
    /* Initializes the return button*/
    private void initializeReturnButton() {
        // Uses an URL for the image and some translation integers, and a name string
        settingsReturnButton = initializeGeneralButton("Resources/arrow.png", 40, 80, 
                "settingsButton"); 
    }
    
    /* Initializes the exit button*/
    private void initializeExitButton() {
        // Uses an URL for the image and some translation integers, and a name string
        exitButton = initializeGeneralButton("Resources/exit.png", 40, -80, 
                "exitButton");
    }
    
    /* Intializes a generic button with an image*/
    public Button initializeGeneralButton(String stringOne, double vertTrans, 
            double horiTrans, String stringTwo) {
        ImageView imageview = new ImageView(new Image(stringOne));
        imageview.fitHeightProperty().bind(this.heightProperty().divide(10));
        imageview.fitWidthProperty().bind(this.widthProperty().divide(6));
        imageview.setPreserveRatio(true); 
        imageview.setSmooth(true);
        imageview.setCache(true);
        Button button = new Button("", imageview);
        button.setMinHeight(imageview.fitHeightProperty().doubleValue());
        button.setStyle
                ("-fx-background-color: black; -fx-border-color: white"); 
       
        // The button is translated by a specified amount from the border pane 
        // boundary
        button.translateXProperty().bind(
                this.widthProperty().divide(horiTrans));
        button.translateYProperty().bind(
                this.heightProperty().divide(vertTrans)); 
       
        return button;
    }
    
    /* Returns the return button*/
    public Button getReturnButton() {
        return settingsReturnButton;
    }
    
    /* Returns the exit button if canBeExited is true or the remove exercise 
     * button if it's false*/
    public Button getExitButton() {
        if (canBeExited) { return exitButton; }
        else { return removeExButton; }

    }

}
