/*******************************************************************************
 * Copyright (c) 2001-2013 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 *******************************************************************************/
package philrobot;


import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

import java.awt.*;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * PhilWalls - a sample robot by Mathew Nelson, maintained by Flemming N. Larsen,
 * and modfified by Phil Harvey
 * <p/>
 * Moves around the outer edge with the gun facing in.
 */
public class PhilWalls extends Robot {

	private boolean peek; // Don't turn if there's a robot there
	private double moveAmount; // How much to move
    private double direction = 1; // 1 means clockwise, -1 means anticlockwise

	/**
	 * run: Move around the walls
	 */
    @Override
	public void run() {
		// Set colors
		setBodyColor(Color.white);
		setGunColor(Color.white);
		setRadarColor(Color.white);
		setBulletColor(Color.white);
		setScanColor(Color.white);

		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnGunRight(90);
		turnRight(90);

		while (true) {
			// Look before we turn when ahead() completes.
			peek = true;

			// Move up the wall
			ahead(moveAmount * direction);

			// Don't look while turning
			peek = false;
			// Turn to the next wall
			turnRight(90 * direction);
		}
	}

    @Override
    public void onKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == VK_P) {
			reverse();
		}
    }

	/**
	 * onHitRobot:  Move away a bit.
	 */
    @Override
    public void onHitRobot(HitRobotEvent e) {
		// If he's in front of us, set back up a bit.
		if (e.getBearing() > -90 && e.getBearing() < 90) {
			back(100);
		} // else he's in back of us, so set ahead a bit.
		else {
			ahead(100);
		}
	}

    private void reverseIfCloseAndGoingAwayFromMe(ScannedRobotEvent e) {
        double relativeHeading = e.getHeading() - getHeading();
		double enemySpeedInMyDirection = e.getVelocity() * Math.cos(Math.toRadians(relativeHeading));
        out.println("My velocity " + getVelocity() + ", enemy velocity in that direction = " + enemySpeedInMyDirection);
        if(e.getDistance() < 500 && enemySpeedInMyDirection < getVelocity() * direction) {
            reverse();
        }
    }

	private boolean inCorner() {
        Set<Point> corners = new HashSet<Point>(Arrays.asList(
                new Point(0, 0),
                new Point(0, (int)getBattleFieldHeight()),
                new Point((int)getBattleFieldWidth(), 0),
                new Point((int)getBattleFieldWidth(), (int)getBattleFieldHeight())
        ));
        for(Point corner: corners) {
            if(corner.distance(getX(), getY()) < 40) {
                return true;
            }
        }
        return false;
	}

	private void reverse() {
		if(inCorner()) {
			out.println("Not reversing because I'm in a corner");
		}
		else {
			ahead(moveAmount / 3.0);
			stop();
			direction = direction * -1;
			out.println("Reversing. Direction = " + direction);
			if(peek) {
				scan();
			}
			ahead(moveAmount * direction);
		}
	}

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        final double power;
        if(e.getDistance() < 200) {
            power = 3;
        }
        else {
            power = 1.5;
        }
        fire(power);

        reverseIfCloseAndGoingAwayFromMe(e);

		// Note that scan is called automatically when the robot is moving.
		// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
		// wall, so that we do not start moving up it until it's gone.
		if (peek) {
			scan();
		}
	}
}
				