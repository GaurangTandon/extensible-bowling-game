import Widget.ContainerPanel;
import Widget.ScrollablePanel;
import Widget.WindowFrame;
import Widget.WindowView;

public class ControlDeskView extends WindowView implements Observer {

    private final ControlDesk controlDesk;
    private final ScrollablePanel partyPanel;

    ControlDeskView(final ControlDesk controlDesk) {
        this.controlDesk = controlDesk;
        String[] buttons = {ButtonNames.BTN_ADD_PARTY, ButtonNames.BTN_QUERIES, ButtonNames.BTN_FINISHED};
        partyPanel = drawScrollable("(Empty)", "Party Queue", 10);
        win = new WindowFrame("Control Desk", new ContainerPanel()
                .put(generateButtonPanel(buttons, "Controls"), "East")
                .put(controlDesk.generateLaneStatusPanel(), "Center")
                .put(partyPanel, "West"));
    }

    protected void buttonHandler(String source) {
        switch (source) {
            case ButtonNames.BTN_ADD_PARTY:
                new AddPartyView(this);
                break;
            case ButtonNames.BTN_FINISHED:
                close();
                break;
            case ButtonNames.BTN_QUERIES:
                new AdhocView();
                break;
        }
    }

    final void updateAddParty(final AddPartyView addPartyView) {
        controlDesk.addPartyToQueue(addPartyView.getParty());
    }

    public final void receiveEvent(final Event ce) {
        partyPanel.setListData(((ControlDeskEvent) ce).getPartyQueue());
    }
}
