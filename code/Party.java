import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container that holds bowlers
 */
class Party {
    final ArrayList<ScorableBowler> bowlers;

    Party() {
        bowlers = new ArrayList<>(0);
    }

    void saveState(final FileWriter fw) throws IOException {
        fw.write(bowlers.size() + "\n");
        for (final BowlerInfo bowler : bowlers) {
            fw.write(bowler.getNickName() + Util.DELIMITER + bowler.getFullName() + Util.DELIMITER + bowler.getEmail() + "\n");
        }
    }

    void loadState(final BufferedReader fr) throws IOException {
        final int size = Integer.parseInt(fr.readLine());
        bowlers.clear();

        for (int i = 0; i < size; i++) {
            final String[] bowler = fr.readLine().split(Util.DELIMITER);
            bowlers.add(new ScorableBowler(bowler[0], bowler[1], bowler[2]));
        }
    }

    final List<ScorableBowler> getMembers() {
        return Collections.unmodifiableList(bowlers);
    }

    final int getPartySize() {
        return bowlers.size();
    }

    final ArrayList<String> getMemberNicks() {
        final ArrayList<String> nicks = new ArrayList<>(getPartySize());

        for (final BowlerInfo bowler : bowlers) {
            nicks.add(bowler.getNickName());
        }
        return nicks;
    }

    void addBowler(final ScorableBowler bowler) {
        bowlers.add(bowler);
    }

    final String getName() {
        return bowlers.get(0).getNickName() + "'s Party";
    }
}
