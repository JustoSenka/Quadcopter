package com.justing.quadcopter.utils;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;

import com.justing.quadcopter.server.Server;

public class CameraAngleHandler {
	
	public final static float SENSITIVITY = 1.5f;
	public final static float MAX_MOVEMENT_SPEED = 4000;
	public final static float MAX_ROTATION_SPEED = 1;
	public static final int FPS = 50;
	
	/**Note:
	 * x and y is angle in radians, because GLU.lookAt() needs sins of theese valuses, Math.sin takes radians.
	 * gl.rotate() takes angles as degrees.
	 * 
	 */
	private float x = 0, y = 0;  // Rotation modifiers
	private float cx = 85, cy = 0.2f, cz = 110;   // Coordinates of centered object
	private float distanceFromObject = 30;
	
	private float speedCx, speedCy, speedCz;  // Speed for movement (C stands for coords)
	private float speedX, speedY;   // Speed for rotation
	
	private float mouseX, mouseY;  // temp mouse pointer data
	private float width, height;  
	private Robot r;
	
	private boolean run = false;
	private Thread t = null;
	
	private ArrowActionContainer aac = ArrowActionContainer.getInstance();
	private Server serv = Server.getInstance();
	
	private final static CameraAngleHandler Instance = new CameraAngleHandler();
	public final static CameraAngleHandler getInstance() {return Instance;}

	private CameraAngleHandler(){
		
		try {
			r = new Robot();
		} catch (AWTException e1) {
		}

		width = (float) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (float) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		mouseX = width / 2;
		mouseY = height / 2;
		
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (run){
					
					applyCameraRotation();
					applyCameraMovement();
					
					try {
						Thread.sleep((int)(1000 / FPS));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			private void applyCameraRotation() {
				
				calculateSpeedForRotation();
				
				// MOUSE:
		        mouseX = MouseInfo.getPointerInfo().getLocation().x;
		        mouseY = MouseInfo.getPointerInfo().getLocation().y;
				
				x += (width / 2 - mouseX) * SENSITIVITY / width;
				y += (height / 2 - mouseY) * SENSITIVITY / width;
				
				// KEYBOARD:
				x -= (float)(aac.getDx() * 0.02f);
				y += (float)(aac.getDy() * 0.02f);
				
				// PHONE:
				x += speedX; 
				
				
				r.mouseMove((int)width / 2, (int)height / 2);
			}

			private void calculateSpeedForRotation() {
				
				// 21% of speed will be left after 1 second (Running on 50 FPS)
				speedX *= 0.97f;
				//speedY *= 0.984f;
				
				speedX -= (float)(serv.getTurn() * 0.0012f); 
				//speedY += (float)(serv.getLift() * 50);
				
				speedX = (speedX < MAX_ROTATION_SPEED) ? speedX : MAX_ROTATION_SPEED;  
				//speedY = (speedCy < MAX_SPEED) ? speedY : MAX_SPEED;  
			}
			
			private void applyCameraMovement() {
				final float speedModifier = (0.4f + aac.getShift() * 5.f) / 2000;
				
				calculateSpeedForMovement();  
				
				cx += speedCx * speedModifier;
				cy += speedCy * speedModifier;
				cz += speedCz * speedModifier;
			}

			private void calculateSpeedForMovement() {
				
				// 44.5% of speed will be left after 1 second (Running on 50 FPS)
				speedCx *= 0.984f;
				speedCy *= 0.984f;
				speedCz *= 0.984f;
				
				speedCx -= (float)(serv.getY() * Math.cos(x) + serv.getX() * Math.sin(x));
				speedCy += (float)(serv.getLift() * 50);
				speedCz -= (float)(serv.getX() * Math.cos(x) - serv.getY() * Math.sin(x));
				
				speedCx = (speedCx < MAX_MOVEMENT_SPEED) ? speedCx : MAX_MOVEMENT_SPEED;  
				speedCy = (speedCy < MAX_MOVEMENT_SPEED) ? speedCy : MAX_MOVEMENT_SPEED;  
				speedCz = (speedCz < MAX_MOVEMENT_SPEED) ? speedCz : MAX_MOVEMENT_SPEED;
			}
		});
	}
	
	public void startThread(){
		run = true;
		t.start();
	}
	public void stopThread(){
		run = false;
	}

	public float getX() {return x;}
	public float getY() {return y;}
	public float getCx() {return cx;}
	public float getCy() {return cy;}
	public float getCz() {return cz;}
	public float getDistance() {return distanceFromObject;}
}
