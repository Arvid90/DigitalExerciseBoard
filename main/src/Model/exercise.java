

package Model;

import View.StaticHelpMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/* Defines an abstraction of an exercise with a name and a list of dayfields*/
public class exercise implements java.io.Serializable {

    private Exercises name;
    private ArrayList<DayPlan> dayPlans;

    /* Creates an exercise of the specific enum parameter type and an empty list
     * of dayplans*/
    public exercise(Exercises name) {
        this.name = name;
        dayPlans = new ArrayList();
    }

    /* Adds a dayPlan with a number of sets (goal) and a number of reps at a
     * certain calendar date if no DayPlan has been added on that day already*/
    public void addDayPlan(int goal, int reps, Calendar cal) {
        if (!doesDayPlanExist(cal)) {
            dayPlans.add(new DayPlan(goal, reps, cal));
        }
    }
    
    /* Returns an ArrayList of the DayPlans for the specific exercise*/
    public ArrayList<DayPlan> getDayPlans() {
        return dayPlans;
    }


    /* Sets the number of sets (goal) and number of reps fo a specific 
     * calendar date if it's already been added in the DayPlan ArrayList*/
    public void fixDayPlan(int goal, int reps, Calendar cal) {
        for(DayPlan dp: dayPlans) {
            if (StaticHelpMethods.compareDates(cal,dp.getDate())==0) {
                dp.setSets(goal); dp.setReps(reps); break;
            }
        }
    }
    
    /* Sets the progress of today if the date calendar is in the DayPlan ArrayList*/
    public void setProgressOfToday(int goal) {
        for(DayPlan dp: dayPlans) {
            if (StaticHelpMethods.compareDates(new GregorianCalendar(),dp.getDate())==0) {
                dp.setProgress(goal); break;
            }
        }
    }
    
    /* Returns the progress for the the exercise of today if the date calendar is 
     * in the DayPlan ArrayList, 0 if not*/
    public int getProgressOfDayPlan(Calendar cal) {
        for (DayPlan dp: dayPlans) {
            if(StaticHelpMethods.compareDates(cal,dp.getDate())==0) {
                return dp.getProgress();
            }
        }
        return 0;
    }


    /* Returns the name of the exercise as a String*/
    public String getName() {
        return name.toString();
    }
    
    /* Returns the enumeration type of the exercise*/
    public Exercises getEnum() {
        return name;
    }
    
    /* Returns true if the Calendar Date exists in the DayPlan ArrayList, false 
     * if it doesn't*/
    public boolean doesDayPlanExist(Calendar date) {
        for(DayPlan dp: dayPlans) {
            if (dp.getDateAsString().equals(StaticHelpMethods
                    .dateAsString(date))) { return true;}
        }
        return false;
    }


}