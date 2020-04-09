package Bowlers;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Utilities.Util;

/**
 * Container that holds bowlers
 */
public class Party {
    public final ArrayList<ScorableBowler> bowlers;

    Party() {
        bowlers = new ArrayList<>(0);
    }

    public void saveState(final FileWriter fw) throws IOException {
        fw.write(bowlers.size() + "\n");
        for (final BowlerInfo bowler : bowlers) {
            fw.write(bowler.getNickName() + Util.DELIMITER + bowler.getFullName() +
                    Util.DELIMITER + bowler.getEmail() + "\n");
        }
    }

    public void loadState(final BufferedReader fr) throws IOException {
        try {
            final int size = Integer.parseInt(fr.readLine());
            bowlers.clear();

            for (int i = 0; i < size; i++) {
                final String[] bowler = fr.readLine().split(Util.DELIMITER);
                bowlers.add(new ScorableBowler(bowler[0], bowler[1], bowler[2]));
            }
        } catch (final Exception e) {
            throw new IOException();
        }
    }

    public final List<ScorableBowler> getMembers() {
        return Collections.unmodifiableList(bowlers);
    }

    public final int getPartySize() {
        return bowlers.size();
    }

    public final ArrayList<String> getMemberNicks() {
        final ArrayList<String> nicks = new ArrayList<>(getPartySize());

        for (final BowlerInfo bowler : bowlers) {
            nicks.add(bowler.getNickName());
        }
        return nicks;
    }

    public void addBowler(final ScorableBowler bowler) {
        bowlers.add(bowler);
    }

    public final String getName() {
        return bowlers.get(0).getNickName() + "'s Bowlers.Party";
    }
}
