package com.paulogaspar.ninja.actors;

import java.util.Random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Boss {
	
	private float speedx;
	private float speedy;
	private float acy;
	private float scaley;
	private float scale;
	private float blink;
	private float rotation;
	private float animation_delta;
	private int life;
	private float [] bomb_delta;
	private float [][] bomb_pos;
	private float[]position;
	private float[][]s_position;
	
	private boolean shooting;
	public boolean alive;
	private boolean explode;
	
	private long timer;
	
	private int state;
	
	private int death_counter;
	
	private Animation idle;
	private Animation smoke_bomb;

	private Sound bomb_sound;
	private long sound_delay;
	
	public Boss(Texture []master_texture,Ninja player,Sound s){
		TextureRegion a[] = new TextureRegion[2];
		a[0] = new TextureRegion(master_texture[0]);
		a[1] = new TextureRegion(master_texture[1]);
		idle = new Animation(0.75f,a);
		
		bomb_delta = new float[30];
		bomb_pos = new float[30][2];
		
		bomb_sound = s;
		sound_delay = 0;
			
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			temp[i] = new TextureRegion(player.smokebomb_texture[i]);
		}
		smoke_bomb = new Animation(0.06f,temp);
		
		position = new float[2];
		s_position = new float[3][2];
		
	}
	public void init(Ninja player){
		death_counter = player.death_counter;

		position[0] = 650;
		position[1] = 566;
		
		alive = true;
		explode = false;
		life = 100;
		
		for(int i = 0; i < 3; i++){
			s_position[i][0] = 0;
			s_position[i][1] = 0;
		}
		shooting = false;
		speedx = 0;
		speedy = 0;
		acy = 0;
		scaley = 1;
		scale = 4;
		rotation = 0;
		timer = -1;
		state = 0;
		animation_delta = 0;
		blink = 0;
	}
	
	public void update(float delta,Ninja player, int [][]map,Music theme){
		animation_delta += delta;
		
		if(death_counter != player.death_counter){
			init(player);
			return;
		}
		
		if(explode){
			theme.setVolume(0);
			
			
			return;
		}
		
		if(alive){
			if(player.shuriken.hitTest(myRect())){
				blink = 0.55f;
				scale -= 25*0.012f;
				life -=25;
			}
			if(player.rect().overlaps(myRect())){
				player.die();
			}
		}
		else{
			for(int i = 0; i < 30; i ++){
				bomb_delta[i] += delta;
			}
			if(System.currentTimeMillis()- timer > 400){
				timer = System.currentTimeMillis();
				bomb_sound.play(player.master_volume);
			}
			
			if(blink == 0) blink = 0.4f;
			rotation += rotation*delta*0.5f + delta*4;
			scale -= 0.2f*delta + scale*0.2f*delta;
			if(scale < 0.1f)explode = true;
			
			if(scale < 1)
			theme.setVolume(player.master_volume * scale);
		}
		
		if(blink != 0)
			blink -= delta*0.4f;
		if(blink <0.05f && blink > -0.05f)blink = 0;
		
		
		if(life <= 0 && alive) {
			alive = false;
			sound_delay = System.currentTimeMillis()-1000;
			int width = 4 * 64;
			int height = 3 * 64;
			Random r = new Random();
			for(int i = 0; i < 30; i ++){
				bomb_pos[i][0] = position[0] + r.nextFloat()*width - 64;
				bomb_pos[i][1] = position[1] + r.nextFloat()*height - 64;
				bomb_delta[i] = 0;
			}
		}
		
		
	}
	
	public Rectangle myRect(){
		return new Rectangle(position[0]+10*scale,position[1]+6*scale*scaley,44*scale,30*scale*scaley);
	}
	
	public void draw(SpriteBatch batch, BitmapFont font,Ninja player,OrthographicCamera camera){
		
		if(alive){
			font.draw(batch, "BOSS: ",camera.position.x-370 , camera.position.y+270);
			batch.setColor(new Color(0f,0f,0f,1f));
			batch.draw(player.white_box,camera.position.x - 277,camera.position.y + 253,204,34);
			batch.setColor(new Color(0.2f,0.2f,0.2f,0.8f));
			batch.draw(player.white_box,camera.position.x - 275,camera.position.y + 255,200*(life/100f),30);
		
		}
		 
		if(!explode){	
			batch.setColor(new Color(0.4f+blink,0.4f+blink,0.4f+blink,1f));	
			if(alive)batch.draw(idle.getKeyFrame(animation_delta,true),position[0],position[1],0,0,64,64,scale,scale*scaley,rotation);		
			else batch.draw(idle.getKeyFrame(animation_delta,true),position[0]+68,position[1]+68,32,32,64,64,scale,scale*scaley,rotation);		
			batch.setColor(Color.WHITE);

		}
		if (!alive && !explode){
			batch.setColor(new Color(1,1,1,scale<1?scale:1));
			for(int i = 0; i < 30; i ++){
				batch.draw(smoke_bomb.getKeyFrame(bomb_delta[i],true),bomb_pos[i][0],bomb_pos[i][1],64,64);
			}
			batch.setColor(Color.WHITE);
		}
		
	}
	
	public void dispose(){
		idle = null;
		smoke_bomb = null;
	}
	
}
