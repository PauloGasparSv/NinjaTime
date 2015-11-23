package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
	
	private float starting_position[];
	private float position[];
	private float death_position[];
	
	private float speedx;
	private float speedy;
	private float smoke_elapsed;

	
	private boolean alive;
	private boolean active;
	private boolean facingR;
	private boolean ground;
	private boolean show_smoke;
	
	private TextureRegion frame;

	private Animation smoke_bomb;
	
	private long timer;
	
	public Enemy(float x, float y,float speedx,Ninja player){
		position = new float[2];
		death_position = new float[2];
		starting_position = new float[2];
		starting_position[0] = x;
		starting_position[1] = y;
		this.speedx = speedx;
		if(speedx>0)facingR = true;
		else facingR = false;
		frame = new TextureRegion(player.jump_texture);
		alive = true;
		timer = 0;
		
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			temp[i] = new TextureRegion(player.smokebomb_texture[i]);
		}
		smoke_bomb = new Animation(0.07f,temp);
		smoke_elapsed = 0;
		show_smoke = false;
		death_position[0] = -20;
		death_position[1] = -20;
		init();
	}
	public void init(){
		ground = true;
		//CHANGE
		active = false;
		position[0] = starting_position[0];
		position[1] = starting_position[1];
		
		speedy = 0;
		if(facingR && speedx < 0)speedx = -speedx;
		if(!facingR && speedx > 0)speedx = - speedx;
	}
	
	public void update(float delta, Ninja player){
		float dx = player.position[0] - position[0];
		float dy = player.position[1] - position[1];
		if(show_smoke){
			smoke_elapsed += delta;
			if(smoke_bomb.isAnimationFinished(smoke_elapsed)){
				smoke_elapsed = 0;
				show_smoke = false;
			}
		}
		if(!alive &&  System.currentTimeMillis() - timer > 2000){
			alive = true;
			death_position[0] = position[0];
			death_position[1] = position[1];
			smoke_elapsed = 0;
			show_smoke = true;
			timer = 0;
		}
		if(active){
			if(!(dx*dx < 620000 && dy*dy < 340000)){
				init();
			}
			if(alive){
				if(ground){
					speedy = 350;
					ground = false;
					position[1] += 2;
				}else{
					position[0] += speedx * delta * player.time_mod;
					position[1] += speedy * delta * player.time_mod;
					speedy -= 405*delta*player.time_mod;
					if(position[1] < starting_position[1]){
						position[1] = starting_position[1];
						speedx = -speedx;
						speedy = 0;
						ground = true;
					}
				}
				if(player.rect().overlaps(new Rectangle(position[0]+15,position[1]+20,38,38)))
					player.die();
				if(player.shuriken.hitTest(new Rectangle(position[0]+15,position[1]+20,38,38))){
					death_position[0] = position[0];
					death_position[1] = position[1];
					init();
					smoke_elapsed = 0;
					show_smoke = true;
					alive = false;
					timer = System.currentTimeMillis();
				}
				
			}
		}else{
			if(dx*dx < 620000 && dy*dy < 340000){
				active = true;
			}
		}
	}
	
	public void draw(SpriteBatch batch,Ninja player){
		if(active && show_smoke)
			batch.draw(smoke_bomb.getKeyFrame(smoke_elapsed,false),death_position[0],death_position[1],64,64);
	
		if(active && alive){
			batch.setColor(new Color(0.05f,0.05f,0.05f,0.9f));
			if((speedx > 0) == frame.isFlipX())
				frame.flip(true, false);
			batch.draw(frame,position[0],position[1],68,68);
			batch.setColor(Color.WHITE);
		}
	}
	
	public void dispose(){
		frame = null;
		smoke_bomb = null;
	}
	
	
}
