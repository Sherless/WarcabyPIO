package Checkers.BoardAndGame;

import Checkers.GUI.CheckersGUI;
import Checkers.CheckerType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.SwingUtilities;

/**
 *
 * @author Mateusz
 */
public class Board extends javax.swing.JComponent {

    public static int FIELDSIZE = CheckersGUI.MEDIUM;
    public static int BOARDSIZE = 8 * CheckersGUI.MEDIUM;
    private CheckerType whoseTurn;
    private boolean inDrag = false;
    private Checker dragged;        //przenoszony pion
    private int prev_x, prev_y;     //współrzędne piona przed przenoszeniem
    private int dx, dy;             //różnica współrzędnych miejsca kliknięcia myszą i środka klikniętego piona
    private CheckersGUI cgui;
    private Game game;
    private myMouseAdapter mouseAdapter;

    public Board(CheckersGUI cgui) {
        this.cgui = cgui;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARDSIZE, BOARDSIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Font f = new Font("serif", Font.BOLD, FIELDSIZE / 2);
        g.setFont(f);
        paintCheckersBoard(g);
        if (game != null) {
            if (game instanceof AIGame && whoseTurn == game.team1) {
                repaint();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (whoseTurn == game.team1) {
                            if ((game.isAnyCapturePossible(whoseTurn))) {
                                ((AIGame) game).makeCapture(whoseTurn);
                                if (!game.isCapturesPossible(game.prevMove.checker)) {
                                    if (game.prevMove.i == 8) {
                                        game.prevMove.checker.setKing(true);    //jeżeli przesunięto na przeciwległy
                                    }
                                    whoseTurn = game.team2;
                                    game.isAnyMovePossible(whoseTurn);
                                }
                            } else {
                                ((AIGame) game).makeMove(whoseTurn, Board.this);
                                whoseTurn = game.team2;
                                game.isAnyMovePossible(whoseTurn);
                            }
                        }
                    }
                });
            }
            for (int i = 0; i < game.n; i++) {
                if (game.checkers[i] != dragged) {
                    game.checkers[i].draw(g, game);
                }
            }
            if (inDrag) {
                dragged.draw(g, game);
            }
            g.setColor(Color.ORANGE);
            String str;
            if (game.inGame) {
                str = whoseTurn + "'s turn";
            } else {
                str = game.winner + " wins!!!";
            }
            double l = g.getFontMetrics().stringWidth(str);
            g.drawString(str, BOARDSIZE / 2 - (int) (l / 2), FIELDSIZE / 2);
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
        if (inDrag) {               //jeżeli pion jest unoszony to wskaż pola, na które można go postawić
            if ((!game.isAnyCapturePossible(dragged.getCheckertype()))) {    //jeżeli drużyna może wykonać bicia
                for (int i = 1; i < 9; i++) {                                   //to nie możę wykonać normalnego ruchu
                    for (int j = ((i % 2 == 1) ? 2 : 1); j < 9; j++) {
                        if (game.isMovePossible(dragged, i, j)) {                    //bicia nie ma, więc wskaż
                            g.setColor(Color.MAGENTA);                          //pola, na które można wykonać zwykły ruch
                            g.fillRect((j - 1) * FIELDSIZE, (i - 1) * FIELDSIZE, FIELDSIZE, FIELDSIZE);
                            g.setColor(Color.BLACK);
                            g.drawRect((j - 1) * FIELDSIZE, (i - 1) * FIELDSIZE, FIELDSIZE, FIELDSIZE);
                        }
                    }
                }
            } else {
                paintCaptures(g);          //wskaż bicia, któe można wykonać przez piona dragged
            }
        }
    }

    private void paintCaptures(Graphics g) {     //zamaluj pola na których można wykonać bicie przez piona c
        for (int i = 1; i < 9; i++) {
            for (int j = ((i % 2 == 1) ? 2 : 1); j < 9; j++) {
                if (game.isCapturePossible(dragged, i, j) != null) {
                    g.setColor(Color.MAGENTA);
                    g.fillRect((j - 1) * FIELDSIZE, (i - 1) * FIELDSIZE, FIELDSIZE, FIELDSIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect((j - 1) * FIELDSIZE, (i - 1) * FIELDSIZE, FIELDSIZE, FIELDSIZE);
                }
            }
        }
    }

    public static int whichRow(int y) {
        return ((int) (1 + y + FIELDSIZE / 2) / FIELDSIZE);  //1 po to, aby uniknąć umiejscowienia piona
    }                                          //w złym miejscu w przypadku
    //nieparzystego wymiaru FieldSize

    public static int whichColumn(int x) {
        return ((int) (1 + x + FIELDSIZE / 2) / FIELDSIZE);
    }

    public void resize(int size) {
        Board.FIELDSIZE = size;
        Board.BOARDSIZE = 8 * Board.FIELDSIZE;
        Checker.FIELDSIZE = Board.FIELDSIZE;
        Checker.CHECKERSIZE = (int) (0.8 * Board.FIELDSIZE);
        for (int i = 0; i < game.n; i++) {
            game.checkers[i].adjust();
        }
    }

    public void undo() {            //cofnij ruch
        if (game.prevMove == null) {
            return;             //żaden ruch nie jest zapamiętany 
        }
        if (game instanceof AIGame) {
            /*prevMove w przypadku gry AI zapamiętuje sekwencje ruchów komputera
            oraz użytkownika*/
            if (game.prevMove.prev == null && game.prevMove.checker.getCheckertype() == game.team1) {
                whoseTurn = game.team1; //to znaczy, że zapamiętany jest 
            } else {                    //ruch AI tuż po rozpoczęciu gry (szczególny przypadek)
                whoseTurn = game.team2; //zapamiętana jest sekwencja ruchów AI
            }                           //oraz gracza bądź użytkownik chce cofnąć początkowe bicia
            //z sekwencji bić
            game.undo(whoseTurn);
        } else if (game.undo(whoseTurn)) {
            /*game.undo zwraca true jeśli zapamiętana jest sekwencja
            ruchów poprzedniego gracza. Należy w takim wypadku zmienić whoseTurn*/
            whoseTurn = (whoseTurn == game.team1) ? game.team2 : game.team1;
        }
        repaint();
    }

    public void setGame(Game game) {
        this.game = game;
        whoseTurn = (game.team1 == CheckerType.WHITE || game.team2 == CheckerType.WHITE) ? CheckerType.WHITE : game.team2;
        /*removeMouseListener(mouseAdapter);
        addMouseListener(new myMouseAdapter());*/
        mouseAdapter = new myMouseAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        repaint();
    }

    public void resetGame() {
        setGame((game instanceof AIGame) ? new AIGame(game.team1, game.team2) : new Game(game.team1, game.team2));
    }

    private class myMouseAdapter extends MouseAdapter {

        public myMouseAdapter() {
            super();
        }

        @Override
        public void mousePressed(MouseEvent me) {
            if (game.inGame) {
                int x = me.getX();
                int y = me.getY();
                for (int i = 0; i < game.n; i++) {
                    if (game.checkers[i].contains(x, y) && game.checkers[i].getCheckertype() == whoseTurn) {
                        dragged = game.checkers[i];
                        prev_x = dragged.x;
                        prev_y = dragged.y;
                        dx = x - dragged.x;
                        dy = y - dragged.y;
                        inDrag = true;
                        return;
                    }
                }
            } else {
                //WindowFrame wf = new WindowFrame(cgui);     //wyświetl okno ustawień
                //wf.setVisible(true);
                //cgui.setWf(wf);
                cgui.setWf();
                removeMouseListener(this);
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
            dragged.x = (x - dx) / FIELDSIZE * FIELDSIZE //zmiana współrzędnych piona
                    + FIELDSIZE / 2;
            dragged.y = (y - dy) / FIELDSIZE * FIELDSIZE
                    + FIELDSIZE / 2;
            Move m;
            if (!game.isAnyCapturePossible(dragged.getCheckertype())) {                  //jeżeli drużyna nie ma szans na bicie
                if (game.isMovePossible(dragged, whichRow(dragged.y), whichColumn(dragged.x))) { //rozpatrz czy dany ruch jest możliwy
                    game.prevMove = new Move(dragged, whichRow(dragged.y), whichColumn(dragged.x));
                    dragged.i = whichRow(dragged.y);                                        //przesunięcie piona
                    dragged.j = whichColumn(dragged.x);
                    if ((dragged.getCheckertype() == game.team1 && dragged.i == 8)
                            || (dragged.getCheckertype() == game.team2 && dragged.i == 1)) {
                        dragged.setKing(true);                                      //jeżeli przesunięto na przeciwległy
                    }                                                               //koniec planszy tzn. że pion staje się damką
                    whoseTurn = (whoseTurn == game.team1) ? game.team2 : game.team1;
                    game.isAnyMovePossible(whoseTurn);  //sprawdź czy drużyna ma możliwość jakiekolwiek ruchu
                } else {
                    if (CheckersGUI.sound && (whichRow(dragged.y) != dragged.i || whichColumn(dragged.x) != dragged.j)) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    dragged.x = prev_x;
                    dragged.y = prev_y;
                }
            } else if ((m = game.isCapturePossible(dragged, whichRow(dragged.y), whichColumn(dragged.x))) != null) { //   skoro drużyna w danym ruchu ma możliwe bicie
                dragged.i = whichRow(dragged.y);                              //   to sprawdź czy dany ruch jest biciem
                dragged.j = whichColumn(dragged.x);                           //   bicie jest możliwe, gdy dane bicie jest pierwszym w turze tej drużyny
                game.remChecker(m.removed);        //usuń zbitego piona                    bądź jest to kolejne bicie przez piona prevMove
                if (game.prevMove == null || game.prevMove.checker.getCheckertype() != whoseTurn) {  //nie jest zapmiętany
                    game.prevMove = m;                          //żaden ruch bądź ostatnim zapamiętanym ruchem jest 
                } else {                                        //ruch przeciwnika, więc można o nim "zapomnieć"
                    m.prev = game.prevMove;         //jest to któreś z kolei bicie
                    game.prevMove = m;          //więc poprzednie trzeba zapisać do prevMove.prev
                }                               //i zapamiętać aktualne w prevMove
                if (!game.isCapturesPossible(dragged)) {                   //sprawdź czy jest konieczność dalszego bicia przez piona dragged
                    if ((dragged.getCheckertype() == game.team1 && dragged.i == 8)
                            || (dragged.getCheckertype() == game.team2 && dragged.i == 1)) {
                        dragged.setKing(true);
                    }
                    whoseTurn = (whoseTurn == game.team1) ? game.team2 : game.team1;
                    game.isAnyMovePossible(whoseTurn);  //sprawdź czy drużyna ma możliwość jakiekolwiek ruchu
                }
            } else {
                if (CheckersGUI.sound && (whichRow(dragged.y) != dragged.i || whichColumn(dragged.x) != dragged.j)) {
                    Toolkit.getDefaultToolkit().beep();
                }
                dragged.x = prev_x;             //ani dane bicie ani ruch
                dragged.y = prev_y;             //jest niemożliwe
            }                                   //pion wraca na poprzednią pozycję
            dragged = null;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            if (inDrag) {
                dragged.x = me.getX() - dx;
                dragged.y = me.getY() - dy;
                repaint();
            }
        }

    }
}
