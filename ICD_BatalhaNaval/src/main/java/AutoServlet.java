import socket.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import protocol.XMLUtils;

/**
 * Servlet implementation class AutoServlet
 */
@WebServlet("/AutoServlet")
public class AutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutoServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		
		try {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			String query 	  = request.getParameter("query");
			String numLetters = request.getParameter("num_letters");
			String numItems   = request.getParameter("num_items");

			if (query == null) query = "";
			int l = 0;
			if (numLetters != null) l = Integer.parseInt(numLetters);
			int i = 10;
			if (numItems != null) i = Integer.parseInt(numItems);
				
			// Pedir os nomes dos jogadores ao servidor
			String players = new User().sendRequestPlayers(query, l, i);
			String[] playersArray = XMLUtils.stringToArray(players);
			List<String> items = Arrays.asList(playersArray);  
			
			// Criar a tag option, para mostrar todos os resultados
			Iterator<String> iterator = items.iterator();	
			while (iterator.hasNext())
				out.write("<option>" + (String) iterator.next() + "</option>");
			
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
