package tp1.battleship;

import java.util.HashMap;
import java.util.Map;

public class GameView {
	
	private Map<Integer, String> view = new HashMap<Integer, String>();

	public void printBorder() {
		
		System.out.print("   X ");
		for (int i = 0; i <= 28; i++) {
			System.out.print((i != 28) ? "-" : " X\n");
		}
	}
	
	/** 
	 * M�todo printBoard
	 * Apresenta o tabuleiro do jogo.
	 */
	public void printBoard(int[][] board) {
		
		view.put(0, new String(" ")); // Por explorar (Sem navio)
	    view.put(1, new String(" ")); // Por explorar (Com navio)
	    view.put(2, new String("X")); // Pressionado (Sucesso)
	    view.put(3, new String(".")); // Pressionado (Sem sucesso)
		
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		
		// Apresenta��o das letras
		System.out.print("    ");
		for (int i = 0; i < 10; i++) {
			System.out.print(" " + letters[i] + " ");
		}
		System.out.println();

		// Apresenta��o da borda do tabuleiro
		printBorder();
		
		// Apresenta��o do conte�do do tabuleiro
		for (int i = 0; i < 10; i++) {
			
			System.out.print(i+1 + " ");
			System.out.print((i != 9) ? " |" : "|");
			for (int j = 0; j < 10; j++) { 
			
				String value = view.get(board[i][j]);
				System.out.print(" " + value + " ");
				System.out.print((j == 9) ? "|" : "");
			}
			System.out.println();
		}
		
		// Apresenta��o da borda do tabuleiro
		printBorder();
	}
}
