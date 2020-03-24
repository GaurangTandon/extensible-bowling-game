/**
 * To change this generated comment edit the template variable "type comment":
 * WindowFrame>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * WindowFrame>Preferences>Java>Code Generation.
 */

class Score {

    private final String nickname;
    private final String date;
    private final String score;

    Score(final String nick, final String date, final String score) {
        nickname = nick;
        this.date = date;
        this.score = score;
    }

    final String getDate() {
        return date;
    }

    final String getScore() {
        return score;
    }

    public final String toString() {
        return nickname + "\t" + date + "\t" + score;
    }

}
