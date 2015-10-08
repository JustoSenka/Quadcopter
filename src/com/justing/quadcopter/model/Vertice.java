package com.justing.quadcopter.model;

public class Vertice {
	protected float x, y, z;
	protected Vertice(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	protected Vertice(String x, String y, String z){
		this(Model.strToFloat(x), Model.strToFloat(y), Model.strToFloat(z));
	}
}