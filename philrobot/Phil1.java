package philrobot;
import robocode.*;

import java.awt.*;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Phil1 - a robot by Phil
 */
public class Phil1 extends AdvancedRobot
{
	public void run() {
		setColors(Color.BLACK, Color.YELLOW, Color.YELLOW); // body,gun,radar

		while(true) {
            setTurnRight(10000);
            setMaxVelocity(5);
            // Start moving (and turning)
            ahead(10000);
        }
	}

    private void spin() {
        turnRight(30);
    }

    private boolean foundVictim(ScannedRobotEvent e) {
        final boolean retVal;
        if(getEnergy() / e.getEnergy() < 0.2) {
            retVal = false;
        }
        else if(e.getDistance() > 500) {
            retVal = false;
        }
        else if (e.getVelocity() > 4) {
            retVal = false;
        }
        else {
            retVal = true;
        }

        out.println("foundVictim returning " + retVal + " for ScannedRobotEvent" + e);
        return retVal;
    }
    /**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

        if(!foundVictim(e)) {
            return;
        }
        stop();
        turnRight(e.getBearing());
        if(e.getDistance() > 100 && e.getDistance() < 300) {
            out.println("Approaching " + e.getName() + " which has distance " + e.getDistance());
            ahead(e.getDistance() / 2.0);
            scan();
        }
        else if(e.getDistance() > 40) {
            out.println("Firing lightly at " + e.getName() + " which has distance " + e.getDistance());
            fire(1);
        }
        else {
            out.println("Firing strongly at " + e.getName() + " which has distance " + e.getDistance());
            fire(3);
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
        }
	}

	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		back(20);
        spin();
        spin();
	}
}
