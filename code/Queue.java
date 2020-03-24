/* Queue.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
 
import java.util.ArrayList;

class Queue {
	private final ArrayList<Party> v;
	
	/** Queue()
	 * 
	 * creates a new queue
	 */
	public Queue() {
		v = new ArrayList<>();
	}

	public Party next() {
		return v.remove(0);
	}

	public void add(final Party o) {
		v.add(o);
	}

	boolean hasMoreElements() {
		return !v.isEmpty();
	}

	public ArrayList<Party> asVector() {
		return v;
	}
	
}
