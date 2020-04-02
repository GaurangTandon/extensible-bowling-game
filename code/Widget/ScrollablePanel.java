package Widget;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.Vector;


public class ScrollablePanel<T> extends GenericPanel {

    private final JList<T> dataList;

    public ScrollablePanel(final String title, final Vector<? extends T> data, final int visibleCount) {
        super(title);
        dataList = new JList<>(data);
        dataList.setFixedCellWidth(120);
        dataList.setVisibleRowCount(visibleCount);
        final JScrollPane dataPane = new JScrollPane(dataList);
        // Remove in one of the occurrences
        dataPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(dataPane);
    }

    public ScrollablePanel(final String title, final Vector<T> data, final int visibleCount,
                           final ListSelectionListener listener) {
        this(title, data, visibleCount);
        dataList.addListSelectionListener(listener);
    }

    public void setListData(final Vector<? extends T> data) {
        dataList.setListData(data);
    }

    public JList getList() {
        return dataList;
    }

    public JPanel getPanel() { return (JPanel) super.getPanel(); }
}
