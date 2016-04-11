public class Game {
	public GUI gui;
	public Square[][] array;
	public Piece currentPiece;
	public int noLines=0;
	public int interval=500;
	private boolean isPaused=false;
	private boolean musicOn=true;
	private boolean incrementalSpeedUp = true;
	private int minInterval=100;
	private int incrementalSpeedUpNoLines=5;
	private int incrementalSpeedUpDecrement=50;
	private MusicPlayer musicPlayer =new MusicPlayer();
	
	public Game() {
		gui = new GUI(this);
		array = new Square[10][20];// TEMPORARY!!!!
	}

	public void restart() throws Exception{
		array= new Square[10][20];
		gui.canvas.clearBoard();
		isPaused=false;
		initGame();
		noLines = 0;
		try {
			startGame();
		} catch (GameOverException e){
			e.printStackTrace();
		}
	}
	
	public void initGame() throws GameOverException{
		currentPiece = new Piece(this);
		if(musicOn){
			musicPlayer.setMusic(0/*musicPlayer.Tunes.DEFAULT*/);//temporary, this could be operated fr
			musicPlayer.play();
		}
	}
		
	public void startGame()throws Exception{
		while (true) {// note that this a primitive solution and we could later
			// modify to use a Timer or something similar
			try {Thread.sleep(interval);} catch (InterruptedException e) {e.printStackTrace();}
			if(isPaused) continue;
			if(currentPiece.tryToMoveDown())
				;
			else {
				try{
					currentPiece.endPiece();
					currentPiece = new Piece(this);
				} catch (GameOverException e){
					gui.displayGameOverMessage();
					break;
				}
			}//else
			clearLinesAndMoveDown();
			gui.canvas.repaint();
			System.out.println("Still in loop");
		}//while true
		System.out.println("escaped from loop");
	}
	
	private void clearLinesAndMoveDown() {
		//loop through lines
		for (int i=0;i<array[0].length;i++){
			boolean complete=true;
			//loop through cols
			for(int j=0;j<array.length;j++){
				if(array[j][i]==null)
					complete=false;
			}
			if(complete){
				clearLine(i);
				updateNoLines();
			}
		}
	}

	private void updateNoLines(){
		noLines++;
		if (noLines%incrementalSpeedUpNoLines == 0)
			if (interval-incrementalSpeedUpDecrement>=minInterval)
				interval -= incrementalSpeedUpDecrement;
		gui.update();
	}
	
	private void clearLine(int line) {
		for (int i=0;i<array.length;i++){
			array[i][line] = null;
		}
		for (int i=line-1;i>=0;i--){
			for(int j=0;j<array.length;j++){
				if (array[j][i]!=null){
					array[j][i].moveDown();
					array[j][i+1]=array[j][i];
				}
			}
		}
		cleanMatrix();
	}

	public void cleanMatrix() {
		for (int i = 0; i < array.length; i++){
			for (int j = 0; j < array[0].length;j++){
				Square tmp;
				if (array[i][j] != null){
					tmp = array[i][j];
					array[i][j] = null;
					array[tmp.getX()][tmp.getY()] = tmp;
				}
			}
		}
	}

	public void tryToMoveLeft() {
		currentPiece.tryToMoveLeft();
	}
	
	public void tryToMoveRight(){
		currentPiece.tryToMoveRight();
	}

	public void tryToMoveDown(){
		currentPiece.tryToMoveDown();
	}
	
	public void tryToRotateRight(){
		currentPiece.tryToRotateRight();
	}
	
	public boolean isPaused(){
		return isPaused;
	}
	public void setPaused(boolean p){
		isPaused=p;
	}
	
	public boolean isMusicPlaying(){
		return musicOn;
	}
	public void toggleMusic(boolean on){
		if (musicOn)
			musicPlayer.stopPlaying();
		else
			musicPlayer.play();
		musicOn=!musicOn;
	}
	public static void main(String[] args) throws Exception {
		Game g = new Game();
		g.initGame();
		g.startGame();
	}
	
}