package Widget;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class WindowView implements ActionListener {
    protected WindowFrame win;
    protected ContainerPanel container;

    protected WindowView() {
        win = null;
        container = null;
    }

    public WindowView(final String title) {
        win = new WindowFrame(title);
        container = win.getContainer();
    }

    protected void setVisible(final boolean state) {
        win.setVisible(state);
    }

    protected void close() {
        win.setVisible(false);
        System.exit(0);
    }

    public final void actionPerformed(final ActionEvent e) {
        final String source = ((AbstractButton) e.getSource()).getText();
        buttonHandler(source);
    }

    protected abstract void buttonHandler(String source);

    protected ButtonPanel generateButtonPanel(final String[] list, final String title) {
        final ButtonPanel controlsPanel = new ButtonPanel(list.length, 1, title);
        for (final String s : list) {
            controlsPanel.put(s, this);
        }
        return controlsPanel;
    }

    protected ScrollablePanel drawScrollable(@SuppressWarnings("SameParameterValue") final String firstElement, final String heading, final int visibleCount) {
        final ArrayList<String> empty = new ArrayList<>(0);
        empty.add(firstElement);
        return new ScrollablePanel(heading, empty, visibleCount);
    }

    protected ScrollablePanel drawScrollable(final ArrayList<String> list, @SuppressWarnings("SameParameterValue") final String heading, @SuppressWarnings("SameParameterValue") final int visibleCount) {
        return new ScrollablePanel(heading, list, visibleCount);
    }
}
