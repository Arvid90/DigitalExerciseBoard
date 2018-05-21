/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Exercises;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author joakimstoor
 */

/* Defines a static class with often used functions used by the view classes*/
public class StaticHelpMethods {
    
    /* Recieves an integer between 0 and infinity, returns the responding week 
    day. 1 is sunday, 2 monday etc. Integers above 7 are recalculated
    by x modulus 7 to get the corresponding week day.*/
    public static String setDays(int x) {
        
        if (x>7) { x = x % 7; }      
        switch(x) {
            case 1: return "Söndag";
            case 2: return "Måndag";
            case 3: return "Tisdag";
            case 4: return "Onsdag";
            case 5: return "Torsdag";
            case 6: return "Fredag";
            case 7: return "Lördag";  
            default: return "Lördag"; // for x = 0, i.e. -1 + 1 = 0 -> saturday
        }
    }
    

    /*Returns the URL of the image thet corresponds to a specific exercise*/
    public static String getURLFromExercises(Exercises e) {
        String str = "/Resources/";
        
        switch(e) {
            case GÅNG: str += "Walking"; break;
            case GÅNG_MED_GÅBORD: str += "WalkingTable"; break;
            case GÅNG_MED_RULLATOR: str += "WalkingWithWalker"; break;
            case GÅNG_MED_KRYCKOR: str += "WalkingWithCane"; break;
            case GÅNG_I_TRAPPA: str += "WalkingInStairs"; break;
            case SITTA: str += "Sit"; break;
            case SITTA_I_RULLSTOL: str += "Wheelchair"; break;   
            case AKTIVA_FOTTRAMP: str += "Fottramp"; break;
            case PEP_MASK: str += "PepMask"; break;
            case PEP_PIPA: str += "PepPipe"; break;
            case INDIVIDUELLT_TRÄNINGSPROGRAM: str += "TrainingProgram"; break;
            // case STÅ_PÅ_TIPPBRÄDA: str += ""; break; 
        }
        str += ".jpg"; return str;
    }
        
    /*Returns a formated string representing the date of the calendar date
    in the form dd-mm-yyyy*/
    public static String dateAsString(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH); String sd = "";
        int month = calendar.get(Calendar.MONTH) + 1; String sm = "";
        int year = calendar.get(Calendar.YEAR);
        
        
        
        if (day > 9) { sd = day + ""; }
        else { sd = "0" + day; }
        if (month > 9) { sm = month + ""; }
        else { sm = "0" + month; }
        
        return sd + "-" + sm + "-" + year;
    }   
    
    
    /*Returns todays date as a formated string in the form dd-mm-yyyy*/
    public static String todayDateaAsString() {
        Calendar calendar = new GregorianCalendar();
        return dateAsString(calendar);
    }   
    
    
    /*Compares two calendar dates, returns an int less than 0 if the first 
    calendar date is less than the first, 0 if equal, and a positive int if the
    first is bigger than the second date, comparisons are made on dates of the 
    form dd-MM-yyyy*/
    public static int compareDates(Calendar c1, Calendar c2) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return (sdf.format(c1.getTime()).compareTo(sdf.format(c2.getTime())));
    }
    
    /*Creates a calendar date from a string of the format dd-mm-yyyy*/
    public static Calendar createCalendarFromString(String dischargeDateString) {
        // Gergorian calendar lists January as month 0. Therefor month is 
        // decremented by 1 in the constructor call
        
        int day = Integer.parseInt(dischargeDateString.substring(0, 2));
        int month = Integer.parseInt(dischargeDateString.substring(3, 5));
        int year = Integer.parseInt(dischargeDateString.substring(6));
        Calendar cal = new GregorianCalendar(year, month-1, day);
        return cal;
    
    }
    
    /* Returns the increment or decrement value used for the specified exercise*/
    public static int getIncrementFromExercise(Exercises e) {
        if (e.equals(Exercises.GÅNG) || e.equals(Exercises.GÅNG_MED_KRYCKOR) || 
                e.equals(Exercises.GÅNG_MED_GÅBORD) || e.equals(
                        Exercises.GÅNG_MED_RULLATOR)) {
            return 50;
        }
        else if (e.equals(Exercises.SITTA) || e.equals(Exercises.SITTA_I_RULLSTOL)) {
            return 5;
        }
        return 1;
    }
    
    /* Returns the string text used when choosing an amount of reps for an exercise 
     * specified by the exercise enum */
    public static String repsText(Exercises e) {
        switch (e) {
            case GÅNG:
            case GÅNG_MED_KRYCKOR:
            case GÅNG_MED_GÅBORD:
            case GÅNG_MED_RULLATOR:
                return "Distans (m)";
            case SITTA:
            case SITTA_I_RULLSTOL:
                return "Duration (min)";
            case GÅNG_I_TRAPPA:
                return "Antal steg";
            case PEP_MASK:
            case PEP_PIPA:
                return "Antal andetag";
            case AKTIVA_FOTTRAMP:
                return "Antal fottramp";
            default:
                return "Antal reps";
        }
    }
    
    /* Returns a formatted string representation of the specified exercise */
    public static String returnFormattedString(Exercises e) {
        switch (e) {
            
            case GÅNG: return "Gång";
            case GÅNG_MED_GÅBORD: return "Gång med bord";
            case GÅNG_MED_KRYCKOR: return "Gång med kryckor";
            case GÅNG_MED_RULLATOR: return "Gång med rullator";
            case SITTA_I_RULLSTOL: return "Sitta i rullstol";
            case SITTA: return "Sitta";
            // case STÅ_PÅ_TIPPBRÄDA: return "Stå på tippbräda";
            case GÅNG_I_TRAPPA: return "Gång i trappa";
            case PEP_MASK: return "PEP-mask";
            case PEP_PIPA: return "PEP-pipa";
            case AKTIVA_FOTTRAMP: return "Aktiva fottramp";
            default: return "Individuellt träningsprogram";
        }
    }
    
    /* Returns the unit as a string for an exercise enum */
    public static String returnUnitString(Exercises e) {
        
        switch (e) {
            case GÅNG:
            case GÅNG_MED_GÅBORD:
            case GÅNG_MED_KRYCKOR:
            case GÅNG_MED_RULLATOR:
                return "m";
            case SITTA_I_RULLSTOL:
            case SITTA:
            //case STÅ_PÅ_TIPPBRÄDA:
                return "min";
            case GÅNG_I_TRAPPA:
                return "steg";
            default:
                return "st";
        }
    }
    
    /* Styles and positions the alerts used in the various view classes */
    public static void stylingAlert(Alert alert, 
            Pane pane, Stage stage) {
        alert.initOwner(stage);
        alert.setX(stage.getX() + (pane.getWidth() - (alert.getWidth()/2)));
        alert.setY(stage.getY() + (pane.getHeight() - (alert.getHeight()/2)));
    }
    
}
