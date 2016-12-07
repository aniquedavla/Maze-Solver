package Maze;

public class DFSSolver extends MazeSolver
{
	// Constructors
	protected DFSSolver(Maze inputMaze) {
		super(inputMaze);
		// TODO Auto-generated constructor stub
		for(int row = 0; row < maze.rowCount; row++)
		{
			for(int col = 0; col < maze.colCount; col++)
			{
				solutionComponents = new DFSSolutionCell[maze.rowCount][maze.colCount];
			}
		}
	}
	
	// Static Methods
	
	// Instance Methods
	@Override
	public void solve() {
		// TODO Auto-generated method stub
		
	}
	
	// Instance Fields
	
	// Inner Classes
	private class DFSSolutionCell extends SolutionCell
	{
		// Constructors
		private DFSSolutionCell(Maze.MazeCell inputCell)
		{
			super(inputCell);
			dTime = 0;		
			cTime = 0;
		}
		
		// Static Methods
		
		// Instance Methods
		
		// Static Fields
		
		// Instance Fields
		int dTime;		// in a given traversal, the time that the cell is encountered
		int cTime;		// in a given traversal, the time that the last logically-adjacent cells is encountered
		// Inner Classes
	}

}
