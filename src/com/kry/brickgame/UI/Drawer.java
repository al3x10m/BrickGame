package com.kry.brickgame.UI;

import static com.kry.brickgame.UI.UIConsts.COMING;
import static com.kry.brickgame.UI.UIConsts.GAME_FIELD_ASPECT_RATIO;
import static com.kry.brickgame.UI.UIConsts.GAME_OVER;
import static com.kry.brickgame.UI.UIConsts.HI;
import static com.kry.brickgame.UI.UIConsts.ICON_MUSIC;
import static com.kry.brickgame.UI.UIConsts.ICON_PAUSE;
import static com.kry.brickgame.UI.UIConsts.ICON_ROTATE_LEFT;
import static com.kry.brickgame.UI.UIConsts.ICON_ROTATE_RIGHT;
import static com.kry.brickgame.UI.UIConsts.LEVEL;
import static com.kry.brickgame.UI.UIConsts.LIVES;
import static com.kry.brickgame.UI.UIConsts.NEXT;
import static com.kry.brickgame.UI.UIConsts.NUMBER_SUBSTRATE;
import static com.kry.brickgame.UI.UIConsts.PAUSE;
import static com.kry.brickgame.UI.UIConsts.ROTATE;
import static com.kry.brickgame.UI.UIConsts.SCORE;
import static com.kry.brickgame.UI.UIConsts.SCORE_SUBSTRATE;
import static com.kry.brickgame.UI.UIConsts.SOON;
import static com.kry.brickgame.UI.UIConsts.SPEED;
import static com.kry.brickgame.UI.UIConsts.bgColor;
import static com.kry.brickgame.UI.UIConsts.emptyColor;
import static com.kry.brickgame.UI.UIConsts.fullColor;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import com.kry.brickgame.boards.Board;
import com.kry.brickgame.boards.Board.Cell;
import com.kry.brickgame.games.GameConsts.Rotation;
import com.kry.brickgame.games.GameConsts.Status;

/**
 * @author noLive
 */
public final class Drawer {
	/**
	 * Font Manager class
	 */
	private static final class FontManager {
		/**
		 * Instance of the standalone FontManager class
		 */
		private static FontManager instance = new FontManager();
		
		/**
		 * Font for digital data (score, etc.)
		 */
		Font digitalFont;
		
		/**
		 * Font for textual data (labels, etc.)
		 */
		Font textFont;
		
		/**
		 * Font of icons
		 */
		Font iconFont;
		
		/**
		 * Calculates the optimal size of the font {@code f} to fit the
		 * specified {@code text} in the specified line {@code maxWidth}.
		 * 
		 * @param g
		 *            {@code Graphics2D} object, which used to draw on it
		 * @param f
		 *            font, to calculate size of
		 * @param text
		 *            specified string
		 * @param maxWidth
		 *            maximal width of the {@code text}
		 * @return optimal size of the font
		 */
		private static float calcOptimumFontSize(Graphics g, Font f, String text, int maxWidth) {
			FontMetrics fm = g.getFontMetrics(f);
			float result = f.getSize();
			int stringWidth = fm.stringWidth(text);
			
			// first, try to increase the font size while
			while (stringWidth < maxWidth) {
				fm = g.getFontMetrics(f.deriveFont(++result));
				stringWidth = fm.stringWidth(text);
			}
			
			if (stringWidth == maxWidth) return result;
			
			// second, try to decrease the font size
			while (stringWidth > maxWidth && result > 0) {
				fm = g.getFontMetrics(f.deriveFont(--result));
				stringWidth = fm.stringWidth(text);
			}
			return result;
			
		}
		
		/**
		 * Gets instance of the FontManager
		 * 
		 * @return instance of the FontManager
		 */
		protected static FontManager getManager() {
			return instance;
		}
		
		/**
		 * Calculates the optimal size to fit the specified {@code text} in the
		 * specified line {@code maxWidth} and applies it to the specified
		 * {@code Font f}
		 * 
		 * @param f
		 *            specified {@code Font} object
		 * @param g
		 *            {@code Graphics2D} object, which used to draw on it
		 * @param text
		 *            specified string
		 * @param maxWidth
		 *            maximal width of the {@code text}
		 */
		protected static Font setOptimalFont(Font f, Graphics g, String text, int maxWidth) {
			final float EPSILON = 1e-5f;
			
			Font result = f;
			float fontSize = calcOptimumFontSize(g, f, text, maxWidth);
			// if not equals
			if (Math.abs(fontSize - f.getSize2D()) > EPSILON) {
				result = f.deriveFont(fontSize);
			}
			return result;
		}
		
