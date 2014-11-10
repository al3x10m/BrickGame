package com.kry.brickgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.kry.brickgame.games.Game;

/**
 * 
 * @author noLive
 * 
 */
public class ScoresManager {
	/**
	 * Name of file containing the high scores
	 */
	private final String HI_SCORE_FILE = "hiscores.dat";
	/**
	 * Single instance of the {@code ScoresManager}
	 */
	private static ScoresManager singleton;
	/**
	 * Comparison of the game class and high scores
	 */
	private Map<Class<Game>, Integer> hiScores;

	/**
	 * Singleton class, which manages the high scores
	 */
	private ScoresManager() {
		if (!loadScores()) {
			hiScores = new HashMap<>();
		}
	}

	/**
	 * Read the high scores from the file (deserialization)
	 * 
	 * @return {@code true} if loading is success
	 */
	@SuppressWarnings("unchecked")
	private boolean loadScores() {
		File hiScoreFile = new File(HI_SCORE_FILE);
		if (hiScoreFile.exists()) {

			try (ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(hiScoreFile))) {
				try {
					hiScores = (HashMap<Class<Game>, Integer>) in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Write the high scores to the file (serialization)
	 */
	private void saveScores() {
		File hiScoreFile = new File(HI_SCORE_FILE);
		try (ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(hiScoreFile))) {
			out.writeObject(hiScores);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get instance of the {@code ScoresManager}
	 * 
	 * @return instance of the {@code ScoresManager}
	 */
	public static ScoresManager getInstance() {
		if (null == singleton) {
			singleton = new ScoresManager();
		}
		return singleton;
	}

	/**
	 * Get stored high score of the specified game
	 * 
	 * @param gameClass
	 *            class of the game
	 * @return high score
	 */
	public int getHiScore(Class<Game> gameClass) {
		if (!hiScores.containsKey(gameClass))
			hiScores.put(gameClass, 0);
		return hiScores.get(gameClass);
	}

	/**
	 * Set the high score of the specified game when it more then previously
	 * stored value.
	 * <p>
	 * Each call of that method causes writing high scores to the file
	 * (serialization)
	 * 
	 * @param gameClass
	 *            class of the game
	 * @param score
	 *            new score
	 * @return new score if it more then previously high score, otherwise -
	 *         return previously high score
	 */
	public int setHiScore(Class<Game> gameClass, int score) {
		int prevScore = getHiScore(gameClass);
		if (prevScore < score) {
			hiScores.put(gameClass, score);
			saveScores();
			return score;
		} else
			return prevScore;
	}

}
