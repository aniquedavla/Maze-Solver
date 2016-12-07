package Maze;

import Maze.Vertex;
import java.util.*;
/**
 * Graph class that creates randomized maze and solve via BFS & DFS
 */
public class Graph {
    private Random randomGen; // Random maze
    private Vertex vertexList[]; // Stores each Vertex
    private Vertex adjMatrix[][]; // 2D representation of maze
    private int adjMatrixSize; // Size of the maze

    /**
     * Constructs the Maze
     * @param dimension dimensions of the maze
     */
    public Graph(int dimension) {
        randomGen = new Random();
        vertexList = new Vertex[dimension * dimension];
        adjMatrix = new Vertex[dimension][dimension];
        adjMatrixSize = adjMatrix.length - 1;

        int count = 0;

        // Create the me of the given dimensions
        for (int j = 0; j < dimension; j++) {
            for (int k = 0; k < dimension; k++) { // matrix to 0
                Vertex vertex = new Vertex(j, k);
                adjMatrix[j][k] = vertex;
                vertexList[count] = vertex;
                count++;
            }
        }
        createMaze();
    }

    /**
     * Generates random number between 0 and 1
     * Method provided by professor Potika
     *
     * @return random number
     */
    public double myRandom() {
        return randomGen.nextDouble();
    }

    /**
     * Creates the maze
     * Breaks necessary walls
     * Connects all of the vertices together
     */
    public void createMaze() {
        // Creates stack
        Stack<Vertex> vertexStack = new Stack<Vertex>();
        Vertex currentVertex = adjMatrix[0][0];
        int visitedVertices = 1;
        int totalVertices = vertexList.length;

        while (visitedVertices < totalVertices) {
            List<Vertex> neighborhoodList = findNeighbors(currentVertex);
            Vertex selectedVertex = currentVertex;

            // Continues and goes to the next Vertex
            if (neighborhoodList.size() > 0 ) {
                vertexStack.push(currentVertex);
                int randomValue = (int) (myRandom() * neighborhoodList.size());
                currentVertex = neighborhoodList.get(randomValue);
                breakWalls(currentVertex, selectedVertex);
                currentVertex.adjList.add(selectedVertex);
                selectedVertex.adjList.add(currentVertex);
                visitedVertices++;
            }
            else {
                currentVertex = vertexStack.pop();
            }
        }

        adjMatrix[0][0].UP = false;
        adjMatrix[adjMatrixSize][adjMatrixSize].DOWN = false;
    }

    /**
     * Prints out the maze with ASCII characters
     * @return String representation of maze
     */
    public String printMaze() {
        String maze = "";
        int size = adjMatrix.length;

        for (int i = 0; i < size; i++) {
            maze += (i == 0) ? "+ " : "+-";
        }
        maze += "+\n";

        for (int i = 0; i < size; i++) {
            maze += "|";
            for (int j = 0; j < size - 1; j++){
                maze += (adjMatrix[j][i].step > -1) ? adjMatrix[j][i].step % 10 : " ";
                maze += (adjMatrix[j][i].RIGHT) ?  "|" : " ";
            }
            maze += (adjMatrix[size - 1][i].step > -1) ? adjMatrix[size-1][i].step % 10 + "|\n+" : " |\n+";
            if (i < size - 1) {
                for (int j = 0; j < size; j++) {
                    maze += (adjMatrix[j][i].DOWN) ? "-+" :" +";
                }
                maze += "\n";
            }
        }

        for (int i = 0; i < size; i++) {
            maze += (!adjMatrix[i][size-1].DOWN) ? " ":"-+";
        }

        maze += "+";
        return maze;
    }

    /**
     * Solves the given puzzle
     * @return Maze for the specific search methodm, ranging from 0 to 9
     */
    public String solveMaze() {
        String maze = "";
        int size = adjMatrix.length;

        // Top part of the maze
        for (int i = 0; i < size; i++){
            maze += (i == 0) ? "+#" : "+-";
        }
        maze += "+\n";

        // Prints sides and bottom part of the maze
        for (int i = 0; i < size; i++){
            maze += "|";
            for (int j = 0; j < size - 1; j++){
                if (adjMatrix[j][i].RIGHT) {
                    maze += (adjMatrix[j][i].value.equals("#")) ? "#|" : " |";
                }
                else {
                    if (adjMatrix[j][i].value.equals("#")) {
                        maze += "#";
                        maze += (adjMatrix[j + 1][i].value.equals("#")) ? "#" : " ";
                    }
                    else maze += "  ";
                }
            }

            maze += adjMatrix[size - 1][i].value.equals("#") ? "#|\n+" : " |\n+";
            if (i < size - 1) {
                for (int j = 0; j < size; j++) {
                    if (adjMatrix[j][i].DOWN) maze += "-";
                    else maze += (adjMatrix[j][i].value.equals("#")) ? "#" :" ";
                    maze += "+";
                }
                maze += "\n";
            }
        }

        for (int i = 0; i < size; i++) {
            maze += (!adjMatrix[i][size-1].DOWN) ? "#":"-+";
        }

        maze += "+";
        return maze;
    }

