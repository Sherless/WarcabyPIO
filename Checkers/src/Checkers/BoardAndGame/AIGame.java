package Checkers.BoardAndGame;

import Checkers.CheckerType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mateusz
 */
public class AIGame extends Game {

    public AIGame(CheckerType team1, CheckerType team2) {
        super(team1, team2);
    }

    public void makeMove(CheckerType t, Board b) {

        if (!inGame) {
            return;
        }

        ArrayList<Move> moves = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (t == checkers[i].getCheckertype()) {
                if (checkers[i].isKing()) {
                    for (int row = 1; row < 9; row++) {
                        for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {
                            if (isMovePossible(checkers[i], row, col)) {
                                moves.add(new Move(checkers[i], row, col));
                            }
                        }
                    }
                } else {
                    if (isMovePossible(checkers[i], checkers[i].i - 1, checkers[i].j - 1)) {
                        moves.add(new Move(checkers[i], checkers[i].i - 1, checkers[i].j - 1));
                    }
                    if (isMovePossible(checkers[i], checkers[i].i - 1, checkers[i].j + 1)) {
                        moves.add(new Move(checkers[i], checkers[i].i - 1, checkers[i].j + 1));
                    }
                    if (isMovePossible(checkers[i], checkers[i].i + 1, checkers[i].j - 1)) {
                        moves.add(new Move(checkers[i], checkers[i].i + 1, checkers[i].j - 1));
                    }
                    if (isMovePossible(checkers[i], checkers[i].i + 1, checkers[i].j + 1)) {
                        moves.add(new Move(checkers[i], checkers[i].i + 1, checkers[i].j + 1));
                    }
                }
            }
        }
        Random r = new Random();
        if (moves.size() > 0) {
            int toSleep = 500 + r.nextInt(500);
            try {
                java.lang.Thread.sleep(toSleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(AIGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Collections.shuffle(moves);
            Move.sort(moves, this);
            int c = 0;
            makeMove(moves.get(c));
            if (prevMove == null) {
                prevMove = moves.get(c);
            } else {
                moves.get(c).prev = prevMove;
                prevMove = moves.get(c);
            }
            moves.get(c).checker.adjust();
            if (moves.get(c).checker.i == 8) {
                moves.get(c).checker.setKing(true);         //jeżeli przesunięto na przeciwległy
            }
        }
    }

    public Move makeCapture(CheckerType t) {

        if (!inGame) {
            return null;
        }

        ArrayList<Move> moves = new ArrayList<>();
        Move m;

        if (prevMove == null || prevMove.checker.getCheckertype() != t) {
            for (int i = 0; i < n; i++) {
                if (t == checkers[i].getCheckertype()) {
                    if (checkers[i].isKing()) {
                        for (int row = 1; row < 9; row++) {       //srpawdź czy bicie damką jest możliwe
                            for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {   //dla każdeo pola
                                if ((m = isCapturePossible(checkers[i], row, col)) != null) {
                                    moves.add(m);
                                }
                            }
                        }
                    } else {                   //sprawdź czy bicie pinem jest możliwe
                        //dla standardowch pól (oddalonych o 2 i po przekątnych)
                        if ((m = isCapturePossible(checkers[i], checkers[i].i - 2, checkers[i].j - 2)) != null) {
                            moves.add(m);
                        }
                        if ((m = isCapturePossible(checkers[i], checkers[i].i - 2, checkers[i].j + 2)) != null) {
                            moves.add(m);
                        }
                        if ((m = isCapturePossible(checkers[i], checkers[i].i + 2, checkers[i].j - 2)) != null) {
                            moves.add(m);
                        }
                        if ((m = isCapturePossible(checkers[i], checkers[i].i + 2, checkers[i].j + 2)) != null) {
                            moves.add(m);
                        }
                    }
                }
            }
        } else {
            if (prevMove.checker.isKing()) {
                for (int row = 1; row < 9; row++) {       //srpawdź czy bicie damką jest możliwe
                    for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {   //dla każdeo pola
                        if ((m = isCapturePossible(prevMove.checker, row, col)) != null) {
                            if (prevMove.direction != -m.direction) {
                                moves.add(m);
                            }
                        }
                    }
                }
            } else {                   //sprawdź czy bicie pionem jest możliwe
                //dla standardowch pól (oddalonych o 2 i po przekątnych)
                if ((m = isCapturePossible(prevMove.checker, prevMove.checker.i - 2, prevMove.checker.j - 2)) != null) {
                    moves.add(m);
                }
                if ((m = isCapturePossible(prevMove.checker, prevMove.checker.i - 2, prevMove.checker.j + 2)) != null) {
                    moves.add(m);
                }
                if ((m = isCapturePossible(prevMove.checker, prevMove.checker.i + 2, prevMove.checker.j - 2)) != null) {
                    moves.add(m);
                }
                if ((m = isCapturePossible(prevMove.checker, prevMove.checker.i + 2, prevMove.checker.j + 2)) != null) {
                    moves.add(m);
                }
            }
        }
        Random r = new Random();
        if (moves.size() > 0) {
            int toSleep = 500 + r.nextInt(500);
            try {
                java.lang.Thread.sleep(toSleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(AIGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            Collections.shuffle(moves);
            Move.sort(moves, this);
            int o = 0;
            moves.get(o).checker.i = moves.get(o).i;
            moves.get(o).checker.j = moves.get(o).j;
            remChecker(moves.get(o).removed);
            if (prevMove == null) {
                prevMove = moves.get(o);
            } else {
                moves.get(o).prev = prevMove;
                prevMove = moves.get(o);
            }
            moves.get(o).checker.adjust();
            return moves.get(o);
        }
        return null;
    }

    public boolean isThreatened(Checker c) {
        Move tmp = prevMove;
        prevMove = null;
        if (isAnyCapturePossible(team2)) {
            for (int i = 0; i < n; i++) {
                if (team2 == checkers[i].getCheckertype() && isCapturesPossible(checkers[i])) {
                    for (int row = 1; row < 9; row++) {
                        for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {
                            Move m = isCapturePossible(checkers[i], row, col);
                            if (m != null && m.removed == c) {
                                prevMove = tmp;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        prevMove = tmp;
        return false;
    }

    public boolean tempRemove(Checker c) {
        for (int i = 0; i < n; i++) {
            if (checkers[i] == c) {
                checkers[i] = checkers[n - 1];
                break;
            }
        }
        n--;
        for (int i = 0; i < n; i++) {
            if (checkers[i].getCheckertype() == c.getCheckertype()) {       //sprawdź czy przypadkiem
                return false;                                //zbity pion nie był ostatnim
            }                                                //pionem tej drużyny
        }
        return true;
    }

    public boolean makeMove(Move m) {
        m.checker.i = m.i;
        m.checker.j = m.j;
        return m.removed != null && tempRemove(m.removed);
    }

    public void undoMove(Move m) {
        m.checker.i = m.prev_i;
        m.checker.j = m.prev_j;
        if (m.removed != null) {
            addChecker(m.removed);
        }
    }

}
