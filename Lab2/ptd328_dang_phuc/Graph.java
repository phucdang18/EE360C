/**
 */

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;


public class Graph {
    public ArrayList<LinkedList<String>> graph;
    public ArrayList<String> notLocked;
    public int edges;

    /*
     * Creates a graph to represent the neighborhood, where unlocked is the file name for the unlocked houses
     * and keys is the file name for which houses have which keys.
     */
    public Graph(String unlocked, String keys) {
        edges = 0;
        graph = new ArrayList<LinkedList<String>>();
        notLocked = new ArrayList<String>();
        String line = null;
        try{
            BufferedReader unlocked_reader = new BufferedReader(new FileReader(unlocked));
            BufferedReader keys_reader = new BufferedReader(new FileReader(keys));
            while((line = unlocked_reader.readLine()) != null) {
                notLocked.add(line);
            }
            while((line = keys_reader.readLine()) != null){
               String[] token = line.split(":[ ]+|,[ ]+|:");
               LinkedList<String> tempList = new LinkedList<String>(Arrays.asList(token));
               edges = edges + tempList.size() - 1;
               graph.add(tempList);
            }
        }catch(FileNotFoundException e){
            System.out.println("Unable to open file " + unlocked);
        }catch(IOException e){
            System.out.println("Error reading file " + unlocked);
        }

    }

    /*
     * This method should return true if the Graph contains the vertex described by the input String.
     */
    public boolean containsVertex(String node) {
        for(int i = 0; i < graph.size(); i++){
            if(node.equals(graph.get(i).peek()))
                return true;
        }
        return false;
    }

    /*
     * This method should return true if there is a direct edge from the vertex
     * represented by start String and end String.
     */
    public boolean containsEdge(String start, String end) {
        if(containsVertex(start)){
            for(int i = 0; i < graph.size(); i++) {
                if (start.equals(graph.get(i).getFirst())) {
                    if (graph.get(i).indexOf(end) != -1 && !graph.get(i).getFirst().equals(end)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * This method returns true if the house represented by the input String is locked
     * and false is the house has been left unlocked.
     */
    public boolean isLocked(String house) {
        if(notLocked.indexOf(house) != -1){
            return false;
        }
        for(int i  = 0; i < graph.size(); i++){
            String temp = graph.get(i).getFirst();
            if(containsEdge(temp, house)){
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @param String house
     * @return the linked list of that house in the graph (what keys it have)
     */
    public LinkedList<String> findList(String house){
        for(int i = 0; i < graph.size(); i++){
            if(house.equals(graph.get(i).peek()))
                return graph.get(i);
        }
        return null;
    }

    /**
     * Remove an edge from the graph
     * @param start vertex
     * @param end vertex
     */
    public void removeEdge(String start, String end) {
        int index;
        if(containsVertex(start)){
            for(int i = 0; i < graph.size(); i++) {
                if (start.equals(graph.get(i).getFirst())) {
                    index = graph.get(i).indexOf(end);
                    if (index != -1 && !graph.get(i).getFirst().equals(end)) {
                        graph.get(i).remove(index);
                        edges--;
                    }
                }
            }
        }
    }
}
