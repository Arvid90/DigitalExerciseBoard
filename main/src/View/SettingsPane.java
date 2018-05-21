package View;

import javafx.geometry.Pos;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;

import javafx.scene.control.Button;


/**
 * @author joakimstoor
 */

/* Defines a settings pane with two buttons specified by the implementer and a
 * return and exit button */
public class SettingsPane extends Pane {
    
    private final MyBorderPane borderPane;
    private VBox settingsButtonsPane;
    private Button bMiddle[];
    
    /* Initializes an instantiation of this class with button text specified 
     * by stringOne and stringTwo.*/
    public SettingsPane(MainClass mainClass, String stringOne, String stringTwo) {
        borderPane = new MyBorderPane(mainClass, true, true); 
        StackPane sp = new StackPane(); // Everything stored in a StackPane
        sp.getChildren().add(borderPane); 
        
        initializeMenuButton(stringOne, stringTwo); // Inits the buttons
        borderPane.setStyle("-fx-background-color: black");
        // this.setStyle("-fx-background-color: black");
        this.getChildren().add(borderPane);
    }
    
    // Inits the buttons stored in an array and placed in a VBox
    private void initializeMenuButton(String strOne, String strTwo) {
        settingsButtonsPane = new VBox();
        settingsButtonsPane.setSpacing(ProgramConstants.WINDOWHEIGHT/10);
                
        bMiddle = new Button[2]; // Two buttons are used
        
        // Styling of buttons
        bMiddle[0] = new Button(strOne); 
        bMiddle[1] = new Button(strTwo);
        for (int i = 0; i < 2; i++) {
            Text t = new Text();
            bMiddle[i].setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
            bMiddle[i].setMinSize(ProgramConstants.WINDOWWIDTH/3,
                    ProgramConstants.WINDOWHEIGHT/6);
            bMiddle[i].setMaxSize(ProgramConstants.WINDOWWIDTH/3,
                    ProgramConstants.WINDOWHEIGHT/6);
            
            // They aren't entirely rectangular, and should'nt be 
            bMiddle[i].setStyle
                ("-fx-background-color: black; -fx-border-color: white; "
                        + " -fx-border-radius: 3px;  -fx-text-align: center;"
                        + "-fx-font-family: Times; -fx-font-size: " +
                        Integer.toString(ProgramConstants.TEXT_SIZE));
        }
        
        settingsButtonsPane.getChildren().addAll(bMiddle);
        settingsButtonsPane.setAlignment(Pos.CENTER);
         // buttons in the center of the border pane 
        borderPane.setCenter(settingsButtonsPane);
    }
    
    /* Sets the new height of the pane structures when the pane changes its height
     * Autosizes the height of the pane componenets.*/ 
    public void setHeightButtons(double value) {
        for (int i = 0; i < bMiddle.length; i++) {
            bMiddle[i].setMinHeight(value/6);
            bMiddle[i].setMaxHeight(value/6);
        }
        settingsButtonsPane.setSpacing(value/10);
        borderPane.setMinHeight(value);
        borderPane.setMaxHeight(value);
    }
    
    /* Sets the new width of the pane structures when the pane changes its width
     * Autosizes the width of the pane componenets. */
    public void setWidthButtons(double value) {
        for (int i = 0; i < bMiddle.length; i++) { 
            bMiddle[i].setMinWidth(value/3); 
            bMiddle[i].setMaxWidth(value/3); 
        }   
        borderPane.setMinWidth(value);
        borderPane.setMaxWidth(value);
    }
    
    /* Returns the return button*/
    public Button getReturnButton() {
        return borderPane.getReturnButton();
    }
    
    /* Returns the first button */
    public Button getButtonOne() {
        return bMiddle[0];
    }
    
    /* Returns the second button */
    public Button getButtonTwo() {
        return bMiddle[1];
    }
    
    
    
}
