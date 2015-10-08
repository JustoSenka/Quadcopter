package com.justing.quadcopter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUtessellator;
import com.jogamp.opengl.util.texture.Texture;

public class Model {

	private List<Face> faces = new ArrayList<>();
	private Map<Integer, MtllibResource> mtl = new HashMap<>();
	
	Model(){}
	void configureModel(List<Face> faces, Map<Integer, MtllibResource> mtl){
		this.faces = faces;
		this.mtl = mtl;
	}
	
	public void render(GL2 gl, GLUtessellator tobj){
		int key = 0, lastKey = 0;
	
		addTexParameteri(gl);
		tryEnableAndBindTexture(gl, mtl.get(key).texture);
		
		gl.glBegin(GL2.GL_TRIANGLES);
		
		for (Face f : faces){
			gl.glNormal3f(f.vn[0].x, f.vn[0].y, f.vn[0].z);
			gl.glTexCoord2f(f.vt[0].x, f.vt[0].y); gl.glVertex3f(f.v[0].x, f.v[0].y, f.v[0].z);
			gl.glTexCoord2f(f.vt[1].x, f.vt[1].y); gl.glVertex3f(f.v[1].x, f.v[1].y, f.v[1].z);
			gl.glTexCoord2f(f.vt[2].x, f.vt[2].y); gl.glVertex3f(f.v[2].x, f.v[2].y, f.v[2].z);
			
			// Change texture if necessary
			if (mtl.containsKey(++key)) {
				lastKey = key;
				
				gl.glEnd();
				
				tryDisableTexture(gl, mtl.get(lastKey).texture);
				
				addTexParameteri(gl);
				tryEnableAndBindTexture(gl, mtl.get(key).texture);
			
				gl.glBegin(GL2.GL_TRIANGLES);
			}
			
		}
		
		gl.glEnd();
		tryDisableTexture(gl, mtl.get(lastKey).texture);
	}
	private void addTexParameteri(GL2 gl) {
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glEnable(GL2.GL_TEXTURE_2D);
	}
	
	private void tryEnableAndBindTexture(GL2 gl, Texture texture){
		try {
			texture.enable(gl);
			texture.bind(gl);
		} catch (GLException e) {
			e.printStackTrace();
			System.err.println("gl error on texture bind");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.err.println("No texture on: " + this.toString());
		}
	}

	private void tryDisableTexture(GL2 gl, Texture texture){
		try {
			texture.disable(gl);
		} catch (GLException e) {
			e.printStackTrace();
			System.err.println("gl error on texture bind");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.err.println("No texture on: " + this.toString());
		}
	}
	
	// Private Classes for Model ---------------------------
	
	protected class Face {
		protected Vertice[] v = new Vertice[3];
		protected Vertice[] vn = new Vertice[3];
		protected VerticeTexture[] vt = new VerticeTexture[3];
		
		protected Face(Vertice[] v, VerticeTexture[] vt, Vertice[] vn){
			this.v = v;
			this.vn = vn;
			this.vt = vt;
		}
	}
	
	protected class MtllibResource {
		protected Texture texture;
		protected int start;
		protected String label;
		
		protected MtllibResource(String label){
			this.label = label;
		}

		public final String getLabel() {
			return label;
		}

		public final void setTexture(Texture texture) {
			this.texture = texture;
		}

		public final void setStart(int start) {
			this.start = start;
		}
	}
	
	// Static helper methods
	
	public final static float strToFloat(String str){
		if (str.equals("")) str = "0";
		float f = 0;
		
		try {
			f = Float.parseFloat(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return f;
	}
}
