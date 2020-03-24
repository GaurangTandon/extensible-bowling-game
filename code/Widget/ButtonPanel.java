package Widget;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ButtonPanel implements GenericPanelInterface {
    private JPanel panel;
    private HashMap<String, Component> components;

    public ButtonPanel(String heading) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
        components = new HashMap<>();
        components.put("_panel", panel);
    }

    public ButtonPanel(int rows, int cols, String heading) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
        components = new HashMap<>();
        components.put("_panel", panel);
    }

    public ButtonPanel put(final String id, final String text, ActionListener listener) {
        final JButton button = new JButton(text);
        final JPanel subPanel = new JPanel();
        subPanel.setLayout(new FlowLayout());
        button.addActionListener(listener);
        subPanel.add(button);
        panel.add(subPanel);
        components.put(id, button);
        return this;
    }

    public Component get(final String id) {
        return components.get(id);
    }
}
