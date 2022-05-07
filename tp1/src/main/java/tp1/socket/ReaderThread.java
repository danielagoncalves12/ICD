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
			for(;;) {				
				String nickname = (String) is.readLine();
				if (nickname.equals("registered")) break;
				System.out.println(nickname.replaceAll("\7", "\n"));
			}

		} catch (IOException e) {}
	} 
}