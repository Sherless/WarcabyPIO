import java.io.*;
import java.util.*;

public class PrototypWarcaby {

    private final static int SIZE = 8;
    private char[][] plansza; 
    private int biale; 
    private int czarne; 
    private char ktosierusza; 

    
    public PrototypWarcaby() {
	
	plansza = new char[SIZE][SIZE];
	biale = 12;
	czarne = 12;
	ktosierusza = 'c';

	
	int i,j;
	for (i=0;i<SIZE;i++)
	    for (j=0;j<SIZE;j++)
		plansza[i][j] = '_';

	for (i=1;i<SIZE;i+=2) {
	    plansza[i][1] = 'c';
	    plansza[i][5] = 'b';
	    plansza[i][7] = 'b';
	}
	for (i=0;i<SIZE;i+=2) {
	    plansza[i][0] = 'c';
	    plansza[i][2] = 'c';
	    plansza[i][6] = 'b';
	}
    }

    
    public void printPlansza() {
	int i,j;
	System.out.println("  1 2 3 4 5 6 7 8 x");
	for (i=0;i<SIZE;i++) {
	    System.out.print((i+1) + " ");
	    for (j=0;j<SIZE;j++) {
		System.out.print(plansza[j][i] + " ");
	    }
	    System.out.println();
	}
	System.out.println("y");
    }

    
    public void następnyRuch() throws IOException {
	
	Scanner stdin = new Scanner(System.in);

	if (ktosierusza=='c')
	    System.out.println("Tura czarnych!");
	else
	    System.out.println("Tura białych");

	boolean poprawny = false;
	
	while (!poprawny) {
	    
	    System.out.println("Wpisz współrzędne pionka którym chcesz się poruszyć.");
	    System.out.print("Wpisz jako 2 cyfrową liczbę np. jeżeli chcesz się poruszyć");
	    System.out.println(" x=1,y=3, wpisz 13");
	    int ruchz = stdin.nextInt();

	    System.out.print("Wpisz współrzędne miejsca w które pionek ma sie przesunąć ");
	    System.out.println("używając tej samiej konwencji co wcześniej43.");
	    int ruchdo = stdin.nextInt();

	    
	    if (poprawnyRuch(ruchz,ruchdo)) {
		poruszanie(ruchz,ruchdo);
		poprawny = true;
	    }
            else{
		System.out.println("Nie poprawny ruch ;( Spróbuj ponownie.");
                
            }
	}

	
	if (ktosierusza == 'c')
	    ktosierusza = 'b';
	else
	    ktosierusza = 'c';
    }

    
    public boolean poprawnyRuch(int ruchz, int ruchdo) {

	
	int xz = ruchz/10 - 1;
	int xdo = ruchdo/10 - 1;
	int yz = ruchz%10 - 1;
        int ydo = ruchdo%10 - 1;
	
	if (xz < 0 || xz > 7 || yz < 0 || yz > 7 ||
	    xdo < 0 || xdo > 7 || ydo < 0 || ydo > 7) 
	    return false;

	
	else if (plansza[xz][yz]==ktosierusza && plansza[xdo][ydo]=='_') {

	    
	    if (Math.abs(xz-xdo)==1) {
		if ((ktosierusza == 'c') && (ydo - yz == 1))
		    return true;
		else if ((ktosierusza == 'b') && (ydo - yz == -1))
		    return true;
	    }
	    
	    
	    else if (Math.abs(xz-xdo)==2) {
		if (ktosierusza == 'c' && (ydo - yz == 2) && 
		    plansza[(xz+xdo)/2][(yz+ydo)/2] == 'b')
		    return true;
		else if (ktosierusza == 'b' && (ydo - yz == -2) && 
		    plansza[(xz+xdo)/2][(yz+ydo)/2] == 'c')
		    return true;
	    }
	}
	
	return false;
    }

   
    public void poruszanie(int ruchz, int ruchdo) {
	
	int xz = ruchz/10 - 1;
	int xdo = ruchdo/10 - 1;
        int yz = ruchz%10 - 1;
	int ydo = ruchdo%10 - 1;
	
	
	plansza[xz][yz] = '_';
	plansza[xdo][ydo] = ktosierusza;
	if (Math.abs(xdo - xz) == 2) {
	    plansza[(xz+xdo)/2][(yz+ydo)/2] = '_';
	    if (ktosierusza == 'c')
		biale--;
	    else
		czarne--;
	}

    }

    
    public boolean koniecGry() {
	return (biale == 0 || czarne == 0);
    }

    // Returns color of the winner.
    public String zwycięzca() {
	if (czarne == 0)
	    return "czarne";
	else
	    return "białe";
    }

    public static void main(String args[]) throws IOException {

	
	PrototypWarcaby gra = new PrototypWarcaby();
	gra.printPlansza();
	
	
	while (!gra.koniecGry()) {
	    
	    gra.następnyRuch();
	    gra.printPlansza();
	}
	
	System.out.println("Zwyciężają: " + gra.zwycięzca());
    }
}
