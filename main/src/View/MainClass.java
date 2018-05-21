package View;

import Model.Patient;
import Controller.MainController;
import Model.Exercises;

import javafx.application.Application;

import javafx.stage.Stage;

import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

/**
 * @author joakimstoor
 */

/*Main class of the view of this MVC program, extends Application*/
public class MainClass extends Application {
    
    private Scene startScene;
    private BorderPane headPane;
    private SettingsPane settingsPaneMain, settingsPaneExercise;
    private PatientGUIPane patientPane;
    private ExerciseSchedulingMainPane exerciseSchedulingPane;
    private ChooseNewExercisePane chooseExercisePane;
    private SystemSettingsPane systemSettingsPane;
    private MainController controller;
    private NewPatientRegisterPane newRegisterPane;
    private String iDString; // = "19930304-XXXX";
    private Stage primaryStage;
    
    
    public void start(Stage stage) {
        
        this.primaryStage = stage;
        headPane = new BorderPane(); //Current pane is stored in headPane.Center
        newRegisterPane = new NewPatientRegisterPane(this, primaryStage);        
        
        /*Creates the communication link between the Model and View*/    
        controller = new MainController(this);
        /* Check if a patient is stored in file, if this is the case, 
         * loads the patient */
        controller.loadPatientFile(); 
        
        
        if (controller.getPatient()==null) {
            /* If no patient is loaded from file, a new can be created in this 
             * pane */
            headPane.setCenter(newRegisterPane); 
        }
        else {
            /* If a patient is stored in file, all the view panes are acivated
             * first, then the view is filled with the information stored in 
             * the patient model*/
            withPatientMethod("");
            controller.loadView();
            headPane.setCenter(patientPane);
        }

        /* Initial scene size is set to whats specified in the program constant
         * class */
        startScene = new Scene(headPane, ProgramConstants.WINDOWWIDTH, 
                ProgramConstants.WINDOWHEIGHT);
        startScene.getStylesheets().add(getClass().getResource(
                "/Resources/stylesheet.css").toExternalForm());
        
        /* NewRegisterPanes height- and width-properties gets listeners
         * who listens to changes and sets its new height and width 
         * to new hwight and width */
        newRegisterPane.heightProperty().addListener(ov -> 
                newRegisterPane.setThisHeight(newRegisterPane.getHeight()));
        newRegisterPane.widthProperty().addListener(ov -> 
                newRegisterPane.setThisWidth(newRegisterPane.getWidth()));                        
        

        primaryStage.setResizable(true);
        primaryStage.setMinHeight(375);
        primaryStage.setMinWidth(700);
        
        if (ProgramConstants.isPI) { 
            primaryStage.setFullScreen(true);
            startScene.setCursor(Cursor.NONE);
        }
        
        primaryStage.setScene(startScene);
        // For the exit button of the stage
        primaryStage.setOnCloseRequest(e -> { e.consume(); exitAlert(); });
        primaryStage.show();
    }
    
    /* Creates a new patient from the newPatientRegisterPane, called from their*/
    protected void createNewPatient(String id) {
        if (controller.getPatient() == null) {
            iDString = id; // Gets the id from the textField
            controller.createPatient(iDString);
            withPatientMethod(iDString);
            headPane.setCenter(patientPane);
        }
    }
    
