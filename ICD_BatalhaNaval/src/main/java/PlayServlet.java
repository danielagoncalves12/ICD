import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import socket.User;

/**
 * Servlet implementation class DirectServlet
 */
@WebServlet("/PlayServlet")
public class PlayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PlayServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username"); // Identificador do jogador
		String position = request.getParameter("position"); // Posicao para jogar
		String result = null;								// Resultado

		try {
			result = new User().sendRequestPlay(username, position);
		} catch (IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}

		// Enviar os atributos no request
		request.getSession(true).setAttribute("username", username);
		request.getSession(true).setAttribute("result", (result != null) ? result : "");
		
		request.getRequestDispatcher("/game.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}