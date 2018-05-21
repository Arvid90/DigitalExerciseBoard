package View;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author joakimstoor
 */

/* Defines constants used in the graphical user interface*/
public class ProgramConstants {
    public static int WINDOWWIDTH = 800;
    public static int WINDOWHEIGHT = 400;
    
    public static boolean isPI = false;
    
    public static int TEXT_LIMIT = 14;
    
    public static double H_FRACTION_DAY = 4.5; // Always > 3
    public static double H_FRACTION_MENU = 1/(1-(3/H_FRACTION_DAY));
    public static double W_FRACTION = 5; // set in stone
    
    public static int TEXT_SIZE = 16;
    public static int TEXT_SIZE_MENU = 20;
    public static int EXERCIZE_TEXT_SIZE = 13;
    public static Color PATIENTBACKGROUNDCOLOR = Color.BLACK;
    public static Color PATIENTOUTLINECOLOR = Color.WHITE;
    public static String PASSWORD = "blge";
    public static Font SCORE_TEXT_FONT = Font.font("Verdana", 16);
    
}
