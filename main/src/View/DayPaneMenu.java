package View;


import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * @author joakimstoor
 */

/* Defines a pane showing a specific weekday in the center of the pane */
public class DayPaneMenu extends Pane {
    
    private StackPane stackPane;
    private Text dayText;
    
    /* Creates a day pane menu with a size determined by the width (x) and height 
     * (y) fractions of the parent pane delivered as parameters*/
    public DayPaneMenu(PatientGUIPane parentPane, double x, double y) {
        /* Everything stored in a self-made stackPane, with a specific
         * height and width fraction of a parent background pane*/
        stackPane = new MyStackPane(parentPane, x, y);
        initializeText();
        
        this.getChildren().add(stackPane); // I.e. this is a pane
    }
    
    /* Inits the text showing the day as a string*/
    private void initializeText() {
        dayText = new Text();
        dayText.setFill(ProgramConstants.PATIENTOUTLINECOLOR);
        dayText.setFont(Font.font("Georgia", FontWeight.BOLD,
                ProgramConstants.TEXT_SIZE_MENU));
        stackPane.getChildren().add(dayText);
    }
    
    /* Sets the text showing the day*/
    public void setDayText(String string) {
        dayText.setText(string);
    }
        
    /* Gets the day of this pane as a string*/
    public String getDay() {
        return dayText.getText();
    }    
    
}
