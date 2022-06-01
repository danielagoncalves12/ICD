

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
		/**
		 * Invalida o estado da sess�o
		 * https://www.javatpoint.com/servlet-http-session-login-and-logout-example
		 * 
		 */
	private static final long serialVersionUID = 1L;

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();
			
			HttpSession session=request.getSession();
			session.invalidate();
			
			out.print("You are successfully logged out!");
			
			out.close();
	}

	

}
