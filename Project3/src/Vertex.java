import java.util.LinkedList;

public class Vertex {
    String place;
    private boolean visited;
    LinkedList<Edge> edges;

    public Vertex(String place) {
        this.place = place;
        visited = false;
        edges = new LinkedList<>();
    }
    public boolean checkVisitStatus(){
        return visited;
    }
    public void makeVisitTrue(){
        visited=true;
    }
    public void makeVisitFalse(){
        visited = false;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "place='" + place + '\'' +
                '}';
    }
}
