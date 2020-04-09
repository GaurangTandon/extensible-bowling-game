package Bowlers;

public class BowlerInfo {
    private final String fullName;
    private final String nickName;
    private final String email;

    public BowlerInfo(final String nick, final String full, final String mail) {
        nickName = nick;
        fullName = full;
        email = mail;
    }

    public final String getNickName() {
        return nickName;
    }

    public final String getFullName() {
        return fullName;
    }

    public final String getEmail() {
        return email;
    }

    public final void log() {
        System.out.println("Name " + nickName + " fullname " + fullName + " email " + email);
    }
}