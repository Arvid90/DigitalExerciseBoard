package Model;

import View.StaticHelpMethods;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/* Defines an abstraction of a patient. Holding an ID and a number of exercises*/
public class Patient implements java.io.Serializable {
    private final String id;
    private Calendar enrollmentDate;
    private ArrayList<exercise> exercises;

    
    /* Creates a Patient with the parameter ID and an empty exercise list 
    with an enrollment date specified by the caller*/
    public Patient(String id, Calendar enrollmentDate) {    
        exercises = new ArrayList<>();
        this.id = id;
        // this.dischargeDate = dischargeDate;
        this.enrollmentDate = enrollmentDate;
    }

    /* Creates a Patient with the specified parameter ID and an empty exercise 
    list with an enrollment Calendar Date of today*/
    public Patient(String id) {
        this(id, new GregorianCalendar());
    }
    
    /* Returns the patient ID*/
    public String getId() {
        return id;
    }
    
    /* Returns the calendar date the patient enrolled*/
    public Calendar getEnrollmentDate() {
        return enrollmentDate;
    }
    

    /* Returns an ArrayList of the exercises the patient are performing*/
    public ArrayList<exercise> getExercises() {
        return exercises;
    }
    
    /* Adds a new exercise of the type determined by the enum in-parameter*/
    public void addNewExercise(Exercises ex) {
        exercises.add(new exercise(ex));
    }

    /* Searches for an exercise determined by the enum in-parameter, if the 
     * patient performs it, it is deleted*/
    public void deleteExercise(Exercises e) {
        for (exercise exerc : exercises) {
            if(exerc.getEnum().equals(e)) { 
                exercises.remove(exerc); 
                break;
            }
        }
    }
    
    /* Returns the number of exercises the patient performs at the moment*/
    public int getAmountOfExercises(){
        return exercises.size();
    }
    
    /* Returns the exercise from the enum in-parameter if the patient performs
     * it, and null if he or she doesn't. */
    public exercise getExercise(Exercises e) {
        for (exercise em : exercises) {
            if (em.getEnum().equals(e)) { return em; }
        } 
        return null;
    }
    
    /* Returns a formated string of the form dd-mm-yyyy of the patients
     * enrollment date*/
    public String enrollmentDateToString() {
        return StaticHelpMethods.dateAsString(enrollmentDate);
    }
    
}