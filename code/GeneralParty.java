import java.util.Vector;

interface GeneralParty {
    Vector<GeneralBowler> getMembers();

    int getPartySize();

    Vector<String> getMemberNicks();

    String getName();

    String getMemberNick(int index);
}
