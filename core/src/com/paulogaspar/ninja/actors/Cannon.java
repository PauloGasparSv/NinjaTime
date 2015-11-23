package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.tools.Particle;

public class Cannon {
	public static final int UP_DOWN = 2, DOWN_LEFT = 1, DOWN_RIGHT = 0,LEFT_RIGHT = 3,DOWN_UP = 4,UP_LEFT = 5,UP_RIGHT = 6,RIGHT_LEFT = 7;
	public static final int UP = 3,DOWN = 2,LEFT = 1,RIGHT = 0;
	private int type;
	private int direction;
	private float position[];
	private boolean shooting;
	private long time;
	private float ball_position[];
	private float init[];
	private int timer;
	private boolean live;
	private boolean particles_on;
	
	private float end;
	
	//DELETE REFERENCE
	private Sound sound;
	private TextureRegion cannon;
	private TextureRegion cannonBall;
	
	//DISPOSE
	private Particle fx;
	
	public Cannon(Texture cannon,Texture cannonBall,float posX,float posY,float end,int direction,int type,int timer,Sound sound){
		position = new float[2];
		fx = new Particle(20, cannonBall, 2.5f);
		fx.setAngleDecoy(70);

		this.end = end;
		
		this.sound = sound;
		position[0] = posX;
		position[1] = posY;
		this.type = type;
		this.timer = timer;
		init = new float[2];
		fx.init();

		if(direction == LEFT){
			init[0] = posX - 36;
			init[1] = posY;

		}
		if(direction == RIGHT){
			init[0] = posX + 36;
			init[1] = posY;

		}
		if(direction == DOWN){
			init[0] = posX;
			init[1] = posY-36;

		}
		if(direction == UP){
			init[0] = posX;
			init[1] = posY+36;

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

		particles_on = true;
	}
	public void update(float delta,OrthographicCamera camera,Ninja player,float master_volume){
		particles_on = player.particles_on;
		if(!particles_on && fx.isActive())fx.stop();
		
		if(fx.isActive() && particles_on){
			fx.update(delta, camera, player.time_mod);
			
			if(!fx.getCan() && fx.getNum() == 0){
				fx.stop();
			}
		
		}
		
		float dx = player.position[0] - position[0];
		float dy = player.position[1] - position[1];
		
		if(!live){
			if(dx*dx < 620000 && dy*dy < 340000){
				live = true;
				time = System.currentTimeMillis();
			}
		}
		else{
			if(!(dx*dx < 620000 && dy*dy < 340000)&&!shooting){
				live = false;
				fx.stop();
				time = 0;
				return;
			}
			if(System.currentTimeMillis() - time > this.timer && !shooting){
				
				shooting = true;
				long i = sound.play(master_volume);
				float pan = 0;
				if(position[0] < player.position[0] - 100){
					pan = (position[0] - camera.position.x)/400;
				}
				if(position[0] > player.position[0] + 160){
					pan = (position[0] - camera.position.x)/400;
				}
				float volume = 1;
				if(pan > 0.75f || pan < -0.75f)
					volume = 0.5f;
				sound.setPan(i, pan, volume*master_volume);
			}
			if(shooting){
				if(particles_on && !fx.isActive()){
					if(direction == LEFT)fx.start(20f, 0, 40, 8);
					else if(direction == RIGHT)fx.start(20f, 180, 40, 8);
					else if(direction == UP)fx.start(20f, 270, 30, 8);
					else if(direction == DOWN)fx.start(20f, 90, 30, 8);
				}
				if(new Rectangle(ball_position[0]+27,ball_position[1]+26,10,12).overlaps(player.rect())){
					player.die();
					shooting = false;
					fx.stop();
					time = System.currentTimeMillis();
					ball_position[0] = init[0];
					ball_position[1] = init[1];
					return;
				}
				if(direction == LEFT){
					ball_position[0] -= delta * 350 * player.time_mod;
					if(ball_position[0] < end){
						shooting = false;
						fx.canCreate(false);
						time = System.currentTimeMillis();
						ball_position[0] = init[0];
						ball_position[1] = init[1];
						return;
					}
				}
				else if(direction == RIGHT){
					ball_position[0] += delta * 350 * player.time_mod;
					if(ball_position[0] > end){
						shooting = false;
						fx.canCreate(false);
						time = System.currentTimeMillis();
						ball_position[0] = init[0];
						ball_position[1] = init[1];
						return;
					}
				}
				else if(direction == DOWN){
					ball_position[1] -= delta * 350 * player.time_mod;
					if(ball_position[1] < end){
						shooting = false;
						fx.canCreate(false);
						time = System.currentTimeMillis();
						ball_position[0] = init[0];
						ball_position[1] = init[1];
						return;
					}
				}
				else if(direction == UP){
					ball_position[1] += delta * 350 * player.time_mod;
					if(ball_position[1] > end){
						shooting = false;
						fx.canCreate(false);
						time = System.currentTimeMillis();
						ball_position[0] = init[0];
						ball_position[1] = init[1];
						return;
					}
				}
				
				if(particles_on){
					fx.setOrigin(ball_position[0]+24, ball_position[1]+24);
				}
				
				if(player.shuriken.hitTest(new Rectangle(ball_position[0]+27,ball_position[1]+26,10,12)) || ball_position[0] > camera.position.x + 450 || 
						ball_position[0] < camera.position.x - 450 || ball_position[1] > camera.position.y+350 ||
						ball_position[1] < camera.position.y-350){
					shooting = false;
					fx.canCreate(false);
					time = System.currentTimeMillis();
					ball_position[0] = init[0];
					ball_position[1] = init[1];
				}
			}
		}
	}
	public void draw(SpriteBatch batch){
		if(live){
			if(direction == DOWN && fx.isActive()&& particles_on)fx.draw(batch);
			if(type == UP_DOWN || type == DOWN_LEFT ||type == DOWN_RIGHT)
				batch.draw(cannon,position[0],position[1],64,64);
			else if(type == LEFT_RIGHT)
				batch.draw(cannon,position[0],position[1],32,32,64,64,1,1,90);
			else if(type == RIGHT_LEFT)
				batch.draw(cannon,position[0],position[1],32,32,64,64,1,1,270);
			else if(type == DOWN_UP || type == UP_LEFT || type == UP_RIGHT)
				batch.draw(cannon,position[0],position[1],32,32,64,64,1,1,180);
			if(shooting)
				batch.draw(cannonBall,ball_position[0],ball_position[1],64,64);
			if(direction != DOWN && fx.isActive() && particles_on)fx.draw(batch);

		}
	}
	
	public void dispose(){
		fx.dispose();
		sound = null;
		cannon = null;
		cannonBall = null;
	}
	
}
