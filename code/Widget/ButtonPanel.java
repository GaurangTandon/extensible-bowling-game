package Widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ButtonPanel extends GenericPanel {

    private HashMap<String, Component> components;

    public ButtonPanel(String heading) {
        super(heading);
        components = new HashMap<>();
        components.put("_panel", panel);
    }

    public ButtonPanel(int rows, int cols, String heading) {
        super(rows, cols, heading);
        components = new HashMap<>();
        components.put("_panel", panel);
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

    public Component get(final String id) {
        return components.get(id);
    }

    public Component getPanel() {
        return panel;
    }
}
