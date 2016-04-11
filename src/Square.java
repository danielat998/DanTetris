import java.awt.Color;

public class Square{
  public Color colour;
  public int x;
  public int y;
  public Piece parentPiece;
  public Square(int givenX, int givenY, Color givenColour, Piece parent){
    colour = givenColour;
    x = givenX;
    y = givenY;
    parentPiece = parent;
  }
  public void moveDown(){
	  y++;
  }
  public void moveLeft(){
	  x--;
  }
  public void moveRight(){
	  x++;
  }
  public int getX(){
	  return x;
  }
  public int getY(){
	  return y;
  }
  public Square clone(){
	  return new Square(x,y,colour,parentPiece);
  }
}