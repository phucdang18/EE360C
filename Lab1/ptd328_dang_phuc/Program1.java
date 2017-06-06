/*
 * Name: Phuc Dang
 * EID: ptd328
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
        Integer cJob = 0;        //current job
        Integer nJob = 0;       //compared job
        ArrayList<ArrayList<Integer>> jobPref = marriage.getJobPreference();
        ArrayList<ArrayList<Integer>> workerPref = marriage.getWorkerPreference();
        ArrayList<Integer> matching = marriage.getWorkerMatching();

        if(matching == null)
            return false;
        for(int i = 0; i < marriage.getWorkerCount(); i++){     //i is worker i i.e worker 0
            cJob = matching.get(i);
            for(int j = 0; j < marriage.getWorkerCount(); j++)   //j is worker j
            {
                nJob = matching.get(j);                                                        // 2 pairs (i,cJob) and (j,nJob)
                if((workerPref.get(i).indexOf(cJob) - workerPref.get(i).indexOf(nJob)) > 0){  //check if worker i prefers nJob
                    if(jobPref.get(nJob).indexOf(j) - jobPref.get(nJob).indexOf(i) > 0){      //check if nJob also prefers i to j
                        return false;
                    }
                }
                if(jobPref.get(cJob).indexOf(i) - jobPref.get(cJob).indexOf(j) > 0){           //check if cJob pref j
                    if(workerPref.get(j).indexOf(nJob) - workerPref.get(j).indexOf(cJob) > 0){ //check if j prefer cJOb over nJob
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableHiringGaleShapley(Matching marriage) {
        ArrayList<Integer> matching = new ArrayList<Integer>(marriage.getWorkerCount());
        Matching solution = new Matching(marriage, matching);
        int count = solution.getWorkerCount();             // number of free job
        Integer theWorker = 0;
        Integer noWorkerIndex;
        boolean[] freeJob = new boolean[count];            //index of the array is the job
        for (int i = 0; i < count; i++) {                  //false is free, and true is occupied
            freeJob[i] = false;
        }
        int[] upToThisWorker = new int[count];
        for(int i = 0; i < count; i++){                     //index of this array is the job
            upToThisWorker[i] = 0;                          //values keeping track of which worker the job has "proposed" to
        }
        ArrayList<ArrayList<Integer>> jobPref = solution.getJobPreference();
        ArrayList<ArrayList<Integer>> workerPref = solution.getWorkerPreference();
        ArrayList<Integer> results = solution.getWorkerMatching();
        for(int i = 0; i < count; i++){
            results.add(i, -1);
        }
        while (count > 0) {
            noWorkerIndex = 0;

            /*looking for free job*/
            while (freeJob[noWorkerIndex] && (noWorkerIndex < marriage.getJobCount())) {
                noWorkerIndex++;
            }

            /*getting next best worker on the preference list that has not been proposed by the current job*/
            for (int i = upToThisWorker[noWorkerIndex]; i < solution.getJobCount() && !freeJob[noWorkerIndex] ; i++) {
                theWorker = jobPref.get(noWorkerIndex).get(i);
                 if (results.get(theWorker) == -1) {
                    results.set(theWorker, noWorkerIndex);
                    freeJob[noWorkerIndex] = true;
                    count--;
                } else {
                    if (workerPref.get(theWorker).indexOf(noWorkerIndex) - workerPref.get(theWorker).indexOf(results.get(theWorker)) < 0) {
                        freeJob[results.get(theWorker)] = false;
                        results.set(theWorker, noWorkerIndex);
                        freeJob[noWorkerIndex] = true;
                    }
                }
                upToThisWorker[noWorkerIndex]++;
            }
        }
        return solution;
    }
}
