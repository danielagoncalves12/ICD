package tp1.battleship;

import java.util.HashMap;
import java.util.Map;

public class GameView {
	
	private Map<Integer, String> view = new HashMap<Integer, String>();

	public void convertSymbols() {
		
		view.put(ShipType.EMPTYHIDDEN, new String(" ")); // Por explorar (Sem navio)
	    view.put(ShipType.EMPTY, new String("."));       // Pressionado (Sem Sucesso)
	    
	    view.put(ShipType.TYPE1SHOW, new String("P"));   // Porta-aviões encontrado
	    view.put(ShipType.TYPE2SHOW, new String("N"));   // Navio-tanque encontrado
	    view.put(ShipType.TYPE3SHOW, new String("C"));   // Contratorpedeiro encontrado
	    view.put(ShipType.TYPE4SHOW, new String("S"));   // Submarino encontrado
	    
	    view.put(ShipType.TYPE1HIDDEN, new String(" ")); // Porta-aviões escondido
	    view.put(ShipType.TYPE2HIDDEN, new String(" ")); // Navio-tanque escondido
	    view.put(ShipType.TYPE3HIDDEN, new String(" ")); // Contratorpedeiro escondido
	    view.put(ShipType.TYPE4HIDDEN, new String(" ")); // Submarino escondido	
	}
	
	/** 
	 * Método printBoard
	 * Apresenta o tabuleiro do jogo.
	 */
	public String printBoard(int[][] board) {

		convertSymbols();
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		StringBuilder strBoard = new StringBuilder();

		// Apresentação das letras
		strBoard.append("    ");
		for (int i = 0; i < 10; i++) {
			strBoard.append(" " + letters[i] + " ");
		}
		strBoard.append("\n");

		// Apresentação da borda do tabuleiro
		strBoard.append("   X ");
		for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		
		// Apresentação do conteúdo do tabuleiro
		for (int i = 0; i < 10; i++) {
			
			strBoard.append(i+1 + " ");
			strBoard.append((i != 9) ? " |" : "|");

			for (int j = 0; j < 10; j++) { 
				strBoard.append(" " + view.get(board[i][j]) + " ");
				strBoard.append((j == 9) ? "|" : "");
			}
			strBoard.append("\n");
		}
		
		// Apresentação da borda do tabuleiro
		strBoard.append("   X ");
		for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		return strBoard.toString();
	}
}
