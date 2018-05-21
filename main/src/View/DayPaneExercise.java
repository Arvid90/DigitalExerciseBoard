package View;

import Model.Exercises;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.scene.control.ProgressIndicator;

import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

/**
 * @author joakimstoor
 */


// Arkitektur & användarnamn, databas, modulariserad 
// Bindings , properties, listeners

/* Defines a pane showing an indicator and a progress text*/
public class DayPaneExercise extends Pane {
    
    private StackPane stackPane, sP;
    private String day;
    private ProgressIndicator progressIndicator;
    private int maxAmount, currentAmount, reps;
    private Label setText;
    private boolean isActive;
    private final ExercisePane exercisePane;
    private Exercises exercise;
    private PatientGUIPane parentPane;
    private Group g;
    String unit;
    
    /* Initializes the constructor with an exercise and a maxAmount for the number 
     * of sets. The width (x) and height (y) of this pane is determined as a 
     * fraction of the ParentPane */
    public DayPaneExercise(Exercises exercise, PatientGUIPane parentPane, ExercisePane exercisePane,
            int maxAmount, double x, double y) {
        
        // Underlying stackPane of specified fraction
        stackPane = new MyStackPane(parentPane, x, y);
        this.exercise = exercise;
        day = "";
        isActive = false; // Inactive at first
        this.exercisePane = exercisePane;
        this.maxAmount = maxAmount;
        this.reps = 0;
        this.currentAmount = 0;
        unit = StaticHelpMethods.returnUnitString(exercise);
        this.parentPane = parentPane;
        
        createTextLabel(); // creates the label of sets and reps
        initializeIndicator(); // activates the indicator
        updateText(); // Updates the textLabel, is called after the other two
        
        this.getChildren().add(stackPane); //I.e. this class is a pane
        
        // Activates the pane, adds listener
        this.setOnMouseClicked(e -> {
            if (DayPaneExercise.this.maxAmount != 0) {
                Calendar cal = new GregorianCalendar();
                String dayString = StaticHelpMethods.setDays(cal.get(Calendar.DAY_OF_WEEK));
                if (day.equals(dayString)) {
                    if(currentAmount == DayPaneExercise.this.maxAmount) {
                        currentAmount = 0; updateText();
                        progressIndicator.setProgress(0); 
                    }
                    else { incrementProgress(); }
                    parentPane.getCotntroller().handleExerciseFieldClicked(
                                exercise, currentAmount);
                    parentPane.getCotntroller().savePatientFile();
                }
            }
        });    
    }
    
    /* Sets the progress indicator with a progress of 0 and styles it*/
    private void initializeIndicator() {
        progressIndicator = new ProgressIndicator(0);
        progressIndicator.setStyle("-fx-progress-color: green; ");
        progressIndicator.getStyleClass().add("ProgressIndicator");
        progressIndicator.setMouseTransparent(true);   
        
        /*Has to be included, the padding makes the text free from the indicator 
         * space*/
        progressIndicator.setPadding(new Insets(5,0,12,0)); 

        //Indicator and text are stored in there own StackPane
        sP = new StackPane();
        sP.getChildren().addAll(progressIndicator);
        sP.setAlignment(Pos.CENTER);
        
        // Primary stackPane stores the other StackPane
        stackPane.getChildren().addAll(progressIndicator, setText);
        StackPane.setAlignment(setText, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(progressIndicator, Pos.TOP_CENTER);
        
        // If maxAmount is 0, the indicator is set to be unvisible
        if (maxAmount==0) { progressIndicator.setVisible(false); }
    }
    
    /* Label initialized before indicator to be included in the common StackPane*/
    private void createTextLabel() {
        setText = new Label("");
        setText.setTextAlignment(TextAlignment.CENTER);
        setText.setAlignment(Pos.CENTER);
        setText.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        // Has to be included to free some space around the text from the indicator
        setText.setPadding(new Insets(5)); 
        setText.setFont(ProgramConstants.SCORE_TEXT_FONT);
    }

    /* Increments the progress of sets by the increment i if the 
     * progress + i is less or equals the maximum amount */
    public void incrementProgress(int i) {
        if ((currentAmount+i) <= maxAmount) {
            currentAmount = currentAmount + i;
            /* Updates the text from the current amount, call comes from the 
             * conttroller */
            updateText(); 
            double value = (double) currentAmount/maxAmount;
            progressIndicator.setProgress(value); 
        }
    }
    
    /* Decrements the progress by increment i if current amount - i is bigger 
     * or equal to 0*/
    public void decrementProgress(int i) {
        if (currentAmount-i >= 0) {
            currentAmount = currentAmount - i;
            updateText();
            double value = (double) currentAmount/maxAmount;
            progressIndicator.setProgress(value);
        }
    }
    
    /* Updates the text label presented to the patient. Different texting 
     * is used, all depeding on if this day has not gone by yet*/
    public void updateText() {
        if (maxAmount != 0) {
            if (!isActive) { 
                setText.setText(Integer.toString(maxAmount)
                    + " x "+Integer.toString(reps) + " " + unit); }   
            else {
                setText.setText(Integer.toString(currentAmount)
                        + "/" + (Integer.toString(maxAmount)) + " x " + 
                        Integer.toString(reps) + " " + unit); }   
        } else { setText.setText(""); }
    }
    
    /* Increments the progress of performed sets by the increment 1 */
    public void incrementProgress() {
        incrementProgress(1);
    }

    /* Decrements the progress of performed sets by the decrement 1 */    
    public void decrementProgress() {
        decrementProgress(1);
    } 
    
    /* Sets the maximum value */
    public void setMaxValue(int val) {
        // Call is coming from the controller
        maxAmount = val;
        updateText();
        
        if (maxAmount != 0) {
            progressIndicator.setVisible(true); 
            progressIndicator.setProgress((double) currentAmount/maxAmount); 
        } else { progressIndicator.setVisible(false);}
    }
    
    /* Returns the amount of sets to be done*/
    public int getMaxValue() {
        return maxAmount;
    }
    
    /* Sets the current amount of sets that has been done*/
    public void setCurrentAmount(int i) {
        this.currentAmount = i;
    }
    
    /* Sets the day of the pane as a string, this is a week day of specified form, 
     * i,e. Söndag, Måndag, Tisdag, etc.*/
    public void setDay(String s) {
        this.day = s;
        isActive = true;
        
        if (maxAmount!=0) {
            // Tests if the day hasn't come around yet
            if (testTomorrowOrNext()) {
                progressIndicator.setOpacity(0.2);
                isActive = false; // Set to false if it hasn't
            }
            updateText(); // If maximum amount != 0
        }   
    }
    
    /* Sets the amount of reps for this exercise day field*/
    public void setReps(int reps) { this.reps = reps; }
    
    /* Tests if this day is tomorrow or the day after that, if so returns true.
     * If not, returns false, i.e. this day has happened or is today*/
    private boolean testTomorrowOrNext() {
        Calendar cal = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal2.add(Calendar.DAY_OF_YEAR, 2);
        return (day.equals(StaticHelpMethods.setDays(cal.get(Calendar.DAY_OF_WEEK))) ||
                    day.equals(StaticHelpMethods.setDays(cal2.get(
                            Calendar.DAY_OF_WEEK))));
    }
    
    /* Returns the exercisePane that stores this pane*/
    public ExercisePane getExercisePane() {
        return exercisePane;
    }
    
    
}


