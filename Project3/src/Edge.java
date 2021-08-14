public class Edge implements Comparable<Edge> {
    Vertex source;
    Vertex destination;
    int distance;

    public Edge(Vertex source, Vertex destination, int distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public int compareTo(Edge o) {
        if(this.distance>o.distance){//object distance is greater than parameter distance.
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Edge{" +
            "source=" + source +
                    ", destination=" + destination +
                    ", distance=" + distance +
                    '}';
    }
}
