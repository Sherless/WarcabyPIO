package Checkers.BoardAndGame;

import Checkers.GUI.CheckersGUI;
import Checkers.CheckerType;

/**
 *
 * @author Mateusz
 */
public class Game {

    protected CheckerType team1;
    protected CheckerType team2;
    protected CheckerType winner;
    protected Checker[] checkers;
    protected boolean inGame;
    protected int n;
    protected Move prevMove;  //zapamiętuje ostatnią sekwencję ruchów jednego gracza w przypadku
    //player vs player; oraz sekwencje ruchów gracza oraz komputera w przypadku player vs pc

    public Game(CheckerType team1, CheckerType team2) {
        this.team1 = team1;
        this.team2 = team2;
        checkers = new Checker[24];
        inGame = true;
        for (int i = 1; i < 9; i++) {
            if (i < 4) {
                for (int j = ((i % 2 == 1) ? 2 : 1); j < 9; j += 2) {
                    addChecker(new Checker(i, j, team1));
                }
            }
            if (i > 5) {
                for (int j = ((i % 2 == 0) ? 1 : 2); j < 9; j += 2) {
                    addChecker(new Checker(i, j, team2));
                }
            }
        }
        prevMove = null;
    }

    public final void addChecker(Checker c) {
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

    public void remChecker(Checker c) {
        for (int i = 0; i < n; i++) {
            if (checkers[i] == c) {
                checkers[i] = checkers[n - 1];
                break;
            }
        }
        n--;
        for (int i = 0; i < n; i++) {
            if (checkers[i].getCheckertype() == c.getCheckertype()) {       //sprawdź czy przypadkiem
                if (CheckersGUI.sound) {                                //zbity pion nie był ostatnim
                    CheckersGUI.playSound("boom.wav");                    //pionem tej drużyny
                }
                return;
            }
        }
        inGame = false;
        winner = checkers[0].getCheckertype();
        if (CheckersGUI.sound) {
            CheckersGUI.playSound("win.wav");
        }
    }

    public Checker isItOccupied(int row, int col) {     //sprawdź czy (row, col) jest wolne
        for (int i = 0; i < n; i++) {
            if (row == checkers[i].i && col == checkers[i].j) {
                return checkers[i];
            }
        }
        return null;
    }

    public boolean isMovePossible(Checker c, int i, int j) {
        if (i < 1 || i > 8 || j < 1 || j > 8) {     //wsp. poza planszą
            return false;
        }
        if (!c.isKing()) {        //zwykły pion - ruchy tylko do przodu o jeden po przekątnych
            if (c.getCheckertype() == team1 && (i != c.i + 1 || (j != c.j - 1 && j != c.j + 1))) {
                return false;
            }
            if (c.getCheckertype() == team2 && (i != c.i - 1 || (j != c.j - 1 && j != c.j + 1))) {
                return false;
            }
        } else {            //damka - ruchy o dowolnej ilości pól, po przekątnych
            if ((c.i - i == c.j - j) && c.i - i > 0) {  //nie wolno przeskakiwać pionów 
                int row = i + 1;
                int col = j + 1;                        //ruch w kierunku płn-zach
                Checker tmp;
                while (row < c.i && col < c.j) {
                    if ((isItOccupied(row, col)) != null) {
                        return false;
                    }
                    row += 1;
                    col += 1;
                }
                return isItOccupied(i, j) == null;      //jeżeli (i, j) wolne to ruch dozwolony
            } else if ((c.i - i == j - c.j) && c.i - i > 0) {  //ruch w kierunku płn-wsch
                int row = i + 1;
                int col = j - 1;
                Checker tmp;
                while (row < c.i && col > c.j) {
                    if ((isItOccupied(row, col)) != null) {
                        return false;
                    }
                    row += 1;
                    col -= 1;
                }
                return isItOccupied(i, j) == null;
            } else if ((c.i - i == j - c.j) && c.i - i < 0) {  //ruch w kierunku płd-zach
                int row = i - 1;
                int col = j + 1;
                Checker tmp;
                while (row > c.i && col < c.j) {
                    if ((isItOccupied(row, col)) != null) {
                        return false;
                    }
                    row -= 1;
                    col += 1;
                }
                return isItOccupied(i, j) == null;
            } else if ((c.i - i == c.j - j) && c.i - i < 0) {  //ruch w kierunku płd-wsch
                int row = i - 1;
                int col = j - 1;
                Checker tmp;
                while (row > c.i && col > c.j) {
                    if ((isItOccupied(row, col)) != null) {
                        return false;
                    }
                    row -= 1;
                    col -= 1;
                }
                return isItOccupied(i, j) == null;

            }
            return false;     //ruch w żaden z powyższych kierunków              
        }
        return isItOccupied(i, j) == null;  //sprawdź czy pole na które chce się ruszyć
    }                                       //zwykł pion jest wolne

    public Move isCapturePossible(Checker c, int i, int j) {
        Checker x = null;
        if (i < 1 || i > 8 || j < 1 || j > 8 || isItOccupied(i, j) != null) {
            return null;
        }
        if (prevMove != null && (prevMove.checker.getCheckertype() == c.getCheckertype() && prevMove.checker != c)) {
            return null;
        }
        if (!c.isKing()) {        //sprawdź czy bicie pionem jest możliwe w któymś kierunku
            if (c.i - i == 2) {
                if (c.j - j == 2) {
                    if ((x = isItOccupied(i + 1, j + 1)) != null && x.getCheckertype() != c.getCheckertype()) {
                        return new Move(c, i, j, x);
                    }
                }
                if (c.j - j == -2) {
                    if ((x = isItOccupied(i + 1, j - 1)) != null && x.getCheckertype() != c.getCheckertype()) {
                        return new Move(c, i, j, x);
                    }
                }
            }
            if (c.i - i == -2) {
                if (c.j - j == 2) {
                    if ((x = isItOccupied(i - 1, j + 1)) != null && x.getCheckertype() != c.getCheckertype()) {
                        return new Move(c, i, j, x);
                    }
                }
                if (c.j - j == -2) {
                    if ((x = isItOccupied(i - 1, j - 1)) != null && x.getCheckertype() != c.getCheckertype()) {
                        return new Move(c, i, j, x);
                    }
                }
            }
        } else {            //sprawdź czy bicie damką jest możliwe w któym z kierunków
            if ((c.i - i == c.j - j) && c.i - i > 0) {
                int row = i + 1;
                int col = j + 1;
                Checker tmp;
                while (row < c.i && col < c.j) {
                    if ((tmp = isItOccupied(row, col)) != null) {
                        if (tmp.getCheckertype() == c.getCheckertype()) {
                            return null;        //nie można przeskoczyć przez piona swojej drużyny
                        } else if (x == null) {
                            x = tmp;
                        } else {
                            return null;        //nie można przeskoczyć przez więcej niż 1 pion
                        }
                    }
                    row += 1;
                    col += 1;
                }
            } else if ((c.i - i == j - c.j) && c.i - i > 0) {
                int row = i + 1;
                int col = j - 1;
                Checker tmp;
                while (row < c.i && col > c.j) {
                    if ((tmp = isItOccupied(row, col)) != null) {
                        if (tmp.getCheckertype() == c.getCheckertype()) {
                            return null;
                        } else if (x == null) {
                            x = tmp;
                        } else {
                            return null;
                        }
                    }
                    row += 1;
                    col -= 1;
                }
            } else if ((c.i - i == j - c.j) && c.i - i < 0) {
                int row = i - 1;
                int col = j + 1;
                Checker tmp;
                while (row > c.i && col < c.j) {
                    if ((tmp = isItOccupied(row, col)) != null) {
                        if (tmp.getCheckertype() == c.getCheckertype()) {
                            return null;
                        } else if (x == null) {
                            x = tmp;
                        } else {
                            return null;
                        }
                    }
                    row -= 1;
                    col += 1;
                }
            } else if ((c.i - i == c.j - j) && c.i - i < 0) {
                int row = i - 1;
                int col = j - 1;
                Checker tmp;
                while (row > c.i && col > c.j) {
                    if ((tmp = isItOccupied(row, col)) != null) {
                        if (tmp.getCheckertype() == c.getCheckertype()) {
                            return null;
                        } else if (x == null) {
                            x = tmp;
                        } else {
                            return null;
                        }
                    }
                    row -= 1;
                    col -= 1;
                }
            }
            if (x == null) {
                return null;
            }
            Move m = new Move(c, i, j, x);
            if (prevMove != null && (prevMove.checker.getCheckertype() == c.getCheckertype() && m.direction == -prevMove.direction)) {
                return null;
            }
            return m;
        }
        return null;
    }

    public void isAnyMovePossible(CheckerType t) {
        for (int i = 0; i < n; i++) {
            if (t == checkers[i].getCheckertype()) {
                if (checkers[i].isKing()) {
                    for (int row = 1; row < 9; row++) {
                        for (int col = ((row % 2 == 1) ? 2 : 1); col < 9; col++) {
                            if (isMovePossible(checkers[i], row, col));
                            return;
                        }
                    }
                } else {
                    if (isMovePossible(checkers[i], checkers[i].i - 1, checkers[i].j - 1)) {
                        return;
                    }
                    if (isMovePossible(checkers[i], checkers[i].i - 1, checkers[i].j + 1)) {
                        return;
                    }
                    if (isMovePossible(checkers[i], checkers[i].i + 1, checkers[i].j - 1)) {
                        return;
                    }
                    if (isMovePossible(checkers[i], checkers[i].i + 1, checkers[i].j + 1)) {
                        return;
                    }
                }
            }
        }
        if (isAnyCapturePossible(t)) {
            return;
        }
        inGame = false;
        winner = (t == team1) ? team2 : team1;
        if (CheckersGUI.sound) {
            CheckersGUI.playSound("win.wav");
        }
    }

    public boolean isAnyCapturePossible(CheckerType t) {
        Checker x;
        for (int i = 0; i < n; i++) {       //sprawdź czy jakiekolwiek bicie jest możliwe
            if (t == checkers[i].getCheckertype()) {        //przez daną drużynę
                if (isCapturesPossible(checkers[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCapturesPossible(Checker c) {
        if (c.isKing()) {
            for (int i = 1; i < 9; i++) {       //srpawdź czy bicie damką jest możliwe
                for (int j = ((i % 2 == 1) ? 2 : 1); j < 9; j++) {   //dla każdeo pola
                    if (isCapturePossible(c, i, j) != null) {
                        return true;
                    }
                }
            }
            return false;
        }                   //sprawdź czy bicie pionem jest możliwe
        //dla standardowch pól (oddalonych o 2 i po przekątnych)
        if (isCapturePossible(c, c.i - 2, c.j - 2) != null) {
            return true;
        }
        if (isCapturePossible(c, c.i - 2, c.j + 2) != null) {
            return true;
        }
        if (isCapturePossible(c, c.i + 2, c.j - 2) != null) {
            return true;
        }
        if (isCapturePossible(c, c.i + 2, c.j + 2) != null) {
            return true;
        }

        return false;
    }

    public boolean undo(CheckerType whoseTurn) {         //cofnij ruch bądź sekwencję ruchów
        boolean differentTeams = false;
        while (prevMove != null) {
            if (whoseTurn != prevMove.checker.getCheckertype()) {
                differentTeams = true;              //zapamiętany jest ruch gracza, który stracił
            }
            if (prevMove.removed != null) //już swoją kolejkę. To znaczy, że 
            {
                addChecker(prevMove.removed);       //należy mu ją przywrócić
            }
            prevMove.checker.setKing(prevMove.wasKing);
            prevMove.checker.i = prevMove.prev_i;
            prevMove.checker.j = prevMove.prev_j;
            prevMove.checker.adjust();
            prevMove = prevMove.prev;       //sprawdź czy jest zapamiętany więcej niż jedne ruch
        }
        return differentTeams;              //true jeżeli należy zmienić whoseTurn
    }
}
