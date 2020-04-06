import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

interface GeneralParty {
    Vector<GeneralBowler> getMembers();

    int getPartySize();

    void addBowler(final GeneralBowler bowler);

    Vector<String> getMemberNicks();

    String getName();

    String getMemberNick(int index);

    void saveState(FileWriter fw) throws IOException;

    void loadState(BufferedReader fr) throws IOException;
}
