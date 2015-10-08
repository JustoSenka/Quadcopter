package com.justing.quadcopter.shapes;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;

public class Cuboid extends Shape {
	
	private float sizeX, sizeY, sizeZ;
	
	public Cuboid(Texture texture, float sizeX, float sizeY, float sizeZ, float cx, float cy, float cz, float x, float y, float z) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		
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
		
		gl.glScalef(sizeX, sizeY, sizeZ);
		
		gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
	    // Front Face
	    gl.glNormal3f( 0.0f, 0.0f, 1.0f);                  // Normal Pointing Towards Viewer
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);  // Point 1 (Front)
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);  // Point 2 (Front)
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);  // Point 3 (Front)
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);  // Point 4 (Front)
	    // Back Face
	    gl.glNormal3f( 0.0f, 0.0f,-1.0f);                  // Normal Pointing Away From Viewer
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);  // Point 1 (Back)
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);  // Point 2 (Back)
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);  // Point 3 (Back)
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);  // Point 4 (Back)
	    // Top Face
	    gl.glNormal3f( 0.0f, 1.0f, 0.0f);                  // Normal Pointing Up
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);  // Point 1 (Top)
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);  // Point 2 (Top)
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);  // Point 3 (Top)
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);  // Point 4 (Top)
	    // Bottom Face
	    gl.glNormal3f( 0.0f,-1.0f, 0.0f);                  // Normal Pointing Down
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);  // Point 1 (Bottom)
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);  // Point 2 (Bottom)
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);  // Point 3 (Bottom)
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);  // Point 4 (Bottom)
	    // Right face
	    gl.glNormal3f( 1.0f, 0.0f, 0.0f);                  // Normal Pointing Right
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);  // Point 1 (Right)
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);  // Point 2 (Right)
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);  // Point 3 (Right)
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);  // Point 4 (Right)
	    // Left Face
	    gl.glNormal3f(-1.0f, 0.0f, 0.0f);                  // Normal Pointing Left
	    gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);  // Point 1 (Left)
	    gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);  // Point 2 (Left)
	    gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);  // Point 3 (Left)
	    gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);  // Point 4 (Left)
	    gl.glEnd();                                // Done Drawing Quads
	    
	    super.tryDisableTexture(gl);
	    
		gl.glPopMatrix();
	}
}
