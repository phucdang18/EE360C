import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;


/**
 */
public class Robber {

    private static final int Item = 0;
    private static final int Buyer = 0;
    private static final int Weight = 1;
    private static final int Time = 1;
    private static final int Value = 2;

    /*
     * This method should return true if the robber can rob all the houses in the neighborhood,
     * which are represented as a graph, and false if he cannot. The function should also print to the console the
     * order in which the robber should rob the houses if he can rob the houses. You do not need to print anything
     * if all the houses cannot be robbed.
     */
    public boolean canRobAllHouses(Graph neighborhood) {
        LinkedList<String> result = new LinkedList<String>();
        ArrayList<String> unlocked = neighborhood.notLocked;
        LinkedList<String> tempList = new LinkedList<String>();
        while(!unlocked.isEmpty()) {
            String temp = unlocked.get(0);
            if(!result.contains(temp))
                result.add(temp);
            unlocked.remove(0);
            tempList = neighborhood.findList(temp);
            if(tempList != null) {
                while (tempList.size() != 1) {
                    String m = tempList.getLast();
                    neighborhood.removeEdge(temp, m);
                    if (!neighborhood.isLocked(m)) {
                        unlocked.add(m);
                    }
                }
            }
        }
        if(neighborhood.edges == 0){
            for(int i = 0; i < result.size() - 1; i ++){
                System.out.print(result.get(i) + ", ");
            }
            System.out.println(result.get(result.size() - 1));
            System.out.println();
            return true;
        }
        else{
            return false;
        }
    }

    /*
     *This method print out the list of items that the robber should
     * steal to maximize the loot
     */
    public void maximizeLoot(String lootList) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<LinkedList<String>> theList = ParseList(lootList);
        double weight = Double.parseDouble(theList.get(0).get(0));
        theList.remove(0);
        sortList(theList);
        for(int i = theList.size() - 1; i >= 0 && weight > 0; i--){
            if(weight > getWeight(theList, i)){
                addItem(result, theList.get(i).get(Item), theList.get(i).get(Weight));
                weight =  weight - getWeight(theList, i);
                DecimalFormat x = new DecimalFormat("#.##");
                weight = Double.valueOf(x.format(weight));

            }
            else{
                addItem(result, theList.get(i).get(Item), Double.toString(weight));
                weight = 0;
            }
        }
        for(int i = 0; i < result.size(); i++){
            System.out.println(result.get(i));
        }
    }

    /**
     * This method print out a list of buyers that the robber should meet to maximize the amount of buyers
     * @param buyerList
     */
    public void scheduleMeetings(String buyerList) {
        String endTime;
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<LinkedList<String>> theList = ParseList(buyerList);
        SortTime(theList);
        System.out.println();
        if(theList.size() != 0) {
            result.add(theList.get(0).get(Buyer));
            endTime = getEndtime(theList, 0);
            for (int i = 0; i < theList.size(); i++) {
                String startTime = getStartTime(theList, i);
                if (!compareTime(endTime, startTime, 15)) {
                    endTime = getEndtime(theList, i);
                    result.add(theList.get(i).get(Buyer));
                }
            }
        }
        for(String s: result){
            System.out.println(s);
        }
    }

    /**
     * This method parse the file into a data structure
     * @param String of file name
     * @return an ArrayList<LinkedList<String>>
     */
    public ArrayList<LinkedList<String>> ParseList(String List){
        ArrayList<LinkedList<String>> result = new ArrayList<LinkedList<String>>();
        try {
            BufferedReader lootList_reader = new BufferedReader(new FileReader(List));
            String line;
            while((line = lootList_reader.readLine()) != null){
                String[] token = line.split(",[ ]");
                LinkedList<String> tempList = new LinkedList<String>(Arrays.asList(token));
                result.add(tempList);
            }
        }catch(FileNotFoundException e){
            System.out.println("Unable to open file " + List);
        }catch(IOException e){
            System.out.println("Error reading file " + List);
        }
        return result;
    }

    /**
     * this method get the price of the item
     * @param list
     * @param index
     * @return the price in double
     */
    public double getPrice(ArrayList<LinkedList<String>> list, int index){
        return Double.parseDouble(list.get(index).get(Value));
    }

    /**
     * this method gets the weight of the item
     * @param list
     * @param index
     * @return the weight in double format
     */
    public double getWeight(ArrayList<LinkedList<String>> list, int index){
        return Double.parseDouble(list.get(index).get(Weight));
    }

    /**
     * this method get the end time of the buyers meeting
     * @param list
     * @param index
     * @return a String specify end time
     */
    public String getEndtime(ArrayList<LinkedList<String>> list, int index){
        String[] token = list.get(index).get(Time).split("-");
        return token[1];
    }

    /**
     * this method get the start time of a buyer meeting
     * @param list
     * @param index
     * @return a String specify start time
     */
    public String getStartTime(ArrayList<LinkedList<String>> list, int index){
        String[] token = list.get(index).get(Time).split("-");
        return token[0];
    }

    /**
     * this method compare 2 time, return true if time1 is after time2 and false otherwise
     * @param time1
     * @param time2
     * @param extra either 0 or 15 minutes added to time 1
     * @return
     */
    public boolean compareTime(String time1, String time2, int extra) {
        int x = 0;
        int hour1, hour2, minute1, minute2;
        String substr1 = time1.substring(0,time1.length() - 2);
        String substr2 = time2.substring(0,time2.length() - 2);
        String[] token1 = substr1.split(":");
        String[] token2 = substr2.split(":");
        hour1 = Integer.parseInt(token1[0]);
        hour2 = Integer.parseInt(token2[0]);
        if(hour1 == 12){
            hour1 = 0;
        }
        if(hour2 == 12){
            hour2 = 0;
        }
        if(time1.contains("pm")){
            hour1 += 12;
        }
        if(time2.contains("pm")){
            hour2 += 12;
        }
        if(token1.length > 1) {
            minute1 = Integer.parseInt(token1[1]) + extra;
            if(minute1 >= 60){
                minute1 = minute1 % 60;
                hour1 = hour1 + 1;
            }
        }
        else{
            minute1 = 0 + extra;
        }
        if(token2.length > 1) {
            minute2 = Integer.parseInt(token2[1]);
        }
        else{
            minute2 = 0;
        }
        if(hour1 > hour2){
            return true;
        }
        else if(hour1 < hour2){
            return false;
        }
        else{
            if(minute1 > minute2){
                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * this method add the items with weight to the result list
     * @param result
     * @param item
     * @param weight
     */
    public void addItem(ArrayList<String> result, String item, String weight){
        result.add(item + " " + weight);
    }

    /**
     * this method sort a list base on the price
     * @param list
     */
    public void sortList(ArrayList<LinkedList<String>> list){
            for(int i = 1; i < list.size(); i++){
                int j = i;
                while( j > 0 && (getPrice(list, j - 1) > getPrice(list, j))){
                    Collections.swap(list, j -1, j);
                    j--;
                }
            }
    }

    /**
     * this method sort the time base on finishing time
     * @param list
     */
    public void SortTime(ArrayList<LinkedList<String>> list){
        for(int i = 1; i < list.size(); i++){
            int j = i;
            while( j > 0 && compareTime(getEndtime(list, j - 1), getEndtime(list, j), 0)){
                Collections.swap(list, j -1, j);
                j--;
            }
        }
    }


}