		private FontManager() {
			initFonts();
		}
		
		/**
		 * Loads fonts from resources
		 */
		private void initFonts() {
			/* textFont */
			textFont = new Font(Font.SANS_SERIF, Font.BOLD, 0);
			/* --- */
			
			/* digitalFont */
			try {
				// trying to get the font from a resource file
				digitalFont = Font.createFont(Font.TRUETYPE_FONT,
				        Drawer.class.getResourceAsStream("/fonts/Segment7Standard.otf"));
				
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
				// if error just create a monospaced font
				digitalFont = new Font(Font.MONOSPACED, Font.PLAIN, 0);
			}
			/* --- */
			
			/* iconFont */
			try {
				iconFont = Font.createFont(Font.TRUETYPE_FONT,
				        Drawer.class.getResourceAsStream("/fonts/icomoon.ttf"));
				
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
				// when error does not use the font
				iconFont = null;
			}
			/* --- */
		}
		
		/**
		 * Calculates the optimal size to fit the specified {@code text} in the
		 * specified line {@code maxWidth} and applies it to the
		 * {@code digitalFont}
		 * 
		 * @param g
		 *            {@code Graphics2D} object, which used to draw on it
		 * @param text
		 *            specified string
		 * @param maxWidth
		 *            maximal width of the {@code text}
		 */
		void setOptimalDigitalFont(Graphics g, String text, int maxWidth) {
			digitalFont = digitalFont
			        .deriveFont(calcOptimumFontSize(g, digitalFont, text, maxWidth));
		}
		
		/**
		 * Calculates the optimal size to fit the specified {@code text} in the
		 * specified line {@code maxWidth} and applies it to the
		 * {@code iconFont}
		 * 
		 * @param g
		 *            {@code Graphics2D} object, which used to draw on it
		 * @param text
		 *            specified string
		 * @param maxWidth
		 *            maximal width of the {@code text}
		 */
		void setOptimalIconFont(Graphics g, String text, int maxWidth) {
			iconFont = setOptimalFont(iconFont, g, text, maxWidth);
		}
		
		/**
		 * Calculates the optimal size to fit the specified {@code text} in the
		 * specified line {@code maxWidth} and applies it to the
		 * {@code textFont}
		 * 
		 * @param g
		 *            {@code Graphics2D} object, which used to draw on it
		 * @param text
		 *            specified string
		 * @param maxWidth
		 *            maximal width of the {@code text}
		 */
		void setOptimalTextFont(Graphics g, String text, int maxWidth) {
			textFont = setOptimalFont(textFont, g, text, maxWidth);
		}
	}
	
	/**
	 * Instance of the standalone Drawer class
	 */
	private static Drawer drawer = new Drawer();
	
	private GameProperties prevProperties;
	
	private int prevWidth, prevHeight;
	
	/* For events feedback */
	/**
	 * Color of elements that change their state from active to inactive
	 */
	private Color blinkColor;
	
	/**
	 * Flag for blinking "Pause" icon
	 */
	private boolean showPauseIcon;
	
	/**
	 * Flag for show high scores
	 */
	private boolean showHiScores;
	
	/**
	 * Flag for show "lives" label
	 */
	private boolean showLives;
	
	/**
	 * Flag for show "next" label
	 */
	private boolean showNext;
	
	/* --- */
	/**
	 * Main canvas, combining all elements
	 */
	private BufferedImage canvas;
	
	/**
	 * Canvas that is used to display the main board
	 */
	private BufferedImage boardCanvas;
	
	/**
	 * Canvas that is used to display the preview board
	 */
	private BufferedImage previewCanvas;
	
	/**
	 * Canvas that is used to display labels and scores
	 */
	private BufferedImage labelsCanvas;
	
	/**
	 * The length of the square side
	 */
	private int squareSideLength;
	/**
	 * The font manager
	 */
	private final FontManager fontManager;
	
	/**
	 * Adding to the {@code targetCanvas} the image from the
	 * {@code sourceCanvas}
	 * 
	 * @param targetCanvas
	 *            the canvas on which the image to be added
	 * @param sourceCanvas
	 *            the canvas, the image from which to be added
	 * @param x
	 *            x-coordinate of upper left corner of the image to be added
	 * @param y
	 *            y-coordinate of upper left corner of the image to be added
	 */
	private static void appendCanvas(BufferedImage targetCanvas, BufferedImage sourceCanvas, int x,
	        int y) {
		if (targetCanvas == null || sourceCanvas == null) return;
		
		Graphics2D g2d = targetCanvas.createGraphics();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		g2d.drawImage(sourceCanvas, x, y, null);
		
		g2d.dispose();
	}
	
