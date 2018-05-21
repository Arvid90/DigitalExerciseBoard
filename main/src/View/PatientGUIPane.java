package View;

import Controller.MainController;
import Model.Exercises;
import Model.exercise;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.image.Image;

import javafx.scene.input.MouseEvent;

import javafx.event.EventHandler;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * @author joakimstoor
 */

/* PAne that shows the dayfield and exercises performed for the patient*/
public class PatientGUIPane extends Pane {
    
    private AnchorPane mainPane;
    private HBox staticSplitPane; // Maximum of 2 panes 
    private HBox exercisePane; // Maximum of 4 excercise panes
    private VBox menuVBox; // Maximum of 4 panes     

    // stores the day menu fields, gives us acces to the textfield so days 
    // can be changed
    private PatientSettingsPane patientSettings;
    private ArrayList<DayPaneMenu> days;
    private ArrayList<ExercisePane> exercises;
    private Stage primaryStage;
    
    private MainClass mainClass;
    
    /* Initializes a new patient view*/
    public PatientGUIPane(MainClass mainClass, Stage stage, ArrayList<Exercises> exercises) {
        // Black background for patient view
        this.setStyle("-fx-background-color: black;"); 
        mainPane = new AnchorPane(); // MainPane is an anchor pane
        
        // always null in the program, can be change easily though
        if (exercises == null) { this.exercises = new ArrayList(); }
        else { for (Exercises eX: exercises) { addExercise(eX); }}
        
        this.primaryStage = stage;
        
        this.mainClass = mainClass; 
        
        days = new ArrayList(); // 
        menuVBox = new VBox();
        exercisePane = new HBox(); // stores the exercise field, between 0 and 4
        staticSplitPane = new HBox(); // stores the menu and the exercisepane
        initializeMenu(); // Inits the menu
        
        staticSplitPane.getChildren().add(exercisePane);
        mainPane.getChildren().add(staticSplitPane);
        
        this.getChildren().add(mainPane);
    }
    
    /* Initilizes the menu, one settings box, and three day boxe*/
    private void initializeMenu() {
        // First, one patientsettingspane, for id, active days, and settingsbutton
        patientSettings = new PatientSettingsPane(this, ProgramConstants.W_FRACTION,
                ProgramConstants.H_FRACTION_MENU);
        // Three daypane menus are added
        for (int i = 0; i < 3; i++) {
            days.add(new DayPaneMenu(this, ProgramConstants.W_FRACTION, 
                    ProgramConstants.H_FRACTION_DAY));
        }
        menuVBox.getChildren().add(patientSettings);
        menuVBox.getChildren().addAll(days);
        staticSplitPane.getChildren().add(menuVBox);
        AnchorPane.setBottomAnchor(staticSplitPane, 0.0);
    }
    
    /* Adds a new exercise to the patient view determined by the specified exercise
     * enum */
    public void addExercise(Exercises exercise) {
        if (exercises.size() < 5) {
            ExercisePane ep = new ExercisePane(exercise, this);
            exercisePane.getChildren().add(ep);
            exercises.add(ep);
            AnchorPane.setTopAnchor(exercisePane, 0.0);
        }
    }
    
    /* Removes the exercise from the patientview specified by the exercise enum
     * if its in the view*/
    public void removeExercise(Exercises e) {
        int size = exercises.size();
        for (int i = 0; i < size; i++) {
            if (exercises.get(i).getExercise().equals(e)) {
                exercisePane.getChildren().remove(exercises.get(i));
                exercises.remove(exercises.get(i));
                break;
            }
        }
    }
        
    /* Returns the amount of exercises*/
    public int getAmountOfExercises() {
        return exercises.size();
    } 
    
    /* Returns the list of ExercisePanes in the patient view*/
    public ArrayList<ExercisePane> getExercises() {
        return exercises;
    } 
    
    /* Sets the day text fo the patient view menu, there are three days chosen
     * by list of strings in the array list*/
    public void setDayTextMenu(ArrayList<String> daysAsStrings) {
        if (daysAsStrings.size() == 3) {
            for (int i = 0; i < days.size(); i++) {
                days.get(i).setDayText(daysAsStrings.get(i));
            }
        }    
    }
    
    /* Returns the patient settings pane with patient id, active days, and
     * settings button */
    public PatientSettingsPane getPatientSettings() {
        return patientSettings;
    }
    
    /* Returns main controller found in the main class */
    public MainController getCotntroller() {
        return mainClass.getController();
    }
    
    /* returns the primary stage used in the program*/
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /* Returns the exercise pane specified by the exercise enum if its in the 
     * the patient view*/
    public ExercisePane getExercisePane(Exercises en) {
        for (ExercisePane eP : exercises) {
            if (eP.getExercise().equals(en)) { return eP; }
        }
        return null;
    }
    
}


