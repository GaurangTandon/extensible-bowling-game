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


    String getNickName() {
        return nickName;
    }

    String getFullName() {
        return fullName;
    }

    String getNick() {
        return nickName;
    }

    String getEmail() {
        return email;
    }

    public void log() {
        System.out.println("Name " + nickName + " fullname " + fullName + " email " + email);
    }

    // Log of deleted unused methods:
    // equals(Bowler other)
}