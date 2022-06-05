import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import session.Profile;
import socket.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("new_username");
		String name     = request.getParameter("new_name");
		String password = request.getParameter("new_password");
		String picture  = request.getParameter("new_picture");

		// TODO UPLOAD de imagens
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User();
		
		String result = "";
		
		try {
			result = user.sendRequestLogin(username, name, password, picture, true);
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
		
		if (username != null && name != null && password != null && !result.substring(0, 4).equals("Erro")) {
			
			session.setAttribute("username", username);		
			session.setAttribute("name", Profile.getName(username));
			session.setAttribute("picture", Profile.getPicture(username));
			
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		else {
			session.invalidate();
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}

}
