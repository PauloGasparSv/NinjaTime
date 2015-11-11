package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.actors.Ninja;

public class Message {
	
	private float x;
	private float y;
	private String message;
	private int timer;
	private long start_time;
	private float alpha;
	private boolean active;
	private boolean draw;
	
	public Message(float x,float y, String message,int timer){
		this.x = x;
		this.y = y;
		this.message = message;
		this.timer = timer;
		alpha = 0;
		start_time = 0;
		active = false;
		draw = false;
	}
	
	public void update(float delta,Ninja player){
		if(!active){
			if(draw){
				if(alpha > 0)alpha -= 0.6f*delta;
				if(alpha < 0){
					draw = false;
					alpha = 0;
				}
			}
			double dist =Math.sqrt(Math.pow(player.position[0]-x,2) + Math.pow(player.position[1] - y, 2));
			if(dist < 600){
				active = true;
				start_time = System.currentTimeMillis();
			}
		}
		else if(active){
			double dist =Math.sqrt(Math.pow(player.position[0]-x,2) + Math.pow(player.position[1] - y, 2));
			if(dist > 600){
				active = false;
				start_time = 0;
				return;
			}
			if(!draw && System.currentTimeMillis() - start_time > 1000*timer){
				draw = true;
			}
			if(draw && alpha < 0.8f)alpha += delta*0.5f;
			if(alpha > 0.8f)alpha = 0.8f;
		}
	}
	
	
	public void draw(SpriteBatch batch,BitmapFont font,Ninja player){
		if(draw){
			font.setColor(0, 0, 0, alpha);
			font.draw(batch, message, x, y);
			font.setColor(1,1,1,1);
		}
	}
	
}
