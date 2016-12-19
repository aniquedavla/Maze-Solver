package Maze;
import stack.Stack;
import java.util.Random;

/**
 * This class implements a randomly-generated maze as a Graph.
 * The maze type differs from a standard graph in the following ways:
 *  -Logical adjacency depends on physical adjacency:
 *  		in order for an edge to exist between two vertices, 
 *  		they must be side by side.
 *  -As a result of the above and the rectangular-grid structure,
 *  		each vertex has degree at most four. This class defines
 *  		the minimum number of rows and columns in the maze to be
 *  		2, which means the degree of any vertex is at least 2
 *  -Edges are unweighted.
 *  
 *  For the above reasons, the maze itself is implemented as a two-
 *     dimensional array of type MazeCell (defined below as in 
 *     inner class). Adjacency is implemented as a byte field, pathState, in the
 *     MazeCell type, with the upper four bits representing the
 *     existence of a physically-adjacent MazeCell, and the lower
 *     four bits representing logically-adjacent MazeCells (i.e. 
 *     an open path in the maze).
 * 
 * To facilitate this implementation, the Maze class also includes
 * an enumerated type Path with the following binary values:
 * 				NORTH = 0b0001;
 * 				EAST  = 0b0010;
 * 				SOUTH = 0b0100;
 * 				WEST  = 0b1000;
 * Path also includes a method that returns the opposite of a given
 *    instance (i.e. Path.NORTH.getOpposite() returns an instance
 *    of Path.SOUTH)
 *    
 * Because the maze is implemented as an array, traveling to an
 *    adjacent node only involves knowing that an open path exists, 
 *    and the array indices that constitute the address of the 
 *    adjacent neighbor.
 *
 */
public class Maze {

///// Constructors /////////////////////////////////////////////////
	/**
	 * Default Constructor: generates a two-dimensional array of
	 * MazeCell types, then calls the mazify() method to convert
	 * the array into a maze with the following properties:
	 * 			- the starting position is the farthest "northwest"
	 * 			- the ending position is the farthest "southeast"
	 * 			- randomized: the constructor creates a randomly
	 * 					selected maze from the set of all possible
	 * 					mazes of the specified size.
	 * 			- only one simple path exists from start to finish
	 * 			- there is exactly one path connecting any two rooms
	 * 			- the maze has more than 2 rows and 2 columns
	 * 
	 * @param rows the number of rows in the maze
	 * @param cols the number of columns in the maze
	 */
	public Maze(int rows, int cols)
	{
		// if an invalid parameter is passed, set to default
		rowCount = rows < 2 ? 2 : rows;
		colCount = cols < 2 ? 2 : cols;
		
		cellCount = rowCount*colCount;
		
		cells = new MazeCell[rowCount][colCount];
		
		// instantiate all maze array elements
		for (int row = 0; row < rowCount; row++)
		{
			for(int col = 0; col < colCount; col++)
			{
				cells[row][col] = new MazeCell(row, col, this.cells);
				
				// identify in which directions physically-adjacent
				// cells lie to avoid attempting to access out-of-
				// bounds array indices later
				byte pathExistence = 0;
				if (row - 1 >= 0) 	pathExistence |= Path.NORTH.value;
				if (row + 1 < rows) pathExistence |= Path.SOUTH.value;
				if (col - 1 >= 0)	pathExistence |= Path.WEST.value;
				if (col + 1 < cols) pathExistence |= Path.EAST.value;
				cells[row][col].pathState |= (pathExistence << 4);
			}
		}

		// identify the start and end cells of the maze
		mazeStart = cells[0][0];
		mazeEnd = cells[rowCount - 1][colCount - 1];
		
		// mazify() creates the paths (i.e. removes the walls) to
		// convert the array into a connected maze
		// (this implements the pseudocode in "Project3.pdf")
		mazify();
	} // Default Maze constructor
	
