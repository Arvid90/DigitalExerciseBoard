package View;

import Model.Exercises;
import Model.exercise;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author joakimstoor
 */

/* Defines a pane holding scheduling possibilities for a performed exercise*/
public class ExerciseSchedulingSecondaryPane extends Pane {
    
    private MyBorderPane borderPane;
    private Label exerciseLabel;
    private VBox vbox;
    private VBox vboxIncrements;
    private ScrollPane scrollPane;
    // private ArrayList<IncrementDecrementPane> incrementDecrementList;
    private ArrayList<IncrementDecrementClass> incrementDecrementClassList;
    
    private ExerciseSchedulingMainPane exerciseSchedulingMainPane;
    private BorderPane mainPane;
    private Button saveButton, deleteExerciseButton;
    private Exercises exercise;
    private MainClass mainClass;
    
    /* Initializes an instance of this class for an exercise specified by the 
     * exercise enum in the constructor call */
    public ExerciseSchedulingSecondaryPane(ExerciseSchedulingMainPane 
            exerciseSchedulingMainPane, BorderPane mainPane,
                Exercises exercise, MainClass mainClass) {
        this.exerciseSchedulingMainPane = exerciseSchedulingMainPane ;
        this.mainPane = mainPane; 
        this.mainClass = mainClass;
        this.exercise = exercise;
        // the border pane will have a remove exercise button, and a return button
        borderPane = new MyBorderPane(mainClass, false, true);
        // Gets the remove exercise button
        deleteExerciseButton = borderPane.getExitButton(); 
        
        /*  VBox -> Holding the exercise label and the hbox with the days where 
         * sets and reps can be chosen*/
        initializingVBox(); 
        initializeScrollPane();
        initSaveButton();
        
        // Checks if the increment decrement panes have been changed but not saved
        borderPane.getReturnButton().setOnAction(e -> {
            if (!saveButton.isDisable()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Träningsinformationen är inte sparad!");
                alert.setContentText("Vill gå vidare ändå?");
                StaticHelpMethods.stylingAlert(alert, mainPane, 
                    mainClass.getPrimaryStage());
        
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    this.mainPane.setCenter(this.exerciseSchedulingMainPane);
                }
                else if (result.get() == ButtonType.CANCEL) { alert.close();}    
            } else { this.mainPane.setCenter(this.exerciseSchedulingMainPane); }
        });
        
