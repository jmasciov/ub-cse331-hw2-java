package ub.cse.algo;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * For use in CSE 331 HW1.
 * This is the class you will be editing and turning in. It will be timed against our implementation
 * NOTE that if you declare this file to be in a package, it will not compile in Autolab
 */

public class Solution {
	private int _nHospital;
	private int _nStudent;

    // The following represent the preference list of hospitals and students.
    // The KEY represents the integer representation of a given hospital or student.
    // The VALUE is a list, from most preferred to least.
    // For hospital, first element of the list is number of available slots
	private HashMap<Integer, ArrayList<Integer>> _hospitalList;
	private HashMap<Integer, ArrayList<Integer>> _studentList;
    
    
    /**
     * The constructor simply sets up the necessary data structures.
     * The grader for the homework will first call this class and pass the necessary variables.
     * There is no need to edit this constructor.
     * @param m Number of hospitals
     * @param n Number of students
     * @param A map linking each hospital with its preference list
     * @param A map linking each student with their preference list
     * @return
     */
	public Solution(int m, int n, HashMap<Integer, ArrayList<Integer>> hospitalList, HashMap<Integer, ArrayList<Integer>> studentList) {
		_nHospital = m;
		_nStudent = n;
		_hospitalList = hospitalList;
		_studentList = studentList;
	}

	/*
	 * My methods and classes
	 */

	/**
	 * total_slots
	 * return an array list of all hospitals and their open slots
	 */
	static ArrayList<Integer> GetSlotsList(int m, HashMap<Integer, ArrayList<Integer>> hospitalList) {
		ArrayList<Integer> slots_list = new ArrayList<Integer>();

		for (int i = 1; i <= m; i++) {
//			System.out.printf("%d: ", i);
//			System.out.println(hospitalList.get(i));
			slots_list.add(hospitalList.get(i).getFirst());
		}
		return slots_list;
	}

	static ArrayList<Match> GaleShapley(int m, int n, HashMap<Integer, ArrayList<Integer>> hospitalList, HashMap<Integer, ArrayList<Integer>> studentList) {
		ArrayList<Match> stable_matching_list = new ArrayList<Match>();
		HashMap<Integer , Match> stable_matching_map = new HashMap<>();
		ArrayList<Integer> unmatched_hospitals = new ArrayList<>();
		ArrayList<Integer> matched_students = new ArrayList<>();

		for (int i = 1; i <= m; i ++) {
			unmatched_hospitals.add(i);
		}
//		System.out.println(unmatched_hospitals);
		while (!unmatched_hospitals.isEmpty()) {
			int proposing_hospital = unmatched_hospitals.getFirst();
			boolean unmatched = true;
			int j = 1;
			while (unmatched && j<n) {
				int proposed_student = hospitalList.get(proposing_hospital).get(j);		// gets most preferred student in descending order
				if (!matched_students.contains(proposed_student)) {						// student is unmatched
					Match new_match = new Match(proposing_hospital, proposed_student);
					stable_matching_map.put(proposed_student, new_match);
					matched_students.add(proposed_student);
					unmatched_hospitals.remove(Integer.valueOf(proposing_hospital));
					j++;
					unmatched = false;
				} else {																// student is matched to hospital h'
					Match existing_match = stable_matching_map.get(proposed_student);
					int existing_hospital_match = existing_match.hospital;
					int existing_hospital_rank = studentList.get(proposed_student).indexOf(existing_hospital_match);
					int proposing_hospital_rank = studentList.get(proposed_student).indexOf(proposing_hospital);
					if (existing_hospital_rank < proposing_hospital_rank) {				// student prefers current match over h'
						j++;
					} else {
						stable_matching_map.remove(proposed_student);
						Match new_match = new Match(proposing_hospital, proposed_student);
						stable_matching_map.put(proposed_student, new_match);
						unmatched_hospitals.add(existing_hospital_match);
						unmatched_hospitals.remove(Integer.valueOf(proposing_hospital));
						unmatched = false;
					}
				}
			}
//			System.out.println(stable_matching_map);
		}
		for (int k = 1; k <= stable_matching_map.size(); k++) {
			stable_matching_list.add(stable_matching_map.get(k));
		}
		return stable_matching_list;
	}


    /**
     * This method must be filled in by you. You may add other methods and subclasses as you see fit,
     * but they must remain within the HW1_Student_Solution class.
     * @return Your stable matches
     */
	public ArrayList<Match> getMatches() {


//		ArrayList<Integer> slots_list = GetSlotsList(_nHospital, _hospitalList);
//		System.out.println(slots_list);



        // Returns an empty ArrayList for now
		ArrayList<Match> stable_match = GaleShapley(_nHospital, _nStudent, _hospitalList, _studentList);
		return stable_match;


//        return new ArrayList<Match>();
	}
}
