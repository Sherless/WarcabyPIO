package Checkers.BoardAndGame;

import Checkers.CheckerType;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Mateusz
 */
public class Checker {

    public static int FIELDSIZE = Board.FIELDSIZE;
    public static int CHECKERSIZE = (int) (0.8 * Board.FIELDSIZE);
    public int i;           //rząd
    public int j;           //kolumna
    public int x;           //współrzędne x
    public int y;           //współrzędne y
    private final CheckerType checkertype;
    private boolean isKing;  //czy damka?
    private static BufferedImage pic1;
    private static BufferedImage pic2;
    private static BufferedImage BLACK = null;
    private static BufferedImage RED = null;
    private static BufferedImage WHITE = null;

    static {
        try {
            BLACK = ImageIO.read(new File("blackOval.png"));
            RED = ImageIO.read(new File("redOval.png"));
            WHITE = ImageIO.read(new File("whiteOval.png"));
        } catch (IOException ex) {
            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Checker(int i, int j, CheckerType ctype) {
        this.i = i;
        this.j = j;
        x = (j - 1) * FIELDSIZE + FIELDSIZE / 2;
        y = (i - 1) * FIELDSIZE + FIELDSIZE / 2;
        checkertype = ctype;
    }

    public CheckerType getCheckertype() {
        return checkertype;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean isKing) {
        this.isKing = isKing;
    }

    public void draw(Graphics g, Game game) {
        if (isKing == true) {
            try {
                if (checkertype == game.team1) {
                    if (pic1 == null) {
                        if (checkertype == CheckerType.BLACK) {
                            g.drawImage(BLACK, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                        } else if (checkertype == CheckerType.RED) {
                            g.drawImage(RED, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                        } else if (checkertype == CheckerType.WHITE) {
                            g.drawImage(WHITE, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                        }
                    } else {
                        g.drawImage(pic1, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                    }
                } else if (checkertype == game.team2) {
                    if (pic2 == null) {
                        if (checkertype == CheckerType.BLACK) {
                            g.drawImage(BLACK, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                        } else if (checkertype == CheckerType.RED) {
                            g.drawImage(RED, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                        } else if (checkertype == CheckerType.WHITE) {
                            g.drawImage(WHITE, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                        }
                    } else {
                        g.drawImage(pic2, x - CHECKERSIZE / 2, y - CHECKERSIZE / 2, CHECKERSIZE, CHECKERSIZE, null);
                    }
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        } else {
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
    }

    public static BufferedImage makeOvalImage(BufferedImage master) {

        int diameter = Math.min(master.getWidth(), master.getHeight());
        BufferedImage mask = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();
        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        applyQualityRenderingHints(g2d);
        int x = (diameter - master.getWidth()) / 2;
        int y = (diameter - master.getHeight()) / 2;
        g2d.drawImage(master, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();

        return masked;
    }

    public static void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static void setPic1(File f) {
        try {
            pic1 = makeOvalImage(ImageIO.read(f));
        } catch (IOException ex) {
            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setPic2(File f) {
        try {
            pic2 = makeOvalImage(ImageIO.read(f));
        } catch (IOException ex) {
            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean contains(int x, int y) {        //sprawdza czy punkt (x, y) należy do piona
        int distance = (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
        return distance < CHECKERSIZE * CHECKERSIZE / 4;
    }

    public void adjust() {
        x = (j - 1) * FIELDSIZE + FIELDSIZE / 2;
        y = (i - 1) * FIELDSIZE + FIELDSIZE / 2;
    }

    @Override
    public String toString() {
        return "Pionek: i = " + i + " j = " + j;
    }

}
