/* $Id$
 *
 * Revisions:
 *   $Log: LaneObserver.java,v $
 *   Revision 1.2  2003/01/30 21:44:25  ???
 *   Fixed spelling of received in may places.
 *
 *   Revision 1.1  2003/01/19 22:12:40  ???
 *   created laneEvent and laneObserver
 *
 *
 */

interface LaneObserver {
	void receiveLaneEvent(LaneEvent le);
}

