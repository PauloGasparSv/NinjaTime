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
	
	public Ninja(OrthographicCamera camera){
		this.camera = camera;
		animation =  new Animation[2];
		
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 1; i < 5; i++){
			temp[i-1] = new TextureRegion(new Texture(Gdx.files.internal("Ninja/run"+i+".png")));
		}
		animation[WALK] = new Animation(0.25f,temp);
		
		temp = new TextureRegion[2];
		temp[0] = new TextureRegion(new Texture(Gdx.files.internal("Ninja/idle1.png")));
		temp[1] = new TextureRegion(new Texture(Gdx.files.internal("Ninja/idle2.png")));
		animation[IDLE] = new Animation(0.75f,temp);
		
		position = new float[2];
		init();
	
	}
	
	public void init(){
		position[0] = 20f;
		position[1] = 280f;
		
		elapsed_time = 0f;
		
		facing_right = true;
		
		current = 0;
		speed_x = 0;
		speed_y = 0;
		
		jump_count = 0;
		
		grounded = false;
		
	}
	
	public void update(float delta,int map[][],int width,int height){
		elapsed_time += delta;
		
		int x1 = ((int)(position[0]+10)/64);
		int x2 = ((int)(position[0]+54)/64);
		int y = ((int)(position[1]+4)/64);
		
		int y2 = ((int)(position[1]+58)/64);
		int y1 = ((int)(position[1]+12)/64);
		int xr = ((int)(position[0]+64)/64);
		int xl = ((int)(position[0])/64);
		
		if(map[y][x1] == -1 && map[y][x2] == -1){
			grounded = false;
		}
		else if(map[y][x1] != 16 && map[y][x2] != 16 && map[y][x1] != 17 && map[y][x2] != 17
				&& map[y][x1] != 22 && map[y][x2] != 22 && map[y][x1] != 23 && map[y][x2] != 23){
			grounded = true;
			jump_count = 0;	
			speed_y = 0;
		}
		else{
			die();
		}
				
		if(!grounded && speed_y < 8){
			speed_y += delta*14.75f;
		}
			
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			facing_right = true;
			if(speed_x < 5)
				speed_x += delta*6.5f;
			if(speed_x < 0)
				speed_x += delta*4.5f;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
			
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			facing_right = false;
			if(speed_x > -5)
				speed_x -= delta*6.5f;
			if(speed_x > 0)
				speed_x -= delta*4.5f;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else{
			if(speed_x < 0.25f && speed_x > -0.25f)
				speed_x = 0;
			if(speed_x > 0.25f)
				speed_x -= delta*8;
			if(speed_x < -0.25f)
				speed_x += delta*8;
				
			if(current == WALK){
				elapsed_time = 0f;
				current = IDLE;
			}
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.Z) && grounded && jump_count < 2){
			jump_count ++;
			grounded = false;
			speed_y = -8;
		}
				
		
		if(current == WALK){
			if(facing_right && (map[y1][xr] != -1 || map[y2][xr] != -1)){
				speed_x = 0;			
			}
			else if(map[y1][xl] != -1 || map[y2][xl] != -1){
				speed_x = 0;
			}
		}
		
		position[0] += speed_x;
		position[1] -= speed_y;
		if(position[0] < -32)
			position[0] = -32;
		if(position[0] > width-64)
			position[0] = width-64;
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
	}
	
	public void dispose(){
		camera = null;
	}
	
	
}
