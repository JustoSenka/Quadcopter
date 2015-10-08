package com.justing.quadcopter.main;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.justing.quadcopter.server.Server;
import com.justing.quadcopter.utils.ArrowActionContainer;
import com.justing.quadcopter.utils.CameraAngleHandler;

public class Main {

	public final static int FPS = 50;
	public final static int SERVER_PORT = 6066;
	
	public static void main(String[] args) {
		final GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
		
		final GLCanvas glcanvas = createGlCanvas(capabilities);
		final JFrame frame = createFrame(glcanvas);
		
		setFrameFullscreen(frame);
		showFrame(glcanvas, frame);
		
		runServer(SERVER_PORT);
		
		makeMousePointerInvisible(frame);
		
		ArrowActionContainer.getInstance().configureKeyBindings(frame);
		CameraAngleHandler.getInstance().startThread();
		
		final FPSAnimator animator = new FPSAnimator(glcanvas, FPS, true);
		animator.start();
	}

	private static void runServer(int port) {
		try {
			Server.getInstance().createSocket(port);
			Server.getInstance().startThread();
		} catch (IOException e) {
			System.err.println("Error starting server.");
			e.printStackTrace();
		}
	}

	private static void showFrame(final GLCanvas glcanvas, final JFrame frame) {
		frame.getContentPane().add(glcanvas);
		frame.setVisible(true);
	}

	private static void makeMousePointerInvisible(final JFrame frame) {
		frame.setCursor(frame.getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));
	}

	private static void setFrameFullscreen(final JFrame frame) {
		frame.setUndecorated(true);     // no decoration such as title bar
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // full screen mode
	}

	private static JFrame createFrame(GLCanvas glcanvas) {
		final JFrame frame = new JFrame("OpenGl Example");
		frame.getContentPane().setPreferredSize( Toolkit.getDefaultToolkit().getScreenSize());
		frame.setSize(frame.getContentPane().getPreferredSize());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}

	private static GLCanvas createGlCanvas(final GLCapabilities capabilities) {
		final GLCanvas glcanvas = new GLCanvas(capabilities);
		glcanvas.addGLEventListener(new Renderer());
		glcanvas.setSize(800, 600);
		return glcanvas;
	}

}
