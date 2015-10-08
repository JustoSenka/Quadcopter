package com.justing.quadcopter.shapes;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

public class Disc extends TessShape{

	private float radius;
	
	public Disc(GLUquadric quad, Texture texture, float radius, float cx, float cy, float cz, float x, float y, float z) {
		this.radius = radius;
		
		super.quad = quad;
		super.texture = texture;
		super.x = x;
		super.y = y;
		super.z = z;
		super.cx = cx;
		super.cy = cy;
		super.cz = cz;
	}
	
	@Override
	public void draw(GL2 gl, GLU glu){
		
		gl.glPushMatrix();
		
		super.tryEnableAndBindTexture(gl);
		super.applyTranslateAndRotate(gl);		
        
		glu.gluDisk(quad, 0, radius, 64, 16);
        
		super.tryDisableTexture(gl);
		
        gl.glPopMatrix();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
}
