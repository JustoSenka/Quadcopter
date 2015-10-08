package com.justing.quadcopter.server;

import java.net.*;
import java.io.*;

public class Server
{
	public static final int FPS = 50;
	public static final int timeout = 10000;
	
	private ServerSocket serverSocket = null;
	
	private int x = 0, y = 0, z = 0;
	private int turnLeft = 0, turnRight = 0, up = 0, down = 0;
	
	public float renderingFPS = 0; // Console Out ONLY
	
	private boolean run = false;
	Thread t = null;
	
	private final static Server Instance = new Server();
	public static final Server getInstance() {
		return Instance;
	}

	private Server(){
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (run){
					try {
						Socket socket = serverSocket.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
						String str;
						while((str = in.readLine()) != null){
					        String[] temp = str.split(" ");
					        
							x = tryParse(temp[1]);
							y = tryParse(temp[2]);
							z = tryParse(temp[3]);
							
							down = tryParse(temp[4]);
							turnLeft = tryParse(temp[5]);
							up = tryParse(temp[6]);
							turnRight = tryParse(temp[7]);
							
							System.out.println(x + " " + y + " " + z + " ---- " + turnLeft + " <> " + turnRight + " : "
											 + up + " ^V " + down + String.format("  Rendering FPS: %.2f", renderingFPS));
				        
							try {
								Thread.sleep((int)(1000 / FPS));
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
					    }
							
						in.close();
						socket.close();
					} catch (SocketTimeoutException s) {
						System.out.println("Socket timed out!");
						break;
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}
			}

			private int tryParse(String str) {
				int i = 0;
				try {
					i = Integer.parseInt(str);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					 i = 0;
				}
				return i;
			}
		});
	}
	
	public void createSocket(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(timeout * 1000);
	}
	
	public void startThread(){
		if (serverSocket == null) throw new NullPointerException("Cannot start server, socket is not yet created.");
		run = true;
		t.start();
	}
	public void stopThread(){
		run = false;
	}

	
//	private float getServerPing() {
//		long curr = System.currentTimeMillis();
//		float FPS = 1000.0f / (float)(curr - lastTime);
//		lastTime = curr;
//		return FPS;
//	}
	
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getZ() {return z;}
	
	public int getTurn() {
		if (turnLeft == 1) return -1;
		else if (turnRight == 1) return 1;
		else return 0;
	}
	
	public int getLift() {
		if (down == 1) return -1;
		else if (up == 1) return 1;
		else return 0;
	}
}
