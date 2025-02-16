package ub.cse.algo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wrapper class for objects given to students.
 */
public class PreferenceLists implements Serializable{
    
    private HashMap<Integer, ArrayList<Integer>> students;
    private HashMap<Integer, ArrayList<Integer>> hospitals;
    
    public PreferenceLists(HashMap<Integer, ArrayList<Integer>> students,
                           HashMap<Integer, ArrayList<Integer>> hospitals) {
        this.students = students;
        this.hospitals = hospitals;
    }
    
    /**
     * Get the map containing the men's preference lists
     *
     * @return map of men's preference list
     */
    public HashMap<Integer, ArrayList<Integer>> getStudents() {
        return students;
    }
    
    /**
     * Get the map containing the women's preference lists
     *
     * @return map of women's preference list
     */
    public HashMap<Integer, ArrayList<Integer>> getHospitals() {
        return hospitals;
    }
}

