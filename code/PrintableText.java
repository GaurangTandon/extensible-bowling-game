import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

class PrintableText implements Printable {
    private final String text;
    private final int POINTS_PER_INCH;

    public PrintableText(String t) {
        POINTS_PER_INCH = 72;
        text = t;
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g; // Allow use of Java 2 graphics on

        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setPaint(Color.black);

        Point2D.Double pen = new Point2D.Double(0.25 * POINTS_PER_INCH, 0.25 * POINTS_PER_INCH);

        Font font = new Font("courier", Font.PLAIN, 12);
        FontRenderContext frc = g2d.getFontRenderContext();

        String[] lines = text.split("\n");

        int i = 0;
        for (String line : lines) {
            if (!line.isEmpty()) {
                TextLayout layout = new TextLayout(line, font, frc);
                layout.draw(g2d, (float) pen.x, (float) (pen.y + i * 14));
            }
            i += 1;
        }

        return PAGE_EXISTS;
    }

}
