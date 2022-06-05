import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FileDownload")
public class FileDownload extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// reads input file from path absolute path
		String filePath = request.getParameter("file");
		if (filePath == null)
			filePath = "isel.jpg";

		String completePath = bean.Dir.getDir(request) + filePath;
		System.out.println("Vai descarregar: " + completePath);
		File downloadFile = new File(completePath);
		FileInputStream inStream;
		try {
			inStream = new FileInputStream(downloadFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Erro no acesso ao ficheiro em: " + completePath);
			return;
		}

		// gets MIME type of the file
		String mimeType = getServletContext().getMimeType(completePath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}
		//System.out.println("MimeType: " + mimeType);
		// modifies response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// forces download
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// obtains response's output stream
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		while ((bytesRead = inStream.read(buffer)) != -1) 
			outStream.write(buffer, 0, bytesRead);

		inStream.close();
		outStream.close();
	}
}
