/**
 * Group Members:
 * Adarsh Raghupti       axh190002
 * Akash Akki            apa190001
 */

package project3;

public class Edge {
    int toVertex;
    int weight;

    Edge( int toVertex, int weight){
        this.toVertex = toVertex;
        this.weight = weight;
    }

    public int getToVertex() {
        return toVertex;
    }

    public void setToVertex(int toVertex) {
        this.toVertex = toVertex;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
