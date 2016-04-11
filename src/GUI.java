/*TODO:
 *Add more parameters/options et. c.
 *This represents a GUI which contains precisely one canvas
 */
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class GUI extends JFrame implements ActionListener{
	public TetrisCanvas canvas;
	public Game game;
	public KeyboardFocusManager keyManager;
	public JPanel settingsPanel =new JPanel();
			
	public JButton newGame = new JButton("New Game");
	public JLabel score = new JLabel();
	public JPanel speedPanel=new JPanel();
	public JButton incInterval = new JButton(">");
	public JLabel currInterval=new JLabel();
	public JButton decInterval = new JButton("<");
	public JButton pause=new JButton("Pause");
	
	public GUI(Game g) {
		game = g;
		initUI();
		update();
	}

	public void initUI() {
		initKeyboard();
		canvas = new TetrisCanvas(50, 20, 10);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		add(canvas);
		initSettingsPanel();
		add(settingsPanel);//may need to do something to make it appear in an apt place
		canvas.game = game;
		setTitle("DanTetris version " + Globals.version);
		setSize(350, 250);// allow things like this to be changed (dynamically?)
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void initSettingsPanel() {
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));	
		score.setAlignmentX(LEFT_ALIGNMENT);
		newGame.setAlignmentX(LEFT_ALIGNMENT);
		settingsPanel.add(newGame);
		settingsPanel.add(score);
		JLabel interval = new JLabel("Interval:");
		pause.setAlignmentX(LEFT_ALIGNMENT);
		settingsPanel.add(pause);
		interval.setAlignmentX(LEFT_ALIGNMENT);
		settingsPanel.add(interval);
		pause.addActionListener(this);
		initSpeedPanel();
		speedPanel.setAlignmentX(LEFT_ALIGNMENT);
		settingsPanel.add(speedPanel);
	}

	private void initSpeedPanel() {
		speedPanel.setLayout(new FlowLayout());
		speedPanel.add(decInterval);
		speedPanel.add(currInterval);
		speedPanel.add(incInterval);
		
		newGame.addActionListener(this);
		incInterval.addActionListener(this);
		decInterval.addActionListener(this);
	}

	public void displayGameOverMessage(){
		JOptionPane.showMessageDialog(this,
			    "The game is now over. Your score was " + game.noLines + ".",
			    "Game Over",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	public void update(){
		score.setText("Score:\t\t" + game.noLines);
		currInterval.setText(""+game.interval);
	}
		
	@Override
	public void actionPerformed(ActionEvent e){
		if (e.getSource()==incInterval){
			game.interval +=10;
			update();
		} else if(e.getSource()==decInterval){
			game.interval -=10;
			update();
		} else if(e.getSource()==pause){
			game.setPaused(!game.isPaused());
		} else if (e.getSource()==newGame){
			try{
				game.restart();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	///////////////Key press code////////////////
	private HashMap<KeyStroke,Action>actionMap=new HashMap<KeyStroke,Action>();

	public void initKeyboard(){
		KeyStroke left = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
		KeyStroke right = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
		KeyStroke up = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
		KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
		KeyStroke space = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
		KeyStroke pButton = KeyStroke.getKeyStroke(KeyEvent.VK_P, 0);
		KeyStroke pause = KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, 0);
		
		actionMap.put(left, new AbstractAction("move left") {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.tryToMoveLeft();
				repaint();
			}
		});
		actionMap.put(right, new AbstractAction("move right") {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.tryToMoveRight();
				repaint();
			}
		});
		actionMap.put(up, new AbstractAction("rotate") {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.tryToRotateRight();
				repaint();
			}
		});
		actionMap.put(down, new AbstractAction("space") {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.tryToRotateRight();
				repaint();
			}
		});
		actionMap.put(down, new AbstractAction("move down") {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.currentPiece.tryToMoveDown();					
				repaint();
			}
		});
		actionMap.put(space, new AbstractAction("move right down") {
			@Override
			public void actionPerformed(ActionEvent e) {
				while(game.currentPiece.tryToMoveDown())
					;
				repaint();
			}
		});
		Action pauseAction=new AbstractAction("pause") {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.setPaused(!game.isPaused());
			}
		};
		actionMap.put(pause, pauseAction);
		actionMap.put(pButton, pauseAction);
			
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher( new KeyEventDispatcher() {
		    @Override
		    public boolean dispatchKeyEvent(KeyEvent e) {
		    	KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
			    if (actionMap.containsKey(keyStroke) ) {
			    	final Action a = actionMap.get(keyStroke);
			    	final ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), null );
			    	SwingUtilities.invokeLater( new Runnable() {
			    		@Override
			    		public void run() {
			    		a.actionPerformed(ae);
			    		}
			    	}); 
			    	return true;
			    }
			    return false;
		    }
		});
	}

}