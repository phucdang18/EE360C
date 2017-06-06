/*
 * Name: Phuc Dang
 * EID: ptd328
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Program1Ties {
    public boolean isStableMatching(Matching marriage) {
        Integer cJob = 0;        //current job
        Integer nJob = 0;       //compared job
        ArrayList<ArrayList<Integer>> jobPref = marriage.getJobPreference();
        ArrayList<ArrayList<Integer>> workerPref = marriage.getWorkerPreference();
        ArrayList<Integer> matching = marriage.getWorkerMatching();

        if (matching == null)
            return false;
        for (int i = 0; i < marriage.getWorkerCount(); i++) {     //i is worker i i.e worker 0
            cJob = matching.get(i);
            for (int j = 0; j < marriage.getWorkerCount(); j++)   //j is worker j
            {
                nJob = matching.get(j);                                                  // 2 pairs (i,cJob) and (j,nJob)
                if ((workerPref.get(i).get(cJob) - workerPref.get(i).get(nJob)) > 0) {   //check if worker i prefers nJob
                    if (jobPref.get(nJob).get(j) - jobPref.get(nJob).get(i) > 0) {      //check if nJob also prefers i to j
                        return false;
                    }
                }
                if (jobPref.get(cJob).get(i) - jobPref.get(cJob).get(j) > 0) {           //check if cJob pref j
                    if (workerPref.get(j).get(nJob) - workerPref.get(j).get(cJob) > 0) { //check if j prefer cJOb over nJob
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
        Integer noWorkerIndex = 0;
        boolean[] freeJob = new boolean[count];            //index of the array is the job false is free, and true is occupied
        boolean[][] proposed = new boolean[count][count];  // index of outer array is the job, index of inner array is the worker and false is not proposed yet
        int[] rank = new int[count];                       //index if the job, content is the current rank of that job ( 0 to N - 1)
        ArrayList<ArrayList<Integer>> jobPref = solution.getJobPreference();
        ArrayList<ArrayList<Integer>> workerPref = solution.getWorkerPreference();
        ArrayList<Integer> results = solution.getWorkerMatching();
        for (int i = 0; i < count; i++) {                   // initialize the array list result to no pairs
            results.add(i, -1);
        }
        while (count > 0) {

            /*looking for free job*/
            noWorkerIndex = 0;
            while (freeJob[noWorkerIndex] && (noWorkerIndex < marriage.getJobCount())) {
                noWorkerIndex++;
            }

            /*looking for highest ranking worker of that the current job that has not been proposed by the job*/
            for (int i = 0; i < marriage.getWorkerCount() && !freeJob[noWorkerIndex]; i++) {
                theWorker = -1;
                if (jobPref.get(noWorkerIndex).get(i) <= rank[noWorkerIndex]) {
                    if (proposed[noWorkerIndex][i] == false) {
                        theWorker = i;
                        proposed[noWorkerIndex][i] = true;
                    }
                }
                if (i == marriage.getWorkerCount() - 1)    // if i equals the last worker in the list go to next rank
                    rank[noWorkerIndex]++;

                if (theWorker != -1) {
                    if (results.get(theWorker) == -1) {
                        results.set(theWorker, noWorkerIndex);
                        freeJob[noWorkerIndex] = true;
                        count--;
                    } else {
                        if (workerPref.get(theWorker).get(noWorkerIndex) - workerPref.get(theWorker).get(results.get(theWorker)) < 0) {
                            freeJob[results.get(theWorker)] = false;
                            results.set(theWorker, noWorkerIndex);
                            freeJob[noWorkerIndex] = true;
                        }
                    }
                }
            }
        }
        return solution;
    }
}
