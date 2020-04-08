package Widget;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;


public class ScrollablePanel<T> extends GenericPanel {

    private final JList<T> dataList;

    public ScrollablePanel(final String title, final ArrayList<? extends T> data, final int visibleCount) {
        super(title);
        dataList = new JList<T>((T[])data.toArray());
        dataList.setFixedCellWidth(120);
        dataList.setVisibleRowCount(visibleCount);
        final JScrollPane dataPane = new JScrollPane(dataList);
        // Remove in one of the occurrences
        dataPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(dataPane);
    }

    public ScrollablePanel(final String title, final ArrayList<T> data, final int visibleCount,
                           final ListSelectionListener listener) {
        this(title, data, visibleCount);
        dataList.addListSelectionListener(listener);
    }

    public void setListData(final ArrayList<? extends T> data) {
        dataList.setListData((T[]) data.toArray());
    }

    public JList getList() {
        return dataList;
    }

    public JPanel getPanel() { return (JPanel) super.getPanel(); }
}
