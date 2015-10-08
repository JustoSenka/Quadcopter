package com.justing.quadcopter.model;

public class VerticeTexture {
	protected float x, y;
	protected VerticeTexture(float x, float y){
		this.x = x;
		this.y = y;
	}
	protected VerticeTexture(String x, String y){
		this(Model.strToFloat(x), Model.strToFloat(y));
	}
}