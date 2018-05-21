package Controller;

import Model.DayPlan;
import Model.Patient;
import Model.Exercises;
import Model.exercise;
import View.DayPaneExercise;
import View.ExercisePane;
import View.PatientGUIPane;
import View.MainClass;
import java.util.ArrayList;
import View.ExerciseSchedulingMainPane;
import View.ExerciseSchedulingSecondaryPane;
import View.IncrementDecrementPane;
import View.StaticHelpMethods;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.time.temporal.ChronoUnit;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



/**
 * 
 * @author joakimstoor
 */

/* Defines a class that communicates with the model and the view when 
 * inputs are incoming from the view*/
public class MainController {
    private Patient patient;
    private ExerciseSchedulingMainPane exerciseSchedulingPane;
    private MainClass mainGUI;
    private Timer timer;
    private DateFormat df;
    private Date dateToExecute;

    /* Constuctor creates a MainController that awaits a patient registration,
     * patient is intially set to null */
    public MainController(MainClass mainGUI) {
        this.mainGUI = mainGUI;
        this.patient = null;
    }
     
    /* Initializes a timer that reboots the system with a change of date, i.e.
     * every new day*/
    private void initTimer() {
        if (timer==null) { 
            timer = new Timer(); 
            df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        }
        System.out.println("--> Current time: " + df.format( new Date()));
        dateToExecute = calculateExecutionDateAndTime();
        if (dateToExecute == null) {
            System.out.println("--> Wrong date formatting, patient rebooting is necessary");
        }
        else {  
            System.out.println(("--> Execution time: " + df.format(dateToExecute)));
            timer.schedule(new MyTimerTask(), dateToExecute);         
        }        
    }
    
    /* Unnecessary method, but used for clarity. Called from the MainClass during 
     * rebooting*/
    public void refreshTimer() {    
        timer.purge(); 
    }
    
    /* Deletes the timer functionality, and therefore the scheduling*/
    public void deleteTimer() {
        timer.cancel(); timer.purge(); timer = null; 
        System.out.println("Timer is deleted");
    }
    
    
    /* Initializes a new patient */
    public void createPatient(String iDString) {
        initTimer(); 
        patient = new Patient(iDString, new GregorianCalendar());
    }
    
    /* Returns the patient-instance. Null if no patient is registered*/
    public Patient getPatient() {
        return patient;
    }
    
    /* Called from the MainClass during rebooting. Unnecessary call, but used
     * dor clarity*/
    public void nullPatient() {
        patient = null;
    }
    
    /* Creates a new exercise of the specified enum type for the registered 
     * patient. If the number of exercises exceeds 4, it's not added.
     * Calls createNewExerciseToView*/
    public void createNewExercise(Exercises e) {
        if (patient.getAmountOfExercises()<4) { 
            patient.addNewExercise(e); 
            createNewExerciseToView(e);
        }
    }
    
    /* Updates the view to the user to include the new exercise as a field with 
     * an Image of the exercise and 3 day fields.*/
    private void createNewExerciseToView(Exercises e) {
            mainGUI.getPatientGUIPane().addExercise(e);
            mainGUI.getSechedulingPane().addImageView(e); 
            
            // If the number of exercises becomes 4 the button to include 
            // new exercises is disabled, should'nt be able to include more 
            // than 4
            if (mainGUI.getPatientGUIPane().getAmountOfExercises()==4) {
                mainGUI.getSettingsPaneExercise().getButtonOne().setDisable(true);
            }
            
            // If the button to the sheduling of use exercises i disabled, the
            // number of exercises was 0, but is 1 now. the button is teherfore
            // activated
            if (mainGUI.getSettingsPaneExercise().getButtonTwo().isDisable()) {
                mainGUI.getSettingsPaneExercise().getButtonTwo().setDisable(false);
            }
    }
            
    
    /* Deletes the exercise specified by the enum type*/
    public void deleteExercise(Exercises e) {
        patient.deleteExercise(e);
        mainGUI.getChooseNewExercisePane().addExercise(e);
        mainGUI.getSechedulingPane().removeImageView(e);
        mainGUI.getPatientGUIPane().removeExercise(e);
        if(patient.getAmountOfExercises()==0) {
            mainGUI.getSettingsPaneExercise().getButtonTwo().setDisable(true);
        }
        else if(patient.getAmountOfExercises()==3) {
            mainGUI.getSettingsPaneExercise().getButtonOne().setDisable(false);
        }
    }    
    
