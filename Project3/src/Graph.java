import java.util.*;

public class Graph {

    Hashtable<String, Vertex> verticesNames = new Hashtable<>();
    //key will have vertex name and value will be the vertex
    //key=vertex name and value=vertex.
    //helps to check if vertex is there in graph or not

    Set<Vertex> vertices;// stores all vertices in the graph.

    List<String> path = new ArrayList<>();// make it function level
    //List to record the shortest path between two vertices. Updated by the shortestPath method.


    public Graph(){
        vertices = new HashSet<>();
    }

    // To add unattached vertices (but have not used because there are none in the given data)
    public void addVertex(Vertex... n) {
        vertices.addAll(Arrays.asList(n));
    }


    public void addEdge(Vertex source, Vertex destination, int distance){
        //first we will check if source and destination are already in graph or not. If not we add them.

        if (!verticesNames.containsKey(source.place)){
            verticesNames.put(source.place,source);
            vertices.add(source);
        }
        if(!verticesNames.containsKey(destination.place)){
            verticesNames.put(destination.place,destination);
            vertices.add(destination);
        }
        //add edge, we will use helper function
        addEdgeHelper(verticesNames.get(source.place),verticesNames.get(destination.place),distance);
        addEdgeHelper(verticesNames.get(destination.place),verticesNames.get(source.place),distance);

    }


    private void addEdgeHelper(Vertex source, Vertex destination, int distance){
        //this helper function adds edge and makes sure we don't have duplicate edges.
        //loop through to check if that edge exists
        for(Edge e : source.edges){
            if(e.source==source && e.destination==destination){
                e.distance=distance;
                return;
            }
        }
        //if it doesn't then add a new edge.
        source.edges.add(new Edge(source,destination,distance));
    }

    public boolean hasEdge(Vertex source, Vertex destination){
        //checks if two sources have an edge between them
        LinkedList<Edge> sourceEdge = source.edges;
        for(Edge e : sourceEdge){
            if(e.destination==destination){
                return true;
            }
        }
        return false;
    }

    public void resetVisitedVertices() {
        // this method is used to make all the vertices unvisited(or false) to use the algorithm another time.
        for (Vertex vertex: vertices) {
            vertex.makeVisitFalse();
        }
    }

    public int shortestPath(Vertex source, Vertex destination){
        // use Djikstra's Algorithm
        path.clear();
        //Clear the path list every time we run this method ensures the new path to be updated properly

        HashMap<Vertex, Vertex> reachedAt = new HashMap<>();
        // To keep track of the previous vertex from which we came to the current vertex
        // Need hashmap to keep track from previous vertex to current vertex.

        reachedAt.put(source, null);



        //we use hashmap as we want to set source vertex to next as null as it has nowhere to go to now.
        //this goes from null to source

        HashMap<Vertex, Integer> minimumCostMap = new HashMap<>();
        //To keep track of the least cost we have found until now for each vertex.

        for(Vertex v : vertices){
            // we want the cost to reach the source to be 0 as there is no cost involved at starting point.
            //we want the cost of the rest to be the maximum int value.
            if(v==source){
                minimumCostMap.put(source,0);
            }
            else{
                minimumCostMap.put(v,Integer.MAX_VALUE);
            }
        }

        for (Edge e : source.edges) { // To go through all vertices from the source vertex
            minimumCostMap.put(e.destination, e.distance);
            reachedAt.put(e.destination, source);

        }


        source.makeVisitTrue();
        //Marking the source to be visited

        while(true){
            // Looping until there is at least one unvisited vertex
            // that can be reached from any of the vertices
            Vertex currentVertex = visitedIsFalseWithMinimumCost(minimumCostMap);

            if (currentVertex == null) {//no path between source and destination
                System.out.println("No path between " + source.place + " and " + destination.place);
                return -1;
            }

            if(currentVertex==destination){
                //If the unvisited vertex with the least cost is the destination
                // then we can stop the algorithm and update the path list.
                Vertex current = destination;
                path.add(destination.place);
                while(true){
                    Vertex previous = reachedAt.get(current);
                    if(previous==null){
                        break;
                    }
                    path.add(0,previous.place);
                    current=previous;

                }
                return minimumCostMap.get(destination);
            }
            currentVertex.makeVisitTrue();// make that vertex true

            //To check all the unvisited vertices that the currentVertex has an edge to whether
            // the least cost passing through the current vertex is less than what the other vertices had before

            for(Edge e: currentVertex.edges){
                if(!e.destination.checkVisitStatus()){
                    if(minimumCostMap.get(currentVertex)+e.distance < minimumCostMap.get(e.destination)){
                        minimumCostMap.put(e.destination,minimumCostMap.get(currentVertex)+e.distance);
                        reachedAt.put(e.destination,currentVertex);
                        //reachedAt.put(currentVertex,e.destination);
                    }
                }
            }
        }


    }
    //the closest vertex that we have not visited before.
    private Vertex visitedIsFalseWithMinimumCost(HashMap<Vertex,Integer> minimumCostMap){
        int minimumCost = Integer.MAX_VALUE;
        Vertex minimumCostVertex = null;
        for(Vertex v: vertices){
            if(!v.checkVisitStatus()){// if visit is false;
                int currentCost= minimumCostMap.get(v);
                if(currentCost!=Integer.MAX_VALUE){
                    if(currentCost<minimumCost){
                        minimumCost=currentCost;
                        minimumCostVertex=v;
                    }
                }
            }
        }
        return minimumCostVertex;
    }

    public void printGraph() {
        //Method to print the Graph. Used while debugging.
        for (Vertex v : vertices) {
            LinkedList<Edge> tempEdges = v.edges;
            if (tempEdges.isEmpty()) {
                System.out.println("Vertex - " + v.place + " has no edges");
            }
            else {
                System.out.println("Vertex - " + v.place + " has edges to ");
            }
            for (Edge e : tempEdges) {
                System.out.println(e.destination.place + "  ");
            }
            System.out.println("----------------------------------------------------------");
        }
    }



}

