package sorting_simulation;

import java.awt.Canvas;
import java.awt.Graphics;
import javax.swing.ImageIcon;

public class MyCanvas extends Canvas {

    public static final long serialVersionUID = 2L;

    private SimulationProvider listener;

    public MyCanvas(SimulationProvider listener) {
        super();
        this.listener = listener;
    }

    public void paint(Graphics g) {
        super.paint(g);
        clear(g);

        listener.onDrawArray();
    }

    public void clear(Graphics g) {
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, this.getWidth(),  this.getHeight());
    }

    public interface SimulationProvider {

        void onDrawArray();
    }
}
