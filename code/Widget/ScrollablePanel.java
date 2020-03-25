package Widget;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.Vector;


public class ScrollablePanel<T> extends GenericPanel {

    private final JList<T> dataList;

    public ScrollablePanel(String title, Vector<T> data, int visibleCount, ListSelectionListener listener) {
        super(title);
        dataList = new JList<>(data);
        dataList.setFixedCellWidth(120);
        dataList.setVisibleRowCount(visibleCount);
        dataList.addListSelectionListener(listener);
        final JScrollPane dataPane = new JScrollPane(dataList);
        // Remove in one of the occurrences
        dataPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(dataPane);
    }

    public void setListData(Vector<T> data) {
        dataList.setListData(data);
    }

    public JList getList() {
        return dataList;
    }

    public JPanel getPanel() { return (JPanel) super.getPanel(); }
}
