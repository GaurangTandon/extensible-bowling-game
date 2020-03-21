/* Queue.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
 
import java.util.Vector;

class Queue {
	private final Vector<Party> v;
	
	/** Queue()
	 * 
	 * creates a new queue
	 */
	public Queue() {
		v = new Vector<>();
	}

	public Party next() {
		return v.remove(0);
	}

	public void add(final Party o) {
		v.addElement(o);
	}

	boolean hasMoreElements() {
		return !v.isEmpty();
	}

	public Vector<Party> asVector() {
		return v;
	}
	
}
