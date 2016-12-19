package Maze;
import Maze.Maze.Path;
import queue.Queue;

/**
 * DFS_Solver inherits from the general MazeSolver class. It
 *    implements the required solve() method using a depth-first
 *    search, along with supporting methods.
 *
 */
public class BFS_Solver extends MazeSolver
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
	public BFS_Solver(Maze inputMaze) {
		super(inputMaze);
	} // DFS_Solver default constructor
	
///// Instance Methods /////////////////////////////////////////////
	/**
	 * Utility method: solves the maze, producing a traversal tree
	 *    and a shortest-path from start to end.
	 *    
	 * NOTE: the book psuedocode marks all cells not visited 
	 *    before anything else. SolutionCell objects are marked 
	 *    not visited in their constructor. Therefore it's
	 *    unnecessary to re-initialize all the cells in this way. 
	 */
	@Override
	public void solve() 
	{	
		SolutionCell start = solution[maze.mazeStart.row][maze.mazeStart.col];
		
		Queue<SolutionCell> q = new Queue<SolutionCell>();
		
		start.visited = true;
		start.searchOrdinal = 0;
		q.enqueue(start);
		SolutionCell sc;
		
		Path[] paths = Path.values();
		while((sc = q.dequeue()) != null && sc.cell != maze.mazeEnd)
		{
			for(int i = 0; i < paths.length; i++)
			{
				//TODO fix this
				SolutionCell neighbor = sc.getUnvisitedNeighbor(paths[i]);
				if(neighbor != null )
				{
					neighbor.visited = true;
					neighbor.searchOrdinal = sc.searchOrdinal + 1;
					neighbor.parent = sc;
					q.enqueue(neighbor);
				}
			}
		}
		
		// traverse the BFS tree from mazeEnd to mazeStart using
		// the .parent attribute of successive DFS_SolutionCells
		// and mark each cell in the resulting simple path as
		// a part of the shortest-path
		SolutionCell curr = solution[maze.mazeEnd.row][maze.mazeEnd.col];
		do
		{
			curr.inOptimalPath = true;
			curr = curr.parent;
		}while(curr != null);
	} // solve()
}