    /* Initializes all the View panes for an active user, these are only active 
     * while a patient is stored in the system*/
    private void withPatientMethod(String iDString) {
        
        settingsPaneMain = new SettingsPane(this,"Övningar", "Systeminställningar");
        settingsPaneExercise = new SettingsPane(this,"Ny övning", "Nuvarande övningar");
        settingsPaneExercise.getButtonTwo().setDisable(true);
        patientPane = new PatientGUIPane(this, primaryStage, null); 
        
        //If bli bla bla
        
        /* Creates the an array with all the exercise enums from the model, a 
         * lot of exercises*/
        ArrayList<Exercises> exercises = new ArrayList();
        for (Exercises e: Exercises.values()) { exercises.add(e); }
        
        /* Creates a string list with the URL for the respective enum in the 
         * above list*/
        ArrayList<String> str = new ArrayList();
        for(int i = 0; i < exercises.size(); i++) {
            str.add(StaticHelpMethods.getURLFromExercises(exercises.get(i)));
        } 

        chooseExercisePane = new ChooseNewExercisePane(this, exercises);

        // List of week days to be presented to the patient
        ArrayList<String> daysAsStrings; 
        Boolean b = false; // is enrollment date today? Holds the answer to this

        if (controller.getPatient() != null) {
            if (controller.getPatient().enrollmentDateToString().equals(
                    StaticHelpMethods.todayDateaAsString())) {
                // If enrollment date is today the text menu is designed i one way
                daysAsStrings = setDaysArray(true);
                patientPane.setDayTextMenu(daysAsStrings);
            } else { b = true; }
        } else { b = true; }
        if (b) {
            /* If the enrollment date is another day the text menu is designed 
             * in another way */
            daysAsStrings = setDaysArray(false);
            patientPane.setDayTextMenu(daysAsStrings);
        }

        systemSettingsPane = new SystemSettingsPane(this, iDString);
        exerciseSchedulingPane = new ExerciseSchedulingMainPane(headPane,
                controller.getPatient().getExercises(), this);

        patientPane.getPatientSettings().setIDText(iDString);
        
        // SettingsButton calls a self-made popup stage
        patientPane.getPatientSettings().getSettingsButton().setOnAction(e -> {
            newStage(primaryStage, headPane);
        });

        /* Automatically saves patientFile when back to the patient Pane from 
         * this settings pane */
        settingsPaneMain.getReturnButton().setOnAction(e -> {
            headPane.setCenter(patientPane);
            controller.savePatientFile();
            
        });
        settingsPaneMain.getButtonOne().setOnAction(e
                -> headPane.setCenter(settingsPaneExercise));
        settingsPaneMain.getButtonTwo().setOnAction(e
                -> headPane.setCenter(systemSettingsPane));
        systemSettingsPane.getReturnButton().setOnAction(e
                -> headPane.setCenter(settingsPaneMain));

        settingsPaneExercise.getReturnButton().setOnAction(e
                -> headPane.setCenter(settingsPaneMain));
        settingsPaneExercise.getButtonOne().setOnAction(e
                -> headPane.setCenter(chooseExercisePane));
        settingsPaneExercise.getButtonTwo().setOnAction(e
                -> headPane.setCenter(exerciseSchedulingPane));

        chooseExercisePane.getReturnButton().setOnAction(e
                -> headPane.setCenter(settingsPaneExercise));

        exerciseSchedulingPane.getReturnButton().setOnAction(e
                -> headPane.setCenter(settingsPaneExercise));

        // Method to resize nodes when the background pane changes size
        resizeMethod();   
    }
    
    /* Resizes the children of the mainPane when the background Pane changes its
     * size. The background Pane is obviously different for the different views*/
    private void resizeMethod() {
        settingsPaneMain.widthProperty().addListener(ov -> 
                settingsPaneMain.setWidthButtons(settingsPaneMain.getWidth()));
        settingsPaneMain.heightProperty().addListener(ov -> 
                settingsPaneMain.setHeightButtons(settingsPaneMain.getHeight()));
        settingsPaneExercise.widthProperty().addListener(ov -> 
                settingsPaneExercise.setWidthButtons(settingsPaneExercise.getWidth()));
        settingsPaneExercise.heightProperty().addListener(ov -> 
                settingsPaneExercise.setHeightButtons(settingsPaneExercise.getHeight()));
        
        chooseExercisePane.widthProperty().addListener(ov -> 
                chooseExercisePane.fixWidthOfNewExercisePane(
                        chooseExercisePane.getWidth()));
        chooseExercisePane.heightProperty().addListener(ov -> 
                chooseExercisePane.fixHeightOfNewExercisePane(
                        chooseExercisePane.getHeight()));
        
        exerciseSchedulingPane.heightProperty().addListener(ov -> 
                exerciseSchedulingPane.fixHeightOfMainPain(
                        exerciseSchedulingPane.getHeight()));
        exerciseSchedulingPane.widthProperty().addListener(ov -> 
                exerciseSchedulingPane.fixWidthOfMainPain(
                        exerciseSchedulingPane.getWidth()));
        systemSettingsPane.widthProperty().addListener(ov ->
                systemSettingsPane.setThisWidth(systemSettingsPane.getWidth()));
        systemSettingsPane.heightProperty().addListener(ov ->
                systemSettingsPane.setThisHeight(systemSettingsPane.getHeight()));
            
    }
    
