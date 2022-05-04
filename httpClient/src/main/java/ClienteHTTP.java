
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class ClienteHTTP {
	
	public ClienteHTTP(String strUrl) throws MalformedURLException {
		URL url = new URL(strUrl); 
		String host = url.getHost();
		int port = url.getPort();
		String path = url.getPath();
		// testar com estes exemplos
		// GET http://www.w3.org/pub/WWW/TheProject.html HTTP/1.1
		// OPTIONS * HTTP/1.1
		
		// se não tiver HTTP Version o servidor não envia cabeçalho na resposta
		String msg = "GET /" + path + " HTTP/1.0";  // e nao tiver o protocolo o servidor só envio o conteudo
		new ClienteHTTP(host, port, msg, "++"+getResource(url));
	}

	public ClienteHTTP(String host, int port, String HTTPMesssage, String fileToSave) {
		File outFile = new File(fileToSave);

		try (	Socket s = new Socket(host, port);
				OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
				BufferedInputStream in = new BufferedInputStream(s.getInputStream());
				FileOutputStream outRecurso = new FileOutputStream(outFile)     ) {

			System.out.println("Ligacao estabelecida!");
			
			System.out.println("Vai enviar a mensagem http: \r\n" + HTTPMesssage);

			out.write(HTTPMesssage+"\r\n\r\n");
			out.flush();

			System.out.println("Enviou a mensagem http, vai ler a resposta...");
			int b;
			while ((b = in.read()) != -1) {
					System.out.println("Vai escrever em: " + fileToSave + " '" + (char)b + "' ("+b+")");
					outRecurso.write(b);
			}
			
			System.out.println("A mensagem descarregado tem " + outFile.length() + " bytes.");
			System.out.println("Terminou!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getResource (URL resourceUrl) {
		String strPath=resourceUrl.getFile();
		
		if(strPath.lastIndexOf('?')==-1)
			strPath=strPath.substring(strPath.lastIndexOf('/'));
		else
			strPath=strPath.substring(strPath.lastIndexOf('/'),strPath.lastIndexOf('?'));
		return strPath.substring(1);
	}
	
	public static void main(String[] args) throws MalformedURLException {
		
		new ClienteHTTP("localhost",8080,"OPTIONS * HTTP/1.1", "++opcoes.html");   		// teste no tomcat 
		new ClienteHTTP("www.w3.org",80,"GET /pub/WWW/TheProject.html HTTP/1.1", "++erro.html");
		new ClienteHTTP("http://localhost:8080/httpClient/index.html");   				// teste no tomcat

		
	}
}