    /* Handles day schedulings for the specified exercise. Imparameters holds a list
     * of the sets to do for this day and the following 3 days, and a similair list 
     * for the reps. */
    public void handleIncrementDecrement(ArrayList<IncrementDecrementPane> sets,
            ArrayList<IncrementDecrementPane> reps, Exercises e) {
        exercise exercise = patient.getExercise(e);
        
        // First check if the exercise exists
        if(exercise != null) {
        
            /* Loops through the sets and the reps, creates new dayplans for 
             * the exercise if they are'nt included yet, or changes the dayplans
             * with the information held in thle lists if they already exists*/
            for (int i = 0; i < sets.size(); i++) {
                Calendar cal = Calendar.getInstance(); // gets todays date
                cal.add(Calendar.DAY_OF_YEAR, i); // increments the date
                if(exercise.doesDayPlanExist(cal)) {
                    exercise.fixDayPlan(sets.get(i).getMaxAmount(), 
                            reps.get(i).getMaxAmount(), cal);
                }
                else { exercise.addDayPlan(sets.get(i).getMaxAmount(), 
                        reps.get(i).getMaxAmount(), cal); 
                }
            }
             
            // Calls method to fix the user interface for every exercise
            for(ExercisePane eP : mainGUI.getPatientGUIPane().getExercises()) {
                if (eP.isExercise(exercise)) {
                    handleDayExercisePanes(eP, sets, reps);
                    break;
                }
            }
        }
    }
    
    
    /* Sets the patient exercise pane to show the sets and repititons to the 
     * patient*/
    private void handleDayExercisePanes(ExercisePane eP, ArrayList<IncrementDecrementPane> sets,
            ArrayList<IncrementDecrementPane> reps) {
        
        /* First check if the this day is the enrollment date. If it is, the 
         * 3 dayfields in the view are looped through with the first 3 of the 4 
         * days kept in the lists. If it isn't the case, the last 2 are looped 
         * through with the first 2 days*/
        if (StaticHelpMethods.todayDateaAsString().equals(patient.enrollmentDateToString())) {
            for(int i = 0; i < sets.size()-1; i++) {
                Calendar cal = new GregorianCalendar();
                cal.add(Calendar.DAY_OF_YEAR, i);
                String str = StaticHelpMethods.setDays(cal.get(Calendar.DAY_OF_WEEK));
                eP.setExercisePane(i, sets.get(i).getCurrentAmount(), sets.get(i).getMaxAmount(), 
                        reps.get(i).getMaxAmount(), str);
            }
        }
        else {
            for(int i = 0; i < sets.size()-2; i++) {
                Calendar cal = new GregorianCalendar();
                cal.add(Calendar.DAY_OF_YEAR, i);
                String str = StaticHelpMethods.setDays(cal.get(Calendar.DAY_OF_WEEK));
                eP.setExercisePane(i+1, sets.get(i).getCurrentAmount(), 
                        sets.get(i).getMaxAmount(), reps.get(i).getMaxAmount(), str);
            }
        }
    }
     
