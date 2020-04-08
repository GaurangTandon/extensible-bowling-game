package Widget;

public abstract class WindowView {
    protected WindowFrame win;
    protected ContainerPanel container;

    WindowView() {
        win = null;
    }

    public WindowView(String title) {
        win = new WindowFrame(title);
        container = win.getContainer();
    }
}