	/**
	 * Converting the height of the board from the <b>visible</b> cells to
	 * pixels
	 * 
	 * @param board
	 *            the board for which determine the width in pixels
	 * @param squareSideLength
	 *            length of the side of the one board's cells
	 * @return the height of the board in pixels
	 * @see #boardWidthInPixels
	 */
	private static int boardHeightInPixels(Board board, int squareSideLength) {
		return board.getHeight() * squareSideLength;
	}
	
	/**
	 * Converting the width of the board from cells to pixels
	 * 
	 * @param board
	 *            the board for which determine the width in pixels
	 * @param squareSideLength
	 *            length of the side of the one board's cells
	 * @return the width of the board in pixels
	 * @see #boardHeightInPixels
	 */
	private static int boardWidthInPixels(Board board, int squareSideLength) {
		return board.getWidth() * squareSideLength;
	}
	
	/**
	 * Draws the borders around the canvas
	 * 
	 * @param canvas
	 *            the canvas to draw the borders
	 * @param borderLineWidth
	 *            line width for the border line
	 */
	private static void canvasSetBorder(BufferedImage canvas, float borderLineWidth) {
		if (canvas == null) return;
		
		Graphics2D g2d = canvas.createGraphics();
		
		g2d.setColor(fullColor);
		// set thickness of the line
		g2d.setStroke(new BasicStroke(borderLineWidth));
		// shift for the proper drawing the thick border line
		int shift = Math.round(borderLineWidth);
		g2d.drawRect(shift / 2, shift / 2, canvas.getWidth() - shift, canvas.getHeight() - shift);
		
		g2d.dispose();
	}
	
	/**
	 * Clears the canvas by filling it with the background color
	 * 
	 * @param canvas
	 *            the canvas to clear
	 * @param bgColor
	 *            the background color
	 */
	private static void clearCanvas(BufferedImage canvas, Color bgColor) {
		if (canvas == null) return;
		Graphics2D g2d = canvas.createGraphics();
		
		g2d.setBackground(bgColor);
		g2d.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		g2d.dispose();
	}
	
	/**
	 * Draws the text labels on the canvas.
	 * <p>
	 * First, the {@code backgroundText} is drawn in {@code emptyColor} color,
	 * then the {@code foregroundText} is drawn in {@code fullColor} color. The
	 * length of the {@code backgroundText} should not exceed the length of the
	 * {@code foregroundText}.
	 * <p>
	 * It's equivalent of
	 * {@code drawTextOnCanvas(canvas, backgroundText, foregroundText, font,  x, y, true)}
	 * 
	 * @param canvas
	 *            the target canvas
	 * @param backgroundText
	 *            the text that displayed in the background
	 * @param foregroundText
	 *            the text that displayed in the foreground
	 * @param font
	 *            text font
	 * @param x
	 *            x-coordinate of lower left corner of the text to be drawn
	 * @param y
	 *            y-coordinate of lower left corner of the text to be drawn
	 */
	private static void drawTextOnCanvas(BufferedImage canvas, String backgroundText,
	        String foregroundText, Font font, int x, int y) {
		drawTextOnCanvas(canvas, backgroundText, foregroundText, font, x, y, true);
	}
	
	/**
	 * Draws the text labels on the canvas.
	 * <p>
	 * First, the {@code backgroundText} is drawn in {@code emptyColor} color,
	 * then the {@code foregroundText} is drawn in {@code fullColor} color. The
	 * length of the {@code backgroundText} should not exceed the length of the
	 * {@code foregroundText}.
	 * 
	 * @param canvas
	 *            the target canvas
	 * @param backgroundText
	 *            the text that displayed in the background
	 * @param foregroundText
	 *            the text that displayed in the foreground
	 * @param font
	 *            text font
	 * @param x
	 *            x-coordinate of lower left corner of the text to be drawn
	 * @param y
	 *            y-coordinate of lower left corner of the text to be drawn
	 * @param clearBackground
	 *            whether to clear the substrate background below the text?
	 */
	private static void drawTextOnCanvas(BufferedImage canvas, String backgroundText,
	        String foregroundText, Font font, int x, int y, boolean clearBackground) {
		if (canvas == null) return;
		
		Graphics2D g2d = canvas.createGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setFont(font);
		
		if (clearBackground) {
			// need to determine the size of the text area
			FontMetrics fm = g2d.getFontMetrics();
			
			// clear the text area
			g2d.setBackground(bgColor);
			g2d.clearRect(x, y - font.getSize(), fm.stringWidth(backgroundText) + 1,
			        font.getSize() + 2);
		}
		
		// forming a string for text formatting, based on backgroundText
		// like "%5.5s"
		// http://download.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax
		StringBuilder formatString = new StringBuilder(7).append('%')
		        .append(backgroundText.length()).append('.').append(backgroundText.length())
		        .append('s');
		
		// draws the backgroundText
		g2d.setColor(emptyColor);
		g2d.drawString(String.format(Locale.ENGLISH, formatString.toString(), backgroundText), x, y);
		
		// draws the foregroundText
		g2d.setColor(fullColor);
		g2d.drawString(String.format(Locale.ENGLISH, formatString.toString(),
		        foregroundText != null ? foregroundText : ""), x, y);
		
		g2d.dispose();
	}
	
