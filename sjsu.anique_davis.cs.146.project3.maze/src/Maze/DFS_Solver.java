package Maze;
import Maze.Maze.Path;

/**
 * DFS_Solver inherits from the general MazeSolver class. It
 *    implements the required solve() method using a depth-first
 *    search, along with supporting methods.
 *
 */
public class DFS_Solver extends MazeSolver
{
///// Constructors /////////////////////////////////////////////////
	/**
	 * Default DFS_Solver Constructor
	 * 
	 * initializes elements of the solution array with instances
	 * of the SolutionCell type
	 * 
	 * @param inputMaze		the Maze this Solver will attempt to
	 * 						   solve
	 */
	public DFS_Solver(Maze inputMaze) {
		super(inputMaze);
	} // DFS_Solver default constructor
	
///// Instance Methods /////////////////////////////////////////////
	/**
	 * Utility method: solves the maze, producing a traversal tree
	 *    and a shortest-path from start to end.
	 *    
	 * NOTE: the book psuedocode sets the color of all cells
	 *    to white before anything else. SolutionCell objects
	 *    are set white in their constructor, and SolutionCell
	 *    inherits this behavior. Therefore it's unnecessary to
	 *    re-initialize all the cells in this way. 
	 */
	@Override
	public void solve() 
	{
		for(int row = 0; row < maze.rowCount; row++)
		{
			for(int col = 0; col < maze.colCount; col++)
			{
				SolutionCell sc = (SolutionCell) solution[row][col];
				
				// recursively solve the maze using depth-first search
				DFS_Visit(sc);
			}
		} 
		
		// traverse the DFS tree from mazeEnd to mazeStart using
		// the .parent attribute of successive DFS_SolutionCells
		// and mark each cell in the resulting simple path as
		// a part of the shortest-path
		SolutionCell curr = 
				(SolutionCell) solution[maze.mazeEnd.row][maze.mazeEnd.col];
		do
		{
			curr.inOptimalPath = true;
			curr = (SolutionCell) curr.parent;
		}while(curr != null);
	} // solve()
	
	/**
	 * Method recursively performs the depth-first search
	 *    maze solution
	 *    
	 * @param sc	the solution-array cell currently being
	 * 					processed
	 */
	private void DFS_Visit(SolutionCell sc)
	{
		if (solved) return; // base case:end cell has been found. 
							// No need to keep looking.
		
		if (sc.cell == maze.mazeEnd)    // this cell is the end cell
		{								
			sc.inOptimalPath = true;	// put it in the optimal path
			sc.searchOrdinal = time++;  // set its traversal order
			sc.visited = true;	// set the last cell visited 
								// (so the last cell will print correctly)
			solved = true;              // mark the maze solved
			return;
		}
		
		// if we get this far, we haven't yet found the end
		sc.searchOrdinal = time++;
		sc.visited = true;
		
		// get the list of all directions in which we might find a
		// neighbor suitable for processing
		Path[] paths = Path.values();
		
		for(int i = 0; i < paths.length; i++)
		{
			// sc.getWhiteNeighbor() returns null if the neighbor
			// in the indicated direction does not exist, or has
			// been visited before
			SolutionCell neighbor = sc.getUnvisitedNeighbor(paths[i]);
			if(neighbor != null)
			{
				// if we get here, neighbor has not been visited
				neighbor.parent = sc;	// add it to the DFS tree
				DFS_Visit(neighbor);	// recursive call
			}
		}
	}
}
