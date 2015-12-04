package com.paulogaspar.ninja.actors;

import java.util.Random;

import com.badlogic.gdx.Gdx;
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
import com.paulogaspar.ninja.tools.DataManager;

public class Boss {
	private int life;
	public int platform;
	
	private float speedx;
	private float speedy;
	private float scaley;
	public float scale;
	private float blink;
	private float rotation;
	private float animation_delta;
	
	private float [] bomb_delta;
	private float [][] bomb_pos;
	public float[]position;
	private float[][]s_position;
	private float [][]direction;
	private float [] s_rotation;
	public float []platform_x;
	public float []platform_y;
	
	
	private boolean rush;
	private boolean shooting;
	public boolean alive;
	private boolean explode;
	public boolean facingR;
	
	private long timer;
	
	private int state;
	private int part;
	
	private int death_counter;
	
	private Animation idle;
	private Animation smoke_bomb;
	
	private TextureRegion shuriken;

	private Sound bomb_sound;
	private Sound got_hit;
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
		
		s_rotation = new float[3];
		
		got_hit = Gdx.audio.newSound(Gdx.files.internal("Sfx/Explosion_02.mp3"));
		
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 0; i < 4; i++){
			temp[i] = new TextureRegion(player.smokebomb_texture[i]);
		}
		smoke_bomb = new Animation(0.06f,temp);
		
		shuriken = new TextureRegion(player.shuriken.spr_star);
		
		position = new float[2];
		s_position = new float[3][2];
		direction = new float[3][2];
		
		platform_x = new float[7];
		platform_x[0] = 140;
		platform_x[1] = 420;
		platform_x[2] = 650;
		platform_x[3] = 840;
		platform_x[4] = 1050;
		platform_x[5] = 1310;
		platform_x[6] = 1570;
		
		platform_y = new float[7];
		platform_y[0] = 566;
		platform_y[1] = 566-128;
		platform_y[2] = 566;
		platform_y[3] = 566-192;
		platform_y[4] = 566;
		platform_y[5] = 566-128;
		platform_y[6] = 566;
		
	}
	public void init(Ninja player){
		facingR = false;
		death_counter = player.death_counter;

		rush = false;
		platform = 1;
		position[0] = platform_x[1];
		position[1] = platform_y[1];
		
		alive = true;
		explode = false;
		life = 100;
		
		part = 0;
		for(int i = 0; i < 3; i++){
			s_position[i][0] = 0;
			s_position[i][1] = 0;
			direction[i][0] = 0;
			direction[i][1] = 0;
			s_rotation[i] = 0;
		}
		shooting = false;
		speedx = 0;
		speedy = 0;
		scaley = 1;
		scale = 4;
		rotation = 0;
		timer = -1;
		
		//HERE
		state = 1;
		
		animation_delta = 0;
		blink = 0;
		
		timer = System.currentTimeMillis();
	}
	
	public void update(float delta,Ninja player, int [][]map,Music theme,OrthographicCamera camera){
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
			if(player.position[0] > position[0])facingR = true;
			else facingR = false;
			if(player.shuriken.hitTest(myRect())){
				blink = 0.55f;
				scale -= 2*0.012f;
				int l = life;
				life -= System.currentTimeMillis()%2 == 1?3:2;
				System.out.println("DAMAGE: "+(l- life));
				got_hit.play(player.master_volume);
			}
			if(player.rect().overlaps(myRect())){
				player.die();
			}
			if(shooting){
				shuriken_test(player);
				if(hit_test(player.rect()))player.die();
			}
			
			//BATLE STUFF
			if(state == 0 || state == 2 || state == 4){
				speedx = 0;
				speedy = 0;
				if(part == 0 && System.currentTimeMillis() - timer > 2200){
					scaley -= 0.14f*delta;
					if(scaley < 0.75f){
						timer = System.currentTimeMillis();
						part = 1;
					}
				}
				if(part == 1){
					if(scaley > 1){
						scaley = 1;
					}
					if(scaley < 1)scaley += 5f*delta;
					//SHOOT
					
					if(!shooting){
						shooting = true;
						for(int i = 0; i < 3; i++){
							s_position[i][0] = facingR?position[0]+60:position[0]+10;
							s_position[i][1] = position[1] + 50;
							direction[i][0] = facingR?1:-1;
							s_rotation[i] = 0;
						}
						direction[0][1] = 0;
						float a = player.position[1] - position[1] ;
						if(a > 90f || a < -90f){
							direction[1][1] = 1f;
							direction[2][1] = -1f;
						}
						else{
							direction[1][1] = 0.5f;
							direction[2][1] = -0.5f;
						}
					}else{
						for(int i = 0; i < 3; i++){
							if(direction[i][0] == 0)continue;
							s_position[i][0] += direction[i][0] * delta * 240;
							s_position[i][1] += direction[i][1] * delta * 240;
							s_rotation[i] += delta*60f;
							if((direction[i][0] > 0 && s_position[i][0] > camera.position.x+400+100) || 
									(direction[i][0]< 0 &&s_position[i][0] < camera.position.x - 400-100) || 
									(direction[i][1] > 0 && s_position[i][1] > camera.position.y + 300 + 100)|| 
									(direction[i][1] < 0 && s_position[i][1] < camera.position.y - 300-100)){
								s_position[i][0] = -1500;
								s_position[i][1] = -1500;
								direction[i][0] = 0;
								direction[i][1] = 0;
								s_rotation[i] = 0;
								boolean a = true;
								for(int b= 0; b < 3; b++){
									if(direction[b][0] != 0)a = false;
								}
								if(a){
									for(int b = 0; b < 3; b++){
										s_position[b][0] = 0;
										s_position[b][1] = 0;
										direction[b][0] = 0;
										direction[b][1] = 0;
										s_rotation[b] = 0;
									}
									shooting = false;
									part = 0;
									timer = System.currentTimeMillis();
									state ++;
								}
								
							}
						}
					}
					
				}
			}
			if(state == 1 || state == 3 || state == 5){
				if(part == 0 && System.currentTimeMillis() - timer > 2000){
					scaley -= 0.14f*delta;
					if(scaley < 0.75f){
						timer = System.currentTimeMillis();
						part = 1;
					}
				}
				else if(part == 1){
					if(scaley > 1){
						scaley = 1;
						part = 2;
						int l = platform;
						while(l == platform){
							Random rand = new Random();
							l =  rand.nextInt(7);
						}
						platform = l;
						speedx = platform_x[platform] - position[0];
						speedx*=0.5f;
						
						position[1] += 20;
						speedy = 22;
					}
					if(scaley < 1)scaley += 5f*delta;
				}
				else if(part == 2){
					if(speedy > -20)speedy -= delta*20f;
					else speedy = -20; 
					
					if(speedx > 0)facingR = true;
					else facingR = false;
					
					if(speedx > 0 && platform_x[platform] - position[0] < 30 && !rush){
						speedx/=2;
						speedy *= 1.2f;
						rush = true;
					}
					if(speedx < 0 && position[0] - platform_x[platform] < 30 && !rush){
						speedx/=2;
						rush = true;
						speedy *= 1.2f;
					}
					position[0] += speedx * delta;
					position[1] += speedy * delta * 22;
					
					if(speedy < 0 && position[1] < platform_y[platform]){
						position[1] = platform_y[platform];
						position[0] = platform_x[platform];
						rush = false;
						speedx = 0;
						speedy = 0;
						timer = System.currentTimeMillis();
						part = 0;
						state ++;
					}
					
				}
			}
			if(state == 6){
				if(System.currentTimeMillis() - timer > 4000){
					timer = System.currentTimeMillis();
					part = 0;
					rush = false;
					state = 0;
				}
			}
			
		}
		else{
			for(int i = 0; i < 30; i ++){
				bomb_delta[i] += delta;
			}
			if(System.currentTimeMillis()- sound_delay > 400){
				sound_delay = System.currentTimeMillis();
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
			DataManager dm = new DataManager();
			if(dm.markAchievement("master", player))player.showAchievement("Ninja Time!");
			
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
		
		
		 
		if(!explode){	
			batch.setColor(new Color(0.4f+blink,0.4f+blink,0.4f+blink,1f));	
			
			TextureRegion frame = idle.getKeyFrame(animation_delta,true);
			if(facingR == frame.isFlipX())
				frame.flip(true, false);
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
		
		if(alive){
			font.draw(batch, "BOSS: ",camera.position.x-370 , camera.position.y+270);
			batch.setColor(new Color(0f,0f,0f,1f));
			batch.draw(player.white_box,camera.position.x - 277,camera.position.y + 253,204,34);
			batch.setColor(new Color(0.2f,0.2f,0.2f,0.8f));
			batch.draw(player.white_box,camera.position.x - 275,camera.position.y + 255,200*(life/100f),30);
			batch.setColor(Color.WHITE);
			if(shooting){
				for(int i = 0; i < 3; i++){
					batch.draw(shuriken,s_position[i][0],s_position[i][1],64,64,128,128,1,1,s_rotation[i]);
				}
			}
		}
		
	}
	
	public boolean hit_test(Rectangle a){
		for(int i = 0; i < 3; i++){
			if(a.overlaps(new Rectangle(s_position[i][0]+48,s_position[i][1]+48,32,32)))
				return true;
		}
		return false;
	}
	
	public void shuriken_test(Ninja player){
		for(int i = 0; i < 3; i++){
			if(player.shuriken.hitTest(new Rectangle(s_position[i][0]+40,s_position[i][1]+40,48,48))){
				player.star_cannon.play(player.master_volume * 0.5f);
				s_position[i][0] = -1500;
				s_position[i][1] = -1500;
				direction[i][0] = 0;
				direction[i][1] = 0;
				s_rotation[i] = 0;
			}
		}
	}
	
	
	public void dispose(){
		idle = null;
		smoke_bomb = null;
		bomb_sound = null;
		got_hit.dispose();
	}
	
}