	/**
	 * Gets instance of the Drawer
	 * 
	 * @return instance of the Drawer
	 */
	public static Drawer getDrawer() {
		return drawer;
	}
	
	/**
	 * Creating the canvas based on a board
	 * 
	 * @param board
	 *            board, the width and the height of which will be used to
	 *            create of the canvas
	 * @param borderLineWidth
	 *            line width for the border line
	 * @param squareSideLength
	 *            the length of the square side
	 * @return {@code null} if the board is not defined, otherwise - the new
	 *         canvas
	 */
	private static BufferedImage initCanvas(BufferedImage canvas, Board board,
	        int squareSideLength, float borderLineWidth) {
		if (board == null) return null;
		
		int border = (int) borderLineWidth;
		
		// increasing the width and height of the canvas by the thickness of the
		// border line on each side
		return initCanvas(canvas, boardWidthInPixels(board, squareSideLength) + border * 2,
		        boardHeightInPixels(board, squareSideLength) + border * 2);
	}
	
	/**
	 * Creating the canvas specified width and height
	 * 
	 * @param width
	 *            width of the created canvas
	 * @param height
	 *            height of the created canvas
	 */
	private static BufferedImage initCanvas(BufferedImage canvas, int width, int height) {
		return initCanvas(canvas, width, height, true);
	}
	
	/**
	 * Creating the canvas specified width and height
	 * 
	 * @param width
	 *            width of the created canvas
	 * @param height
	 *            height of the created canvas
	 * @param clear
	 *            whether or not to clear the canvas
	 */
	private static BufferedImage initCanvas(BufferedImage canvas, int width, int height,
	        boolean clear) {
		BufferedImage result;
		if (null == canvas || canvas.getWidth() != width || canvas.getHeight() != height) {
			result = UIUtils.getCompatibleImage(width, height, Transparency.OPAQUE);
		} else {
			result = canvas;
		}
		if (clear) {
			clearCanvas(result, bgColor);
		}
		
		return result;
	}
	
	private Drawer() {
		canvas = null;
		boardCanvas = null;
		previewCanvas = null;
		labelsCanvas = null;
		prevProperties = null;
		squareSideLength = 0;
		fontManager = FontManager.getManager();
	}
	
    /**
     * Calculate optimal line width for a border line, depending of the size of a square. The line
     * width can't be more than {@code MAX_BORDER_WIDTH} and less than 1..
     *
     * @return line width for a border line
     */
    private float calcBorderLineWidth() {
        final float MAX_BORDER_WIDTH = 4f;

        // return minimal from squareSideLength/4 or MAX_BORDER_WIDTH
        float result = Math.min(squareSideLength / 4f, MAX_BORDER_WIDTH);
        // not less than 1
        result = Math.max(result, 1);

        return result;
    }
	
