import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Container that holds bowlers
 */
class Party {
    final ArrayList<ScorableBowler> bowlers;
    private String name;

    Party() {
        bowlers = new ArrayList<>(0);
        name = "";
    }

    void saveState(final FileWriter fw) throws IOException {
        fw.write(bowlers.size() + "\n");
        for (final Bowler bowler : bowlers) {
            fw.write(bowler.getNickName() + "/" + bowler.getFullName() + "/" + bowler.getEmail() + "\n");
        }
    }

    void loadState(final BufferedReader fr) throws IOException {
        final int size = Integer.parseInt(fr.readLine());
        bowlers.clear();

        for (int i = 0; i < size; i++) {
            final String[] bowler = fr.readLine().split("/");
            bowlers.add(new ScorableBowler(bowler[0], bowler[1], bowler[2]));
        }
    }

    final ArrayList<ScorableBowler> getMembers() {
        return bowlers;
    }

    final int getPartySize() {
        return bowlers.size();
    }

    final ArrayList<String> getMemberNicks() {
        final ArrayList<String> nicks = new ArrayList<>(getPartySize());

        for (final Bowler bowler : bowlers) {
            nicks.add(bowler.getNickName());
        }
        return nicks;
    }

    void addBowler(final ScorableBowler bowler) {
        bowlers.add(bowler);

        if (bowlers.size() == 1) {
            name += bowler.getNickName() + "'s Party";
        }
    }

    final String getName() {
        return name;
    }
}
