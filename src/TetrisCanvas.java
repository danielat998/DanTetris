import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;

public class TetrisCanvas extends JPanel{
	private static final long serialVersionUID = -6398559156603039205L;
	public final int SQUARE_WIDTH;
	public final int PANEL_WIDTH;
	public final int PANEL_HEIGHT;
	public Game game;

	public TetrisCanvas(int width, int panelH, int panelW) {
		SQUARE_WIDTH = width;
		PANEL_HEIGHT = panelH;
		PANEL_WIDTH = panelW;
		setPreferredSize(new Dimension(panelW * SQUARE_WIDTH, panelH
				* SQUARE_WIDTH));
		setBackground(Color.BLACK);
	}

	// we could also allow images et. c. at a later date...
	public void plotSquare(Square square, Graphics g) {
		if (square != null) {// square will be null if nothing there
			Color oldColour=g.getColor();
			g.setColor(square.colour);
			g.fillRect(square.x * SQUARE_WIDTH, square.y * SQUARE_WIDTH,
					SQUARE_WIDTH, SQUARE_WIDTH);
			g.setColor(oldColour);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		plotBoard(game.array, g);
	}

	public void plotBoard(Square[][] board, Graphics g) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null)
					plotSquare(board[i][j], g);
			}
		}
	}
	
	public void clearBoard(){
		Graphics g = getGraphics();
		for (int i = 0; i < game.array.length; i++) {
			for (int j = 0; j < game.array[0].length; j++) {
				g.setColor(Color.black);
				g.fillRect(i * SQUARE_WIDTH, j * SQUARE_WIDTH,
						SQUARE_WIDTH, SQUARE_WIDTH);
			}
		}
	}
}