	/**
	 * Draws the contents of the board to the canvas
	 * 
	 * @param canvas
	 *            the target canvas
	 * @param board
	 *            the board whose contents need to be drawn
	 * @param borderLineWidth
	 *            line width for the border line
	 */
	private void drawBoardOnCanvas(BufferedImage canvas, Board board, float borderLineWidth) {
		if (canvas == null || board == null) return;
		
		int boardWidth = board.getWidth();
		int boardHeight = board.getHeight();
		
		// shift from the beginning of the canvas to avoid the border
		int shift = Math.round(borderLineWidth);
		
		float squareBorderWidth = borderLineWidth * 0.7f;
		BufferedImage fullSquare = drawSquare(Cell.Full, squareBorderWidth);
		BufferedImage emptySquare = drawSquare(Cell.Empty, squareBorderWidth);
		BufferedImage blinkSquare = blinkColor.equals(fullColor) ? fullSquare : emptySquare;
		
		Graphics2D g2d = canvas.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw squares of the board
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWidth; j++) {
				// "boardHeight - i - 1" - the board reads from the bottom up
				Cell fill = board.getCell(j, boardHeight - i - 1);
				int x = shift + j * squareSideLength;
				int y = shift + i * squareSideLength;
				switch (fill) {
				case Full:
					g2d.drawImage(fullSquare, x, y, null);
					break;
				case Empty:
					g2d.drawImage(emptySquare, x, y, null);
					break;
				case Blink:
					g2d.drawImage(blinkSquare, x, y, null);
					break;
				}
			}
		}
		
		g2d.dispose();
	}
	
	/**
	 * Draws the text labels for "Coming soon" status on the specified canvas
	 * 
	 * @param canvas
	 *            the target canvas
	 * @param status
	 *            status of the game
	 */
	private void drawComingSoonStatus(BufferedImage canvas, Status status) {
		if (canvas == null || fontManager == null || status != Status.ComingSoon) return;
		
		/* Coming soon label */
		Graphics g = canvas.getGraphics();
		FontMetrics fm;
		int x, y;
		
		int maxWidth = canvas.getWidth() - squareSideLength;
		Font font = fontManager.digitalFont.deriveFont(Font.PLAIN);
		font = FontManager.setOptimalFont(font, g, COMING, maxWidth);
		
		// set on center of the canvas
		x = squareSideLength / 2;
		y = canvas.getHeight() / 2 - font.getSize() / 2;
		drawTextOnCanvas(canvas, COMING, COMING, font, x, y, false);
		
		fm = g.getFontMetrics(fontManager.digitalFont);
		// set on center of the canvas
		x = (canvas.getWidth() - squareSideLength - fm.stringWidth(SOON)) / 2;
		y = canvas.getHeight() / 2 + font.getSize() / 2;
		drawTextOnCanvas(canvas, SOON, SOON, font, x, y, false);
		/* --- */
	}
	
	/**
	 * Draws all text labels and icons on the specified canvas
	 * 
	 * @param canvas
	 *            the target canvas
	 * @param properties
	 *            main properties of the game
	 */
	private void drawLabelsAndIcons(BufferedImage canvas, GameProperties properties, boolean cleared) {
		if (canvas == null || fontManager == null) return;
		
		Graphics g = canvas.getGraphics();
		FontMetrics fm;
		int x, y;
		int maxWidth;
		
        boolean forceUpdate = cleared || prevProperties == null;
        
		// set scores string
		String scoresInfo = showHiScores ? properties.hiScores : properties.info;
		if (scoresInfo == null) scoresInfo = "";
		String oldScoresInfo = null;
        if (prevProperties != null) {
            oldScoresInfo =
                    prevProperties.showHiScores ? prevProperties.hiScores : prevProperties.info;
        }
		
		boolean updateScores = forceUpdate || !scoresInfo.equals(oldScoresInfo) ||
                showHiScores != prevProperties.showHiScores;
		if (updateScores) {
			/* Scores */
			maxWidth = canvas.getWidth();
			fontManager.setOptimalDigitalFont(g, SCORE_SUBSTRATE, maxWidth);
			
			x = 0;
			y = fontManager.digitalFont.getSize();
			
			drawTextOnCanvas(canvas, SCORE_SUBSTRATE, scoresInfo, fontManager.digitalFont, x, y);
			/* --- */
			
			/* Scores label */
			// minus width of the music icon and space between label and icon
			maxWidth = canvas.getWidth() - (squareSideLength + squareSideLength / 2);
			
			fontManager.setOptimalTextFont(g, "  " + HI + SCORE, maxWidth);
			
			fm = g.getFontMetrics(fontManager.textFont);
			x = fm.stringWidth("  ");
			// max height of a two squares or height of the scores and scores
			// label
			y = Math.max(2 * squareSideLength, fontManager.digitalFont.getSize())
			        + fontManager.textFont.getSize();
			
			drawTextOnCanvas(canvas, HI, showHiScores && !scoresInfo.isEmpty() ? HI : "",
			        fontManager.textFont.deriveFont(Font.PLAIN), x, y);
			drawTextOnCanvas(canvas, SCORE, !scoresInfo.isEmpty() ? SCORE : "",
			        fontManager.textFont.deriveFont(Font.PLAIN), x + fm.stringWidth(HI), y);
			/* --- */
		}
		
		if (updateScores || properties.mute != prevProperties.mute) {
			/* Music icon */
			if (fontManager.iconFont != null) {
				fontManager.setOptimalIconFont(g, ICON_MUSIC, squareSideLength);
				
				fm = g.getFontMetrics(fontManager.iconFont);
				// set position on the right side of the canvas
				x = canvas.getWidth() - (fm.stringWidth(ICON_MUSIC) + squareSideLength / 4);
				// max height of a 2 squares or height of the scores and music
				// icon
				y = Math.max(2 * squareSideLength, fontManager.digitalFont.getSize())
				        + fontManager.iconFont.getSize();
				
				drawTextOnCanvas(canvas, ICON_MUSIC, !properties.mute ? ICON_MUSIC : "",
				        fontManager.iconFont, x, y);
			}
			/* --- */
		}
		
		if (forceUpdate || properties.showNext != prevProperties.showNext
		        || properties.showLives != prevProperties.showLives) {
			/* Next/Lives labels */
			maxWidth = canvas.getWidth() - squareSideLength;
			fontManager.setOptimalTextFont(g, NEXT + " " + LIVES, maxWidth);
			
			x = squareSideLength / 2;
			// height is 4.75 of a squares
			y = 5 * squareSideLength - squareSideLength / 4;
			
			drawTextOnCanvas(canvas, NEXT, showNext ? NEXT : "", fontManager.textFont, x, y);
			
			fm = g.getFontMetrics(fontManager.textFont);
			// set position on the right side of the canvas
			drawTextOnCanvas(canvas, LIVES, showLives ? LIVES : "", fontManager.textFont,
			        canvas.getWidth() - fm.stringWidth(LIVES) - squareSideLength / 2, y);
			/* --- */
		}
		

        boolean updateSpeedAndLevel = forceUpdate || properties.speed != prevProperties.speed ||
                properties.level != prevProperties.level;

        if (updateSpeedAndLevel) {
			/* Speed and Level */
			fm = g.getFontMetrics(fontManager.digitalFont);
			// set the same distance to the labels from both sides
			x = (canvas.getWidth() - fm.stringWidth(NUMBER_SUBSTRATE + " " + NUMBER_SUBSTRATE)) / 2;
			// height is 9.5 of a squares
			y = 9 * squareSideLength + squareSideLength / 2 + fontManager.digitalFont.getSize();
			
			if (forceUpdate || properties.speed != prevProperties.speed) {
				drawTextOnCanvas(canvas, NUMBER_SUBSTRATE, String.valueOf(properties.speed),
				        fontManager.digitalFont, x, y);
			}
			if (forceUpdate || properties.level != prevProperties.level) {
				drawTextOnCanvas(canvas, NUMBER_SUBSTRATE, String.valueOf(properties.level),
				        fontManager.digitalFont,
				        x + squareSideLength / 2 + fm.stringWidth(NUMBER_SUBSTRATE), y);
				/* --- */
			}
		}
		
		if (updateSpeedAndLevel) {
			/* Speed and Level labels */
			maxWidth = canvas.getWidth() - squareSideLength;
			fontManager.setOptimalTextFont(g, SPEED + LEVEL, maxWidth);
			
			x = squareSideLength / 2;
			y = 10 * squareSideLength // 10 of a squares
			        // and max from height of speed/level
			        + Math.max(fontManager.digitalFont.getSize(),
			        // or 2 of a squares
			                squareSideLength * 2) + fontManager.textFont.getSize();
			
			drawTextOnCanvas(canvas, SPEED, SPEED, fontManager.textFont, x, y);
			
			fm = g.getFontMetrics(fontManager.textFont);
			// set position on the right side of the canvas
			drawTextOnCanvas(canvas, LEVEL, LEVEL, fontManager.textFont,
			        canvas.getWidth() - fm.stringWidth(LEVEL) - squareSideLength / 4, y);
			/* --- */
		}
		
		if (forceUpdate || !properties.rotation.equals(prevProperties.rotation)) {
			/* Rotate label */
			// minus width of the rotate icons and space between label and icons
			maxWidth = canvas.getWidth() - (squareSideLength * 2 + squareSideLength / 4);
			fontManager.setOptimalTextFont(g, ROTATE, maxWidth);
			
			x = squareSideLength / 4;
			// height is 13.5 of a squares
			y = 13 * squareSideLength + squareSideLength / 2 + fontManager.textFont.getSize();
			
			drawTextOnCanvas(canvas, ROTATE, properties.rotation != Rotation.NONE ? ROTATE : "",
			        fontManager.textFont, x, y);
			/* --- */
			
			/* Rotate icons (left/right) */
			if (fontManager.iconFont != null) {
				fontManager.setOptimalIconFont(g, ICON_ROTATE_RIGHT, squareSideLength);
				
				fm = g.getFontMetrics(fontManager.iconFont);
				x = canvas.getWidth() - fm.stringWidth(ICON_ROTATE_RIGHT) - squareSideLength * 3
				        / 4;
				
				drawTextOnCanvas(canvas, ICON_ROTATE_RIGHT,
				        properties.rotation == Rotation.CLOCKWISE ? ICON_ROTATE_RIGHT : "",
				        fontManager.iconFont, x, y - squareSideLength / 4);
				
				int space = squareSideLength / 2;
				drawTextOnCanvas(canvas, ICON_ROTATE_LEFT,
				        properties.rotation == Rotation.COUNTERCLOCKWISE ? ICON_ROTATE_LEFT : "",
				        fontManager.iconFont, x + space, y + squareSideLength / 2);
			}
			/* --- */
		}
		
		if (forceUpdate || !properties.status.equals(prevProperties.status)) {
			/* Pause label */
			maxWidth = canvas.getWidth() / 2;
			fontManager.setOptimalTextFont(g, PAUSE, maxWidth);
			
			x = squareSideLength / 4;
			// height is 15.5 of a squares
			y = 15 * squareSideLength + squareSideLength / 2 + fontManager.textFont.getSize();
			
			drawTextOnCanvas(canvas, PAUSE, properties.status == Status.Paused ? PAUSE : "",
			        fontManager.textFont, x, y);
			/* --- */
		}
		
		if (forceUpdate || properties.showPauseIcon != prevProperties.showPauseIcon) {
			/* Pause icon */
			if (fontManager.iconFont != null) {
				fontManager.setOptimalIconFont(g, ICON_PAUSE, squareSideLength * 2);
				
				maxWidth = canvas.getWidth() / 2;
				fontManager.setOptimalTextFont(g, PAUSE, maxWidth);
				
				fm = g.getFontMetrics(fontManager.textFont);
				x = squareSideLength / 4 + fm.stringWidth(PAUSE);
				// height is 15.5 of a squares and height of the pause label
				y = 15 * squareSideLength + squareSideLength / 2 + fontManager.textFont.getSize()
				        + fontManager.iconFont.getSize();
				
				drawTextOnCanvas(canvas, ICON_PAUSE, showPauseIcon ? ICON_PAUSE : "",
				        fontManager.iconFont, x, y);
			}
			/* --- */
		}
		
		if (forceUpdate || !properties.status.equals(prevProperties.status)) {
			/* Game Over label */
			maxWidth = canvas.getWidth() - squareSideLength / 2;
			fontManager.setOptimalTextFont(g, GAME_OVER, maxWidth);
			
			x = squareSideLength / 4;
			// height is 19.75 of a squares
			y = 20 * squareSideLength - squareSideLength / 4;
			
			drawTextOnCanvas(canvas, GAME_OVER, properties.status == Status.GameOver ? GAME_OVER
			        : "", fontManager.textFont, x, y);
			/* --- */
		}
	}
	
	/**
	 * Draws one square for the insertion to the main canvas
	 * 
	 * @param fill
	 *            fill type: {@code Full}, {@code Empty} or {@code Blink}
	 * @param lineWidth
	 *            line width for a square border line
	 * @return {@code BufferedImage} for the insertion to the main canvas
	 */
	private BufferedImage drawSquare(Cell fill, float lineWidth) {
		BufferedImage image = new BufferedImage(squareSideLength, squareSideLength,
		        BufferedImage.TYPE_INT_RGB);
		Color colors[] = { emptyColor, fullColor, blinkColor };
		Color color = colors[fill.ordinal()];
		
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		clearCanvas(image, bgColor);

        float calcLineWidth;
        //decrease thickness of the line on a small screen
		if (lineWidth * 1.25f >= ((squareSideLength - lineWidth)  / 4f))
			calcLineWidth = lineWidth * 0.7f;
		else calcLineWidth= lineWidth;

		g2d.setColor(color);
		// set thickness of the line
		g2d.setStroke(new BasicStroke(calcLineWidth));
		
		// separate the distances between a squares
		int separator = Math.round(calcLineWidth * 1.75f);
		
		int left, top, width, height;
		
        //coordinates for the outer square
        left = top = separator / 2;
        width = height = squareSideLength - separator;
        
		// draw the frame
		g2d.drawRect(left, top, width, height);
		
		// draw the inner square
		int innerStart = Math.max(Math.round((squareSideLength - separator) * 0.25f),
		        Math.round(calcLineWidth + 1));
		
        //coordinates for the inner square
        left = top = separator / 2 + innerStart;
        width = height = squareSideLength - separator - innerStart * 2 + 1;
        
 		g2d.fillRect(left, top, width, height);
		
		g2d.dispose();
		
		return image;
	}
	
	/**
	 * Returns the rendered game field with aspect ratio 4/3
	 * 
	 * @param width
	 *            desired width of the canvas
	 * @param height
	 *            desired height of the canvas
	 * @param properties
	 *            main properties of the game
	 * @return canvas, containing the rendered game field
	 */
	public BufferedImage getDrawnGameField(int width, int height, GameProperties properties) {
		GameProperties newProperties = properties.clone();
		
		blinkColor = newProperties.blinkColor;
		showPauseIcon = newProperties.showPauseIcon;
		showHiScores = newProperties.showHiScores;
		showLives = newProperties.showLives;
		showNext = newProperties.showNext;
		
		// set the size of the canvas based on GAME_FIELD_ASPECT_RATIO
		Dimension d = UIUtils.getDimensionWithAspectRatio(new Dimension(width, height),
		        GAME_FIELD_ASPECT_RATIO);
		
		// calculate size of a one square
		squareSideLength = d.height / (newProperties.board.getHeight() + 1);
		
		boolean updateBoard = false;
		boolean updatePreview = false;
		boolean updateLabels = false;
		boolean updateMainCanvas = false;
		
		// checking of the need to update canvases
		if (d.width != prevWidth || d.height != prevHeight || prevProperties == null
		        || canvas == null || boardCanvas == null || previewCanvas == null) {
			updateBoard = updatePreview = updateLabels = updateMainCanvas = true;
		} else {
			if (prevProperties.blinkColor != blinkColor) {
				updateBoard = newProperties.board.hasBlinkedCell();
				updatePreview = newProperties.preview.hasBlinkedCell();
			}
            updateBoard = updateBoard || boardCanvas == null ||
                    !prevProperties.board.equals(newProperties.board);
            updatePreview = updatePreview || previewCanvas == null ||
                    !prevProperties.preview.equals(newProperties.preview);
            updateLabels = updateLabels || !prevProperties.isLabelsEquals(newProperties);
		}
		
		if (updateMainCanvas) {
			canvas = initCanvas(canvas, d.width, d.height);
		}
		
		float borderLineWidth = calcBorderLineWidth();
		if (updateBoard) {
			boardCanvas = initCanvas(boardCanvas, newProperties.board, squareSideLength,
			        borderLineWidth);
		}
		if (updatePreview) {
			previewCanvas = initCanvas(previewCanvas, newProperties.preview, squareSideLength,
			        borderLineWidth);
		}
		
		if (boardCanvas != null && previewCanvas != null) {
			// draw the board and the preview
			int space = squareSideLength / 2;	
			int boardX = space / 2;
			int boardY = (canvas.getHeight() - boardCanvas.getHeight()) / 2f > (space * 2) ?
                    space : space / 2;
			int labelX = boardX + boardCanvas.getWidth();
			int labelY = boardY;
			
			if (updateLabels) {
				int newWidth = d.width - (boardCanvas.getWidth() + space);
				int newHeight = boardCanvas.getHeight();
				
				// clear the labelsCanvas only when dimensional changes
				boolean needToClearCanvas = labelsCanvas == null
				        || newWidth != labelsCanvas.getWidth()
				        || newHeight != labelsCanvas.getHeight();
				
				labelsCanvas = initCanvas(labelsCanvas, newWidth, newHeight, needToClearCanvas);
				// append labels and icons
				drawLabelsAndIcons(labelsCanvas, newProperties, needToClearCanvas);
			}
			// add label canvas
			appendCanvas(canvas, labelsCanvas, labelX, labelY);
			
			int previewX = labelX
			// center of the label canvas
			        + (labelsCanvas.getWidth() - previewCanvas.getWidth()) / 2;
			int previewY = labelY + 5 * squareSideLength;
			
			if (updatePreview) {
				updateCanvas(previewCanvas, newProperties.preview, borderLineWidth);
			}
			// add preview canvas
			appendCanvas(canvas, previewCanvas, previewX, previewY);
			
			if (updateBoard) {
				updateCanvas(boardCanvas, newProperties.board, borderLineWidth);
				canvasSetBorder(boardCanvas, borderLineWidth);
				drawComingSoonStatus(boardCanvas, newProperties.status);
			}
			// add main board canvas
			appendCanvas(canvas, boardCanvas, boardX, boardY);
			
		}
		
		prevProperties = newProperties;
		prevWidth = d.width;
		prevHeight = d.height;
		return canvas;
	}
	
	/**
	 * Draws on the canvas the contents of the board
	 * 
	 * @param canvas
	 *            the target canvas
	 * @param board
	 *            the board whose contents need to be drawn
	 * @param borderLineWidth
	 *            line width for the border line
	 */
	private void updateCanvas(BufferedImage canvas, Board board, float borderLineWidth) {
		if (canvas == null) return;
		drawBoardOnCanvas(canvas, board, borderLineWidth);
	}
	
}
