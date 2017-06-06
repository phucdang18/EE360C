/*
 * Name: Phuc Dang
 * EID: ptd328
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DriverTies {
    public static String filename;

    public static void main(String[] args) throws Exception {
        parseArgs(args);
        Matching problem = parseMatchingProblem(filename);
        testRun(problem);
    }

    private static void usage() {
        System.err.println("usage: <filename>");
        System.exit(1);
    }

    public static void parseArgs(String[] args) {
        if (args.length == 0) {
            usage();
        }
        filename = args[0];
    }

    public static Matching parseMatchingProblem(String inputFile)
            throws Exception {
        int m = 0;
        int n = 0;
        ArrayList<ArrayList<Integer>> jobPrefs, workerPrefs;
        jobPrefs = new ArrayList<>();
        workerPrefs = new ArrayList<>();

        Scanner sc = new Scanner(new File(inputFile));
        String[] inputSizes = sc.nextLine().split(" ");

        m = Integer.parseInt(inputSizes[0]);
        n = Integer.parseInt(inputSizes[1]);

        ArrayList<Boolean> jobFulltime = new ArrayList<>();
        ArrayList<Boolean> workerHardworking = new ArrayList<>();
        readPreferenceLists(sc, m, jobPrefs, jobFulltime);
        readPreferenceLists(sc, n, workerPrefs, workerHardworking);



        Matching problem = new Matching(m, n, jobPrefs, workerPrefs, jobFulltime, workerHardworking);

        return problem;
    }

    private static void readPreferenceLists(Scanner sc, int m, ArrayList<ArrayList<Integer>> preferenceLists,
                                            ArrayList<Boolean> status) {
        for (int i = 0; i < m; i++) {
            String line = sc.nextLine();
            String[] preferences = line.split(" ");
            String s = "";
            ArrayList<Integer> preferenceList = new ArrayList<Integer>(0);

            for (Integer j = 0; j < preferences.length; j++) {
                preferenceList.add(Integer.parseInt(preferences[j]));
            }

            // The first number of the preference list is the status (hardworking/full-time)
            status.add(preferenceList.remove(0) == 1);

            preferenceLists.add(preferenceList);
        }
    }

    public static void testRun(Matching problem) {
        Program1Ties program = new Program1Ties();   //The only thing that changed from Driver.java
        boolean isStable;
        Matching GSMatching = program.stableHiringGaleShapley(problem);
        System.out.println(GSMatching);
        isStable = program.isStableMatching(GSMatching);
        System.out.printf("%s: stable? %s\n", "Gale-Shapley", isStable);
        System.out.println();
    }
}
