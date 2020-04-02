class Score {

    private final String nickname;
    private final String date;
    private final Integer score;

    Score(final String nick, final String date, final String score) {
        nickname = nick;
        this.date = date;
        this.score = Integer.parseInt(score);
    }

    Score() {
        nickname = "";
        date = "";
        score = 0;
    }

    final String getDate() {
        return date;
    }

    final Integer getScore() {
        return score;
    }

    public final String toString() {
        return nickname + "\t" + date + "\t" + score;
    }

    final String getNick() {
        return nickname;
    }
}