    /* Increments the indicator for the patient and increments the amount of 
     * fininshed sets in the patient model*/ 
    public void handleExerciseFieldClicked(Exercises e, int currentAmount) {
        exercise exercise = patient.getExercise(e);
        
        //First check if the patient performs the exercise 
        if (exercise!=null) {
            exercise.setProgressOfToday(currentAmount);
            ExerciseSchedulingSecondaryPane eSP = 
                    mainGUI.getSechedulingPane().getExercise(e);
            // check if the the scheduling pane for the exercise is active
            if (eSP!=null) {
                // Sets the current amount for increment decrement pane 0, this
                // is the sets pane,not the reps pane
                eSP.setIncrementDecrementPane(0, currentAmount);
            }
        }
    }
    
    
    /* Creates a .ser file from the model classes that implemets the serializable 
     * interface. The file is a bit map of the patient*/
    public void savePatientFile() {
        try {
            File dir = new File(System.getProperty("user.home") + "/Desktop/PatientData/");
            
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(System.getProperty
                    ("user.home") + "/Desktop/PatientData/patient.ser");
            
            // File file = new File("src/PatientFiles/patient.ser");
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(patient);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in src/PatientFiles/patient.ser");
        }
        catch (IOException i) {
            System.out.println("File not found Exception");
        }
    }
    
    /* Deletes the patient file when the active user is deleted*/
    public void deleteFile() {
        try {
            File file = new File(System.getProperty("user.home") + "/Desktop/PatientData/patient.ser");
        
            // Checks if the file is found. It's deleted if it's found
            if(file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else { System.out.println("Deletion of file failed"); } 
        } catch (Exception e) { e.printStackTrace(); }
    } 
    
    /* Loads the patient file when the java program is booted. */
    public void loadPatientFile() {
        patient = null;
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.home") +
                    "/Desktop/PatientData/patient.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            patient = (Patient) in.readObject();
            in.close();
            fileIn.close(); 
            System.out.println("Serialized data is loaded from src/PatientFiles/patient.ser");
        }
        catch (IOException i) { 
            System.out.println("File not found Exception"); 
        }
        catch(ClassNotFoundException cNF) {
            System.out.println("Patient class not found");
        }
    }    
    
    /* Called to load the view from the patient when the program is 
     * booted */
    public void loadView() {
        Calendar cal = new GregorianCalendar();

        /* Patient pane and IncrementDecrement panes are updated, id is set
         * in the two panes in the view.*/
        mainGUI.getSystemSettingsPane().setUserName(patient.getId());
        mainGUI.getPatientGUIPane().getPatientSettings().setIDText(patient.getId());
        mainGUI.getPatientGUIPane().getPatientSettings().setDateText(getNumberOfDays());
        for (int i = 0; i < patient.getExercises().size(); i++) {
            createNewExerciseToView(patient.getExercises().get(i).getEnum());
            mainGUI.getChooseNewExercisePane().deleteExerciseImageView(
                    patient.getExercises().get(i).getEnum());
            if (StaticHelpMethods.compareDates(cal, patient.getEnrollmentDate()) == 0) {
                handleTodayInread(i);
            } else {
                handleOtherDayInread(i);
            }
            handleLoadIncrementDecrementPanes(i);
        }
        initTimer();//Initializes the timer after the patient view is loaded
    }

    /* Loads one exercise view for the registered patient, takes in the
     * ineteger representing the index of a specific exercise and fills the
     * indicators in the view, called exercises.size() number of times.
     * This is called if this is the enrollment date day*/
    private void handleTodayInread(int i) {
        exercise e = patient.getExercises().get(i);       
        ExercisePane exercisePane = mainGUI.getPatientGUIPane().getExercisePane(e.getEnum());
        Calendar cal = new GregorianCalendar(); 
        
         int x = 0;
         for (DayPlan dP : e.getDayPlans()) {
             exercisePane.setExercisePane(x, dP.getProgress(), dP.getSets(), dP.getReps(), 
                     StaticHelpMethods.setDays(cal.get(Calendar.DAY_OF_WEEK)));
             cal.add(Calendar.DATE, 1);
             x++;
             if (x==3) {break;}
         }    
    }
    
