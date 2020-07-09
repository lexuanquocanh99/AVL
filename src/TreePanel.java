import java.awt.*;
import javax.swing.*;

public class TreePanel extends JPanel implements Runnable {

    /**Instance Variable**/
    private boolean name = false, lines = false;
    private Tree tree = null;
    private MainFrame mf;
    private Thread thread = null;
    private Dimension dimensionSize, currentSize;
    private Image img;
    private Graphics graph;

    /**Constructor**/
    public TreePanel() {

    }

    public void setObjects(Tree tree, MainFrame mf) {
        this.tree = tree;
        this.mf = mf;
    }

    public void startDrawingThread() {
        if (this.thread == null) {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    public void paintComponent(Graphics g) {
        currentSize = getSize();

        if ((this.img == null) || (currentSize.width != this.dimensionSize.width) || (currentSize.height != this.dimensionSize.height)) {
            this.img = createImage(currentSize.width, currentSize.height);
            this.graph = this.img.getGraphics();
            this.dimensionSize = currentSize;
            this.tree.readjustTheNodes();
        }

        this.graph.setColor(Color.WHITE); //sets the color to white
        this.graph.fillRect(0, 0, getWidth(), getHeight()); //paints the background in white

        if(this.tree != null) {
            if(lines)
                drawDashedLine(this.graph);

            if(name)
                drawDepthString(this.graph);

            this.tree.drawOntoTheDisplayPanel(this.graph);
        }
        g.drawImage(this.img, 0, 0, null);
    }

    public void drawDashedLine(Graphics g) {
        Graphics2D g2d = (Graphics2D) img.getGraphics();

        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{1,2}, 0);
        g2d.setStroke(dashed);

        final int x1 = 0;
        final int x2 = this.getWidth();
        final int numLines = 10;
        int y = 100;

        for(int i=0; i<numLines; i++) {
            g2d.drawLine(x1, y, x2, y);
            y += 55;
        }
        g2d.dispose();
    }

    public void drawDepthString(Graphics g) {
        Graphics2D g2d = (Graphics2D) img.getGraphics();

        final int numLines = 10;
        int y = 100;
        int x = this.getWidth();
        g2d.setFont(new Font("Courier New", Font.PLAIN, 11));

        for(int i=0; i<numLines; i++) {
            g2d.drawString("depth "+i, x-50, y-5);
            y += 55;
        }
        g2d.dispose();
    }

    public void run() {
        for(;;)
        {
            repaint();
            try {
                int sleepValue = mf.getSpeedSlider().getValue();
                Thread.sleep(sleepValue);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public Dimension getDimensionSize() {
        return dimensionSize;
    }
}
