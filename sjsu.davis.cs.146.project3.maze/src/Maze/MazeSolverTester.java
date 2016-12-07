package Maze;

/**
 * Created by aniquedavla on 12/4/16.
 */
public class MazeSolverTester {
    public static void main(String[] args) {
        Graph grid = new Graph(4);
        //  maze = new Maze(4,4);
        System.out.println("maze +\n");
        System.out.println(grid);
        System.out.println(grid.printBFS());
        System.out.print(grid.printDFS());
//        System.out.print(grid.solveBFS());
//        System.out.print(grid.solveDFS());
    }
}
