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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("input_username");
		String password = request.getParameter("input_password");

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String result = "";
		
		if (user == null) user = new User();
		
		try {
			result = user.sendRequestLogin(username, "", password, "", false);
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}

		if (username != null && password != null && !result.substring(0, 4).equals("Erro")) {
			
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
