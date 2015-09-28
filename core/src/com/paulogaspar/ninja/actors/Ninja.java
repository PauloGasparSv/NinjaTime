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
		
		init();
	
	}
	
	public void init(){
		position = new float[2];
		position[0] = 20f;
		position[1] = 200f;
		
		elapsed_time = 0f;
		
		facing_right = true;
		
		current = 0;
		
	}
	
	public void update(float delta){
		elapsed_time += delta;
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			facing_right = true;
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			facing_right = false;
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else{
			if(current == WALK){
				elapsed_time = 0f;
				current = IDLE;
			}
		}
		
	}
	
	public void draw(SpriteBatch batch){
		TextureRegion frame = animation[current].getKeyFrame(elapsed_time,true);
		if(facing_right == frame.isFlipX())
			frame.flip(true, false);
		
		batch.draw(frame,position[0],position[1],64,64);
	}
	
	
}
