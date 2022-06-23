package battleship;

import java.time.Duration;
import java.time.Instant;

import socket.GameQueueThread;

public class TimerThread extends Thread {

	private Instant then;
	private Duration threshold;
	private GameModel game;
	
	public TimerThread(GameModel game) {
		
		this.game = game;
	}
	
	public void run() {

		// Espera 30 segundos (+ 1 segundo para indireitar)
        try {
        	then = Instant.now();
            threshold = Duration.ofSeconds(31);
			Thread.sleep(31000);
			
			assert timeHasElapsedSince(then, threshold) == true;
	        game.close();
	        
	        if (game.play1 == false && game.play2 == false)
	        	if (GameQueueThread.activeGames.contains(game))
	        		GameQueueThread.activeGames.remove(game);
			
		} catch (InterruptedException ignored) {}      
	}
	
	public boolean timeHasElapsedSince(Instant then, Duration threshold) {
        return (Duration.between(then, Instant.now())).compareTo(threshold) > 0; //> threshold.toSeconds();
    }

}
