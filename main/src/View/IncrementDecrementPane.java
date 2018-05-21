/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Exercises;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author joakimstoor
 */

/* Self-made implementation of an incrementor and decrementor, a counter. An 
 * increment and decrement button with the result shown in the middle. A text 
 * presenting the structure is shown to the left. */
public class IncrementDecrementPane extends StackPane {
    
    private Button addButton;
    private Button subtractButton;
    private int maxAmount;
    private int minAmount;
    private Label informationLabel;
    private Label amountLabel;
    private final ExerciseSchedulingSecondaryPane exSchPane;
    private final boolean isSet;
    
    /* Initializes an instantiation of this class with buttons and textfields
     * of a size specified by the double fracton*/
    public IncrementDecrementPane(ExerciseSchedulingSecondaryPane  exSchPane,
            double fraction, Pane pane, boolean isSet) {
        this.exSchPane = exSchPane;
        this.isSet = isSet;
        addButton = initializeButton("+", pane, fraction);
        subtractButton = initializeButton("-", pane, fraction);
        StackPane sp = initializeTextIAndDField(pane, fraction);
        StackPane np = initializeDateTextField(pane, fraction, isSet);
        
        HBox hbox = initializeHBox(np, sp, fraction);

        // this.dateLabel = new Label(dateString + ": ");
  
        Group group = new Group();
        group.getChildren().add(hbox);

        
        this.getChildren().add(group);
        
    }
    
    /* Initialzes the + and - buttons, the sizes of these depends on the fraction*/
    private Button initializeButton(String str, Pane pane, double fraction) {
        Button button = new Button(str);
        
        button.prefWidthProperty().bind(pane.widthProperty().divide(fraction*2));
        button.prefHeightProperty().bind(pane.widthProperty().divide(fraction*2));
        // button.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        button.getStyleClass().add("IandD-button");
        
        // Sets the buttons on action
        if (str.equals("+")) { button.setOnAction(e -> increment()); }
        else if (str.equals("-")) { button.setOnAction(e -> decrement()); }
        
        return button;
    }
    
    /* Initializes the textfield showing the integer that is incremented or 
     * decremented*/
    private StackPane initializeTextIAndDField(Pane pane, double fraction) {
        Rectangle r = createRectangle(pane, fraction*2, fraction*2);   
        r.setStroke(Color.LIGHTSLATEGREY);
        r.setFill(ProgramConstants.PATIENTOUTLINECOLOR);
        
        maxAmount = 0; minAmount = 0;
        amountLabel = new Label(Integer.toString(maxAmount)); 
        amountLabel.setTextFill(Color.BLACK);
        
        // holds the rectangle with a white background and the textfield with 
        // the integer
        StackPane sp = new StackPane();        
        sp.getChildren().addAll(r, amountLabel);
        
        return sp;
    }
    
    // Initializes the textfield showing the type of increment decrementor
    private StackPane initializeDateTextField(Pane pane, double fraction, boolean isSet) {
        Rectangle r = createRectangle(pane, fraction/2, fraction*2); 
        r.setFill(ProgramConstants.PATIENTBACKGROUNDCOLOR);
        r.setStroke(ProgramConstants.PATIENTOUTLINECOLOR);
        
        String s = "Antal set"; 
        
        // Calls a static method to get a string that depends on the exercise enum
        if (!isSet) { s = StaticHelpMethods.repsText(exSchPane.getExercise()); }
        
        informationLabel = new Label(s); 
        informationLabel.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        informationLabel.setFont(Font.font("Times", FontPosture.REGULAR, 
                ProgramConstants.TEXT_SIZE));
        
        // Rectangle and label are stored in a stackpane to position them
        StackPane sp = new StackPane();        
        sp.getChildren().addAll(r, informationLabel);
        
        return sp;
    }
    
    /* Called by the private textfield-methods, creates the rectangles in the 
     * stackpanes created there */
    private Rectangle createRectangle(Pane pane, double fraction_1, double fraction_2) {
        Rectangle r = new Rectangle();   
        
        r.widthProperty().bind(pane.widthProperty().divide(fraction_1));
        r.heightProperty().bind(pane.widthProperty().divide(fraction_2));
        
        r.setStroke(Color.LIGHTSLATEGREY);
        r.setFill(ProgramConstants.PATIENTOUTLINECOLOR);
        r.setArcHeight(5);
        r.setArcWidth(5); 
        return r;
    }
    
    // Initializes the HBox holding the two stackpanes and buttons
    private HBox initializeHBox(StackPane np, StackPane sp, double fraction) {
        HBox hbox = new HBox();
        hbox.spacingProperty().set(fraction/2);
        hbox.getChildren().addAll(np, subtractButton, sp, addButton);
        return hbox;
    }
   
    /* increments the counter*/
    private void increment() {
        // Have to check if it's a set or a rep. If it's a rep, have to check 
        // if it's an exercis that has to do with walking 
        if (isSet) { maxAmount++; }
        else {  maxAmount += StaticHelpMethods.getIncrementFromExercise(
                        exSchPane.getExercise());
        }
        amountLabel.setText(Integer.toString(maxAmount));
        exSchPane.updateSaveButton();
    }
    
    /* Decrements the counter*/
    private void decrement() {
        
        // Have check that it's not possible to below currentAmount
        
        if (maxAmount > minAmount) {
            if (isSet) { maxAmount--; }
            else { maxAmount -= StaticHelpMethods.getIncrementFromExercise(
                        exSchPane.getExercise());
            }
            amountLabel.setText(Integer.toString(maxAmount));
            exSchPane.updateSaveButton();
        }
    }
    
    /* Returns the value of the counter*/
    public int getMaxAmount() {
        return maxAmount;
    }
    
    /* Returns the minimal value that this counter can take */
    public int getCurrentAmount() {
        return minAmount;
    }
    
    /* Sets the value of the counter and the minimal value the counter can take*/
    public void setLimits(int maxAmount, int minAmount) {
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        amountLabel.setText(Integer.toString(this.maxAmount));
    }
    
    /* Sets the minimal value the counter can take*/
    public void setCurrentAmount(int minAmount) {
        this.minAmount = minAmount;
    }
    
}
