package download;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

class MainFileCopy {

	public static void downloadFile(URL url, String fileToSave) throws Exception {
		try (InputStream in = url.openStream()) 
		{
			Files.copy(in, Paths.get(fileToSave));
		}
	}
	public static void main(String[] args) throws Exception {
		MainFileCopy.downloadFile(new URL("http://localhost/index.html"), "saveToFile.html");
	}
}