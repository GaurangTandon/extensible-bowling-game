class BowlerInfo {
    private final String fullName;
    private final String nickName;
    private final String email;

    BowlerInfo(final String nick, final String full, final String mail) {
        nickName = nick;
        fullName = full;
        email = mail;
    }

    final String getNickName() {
        return nickName;
    }

    final String getFullName() {
        return fullName;
    }

    final String getEmail() {
        return email;
    }

    final void log() {
        System.out.println("Name " + nickName + " fullname " + fullName + " email " + email);
    }
}