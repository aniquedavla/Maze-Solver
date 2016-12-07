package Maze;
import static org.junit.Assert.*;
/**
 * Driver
 * \main() method instantiates Maze and MazeSolver types
 *    then solves the maze and outputs the result.
 *
 */
public class MazeDriver {
	@org.junit.Test
	public void maze2x2() {
		System.out.println("\n\n------- 1x1 Graph -------");
		Maze maze2x2 = new Maze(2, 2);
		testDFSBFS(maze2x2);
	}
	@org.junit.Test
	public void maze4x4() {
		System.out.println("\n\n------- 1x1 Graph -------");
		Maze maze4x4 = new Maze(4, 4);
		testDFSBFS(maze4x4);
	}
	@org.junit.Test
	public void maze6x6() {
		System.out.println("\n\n------- 1x1 Graph -------");
		Maze maze6x6 = new Maze(6, 6);
		testDFSBFS(maze6x6);
	}
	@org.junit.Test
	public void maze8x8() {
		System.out.println("\n\n------- 1x1 Graph -------");
		Maze maze8x8 = new Maze(8, 8);
		testDFSBFS(maze8x8);
	}

	Maze maze = new Maze(4, 4);

	public void testDFSBFS(Maze maze) {
		DFS_Solver solver1 = new DFS_Solver(maze);
		solver1.solve();
		System.out.println("===== UNSOLVED MAZE =====");
		maze.printMaze();
		System.out.println("\n");

		System.out.println("===== DFS TRAVERSAL =====");
		System.out.println(solver1.printTraverseMap());
		System.out.println("\n");

		System.out.println("===== SHORTEST PATH =====");
		System.out.println(solver1.printSolution());
		//assertEquals(solver1.printSolution(),solver1.printTraverseMap());

		System.out.println("\n\n");
		System.out.println("==============================================================");
		System.out.println("==============================================================");
		System.out.println("\n\n");

		BFS_Solver solver2 = new BFS_Solver(maze);
		solver2.solve();
		System.out.println("===== UNSOLVED MAZE =====");
		maze.printMaze();
		System.out.println("\n");

		System.out.println("===== BFS TRAVERSAL =====");
		System.out.println(solver2.printTraverseMap());
		System.out.println("\n");

		System.out.println("===== SHORTEST PATH =====");
		System.out.println(solver2.printSolution());
		assertEquals(solver1.printSolution(),solver2.printSolution());
	}
}
