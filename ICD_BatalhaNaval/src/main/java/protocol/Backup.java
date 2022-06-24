package protocol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Backup {

	private Path source = Paths.get("src/main/java/protocol/HonorBoard.xml");
	
	public void start() {
		
		try {
		
			// Data atual
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd");  
			LocalDateTime now	  = LocalDateTime.now();  
			
			// Realizar a copia
		    Path newBackup = Paths.get("src/main/java/protocol/backups/HonorBoard_" + dtf.format(now) + ".xml");    
	        Files.copy(this.source, newBackup, StandardCopyOption.REPLACE_EXISTING);
	        
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
		
	}
}
