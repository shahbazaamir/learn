package move.mouse;

import java.awt.Robot;

public class MoveMouse {

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void moveTo(int x,int y) {

		try{
			Robot robot = new Robot();
			robot.mouseMove(400,400);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