	// Instance Methods
	/**
	 * Utility method: changes the maze instance field an 
	 * unconnected two-dimensional array of type MazeCell into a
	 * randomized connected maze
	 */
	private void mazify()
	{
		Stack<MazeCell> stack = new Stack<MazeCell>();

		MazeCell currentCell = mazeStart;
		int visitedCells = 1;
		MazeCell neighbor = null;
		
		// spinner will eventually contain all the values in Path
		// (the cardinal directions) in random order
		// shuffling the set of values instead of randomly
		// generating them avoids searching the same invalid
		// direction multiple times
		Path[] spinner = Path.values();
		
		while(visitedCells < cellCount)
		{
			neighbor = null;
			// randomize the traverse order from the current cell
			spinner = shuffle(spinner);
			Path direction = null;

			for(int i = 0; i < spinner.length  && neighbor == null; i++)
			{
				direction = spinner[i];
				// if no neighbor exists in the given direction,
				// getNeighbor() returns null
				MazeCell temp = currentCell.getNeighbor(direction);
				
				if(temp != null && temp.hasAllWalls())
					neighbor = temp;
			}
			
			// neighbor is either a randomly-selected physically-
			// adjacent cell, or if no such cell exists, neighbor
			// is null
			if(neighbor != null)
			{
				// knock down the wall between current cell and
				// neighbor and make the neighbor the new current
				// cell
				currentCell.pathConnect(direction);
				stack.push(currentCell);
				currentCell = neighbor;
				visitedCells++;
			}
			else
			{
				currentCell = stack.pop();
			}
			
		}
		
		// the next two lines create paths into and out of the 
		// maze... only necessary for printing the maze
		mazeStart.pathConnect(Path.NORTH);
		mazeEnd.pathConnect(Path.SOUTH);
	} // mazify()
	
	/**
	 * Utility method: randomizes the order of the elements in a
	 *    copy of the array Path.values()
	 * 
	 * shuffle adapts the Fisher-Yates array shuffle algorithm
	 *    found at:
	 *    http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
	 * 
	 * @param spinner	the array to shuffle
	 * @return			the shuffled array
	 */
	private Path[] shuffle(Path[] spinner)
	{
		Random random = new Random();
		for(int i = spinner.length - 1; i > 0; i--)
		{
			int index = random.nextInt(i + 1);
			
			Path a = spinner[index];
			spinner[index] = spinner[i];
			spinner[i] = a;
		}
		return spinner;
	} // shuffle()
	
	/**
	 * Output method/instrumentation: prints a text representation
	 * of the maze to the console.
	 */
	public void printMaze()
	{
		System.out.println("start");
		for(int row = 0; row < rowCount; row++)
		{
			String northWalls = "+";
			String eastWestWalls = "|";
			String southWalls = "+";
			for( int col = 0; col < colCount; col++)
			{
				MazeCell currentCell = cells[row][col];
				if(row == 0) northWalls += 
						currentCell.hasWall(Path.NORTH) ? "���+" : "   +";
				southWalls += 
						currentCell.hasWall(Path.SOUTH) ? "���+" : "   +";
				eastWestWalls += 
						currentCell.hasWall(Path.EAST) ? "   |" :  "    ";
			}
			if(row == 0) System.out.println(northWalls);
			System.out.println(eastWestWalls);
			System.out.println(southWalls);
		}
		
		// for spacing out the "end" label
		String buffer = "";
		for(int i = 0; i < (colCount)*4 - 3; i++)
		{
			buffer += " ";
		}
		System.out.println(buffer + "end");
	} // printMaze()

	
///// Instance Fields //////////////////////////////////////////////
	protected MazeCell[][] cells;		// the chambers in the maze
	final public MazeCell mazeStart;	// the starting chamber
	final public MazeCell mazeEnd;		// the ending chamber
	public final int rowCount;			// the number of rows
	public final int colCount;			// the number of columns
	public final int cellCount;			// the total number of cells
	

	
///// Inner Classes ////////////////////////////////////////////////
	/**
	 * enum type lists the cardinal directions, each associated with a unique
	 * power of 2, allowing the path status to be determined by a 4-bit value
	 * as follows:
	 * 		Bit 0 => status of NORTH path from MazeCell
	 * 		Bit 1 => status of EAST path from MazeCell
	 * 		Bit 2 => status of SOUTH path from MazeCell
	 * 		Bit 3 => status of WEST path from MazeCell
	 * 
	 * 		total value 0 => all paths closed (all walls in place)
	 * 		total value 15 => all paths open (no walls remain)
	 * 		any value < 15 => at least one wall remains
	 * 		total value	5 =>  SOUTH and NORTH paths open
	 * 		etc...
	 * @author Iain Davis
	 */
	protected enum Path
	{
		// labels and values
		NORTH(0b0001), EAST(0b0010), SOUTH(0b0100), WEST(0b1000);
		
		// Instance Field
		private byte value;
		
		// Constructor
		private Path(int newValue)
		{
			this.value = (byte) newValue;
		} // default Path Constructor
		
		/**
		 * Utility method: for a given instance of Path, returns
		 * another instance of Path with the opposite value
		 * (i.e. Path.EAST.getOpposite() returns Path.WEST)
		 * @return	an instance of Path with the opposite value of 
		 * 				the caller
		 */
		private Path getOpposite()
		{
			switch(this)
			{
			case NORTH:
				return Path.SOUTH;
			case EAST:
				return Path.WEST;
			case SOUTH:
				return Path.NORTH;
			case WEST:
				return Path.EAST;
			default:
				return null;	
			}
		}// getOpposite
	}// enum Path
	
