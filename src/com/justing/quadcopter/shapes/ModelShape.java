package com.justing.quadcopter.shapes;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUtessellator;
import com.justing.quadcopter.model.Model;

public class ModelShape { 
	
	private float sizeX, sizeY, sizeZ, x, y, z;
	private float cx, cy, cz;
	private Model model;
	
	public ModelShape(Model model, float sizeX, float sizeY, float sizeZ, float cx, float cy, float cz, float x, float y, float z) {
		this.model = model;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.x = x;
		this.y = y;
		this.z = z;
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}
	
	public final void draw(GL2 gl){this.draw(gl, null, null);}
	public void draw(GL2 gl, GLU glu, GLUtessellator tobj){

		gl.glPushMatrix();
		applyTranslateAndRotate(gl);
		gl.glScalef(sizeX, sizeY, sizeZ);
		
		model.render(gl, tobj);
	    
		gl.glPopMatrix();
	}
	
	private void applyTranslateAndRotate(GL2 gl){
		gl.glTranslatef(cx, cy, cz);
		gl.glRotatef(y, 0, 1, 0);
		gl.glRotatef(x, 1, 0, 0);
		gl.glRotatef(z, 0, 0, 1);
	}
	
	public void setCoords(float cx, float cy, float cz){
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}
	
	public void setTilt(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setSize(float sizeX, float sizeY, float sizeZ){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
	}
}
