package SnakeGame;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class Game extends Application implements Runnable {

	// ***************************************************************************** //
	// *                                                                           * //
	// *                      EDIT CONSTANTS AS YOU LIKE                           * //
	// *                                                                           * //
	// ***************************************************************************** //
	/** Speed of the game */
	static public final long DELAY_MS = 300L;

	/** Graphical window title */
	static public String TITLE = "Game";

	/** Background Color */
	static public final Color BG_COLOR = Color.WHITE;

	/** The size of all the elements building up the grid */
	static public final double ELEMENT_SIZE = 32;
	
	/** The number of lines/columns in the grid */
	static public final int GRID_SIZE = 24;

	// ***************************************************************************** //
	// *                                                                           * //
	// *                   DO NOT EDIT THE FOLLOWING ATTRIBUTES                    * //
	// *                                                                           * //
	// ***************************************************************************** //

	/** Graphical window size (square) */
	static public final double WINDOW_SIZE = GRID_SIZE*ELEMENT_SIZE;
	
	/** Root of the Java FX scene graph containing all the elements to display */
	private Group root;
	
	/** The timer for scheduling next game step */
	private Timer timer;

	// ***************************************************************************** //
	// *                                                                           * //
	// *                         DO NOT EDIT PAST HERE                             * //
	// *                                                                           * //
	// ***************************************************************************** //

	/** 
	 * Initialize the graphical display.
	 *
	 * In a Java FX application, this is a mandatory replacement of the constructor.
	 */
	@Override
	public void start(Stage primaryStage) {
		// Create the root of the graph scene, and all its children 
		root = new Group();

		// create the scene graph
		Scene scene = new Scene(root, WINDOW_SIZE, WINDOW_SIZE, BG_COLOR);
		
		// add a KeyEvent listener to the scene
		scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> Keyboard.storeLastKeyCode(event.getCode()));
		
		// terminate the Application when the window is closed
		primaryStage.setOnCloseRequest(event -> { Platform.exit(); System.exit(0); } );

		// Create the timer
		timer = new Timer();
		
		// Show a graphical window with all the graph scene content
		primaryStage.setScene(scene);
		primaryStage.setTitle(TITLE);
		primaryStage.setResizable(false);
		primaryStage.show();	

		// Schedule the first game step.
		// Usually a JavaFX game would use a TimeLine object in order to define the KeyFrames
		// of an animation.
		// However here we anticipate the fact that a Game would have to be synchronized on
		// two separate computers : they have their own clocks, and there's always a drift
		// (or skew) between two clocks.
		// So, we decide to use the good old Timer instead, and don't use a strictly periodic
		// TimerTask : the next delay will only be scheduled at the end of a game step,
		// allowing the timers on two different computers to roughly synchronize when 
		// exchanging messages.
		timer.schedule(new GameTimerTask(this), DELAY_MS);
	}	
	

	private class GameTimerTask extends TimerTask {
		private Runnable gameStep;
		
		public GameTimerTask(Runnable gameStep) { this.gameStep = gameStep; }

		// The JavaFX thread is the only one that should draw on screen.
		// This run method will be executed by a timer thread,
		// so we gently ask the JavaFX to run our gameStep when he has
		// finished its other duties (hopefully as soon as possible)
		public void run() {	Platform.runLater(gameStep); }

	}

	// This is the run method that will be executed roughly periodically by the JavaFX thread.
	// It could be much more optimized wrt to the graph scene updating,
	// but this should be enough for simple games.
	final public void run() {
		root.getChildren().setAll(gameStep());
		timer.schedule(new GameTimerTask(this), DELAY_MS);		
	}
	
	/**
	 * Implement your own gameStep method for your game.
	 * 
	 * @return a Collection of Node that will form the new JavaFX graph scene.
	 * All theses nodes will be displayed on screen, except if their coordinates are
	 * outside the window bounds...
	 */
	public abstract Collection<Node> gameStep();
}
