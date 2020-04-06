import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * Container that holds bowlers
 */
class Party implements GeneralParty {
    private final Vector<GeneralBowler> bowlers;
    private String name;

    Party() {
        bowlers = new Vector(0);
        name = "";
    }

    public void addBowler(final GeneralBowler bowler) {
        bowlers.add(bowler);
        if (bowlers.size() == 1) {
            name += bowler.getNickName() + "'s Party";
        }
    }

    /**
     * Accessor for members in this party
     *
     * @return A vector of the bowlers in this party
     */

    public void saveState(FileWriter fw) throws IOException {
        fw.write(bowlers.size() + "\n");
        for (GeneralBowler bowler : bowlers) {
            fw.write(bowler.getNickName() + "/" + bowler.getFullName() + "/" + bowler.getEmail() + "\n");
        }
    }

    public void loadState(BufferedReader fr) throws IOException {
        int size = Integer.parseInt(fr.readLine());
        bowlers.clear();
        for (int i = 0; i < size; i++) {
            String[] bowler = fr.readLine().split("/");
            bowlers.add(new Bowler(bowler[0], bowler[1], bowler[2]));
        }
    }

    public final Vector<GeneralBowler> getMembers() {
        return bowlers;
    }

    public final int getPartySize() {
        return bowlers.size();
    }

    public final Vector<String> getMemberNicks() {
        final Vector<String> nicks = new Vector<>(getPartySize());

        for (final GeneralBowler bowler : bowlers) {
            nicks.add(bowler.getNickName());
        }
        return nicks;
    }

    public final String getName() {
        return name;
    }

    public final String getMemberNick(final int index) {
        return bowlers.get(index).getNickName();
    }
}
