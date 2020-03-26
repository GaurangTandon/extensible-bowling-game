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
	private final Vector<GeneralParty> v;
	
	/** Queue()
	 * 
	 * creates a new queue
	 */
	Queue() {
		v = new Vector<>();
	}

	GeneralParty next() {
		return v.remove(0);
	}

	public void add(final GeneralParty o) {
		v.addElement(o);
	}

	boolean hasMoreElements() {
		return !v.isEmpty();
	}

	Vector<GeneralParty> asVector() {
		return (Vector<GeneralParty>) v.clone();
	}
	
}
