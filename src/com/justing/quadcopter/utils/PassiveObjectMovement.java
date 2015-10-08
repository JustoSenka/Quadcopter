package com.justing.quadcopter.utils;

import java.util.List;

import com.justing.quadcopter.server.Server;
import com.justing.quadcopter.shapes.ModelShape;

public class PassiveObjectMovement {
	
	public static final int FPS = 50;
	private float x = 0, y = 0, cx = 0, cy = 0, cz = 0;
	private boolean run = false;
	private List<ModelShape> ms;
	Thread t = null;
	
	private CameraAngleHandler cah = CameraAngleHandler.getInstance();
	private Server serv = Server.getInstance();
	
	private final static PassiveObjectMovement Instance = new PassiveObjectMovement();
	public static final PassiveObjectMovement getInstance() { return Instance; }

	private PassiveObjectMovement(){
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (run){
					
//					x += 0.05f;
//					y += 0.05f;
//					
//					cx += 0.4f;
//					cy += 0.4f;
//					cz += 0.4f;
					
					//ms.get(0).setCoords(cah.getCx(), cah.getCy(), cah.getCz()); // THIS IS NOW NEAR 'GLU LOOK AT'
					
					ms.get(0).setTilt(serv.getY() / 3.f, (float) Math.toDegrees(cah.getX()) - 90, serv.getX() / 3.f);
					
					try {
						Thread.sleep((int)(1000 / FPS));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void startThread(List<ModelShape> ms){
		this.ms = ms;
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
}
