package battleship;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import protocol.XMLUtils;

public class GameView {
	
	private static Map<String, String> symbols = new HashMap<String, String>();

	public static void convertSymbols() {
		
		symbols.put(String.valueOf(ShipType.EMPTYHIDDEN), new String(" ")); // Por explorar (Sem navio)
		symbols.put(String.valueOf(ShipType.EMPTY), new String("x"));       // Pressionado (Sem Sucesso)
	    
		symbols.put(String.valueOf(ShipType.TYPE1SHOW), new String("P"));   // Porta-avi�es encontrado
		symbols.put(String.valueOf(ShipType.TYPE2SHOW), new String("N"));   // Navio-tanque encontrado
		symbols.put(String.valueOf(ShipType.TYPE3SHOW), new String("C"));   // Contratorpedeiro encontrado
		symbols.put(String.valueOf(ShipType.TYPE4SHOW), new String("S"));   // Submarino encontrado
	    
		symbols.put(String.valueOf(ShipType.TYPE2HIDDEN), new String("Q")); // Navio-tanque escondido
		symbols.put(String.valueOf(ShipType.TYPE3HIDDEN), new String("W")); // Contratorpedeiro escondido
		symbols.put(String.valueOf(ShipType.TYPE4HIDDEN), new String("O")); // Submarino escondido	
		symbols.put(String.valueOf(ShipType.TYPE1HIDDEN), new String("R")); // Porta-avi�es escondido
		
		symbols.put(String.valueOf(ShipType.TYPESHOOT), new String("X"));	   // Tiro num navio
		symbols.put(String.valueOf(ShipType.TYPESHOOTEMPTY), new String("Y")); // Tiro falha
	}
	/*
	public static String viewBoard(String board) {
		
		// Numeros e Letras do tabuleiro
		
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
		
		// Espacos, navios e tiros
		
		board = board.replaceAll("(\r\n|\n)", "<br>");
		board = board.replaceAll("x", "<img src='resources/found_empty.png'/>");
		
		//board = board.replaceAll(" ", "<button type='submit' style='background:url(resources/not_found.png); width:36px; height:36px; display:in-line; color: inherit;"
		//		+ "border: none; padding: 0; font: inherit; cursor: pointer; outline: inherit;' name='position' type='submit' value='5F'></button>");
		
		board = board.replaceAll(" ", "<img src='resources/not_found.png'/>");
		board = board.replaceAll("(C|S|P|N)", "<img src='resources/ship.png'/>");
		board = board.replaceAll("(Q|W|O|R)", "<img src='resources/not_found.png'/>");	

		return board;
	}*/
	
	public static void main(String[] args) {
		
		String[][] array = new String[10][10];
		array[0] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[1] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[2] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[3] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[4] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[5] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[6] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[7] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[8] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		array[9] = new String[] {"0","0","0","0","0","0","0","0","0","0"};
		
		viewBoard(Arrays.deepToString(array));
	}
	
	// Ver o tabuleiro adversario
	public static String viewBoard(String boardStr) {

		String[][] board = XMLUtils.stringToArray2D(boardStr);
		StringBuilder output = new StringBuilder();

		String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		String[] letters = {"0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		
		for (String letter : letters) {
			output.append("<img src='resources/" + letter + ".png' style='width: 36px !important; height:36px !important;'/>");
		}
		output.append("<br>");
		
		for (int i = 0; i < 10; i++) {
			
			output.append("<img src='resources/" + numbers[i] + ".png' style='width: 36px !important; height:36px !important;'/>");
			for (int j = 0; j < 10; j++) {
			
				String number = board[i][j];
				
				// Clicavel
				
				if (number.equals("0"))
					output.append("<input onclick='buttonClick()' type='submit' name='position' value='" + numbers[i] + letters[j+1] + "' style='box-sizing: border-box; background-image: url(resources/not_found.png); opacity: 0.6; font-size:0px; border:none; width: 36px; height:36px;'/>");		
				
				if (number.equals("6") || number.equals("7") || number.equals("8") || number.equals("9"))
					output.append("<img src='resources/not_found.png' style='width: 36px !important; height:36px !important;'/>");
				
				// Nao clicavel
				
				if (number.equals("2") || number.equals("3") || number.equals("4") || number.equals("5"))
					output.append("<img src='resources/ship.png' style='width: 36px !important; height:36px !important;'/>");
				
				if (number.equals("1"))
					output.append("<img src='resources/found_empty.png' style='width: 36px !important; height:36px !important;'/>");
				
			}
			output.append("<br>");
		}
		return output.toString();
	}
	
	// Ver o proprio tabuleiro
	public static String viewBoardView(String boardStr) {

		String[][] board = XMLUtils.stringToArray2D(boardStr);
		StringBuilder output = new StringBuilder();

		String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
		String[] letters = {"0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		
		for (String letter : letters) {
			output.append("<img src='resources/" + letter + ".png'/>");
		}
		output.append("<br>");

		for (int i = 0; i < 10; i++) {
			
			output.append("<img src='resources/" + numbers[i] + ".png'/>");
			for (int j = 0; j < 10; j++) {
			
				String number = board[i][j];
				
				if (number.equals("0"))
					output.append("<img src='resources/not_found.png'/>");
				
				if (number.equals("1"))
					output.append("<img src='resources/found_empty.png'/>");
				
				if (number.equals("2") || number.equals("3") || number.equals("4") || number.equals("5"))
					output.append("<img src='resources/ship.png'/>");
				
				if (number.equals("6") || number.equals("7") || number.equals("8") || number.equals("9"))
					output.append("<img src='resources/target_ship.png'/>");
				
				if (number.equals("10"))
					output.append("<img src='resources/target_ship.png'/>");
					
				if (number.equals("11"))
					output.append("<img src='resources/target_empty.png'/>");
				
			}
			output.append("<br>");
		}
		return output.toString();
	}
	
		/*
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
		

		return board; */
	
	/** 
	 * M�todo printBoard
	 * Apresenta o tabuleiro do jogo.
	 */
	public static String printBoard(String boardString) {
		
		String[][] board = XMLUtils.stringToArray2D(boardString);
		
		convertSymbols();
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
		StringBuilder strBoard = new StringBuilder();

		// Apresentacao das letras
		strBoard.append("0 ");
		for (int i = 0; i < 10; i++) {
			strBoard.append(" " + letters[i] + " ");
		}
		strBoard.append("\n");

		// Apresentacao da borda do tabuleiro
		//strBoard.append("   X ");
		//for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
			
		// Apresentacao do conteudo do tabuleiro
		for (int i = 0; i < 10; i++) {
			
			strBoard.append(i+1 + " ");
			//strBoard.append((i != 9) ? " |" : "|");

			for (int j = 0; j < 10; j++) { 
				strBoard.append(" " + symbols.get(board[i][j]) + " ");
				//strBoard.append((j == 9) ? "|" : "");
			}
			strBoard.append("\n");
		}
		
		// Apresentacao da borda do tabuleiro
		//strBoard.append("   X ");
		//for (int i = 0; i <= 28; i++) strBoard.append((i != 28) ? "-" : " X\n");
		
		return strBoard.toString();
	}
}