    /* Creates the array of weekday strings used in the main patient view for 
     * the exercises. */
    private ArrayList<String> setDaysArray(boolean bool) {
        ArrayList<String> daysAsStrings = new ArrayList();
        Calendar calendar = new GregorianCalendar();
        int x = calendar.get(Calendar.DAY_OF_WEEK);
        int i = 0;
        
        if(!bool) { i = -1; } // Yesterday, means possible day is x = 0;
        
        for (int j = i ; j < i+3; j++) {
            daysAsStrings.add(StaticHelpMethods.setDays(x+j)); //[0,9]
        }
        return daysAsStrings;
    }
    
    /* Sets the main Pane of the scene */
    public void setScene(Node e) {
        headPane.setCenter(e);
    }
    
    /* Returns the controller*/
    public MainController getController() {
        return controller;
    }
    
    /* Returns the Patient View Pane*/
    public PatientGUIPane getPatientGUIPane() {
        return patientPane;
    }
    
    /* Returns the Pane showing the performed exercises*/
    public ExerciseSchedulingMainPane getSechedulingPane() {
        return exerciseSchedulingPane;
    }
    
    /* Returns the Pane with buttons to go to add new exercise or performed
     * exercises*/
    public SettingsPane getSettingsPaneExercise() {
        return settingsPaneExercise;
    }
    
    /* Returns the choose new exercise pane where it's possible to choose 
     * a new exercise which isn't used yet*/
    public ChooseNewExercisePane getChooseNewExercisePane() {
        return chooseExercisePane;
    }
    
    /* Returns the system settings pane where one can delete the user*/
    public SystemSettingsPane getSystemSettingsPane() {
        return systemSettingsPane;
    }
    
