package download;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

class MainNioChannels {
	public static void downloadFile(URL url, String fileToSave) throws IOException {
		try(InputStream in = url.openStream();
			ReadableByteChannel rbc = Channels.newChannel(in);
			FileOutputStream fos = new FileOutputStream(fileToSave))
		{
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
	}

	public static void main(String[] args) throws Exception {
		MainNioChannels.downloadFile(new URL("http://localhost/index.html"), "saveToFile.html");
	}
}