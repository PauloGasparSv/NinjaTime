package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle {

	private int x[];
	private int y[];
	private int angle[];
	private float speed[];
	private float average_speed;
	
	//DELETE REFERENCE
	private TextureRegion texture;
	
	public Particle(int size, Texture texture, float average_speed){
		x = new int[size];
		y = new int[size];
		angle = new int[size];
		speed = new float[size];
		average_speed = average_speed;
		this.texture = new TextureRegion(texture);
	}
	
	public void init(){
		x = new int[x.length];
		y = new int[y.length];
		angle = new int[angle.length];
		speed = new float[speed.length];
	}
	public void dispose(){
		texture = null;
	}
	
	
}
