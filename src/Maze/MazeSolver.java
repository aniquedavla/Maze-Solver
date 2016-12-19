package Maze;

import Maze.Maze.MazeCell;
import Maze.Maze.Path;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Abstract Class MazeSolver implements a general maze solver. It 
 *    contains a pointer to the maze to be solved and a two-
 *    dimensional array (of the same size) with attributes 
 *    necessary to solving the maze that are not included in the
 *    maze itself, implemented as an abstract inner class,
 *    SolutionCell.
 *    
 * MazeSolver contains methods for printing the solved maze in
 *    two ways: one shows the traversal order of cells in the maze,
 *    the other shows the optimum path from start to finish. It
 *    also contains methods for confirming the existence of and
 *    retrieving a given cell's neighbors based on criteria
 *    
 * MazeSolver leaves unimplemented the abstract solver() method.
 *     Derived classes must implement this using any suitable
 *     algorithm.
 *     
 * MazeSolver contains a boolean (solved) that is false until a
 *    solution traversal visits the cell that is the end of the
 *    maze. It's used to stop processing once the exit is found.	
 *
 */
abstract class MazeSolver {
///// Constructors /////////////////////////////////////////////////
	/**
	 * Default constructor. Connects MazeSolver to a specific Maze
	 * 
	 * @param inputMaze		the Maze this instance of MazeSolver
	 * 							will solve.
	 */
	protected MazeSolver(Maze inputMaze)
	{
		maze = inputMaze;
		solved = false;
		time = 0;
		solution = new SolutionCell[maze.rowCount][maze.colCount];
		
		for(int row = 0; row < maze.rowCount; row++)
		{
			for(int col = 0; col < maze.colCount; col++)
			{
				// default SolutionCell sets visited = false
				solution[row][col] = new SolutionCell(maze.cells[row][col]);
			}
		}
	} // Default MazeSolver constructor

///// Instance Methods /////////////////////////////////////////////
	/**
	 * Utility Method: derived classes must implement their
	 * solution algorithm in the solve() method
	 */
	public abstract void solve();
	
	/**
	 * Output method: Prints a text representation of the maze
	 *    including the shortest-path from start to end.
	 */
	public String printSolution()
	{
		// Create a stream to hold the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		// IMPORTANT: Save the old System.out!
		PrintStream old = System.out;
		// Tell Java to use your special stream
		System.setOut(ps);
		System.out.println("start");
		for(int row = 0; row < maze.rowCount; row++)
		{
			String northWalls = "+";
			String eastWestWalls = "|";
			String southWalls = "+";
			for( int col = 0; col < maze.colCount; col++)
			{
				SolutionCell sc = solution[row][col];
				if(row == 0) northWalls += sc.cell.hasWall(Path.NORTH) ? "���+" : "   +";
				southWalls += sc.cell.hasWall(Path.SOUTH) ? "���+" : "   +";
				eastWestWalls += sc.inOptimalPath ? " # " : "   ";
				eastWestWalls += sc.cell.hasWall(Path.EAST) ? "|" :  " ";
			}
			if(row == 0) System.out.println(northWalls);
			System.out.println(eastWestWalls);
			System.out.println(southWalls);
		}
		
		// for spacing out the "end" label
		String buffer = "";
		for(int i = 0; i < (maze.colCount)*4 - 3; i++)
		{
			buffer += " ";
		}
		System.out.println(buffer + "end");
		// Put things back
		System.out.flush();
		System.setOut(old);
		return baos.toString();
	} // printSolution()

