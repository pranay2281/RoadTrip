import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class RoadTrip {


    Graph roads;

    Hashtable<String, String> attractions;
    //Key = attraction name, Value = attraction place
    //To store the attractions parsed from the CSV file

    List<String> AttractionArrayList = new ArrayList<>();
    List<String> CityArrayList = new ArrayList<>();

    public void storeAttractions(String A) throws IOException {
        attractions = new Hashtable<>();
        File file = new File(A);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String [] temp = line.split(",");
            attractions.put(temp[0], temp[1]);
            AttractionArrayList.add(temp[0]);
        }
    }

    public void storeRoads(String R) throws IOException {
        roads= new Graph();
        File file = new File(R);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while((line= br.readLine()) != null){
            String [] temp = line.split(",");
            Vertex source = new Vertex(temp[0]);
            Vertex destination = new Vertex(temp[1]);
            int distance = Integer.parseInt(temp[2]);

            roads.addEdge(source,destination,distance);
            CityArrayList.add(temp[0]);
            CityArrayList.add(temp[1]);
        }
    }

    List<String> route(String starting_city, String ending_city, List<String> attractions){

        List<String> finalPath = new ArrayList<>();// this stores the final path to be taken

        Vertex destination = getVertex(ending_city);

        if (attractions.isEmpty()) {//if there are no attractions,
            //we just want to give the shortest path from from starting city to destination
            roads.shortestPath(getVertex(starting_city), destination);
            finalPath.addAll(roads.path);
            return finalPath;
        }
        //if there are attractions in the list
        else{
            Vertex start = getVertex(starting_city);
            int minDistance = Integer.MAX_VALUE;
            Vertex closestPlace = null;

            while(!attractions.isEmpty()){
                //loops until shortest path to all the attractions has been found
                for(int i = 0; i < attractions.size(); i++){
                    //check closest place from start place
                    Vertex current = getVertex(attractions.get(i));
                    int currentDistance = roads.shortestPath(start,current);
                    roads.resetVisitedVertices();
                    if(currentDistance<minDistance){
                        minDistance=currentDistance;
                        closestPlace = current;
                    }
                }

                roads.shortestPath(start,closestPlace);
                roads.resetVisitedVertices();
                finalPath.addAll(roads.path);
                finalPath.remove(finalPath.size()-1);

                attractions.remove(closestPlace.place);

                start=closestPlace;
                //Make the current closest place the start so that we can find the closest place to this place
                if(!attractions.isEmpty()){
                    closestPlace=null;
                }
                minDistance=Integer.MAX_VALUE;
            }

            roads.resetVisitedVertices();

            roads.shortestPath(closestPlace,destination);
            //to find the shortest path from the last place of interest visited to the destination.
            finalPath.addAll(roads.path);
        }

        return finalPath;

    }

    private Vertex getVertex(String name){
        //its is a private method because it is a helper method
        // it is to get the vertex from thr graph based on the name
        for(Vertex v: roads.vertices){
            if(v.place.equals(name)){
                return v;
            }
        }
        return null;
    }

    public void printPath(List<String> array) {
        System.out.println("The path is: ");
        for (String i : array) {
            System.out.print(i + "  ");
        }
    }

    public boolean checkAttractions(List A) {
        int count=0;
        for(int i=0;i<A.size();i++){
            for(int j=0; j< AttractionArrayList.size();j++){
                if(A.get(i).equals(AttractionArrayList.get(j))){
                    count++;
                }
            }
        }
        if(count==A.size()){
            return true;
        }
        else{
            return false;
        }
    }

    public static void main(String[] args) throws IOException {

        RoadTrip roadTrip= new RoadTrip();
        roadTrip.storeAttractions(args[0]);
        roadTrip.storeRoads(args[1]);
        //roadTrip.roads.printGraph();

        String starting_city;
        String ending_city;
        List<String> attractions = new ArrayList<>();
        String flag;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter starting city: ");
        starting_city = scanner.nextLine();

        System.out.println("Enter ending city: ");
        ending_city = scanner.nextLine();

        System.out.println("Add an attraction (y/n)? ");
        flag = scanner.nextLine();

        while (flag.equals("y")) {
            System.out.println("Add place: ");
            attractions.add(roadTrip.attractions.get(scanner.nextLine()));
            System.out.println("Add an attraction (y/n)? ");
            flag = scanner.nextLine();
        }

        List<String> finalPath = roadTrip.route(starting_city,ending_city,attractions);
        roadTrip.printPath(finalPath);
    }
}
