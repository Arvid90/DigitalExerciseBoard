/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.StaticHelpMethods;
import java.util.Calendar;

/**
 *
 * @author joakimstoor
 */

/* Defines a dayplan with a number of sets and reps to be done for a specific 
 * day. */
public class DayPlan implements java.io.Serializable {

    private int setsLeft;
    private int sets;
    private int reps;
    private Calendar date;

    /* Creates a DayPlan for the specific Calendar Date with a number of sets 
     * and repititons. The number of sets performed is set to 0*/
    public DayPlan(int goal, int reps, Calendar date) {
        this.sets = goal;
        this.reps = reps;
        this.setsLeft = 0;
        this.date = date;
    }

    /* Returns the number of sets of the day*/
    public int getSets() {
        return sets;
    }

    /* Sets the number of repititon per set of the day*/
    public void setReps(int reps) {
        this.reps = reps;
    }

    /* Returns the number of repititons per set of the day*/
    public int getReps() {
        return reps;
    }

    /* Sets the number of sets of the day if the number of sets already
     * performed are equal or less the number of sets that are trying to be set*/
    public void setSets(int sets) {
        if (sets >= setsLeft) {
            this.sets = sets;
        }
    }

    /* Returns a formatted string of the Date Calendar of the form dd-mm-yyyy*/
    public String getDateAsString() {
        return StaticHelpMethods.dateAsString(date);
    }

    /* Increments the number of sets left by 1 if the sets left are less than the 
     * number that should be performed*/
    public void increment() {
        if (setsLeft < sets) {
            setsLeft++;
        }
    }

    /* Decrements the number of sets left by 1 if the sets left are more than 0*/
    public void decrement() {
        if (setsLeft > 0) {
            setsLeft--;
        }
    }

    /* Returns the amount of sets performed*/
    public int getProgress() {
        return setsLeft;
    }

    /* Sets the progress if it's in the interval [0, sets]*/
    public void setProgress(int i) {
        if ((i >= 0) && (i <= sets)) {
            setsLeft = i;
        }
    }
    
    /* Returns the Calendar Date*/
    public Calendar getDate() {
        return date;
    }
}
