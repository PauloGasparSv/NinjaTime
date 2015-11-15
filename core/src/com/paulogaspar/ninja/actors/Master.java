package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Master {
	public int current_message;
	private int dumb_counter;
	private int current_char;
	
	
	private boolean live;
	private boolean facingR;
	private boolean white_text;
	
	private float position[];
	private float elapsed;
	
	private long timer;
	private long delay;
	
	private String message[];
	private String secret_message;
	private String display_message;

	private boolean last_master;
	private boolean pressed;
	
	//DELETE REFERENCE
	private Animation idle;

	
	public Master(Texture master[],float posX,float posY,String message[],String secret_message,long delay,boolean last){
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
		this.timer = 0;
		this.delay = delay;
		current_char = 0;
		display_message = "";
		this.message = message;
		live = false;
		current_message = -1;
		dumb_counter = 0;
		last_master = last;
		pressed =false;
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
			if(current_message!=-1){
				if(dumb_counter < 32 && current_char < message[current_message].length() && System.currentTimeMillis() - timer > delay){
					timer = System.currentTimeMillis();
					display_message += message[current_message].charAt(current_char);
					current_char++;
				}
				if(dumb_counter >= 32 && current_char < secret_message.length() && System.currentTimeMillis() - timer > delay/2){
					timer = System.currentTimeMillis();
					display_message += secret_message.charAt(current_char);
					current_char ++;
				}
			}
			
			elapsed += delta;
			if(player.position[0] > position[0]+30)
				facingR = true;
			else if(player.position[0] < position[0] - 10)
				facingR = false;
			if(!player.interact_press)pressed = false;
			if(player.interact_press&& !pressed && new Rectangle(position[0]-100,position[1]-64,264,192).overlaps(
					new Rectangle(player.rect()))){
				pressed = true;
				if(!last_master){
					display_message = "";
					current_char = 0;
					timer = System.currentTimeMillis();
					current_message++;
					if(current_message > message.length-1){
						current_message = message.length-1;
						dumb_counter ++;
					}
				}
				if(last_master){
					display_message = "";
					current_char = 0;
					timer = System.currentTimeMillis();
					if(player.item_counter == 3){
						current_message = 0;
					}
					else if(player.item_counter < 3){
						current_message = 1;
					}
					else if (player.item_counter > 3){
						current_message = 2;
					}
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
					font.draw(batch, display_message, position[0]-message[current_message].length()*7f, position[1]+100);
				else
					font.draw(batch,display_message, position[0]-secret_message.length()*7f, position[1]+100);
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
