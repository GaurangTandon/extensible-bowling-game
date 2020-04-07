/*
 * Alley.java
 *
 * Version:
 *   $Id$
 *
 * Revisions:
 *   $Log: Alley.java,v $
 *   Revision 1.4  2003/02/02 20:28:58  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.3  2003/01/12 22:23:32  ???
 *   *** empty log message ***
 *
 *   Revision 1.2  2003/01/12 20:44:30  ???
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/01/12 19:09:12  ???
 *   Adding Party, Lane, Bowler, and Alley.
 *
 */

/**
 * Class that is the outer container for the bowling sim
 */

class Alley {
    Alley(@SuppressWarnings("SameParameterValue") final int numLanes, final int maxPatronsPerParty) {
        final ControlDesk controldesk = new ControlDesk(numLanes);
        new Thread(controldesk).start();

        final Observer cdv = new ControlDeskView(controldesk, maxPatronsPerParty);
        controldesk.subscribe(cdv);
    }
}


    
