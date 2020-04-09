package Widget;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends GenericPanel {

    private final JLabel[] itemLabel, blockLabel;

    public GridPanel(final int itemCount, final int blockCount, final String heading) {
        super(0, 10, heading);
        assert (itemCount == blockCount * 2 + 3);

        itemLabel = new JLabel[itemCount];
        blockLabel = new JLabel[blockCount];

        final JPanel[] itemPanel = new JPanel[itemCount]; // is reused per bowler
        for (int j = 0; j < itemCount; j++) {
            itemLabel[j] = new JLabel(" ");
            itemPanel[j] = new JPanel();
            itemPanel[j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemPanel[j].add(itemLabel[j]);
        }

        for (int blockIdx = 0; blockIdx < blockCount; blockIdx++) {
            final JPanel blockPanel = new JPanel();
            blockPanel.setLayout(new GridLayout(0, 3));
            if (blockIdx != blockCount - 1)
                blockPanel.add(new JLabel("  "), BorderLayout.EAST);
            blockPanel.add(itemPanel[2 * blockIdx], BorderLayout.EAST);
            blockPanel.add(itemPanel[2 * blockIdx + 1], BorderLayout.EAST);

            if (blockIdx == blockCount - 1)
                blockPanel.add(itemPanel[2 * blockIdx + 2]);
            blockLabel[blockIdx] = new JLabel("  ", SwingConstants.CENTER);

            final JPanel labelPanel = new JPanel();
            labelPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            labelPanel.setLayout(new GridLayout(0, 1));
            labelPanel.add(blockPanel, BorderLayout.EAST);
            labelPanel.add(blockLabel[blockIdx], BorderLayout.SOUTH);
            panel.add(labelPanel, BorderLayout.EAST);
        }
    }

    public JLabel getItemLabel(final int i) {
        return itemLabel[i];
    }

    public JLabel getBlockLabel(final int i) {
        return blockLabel[i];
    }
}
