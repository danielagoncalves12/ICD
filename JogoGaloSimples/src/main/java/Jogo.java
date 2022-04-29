import java.io.IOException;
import java.util.Scanner;

/**
 * @author Porfírio Filipe
 *
 */
public class Jogo {
	char[][] tabuleiro = { { '1', '2', '3' }, { '4', '5', '6' }, { '7', '8', '9' } }; // 3X3

	Jogo() {

	}

	public boolean vitoria(char simbolo) {
		return tabuleiro[0][0] == simbolo && tabuleiro[1][0] == simbolo && tabuleiro[2][0] == simbolo
				|| tabuleiro[0][1] == simbolo && tabuleiro[1][1] == simbolo && tabuleiro[2][1] == simbolo
				|| tabuleiro[0][2] == simbolo && tabuleiro[1][2] == simbolo && tabuleiro[2][2] == simbolo ||

				tabuleiro[0][0] == simbolo && tabuleiro[0][1] == simbolo && tabuleiro[0][2] == simbolo
				|| tabuleiro[1][0] == simbolo && tabuleiro[1][1] == simbolo && tabuleiro[1][2] == simbolo
				|| tabuleiro[2][0] == simbolo && tabuleiro[2][1] == simbolo && tabuleiro[2][2] == simbolo ||

				tabuleiro[0][0] == simbolo && tabuleiro[1][1] == simbolo && tabuleiro[2][2] == simbolo
				|| tabuleiro[0][2] == simbolo && tabuleiro[1][1] == simbolo && tabuleiro[2][0] == simbolo;

	}

	// transforma o jogo em texto
	public String JogoToTXT() {
		String out = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				out = out + tabuleiro[i][j];
				if (j < 2)
					out = out + '|';
			}
			out = out + "\n";
			if (i < 2)
				out = out + "------\n";
		}
		return out;
	}

	// deteta se o tabuleiro está preenchido
	public boolean empate() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (tabuleiro[i][j] != 'X' && tabuleiro[i][j] != 'O')
					return false;
		return true;
	}

	// mostra o jogo no ecrã
	public void mostrar() {

		System.out.println(JogoToTXT());
		// System.out.println(JogoToXML());
	}

	// assinala a jogada 'simbolo' na casa assinala pelo 'numero' (1..9)
	// retorna verdadeiro se a jogada é válida
	public boolean jogar(short numero, char simbolo) {
		if (numero > 9 || numero < 1)
			return false; // salta a jogada
		numero--;
		int linha = numero / 3;
		int coluna = numero % 3;
		if (tabuleiro[linha][coluna] == 'X' || tabuleiro[linha][coluna] == 'O')
			return false; // casa ocupada
		tabuleiro[linha][coluna] = simbolo;
		return true;
	}

	public static void main(String[] args) throws IOException {
		Jogo j = new Jogo();
		try (Scanner in = new Scanner(System.in)) {
			j.mostrar();
			for (;;) {
				System.out.println("\nJoga X:\n");
				j.jogar(in.nextShort(), 'X');
				j.mostrar();
				if (j.vitoria('X')) {
					System.out.println("\nVitoria do X!\n");
					break;
				} else if (j.empate()) {
					System.out.println("\nEmpate!\n");
					break;
				}
				System.out.println("\nJoga O:\n");
				j.jogar(in.nextShort(), 'O');
				j.mostrar();
				if (j.vitoria('O')) {
					System.out.println("\nVitoria do O\n");
					break;
				} else if (j.empate()) {
					System.out.println("\nEmpate!\n");
					break;
				}

			}
		}
	}
}
