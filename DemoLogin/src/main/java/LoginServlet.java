import bean.UserDataStore;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String name=request.getParameter("j_username");
		String password=request.getParameter("j_password");

		if(name!=null && password!=null && UserDataStore.getInstance().isLoginCorrect(name, password)){
			HttpSession session=request.getSession();
			session.setAttribute("name",name);
			request.getRequestDispatcher("/").forward(request, response);
		}
		else
			request.getRequestDispatcher("/login.html").forward(request, response);
	}

}
