import java.awt.Color;
import java.util.Random;

public class Piece {
	public Square[] squares = new Square[4];
	public Color colour;
	public Game game;
	public Square[][] array;
	public int rotation;
	public int pieceType;
	/* Piece Types:
	 * 0:  X    1: XX  2: X   3:  X   4: X   5: X   6:  X
	 *    XX       XX     XX     XX      X      X       X
	 *     X               X     X       X      XX     XX
	 *                                   X
	 */                                   

	public Piece(Game g) throws GameOverException {
		game = g;
		pieceType = (new Random()).nextInt(7);
		colour = getRandomColour();
		if(!assignSquares())
			throw new GameOverException("Unable to place a new square");
		array = game.array;
	}

	private Color getRandomColour() {
		Random r = new Random();
		return new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
	}

	public void updateGameArray() throws Exception {
		game.array[squares[0].getX()][squares[0].getY()] = squares[0];
		game.array[squares[1].getX()][squares[1].getY()] = squares[1];
		game.array[squares[2].getX()][squares[2].getY()] = squares[2];
		game.array[squares[3].getX()][squares[3].getY()] = squares[3];
		//need to investigate whether these two methods can be merged/modified for efficiency...
		game.cleanMatrix();
	}

	public void endPiece() {
	}

	private boolean assignSquares() {
		switch (pieceType) {
		case 0:
			squares[0]=new Square(5,0,colour,this);
			squares[1]=new Square(4,1,colour,this);
			squares[2]=new Square(5,1,colour,this);
			squares[3]=new Square(5,2,colour,this);
			break;
		case 1:
			squares[0]=new Square(4,0,colour,this);
			squares[1]=new Square(4,1,colour,this);
			squares[2]=new Square(5,0,colour,this);
			squares[3]=new Square(5,1,colour,this);
			break;
		case 2:
			squares[0]=new Square(4,0,colour,this);
			squares[1]=new Square(4,1,colour,this);
			squares[2]=new Square(5,1,colour,this);
			squares[3]=new Square(5,2,colour,this);
			break;
		case 3:
			squares[0]=new Square(4,1,colour,this);
			squares[1]=new Square(4,2,colour,this);
			squares[2]=new Square(5,0,colour,this);
			squares[3]=new Square(5,1,colour,this);
			break;
		case 4:
			squares[0]=new Square(4,0,colour,this);
			squares[1]=new Square(4,1,colour,this);
			squares[2]=new Square(4,2,colour,this);
			squares[3]=new Square(4,3,colour,this);
			break;
		case 5:
			squares[0]=new Square(4,0,colour,this);
			squares[1]=new Square(4,1,colour,this);
			squares[2]=new Square(4,2,colour,this);
			squares[3]=new Square(5,2,colour,this);
			break;
		case 6:
			squares[0]=new Square(5,0,colour,this);
			squares[1]=new Square(5,1,colour,this);
			squares[2]=new Square(5,2,colour,this);
			squares[3]=new Square(4,2,colour,this);
		}
		for(Square s:squares)
			if(game.array[s.x][s.y]!=null)
				return false;//i.e game over; unable to position new piece
		return true;
	}

	public boolean tryToMoveDown(){
		for (Square s:squares){
			if(s.getY()==array[0].length-1)
				return false;
			if(s.getY()!=array[0].length-1 && array[s.getX()][s.getY() +1] !=null
					                       && array[s.getX()][s.getY() +1].parentPiece != this)
				return false;
		}
		for (Square s: squares)
			s.moveDown();
		try {
			updateGameArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean tryToMoveLeft(){
		for (Square s : squares) {
			if (s.getX() == 0)
				return false;
			if (s.getX() != 0 && array[s.getX() - 1][s.getY()] != null
					&& array[s.getX() - 1][s.getY()].parentPiece != this)
				return false;
		}
		for (Square s : squares)
			s.moveLeft();
		return true;
	}

	public boolean tryToMoveRight(){
		for (Square s : squares) {
			if (s.getX() == game.array[0].length)
				return false;
			if (s.getX() != game.array[0].length
					&& array[s.getX() + 1][s.getY()] != null
					&& array[s.getX() + 1][s.getY()].parentPiece != this)
				return false;
		}
		for (Square s : squares)
			s.moveRight();
		return true;
	}


	public void tryToRotateRight(){
		int[][] matrix = getRotationMatrix();
		boolean canRotate=true;
		for (int i = 0; i < 4; i++){
			if (!isFree(squares[i].x + matrix[rotation][2 * i],
				       squares[i].y + matrix[rotation][2 * i + 1])){
				canRotate=false;
			}
		}
		if(canRotate){
			for (int i=0;i<4;i++){
				squares[i].x += matrix[rotation][2 * i];
				squares[i].y += matrix[rotation][2 * i + 1];
			}
			if (rotation < 3)
				rotation++;
			else
				rotation = 0;
			game.cleanMatrix();
		}//if
	}

	private boolean isFree(int x, int y) {
		if (x < 0 || x > array.length || y < 0 || y > array[0].length)
			return false;
		if (array[x][y] != null && array[x][y].parentPiece != this)
			return false;
		return true;
	}


	public int[][] getRotationMatrix(){
		switch (pieceType){
			case 0: return piece0RotationMatrix;
			case 1: return piece1RotationMatrix;
			case 2: return piece2RotationMatrix;
			case 3: return piece3RotationMatrix;
			case 4: return piece4RotationMatrix;
			case 5: return piece5RotationMatrix;
			case 6: return piece6RotationMatrix;
			default:return null;
		}
	}

	private int[][] piece0RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{0,0,0,0,0,0,1,-1},
		{0,0,1,1,0,0,0,0},
		{-1,1,0,0,0,0,0,0},
		{1,-1,-1,-1,0,0,-1,1}
	};
	private int[][] piece1RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0}
	};
	private int[][] piece2RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{ 2, 1, 0, 1,0,0,0,0},
		{-2,-1, 0,-1,0,0,0,0},
		{ 2, 1, 0, 1,0,0,0,0},
		{-2,-1, 0,-1,0,0,0,0}
	};
	private int[][] piece3RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{0,0,2,0,0,2,0,0},
		{0,0,-2,0,0,-2,0,0},
		{0,0,2,0,0,2,0,0},
		{0,0,-2,0,0,-2,0,0}

	};
	private int[][] piece4RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{-1,1,0,0,1,-1,2,-2},
		{1,-1,0,0,-1,1,-2,2},
		{-1,1,0,0,1,-1,2,-2},
		{1,-1,0,0,-1,1,-2,2}
	};
	private int[][] piece5RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{1,1,0,0,-1,-1,-2,0},
		{-1,1,0,0,0,-1,1,-2},
		{-1,-1,0,0,2,1,1,0},
		{1,-1,0,0,-1,1,0,2}
	};
	private int[][] piece6RotationMatrix = {
		//ax,ay,bx,by,cx,cy,dx,dy, 
		{1, 1, 0,0,-1,-1,0,-2},
		{-1,1, 0,0,1,-1,2,0},
		{-1,-1,0,0,1,1 ,0,2},
		{1,-1, 0,0,-1,1,-2,0}
	};
}