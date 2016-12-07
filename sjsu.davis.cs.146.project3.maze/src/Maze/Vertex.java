package Maze;

/**
 * Created by aniquedavla on 12/5/16.
 */

import java.util.LinkedList;

// Constant Colors
enum VertexColor {
    WHITE, GRAY, BLACK;
}

/**
 * Vertex class
 */
public class Vertex {
    // Directions for the walls for each vertex. All Walls are set to true by default
    boolean UP = true;
    boolean DOWN = true;
    boolean LEFT = true;
    boolean RIGHT = true;
    // default color for vertex is White
    VertexColor color = VertexColor.WHITE;
    LinkedList<Vertex> adjList;
    String value; //value of the vertex (ex: "#" or " ")
    int i; // row position of the Vertex
    int j; // column position of the Vertex
    Vertex previous; // previous vertex
    int step; // Step in the sequence

    /**
     * Contructs the vertex
     * @param row
     * @param column
     */
    public Vertex(int row, int column) {
        this.i = row;
        this.j = column;
        this.value = "";
        adjList = new LinkedList<>();
        this.previous = null;
        this.step = -1;
    }
} // end class Vertex
