package Widget;

import javax.naming.NameNotFoundException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Vector;


public class ScrollablePanel<T> implements GenericPanelInterface {

    private final JPanel panel;
    private final JList<T> dataList;

    public ScrollablePanel(String title, Vector<T> data, int visibleCount, ListSelectionListener listener) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBorder(new TitledBorder(title));
        dataList = new JList<T>(data);
        dataList.setFixedCellWidth(120);
        dataList.setVisibleRowCount(visibleCount);
        dataList.addListSelectionListener(listener);
        final JScrollPane dataPane = new JScrollPane(dataList);
        // Remove in one of the occurrences
        dataPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(dataPane);
    }

    public Component get(String id) throws NameNotFoundException {
        throw new NameNotFoundException();
    }

    public Component getPanel() {
        return panel;
    }

    public void setListData(Vector<T> data) {
        dataList.setListData(data);
    }

    public JList getList() {
        return dataList;
    }
}