    /**
     * Find all Vertices connected to the givenVertex
     * @param givenVertex
     * @return list of all the neighbors
     */
    public List<Vertex> findNeighbors(Vertex givenVertex) {
        List<Vertex> neighborhoodList = new ArrayList<Vertex>();
        int matrixSize = adjMatrixSize;

        //Checking all Vertices in the Neighborhood and adding them to the list only if they are full.
        if (givenVertex.j < matrixSize && checkFullNeighborhood(adjMatrix[givenVertex.i][givenVertex.j + 1])) {
            neighborhoodList.add(adjMatrix[givenVertex.i][givenVertex.j + 1]);
        }

        if (givenVertex.i < matrixSize && checkFullNeighborhood(adjMatrix[givenVertex.i + 1][givenVertex.j])) {
            neighborhoodList.add((adjMatrix[givenVertex.i + 1][givenVertex.j]));
        }

        if (givenVertex.j > 0 && checkFullNeighborhood(adjMatrix[givenVertex.i][givenVertex.j - 1])) {
            neighborhoodList.add((adjMatrix[givenVertex.i][givenVertex.j - 1]));
        }

        if (givenVertex.i > 0 && checkFullNeighborhood(adjMatrix[givenVertex.i - 1][givenVertex.j])) {
            neighborhoodList.add((adjMatrix[givenVertex.i - 1][givenVertex.j]));
        }

        return neighborhoodList;
    }

    /**
     * Check if the given neighbor is already visited or not
     * @param  givenVertex cell in the maze
     * @return if all the cells are full
     */
    public boolean checkFullNeighborhood(Vertex givenVertex) {
        return givenVertex.UP && givenVertex.DOWN && givenVertex.LEFT && givenVertex.RIGHT;
    }

    /**
     * Breaks down the walls between v1 and v2
     * @param v1 first cell
     * @param v2 second cell
     */
    public void breakWalls(Vertex v1, Vertex v2) {
        int matrixSize = adjMatrixSize;

        if (v1.i < matrixSize && adjMatrix[v1.i + 1][v1.j].equals(v2)) {
            v1.RIGHT = false;
            v2.LEFT = false;
        }

        if (v1.j < matrixSize && adjMatrix[v1.i][v1.j + 1].equals(v2)) {
            v1.DOWN = false;
            v2.UP = false;
        }

        if (v1.i > 0 && adjMatrix[v1.i - 1][v1.j].equals(v2)) {
            v1.LEFT = false;
            v2.RIGHT = false;
        }

        if (v1.j > 0 && adjMatrix[v1.i][v1.j - 1].equals(v2)) {
            v1.UP = false;
            v2.DOWN = false;
        }
    }


    /**
     * Prints the Breadth-first Search maze
     * @return maze
     */
    public String printBFS() {
        // Reset the maze
        reset();
        // Pushes currentVertex to the stack
        Vertex currentVertex = vertexList[0];
        Queue<Vertex> verticesQueue = new LinkedList<Vertex>();
        verticesQueue.add(currentVertex);
        int step = 0;

        // While not at the end:
        while(!verticesQueue.isEmpty() && !currentVertex.equals(vertexList[vertexList.length - 1])) {
            // Remove it, change color to black, and increment step
            currentVertex = verticesQueue.remove();
            currentVertex.color = VertexColor.BLACK;
            currentVertex.step = step;
            step++;
            // if color is white, change to gray, set previous to current
            // Add vertex to the stack
            for (Vertex vertex : currentVertex.adjList){
                if (vertex.color == VertexColor.WHITE){
                    vertex.color = VertexColor.GRAY;
                    vertex.previous = currentVertex;
                    verticesQueue.add(vertex);
                }
            }
        }

        // Adds "#" if currentVertex isn't in the vertexList
        while(currentVertex != vertexList[0]) {
            currentVertex.value = "#";
            currentVertex = currentVertex.previous;
        }
        currentVertex.value = "#";

        // Prints out DFS, maze with numbers, maze with #
        System.out.println("\n\nBFS:");
        System.out.println(printMaze());
        System.out.println();
        return solveMaze();
    }

    /**
     * Prints Depth-first Search maze
     * @return maze
     */
    public String printDFS() {
        // Reset the maze
        reset();

        // Pushes currentVertex to the stack
        Vertex currentVertex = vertexList[0];
        Stack<Vertex> verticesStack = new Stack<>();
        verticesStack.push(currentVertex);
        int step = 0;

        // While stack isn't empty and not last
        while (!verticesStack.isEmpty() && !currentVertex.equals(vertexList[vertexList.length - 1])) {
            // Pop it, change color to black, and increment step
            currentVertex = verticesStack.pop();
            currentVertex.color = VertexColor.BLACK;
            currentVertex.step = step;
            step++;

            // if color is white, change to gray, set previous to current
            // Add v to the stack
            for (Vertex v: currentVertex.adjList) {
                if (v.color == VertexColor.WHITE) {
                    v.color = VertexColor.GRAY;
                    v.previous = currentVertex;
                    verticesStack.add(v);
                }
            }
        }

        // Adds "#" if currentVertex isn't in the vertexList
        while(currentVertex != vertexList[0]) {
            currentVertex.value = "#";
            currentVertex = currentVertex.previous;
        }
        currentVertex.value = "#";

        // Prints out DFS, maze with numbers, maze with #
        System.out.println("\n\nDFS:");
        System.out.println(printMaze());
        System.out.println();
        return solveMaze();
    }

    /**
     * Resets the maze
     * Iterates over the list and resets its color, step, and previous
     */
    public void reset() {
        for (int i = 0; i < vertexList.length; i++) {
            vertexList[i].color = VertexColor.WHITE;
            vertexList[i].step = -1;
            vertexList[i].previous = null;
        }
    }
}