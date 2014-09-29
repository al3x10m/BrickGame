package com.kry.brickgame.games;

import static com.kry.brickgame.games.GameUtils.checkBoardCollisionHorizontal;
import static com.kry.brickgame.games.GameUtils.checkCollision;
import static com.kry.brickgame.games.GameUtils.drawShape;
import static com.kry.brickgame.games.GameUtils.insertCellsToBoard;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.kry.brickgame.Board;
import com.kry.brickgame.Board.Cell;
import com.kry.brickgame.shapes.Shape;
import com.kry.brickgame.splashes.FroggerSplash;
import com.kry.brickgame.splashes.Splash;

/**
 * @author noLive
 * 
 */
public class FroggerGame extends GameWithLives {
	/**
	 * Animated splash for game
	 */
	public static final Splash splash = new FroggerSplash();
	/**
	 * Number of subtypes
	 */
	public static final int subtypesNumber = 32;
	/**
	 * The frog
	 */
	private Shape frog;

	private Board road;

	private Cell[][] tracts;
	/**
	 * Positions of the road's tract shift
	 */
	private int[] roadPositions;
	/**
	 * Traffic, which move in the direction opposite the rest road
	 */
	private Set<Integer> oncomingTraffic;
	/**
	 * Direction of the shift of the road
	 * <p>
	 * {@code true} - left to right, {@code false} - right to left
	 */
	private boolean shiftRoadFromLeftToRigth;
	/**
	 * Whether the road shifting with the frog?
	 */
	private boolean shiftRoadWithFrog;
	/**
	 * Has the road the oncoming traffic?
	 */
	private boolean isRoadWithOncomingTraffic;
	/**
	 * Use preloaded tracts or generate new ones?
	 */
	private boolean usePreloadedTracts;
	/**
	 * Whether to draw the board upside down?
	 */
	private boolean drawInvertedBoard;

	/**
	 * The Frogger
	 * 
	 * @param speed
	 *            initial value of the speed
	 * @param level
	 *            initial value of the level
	 * @param type
	 *            type of the game type of the game:
	 *            <ol>
	 *            <li>frogger with the road shifting from left to right and the
	 *            frog shifting with that;
	 *            <li>frogger with the road shifting from right to left and the
	 *            frog shifting with that;
	 *            <li>frogger with the road shifting from left to right and the
	 *            frog don't shifting with that;
	 *            <li>frogger with the road shifting from right to left and the
	 *            frog don't shifting with that;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from left to right and the frog shifting with that;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from right to left and the frog shifting with that;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from left to right and the frog don't shifting with
	 *            that;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from right to left and the frog don't shifting with
	 *            that;
	 *            <li>frogger with the randomly generated road shifting from
	 *            left to right and the frog shifting with that;
	 *            <li>frogger with the randomly generated road shifting from
	 *            right to left and the frog shifting with that;
	 *            <li>frogger with the randomly generated road shifting from
	 *            left to right and the frog don't shifting with that;
	 *            <li>frogger with the randomly generated road shifting from
	 *            right to left and the frog don't shifting with that;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from left to right and the frog
	 *            shifting with that;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from right to left and the frog
	 *            shifting with that;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from left to right and the frog
	 *            don't shifting with that;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from right to left and the frog
	 *            don't shifting with that;
	 *            <li>frogger with the road shifting from left to right and the
	 *            frog shifting with that, the board is upside down;
	 *            <li>frogger with the road shifting from right to left and the
	 *            frog shifting with that, the board is upside down;
	 *            <li>frogger with the road shifting from left to right and the
	 *            frog don't shifting with that, the board is upside down;
	 *            <li>frogger with the road shifting from right to left and the
	 *            frog don't shifting with that, the board is upside down;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from left to right and the frog shifting with that,
	 *            the board is upside down;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from right to left and the frog shifting with that,
	 *            the board is upside down;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from left to right and the frog don't shifting with
	 *            that, the board is upside down;
	 *            <li>frogger with the road, that has the oncoming traffic,
	 *            shifting from right to left and the frog don't shifting with
	 *            that, the board is upside down;
	 *            <li>frogger with the randomly generated road shifting from
	 *            left to right and the frog shifting with that, the board is
	 *            upside down;
	 *            <li>frogger with the randomly generated road shifting from
	 *            right to left and the frog shifting with that, the board is
	 *            upside down;
	 *            <li>frogger with the randomly generated road shifting from
	 *            left to right and the frog don't shifting with that, the board
	 *            is upside down;
	 *            <li>frogger with the randomly generated road shifting from
	 *            right to left and the frog don't shifting with that, the board
	 *            is upside down;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from left to right and the frog
	 *            shifting with that, the board is upside down;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from right to left and the frog
	 *            shifting with that, the board is upside down;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from left to right and the frog
	 *            don't shifting with that, the board is upside down;
	 *            <li>frogger with the randomly generated road, that has the
	 *            oncoming traffic, shifting from right to left and the frog
	 *            don't shifting with that, the board is upside down;
	 */
	public FroggerGame(int speed, int level, int type) {
		super(speed, level, type);

		// initialize the frog
		this.frog = new Shape(1);
		frog.setCoord(0, new int[] { 0, 0 });
		frog.setFill(Cell.Blink);

		// ==define the parameters of the types of game==
		// for every odd type of game
		shiftRoadFromLeftToRigth = (getType() % 2 != 0);
		// for every 1st and 2nd type of game
		shiftRoadWithFrog = ((getType() % 4 == 1) || (getType() % 4 == 2));
		// for every 4-8 type of game
		isRoadWithOncomingTraffic = ((getType() % 8 == 5)
				|| (getType() % 8 == 6) || (getType() % 8 == 7) || (getType() % 8 == 0));
		// for types 1-8 and 16-24
		usePreloadedTracts = ((getType() <= 8) || ((getType() >= 16) && (getType() <= 24)));
		// for types 16-32
		drawInvertedBoard = (getType() > 16);
	}

