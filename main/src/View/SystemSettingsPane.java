package View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author joakimstoor
 */

/* Defines a system settings pane showing the patient user name, todays date, 
 * and a button to delete the user. Also incorporates an exit and return button*/
public class SystemSettingsPane extends Pane {
    
    private final MyBorderPane borderPane;
    private ArrayList<Label> labelListTitles;
    private ArrayList<Label> labelListActuals;
    private GridPane gridPane;
    private Calendar calendar;
    private MainClass mainClass;
    private Button eraseUserButton;
    
    /* Initializes an instantiation of this class with a specified user id (name)*/
    public SystemSettingsPane(MainClass mainClass, String iDString) {
        borderPane = new MyBorderPane(mainClass, true, true); 
        this.mainClass = mainClass;
        initializeButton(); // creates delete user buttons
        initilizelabelList(); // Labels used to present user id and todays date 
        initializeActualLabels(iDString); // user id label and date label
        initilizeGridPane(); // Stroing the labels in a list 
        
        /* Grid pane and delete user button are vertically spaced and centered on
         * the window */
        VBox vb = new VBox(20); 
        vb.getChildren().addAll(gridPane, eraseUserButton);
        vb.setAlignment(Pos.CENTER);
        
        // stackPane.getChildren().add(gridPane);
        borderPane.setCenter(vb);

        this.setStyle("-fx-background-color: black");
        this.getChildren().add(borderPane);
    }
    
    /* Returns return button*/
    public Button getReturnButton() {
        return borderPane.getReturnButton();
    }
    
   /* Initializes the labels used to present the used id and todays date*/
    private void initilizelabelList() {
        labelListTitles = new ArrayList();
        labelListTitles.add(new Label("Användarnamn: "));
        labelListTitles.add(new Label("Dagens datum: "));
        loopList(labelListTitles); // Styling, def function
    }
 
    /* Initializes the actuals labels to hod the user name and todays date*/
    private void initializeActualLabels(String iDString) {
        labelListActuals = new ArrayList(); 
        labelListActuals.add(new Label(iDString));
        calendar = new GregorianCalendar();
        
        // Gets todays date as a formatted string    
        labelListActuals.add(new Label(updateDateString(calendar)));       
        loopList(labelListActuals);
    }    
    
    /* Styles lables found in list of labels*/
    private void loopList(ArrayList<Label> list) {
        for (Label l : list) {
            l.setFont(Font.font("Times", FontPosture.ITALIC, 20));
            l.setTextFill(ProgramConstants.PATIENTOUTLINECOLOR);
        }
    }   
    
    /* Returns a calendar instant of today*/
    public Calendar getCalendarDate() {
        return calendar;
    }
    
       
    /* Formats todays string */
    private String updateDateString(Calendar cal) {
        int day = cal.get(Calendar.DAY_OF_MONTH); String sd;
        int month = cal.get(Calendar.MONTH) + 1; String sm;
        int year = cal.get(Calendar.YEAR);        
        
        if (day > 9) { sd = day + ""; }
        else { sd = "0" + day; }
        if (month > 9) { sm = month + ""; }
        else { sm = "0" + month; }
        
        return sd + "-" + sm + "-" + year;
    }
   
    /* Initializes the grid pane discussed above*/
    private void initilizeGridPane() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); // Is centered
        gridPane.setPadding(new Insets(20));
        // hgap and vgap depends on the size of the border pane 
        gridPane.hgapProperty().bind(borderPane.heightProperty().divide(30));
        gridPane.vgapProperty().bind(borderPane.widthProperty().divide(45));
        
        // Positioning shall not be changed! Obviously.
        gridPane.add(labelListTitles.get(0), 0, 0);
        gridPane.add(labelListTitles.get(1), 0, 1);
        
        gridPane.add(labelListActuals.get(0), 1, 0);
        gridPane.add(labelListActuals.get(1), 1, 1);
       
     
    } 
 
    /* Intitializes the delete user button */
    private void initializeButton() {
        eraseUserButton = new Button("Radera användaren");
        eraseUserButton.setFont(Font.font("Arial Black", FontWeight.BOLD, 14));
        eraseUserButton.setTextFill(Color.RED);
        eraseUserButton.setMinSize(200, 50);
        eraseUserButton.setMaxSize(200, 50);
        eraseUserButton.setStyle
                ("-fx-background-color: black; -fx-border-color: white"); 
        // If pressed, calls a function to delete the user in the MainClass
        eraseUserButton.setOnAction(e -> {
            mainClass.deleteUser();
        });
    }
    
    /* Sets the new width of the used borderPane when this pane changes its width.
     * Autosizes the BorderPane to fit the pane. */
    public void setThisWidth(double value) {
        borderPane.setMinWidth(value);
        borderPane.setMaxWidth(value);
    }
    
    /* Sets the new height of the used borderPane when this pane changes its height.
     * Autosizes the BorderPane to fit the pane. */
    public void setThisHeight(double value) {
        borderPane.setMinHeight(value);
        borderPane.setMaxHeight(value);
    }    
    
    /* Sets the user name id of the patient*/
    public void setUserName(String ID) {
        labelListActuals.get(0).setText(ID); // Position 0 in the list
    }
}