	/**
	 * Class implements one cell in a Maze represented as a two-
	 *    dimensional array. MazeCell contains its own "address"
	 *    in the form of row and column indices into the maze
	 *    array. MazeCell also contains a pointer back to the
	 *    maze that contains it.
	 *    
	 * @author Iain Davis
	 *
	 */
	protected class MazeCell
	{
	///// Constructors /////////////////////////////////////////////
		/**
		 * Default Constructor: initializes values
		 * 
		 * @param newRow		the row index (into the maze array)
		 * 							that points at this cell
		 * @param newCol		the column index (into the maze
		 * 							array) that points at this cell
		 * @param parentMaze	the 2D array that contains this cell
		 */
		protected MazeCell(int newRow, int newCol, MazeCell[][] parentMaze)
		{
			row = newRow;
			col = newCol;
			maze = parentMaze;

			pathState = 0;
		} // default MazeCell constructor
		
	///// Instance Methods /////////////////////////////////////////
		/**
		 * Utility method: Checks the lower four bits of the
		 *    pathState field for the value that indicates all
		 *    walls in place
		 * 
		 * @return	true if all four walls exist, false if caller
		 * 				has any paths open
		 */
		private boolean hasAllWalls(){
			return (pathState & 0b00001111) == 0;
		} // hasAllWalls()
		
		/**
		 * Utility method: checks the lower four bits of the 
		 *    pathState field to see if a wall exists in the
		 *    indicated direction.
		 * 
		 * @param direction		the direction to check for a wall
		 * @return				true, if a wall exists in the
		 * 							indicated direction
		 * 						false, if the wall is removed
		 * 
		 * opposite to the hasPath() method
		 */
		protected boolean hasWall(Path direction){
			return (pathState & direction.value) == 0;
		} // hasWall()
		
		/**
		 * Utility method: checks the lower four bits of the 
		 *    pathState field to see if a path exists in the
		 *    indicated direction.
		 *    
		 * @param direction		the direction to check for a path
		 * @return				true, if a path exists in the 
		 * 							indicated direction
		 * 						false, if the path is blocked
		 * 
		 * opposite to the hasWall() method
		 */
		protected boolean hasPath(Path direction){
			return (pathState & direction.value) != 0;
		} // hasPath()
		
		/**
		 * Utility method: checks the upper four bits of the
		 *    pathState for the presence of a physically adjacent
		 *    cell (a neighbor) whether an edge exists or not. 
		 *    Used to prevent attempting to access a null MazeCell
		 *    or an out-of-bounds array index.
		 * 
		 * @param direction		the direction to check for a neighbor
		 * @return				true, if a neighbor exists in the
		 * 							indicated direction
		 * 						false, if no neighbor exists
		 */
		protected boolean hasNeighbor(Path direction){
			return (pathState & (direction.value << 4)) != 0;
		} // hasNeighbor()
		
		/**
		 * Utility method: returns the neighbor of the calling
		 *    MazeCell in the indicated direction, if one exists
		 *    
		 * @param direction		the direction from which to retrieve
		 * 							the neighbor
		 * @return				the neighbor, if it exists
		 * 						null, if no neighbor exists in the
		 * 							indicated direction
		 */
		protected MazeCell getNeighbor(Path direction)
		{
			if(hasNeighbor(direction)) 
			{
				switch(direction)
				{
				case NORTH:
					return maze[row - 1][col];
				case EAST:
					return maze[row][col + 1];
				case SOUTH:
					return maze[row + 1][col];
				case WEST:
					return maze[row][col - 1];
				}
			}
			return null;
		} // getNeighbor()
		
		/**
		 * Utility method: creates a two-way path between the
		 *    MazeCell and its neighbor in the indicated direction.
		 *    
		 * If no neighbor exists, creates a one-way path in 
		 *    the indicated direction (i.e. out of the maze).
		 *    
		 * @param direction		the direction to create a path
		 * 							from the caller
		 * @return				the new pathState of the caller
		 */
		private byte pathConnect(Path direction)
		{
			// create outbound path
			this.pathState |= direction.value;
			
			MazeCell that = this.getNeighbor(direction);
			if(that != null)
				// create inbound path (in neighbor)
				that.pathState |= direction.getOpposite().value;
			
			return this.pathState;
		} // pathConnect()

		
	///// Instance Fields //////////////////////////////////////////
		final int row;
		final int col;
		final MazeCell[][] maze;
		
		byte pathState; 	// the lower 4 bits of pathState indicate which
							// neighboring cells are logically-adjacent
							// the upper 4 bits of pathState indicate whether
							// the array contains array-adjacent cells
	} // MazeCell class
	
} // Maze class
