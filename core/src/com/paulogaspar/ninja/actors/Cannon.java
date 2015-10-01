package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Cannon {
	public static final int UP_DOWN = 2, DOWN_LEFT = 1, DOWN_RIGHT = 0,LEFT_RIGHT = 3,DOWN_UP = 4,UP_LEFT = 5,UP_RIGHT = 6;
	public static final int UP = 3,DOWN = 2,LEFT = 1,RIGHT = 0;
	private int type;
	private int direction;
	private TextureRegion cannon;
	private TextureRegion cannonBall;
	private float position[];
	private boolean shooting;
	private long time;
	private float ball_position[];
	private float init[];
	private int timer;
	private boolean live;
	
	public Cannon(Texture cannon,Texture cannonBall,float posX,float posY,int direction,int type,int timer){
		position = new float[2];
		position[0] = posX;
		position[1] = posY;
		this.type = type;
		this.timer = timer;
		init = new float[2];
		if(direction == LEFT){
			init[0] = posX - 42;
			init[1] = posY;
		}
		if(direction == RIGHT){
			init[0] = posX + 42;
			init[1] = posY;
		}
		if(direction == DOWN){
			init[0] = posX;
			init[1] = posY-42;
		}
		if(direction == UP){
			init[0] = posX;
			init[1] = posY+42;
		}
		ball_position = new float[2];
		ball_position[0] = init[0];
		ball_position[1] = init[1];
		time = 0;
		this.cannon = new TextureRegion(cannon);
		this.cannonBall = new TextureRegion(cannonBall);
		this.direction = direction;
		shooting = false;
		live = false;
		
	}
	public void update(float delta,OrthographicCamera camera,Ninja player){
		if(!live){
			if(new Rectangle(position[0],position[1],64,64).overlaps(new Rectangle(camera.position.x-400,camera.position.y-300,800,600))){
				live = true;
				time = System.currentTimeMillis();
			}
		}
		else{
			if(!new Rectangle(position[0],position[1],64,64).overlaps(new Rectangle(camera.position.x-400,camera.position.y-300,800,600))){
				live = false;
				time = 0;
				return;
			}
			if(System.currentTimeMillis() - time > this.timer && !shooting){
				shooting = true;
			}
			if(shooting){
				if(new Rectangle(ball_position[0]+24,ball_position[1]+24,16,16).overlaps(player.rect()))
					player.die();
				if(direction == LEFT)
					ball_position[0] -= delta * 350 * player.time_mod;
				if(direction == RIGHT)
					ball_position[0] += delta * 350 * player.time_mod;
				if(direction == DOWN)
					ball_position[1] -= delta * 350 * player.time_mod;
				if(direction == UP)
					ball_position[1] += delta * 350 * player.time_mod;
				
				if(ball_position[0] > camera.position.x + 450 || ball_position[0] < camera.position.x - 450 || ball_position[1] > camera.position.y+350 || ball_position[1] < camera.position.y-350){
					shooting = false;
					time = System.currentTimeMillis();
					ball_position[0] = init[0];
					ball_position[1] = init[1];
				}
			}
		}
	}
	public void draw(SpriteBatch batch){
		if(live){
			if(type == UP_DOWN || type == DOWN_LEFT ||type == DOWN_RIGHT)
				batch.draw(cannon,position[0],position[1],64,64);
			if(type == LEFT_RIGHT)
				batch.draw(cannon,position[0],position[1],32,32,64,64,1,1,90);
			if(type == DOWN_UP || type == UP_LEFT || type == UP_RIGHT)
				batch.draw(cannon,position[0],position[1],32,32,64,64,1,1,180);
			if(shooting)
				batch.draw(cannonBall,ball_position[0],ball_position[1],64,64);
		}
		
	}
	
}
