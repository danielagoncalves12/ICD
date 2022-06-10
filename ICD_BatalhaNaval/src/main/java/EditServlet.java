

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import protocol.MessageProcessor;
import socket.User;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String username = request.getParameter("username");
		String name    = request.getParameter("new_name");
		String color   = request.getParameter("new_color");
		String picture = request.getParameter("new_picture");
		String date    = request.getParameter("new_date");
		
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User();
	
		try {
			// Atualizar o nome publico	
			if (name != null) {
				if (!name.equals("")) {
					name = user.sendRequestUpload("Name", username, name);
					request.setAttribute("name", name);
				}
			}
				
			// Atualizar a cor
			if (color != null) {
				if (!color.equals("")) {
					color = user.sendRequestUpload("Color", username, color);
					request.setAttribute("color", color);
				}
			}
			
			// Atualizar a fotografia
			if (picture != null) {
				if (!picture.equals("")) {
					picture = user.sendRequestUpload("Picture", username, picture);
					request.setAttribute("picture", picture);
				}
			}
			
			// Atualizar a data de nascimento
			if (date != null) {
				if (!date.equals("")) {
					date = user.sendRequestUpload("Date", username, date);
					request.setAttribute("date", date);
				}
			}
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("/index.jsp").forward(request, response);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
