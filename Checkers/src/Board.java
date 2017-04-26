
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author Mateusz
 */
public class Board extends javax.swing.JComponent {

    private final static int FIELDSIZE = 75;
    private final static int BOARDSIZE = 8 * FIELDSIZE;
    private Dimension dimPrefSize;

    private boolean inDrag = false;
    private Checker dragged;
    private int prev_x, prev_y;
    private int dx, dy;
    private Checker[] checkers;
    private int n;

    public Board() {
        dimPrefSize = new Dimension(BOARDSIZE, BOARDSIZE);
        checkers = new Checker[24];
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int x = me.getX();
                int y = me.getY();
                for (int i = 0; i < n; i++) {
                    if (checkers[i].contains(x, y)) {
                        Board.this.dragged = checkers[i];
                        prev_x = dragged.x;
                        prev_y = dragged.y;
                        dx = x - dragged.x;
                        dy = y - dragged.y;
                        inDrag = true;
                        return;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (inDrag) {
                    inDrag = false;
                } else {
                    return;
                }
                int x = me.getX();
                int y = me.getY();
                dragged.x = (x - dx) / FIELDSIZE * FIELDSIZE
                        + FIELDSIZE / 2;
                dragged.y = (y - dy) / FIELDSIZE * FIELDSIZE
                        + FIELDSIZE / 2;
                for (int i = 0; i < n; i++) {
                    if (dragged != checkers[i] && dragged.x == checkers[i].x && dragged.y == checkers[i].y) {
                        dragged.x = prev_x;
                        dragged.y = prev_y;
                    }
                }
                dragged = null;
                repaint();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                if (inDrag) {
                    dragged.x = me.getX() - dx;
                    dragged.y = me.getY() - dy;
                    repaint();
                }
            }
        });
    }
    
    @Override
    public Dimension getPreferredSize() {
        return dimPrefSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintCheckersBoard(g);
        for (int i = 0; i < n; i++) {
            checkers[i].draw(g);
        }
    }

    private void paintCheckersBoard(Graphics g) {
        for (int i = 0; i < 8; i++) {
            g.setColor(((i % 2) != 0) ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            for (int j = 0; j < 8; j++) {
                g.fillRect(j * FIELDSIZE, i * FIELDSIZE, FIELDSIZE, FIELDSIZE);
                g.setColor((g.getColor() == Color.LIGHT_GRAY) ? Color.DARK_GRAY : Color.LIGHT_GRAY);
            }
        }
    }

    public void addChecker(Checker c) {
        if (c.i < 0 || c.i > 8 || c.j < 0 || c.j > 8) {
            throw new IllegalArgumentException("Checker out of board");
        }
        for (int i = 0; i < n; i++) {
            if (c.i == checkers[i].i && c.j == checkers[i].j) {
                throw new AlreadyOccupiedException("Field at (" + c.i + ","
                        + c.j + ") is occupied");
            }
        }
        checkers[n++] = c;
    }

    public static int getFieldSize() {
        return FIELDSIZE;
    }
    /*
    public static int whichRow(int y) {

    }

    public static int whichColumn(int x) {

    }*/
}
