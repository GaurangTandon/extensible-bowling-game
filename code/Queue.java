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
	Queue() {
		v = new Vector<>();
	}

	Party next() {
		return v.remove(0);
	}

	public void add(final Party o) {
		v.addElement(o);
	}

	boolean hasMoreElements() {
		return !v.isEmpty();
	}

	Vector<Party> asVector() {
		return (Vector<Party>) v.clone();
	}
	
}
