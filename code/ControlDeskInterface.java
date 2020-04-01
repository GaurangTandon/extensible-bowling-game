import java.util.HashSet;

/**
 * This interface is between ControlDesk and ControlDeskView
 */
interface ControlDeskInterface {
    void assignLane();
    void addPartyToQueue(final Iterable<String> partyNicks);
    HashSet<Lane> getLanes();
    int getNumLanes();
}
