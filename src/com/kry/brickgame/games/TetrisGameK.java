package com.kry.brickgame.games;

import com.kry.brickgame.shapes.TetrisShape;
import com.kry.brickgame.shapes.TetrisShape.Figures;

/**
 * @author noLive
 * 
 */
public class TetrisGameK extends TetrisGame {

	/**
	 * The Tetris with the changing of the figures instead of rotating
	 */
	public TetrisGameK(int speed, int level, int type) {
		super(speed, level, type);
	}

	@Override
	protected void processKeys() {
		if (getStatus() == Status.None)
			return;

		if (keys.contains(KeyPressed.KeyReset)) {
			keys.remove(KeyPressed.KeyReset);
			ExitToMainMenu();
			return;
		}

		if (keys.contains(KeyPressed.KeyStart)) {
			keys.remove(KeyPressed.KeyStart);
			pause();
			return;
		}

		if ((getStatus() == Status.Running) && (!isFallingFinished)) {
			if (keys.contains(KeyPressed.KeyLeft)) {
				tryMove(curPiece, curX - 1, curY);
				sleep(ANIMATION_DELAY * 3);
			}
			if (keys.contains(KeyPressed.KeyRight)) {
				tryMove(curPiece, curX + 1, curY);
				sleep(ANIMATION_DELAY * 3);
			}
			if (keys.contains(KeyPressed.KeyRotate)) {
				// if we have the super gun
				if (curPiece.getShape() == Figures.SuperGun) {
					// than shoot of it
					shoot(curX, curY + curPiece.minY());
				} else if (curPiece.getShape() == Figures.SuperMudGun) {
					mudShoot(curX, curY + curPiece.minY());
				} else {
					TetrisShape rotatedPiece = TetrisShape.getNextShape(
							curPiece, false);
					tryMove(rotatedPiece, curX, curY);
				}
				keys.remove(KeyPressed.KeyRotate);
			}
			if (keys.contains(KeyPressed.KeyDown)) {
				oneLineDown();
				sleep(ANIMATION_DELAY);
			}
			if (keys.contains(KeyPressed.KeyUp)) {
				dropDown();
				keys.remove(KeyPressed.KeyUp);
			}
		}
	}

}
