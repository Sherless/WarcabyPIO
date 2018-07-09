package Checkers.GUI;

import Checkers.BoardAndGame.*;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Mateusz
 */
public class CheckersGUI extends javax.swing.JFrame {

    private Board board;
    public static boolean sound = true;
    private static Clip clip;
    private WindowFrame wf;

    public static final int SCREENX = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREENY = Toolkit.getDefaultToolkit().getScreenSize().height - 100;
    public static final int SMALL = (int) (0.5 * SCREENY / 8);
    public static final int MEDIUM = (int) (0.75 * SCREENY / 8);
    public static final int BIG = SCREENY / 8;

    public CheckersGUI() {
        super("Checkers");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        getContentPane().setLayout(new GridBagLayout());
        setLocation((SCREENX - Board.BOARDSIZE) / 2, (SCREENY - Board.BOARDSIZE) / 4);
        board = new Board(this);
        getContentPane().add(board);
        pack();
        setResizable(false);
        setVisible(true);
    }

    public static void playSound(final String name) {
        try {
            if (clip != null) {
                clip.stop();
            }
            clip = null;
            clip = AudioSystem.getClip(null);
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    new File(name));
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;

    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("New Game");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItem1.setText("Restart");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem2.setText("Options");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Settings");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        jMenuItem3.setText("Undo a move");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenu4.setText("Set a picture");

        jMenuItem4.setText("for Team1");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem5.setText("for Team2");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenu2.add(jMenu4);

        jCheckBoxMenuItem1.setSelected(sound);
        jCheckBoxMenuItem1.setText("Sound");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItem1);

        jMenu3.setText("Size");

        jRadioButtonMenuItem1.setSelected(Board.FIELDSIZE == SMALL);
        jRadioButtonMenuItem1.setText("Small");
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem1);

        jRadioButtonMenuItem2.setSelected(Board.FIELDSIZE == MEDIUM);
        jRadioButtonMenuItem2.setText("Medium");
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem2);

        jRadioButtonMenuItem3.setSelected(Board.FIELDSIZE == BIG);
        jRadioButtonMenuItem3.setText("Big");
        jRadioButtonMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem3);

        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

    }

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jRadioButtonMenuItem1.isSelected()) {
            jRadioButtonMenuItem1.setSelected(true);
        }
        jRadioButtonMenuItem2.setSelected(false);// TODO add your handling code here:
        jRadioButtonMenuItem3.setSelected(false);
        setResizable(true);
        setVisible(false);
        board.resize(SMALL);
        setLocation((SCREENX - Board.BOARDSIZE) / 2, (SCREENY - Board.BOARDSIZE) / 4);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jRadioButtonMenuItem2.isSelected()) {
            jRadioButtonMenuItem2.setSelected(true);
        }
        jRadioButtonMenuItem1.setSelected(false);// TODO add your handling code here:
        jRadioButtonMenuItem3.setSelected(false);
        setResizable(true);
        setVisible(false);
        board.resize(MEDIUM);
        setLocation((SCREENX - Board.BOARDSIZE) / 2, (SCREENY - Board.BOARDSIZE) / 4);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void jRadioButtonMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jRadioButtonMenuItem3.isSelected()) {
            jRadioButtonMenuItem3.setSelected(true);
        }
        jRadioButtonMenuItem1.setSelected(false);// TODO add your handling code here:
        jRadioButtonMenuItem2.setSelected(false);
        setResizable(true);
        setVisible(false);
        board.resize(BIG);
        setLocation((SCREENX - Board.BOARDSIZE) / 2, (SCREENY - Board.BOARDSIZE) / 4);
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (wf != null) {
            wf.setVisible(false);
        }
        board.resetGame();
        if (sound) {
            playSound("start.wav");
        }
    }

    public void newGame(Game game) {
        setEnabled(true);
        board.setGame(game);
        if (sound) {
            playSound("start.wav");
        }
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        setWf();
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        board.undo();
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser();
        fc.removeChoosableFileFilter(fc.getFileFilter());
        fc.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selected = fc.getSelectedFile();
            if (fc.accept(selected) && selected.exists()) {
                Checker.setPic1(fc.getSelectedFile());
                board.repaint();
            }
        }
    }

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {
        final JFileChooser fc = new JFileChooser();
        fc.removeChoosableFileFilter(fc.getFileFilter());
        fc.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selected = fc.getSelectedFile();
            if (fc.accept(selected) && selected.exists()) {
                Checker.setPic2(fc.getSelectedFile());
                board.repaint();
            }
        }
    }

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        sound = jCheckBoxMenuItem1.isSelected(); // TODO add your handling code here:
    }

    public void setWf() {
        if (wf != null) {
            wf.setVisible(false);
        }
        wf = new WindowFrame(this);
        wf.setVisible(true);
    }

}
