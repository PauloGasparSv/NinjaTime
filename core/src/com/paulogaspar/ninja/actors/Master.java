package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Master {
	
	private float position[];
	private String message[];
	private String secret_message;
	private boolean live;
	private int current_message;
	private float elapsed;
	private boolean facingR;
	private int dumb_counter;
	private boolean white_text;

	//DELETE REFERENCE
	private Animation idle;

	
	public Master(Texture master[],float posX,float posY,String message[],String secret_message){
		TextureRegion a[] = new TextureRegion[2];
		this.secret_message = secret_message;
		a[0] = new TextureRegion(master[0]);
		a[1] = new TextureRegion(master[1]);
		idle = new Animation(0.75f,a);
		white_text = false;
		elapsed = 0f;
		this.facingR = true;
		position = new float[2];
		position[0] = posX;
		position[1] = posY;
		this.message = message;
		live = false;
		current_message = -1;
		dumb_counter = 0;
	}
	public void changeTextColor(){
		white_text = !white_text;
	}
	
	public void update(float delta,OrthographicCamera camera,Ninja player){
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		if(!live){
			if(new Rectangle(position[0],position[1],64,64).overlaps(new Rectangle
					(camera.position.x-width/2,camera.position.y-height/2,width,height)))
				live = true;
		}
		else{
			elapsed += delta;
			if(player.position[0] > position[0])
				facingR = true;
			else
				facingR = false;
			if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)&&new Rectangle(position[0]-100,position[1]-64,264,192).overlaps(
					new Rectangle(player.rect()))){
				current_message++;
				if(current_message > message.length-1){
					current_message = message.length-1;
					dumb_counter ++;
				}
			}
			
			if(!new Rectangle(position[0],position[1],64,64).overlaps(new Rectangle(
					camera.position.x-width/2,camera.position.y-height/2,width,height))){
				live = false;
				elapsed = 0 ;
				dumb_counter = 0;
				current_message = -1;
			}
			
		}
	}
	public void draw(SpriteBatch batch,BitmapFont font){
		if(live){
			if(current_message > -1){
				if(dumb_counter < 32)
					font.draw(batch, message[current_message], position[0]-message[current_message].length()*9f, position[1]+100);
				else
					font.draw(batch,secret_message, position[0]-145, position[1]+100);
			}
			TextureRegion frame = idle.getKeyFrame(elapsed,true);
			if(facingR == frame.isFlipX())
				frame.flip(true, false);
		
			batch.draw(idle.getKeyFrame(elapsed,true),position[0],position[1],64,64);
		}
	}
	
	public void dispose(){
		idle = null;
	}

}
