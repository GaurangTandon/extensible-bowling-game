package Widget;

import javax.naming.NameNotFoundException;
import java.awt.*;

public interface GenericPanelInterface {
    public Component get(final String id) throws NameNotFoundException;
    public Component getPanel();
}
