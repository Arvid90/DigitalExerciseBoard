package View;

import Controller.MainController;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.Image;

import javafx.scene.text.Font;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import Model.exercise;
import Model.Exercises;

/**
 * @author joakimstoor
 */

/* Defines a Pane holding the chosen exercises as image views, these can be clicked
 * to enter their respective scheduing view*/ 
public class ExerciseSchedulingMainPane extends Pane  {
    
    private MyBorderPane borderPane;
    private Label label;
    private final ArrayList<MyImageViewConnection> an;
    private BorderPane headPane;
    private VBox vbox;
    private HBox hbox;
    private MainClass mainClass;
    
    /* Initializes the pane holdong the chosen exercises*/
    public ExerciseSchedulingMainPane(BorderPane headPane, 
            ArrayList<exercise> exerciseList, MainClass mainClass) {
        this.mainClass = mainClass;
        an = new ArrayList(); // ArrayList of exercise Images
        this.headPane = headPane; 
        borderPane = new MyBorderPane(mainClass, true, true); // mainPane      
        
        // Holds the label "välj övning" and the exercise image views
        // Has white background
        vbox = new VBox();
        vbox.spacingProperty().bind(borderPane.heightProperty().divide(10));
        vbox.setPadding(new Insets(20, 20 ,20 , 20));
        vbox.setStyle("-fx-border-color: white");
        vbox.setMaxSize(ProgramConstants.WINDOWWIDTH/1.1, 
                ProgramConstants.WINDOWHEIGHT/1.5);
        vbox.setMinSize(ProgramConstants.WINDOWWIDTH/1.1, 
                ProgramConstants.WINDOWHEIGHT/1.5);
        
        
        // Inits the label and centralizes it
        label = new Label("Välj Övning");
        label.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        label.setFont(Font.font("Times", ProgramConstants.TEXT_SIZE));
        label.setAlignment(Pos.CENTER);
        
        // Stores the different exercise image views, empty from the beginning
        hbox = new HBox();
        hbox.spacingProperty().bind(this.widthProperty().divide(20));
        hbox.setAlignment(Pos.CENTER);
        
        // Vbox holds label and the exercises
        vbox.getChildren().addAll(label, hbox);
        vbox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vbox);
        
        this.setStyle("-fx-background-color: black;");
        this.getChildren().add(borderPane);
    } 
    
    /* Adds the new exercise into the list storing the info about the exercise*/
    public void addImageView(Exercises ex) {
        MyImageViewConnection m = new MyImageViewConnection(ex);
        an.add(m); // New instantiation of the private class 
        hbox.getChildren().add(m.exI); //adds the image view into the view hbox
        // If the image view is clicked the exercise scheduling is shown 
        // in the view
        m.exI.setOnMouseClicked(e -> headPane.setCenter(m.getSchPane()));
    }
    
    /* Removes the image view of the chosen exercises from the view*/
    public void removeImageView(Exercises e) {
        for (MyImageViewConnection mIVC: an) {
            if (mIVC.exercise.equals(e)) {
                hbox.getChildren().remove(mIVC.exI);
                an.remove(mIVC);
                break;
            }
        }
    }
    
    /* Returns the return button to enter the first SettingsPane */
    public Button getReturnButton() {
        return borderPane.getReturnButton();
    }
    
    /* When the width of the pane which has this as a listener changes this is called*/
    public void fixWidthOfMainPain(double value) {
        borderPane.setMaxWidth(value);
        borderPane.setMinWidth(value);
        vbox.setMaxWidth(value/1.1);
        vbox.setMinWidth(value/1.1);
        
    }
    
    /* When the height of the pane which has this as a listener changes this is called*/
    public void fixHeightOfMainPain(double value) {
        borderPane.setMaxHeight(value);
        borderPane.setMinHeight(value);
        vbox.setMaxHeight(value/1.5);
        vbox.setMinHeight(value/1.5);
    }   
    
    /* Returns the controller from the MainClass*/
    public MainController getController() {
        return mainClass.getController();
    }
    
    /* Returns the ExerciseSchedulingSecondaryPane of the exercise determined by
     * the exercise enum e*/
    public ExerciseSchedulingSecondaryPane getExercise(Exercises e) {
        for(MyImageViewConnection m : an) {
            if (m.getSchPane().getExercise().equals(e)) {
                return m.getSchPane(); // Stored in the private class
            }
        }
        return null;
    }
    
    /* Returns the ExerciseSchedulingSecondaryPanes in an ArrayList*/
    public ArrayList<ExerciseSchedulingSecondaryPane> getExercisePanes() {
        /* A new list has to be creeated, the information is stored in the 
         * private class*/
        ArrayList<ExerciseSchedulingSecondaryPane> a = new ArrayList();
        for(MyImageViewConnection m : an) {
            a.add(m.getSchPane());
        }
        return a;
    }
    
    /* Returns the number of exercises*/
    public int getNumberOfExercises() {
        return an.size();
    }

    /* Private class of an exercise storing an image of the exercise, a
     * SecondaryPane instantiation of the exercise, the exerise enum, and 
     * an image view*/
    private class MyImageViewConnection {
        
        private Image image;
        private ExerciseSchedulingSecondaryPane exS;
        private Exercises exercise;
        private ExerciseImageView exI;
        
        public MyImageViewConnection(Exercises exercise) {
            
            this.exercise = exercise;
            
            image = new Image(StaticHelpMethods.getURLFromExercises(exercise));
            exI = new ExerciseImageView(exercise, image, 6, 3, borderPane, false);
            exS = new ExerciseSchedulingSecondaryPane(
                    ExerciseSchedulingMainPane.this, headPane, exercise,
                    ExerciseSchedulingMainPane.this.mainClass);
        }
        
        /* Returns the exerciseImageView of this exercise*/
        public ExerciseImageView getExPane() {
            return exI;
        }
        
        /* Returns the ExerciseSchedulingSecondaryPane of this exercise */
        public ExerciseSchedulingSecondaryPane getSchPane() {
            return exS;
        } 
        
    }  
}
