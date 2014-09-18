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
    private int _spinSize = 30;

	/**
	 * run: Phil1's default behavior
	 */
	public void run() {
		setColors(Color.BLACK, Color.YELLOW, Color.YELLOW); // body,gun,radar

		while(true) {
            turn();
		}
	}

    private void turn() {
        ahead(30);
        spin();
    }

    private void spin() {
        turnRight(_spinSize * _direction);
    }

    /**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

        turnRight(e.getBearing());
        if(e.getDistance() > 200) {
            ahead(e.getDistance() / 2);
            scan();
        }
        else if(e.getDistance() > 40) {
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
        double absBearingOfBullet = Math.abs(e.getBearingRadians());
        if(absBearingOfBullet < 0.25 * Math.PI / 4.0 || absBearingOfBullet > 0.75 * Math.PI) {
            // bullet is roughly in line with our direction of travel so spin before fleeing
            turnLeft(90);
            ahead(30);
            turn();
        }
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		back(20);
        spin();
	}
}
