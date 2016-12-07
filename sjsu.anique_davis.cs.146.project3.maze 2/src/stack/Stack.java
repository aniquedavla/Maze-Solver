package stack;
/**
 * Class implements a generic stack
 * @author Iain Davis
 *
 * @param <T> the type to store in this data structure
 */
public class Stack<T> 
{
	// relying on default constructor
	
///// Instance Methods //////////////////////////////////////////////
	/**
	 * Utility method: checks whether the stack is empty to avoid 
	 * popping off an empty stack
	 * 
	 * @return true if stack is empty, false otherwise
	 */
	private boolean isEmpty()
	{
		return top == null;
	} // isEmpty()
	
	/**
	 * Utility method: push a value onto the stack
	 * @param newData
	 */
	public void push(T newData)
	{
		StackNode newNode = new StackNode(newData);
		
		newNode.next = top;
		top = newNode;
	} // push()
	
	/**
	 * Utility method: retrieve the top value off of the stack
	 * 
	 * @return the popped value
	 */
	public T pop()
	{
		if(!isEmpty())
		{
			T retVal = top.data;
			top = top.next;
			return retVal;
		}
		else return null;
	} // pop()
	
///// Instance Fields ///////////////////////////////////////////////
	StackNode top;
	
///// Internal Classes //////////////////////////////////////////////
	/**
	 * Class implements a generic node in a stack
	 * @author Iain Davis
	 *
	 */
	private class StackNode
	{
	///// Constructors //////////////////////////////////////////////
		/**
		 * Default constructor
		 * @param newData
		 */
		private StackNode(T newData)
		{
			data = newData;
			next = null;
		} // StackNode constructor
		
	///// Instance Fields ///////////////////////////////////////////
		StackNode next;
		T data;
	} // StackNode class
} // Stack class
