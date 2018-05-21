package View;

import Model.Exercises;
import Model.exercise;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.ArrayList;


/**
 * @author joakimstoor
 */

/* Defines a pane holding an exercise image view and three dayfields with 
 * progress indicators */
public class ExercisePane extends Pane {
    
    private VBox mainPane;
    private PatientGUIPane parentPane;
    private ImageView imageview;
    private boolean isWalking;
    private ArrayList<DayPaneExercise> dayFields;
    private Exercises exercise;
    
    
    /* Inits the exercise pane with a specific exercise enum */
    public ExercisePane(Exercises exercise, PatientGUIPane parentPane) {
        this.exercise = exercise; 
        /* MainPane is a VBox with an image view and three panes storing 
         * an indicator showing the progress of the day*/
        mainPane = new VBox(); 
        dayFields = new ArrayList();
        imageview = new ImageView();
        this.isWalking = exercise.equals(Exercises.GÅNG);
        this.parentPane = parentPane;
        // initializeImageView(image); // Must be called first 
        Image image = new Image(StaticHelpMethods.getURLFromExercises(exercise), true);
        
        double x = ProgramConstants.W_FRACTION;
        double y = ProgramConstants.H_FRACTION_MENU;
        double w = ProgramConstants.H_FRACTION_DAY;
        
        // Image view representing the exercise
        StackPane ap = new ExerciseImageView(exercise, image, x, y, parentPane, true);
        mainPane.getChildren().add(ap);
        inititlizeDayFields(x, w); // Inits the size of the dayfield
        
        this.getChildren().add(mainPane); // this pane is a vbox
    }
    
    /* Inits the day fields which are of the DayPaneExercise type, there are 
     * three of them*/
    private void inititlizeDayFields(double x, double y) {
        for (int i = 0; i < 3; i++) { 
            dayFields.add(new DayPaneExercise(exercise, parentPane, this, 0, x, y));
        }
        mainPane.getChildren().addAll(dayFields);
    }
    
    /* Returns the dayfields in an ArrayList*/
    public ArrayList<DayPaneExercise> getExerciseFields() {
        return dayFields;
    }
    
    /* Returns true if this exercise pane is in the patient view, false if this 
     * isn't the case*/
    public boolean isWalking() { return isWalking; }
    
    /* Sets the infromation in a dayfield specified by the index fieldNumber. This 
     * information is the current amount of sets done, max amount of sets 
     * and the amount of reps, the day is also set*/
    public void setExercisePane(int fieldNumber, int current, int sets,  int reps, String str) {   
        dayFields.get(fieldNumber).setReps(reps); 
        dayFields.get(fieldNumber).setCurrentAmount(current);
        dayFields.get(fieldNumber).setMaxValue(sets);// Must be after 1 & 2
        dayFields.get(fieldNumber).setDay(str); // must be after!!!
    }
    
    /* Returns true if this exercise is representing an exercise of the specified
     * exercise enum e, false if not*/
    public boolean isExercise(exercise e) {
        return e.getEnum().equals(this.exercise);
    }
    
    /* Returns the exercise enum of this exercise pane*/
    public Exercises getExercise() {
        return exercise;
    }
}
