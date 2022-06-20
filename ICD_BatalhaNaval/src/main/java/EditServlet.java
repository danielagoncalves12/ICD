
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
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
	
		HttpSession session = request.getSession(true);
		User user = (User) session.getAttribute("user");
		if (user == null) user = new User();
	
		String username = "", newName = "", newColor = "", newDate = "", newPicture = "";
		
		try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));

            for (FileItem item : items) {
            	
            	if (item.isFormField()) {
            		
            		if (item.getFieldName().equals("username"))  username = item.getString();
            		if (item.getFieldName().equals("new_name"))  newName = item.getString();
            		if (item.getFieldName().equals("new_color")) newColor = item.getString();
            		if (item.getFieldName().equals("new_date"))  newDate = item.getString();
            	} 
            	
            	else if (item.getFieldName().equals("new_picture")) {
    		
            		if (item.getSize() > 0) {
            		
            			byte[] bytes = item.get();
                        newPicture = Base64.getEncoder().encodeToString(bytes);
            		}             	
                }
            }

			// Atualizar o nome publico	
			if (newName != null) if (!newName.equals("")) newName = user.sendRequestUpload("Name", username, newName);
				
			// Atualizar a cor
			if (newColor != null) if (!newColor.equals("")) newColor = user.sendRequestUpload("Color", username, newColor);
	
			// Atualizar a data de nascimento
			if (newDate != null) if (!newDate.equals("")) newDate = user.sendRequestUpload("Date", username, newDate);

			// Atualizar a fotografia
			if (newPicture != null) if (!newPicture.equals("")) newPicture = user.sendRequestUpload("Picture", username, newPicture);
	
        } catch (Exception e) {
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
