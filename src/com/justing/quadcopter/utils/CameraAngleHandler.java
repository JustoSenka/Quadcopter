package com.justing.quadcopter.utils;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;

import com.justing.quadcopter.server.Server;

public class CameraAngleHandler {
	
	public final static float MOUSE_SENSITIVITY = 1.5f;
	public final static float PHONE_TILTING_SENSITIVITY = 0.8f;
	public final static float MAX_MOVEMENT_SPEED = 1000;
	public final static float MAX_ROTATION_SPEED = 1;
	public final static float MAX_TILT = 45; // Degrees
	public final static boolean CAMERA_DELAY_ON = true; // Delay camera movement when quad moves fast
	public static final int FPS = 50;
	
	/**Note:
	 * x and y is angle in radians, because GLU.lookAt() needs sins of theese valuses, Math.sin takes radians.
	 * gl.rotate() takes angles as degrees.
	 * 
	 */
	private float x = 0, y = 0;  // Rotation modifiers (Radians)
	private float delayedX, delayedY;  // Delayed rotation modifiers for camera when quad moves fast (Radians)
	private float lockedX; // locked x and y for camera when started rotating (Radians)
	private int isLocekd; // used to deside whether lock x and y or not.
	
	private float cx = 85, cy = 0.2f, cz = 110;   // Coordinates of centered object
	private float tiltX, tiltZ;  // Quad tilting rotation modifiers (Degrees)
	
	private float distanceFromObject = 30;
	
	private float speedCx, speedCy, speedCz;  // Speed for movement (C stands for coords)
    private float speedX, speedY;   // Speed for rotation
	private float speedTX, speedTZ; // Speed for tilting
	
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
					
					calculateQuadTilting();
					applyCameraRotation();
					applyCameraMovement();
					
					calculateDelayedCameraRotation();
					
					try {
						Thread.sleep((int)(1000 / FPS));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			private void calculateQuadTilting() {
				
				// 16% of speed will be left after 1 second (Running on 50 FPS)
				speedTX *= 0.9065f; //0.965f;
				speedTZ *= 0.9065f; //0.965f;
				
				speedTX += (serv.getY() * PHONE_TILTING_SENSITIVITY - tiltX) * 0.01f;
				speedTZ += (serv.getX() * PHONE_TILTING_SENSITIVITY - tiltZ) * 0.01f;
				
				speedTX = (float) ((speedTX < 10) ? speedTX : 10);  
				speedTZ = (float) ((speedTZ < 10) ? speedTZ : 10);
			
				tiltX += speedTX;
				tiltZ += speedTZ;
				
				tiltX = (tiltX < MAX_TILT) ? tiltX : MAX_TILT;
				tiltX = (tiltX > -MAX_TILT) ? tiltX : -MAX_TILT;
				tiltZ = (tiltZ < MAX_TILT) ? tiltZ : MAX_TILT;
				tiltZ = (tiltZ > -MAX_TILT) ? tiltZ : -MAX_TILT;
				
				if (tiltX == MAX_TILT || tiltX == -MAX_TILT) speedTX = 0;
				if (tiltZ == MAX_TILT || tiltZ == -MAX_TILT) speedTZ = 0;
			}

			private void applyCameraRotation() {
				
				calculateSpeedForRotation();
				
				// MOUSE:
		        mouseX = MouseInfo.getPointerInfo().getLocation().x;
		        mouseY = MouseInfo.getPointerInfo().getLocation().y;
				
				x += (width / 2 - mouseX) * MOUSE_SENSITIVITY / width;
				y += (height / 2 - mouseY) * MOUSE_SENSITIVITY / width;
				
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
				final float speedModifier = (0.4f + aac.getShift() * 5.f) / 500;
				
				calculateSpeedForMovement();  
				
				cx += speedCx * speedModifier;
				cy += speedCy * speedModifier;
				cz += speedCz * speedModifier;
			}

			private void calculateSpeedForMovement() {
				
				// 44.5% of speed will be left after 1 second (Running on 50 FPS)
				speedCx *= 0.960f;
				speedCy *= 0.960f;
				speedCz *= 0.960f;
				
				speedCx -= (float)(tiltX * Math.cos(x) + tiltZ * Math.sin(x));
				speedCy += (float)(serv.getLift() * 50);
				speedCz -= (float)(tiltZ * Math.cos(x) - tiltX * Math.sin(x));				
				
				speedCx = (speedCx < MAX_MOVEMENT_SPEED) ? speedCx : MAX_MOVEMENT_SPEED;  
				speedCy = (speedCy < MAX_MOVEMENT_SPEED) ? speedCy : MAX_MOVEMENT_SPEED;  
				speedCz = (speedCz < MAX_MOVEMENT_SPEED) ? speedCz : MAX_MOVEMENT_SPEED;
			}
			
			private void calculateDelayedCameraRotation() {
				checkIfLocked();
				if (CAMERA_DELAY_ON){
					delayedX = x - (10 * speedX);
					delayedY = y - (10 * speedY);
					
					// not fully implemented, need fixes, temporary disabled
					//if (isLocekd == -1 && delayedX < lockedX) delayedX = lockedX;
					//if (isLocekd ==  1 && delayedX > lockedX) delayedX = lockedX;
					
				} else {
					delayedX = x;
					delayedY = y;
				}
			}

			private void checkIfLocked() {
				if (isLocekd != serv.getTurn() && serv.getTurn() != 0){
					isLocekd = serv.getTurn();
					lockedX = x;
				} else if (serv.getTurn() == 0){
					isLocekd = 0;
				}
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
	public float getTX() {return tiltX;}
	public float getTZ() {return tiltZ;}
	public float getDelayedX() {return delayedX;}
	public float getDelayedY() {return delayedY;}
	public float getDistance() {return distanceFromObject;}
}
