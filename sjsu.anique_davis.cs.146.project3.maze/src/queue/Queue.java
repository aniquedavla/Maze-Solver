package queue;

/**
 * Class implements a generic queue
 * 
 * @author Iain Davis
 *
 * @param <T>
 */
public class Queue<T> 
{
///// Constructors //////////////////////////////////////////////////
	/**
	 * Default constructor
	 */
	public Queue()
	{
		onDeck = null;
		last = null;
		count = 0;
	} // default Queue constructor

///// Instance Methods //////////////////////////////////////////////
	/**
	 * Utility method: add an element to the queue
	 * @param newData  the data to add
	 */
	public void enqueue(T newData)
	{
		QNode newNode = new QNode(newData);
		
		if(onDeck == null) // empty queue
		{
			onDeck = newNode;
			last = newNode;
		}
		else
		{
			last.next = newNode;
			last = newNode;
		}
		
		count++;
	} // enqueue()
	
	/**
	 * Utility method: removes an item from the queue in a
	 * first-in-first-out fashion.
	 * 
	 * @return the removed item
	 */
	public T dequeue()
	{
		if(count < 1) 
			return null;
		else
		{
			QNode dq = onDeck;
			onDeck = onDeck.next;
			dq.next = null;
			count--;
			return dq.data;
		}
	} // dequeue()
	
	/**
	 * Utility method: checks whether queue has any data in it
	 * 
	 * @return		true if empty, false otherwise
	 */
	public boolean isEmpty()
	{
		return count < 1;
	}

///// Instance Fields ///////////////////////////////////////////////
	QNode onDeck;			// the next item to remove
	QNode last;				// the item added most recently
	int count;				// the number of items in queue
	
///// Inner Classes /////////////////////////////////////////////////
	/**
	 * Default QNode constructor
	 * 
	 * @author Iain Davis
	 */
	private class QNode
	{
	///// Constructors //////////////////////////////////////////////
		/**
		 * default constructor
		 * 
		 * @param newData the data to store in this node
		 */
		private QNode(T newData)
		{
			data = newData;
			next = null;
		}
		
	///// Instance Fields ///////////////////////////////////////////
		T data;
		QNode next;
	} // QNode class
} // Queue class