	/**
	 * Output method: Prints a text representation of the maze
	 *    including the order that the solution algorithm
	 *    traversed the maze
	 */
	public String printTraverseMap()
	{
		// Create a stream to hold the output
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		// IMPORTANT: Save the old System.out!
		PrintStream old = System.out;
		// Tell Java to use your special stream
		System.setOut(ps);

		System.out.println("start");
		for(int row = 0; row < maze.rowCount; row++)
		{
			String northWalls = "+";
			String eastWestWalls = "|";
			String southWalls = "+";
			for( int col = 0; col < maze.colCount; col++)
			{
				SolutionCell sc = solution[row][col];
				if(row == 0) northWalls += sc.cell.hasWall(Path.NORTH) ? "���+" : "   +";
				southWalls += sc.cell.hasWall(Path.SOUTH) ? "���+" : "   +";
				eastWestWalls += " ";
				eastWestWalls += sc.visited ? (sc.searchOrdinal % 10) + " " : "  ";
				eastWestWalls += sc.cell.hasWall(Path.EAST) ? "|" :  " ";
			}
			if(row == 0) System.out.println(northWalls);
			System.out.println(eastWestWalls);
			System.out.println(southWalls);
		}
		
		// for spacing out the "end" label
		String buffer = "";
		for(int i = 0; i < (maze.colCount)*4 - 3; i++)
		{
			buffer += " ";
		}
		System.out.println(buffer + "end");
		// Print some output: goes to your special stream
		//System.out.println("Foofoofoo!");
		// Put things back
		System.out.flush();
		System.setOut(old);
//		// Show what happened
//		System.out.println("Here: " + baos.toString());
	return baos.toString();
	} // printTraverseMap()

///// Instance Fields //////////////////////////////////////////////
	Maze maze;
	SolutionCell[][] solution;
	boolean solved;
	int time;
	
///// Inner Classes ////////////////////////////////////////////////
	
	/**
	 * Abstract class implements a general cell in a solution
	 *    array. SolutionCell contains:
	 *    			- a pointer to the corresponding cell in the
	 *    				 the maze (from which the address of either
	 *    				 (MazeCell or SolutionCell can be retrieved)
	 *    			- a boolean indicating whether the cell has been visited
	 *    			- a boolean indicating whether it is in a
	 *    			     shortest-path from start to end
	 *    			- an int indicating when the cell was traversed
	 *    
	 * @author Iain Davis
	 *
	 */
	protected class SolutionCell
	{
		///// Constructors /////////////////////////////////////////////
		/**
		 * Default SolutionCell constructor
		 * 
		 * @param inputCell
		 */
		protected SolutionCell(MazeCell inputCell)
		{
			cell = inputCell;
			visited = false;
			inOptimalPath = false;
			searchOrdinal = 0;
			parent = null;
		} // default SolutionCell constructor
		
		///// Instance Methods /////////////////////////////////////
		/**
		 * Method returns an unvisited neighbor in the indicated
		 *    direction, if one exists
		 *    
		 * @param direction		the direction to check for a 
		 * 							suitable neighbor
		 * @return			an unvisited neighbor, if one exists
		 * 					null, otherwise
		 */
		protected SolutionCell getUnvisitedNeighbor(Path direction)
		{
			if(this.hasUnvisitedNeighbor(direction))
			{
				return getNeighbor(direction);
			}
			else return null;
		} // getUnvisitedNeighbor()
		
		/**
		 * Utility method: checks the compound condition that a
		 *    neighbor exists in the indicated direction, 
		 *    is logically adjacent (a connecting path exists),
		 *    and is unvisited (i.e. has attribute visited == false).
		 *    
		 * @param direction		the direction in which to check
		 * @return				true if an appropriate neighbor exists
		 * 						false otherwise
		 */
		private boolean hasUnvisitedNeighbor(Path direction)
		{
			if(this.cell.hasNeighbor(direction) && this.cell.hasPath(direction))
			{
				int row = cell.getNeighbor(direction).row;
				int col = cell.getNeighbor(direction).col;
				SolutionCell neighbor = solution[row][col];
				return !neighbor.visited;
			} 
			else return false;
		} // hasUnvisitedNeighbor()
		
		/**
		 * Utility method: retrieves a neighboring SolutionCell by
		 * first retrieving the neighbor of the corresponding MazeCell
		 * and extracting its row and column indices.
		 * 
		 * @param direction
		 * @return the neighboring SolutionCell
		 */
		private SolutionCell getNeighbor(Path direction)
		{
			Maze.MazeCell mazeNeighbor = this.cell.getNeighbor(direction);
			int row = mazeNeighbor.row;
			int col = mazeNeighbor.col;
			
			return solution[row][col];
		} // getNeighbor();
		
		final MazeCell cell; 	// link to the related cell in the Maze
		SolutionCell parent; 		// link to the parent cell in the search tree
		boolean visited;
		boolean inOptimalPath;		// include in shortest-path solution?
		int searchOrdinal;			// this cell's place in the discovery sequence
	} // class SolutionCell
} // class MazeSolver
