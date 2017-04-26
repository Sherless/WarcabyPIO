
/**
 *
 * @author Mateusz
 */
public class CheckersGUI extends javax.swing.JFrame {

    public CheckersGUI() {
        super("Checkers");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Board board = new Board();
        for(int i = 1; i < 9; i++){
            if (i < 4) {
                for (int j = ((i%2==1) ? 2 : 1); j < 9; j+=2)
                    board.addChecker(new Checker(i, j, CheckerType.WHITE));
            }
            if (i > 5)
                for (int j = ((i%2==1) ? 1 : 2); j < 9; j+=2)
                    board.addChecker(new Checker(i, j, CheckerType.BLACK));
        }
        /*board.addChecker(new Checker(6, 3, CheckerType.BLACK));
        board.addChecker(new Checker(4, 1, CheckerType.RED));
        board.addChecker(new Checker(5, 6, CheckerType.WHITE));*/
        setContentPane(board);
        pack();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CheckersGUI().setVisible(true);
            }
        });
    }
}
