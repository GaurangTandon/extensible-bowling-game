package Widget;

import javax.swing.*;
import java.awt.*;

public class TextFieldPanel extends FormPanel {

    public TextFieldPanel(int rows, int cols, String heading) {
        super(rows, cols, heading);
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

    public String getText(final String id) {
        JTextField field = (JTextField) components.get(id);
        return field.getText();
    }
}
