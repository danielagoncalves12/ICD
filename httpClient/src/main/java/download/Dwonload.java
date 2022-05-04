package download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
//import java.security.Security;

public class Dwonload {
	public static void main(String[] args) {
		// para funcionar com https 
		/*System.setProperty("java.protocol.handler.pkgs",
			       "com.sun.net.ssl.internal.www.protocol");
			  Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());*/
			  
		File file = new File("HorarioLEIM.pdf");
		URL url;
		URLConnection con;
		try {
			url = new URL("https://www.isel.pt/media/uploads/ADEETC_LEIM_210228.pdf");
			con = url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try (
			 BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
			 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file.getName())))
		{
			int i;
			while ((i = bis.read()) != -1)
				bos.write(i);
			System.out.println("O ficheiro obtido tem " + file.length() + " bytes.");
			System.out.println("Terminou!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}