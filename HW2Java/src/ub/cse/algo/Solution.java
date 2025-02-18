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
//		int num_loop = 1;
		while (!unmatched_hospitals.isEmpty()) {
//			System.out.println("~~~~~~~``");
//			System.out.printf("Number loops: %d\n", num_loop);
//			System.out.println(unmatched_hospitals);
//			num_loop++;
			int proposing_hospital = unmatched_hospitals.getFirst();
			boolean unmatched = true;
			int j = 1;
			while (unmatched) {
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
		ArrayList<Integer> slots_list = GetSlotsList(_nHospital, _hospitalList);
		int total_slots = 0;
		for (int slot = 0; slot <slots_list.size(); slot++) {
			total_slots += slots_list.get(slot);
		}
		int num_dummy_hospitals = (_nStudent - total_slots);

		//
//		System.out.println(slots_list);
//		System.out.println(total_slots);
		//

		/**
		 * Add mini hospitals
		 * Clones of existing hospitals with identical preferences
		 */
		int hospital_padding = 0;
		HashMap<Integer, ArrayList<Integer>> mini_hospital_list = new HashMap<>();
		for (int h = 0; h < _nHospital; h++) {
			int num_mini_h = slots_list.get(h);
			for (int w = 0; w < num_mini_h; w++) {
				mini_hospital_list.put(hospital_padding + w +1, _hospitalList.get(h+1));
			}
		hospital_padding += num_mini_h;
		}



		/**
		 * Add dummy hospitals
		 * NOTE: start of dummy == total_slots
		 */
		for (int d = 1; d<=num_dummy_hospitals; d++) {
			mini_hospital_list.put(total_slots + d, _hospitalList.get(_nHospital));			// key for dummy and value are filler.
		}

		//
 		//
//		System.out.printf("Number slots: %d\n", total_slots);
//		System.out.printf("Number hospitals: %d\n", _nHospital);
//		System.out.printf("Number dummy hospitals: %d\n", num_dummy_hospitals);
//		System.out.print("mini_list size: ");
//		System.out.println(mini_hospital_list.size());
//		System.out.println("~~~~~~~~~~~~~~");
		//
//		for (int x = 1; x <= _nStudent; x++) {
//			System.out.printf("%d:   ", x);
//			System.out.println(mini_hospital_list.get(x));
//		}
//
//		System.out.println(mini_hospital_list);
//		System.out.println("~~~~~~~~~~~~~~");
//		System.out.printf("Number students: %d\n", _nStudent);
//		System.out.println(mini_hospital_list.get(1).size());
//		System.out.println("~~~~~~~~~~~~~~");
//		System.out.println("~~~~~~~~~~~~~~");
		//
 		//

		/**
		 * mini-hospital codex
		 * Store a correlation between original hospital numbers and mini-hospital numbers
		 * index: original hospital number -1
		 * value: list of all mini hospitals that align to original hospital
		 */
		ArrayList<ArrayList<Integer>> mini_hospital_codex = new ArrayList<>();
		int codex_padding = 1;

		for (int h = 0; h < slots_list.size(); h++) {
			mini_hospital_codex.add(new ArrayList<>());
			for (int w = 0; w < slots_list.get(h); w++) {
				mini_hospital_codex.get(h).add(codex_padding);
				codex_padding += 1;
			}
		}

		//
//		System.out.println("~~~~~~~~~~~~~~~~~");
//		System.out.println(mini_hospital_codex);
//		System.out.println("~~~~~~~~~~~~~~~~~");
		//

		/**
		 * Make student list into preference of jobs
		 */
		HashMap<Integer, ArrayList<Integer>> padded_student_list = new HashMap<>();
		for (int s = 1; s<= _nStudent; s++) {
			ArrayList<Integer> padded_student_preference = new ArrayList<>();
			for (int h = 0; h < _studentList.get(s).size(); h++) {
				int hospital_num = _studentList.get(s).get(h);
				int num_slots = slots_list.get(hospital_num -1);
				for (int j = 0; j < num_slots; j++) {
					padded_student_preference.add(mini_hospital_codex.get(hospital_num -1).get(j));
				}
			}
			for (int d = 0; d < num_dummy_hospitals; d++){
				padded_student_preference.add(total_slots+d+1);
			}
			padded_student_list.put(s, padded_student_preference);
		}

		/**
		 * Post-Processing
		 * Remove students who are matched to dummy hospitals.
		 * Establish connection between mini-hospital and original
		 */


		//
		ArrayList<Match> stable_match_dummy = GaleShapley(mini_hospital_list.size(), _nStudent, mini_hospital_list, padded_student_list);
//		System.out.printf("stable match size: %d", stable_match_dummy.size());
//		System.out.println(stable_match_dummy);
		ArrayList<Match> stable_match_final = new ArrayList<>();

		for (int match = 0; match < stable_match_dummy.size(); match++) {
//			System.out.println("~~~~~~~~~~~~~");
//			System.out.println(stable_match_dummy.get(match).hospital);
			int matched_hospital = stable_match_dummy.get(match).hospital;
			if (matched_hospital <= total_slots) {
				stable_match_final.add(stable_match_dummy.get(match));
			}

		}

		for (int mini = 0; mini < stable_match_final.size(); mini++) {
			int mini_hospital_num = stable_match_final.get(mini).hospital;

			for (int h = 0; h < mini_hospital_codex.size(); h++) {
				if (mini_hospital_codex.get(h).contains(mini_hospital_num)) {
					stable_match_final.get(mini).hospital = h +1;
				}
			}
		}

		return stable_match_final;

		// 		return GaleShapley(mini_hospital_list.size(), _nStudent, mini_hospital_list, padded_student_list);
		//

//        return new ArrayList<Match>();
	}
}


/**
 * Sources:
 * Algorithm Design K&T
 * 		Chapter 1
 * 		pg. 28 contains algorithm for Gale-Shapley
 *
 * Oracle Java Documentation:
 * 		ArrayList
 * 		HashMap
 *
 */