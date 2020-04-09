package Widget;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;


public class ScrollablePanel extends GenericPanel {

    private final JList<String> dataList;

    public ScrollablePanel(final String title, final ArrayList<String> data, final int visibleCount) {
        super(title);
        String[] inData = new String[data.size()];
        inData = data.toArray(inData);
        dataList = new JList<>(inData);
        dataList.setFixedCellWidth(120);
        dataList.setVisibleRowCount(visibleCount);
        final JScrollPane dataPane = new JScrollPane(dataList);
        // Remove in one of the occurrences
        dataPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(dataPane);
    }

    public ScrollablePanel(final String title, final ArrayList<String> data, final int visibleCount,
                           final ListSelectionListener listener) {
        this(title, data, visibleCount);
        dataList.addListSelectionListener(listener);
    }

    public ScrollablePanel attachListener(final ListSelectionListener listener) {
        dataList.addListSelectionListener(listener);
        return this;
    }

    public void setListData(final ArrayList<String> data) {
        String[] inData = new String[data.size()];
        inData = data.toArray(inData);
        dataList.setListData(inData);
    }

    public JList getList() {
        return dataList;
    }

    public JPanel getPanel() { return (JPanel) super.getPanel(); }
}
