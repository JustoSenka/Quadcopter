package com.justing.quadcopter.shapes;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;

public abstract class Shape {
	public float x, y, z, cx, cy, cz;
	protected Texture texture;
	
	public abstract void draw(GL2 gl, GLU glu);
	
	public final void draw(GL2 gl){
		this.draw(gl, null);
	}
	
	protected void tryEnableAndBindTexture(GL2 gl){
		try {
			texture.enable(gl);
			texture.bind(gl);
		} catch (GLException e) {
			e.printStackTrace();
			System.out.println("gl error on texture bind");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println("No texture on: " + this.toString());
		}
	}

	protected void tryDisableTexture(GL2 gl){
		try {
			texture.disable(gl);
		} catch (GLException e) {
			e.printStackTrace();
			System.out.println("gl error on texture bind");
		} catch (NullPointerException npe) {
			npe.printStackTrace();
			System.out.println("No texture on: " + this.toString());
		}
	}
	protected void applyTranslateAndRotate(GL2 gl){
		gl.glTranslatef(cx, cy, cz);
		gl.glRotatef(x, 1, 0, 0);
		gl.glRotatef(y, 0, 1, 0);
		gl.glRotatef(z, 0, 0, 1);
	}
	
	public final Texture getTexture() {
		return texture;
	}
	public final void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public String toString() {
		return "Shape [cx=" + cx + ", cy=" + cy + ", cz=" + cz + ", texture=" + texture + "]";
	}
}
