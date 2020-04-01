import java.util.Vector;

interface GeneralParty {
    Vector<GeneralBowler> getMembers();

    int getPartySize();

    void addBowler(final GeneralBowler bowler);

    Vector<String> getMemberNicks();

    String getName();

    String getMemberNick(int index);
}