	/**
	 * Launching the game
	 */
	@Override
	public void start() {
		super.start();
		setStatus(Status.Running);

		loadNewLevel();

		while (!interrupted() && (getStatus() != Status.GameOver)) {
			if ((getStatus() != Status.Paused)
					&& (elapsedTime(getSpeed(true) * 3))) {
				shiftRoad(shiftRoadFromLeftToRigth, shiftRoadWithFrog);
			}
			// processing of key presses
			processKeys();
		}
	}

	/**
	 * Loading or reloading the specified level
	 */
	@Override
	protected void loadNewLevel() {
		// create the road
		road = loadRoad(usePreloadedTracts);
		insertCellsToBoard(getBoard(), road.getBoard(), 0, 1);
		// initialize the frog
		setFrog();

		setStatus(Status.Running);
	}

	/**
	 * Drawing the frog in the start position
	 */
	private void setFrog() {
		// starting position - the middle of the bottom border of the board
		curX = boardWidth / 2 - 1;
		curY = 0;

		jumpFrog(curX, curY);
	}

	/**
	 * Creating the road
	 * 
	 * @param usePreloaded
	 *            determine, use the preloaded tracts of the road or generate
	 *            new ones
	 * @return the road
	 */
	private Board loadRoad(boolean usePreloaded) {
		final Cell F = Cell.Full;
		final Cell E = Cell.Empty;
		// preloaded tracks
		tracts = new Cell[][] {
				//
				{ E, E, F, F, E, E, E, E, E, F, F, F, F, E, E, E },
				{ E, E, E, F, F, E, E, E, E, E, F, F, F, F, E, E },
				{ F, E, E, E, E, F, F, E, E, E, E, E, F, F, F, F },
				{ E, E, E, F, E, E, E, E, F, F, E, E, E, E, F, F },
				{ F, E, E, E, E, F, F, F, F, E, E, E, E, F, F, F },
				{ F, F, F, E, E, E, E, F, F, F, F, E, E, E, E, F },
				{ F, E, E, E, E, F, F, F, F, E, E, E, E, F, F, F },
				{ F, F, E, E, E, E, E, F, F, F, E, E, E, E, E, F }, };

		// initial position changes from level
		roadPositions = new int[tracts.length];
		for (int i = 0; i < roadPositions.length; i++) {
			roadPositions[i] = getLevel() - 1;
		}

		// generate the oncoming traffic, who move in the direction opposite the
		// rest road
		oncomingTraffic = new HashSet<>();
		if (usePreloaded) {
			// tracts at the end and in the middle
			oncomingTraffic.add(tracts.length / 2 - 1);
			oncomingTraffic.add(tracts.length - 1);
		} else {
			// from 1 on level 1, to 4 on level 10
			for (int i = 0; i < getLevel() / 3 + 1; i++) {
				// defines a few random non-recurring tracts
				while (!oncomingTraffic
						.add(new Random().nextInt(tracts.length))) {
				}
			}
		}

		// generated random tracts
		if (!usePreloaded) {
			Random r = new Random();
			int emptySpanLength, fullSpanLength;
			int k;

			for (int i = 0; i < tracts.length; i++) {
				// set length of full and empty span
				emptySpanLength = r.nextInt(2) + 3;
				fullSpanLength = r.nextInt(3) + 1;

				do {
					// start point
					k = r.nextInt(tracts[i].length);
					// checking if under the start point has an empty cell
				} while ((i > 1) && (tracts[i - 1][k] != E));

				for (int j = 0; j < tracts[i].length; j++) {
					// in first, create the empty span
					if (emptySpanLength > 0) {
						tracts[i][k + j] = E;
						emptySpanLength--;
					}
					// in second, create the full span
					else if (fullSpanLength > 0) {
						tracts[i][k + j] = F;
						fullSpanLength--;
					}

					if ((emptySpanLength == 0) && (fullSpanLength == 0)) {
						// when created both, regenerating their length
						emptySpanLength = r.nextInt(2) + 3;

						while (tracts[i].length - j - 1 <= emptySpanLength) {
							// check the empty span does not leave the tract
							// and there is at least one cell to the full span
							emptySpanLength--;
						}
						// sets the length of the full span for the remainder of
						// the tract length
						fullSpanLength = tracts[i].length - j - 1
								- emptySpanLength;
						// but not more than 4
						if (fullSpanLength > 4)
							fullSpanLength = r.nextInt(3) + 1;
					}
					// if "k + j" out of bounds, set "k" such that "k + j = 0"
					if ((k + j + 1) >= tracts[i].length)
						k = -(j + 1);
				}
			}

		}

		int width = boardWidth;
		int height = boardHeight - 3;
		Board board = new Board(width, height);

		Cell[] tract = new Cell[width];

		for (int i = 0; i < height; i++) {
			if (i % 2 == 0) {
				// even lines must be filled
				for (int j = 0; j < width; j++) {
					tract[j] = Cell.Full;
				}
			} else {
				int tractLength = tracts[i / 2].length;
				int length = width;
				// if the remainder is less than the width of the tract, copies
				// only the remainder at first
				if (tractLength - roadPositions[i / 2] < width)
					length = (tractLength - roadPositions[i / 2]);

				System.arraycopy(tracts[i / 2], roadPositions[i / 2], tract, 0,
						length);

				if (length != width) {
					// complement to the width from the beginning of the tract
					System.arraycopy(tracts[i / 2], 0, tract, length, width
							- length);
				}
			}
			board.setRow(tract, i);
		}
		return board;
	}

