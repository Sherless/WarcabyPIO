
import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Math.sqrt;

/**
 *
 * @author Mateusz
 */
public class Checker {

    private static int FIELDSIZE;
    private final static int CHECKERSIZE = (int) (0.8 * Board.getFieldSize());
    public int x;
    public int y;
    public int i;
    public int j;
    private CheckerType checkertype;

    public Checker(int i, int j, CheckerType ctype) {
        FIELDSIZE = Board.getFieldSize();
        this.i = i;
        this.j = j;
        x = (j-1) * FIELDSIZE + FIELDSIZE/2;
        y = (i-1) * FIELDSIZE + FIELDSIZE/2;
        checkertype = ctype;
    }

    /*public int getPosition_x() {
        return (j-1) * FIELDSIZE + FIELDSIZE/2;
    }
    
    public int getPosition_y() {
        return (i-1) * FIELDSIZE + FIELDSIZE/2;
    }*/
    
    public void draw(Graphics g) {
        if (checkertype == CheckerType.BLACK) {
            g.setColor(Color.BLACK);
        }
        if (checkertype == CheckerType.RED) {
            g.setColor(Color.RED);
        }
        if (checkertype == CheckerType.WHITE) {
            g.setColor(Color.WHITE);
        }
        g.fillOval(x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE);
        
        
    }

    boolean contains(int x, int y) {
        /*int pos_x = getPosition_x();
        int pos_y = getPosition_y();*/
        double distance = sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
        return distance < CHECKERSIZE / 2;
    }
}
