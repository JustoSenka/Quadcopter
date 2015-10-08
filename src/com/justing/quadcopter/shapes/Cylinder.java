package com.justing.quadcopter.shapes;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

public class Cylinder extends TessShape{

	private float radius, height;
	
	public Cylinder(GLUquadric quad, Texture texture, float radius, float height, float cx, float cy, float cz, float x, float y, float z) {
		this.radius = radius;
		this.height = height;
		
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
		
		glu.gluDisk(quad, 0, radius, 64, 8);
        glu.gluCylinder(quad, radius, radius, height, 64, 8);
        gl.glTranslatef(0, 0, height);
        glu.gluDisk(quad, 0, radius, 64, 8);
		
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