	/**
	 * Does the shift of the road at one position
	 * 
	 * @param isLeftToRight
	 *            direction of the shift of the road: {@code true} - left to
	 *            right, {@code false} - right to left
	 * @param withFrog
	 *            determine, whether the road is shifted with the frog
	 */
	private void shiftRoad(boolean isLeftToRight, boolean withFrog) {
		Board board = getBoard();
		board = drawShape(board, curX, curY, frog, Cell.Empty);

		Cell[] tract = new Cell[boardWidth];

		int tractLength = tracts[0].length;

		// determines the position for the shift
		for (int i = 0; i < roadPositions.length; i++) {
			if (isRoadWithOncomingTraffic && oncomingTraffic.contains(i)) {
				// for oncoming traffic
				roadPositions[i] = (isLeftToRight) ? roadPositions[i] + 1
						: roadPositions[i] - 1;
			} else {
				// for direct traffic
				roadPositions[i] = (isLeftToRight) ? roadPositions[i] - 1
						: roadPositions[i] + 1;
			}

			if (roadPositions[i] < 0)
				roadPositions[i] = tractLength - 1;
			else if (roadPositions[i] >= tractLength)
				roadPositions[i] = 0;
		}

		for (int i = 0; i < road.getHeight(); i++) {
			if (i % 2 != 0) {
				int length = boardWidth;
				// if the remainder is less than the width of the tract, copies
				// only the remainder at first
				if (tractLength - roadPositions[i / 2] < boardWidth)
					length = (tractLength - roadPositions[i / 2]);

				System.arraycopy(tracts[i / 2], roadPositions[i / 2], tract, 0,
						length);

				if (length != boardWidth) {
					// complement to the width from the beginning of the tract
					System.arraycopy(tracts[i / 2], 0, tract, length,
							boardWidth - length);
				}
				road.setRow(tract, i);
			}
		}
		insertCellsToBoard(board, road.getBoard(), 0, 1);

		// shifting the frog with the road
		if (withFrog && ((curY > 0) && (curY < boardHeight - 1))) {
			if (isRoadWithOncomingTraffic
					&& oncomingTraffic.contains((curY - 1) / 2))
				curX = (isLeftToRight) ? curX - 1 : curX + 1;
			else
				curX = (isLeftToRight) ? curX + 1 : curX - 1;
		}

		// checks for collision with the frog and an obstacles
		boolean isFrogMustDie = (checkBoardCollisionHorizontal(board, frog,
				curX) || checkCollision(board, frog, curX, curY));

		setBoard(drawShape(board, curX, curY, frog, frog.getFill()));

		if (isFrogMustDie)
			loss();
	}

