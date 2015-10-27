package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.tools.Particle;

public class Cannon {
	public static final int UP_DOWN = 2, DOWN_LEFT = 1, DOWN_RIGHT = 0,LEFT_RIGHT = 3,DOWN_UP = 4,UP_LEFT = 5,UP_RIGHT = 6;
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
	private long fx_timer;
	private boolean particles_on;
	
	//DELETE REFERENCE
	private Sound sound;
	private TextureRegion cannon;
	private TextureRegion cannonBall;
	
	//DISPOSE
	private Particle fx;
	
	public Cannon(Texture cannon,Texture cannonBall,float posX,float posY,int direction,int type,int timer,Sound sound){
		position = new float[2];
		fx = new Particle(6, cannonBall, 2.25f);

		this.sound = sound;
		position[0] = posX;
		position[1] = posY;
		this.type = type;
		this.timer = timer;
		init = new float[2];

		if(direction == LEFT){
			init[0] = posX - 36;
			init[1] = posY;
			fx.init(init[0], init[1]+12);

		}
		if(direction == RIGHT){
			init[0] = posX + 36;
			init[1] = posY;
			fx.init(init[0]+18, init[1]+22);

		}
		if(direction == DOWN){
			init[0] = posX;
			init[1] = posY-36;
			fx.init(init[0], init[1]+12);

		}
		if(direction == UP){
			init[0] = posX;
			init[1] = posY+36;
			fx.init(init[0]+24, init[1]+20);

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

		fx_timer = 0;
		particles_on = true;
	}
	public void update(float delta,OrthographicCamera camera,Ninja player,float master_volume){
		particles_on = player.particles_on;
		if(!particles_on && fx.isActive())fx.stop();
		
		if(fx.isActive() && particles_on){
			fx.update(delta, camera, player.time_mod);
			if(fx.getCan() && System.currentTimeMillis() - fx_timer > 500){
				fx.canCreate(false);
				fx_timer = 0;
			}
			if(!fx.getCan() && fx.getNum() == 0){
				fx.stop();
			}
		}
		
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		if(!live){
			if(new Rectangle(position[0],position[1],64,64).overlaps(new Rectangle(camera.position.x-width/2,camera.position.y-height/2,width,height))){
				live = true;
				time = System.currentTimeMillis();
			}
		}
		else{
			if(!new Rectangle(position[0],position[1],64,64).overlaps(new Rectangle(camera.position.x-width/2,camera.position.y-height/2,width,height))&&!shooting){
				live = false;
				fx.stop();
				fx_timer = 0;
				time = 0;
				return;
			}
			if(System.currentTimeMillis() - time > this.timer && !shooting){
				if(particles_on){
					fx.start(35f, 90, 80, 60);
					fx_timer = System.currentTimeMillis();
				}
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
				
				if(new Rectangle(ball_position[0]+27,ball_position[1]+26,10,12).overlaps(player.rect()))
					player.die(master_volume);
				if(direction == LEFT)
					ball_position[0] -= delta * 350 * player.time_mod;
				else if(direction == RIGHT)
					ball_position[0] += delta * 350 * player.time_mod;
				else if(direction == DOWN)
					ball_position[1] -= delta * 350 * player.time_mod;
				else if(direction == UP)
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
			if(direction == DOWN && fx.isActive()&& particles_on)fx.draw(batch);
			if(type == UP_DOWN || type == DOWN_LEFT ||type == DOWN_RIGHT)
				batch.draw(cannon,position[0],position[1],64,64);
			else if(type == LEFT_RIGHT)
				batch.draw(cannon,position[0],position[1],32,32,64,64,1,1,90);
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