    /* Same as the above, but called if today is not not the patient enrollment
     * date*/
    private void handleOtherDayInread(int i) {
        exercise e = patient.getExercises().get(i);
        ExercisePane exercisePane = mainGUI.getPatientGUIPane().getExercisePane(e.getEnum());
        
        Calendar c = new GregorianCalendar(); Calendar m = new GregorianCalendar();
        c.add(Calendar.DATE, -1); m.add(Calendar.DATE, -1); 
        
        boolean b = false; int y = 0;
        
        /* b = true if tomorrow is in the dayPlans list -> panes are filled*/
        for (int j = 0; j < e.getDayPlans().size(); j++) {
            if (StaticHelpMethods.compareDates(c, 
                    e.getDayPlans().get(j).getDate())==0 && (!b)) {
                    b = true;
            }
            if (b && y < 3) {
                DayPlan dP = e.getDayPlans().get(j);
                exercisePane.setExercisePane(y, dP.getProgress(), dP.getSets(),
                            dP.getReps(), StaticHelpMethods.setDays(m.get(Calendar.DAY_OF_WEEK)));
                m.add(Calendar.DATE, 1); y++;
                if (y==3) {break;}
            }
        }
        
        
        /* If tomorrow is not in the dayPlans list, the if statement is passed
         * and the exercise panes are filled for the exercise, from today and
         * forward. If not, nothing is done*/
        if (!b) {
            m.add(Calendar.DATE, 1); 
            for (int j = 0; j < e.getDayPlans().size(); j++) {
                if(StaticHelpMethods.compareDates(new GregorianCalendar(), 
                        e.getDayPlans().get(j).getDate())==0 && (!b)) { b = true; }
            
                if (b && y < 2) {
                    DayPlan dP = e.getDayPlans().get(j);
                    exercisePane.setExercisePane(y+1, dP.getProgress(), dP.getSets(),
                                dP.getReps(), StaticHelpMethods.setDays(m.get(Calendar.DAY_OF_WEEK)));
                    m.add(Calendar.DATE, 1); y++;
                    if (y==3) {break;}
                }
            }
        }   
        
    }
    
   
    /* Fills the Increment and Decrement panes in the settings space for the 
     * exercise determined by the index. */
    private void handleLoadIncrementDecrementPanes(int i) {
        // exercise is coming
        Calendar cal = Calendar.getInstance();
        ExerciseSchedulingSecondaryPane mm = 
                                mainGUI.getSechedulingPane().getExercisePanes().get(i);
        
        int x = 0; boolean bool = false;
        for (DayPlan dP : patient.getExercises().get(i).getDayPlans()) {
            if (StaticHelpMethods.compareDates(cal, dP.getDate())==0) { bool = true; }
            if(bool){
                mm.setIncrementDecrementPane(x++, dP.getProgress(), 
                        dP.getSets(), dP.getReps());
            }    
        }
    }
    
    /* Returns the number of days the patient has been registered. Not a part
     * of the patient model*/
    private int getNumberOfDays() {
        Calendar cal = new GregorianCalendar();
        int x = 1;
        while (StaticHelpMethods.compareDates(patient.getEnrollmentDate(), cal)!=0) {
            cal.add(Calendar.DATE, -1);
            x++;
        }
        return x;
    }
    
    /*Help method for the timer functionality, returns the new date that 
     * executes a TimerTask, i.e. reboots the patient panes*/
    private Date calculateExecutionDateAndTime() {
        try {
            Calendar c = new GregorianCalendar();
            c.add(Calendar.DATE, 1);// adds a day to this day
            
            Date d = df.parse(StaticHelpMethods.dateAsString(c) + " 00:00:30");
            return d;
        } catch(ParseException pE) {  }
        return null; // No timing will be incorporated
    }
    
    /* Is called when the date scheduling set for the above date is the same as 
     * this moment. The system patient panes are rebooted by calling a method in 
     * the main class */
    private class MyTimerTask extends TimerTask {
        @Override 
        public void run() {
            Platform.runLater(() -> {
                System.out.println("Registered change of date -> updated user interface");           
                MainController.this.mainGUI.rebootUser();
            });
        }
    }
    
}