	/**
	 * Moving the frog to the new position
	 * 
	 * @param x
	 *            x-coordinate position of the new position
	 * @param y
	 *            y-coordinate position of the new position
	 * @return {@code true} if the movement succeeded, otherwise {@code false}
	 */
	private boolean jumpFrog(int x, int y) {
		if ((y < 0) || (y >= boardHeight))
			return true;

		if ((x < 0) || (x >= boardWidth))
			return false;

		// Create a temporary board, a copy of the basic board
		Board board = getBoard().clone();

		// Erase the frog to not interfere with the checks
		board = drawShape(board, curX, curY, frog, Cell.Empty);

		// check for collisions
		if (checkCollision(board, frog, x, y))
			return false;

		// draw the frog on the new place
		if (y == boardHeight - 1) {
			setBoard(drawShape(board, x, y, frog, Cell.Full));
			checkForWin();
		} else {
			setBoard(drawShape(board, x, y, frog, frog.getFill()));
			curX = x;
			curY = y;
		}

		return true;
	}

	/**
	 * Drawing effect of the collisions and decreasing lives
	 */
	private void loss() {
		// saves the upper row with the frogs, who went over the road
		Cell[] frogs = getBoard().getRow(boardHeight - 1);

		super.loss(curX, curY);

		// restores the frogs' row
		getBoard().setRow(frogs, boardHeight - 1);
	}

	/**
	 * Checking the conditions of victory
	 */
	private void checkForWin() {
		boolean isVictory = true;
		// victory when filled the entire upper row
		for (int i = 0; i < boardWidth; i++) {
			if (getBoard().getCell(i, boardHeight - 1) == Cell.Empty) {
				isVictory = false;
				break;
			}
		}

		setScore(getScore() + 1);

		if (isVictory) {
			animatedClearLine(getBoard(), curX, boardHeight - 1);
			sleep(ANIMATION_DELAY);

			win();
		} else {
			setFrog();
		}
	}

	@Override
	protected synchronized void fireBoardChanged(Board board) {
		Board newBoard = board.clone();

		// draws the inverted board
		if (drawInvertedBoard) {
			for (int i = 0; i < board.getHeight(); i++) {
				newBoard.setRow(board.getRow(i), board.getHeight() - i - 1);
			}
		}

		super.fireBoardChanged(newBoard);
	}

	/**
	 * Processing of key presses
	 */
	@Override
	protected void processKeys() {
		if (getStatus() == Status.None)
			return;

		super.processKeys();

		if (getStatus() == Status.Running) {

			if (keys.contains(KeyPressed.KeyLeft)) {
				if (!jumpFrog(curX - 1, curY))
					loss();
				keys.remove(KeyPressed.KeyLeft);
			}
			if (keys.contains(KeyPressed.KeyRight)) {
				if (!jumpFrog(curX + 1, curY))
					loss();
				keys.remove(KeyPressed.KeyRight);
			}

			int dY;

			if (keys.contains(KeyPressed.KeyDown)) {
				if (drawInvertedBoard)
					dY = (curY < boardHeight - 2) ? 2 : 1;
				else
					dY = -2;

				if (!jumpFrog(curX, curY + dY))
					loss();
				keys.remove(KeyPressed.KeyDown);
			}
			if (keys.contains(KeyPressed.KeyUp)) {
				if (drawInvertedBoard)
					dY = -2;
				else
					dY = (curY < boardHeight - 2) ? 2 : 1;

				if (!jumpFrog(curX, curY + dY))
					loss();
				keys.remove(KeyPressed.KeyUp);
			}
			if (keys.contains(KeyPressed.KeyRotate)) {
				dY = (curY < boardHeight - 2) ? 2 : 1;
				if (!jumpFrog(curX, curY + dY))
					loss();
				keys.remove(KeyPressed.KeyRotate);
			}
		}
	}

}
