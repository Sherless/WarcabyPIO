package Checkers.BoardAndGame;

import java.util.ArrayList;

/**
 *
 * @author Mateusz
 */
class Move {

    public Checker checker;
    public boolean wasKing;
    public int prev_i;
    public int prev_j;
    public int i;
    public int j;
    public Checker removed;
    int direction;
    public Move prev;

    public Move(Checker checker, int i, int j) {
        this.checker = checker;
        wasKing = checker.isKing();
        prev_i = checker.i;
        prev_j = checker.j;
        this.i = i;
        this.j = j;
        removed = null;
        prev = null;
    }

    public Move(Checker c, int i, int j, Checker removed) {
        this(c, i, j);
        this.removed = removed;
        if ((c.i - i == c.j - j) && c.i - i > 0) {
            direction = 1;
        }
        if ((c.i - i == j - c.j) && c.i - i > 0) {
            direction = 2;
        }
        if ((c.i - i == j - c.j) && c.i - i < 0) {
            direction = -2;
        }
        if ((c.i - i == c.j - j) && c.i - i < 0) {
            direction = -1;
        }
    }

    public static void sort(ArrayList<Move> moves, AIGame g) {
        int i, j;
        Move tmp;
        for (i = 1; i < moves.size(); i++) {
            tmp = moves.get(i);
            for (j = i; j > 0 && tmp.compareTo(moves.get(j - 1), g) < 0; j--) {
                moves.set(j, moves.get(j - 1));
            }
            moves.set(j, tmp);
        }
    }

    public int compareTo(Move m, AIGame g) {
        int a = 0;
        int b = 0;
        if (removed == null) {
            if (g.isThreatened(checker)) {
                g.makeMove(this);
                boolean isStillThreatened = g.isThreatened(checker);

                g.undoMove(this);
                if (!isStillThreatened) {
                    a = 1;
                }
            }
            if (g.isThreatened(m.checker)) {
                g.makeMove(m);
                boolean isStillThreatened = g.isThreatened(m.checker);
                g.undoMove(m);
                if (!isStillThreatened) {
                    b = 1;
                }
            }
            if (a != b) {           //lepszy ruch jeżeli uciekł od bicia
                return b - a;
            }

            g.makeMove(this);
            if (g.isThreatened(checker)) {
                a = -1;
            }
            g.undoMove(this);
            g.makeMove(m);
            if (g.isThreatened(m.checker)) {
                b = -1;
            }
            g.undoMove(m);
            if (a != b) {           //ruch gorszy jeżeli wystawia się do bicia
                return b - a;
            }

            g.makeMove(this);
            for (int i = 0; i < g.n; i++) {
                if (g.checkers[i].getCheckertype() == g.team2) {
                    if (g.isThreatened(g.checkers[i])) {
                        a++;
                    }
                }
            }
            g.undoMove(this);
            g.makeMove(m);
            for (int i = 0; i < g.n; i++) {
                if (g.checkers[i].getCheckertype() == g.team2) {
                    if (g.isThreatened(g.checkers[i])) {
                        a++;
                    }
                }
            }
            g.undoMove(m);
            if (a != b) {           //ruch lepszy jeżeli ustawia więcej pionków
                return b - a;       //przeciwnika na zagrożonej pozycji
            }

            if (!checker.isKing() && !m.checker.isKing()) {
                if (i != m.i) {
                    return m.i - i;         //ruch lepszy jeżeli dąży do damki
                }
            }
        } else {
            ArrayList<Checker> rems = new ArrayList<>(24);
            ArrayList<Move> posibilities = new ArrayList<>(13);

            if (g.makeMove(this)) {
                return -1;              //makeMove zwraca true jezeli usunięty
            }
            g.undoMove(this);           //został ostatni pion przeciwnika
            if (g.makeMove(m)) {
                return 1;
            }
            g.undoMove(m);

            g.makeMove(this);
            while (g.isCapturesPossible(checker)) {
                for (int row = 1; row < 9; row++) {
                    for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {
                        Move o = g.isCapturePossible(checker, row, col);
                        if (o != null && o.direction != -this.direction) {
                            posibilities.add(o);
                        }
                    }
                }
                sort(posibilities, g);
                g.makeMove(posibilities.get(0));
                rems.add(posibilities.get(0).removed);
                posibilities.clear();
                a++;
            }

            int c = 0;
            if (g.isThreatened(checker)) {
                c = -1;
            }
            for (int i = 0; i < a; i++) {
                g.addChecker(rems.get(i));
            }
            rems.clear();
            g.undoMove(this);

            g.makeMove(m);
            while (g.isCapturesPossible(m.checker)) {
                for (int row = 1; row < 9; row++) {
                    for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {
                        Move o = g.isCapturePossible(m.checker, row, col);
                        if (o != null && o.direction != -m.direction) {
                            posibilities.add(o);
                        }
                    }
                }
                sort(posibilities, g);
                g.makeMove(posibilities.get(0));
                rems.add(posibilities.get(0).removed);
                posibilities.clear();
                b++;
            }
            int d = 0;
            if (g.isThreatened(checker)) {
                d = -1;
            }
            for (int i = 0; i < b; i++) {
                g.addChecker(rems.get(i));
            }
            rems.clear();
            g.undoMove(m);

            if (a != b) {
                return b - a;     //krotność bicia
            }
            if (c != d) {
                return d - c;     //czy wylądował na zagrożonej pozycji
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        if (removed == null) {
            return "Ruch pionkiem " + checker.getCheckertype() + " z pola i = " + prev_i + " j = " + prev_j + " na pole i = " + i + " j = " + j;
        }
        return "Bicie pionkiem " + checker.getCheckertype() + " pionka " + removed.getCheckertype() + " (" + removed + ") z pola i = " + prev_i + " j = " + prev_j + " na pole i = " + i + " j = " + j;
    }
}
