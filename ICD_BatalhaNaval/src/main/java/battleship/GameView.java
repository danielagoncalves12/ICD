package battleship;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameView {
	
	private static Map<Integer, String> symbols = new HashMap<Integer, String>();

	public static void convertSymbols() {
		
		symbols.put(ShipType.EMPTYHIDDEN, new String(" ")); // Por explorar (Sem navio)
		symbols.put(ShipType.EMPTY, new String("x"));       // Pressionado (Sem Sucesso)
	    
		symbols.put(ShipType.TYPE1SHOW, new String("P"));   // Porta-aviões encontrado
		symbols.put(ShipType.TYPE2SHOW, new String("N"));   // Navio-tanque encontrado
		symbols.put(ShipType.TYPE3SHOW, new String("C"));   // Contratorpedeiro encontrado
		symbols.put(ShipType.TYPE4SHOW, new String("S"));   // Submarino encontrado
	    
		symbols.put(ShipType.TYPE2HIDDEN, new String("Q")); // Navio-tanque escondido
		symbols.put(ShipType.TYPE3HIDDEN, new String("W")); // Contratorpedeiro escondido
		symbols.put(ShipType.TYPE4HIDDEN, new String("O")); // Submarino escondido	
		symbols.put(ShipType.TYPE1HIDDEN, new String("R")); // Porta-aviões escondido
		
		symbols.put(ShipType.TYPESHOOT, new String("X"));	   // Tiro num navio
		symbols.put(ShipType.TYPESHOOTEMPTY, new String("Y")); // Tiro falha
	}
	
	public static String viewBoard(String board) {
		
		String path = "<img src='resources/dez.png' />";
		board = board.replaceFirst("10", path);
		
		String[] valuesToChange1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (String str : valuesToChange1) {
			path = "<img src='resources/" + str + ".png' />";
			board = board.replaceAll(str, path);
		}
		
		path = "<img src='resources/corner.png' />";
		board = board.replaceFirst("0", path);
		
		String[] valuesToChange2 = {"A", "B", "D", "E", "F", "G", "H", "I", "J"};
		
		for (String str : valuesToChange2) {
			path = "<img src='resources/" + str + ".png' />";
			board = board.replaceAll(str, path);		
		}
		
		path = "<img src='resources/num.png'/>";
		board = board.replaceFirst("C", path); 
		
		board = board.replaceAll("(\r\n|\n)", "<br>");
		board = board.replaceAll("x", "<img src='resources/found_empty.png'/>");
		board = board.replaceAll(" ", "<img src='resources/not_found.png'/>");
		board = board.replaceAll("(C|S|P|N)", "<img src='resources/ship.png'/>");
		board = board.replaceAll("(Q|W|O|R)", "<img src='resources/not_found.png'/>");	

		return board;
	}
	
	public static String viewBoardView(String board) {

		String path = "<img src='resources/dez.png' />";
		board = board.replaceFirst("10", path);
		
		String[] valuesToChange1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		
		for (String str : valuesToChange1) {
			path = "<img src='resources/" + str + ".png' />";
			board = board.replaceAll(str, path);
		}
		
		path = "<img src='resources/corner.png' />";
		board = board.replaceFirst("0", path);
		
		String[] valuesToChange2 = {"A", "B", "D", "E", "F", "G", "H", "I", "J"};
		
		for (String str : valuesToChange2) {
			path = "<img src='resources/" + str + ".png' />";
			board = board.replaceAll(str, path);		
		}
		
		path = "<img src='resources/num.png'/>";
		board = board.replaceFirst("C", path); 
		
		board = board.replaceAll("(\r\n|\n)", "<br>");
		board = board.replaceAll("x", "<img src='resources/found_empty.png'/>");
		board = board.replaceAll(" ", "<img src='resources/not_found.png'/>");
		
		board = board.replaceAll("X", "<img src='resources/target_ship.png'/>");
		board = board.replaceAll("Y", "<img src='resources/target_empty.png'/>");
		board = board.replaceAll("(C|S|P|N)", "<img src='resources/ship.png'/>");
		board = board.replaceAll("(Q|W|O|R)", "<img src='resources/target_ship.png'/>");

		return board;
	}
	
	/** 
	 * Método printBoard
	 * Apresenta o tabuleiro do jogo.
	 */
	public static String printBoard(String player, String view, String pointsPlayer1, String pointsPlayer2, int[][] board) {
			
		convertSymbols();
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		StringBuilder strBoard = new StringBuilder();

		// Apresentação das letras
		strBoard.append("0 ");
		for (int i = 0; i < 10; i++) {
			strBoard.append(" " + letters[i] + " ");
		}
		strBoard.append("\n");

		// Apresentação da borda do tabuleiro
		//strBoard.append("   X ");
		//for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
			
		// Apresentação do conteúdo do tabuleiro
		for (int i = 0; i < 10; i++) {
			
			strBoard.append(i+1 + " ");
			//strBoard.append((i != 9) ? " |" : "|");

			for (int j = 0; j < 10; j++) { 
				strBoard.append(" " + symbols.get(board[i][j]) + " ");
				//strBoard.append((j == 9) ? "|" : "");
			}
			strBoard.append("\n");
		}
		
		// Apresentação da borda do tabuleiro
		//strBoard.append("   X ");
		//for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		return strBoard.toString();
	}
}
