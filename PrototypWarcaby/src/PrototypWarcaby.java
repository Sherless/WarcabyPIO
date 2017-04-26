import java.io.*;
import java.util.*;

public class PrototypWarcaby {

    private final static int SIZE = 8;
    private char[][] board; // Stores the checkerboard, with chars 'r','b','_'
    private int redcheckers; // Number of red checkers on the board
    private int blackcheckers; // Number of black checkers on the board
    private char whosemove; // Either 'r' or 'b', for who's move it currently is.

    // Constructs default Checkers object, initializing board to starting
    // playing position.
    public PrototypWarcaby() {
	
	board = new char[SIZE][SIZE];
	redcheckers = 12;
	blackcheckers = 12;
	whosemove = 'r';

	// Initialize board with all the red and black checkers in starting
	// positions.
	int i,j;
	for (i=0;i<SIZE;i++)
	    for (j=0;j<SIZE;j++)
		board[i][j] = '_';

	for (i=1;i<SIZE;i+=2) {
	    board[i][1] = 'r';
	    board[i][5] = 'b';
	    board[i][7] = 'b';
	}
	for (i=0;i<SIZE;i+=2) {
	    board[i][0] = 'r';
	    board[i][2] = 'r';
	    board[i][6] = 'b';
	}
    }

    // Print out the checkerboard, by looping through all board positions in
    // appropriate order.
    public void printBoard() {
	int i,j;
	System.out.println("  1 2 3 4 5 6 7 8 x");
	for (i=0;i<SIZE;i++) {
	    System.out.print((i+1) + " ");
	    for (j=0;j<SIZE;j++) {
		System.out.print(board[j][i] + " ");
	    }
	    System.out.println();
	}
	System.out.println("y");
    }

    // This method executes one move.
    public void getNextMove() throws IOException {
	
	Scanner stdin = new Scanner(System.in);

	if (whosemove=='r')
	    System.out.println("It is your turn, red.");
	else
	    System.out.println("It is your turn, black.");

	boolean moved = false;
	// Loops until legal move is entered.
	while (!moved) {
	    // Reads in square to move from and to.
	    System.out.println("Enter from the square you would like to move from.");
	    System.out.print("Enter as a 2-digit number. (e.g. if you were moving from");
	    System.out.println(" x=1,y=3, enter 13");
	    int movefrom = stdin.nextInt();

	    System.out.print("Enter from the square you would like to move to, ");
	    System.out.println("using the same convention.");
	    int moveto = stdin.nextInt();

	    // Checks to see if move is valid, if so, executes it.
	    if (validMove(movefrom,moveto)) {
		executeMove(movefrom,moveto);
		moved = true;
	    }
	    else
		System.out.println("That was an invalid move, try again.");
	}

	// Update whosemove it is.
	if (whosemove == 'r')
	    whosemove = 'b';
	else
	    whosemove = 'r';
    }

    // Checks if a move is valid.
    public boolean validMove(int movefrom, int moveto) {

	// Gets array indeces corresponding to the move, from parameters.
	int xfrom = movefrom/10 - 1;
	int yfrom = movefrom%10 - 1;
	int xto = moveto/10 - 1;
	int yto = moveto%10 - 1;
	
	// Check if indeces in range, if not, return false.
	if (xfrom < 0 || xfrom > 7 || yfrom < 0 || yfrom > 7 ||
	    xto < 0 || xto > 7 || yto < 0 || yto > 7) 
	    return false;

	// Check to see you are moving your piece to a blank square.
	else if (board[xfrom][yfrom]==whosemove && board[xto][yto]=='_') {

	    // Checks case of simple move
	    if (Math.abs(xfrom-xto)==1) {
		if ((whosemove == 'r') && (yto - yfrom == 1))
		    return true;
		else if ((whosemove == 'b') && (yto - yfrom == -1))
		    return true;
	    }
	    
	    // Checks case of a jump
	    else if (Math.abs(xfrom-xto)==2) {
		if (whosemove == 'r' && (yto - yfrom == 2) && 
		    board[(xfrom+xto)/2][(yfrom+yto)/2] == 'b')
		    return true;
		else if (whosemove == 'b' && (yto - yfrom == -2) && 
		    board[(xfrom+xto)/2][(yfrom+yto)/2] == 'r')
		    return true;
	    }
	}
	// If move is neither a simple one or a jump, it is not legal.
	return false;
    }

    // Executes a move.
    public void executeMove(int movefrom, int moveto) {
	// Gets array indeces corresponding to the move, from parameters.
	int xfrom = movefrom/10 - 1;
	int yfrom = movefrom%10 - 1;
	int xto = moveto/10 - 1;
	int yto = moveto%10 - 1;
	
	// Change appropriate board elements and decrement redcheckers or
	// blackcheckers if necessary.
	board[xfrom][yfrom] = '_';
	board[xto][yto] = whosemove;
	if (Math.abs(xto - xfrom) == 2) {
	    board[(xfrom+xto)/2][(yfrom+yto)/2] = '_';
	    if (whosemove == 'r')
		redcheckers--;
	    else
		blackcheckers--;
	}

    }

    // Checks to see if game is over based on number of checkers left.
    public boolean gameOver() {
	return (redcheckers == 0 || blackcheckers == 0);
    }

    // Returns color of the winner.
    public String winnerIs() {
	if (blackcheckers == 0)
	    return "red";
	else
	    return "black";
    }

    public static void main(String args[]) throws IOException {

	// Setup and print out checker board.
	PrototypWarcaby game = new PrototypWarcaby();
	game.printBoard();
	
	// Loop until game is over.
	while (!game.gameOver()) {
	    //Execute a move and print the board out afterwards.
	    game.getNextMove();
	    game.printBoard();
	}
	// Announce winner.
	System.out.println("The winner is " + game.winnerIs());
    }
}