    /* Deteletes the user using an alert*/
    public void deleteUser() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("Vill du verkligen ta bort användaren? \n"
                + "All data kommer att gå förlorad");
        alert.setContentText("Bekräfta om du vill fortgå!");
        // Static method similar for all the alerts in the program
        StaticHelpMethods.stylingAlert(alert, headPane, primaryStage);
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            controller.nullPatient(); controller.deleteFile();
            controller.deleteTimer(); // Has to ber rid off
            // nulls all the panes which are only active when the patient is 
            nullPatientActivePanes(); 
            alert.close();
            headPane.setCenter(newRegisterPane); // For a new patient
        }
        else if (result.get() == ButtonType.CANCEL) { alert.close();}        
    }
    
    // Reboots the user after 24.00
    public void rebootUser() {
        boolean bool = true;
        // Checks if somebody is in the settings panes while this is happening
        if (headPane.getCenter().equals(patientPane)) { bool=false;}

        controller.refreshTimer(); // Purges the timer
        nullPatientActivePanes(); // Nulls all the patient panes
        withPatientMethod(""); // Inits all the patient panes again 
        controller.loadView(); // Loads the patient into the view again
        if (!bool) { headPane.setCenter(patientPane);}
    }
    
    /* Nulls all the patient Panes*/
    public void nullPatientActivePanes() {
        settingsPaneMain = null; settingsPaneExercise = null; 
        settingsPaneExercise = null; patientPane = null; 
        chooseExercisePane = null; systemSettingsPane = null;
        exerciseSchedulingPane = null;
    }
    
    /* Returns the primary stage used*/
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /* Creates a new stage with password field to enter the settings view*/
    public void newStage(Stage primaryStage, BorderPane headPane) {

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL); // has to be used
        dialog.initOwner(primaryStage);
        
        int width = 300; int height = 130; // Specifies the size of the stage
        
        dialog.setAlwaysOnTop(true); // This one should always be on top
        
        /* Code to position the stage ralative to the owner stage in the same
         * maner, always */
        dialog.setX(primaryStage.getX() + (MainClass.this.headPane.getWidth()-width)/2);
        dialog.setY(primaryStage.getY() + 30);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setAlignment(Pos.CENTER_LEFT);
        passwordField.setMaxWidth(150);
        passwordField.setMaxWidth(150);
        passwordField.setFont(new Font(12));
        
        Label lm = new Label("Särskild behörighet krävs!");
        Label l = new Label("Lösenord:");
        lm.setFont(Font.font("Times New Roman", 18));
        l.setFont(Font.font("Times New Roman", 15));
        
        // Stores "lösenord" and passwordField horizontally 
        HBox hbox_1 = new HBox(20);
        hbox_1.getChildren().addAll(l, passwordField);
        hbox_1.setAlignment(Pos.CENTER);
        
        // For the styling, old school style
        Rectangle rect = new Rectangle(width, 1);
        rect.setFill(Color.DIMGREY);
        
        // Ok and Avbryt buttons
        Button b1 = createButton("OK");
        Button b2 = createButton("Avbryt");
        
        // Stores the two buttons horozintally, sepcified width to fit textfield
        HBox hbox_2 = new HBox(10);
        hbox_2.getChildren().addAll(b2, b1);
        hbox_2.setAlignment(Pos.CENTER);
        hbox_2.translateXProperty().setValue(42); // Fits to the textfield
        
        // Stores the hbox_1 and hbox_2 vertically
        VBox vb = new VBox(10);
        vb.getChildren().addAll(hbox_1, hbox_2);
        vb.setAlignment(Pos.CENTER);
        
        // stores the title label, the rectangle and the vb vertically
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(lm, rect, vb);
        vbox.setAlignment(Pos.CENTER);
        
        // Reset title if wrong password has been filled
        passwordField.setOnMouseClicked(e ->  { 
            dialog.setTitle("");
        }); 
        
        // Init buttons
        b1.setOnAction(e -> { 
            if (passwordField.getText().equals(ProgramConstants.PASSWORD)) {
                MainClass.this.headPane.setCenter(MainClass.this.settingsPaneMain);
                dialog.close();
            }
            else { 
                passwordField.setText("");
                dialog.setTitle("Fel lösenord!");//Show "wrong password" in title
            }
        });
        b2.setOnAction(e -> { dialog.close(); });
        
        dialog.setResizable(false);
        Scene dialogScene = new Scene(vbox, width, height);
        
        if (ProgramConstants.isPI) { dialogScene.setCursor(Cursor.NONE);}
        
        dialog.setScene(dialogScene);
        dialog.show();       
    }
    
    /* Inits buttons used in the self-made stage */
    private Button createButton(String s) {
        Button button = new Button(s);
        button.setMaxWidth(70);
        button.setMinWidth(70);
        return button;
    }
    
    /*Alert to exit the program, can be exited from the stage and almost all
     * of the settings panes*/
    public Alert exitAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Du loggar nu ut från programmet!");
        alert.setContentText("Bekräfta utloggningen");
        StaticHelpMethods.stylingAlert(alert, headPane, primaryStage);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (getController().getPatient()!=null) { // is a patient active? 
                getController().savePatientFile(); // Saves the patient file    
            }
            System.exit(0);
        } else if (result.get() == ButtonType.CANCEL) { alert.close(); }
        return alert;
    }
    
    /* Main method used, not necessary though*/
    public static void main(String[] args) {
        launch(args);
    }
    
}

