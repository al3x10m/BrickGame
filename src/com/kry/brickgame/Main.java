package com.kry.brickgame;

import java.awt.EventQueue;

/**
 * The main class to launching
 * 
 * @author noLive
 * @since 01.08.2014
 * 
 */
public final class Main {

	/**
	 * The current game
	 */
	private static Game game;
	/**
	 * The current thread of the game
	 */
	private static Thread gameThread;
	/**
	 * The selection screen of a game
	 */
	public static GameSelector gameSelector = new GameSelector();
	/**
	 * Subscriber to {@code KeyEvent}
	 */
	public static GameKeyAdapter gameKeyAdapter = new GameKeyAdapter();

	public static Game getGame() {
		return game;
	}

	public static void setGame(Game game) {
		Main.game = game;
		gameThread = new Thread(game, "TGameThread");
		gameThread.start();
	}

	/**
	 * Launch the application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
