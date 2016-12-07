package Maze;

abstract class MazeSolver {
	// Constructors
	protected MazeSolver(Maze inputMaze)
	{
		maze = inputMaze;
		solutionComponents = new SolutionCell[maze.rowCount][maze.colCount];
	}
	
	// Static Methods
	
	// Instance Methods
	public abstract void solve();
	
	//TODO implement printSolution() method
	public void printSolution()
	{
		
	}
	//TODO implement printTraverseMap() method
	public void printTraverseMap()
	{
		
	}
	
	
	// Static Fields
	
	// Instance Fields
	Maze maze;
	SolutionCell[][] solutionComponents;
	boolean solved;
	// Inner Classes
	
	enum cellColor {WHITE, GRAY, BLACK};
	
	abstract class SolutionCell
	{
		// Constructors
		protected SolutionCell(Maze.MazeCell inputCell)
		{
			cell = inputCell;
			color = cellColor.WHITE;
			inOptimalPath = false;
			searchOrdinal = 0;
			solved = false;
		}
		
		final Maze.MazeCell cell; 	// link to the related cell in the Maze
		Maze.MazeCell parent; 	// link to the parent cell in the search tree
		cellColor color;		// discovery status color
		boolean inOptimalPath;	// include in shortest-path solution?
		int searchOrdinal;		// this cell's place in the discovery sequence
	}
}
