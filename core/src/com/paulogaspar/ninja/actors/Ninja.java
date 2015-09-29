package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ninja {
	private final int IDLE = 0, WALK = 1;
	
	private Animation animation[];
	private float elapsed_time;
	public float position[];
	boolean facing_right;
	private int current;
	
	public float speed_y;
	public float speed_x;
	
	private boolean grounded;
	private int jump_count;
	
	private OrthographicCamera camera;
	
	private Animation smoke_bomb;
	private float smoke_elapsed;
	
	private TextureRegion gauge[];
	private Texture gauge_texture;
	private int current_gauge;
	
	private float time_mod;
	private boolean stop_time;
	private boolean slow_time;
	private float timer;
	
	private boolean touching;
	private float teleport_pos[];
	
	public Ninja(OrthographicCamera camera){
		this.camera = camera;
		animation =  new Animation[2];
		
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 1; i < 5; i++)
			temp[i-1] = new TextureRegion(new Texture(Gdx.files.internal("Ninja/run"+i+".png")));
		
		animation[WALK] = new Animation(0.25f,temp);
		
		temp = new TextureRegion[2];
		temp[0] = new TextureRegion(new Texture(Gdx.files.internal("Ninja/idle1.png")));
		temp[1] = new TextureRegion(new Texture(Gdx.files.internal("Ninja/idle2.png")));
		animation[IDLE] = new Animation(0.75f,temp);
		
		temp = new TextureRegion[4];
		for(int i = 1; i < 5; i++)
			temp[i-1] = new TextureRegion(new Texture(Gdx.files.internal("Misc/spr_smoke_"+i+".png")));
		
		smoke_bomb = new Animation(0.07f,temp);
		
		
		gauge = new TextureRegion[5];
		gauge_texture = new Texture(Gdx.files.internal("Misc/meter.png"));
		for(int i = 0; i < 5; i++)
			gauge[i] = new TextureRegion(gauge_texture,171,0,(4-i)*14,20);
		
		position = new float[2];
		teleport_pos = new float[2];
		init();
	
	}
	
	public void init(){
		position[0] = 20f;
		position[1] = 280f;
		
		teleport_pos[0] = 0;
		teleport_pos[1] = 0;
		
		elapsed_time = 0f;
		
		facing_right = true;
		
		current = 0;
		speed_x = 0;
		speed_y = 0;
		
		jump_count = 0;
		
		grounded = false;
		
		slow_time = false;
		stop_time = false;
		time_mod = 1;
		
		touching = false;
		
		timer = 0;
		current_gauge = 0;
		smoke_elapsed = 0;
	}
	
	public void update(float delta,int map[][],int width,int height){
		elapsed_time += delta * time_mod;
				
		int x1 = ((int)(position[0]+10)/64);
		int x2 = ((int)(position[0]+54)/64);
		int y = ((int)(position[1]+4)/64);
		
		int y2 = ((int)(position[1]+48)/64);
		int y1 = ((int)(position[1]+12)/64);
		int xr = ((int)(position[0]+64)/64);
		int xl = ((int)(position[0])/64);
		
		int yu = ((int)(position[1]+54)/64);
		int xl2 = ((int)(position[0]+16)/64);
		int xr2 = ((int)(position[0]+48)/64);
		
		grounded = true;
		if(map[y][x1] == -1 && map[y][x2] == -1){
			grounded = false;
		}
		
		if(grounded){
			jump_count = 0;	
			speed_y = 0;
		}
			
		if(!grounded && speed_y < 8){
			speed_y += delta*14.75f*time_mod;
		}

		if(slow_time)
			timer += delta*1.5f;
		if(stop_time)
			timer += delta*0.8f;
		if(slow_time){
			if(timer > 5){
				time_mod = 1f;
				current_gauge =9-(int)timer;
			}
			else
				current_gauge = (int)timer;
			if(timer > 10){
				timer = 0;
				current_gauge = 0;
				slow_time = false;
			}
		}
		
	
		if(stop_time){
			smoke_elapsed += delta;
			if(timer < 5)
				current_gauge = 4 - (int)timer;
			if(timer > 5){
				stop_time = false;
				timer = 0;
			}
		}
	
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && current_gauge == 0 && !slow_time && !stop_time){
			slow_time = true;
			time_mod = 0.5f;
			timer = delta;
		}
		if(Gdx.input.isTouched() && !slow_time && !stop_time){
			touching = true;
			smoke_elapsed = 0;
			teleport_pos[0] = position[0];
			teleport_pos[1] = position[1];
			int tx = ((int)(camera.position.x-400+Gdx.input.getX())/64);
			int ty =  ((int)(camera.position.y-300+(600-Gdx.input.getY()))/64);
			if(tx < 0)
				tx = 0;
			if(tx > map[0].length)
				tx = map[0].length;
			if(ty < 0)
				ty = 0;
			if(ty > map.length)
				ty = map.length;
			
			if(map[ty][tx] == -1){
				teleport_pos[0] = tx*64;
				teleport_pos[1] = ty*64;
			}
		}
		if(!Gdx.input.isTouched()&& !slow_time && !stop_time && touching){
			stop_time = true;
			touching = false;
			timer = delta;
			current_gauge = 4;
			float tx = position[0];
			float ty = position[1];
			position[0] = teleport_pos[0];
			position[1] = teleport_pos[1];
			teleport_pos[0] = tx;
			teleport_pos[1] = ty;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			facing_right = true;
			if(speed_x < 4.5f)
				speed_x += delta*6.5f*time_mod;
			if(speed_x < 0)
				speed_x += delta*4.5f*time_mod;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
			
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			facing_right = false;
			if(speed_x > -4.5f)
				speed_x -= delta*6.5f*time_mod;
			if(speed_x > 0)
				speed_x -= delta*4.5f*time_mod;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else{
			if(speed_x < 0.25f && speed_x > -0.25f)
				speed_x = 0;
			if(speed_x > 0.25f)
				speed_x -= delta*8*time_mod;
			if(speed_x < -0.25f)
				speed_x += delta*8*time_mod;
				
			if(current == WALK){
				elapsed_time = 0f;
				current = IDLE;
			}
		}
		
		if((Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && jump_count < 2){
			if(!grounded)
				jump_count = 2;
			else
				jump_count = 1;
			grounded = false;
			if(jump_count == 1)
				speed_y = -8;
			else
				speed_y = -4.5f;
		}
		
		if(jump_count != 0 && (map[yu][xl2] != -1 || map[yu][xr2]!=-1) && speed_y < 0){
			speed_y = 0;
		}
				
		
		
		if(current == WALK){
			if(speed_x > 0 && (map[y1][xr] != -1 || map[y2][xr] != -1)){
				speed_x = 0;
				position[0] -= 1;
			}
			if(speed_x < 0 && (map[y1][xl] != -1 || map[y2][xl] != -1)){
				speed_x = 0;
				position[0] += 1;
			}
		}
		
		position[0] += speed_x*time_mod;
		position[1] -= speed_y*time_mod;
		if(position[0] < 0)
			position[0] = 0;
		if(position[0] > width-72)
			position[0] = width-72;
	}
	
	public void die(){
		init();
		camera.translate(-1*((camera.position.x-400)-position[0]),0);
	}
	
	public void draw(SpriteBatch batch){
		TextureRegion frame = animation[current].getKeyFrame(elapsed_time,true);
		if(facing_right == frame.isFlipX())
			frame.flip(true, false);
		
		batch.draw(frame,position[0],position[1],64,64);
		
		if(slow_time || stop_time)
			batch.draw(gauge[current_gauge],position[0]+5,position[1]+70);
		
		if(touching){
			batch.setColor(0, 0, 0, 0.5f);
			batch.draw(frame,teleport_pos[0],teleport_pos[1],64,64);
			batch.setColor(1, 1, 1, 1);
		}
		
		if(stop_time && !smoke_bomb.isAnimationFinished(smoke_elapsed)){
			batch.draw(smoke_bomb.getKeyFrame(smoke_elapsed,false),position[0],position[1],64,64);
			batch.draw(smoke_bomb.getKeyFrame(smoke_elapsed,false),teleport_pos[0],teleport_pos[1],64,64);			
		}
		
	}
	
	public void dispose(){
		camera = null;
		gauge_texture.dispose();
	}
	
	
}




