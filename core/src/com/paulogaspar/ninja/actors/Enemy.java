package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {

	private float []star_position;
	private float starting_position[];
	private float position[];
	private float death_position[];
	
	private float speedx;
	private float speedy;
	private float smoke_elapsed;
	private float rotation;

	private int type;
	
	private boolean alive;
	private boolean active;
	private boolean facingR;
	private boolean ground;
	private boolean show_smoke;
	private boolean shooting;

	private TextureRegion frame;
	private Texture star;

	private Animation smoke_bomb;
	
	private long timer;
	private long shoot_timer;
	
	public Enemy(float x, float y,float speedx,int type,Ninja player){
		position = new float[2];
		death_position = new float[2];
		starting_position = new float[2];
		starting_position[0] = x;
		starting_position[1] = y;
		this.speedx = speedx;
		if(speedx>0)facingR = true;
		else facingR = false;
		
		this.type = type;
		if(type == 0){
			frame = new TextureRegion(player.jump_texture);
			star = null;
		}
		else{
			frame = new TextureRegion(player.wallslide_texture);
			star = player.shuriken.spr_star;
		}
		alive = true;
		timer = 0;
		shooting = false;
		star_position = new float[2];
		
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
		if(!shooting){
			rotation = 0;
			star_position[0] = starting_position[0];
			star_position[1] = starting_position[1];
			shoot_timer = 0;
		}
		speedy = 0;
		if(facingR && speedx < 0)speedx = -speedx;
		if(!facingR && speedx > 0)speedx = - speedx;
	}
	
	public void update(float delta, Ninja player,int [][]map){
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
				shooting = false;
				init();
			}
			if(type == 1)wall_update(delta,player,map);
			if(alive){
				if(type == 0)jump_update(delta, player);
				
				if(player.rect().overlaps(new Rectangle(position[0]+15,position[1]+20,38,38)))
					player.die();
				if(player.shuriken.hitTest(new Rectangle(position[0]+15,position[1]+20,38,38))){
					player.enemy_kill.play(0.4f*player.master_volume);
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
	
	public void wall_update(float delta,Ninja player,int [][]map){
		
		
			
		if(alive && !shooting && System.currentTimeMillis() - shoot_timer > 900){
			if(player.position[1] > position[1]+60)speedy = 1;
			else if(player.position[1] < position[1] - 50)speedy = -1;
			else speedy = 0;
			shooting = true;
			star_position[0] = position[0];
			star_position[1] = position[1]+12;
			if(speedx < 0)star_position[0] += 32;
			else star_position[0] += 12;
			shoot_timer = 0;
		}
		if(shooting){
			if(speedx < 0)
				star_position[0] += delta*220*player.time_mod;
			else
				star_position[0] -= delta*220*player.time_mod;
			if(speedy > 0)
				star_position[1] += delta*220*player.time_mod;
			if(speedy < 0)
				star_position[1] -= delta*220*player.time_mod;
			rotation -= delta*460 * player.time_mod*player.time_mod;

			int x = (int)(star_position[0] + 20)/64;
			int y = (int)(star_position[1] + 20)/64;
			if(x < 0)x = 0;
			if(y < 0)y = 0;
			if(x > map[0].length-1)x = map[0].length-1;
			if(y > map.length-1)y = map.length-1;
			
			if(map[y][x] >= 0 || star_position[0] > player.position[0] + 800|| star_position[0] < player.position[0] - 800
					|| star_position[1] > player.position[1] + 600|| star_position[1] < player.position[1] - 600){
				shooting = false;
				star_position[0] = starting_position[0];
				star_position[1] = starting_position[1];
				shoot_timer = System.currentTimeMillis();
			}
			if(player.shuriken.hitTest(new Rectangle(star_position[0]+10,star_position[1]+14,10,10))){
				shooting = false;
				star_position[0] = starting_position[0];
				star_position[1] = starting_position[1];
				shoot_timer = System.currentTimeMillis();
				player.shuriken.hit.play(player.master_volume);
			}
			if(player.rect().overlaps(new Rectangle(star_position[0]+10,star_position[1]+14,10,10))){
				shooting = false;
				star_position[0] = starting_position[0];
				star_position[1] = starting_position[1];
				player.die();
				shoot_timer = System.currentTimeMillis();
			}
			
		}
	}
	
	public void jump_update(float delta,Ninja player){
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
	}
	
	public void draw(SpriteBatch batch,Ninja player){
		if(active && show_smoke)
			batch.draw(smoke_bomb.getKeyFrame(smoke_elapsed,false),death_position[0],death_position[1],64,64);
		if(shooting)
			batch.draw(star,star_position[0],star_position[1],20,20,40,40,1,1,rotation,0,0,16,16,false,false);
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
		star = null;
	}
	
	
}
