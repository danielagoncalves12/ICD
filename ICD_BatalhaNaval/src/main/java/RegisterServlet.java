import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import bean.Check;
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
		
		// Sessão
		HttpSession session = request.getSession();
		
		// Verificar se existe alguma entrada nos cookies
		String username = (String) session.getAttribute("username");
		
		if (username == null) {
			Cookie[] cookies = request.getCookies();
			
			if (cookies != null) 
				for (Cookie cookie : cookies) 
				   if (cookie.getName().equals("username")) 
					   username = cookie.getValue();
		}
		
		if (username != null) {
			if (!username.equals("")) {
				session.setAttribute("username", username);		
				session.setAttribute("name", Profile.getName(username));
				session.setAttribute("color", Profile.getColor(username));
				session.setAttribute("date", Profile.getDate(username));
				session.setAttribute("picture", Profile.getPicture(username));
				
				// TODO
				
				request.getRequestDispatcher("/index.jsp").forward(request, response);
				return;
			}
		}
		
		// Criar uma nova conta
		String newUsername = request.getParameter("new_username");
		String newName     = request.getParameter("new_name");
		String newPassword = request.getParameter("new_password");
		String newColor    = request.getParameter("new_color");
		String newDate     = request.getParameter("new_date");
		String newPicture  = request.getParameter("new_picture");
	
		// TODO UPLOAD de imagens
		
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User();
		
		String result = "";
		
		try {
			result = user.sendRequestLogin(newUsername, newName, newPassword, newColor, newDate, newPicture, true);
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
		
		if (newUsername != null && newName != null && newPassword != null && !result.substring(0, 4).equals("Erro")) {
			
			session.setAttribute("username", newUsername);		
			session.setAttribute("name", Profile.getName(newUsername));
			session.setAttribute("color", Profile.getColor(newUsername));
			session.setAttribute("date", Profile.getDate(newUsername));
			session.setAttribute("picture", Profile.getPicture(newUsername));
			
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
