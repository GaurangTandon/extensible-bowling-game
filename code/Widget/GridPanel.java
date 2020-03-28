package Widget;

import javax.swing.*;
import java.awt.*;

public class GridPanel extends GenericPanel {

    private JLabel[] itemLabel, blockLabel;

    public GridPanel(int item_count, int block_count, String heading) {
        super(0, 10, heading);
        assert (item_count == block_count * 2 + 3);
        itemLabel = new JLabel[item_count];
        blockLabel = new JLabel[block_count];

        final JPanel[] itemPanel = new JPanel[item_count]; // is reused per bowler
        for (int j = 0; j < item_count; j++) {
            itemLabel[j] = new JLabel(" ");
            itemPanel[j] = new JPanel();
            itemPanel[j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            itemPanel[j].add(itemLabel[j]);
        }

        for (int block_idx = 0; block_idx < block_count; block_idx++) {
            try {
                final JPanel fullBlockPanel = new JPanel();
                fullBlockPanel.setLayout(new GridLayout(0, 3));
                if (block_idx != block_count - 1)
                    fullBlockPanel.add(new JLabel("  "), BorderLayout.EAST);
                fullBlockPanel.add(itemPanel[2 * block_idx], BorderLayout.EAST);
                fullBlockPanel.add(itemPanel[2 * block_idx + 1], BorderLayout.EAST);
                if (block_idx == block_count - 1)
                    fullBlockPanel.add(itemPanel[2 * block_idx + 2]);
                blockLabel[block_idx] = new JLabel("  ", SwingConstants.CENTER);

                final JPanel blockPanel = new JPanel();
                blockPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                blockPanel.setLayout(new GridLayout(0, 1));
                blockPanel.add(fullBlockPanel, BorderLayout.EAST);
                blockPanel.add(blockLabel[block_idx], BorderLayout.SOUTH);
                panel.add(blockPanel, BorderLayout.EAST);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Block ID: " + block_idx);
                System.err.println("Item Group Length: " + itemPanel.length);
                System.err.println("Block Group Length: " + blockLabel.length);
                System.err.println("Item Count: " + item_count);
                System.err.println("Block Count: " + block_count);
                e.printStackTrace();
            }
        }
    }

    public JLabel getItemLabel(int i) {
        return itemLabel[i];
    }

    public JLabel getBlockLabel(int i) {
        return blockLabel[i];
    }
}
