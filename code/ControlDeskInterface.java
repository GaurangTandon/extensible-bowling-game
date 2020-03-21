import java.util.HashSet;
import java.util.Vector;

/**
 * This interface is between ControlDesk and ControlDeskView
 */
interface ControlDeskInterface {
    void assignLane();
    void addPartyToQueue(Vector<String> party);
    HashSet<Lane> getLanes();
    int getNumLanes();
}
