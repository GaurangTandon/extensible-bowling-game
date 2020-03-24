package Widget;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

public class TextFieldPanel implements GenericPanelInterface {
    private JPanel panel;
    private HashMap<String, Component> components;

    public TextFieldPanel(int rows, int cols, String heading) {
        panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols));
        if (heading.length() > 0) {
            panel.setBorder(new TitledBorder(heading));
        }
        components = new HashMap<>();
        components.put("_panel", panel);
    }

    public TextFieldPanel put(final String text) {
        final JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new FlowLayout());
        final JLabel itemLabel = new JLabel(text);
        final JTextField itemField = new JTextField("", 15);
        itemPanel.add(itemLabel);
        itemPanel.add(itemField);
        panel.add(itemPanel);
        components.put(text, itemField);
        return this;
    }

    public Component get(final String id) {
        return components.get(id);
    }

    public Component getPanel() {
        return panel;
    }

    public String getText(final String id) {
        JTextField field = (JTextField) components.get(id);
        return field.getText();
    }
}
