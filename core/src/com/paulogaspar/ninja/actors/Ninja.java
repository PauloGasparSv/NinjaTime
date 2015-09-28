package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	
	private float speed_y;
	private float speed_x;
	
	private boolean is_grounded;
	private int jump_count;
	
	public Ninja(){
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
		
		is_grounded = false;
		
	}
	
	public void update(float delta,int map[][]){
		elapsed_time += delta;
		
		int x1 = ((int)(position[0]+10)/64);
		int x2 = ((int)(position[0]+54)/64);
		int y = ((int)(position[1])/64);
		
		if(map[y][x1] == -1 && map[y][x2] == -1){
			is_grounded = false;
		}
		else if(map[y][x1] != 16 && map[y][x2] != 16 && map[y][x1] != 17 && map[y][x2] != 17
				&& map[y][x1] != 22 && map[y][x2] != 22 && map[y][x1] != 23 && map[y][x2] != 23){
			is_grounded = true;
			
		}
				
		if(!is_grounded && speed_y < 10){
			speed_y += delta*10;
		}
		if(is_grounded){
			speed_y = 0;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			facing_right = true;
			if(speed_x < 4)
				speed_x += delta*5;
			if(speed_x < 0)
				speed_x += delta*4;
			
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			facing_right = false;
			
			if(speed_x > -4)
				speed_x -= delta*5;
			if(speed_x > 0)
				speed_x -= delta*4;
			
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
		
		System.out.println("X: "+position[0]%64+" Y: "+position[1]%64);
		
		position[0] += speed_x;
		position[1] -= speed_y;
	}
	
	public void die(){
		init();
	}
	
	public void draw(SpriteBatch batch){
		TextureRegion frame = animation[current].getKeyFrame(elapsed_time,true);
		if(facing_right == frame.isFlipX())
			frame.flip(true, false);
		
		batch.draw(frame,position[0],position[1],64,64);
	}
	
	
}
