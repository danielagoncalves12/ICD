import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import protocol.XMLUtils;
import session.Profile;
import socket.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("input_username");
		String password = request.getParameter("input_password");

		// Verificar se existe alguma entrada nos cookies
		
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		String result = "", honor = "";
		if (user == null) user = new User();

		try {		
			result = user.sendRequestLogin(username, "", password, "", "", "", false);
			honor  = user.sendRequestHonorBoard();
			
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}

		if (username != null && password != null && !result.substring(0, 4).equals("Erro")) {
			
			session.setAttribute("username", username);
			session.setAttribute("name", Profile.getName(username));
			session.setAttribute("color", Profile.getColor(username));
			session.setAttribute("date", Profile.getDate(username));
			session.setAttribute("picture", Profile.getPicture(username));
			session.setAttribute("win_num", Profile.getWinNum(username));
			session.setAttribute("honor", honor);
			
			Cookie cookie = new Cookie("username", username);
			response.addCookie(cookie);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		else {
			session.invalidate();
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}
	}
}
