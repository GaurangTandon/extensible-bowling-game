import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

class PrintableText implements Printable {
    private final String text;
    @SuppressWarnings("NonConstantFieldWithUpperCaseName")
    private final int POINTS_PER_INCH;

    PrintableText(final String t) {
        POINTS_PER_INCH = 72;
        text = t;
    }

    public int print(final Graphics graphics, final PageFormat pageFormat, final int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        final Graphics2D g2d = (Graphics2D) graphics; // Allow use of Java 2 graphics on

        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.setPaint(Color.black);

        final Point2D.Double pen = new Point2D.Double(0.25 * POINTS_PER_INCH, 0.25 * POINTS_PER_INCH);

        final Font font = new Font("courier", Font.PLAIN, 12);
        final FontRenderContext frc = g2d.getFontRenderContext();

        final String[] lines = text.split("\n");

        int i = 0;
        for (final String line : lines) {
            if (!line.isEmpty()) {
                final TextLayout layout = new TextLayout(line, font, frc);
                layout.draw(g2d, (float) pen.x, (float) (pen.y + i * 14));
            }
            i += 1;
        }

        return PAGE_EXISTS;
    }

}
