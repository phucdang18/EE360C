import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Name: Phuc Dang
 * EID: ptd328
 */

public class Program3 {

    public ActivityResult selectActivity(ActivityProblem activityProblem){
        String[] activitiesList = activityProblem.getActivities();
        int[] funLevel = activityProblem.getFunLevels();
        int[] riskLevel = activityProblem.getRiskLevels();
        int max = activityProblem.riskBudget;
        HashSet<String> resultList = new HashSet<String>();
        int result = knapSack(activitiesList, funLevel, riskLevel, max, resultList);
        return new ActivityResult(result, resultList);
    }

    public SchedulingResult selectScheduling(SchedulingProblem schedulingProblem){
        int[] mCost = schedulingProblem.getMauiCosts();
        int[] oCost = schedulingProblem.getOahuCosts();
        int transferCost = schedulingProblem.getTransferCost();
        boolean[] result = findSchedule(mCost, oCost, transferCost);
        return new SchedulingResult(result);
    }

    public boolean[] findSchedule(int[] costM, int[] costO, int fee){
        /*boolean[] stM = new boolean[costM.length];
        boolean[] stO = new boolean[costM.length];*/
        boolean[] result = new boolean[costM.length];
        int[] M =  new int[costM.length + 1];
        int[] O = new int[costM.length + 1];
        M[0] = 0;
        M[1] = costM[0];
        O[0] = 0;
        O[1] = costO[0];
        /*stM[costM.length - 1] = true;
        stO[costM.length - 1] = false;*/
        for(int i = 2; i < costM.length + 1; i++){
            //M[i] = costM[i - 1] + findMin(M[i - 1], fee + O[i - 1], stM, i, true);
            //O[i] = costO[i - 1] + findMin(O[i - 1], fee + M[i - 1], stO, i, false);
            M[i] = costM[i - 1] + findMin(M[i - 1], fee + O[i - 1]);
            O[i] = costO[i - 1] + findMin(O[i - 1], fee + M[i - 1]);
        }
        if(M[costM.length] < O[costM.length]){
           result[costM.length - 1] = true;
        }
        else{
            result[costM.length - 1] = false;
        }
        if(costM.length > 2) {
            for (int i = costM.length - 2; i >= 0; i--) {
                if (result[i + 1]) {
                    if (M[i + 2] == M[i + 1] + costM[i + 1]) {
                        result[i] = true;
                    } else {
                        result[i] = false;
                    }
                } else {
                    if (O[i + 2] == O[i + 1] + costO[i + 1]) {
                        result[i] = false;
                    } else {
                        result[i] = true;
                    }
                }
            }
        }
        return result;
    }

    public int findMin(int a, int b){
        if (a <= b) {
            return a;
        } else {
            return b;
        }
    }

    /*public int findMin(int a, int b, boolean[] arr, int index, boolean x){
        if (a <= b) {
            arr[index - 2] = x;
            return a;
        } else {
            arr[index - 2] = !x;
            return b;
        }
    }*/


    public int knapSack(String[] list, int[] fun, int[] risk, int max, Set<String> result){
        int[][] dynamic = new int[fun.length + 1][max + 1];
        String[][] nameList = new String[fun.length + 1][max + 1];
        for( int i = 0; i < max + 1; i++){
            dynamic[0][i] = 0;
            nameList[0][i] = "";
        }
        for(int i = 1; i < fun.length + 1; i++){
            for(int j = 0; j < max + 1; j++){
                if(risk[i - 1] > j){
                    dynamic[i][j] = dynamic[i - 1][j];
                    nameList[i][j] = nameList[i - 1][j];
                }
                else{
                    //dynamic[i][j] = maxValue(dynamic[i - 1][j], fun[i - 1] + dynamic[i - 1][j - risk[i - 1]]);
                    if(dynamic[i - 1][j] < fun[i - 1] + dynamic[i - 1][j - risk[i - 1]]){
                        dynamic[i][j] = fun[i - 1] + dynamic[i - 1][j - risk[i - 1]];
                        nameList[i][j] = nameList[i - 1][j - risk[i - 1]] + "|" + list[i - 1];
                    }
                    else{
                        dynamic[i][j] = dynamic[i - 1][j];
                        nameList[i][j] = nameList[i - 1][j];
                    }
                }
            }
        }
        findSet(nameList[fun.length][max], result);
        return dynamic[fun.length][max];
    }

    public void findSet(String s, Set<String> set){
        if(s.length() > 0) {
            String[] result = s.split("\\|");
            for (int i = 0; i < result.length; i++) {
                if(result[i].length() > 0)
                    set.add(result[i]);
            }
        }
    }
}