        /* Shows an alert for a user, he/she has to confirm if the exercise 
         * should be removed*/
        deleteExerciseButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Vill du verkligen ta bort övningen?");
            alert.setContentText("Bekräfta borttagningen");
            StaticHelpMethods.stylingAlert(alert, mainPane, 
                    mainClass.getPrimaryStage());
            
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                exerciseSchedulingMainPane.getController().deleteExercise(exercise);
                if (exerciseSchedulingMainPane.getNumberOfExercises()!=0) { 
                    mainClass.setScene(exerciseSchedulingMainPane); }
                else { mainClass.setScene(mainClass.getSettingsPaneExercise()); }
            }
            else if (result.get() == ButtonType.CANCEL) { alert.close();} 
        });
                

        // HBox holding the scrollpane and the save button
        HBox hb = new HBox();
        hb.getChildren().addAll(scrollPane, saveButton);
        hb.spacingProperty().bind(borderPane.widthProperty().divide(70));
        hb.setAlignment(Pos.CENTER); 
  
        // The Vbox mentioned above, has the exercise label and the hb
        vbox.getChildren().addAll(exerciseLabel, hb);
        vbox.setAlignment(Pos.CENTER);
                
        // Vbox is in the center of a MyBorderPane
        borderPane.setCenter(vbox);        
        
        this.setStyle("-fx-background-color: black;");
        this.getChildren().add(borderPane); // This is a pane
        
        // This pane has listeners for width and height changes
        this.widthProperty().addListener(ov ->  {
            resizeWidth(this.getWidth());
        });
        this.heightProperty().addListener(ov -> {
            resizeHeight(this.getHeight());
        });
    }
    
    /* Vertical Scroll pane is used to store day increment decrement panes */
    private void initializeScrollPane() {
        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No Hbar
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);    
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.getStyleClass().add("scroll-pane"); // Styling from css
        scrollPane.setStyle("-fx-border-color: white; -fx-background: black");
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxSize(ProgramConstants.WINDOWWIDTH/1.3, 
                ProgramConstants.WINDOWHEIGHT/1.7);
        scrollPane.setMinSize(ProgramConstants.WINDOWWIDTH/1.3, 
                ProgramConstants.WINDOWHEIGHT/1.7);
        scrollPane.setContent(vboxIncrements);
    }
    
    /* Initializes the exerciseLabel of this exercise*/
    private void initializeExerciseLabel() {
        this.exerciseLabel = new Label(StaticHelpMethods.returnFormattedString(exercise));
        exerciseLabel.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        exerciseLabel.setFont(Font.font("Times", ProgramConstants.TEXT_SIZE));
        exerciseLabel.setAlignment(Pos.CENTER);
        exerciseLabel.setPadding(new Insets(10,20,10,20)); // For formatting
        // Not entirely rectangular exercise label 
        exerciseLabel.setStyle("-fx-border-color: white; -fx-background-radius: 1em "); 
        // Has to be translated a bit for formatting purposes
        exerciseLabel.translateXProperty().bind(borderPane.widthProperty().divide(-10));
    }
    
    /* Inits VBox holding the increment decrement panes */
    private void initializingVBox() {
        
        vbox = new VBox();
        vbox.spacingProperty().bind(borderPane.heightProperty().divide(20));
        vbox.setStyle("-fx-background-color: transparent;");
        vbox.setAlignment(Pos.CENTER);
        vboxIncrements = new VBox();
        vboxIncrements.setPadding(new Insets(10));
        vboxIncrements.spacingProperty().bind(borderPane.heightProperty().divide(20));
        vboxIncrements.setStyle("-fx-background-color: black;");
        initializeExerciseLabel();
        changeSchedulingPaneTertiary();
    }
    
    /* Creates and postions the scheduling counters for this and the coming 3 days*/
    public void changeSchedulingPaneTertiary() {
        // Do not out-comment this code again you fuck!
        Calendar c_1 = new GregorianCalendar();
        int x = c_1.get(Calendar.DAY_OF_WEEK);
        
        // code that depends on discharge date --> not active 
        // int y = getAmountOfIncrementDecrementPanes(c_1);
        int y = 4; // 4 days from this day and forward is used in the scheduling
        
        List<String> list = new ArrayList(); // Day strings
        List<Calendar> dates = new ArrayList(); // Calendars for the days
        
        for (int i = 0; i < y; i++) {
            list.add(StaticHelpMethods.setDays(x+i));
            Calendar c = new GregorianCalendar(); c.add(Calendar.DATE, i);
            dates.add(c);
        }
        
        
        /* list of priavte clas instantiations storing date label and 2 increment
         * decrement panes*/
        if (incrementDecrementClassList==null) {
            incrementDecrementClassList= new ArrayList(); 
            for (int i = 0; i < list.size(); i++) {
                IncrementDecrementClass iDC = new IncrementDecrementClass(
                    list.get(i), dates.get(i));
                incrementDecrementClassList.add(iDC); 
                vboxIncrements.getChildren().add(iDC.g); // vbox to show info
                vboxIncrements.setAlignment(Pos.CENTER);
            }
        } 
    }
   
    /* Changes the width of the different pane components when this panes width 
     * changes*/
    private void resizeWidth(double value) {
        borderPane.setMinWidth(value);
        borderPane.setMinWidth(value);
        scrollPane.setMaxWidth(value/1.3);
        scrollPane.setMinWidth(value/1.3);
        saveButton.setMinWidth(value/6);
        saveButton.setMaxWidth(value/6);
    }
    
    /* Changes the height of the different pane components when this panes height 
     * changes*/
    private void resizeHeight(double value) {
        borderPane.setMinHeight(value);
        borderPane.setMinHeight(value);
        scrollPane.setMaxHeight(value/1.7);
        scrollPane.setMinHeight(value/1.7);    
        saveButton.setMinHeight(value/12);
        saveButton.setMaxHeight(value/12);        
    }
    
    /* Returns the exercise enum of this exercise*/
    public Exercises getExercise() {
        return exercise;
    }
    
    /* Activates the save button in the pane when called*/
    public void updateSaveButton() {
        saveButton.setDisable(false);  
    }
    
    /* Initializes the save button and puts it into place in the hbox*/
    private void initSaveButton() {
        saveButton = initializeButton();
        borderPane.setRight(saveButton);
        
        // Lists to update the model
        ArrayList<IncrementDecrementPane> sets = new ArrayList();
        ArrayList<IncrementDecrementPane> reps = new ArrayList();
        
        /* A decrement and increment pane for the sets and reps respectively
         * All day info about sets and reps for the 4 days are included to the 
         * lists */
        saveButton.setOnAction(e -> {
            saveButton.setDisable(true); 
            for (IncrementDecrementClass iDC : incrementDecrementClassList) {
                sets.add(iDC.incrementDecrementPanes.get(0));
                reps.add(iDC.incrementDecrementPanes.get(1));
            } 
            
            /* The controller gets the lists and updates the model and some other 
             * view panes */
            exerciseSchedulingMainPane.getController().handleIncrementDecrement(
                    sets, reps, exercise); 
            
            sets.clear(); reps.clear(); // Lists has to be cleared for next time
        });
    }
    
    /* Styles the save button*/
    private Button initializeButton() {
        Button button = new Button("Spara");
        button.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        button.setMinSize(ProgramConstants.WINDOWWIDTH/6,
                    ProgramConstants.WINDOWHEIGHT/12);
        button.setMaxSize(ProgramConstants.WINDOWWIDTH/6,
                    ProgramConstants.WINDOWHEIGHT/12);
        button.setStyle("-fx-background-color: black; "
                + "-fx-border-color: white; -fx-border-radius: 3px;  "
                + "-fx-text-align: center; -fx-font-family: Times; "
                + "-fx-font-size: " + Integer.toString(ProgramConstants.EXERCIZE_TEXT_SIZE));
        
        button.setDisable(true); // Is disabled from the start
        return button;
    }
    
    /* Sets the counter panes for the specified day chosen by index
     * j. Finished number of sets, and sets and reps to be done are set*/
    public void setIncrementDecrementPane(int j, int current, int set, int reps) {
        incrementDecrementClassList.get(j).incrementDecrementPanes.get(0).
                setLimits(set, current);
        incrementDecrementClassList.get(j).incrementDecrementPanes.get(1).
                setLimits(reps, 0);
    }
    
    /* Sets the current number of finished sets for a specified day determined by 
     * index j*/
    public void setIncrementDecrementPane(int j, int current) {
        incrementDecrementClassList.get(j).incrementDecrementPanes.get(0).
                setCurrentAmount(current);
    }
    
    /* Returns the number of days used for scheduling*/
    public int getIncrementDecrementPaneSize() {
        return incrementDecrementClassList.size();
    }
    
    /* Private class to store the counters and a day label.*/
    private class IncrementDecrementClass {
        
        StackPane sP;
        HBox hbox;
        Rectangle rectangle;
        Label dateLabel;
        ArrayList<IncrementDecrementPane> incrementDecrementPanes;
        Group g;
        Calendar calendar;
        
        /* Instantiates an instant of this class for a specified day */
        public IncrementDecrementClass(String s, Calendar calendar) {
            sP = new StackPane(); // stores dayfield in an rectangle
            this.calendar = calendar;
            initLabel(s);
            rectangle = newRectangle(6, 6*2); // day text in rectangle  
            sP.getChildren().addAll(rectangle, dateLabel); 
            sP.setAlignment(Pos.CENTER); // Is centered
            initIncrementDecrementPanes();
            initHBox(); // Done last, stores all the structures horizontally
            
            // Everything stored in a group for autosizing purposes
            g = new Group(); g.getChildren().add(hbox); 
        }
        
        /* Inits the hbox holding the  dayfield and two counters */
        private void initHBox() {
            hbox = new HBox();
            // Spacing property bound to the size of the background pane
            hbox.spacingProperty().bind(ExerciseSchedulingSecondaryPane.this.
                    borderPane.widthProperty().divide(20));
            VBox vbox = new VBox(10); // VBox stores the counters
            vbox.getChildren().addAll(incrementDecrementPanes);
            hbox.getChildren().addAll(sP, vbox);
            hbox.setAlignment(Pos.CENTER);
        }
        
        /* Inits a generic label with a string*/
        private void initLabel(String s) {
            dateLabel = new Label(s);
            dateLabel.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
            dateLabel.setFont(Font.font("Georgia", FontWeight.BOLD, FontPosture.ITALIC, 14));
        }
        
        /* Inits a new rectangle which is bound to background pane*/
        private Rectangle newRectangle(double w_fraction, double h_fraction) {
            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(ExerciseSchedulingSecondaryPane.
                    this.borderPane.widthProperty().divide(w_fraction));
            rect.heightProperty().bind(ExerciseSchedulingSecondaryPane.
                    this.borderPane.widthProperty().divide(h_fraction));
            rect.setFill(ProgramConstants.PATIENTBACKGROUNDCOLOR);
            rect.setStroke(ProgramConstants.PATIENTOUTLINECOLOR);
            rect.setArcHeight(5); // Not entirely rectangular
            rect.setArcWidth(5); // Not entirely rectangular
            return rect;
        }
        
        /* Inits the counters */
        private void initIncrementDecrementPanes() {
            incrementDecrementPanes = new ArrayList();
            incrementDecrementPanes.add(new IncrementDecrementPane(
                    ExerciseSchedulingSecondaryPane.this, 10, borderPane, true));
            incrementDecrementPanes.add(new IncrementDecrementPane(
                    ExerciseSchedulingSecondaryPane.this, 10, borderPane, false));  
            
        }
        
    }
    
}
