package Widget;

import javax.naming.NameNotFoundException;
import java.awt.*;

public interface GenericPanelInterface {
    Component get(final String id) throws NameNotFoundException;
    Component getPanel();
}
