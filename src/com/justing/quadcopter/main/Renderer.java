package com.justing.quadcopter.main;

import java.awt.DisplayMode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.DebugGL2;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.glu.GLUtessellator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.justing.quadcopter.model.Model;
import com.justing.quadcopter.model.ModelIO;
import com.justing.quadcopter.server.Server;
import com.justing.quadcopter.shapes.ModelShape;
import com.justing.quadcopter.shapes.Shape;
import com.justing.quadcopter.shapes.Sphere;
import com.justing.quadcopter.utils.CameraAngleHandler;
import com.justing.quadcopter.utils.PassiveObjectMovement;


public class Renderer implements GLEventListener{

	
	public final static float FOV = 45.0f;
	public static DisplayMode dm, dm_old;
	private GLU glu = new GLU();

	private Server serv = Server.getInstance();
	private CameraAngleHandler cah = CameraAngleHandler.getInstance();

	private List<Shape> s = new ArrayList<>();
	private List<ModelShape> ms = new ArrayList<>();
	
	private Texture sky;
	
	private Model[] models = new Model[40];
	
	GLUquadric quad;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		applyMovementAndRotation(gl);
		applyLight(gl);
        
		for (Shape el : s){
			el.draw(gl, glu);
		}
		
		for (ModelShape el : ms){
			if (ms.indexOf(el) == 0) enableTransparency(gl);
			else disableTransparency(gl);
			
			el.draw(gl);
		}
		
		gl.glFlush();
	}

	private void applyMovementAndRotation(final GL2 gl) {
		float a = cah.getDistance();
		
		glu.gluLookAt(
			-a * Math.sin(cah.getX()) + cah.getCx(), -a * Math.sin(cah.getY())+ cah.getCy(), -a * Math.cos(cah.getX()) + cah.getCz(),
			0.0 + cah.getCx(), 0.0 + cah.getCy(), 0.0 + cah.getCz(),
			0.0, 1.0, 0.0);
		
		/** Changing here because 2 Threads doesn't work properly
		*   first it changes camera coords, only then it changes quad coords.
		*/
		ms.get(0).setCoords(cah.getCx(), cah.getCy(), cah.getCz());
		ms.get(0).setTilt(serv.getY() / 3.f, (float) Math.toDegrees(cah.getX()) - 90, serv.getX() / 3.f);
		
		//System.out.println(cah.getCx() + " " + cah.getCy() + " " + cah.getCz() + " ");
	}
	
	private void applyLight(GL2 gl) {
        // Prepare light parameters.
        float SHINE_ALL_DIRECTIONS = 1;
        float[] lightPos = {0, 40, -100, SHINE_ALL_DIRECTIONS};
        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 0.5f};
        float[] lightColorDiffuse = {1f, 1f, 1f, 0.5f};
        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 0.5f};

        // Set light parameters.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorDiffuse, 0);

        // Enable lighting in GL.
        gl.glEnable(GL2.GL_LIGHT1);
        gl.glEnable(GL2.GL_LIGHTING);

        // Set material properties.
//        float[] rgba = {0.3f, 0.5f, 1f};
        float[] rgba = {0.8f, 0.8f, 1f, 1f};
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, rgba, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 20f);
        
	}
	
	private void enableTransparency(final GL2 gl) {
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void disableTransparency(final GL2 gl) {
		gl.glDisable(GL2.GL_BLEND);
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if (height <= 0) height = 1;

		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(FOV, h, 1.0, 4000.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClearDepth(1.0f);
		
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		//gl.glEnable(GL2.GL_CULL_FACE); // don't render face backside of triangles
		//gl.glCullFace(GL2.GL_BACK); // backside
		
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		
		
		loadTextures();
		loadModels();
		setQuadParams();
		
		addTestListData();
		
		PassiveObjectMovement.getInstance().startThread(ms);
	}

	private void setQuadParams() {
		quad = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
		glu.gluQuadricNormals(quad, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(quad, GLU.GLU_OUTSIDE);
		glu.gluQuadricTexture(quad, true);
	}
		
	private void addTestListData() {
		
		s.add(new Sphere(quad, sky, 1000, 0, 0, 0, -90, 0, 0));
		
		ms.add(new ModelShape(models[1], 0.7f, 0.7f, 0.7f, cah.getCx(), cah.getCy(), cah.getCz(), 0, 0, 0));
		ms.add(new ModelShape(models[2], 12, 6, 12, 0, 0, 0, 0, 0, 0));
	}
	
	
	private void loadTextures(){
		sky = loadTexture("sky.jpg");
	}

	private void loadModels() {
		models[1] = loadModel("MilitaryQuad.obj");
		models[2] = loadModel("COD.obj");
	}

	private static Texture loadTexture(String str) {
		Texture temp = null;
		try {
			temp = TextureIO.newTexture(new File("res/tex/" + str), true);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed loading: " + str);
		}
		return temp;
	}
	
	private static Model loadModel(String str) {
		Model temp = null;
		try {
			temp = ModelIO.newModel("res/obj/" + str);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed loading: " + str);
		}
		return temp;
	}
}
