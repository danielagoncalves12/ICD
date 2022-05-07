package tp1.socket;

import java.io.BufferedReader;
import java.io.IOException;

class ReaderThread extends Thread {

	private BufferedReader is;

	public ReaderThread(BufferedReader is) {
		this.is = is;
	}

	public void run() {

		try {			
			String read = (String) is.readLine();
			System.out.println(read.replaceAll("\7", "\n"));
		} catch (IOException e) {}
	} 
}