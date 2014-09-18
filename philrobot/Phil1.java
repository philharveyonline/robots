package philrobot;
import robocode.*;
import robocode.Robot;

import java.awt.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Phil1 - a robot by Phil
 */
public class Phil1 extends Robot
{
    private int _direction = 1;

	/**
	 * run: Phil1's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here


		setColors(Color.BLACK, Color.YELLOW, Color.YELLOW); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
            turn();
		}
	}

    private void turn() {
        //ahead(10);
        spin();
    }

    private void spin() {
        turnRight(10 * _direction);
    }

    /**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

        turnRight(e.getBearing());
        if(e.getDistance() > 120) {
            ahead(e.getDistance() / 2);
            scan();
        }
        else if(e.getDistance() > 20) {
            fire(1);
        }
        else {
            fire(4);
        }
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
        turn();
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}
}
