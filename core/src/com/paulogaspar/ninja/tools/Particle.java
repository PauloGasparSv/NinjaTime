package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Particle {
	//REALLY BAD
	private float x[];
	private float y[];
	private float ix[];
	private float iy[];
	private float speed[];
	private float angle[];
	private float average_speed;
	private float average_angle;
	private float origin_x;
	private float origin_y;
	private long delay;
	private long timer;
	private boolean active;
	private float dist;
	private int sent;
	private int width;
	private int height;
	private float scale;
	private int counter;
	private boolean cancreate;
	private int angleDecoy;
	
	//DELETE REFERENCE
	private TextureRegion texture;
	
	
	public Particle(int size, Texture texture,float scale){
		x = new float[size];
		y = new float[size];
		ix = new float[size];
		iy = new float[size];
		speed = new float[size];
		angle = new float[size];
		sent = 0;
		this.scale = scale;
		this.average_angle = 0;
		this.average_speed = 0;
		this.texture = new TextureRegion(texture);
		this.origin_x = 0;
		this.origin_y = 0;
		this.delay = 0;
		this.timer = -1;
		this.active = false;
		this.dist = 0;
		this.counter = 0;
		width = texture.getWidth();
		height = texture.getHeight();
		this.angleDecoy = 16;
		cancreate = true;
		
	}
	
	public void init(float origin_x,float origin_y){
		for(int i = 0; i < x.length; i++){
			x[i] = 0;
			y[i] = 0;
			ix[i] = 0;
			iy[i] = 0;
			angle[i] = 0;
			speed[i] = 0;
		}
		this.origin_x = origin_x;
		this.origin_y = origin_y;
		counter = 0;
		timer = -1;
		sent = 0;
		active = false;
	}
	public void init(){
		for(int i = 0; i < x.length; i++){
			x[i] = 0;
			y[i] = 0;
			ix[i] = 0;
			iy[i] = 0;
			angle[i] = 0;
			speed[i] = 0;
		}
		
		counter = 0;
		timer = -1;
		sent = 0;
		active = false;
	}

	
	public void start(float average_speed,float average_angle,long delay,float dist){
		this.average_speed = average_speed;
		this.average_angle = average_angle;
		this.delay = delay;
		this.dist = dist;
		active = true;
		cancreate = true;
		timer = System.currentTimeMillis();
		for(int i = 0; i < x.length; i++){
			if(speed[i]==0){
				sent = i;
				break;
			}
		}
	}
	public void stop(){init();}
	public void stop(float x, float y){init(x,y);}
	public void canCreate(boolean can){cancreate = can;}
	public boolean getCan(){return cancreate;}
	public int getNum(){return counter;}
	
	public void setAngleDecoy(int a){
		this.angleDecoy = a;
	}
	
	public void update(float delta,OrthographicCamera camera,float time_mod){
		if(active){
			if(sent != -1 && cancreate){
				if(System.currentTimeMillis() - timer > delay){

					x[sent] = origin_x;
					y[sent] = origin_y;
					ix[sent] = origin_x;
					iy[sent] = origin_y;
					
					speed[sent] = average_speed + (System.currentTimeMillis()%16 - 8)*0.01f*average_speed;
					angle[sent] = average_angle + (System.currentTimeMillis()%angleDecoy - (angleDecoy/2));
					counter++;
					
					boolean found = false;
					for(int i = 0; i < speed.length; i++){if(speed[i] == 0){sent = i;found = true;}};
					if(!found) sent = -1;
					
					timer = System.currentTimeMillis();
				}
			}
			for(int i = 0; i < x.length; i++){
				if(speed[i] != 0){

					x[i] += delta*speed[i]*Math.cos(Math.toRadians(angle[i]))*time_mod;
					y[i] += delta*speed[i]*Math.sin(Math.toRadians(angle[i]))*time_mod;
					
					if((x[i] > ix[i] + dist) || (x[i] < ix[i] - dist) ||
							(y[i] > iy[i] + dist) || (y[i] < iy[i] -dist)){
						counter --;
						x[i] = 0;
						y[i] = 0;
						ix[i] = 0;
						iy[i] = 0;
						speed[i] = 0;
						angle[i] = 0;
						sent = i;					
					}
				}
			}
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void draw(SpriteBatch batch){

		for(int i = 0; i < x.length; i++){
			if(speed[i] != 0){
				float alpha = 1;
				if((angle[i] > 45 && angle[i] < 135) || (angle[i] > 230 && angle[i] < 315))alpha = (x[i] - ix[i])/dist;
				else alpha = (y[i] - iy[i])/dist;
				
				
				if(alpha < 0) alpha = -1*alpha;
				alpha = 1-(alpha*10);
				if(alpha < 0) alpha = 0;
				
				batch.setColor(1, 1, 1,alpha);
				batch.draw(texture,x[i],y[i],width/2,height/2,width,height,scale,scale,alpha*360);
				batch.setColor(1,1,1,1);
			}
		}
	}
	public void setOrigin(float x,float y){
		this.origin_x = x;
		this.origin_y = y;
	}
	
	public void dispose(){
		texture = null;
	}
	
	
}
