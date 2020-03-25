package Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends FormPanel {

    public ButtonPanel(String heading) {
        super(heading);
    }

    public ButtonPanel(int rows, int cols, String heading) {
        super(rows, cols, heading);
    }

    public ButtonPanel put(final String text, ActionListener listener) {
        final JButton button = new JButton(text);
        final JPanel subPanel = new JPanel();
        subPanel.setLayout(new FlowLayout());
        button.addActionListener(listener);
        subPanel.add(button);
        panel.add(subPanel);
        components.put(text, button);
        return this;
    }
}
