package SnakeGame;

import javafx.scene.input.KeyCode;

public class Keyboard {

	/** Last-pressed KeyCode */
	private static KeyCode lastKeyCode = KeyCode.ENTER;

	/** You shall not create a Keyboard object */
	private Keyboard() { }

	/**
	 * Automatically invoked by the keyboard event listener,
	 * stores the last-pressed KeyCode.
	 */
	public static void storeLastKeyCode(KeyCode keycode) { lastKeyCode = keycode; }

	/** 
	 * Get the last typed KeyCode.
	 * @return the last pressed KeyCode.
	 */
	public static KeyCode getLastKeyCode() { return lastKeyCode; }


}
