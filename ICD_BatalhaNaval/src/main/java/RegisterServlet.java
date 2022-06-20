
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
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
				request.getRequestDispatcher("/index.jsp").forward(request, response);
				return;
			}
		}
		
		// Criar uma nova conta
		String newUsername = "", newName = "", newPassword = "", newColor = "", newDate = "", newPicture = "";

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            return;
        }
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));

            for (FileItem item : items) {
            	
            	if (item.isFormField()) {
            		
            		if (item.getFieldName().equals("new_username")) newUsername = item.getString();
            		if (item.getFieldName().equals("new_name")) 	newName = item.getString();
            		if (item.getFieldName().equals("new_password")) newPassword = item.getString();
            		if (item.getFieldName().equals("new_color")) 	newColor = item.getString();
            		if (item.getFieldName().equals("new_date")) 	newDate = item.getString();
            	} 
            	
            	else if (item.getFieldName().equals("new_picture")) {
    		
                    String imgtype = item.getName().substring(item.getName().lastIndexOf("."));
                    String imgName = newUsername + imgtype;
                    newPicture = imgName;
                    item.write(new File("./src/main/webapp/pictures/", imgName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User();
		
		String result = "", honor = " ";
		
		// Enviar os pedidos
		try {
			result = user.sendRequestLogin(newUsername, newName, newPassword, newColor, newDate, newPicture, true);
			honor  = user.sendRequestHonorBoard();
		} catch (ParserConfigurationException | IOException e) {
			e.printStackTrace();
		}
		
		if (newUsername != null && newName != null && newPassword != null && !result.substring(0, 4).equals("Erro")) {
			
			// TODO
			/*session.setAttribute("username", newUsername);		
			session.setAttribute("name", Profile.getName(newUsername));
			session.setAttribute("color", Profile.getColor(newUsername));
			session.setAttribute("date", Profile.getDate(newUsername));
			session.setAttribute("picture", Profile.getPicture(newUsername));
			session.setAttribute("win_num", Profile.getWinNum(newUsername));*/
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
