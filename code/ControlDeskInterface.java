import java.util.HashSet;
import java.util.ArrayList;

/**
 * This interface is between ControlDesk and ControlDeskView
 */
interface ControlDeskInterface {
    void assignLane();
    void addPartyToQueue(ArrayList<String> party);
    HashSet<Lane> getLanes();
    int getNumLanes();
}
