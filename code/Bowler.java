/*
 * Bowler.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log: Bowler.java,v $
 *     Revision 1.3  2003/01/15 02:57:40  ???
 *     Added accessors and and equals() method
 *
 *     Revision 1.2  2003/01/12 22:23:32  ???
 *     *** empty log message ***
 *
 *     Revision 1.1  2003/01/12 19:09:12  ???
 *     Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 * Class that holds all bowler info
 */

public class Bowler {

    private final String fullName;
    private final String nickName;
    private final String email;

    public Bowler(final String nick, final String full, final String mail) {
        nickName = nick;
        fullName = full;
        email = mail;
    }


    public String getNickName() {
        return nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNick() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public void log() {
        System.out.println("Name " + nickName + " fullname " + fullName + " email " + email);
    }

    public boolean equals(final Bowler bowler) {
        final String bowlerNickName = bowler.getNickName();
        final boolean nicknameequals = nickName.equals(bowlerNickName);

        final String bowlerFullName = bowler.getFullName();
        final boolean fullNameEquals = fullName.equals(bowlerFullName);

        final String bowlerEmail = bowler.getEmail();
        final boolean emailEquals = email.equals(bowlerEmail);

        return nicknameequals && fullNameEquals && emailEquals;
    }
}