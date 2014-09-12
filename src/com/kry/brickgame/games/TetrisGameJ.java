package com.kry.brickgame.games;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author noLive
 * 
 */
public class TetrisGameJ extends TetrisGame {
	final int TIME_BETWEEN_ADDING_LINE = 30;
	volatile int time;
	volatile boolean isTimeToAddLine;

	/**
	 * The Tetris with the addition of new lines every few seconds
	 */
	public TetrisGameJ(int speed, int level, int type) {
		super(speed, level, type);
		isTimeToAddLine = false;
		time = TIME_BETWEEN_ADDING_LINE;
	}

	@Override
	public void start() {

		// create timer for addition of lines
		Timer addLineTimer = new Timer("AddLineTicTac", true);
		addLineTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (getStatus() == Status.Running) {
					fireInfoChanged("-" + String.format("%02d", time) + "-");
					if (time == 0) {
						isTimeToAddLine = true;
					} else {
						time--;
					}
				}
			}
		}, 0, 1000);

		super.start();

		addLineTimer.cancel();
	}

	@Override
	protected void doRepetitiveWork() {
		// if it's time to add a line, trying to add a line
		if ((isTimeToAddLine) && (tryAddLine())) {
			time = TIME_BETWEEN_ADDING_LINE;
			isTimeToAddLine = false;
		} else {
			super.doRepetitiveWork();
		}
	}

	protected boolean tryAddLine() {
		if ((!checkBoardCollisionVertical(curPiece, curY + 1, true))
				&& (addLines())) {
			// the current y-coordinate lifts by one cell upward
			curY++;
			return true;
		} else
			return false;
	}

}