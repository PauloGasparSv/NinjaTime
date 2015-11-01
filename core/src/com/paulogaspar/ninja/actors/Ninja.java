package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.tools.Particle;

public class Ninja {
	private final int IDLE = 0, WALK = 1;
		
	private int current;
	private int jump_count;
	private int current_gauge;
	public int item_counter;
	public int death_counter;
	
	public float position[];
	public float spawn_position[];
	private float teleport_pos[];
	public float camera_start_pos[];
	private float elapsed_time;
	private float smoke_elapsed;
	public float speed_y;
	public float speed_x;
	public float time_mod;
	private float timer;
	public float jump_mod;
	public float g_mod;
	
	boolean facing_right;
	private boolean stop_time;
	private boolean slow_time;
	private boolean touching;
	private boolean slide_l,slide_r;	
	private boolean grounded;
	public boolean particles_on;
	
	private long current_slide_sound;
	private long clock_playing;	
	
	
	//DELETE REFERENCE
	private OrthographicCamera camera;
	
	private Animation smoke_bomb;
	private Animation animation[];
	
	private TextureRegion wallslide;
	private TextureRegion gauge[];
	
	//DISPOSE	
	private Sound jump_sound;
	private Sound slide_sound;
	private Sound clock_sound;
	private Sound teleport_sound;
	private Sound death_sound;
	
	private Texture gauge_texture;
	private Texture wallslide_texture;
	private Texture walk_texture[];
	private Texture idle_texture[];
	private Texture smokebomb_texture[];
	
	private Particle particlefx;	

	
	public Ninja(OrthographicCamera camera,float x, float y){
		this.camera = camera;
		
		camera_start_pos = new float[2];
		camera_start_pos[0] = camera.position.x;
		camera_start_pos[1] = camera.position.y;
		
		wallslide_texture = new Texture(Gdx.files.internal("Ninja/wallslide.png"));
		wallslide = new TextureRegion(wallslide_texture);
		
		animation =  new Animation[2];

		walk_texture = new Texture[4];
		
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 1; i < 5; i++){
			walk_texture[i-1] = new Texture(Gdx.files.internal("Ninja/run"+i+".png"));
			temp[i-1] = new TextureRegion(walk_texture[i-1]);
		}
		animation[WALK] = new Animation(0.25f,temp);
		
		idle_texture = new Texture[2];
		temp = new TextureRegion[2];
		idle_texture[0] = new Texture(Gdx.files.internal("Ninja/idle1.png"));
		idle_texture[1] = new Texture(Gdx.files.internal("Ninja/idle2.png"));
		temp[0] = new TextureRegion(idle_texture[0]);
		temp[1] = new TextureRegion(idle_texture[1]);
		animation[IDLE] = new Animation(0.75f,temp);
		
		smokebomb_texture = new Texture[4];
		temp = new TextureRegion[4];
		for(int i = 1; i < 5; i++){
			smokebomb_texture[i-1] = new Texture(Gdx.files.internal("Misc/spr_smoke_"+i+".png"));
			temp[i-1] = new TextureRegion(smokebomb_texture[i-1]);
		}
		smoke_bomb = new Animation(0.07f,temp);
		
		gauge = new TextureRegion[5];
		gauge_texture = new Texture(Gdx.files.internal("Misc/meter.png"));
		for(int i = 0; i < 5; i++)
			gauge[i] = new TextureRegion(gauge_texture,171,0,(4-i)*14,20);
		
		jump_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/jump_10.wav"));
		slide_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/hiss.wav"));
		clock_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/clock.wav"));
		teleport_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/skill_hit.mp3"));
		death_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/death.wav"));	
		
		particlefx = new Particle(20, smokebomb_texture[0],2.2f);		
		init(x,y);
	}
	
	public void init(float x, float y){
		death_counter = 0;
		particles_on = true;
		particlefx = new Particle(14, smokebomb_texture[0],2.2f);
		position = new float[2];
		teleport_pos = new float[2];
		spawn_position = new float[2];
		position[0] = x;
		position[1] = y;
		spawn_position[0] = x;
		spawn_position[1] = y;
		
		current_slide_sound = 0;
		item_counter = 0;
		
		clock_playing = 0;
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
		jump_mod = 1;
		g_mod = 1;
		slide_l = false;
		slide_r = false;
	}
	
	public void init(float x, float y,OrthographicCamera camera){
		death_counter = 0;
		particles_on = true;
		this.camera = camera;
		particlefx = new Particle(14, smokebomb_texture[0],2.2f);
		position = new float[2];
		teleport_pos = new float[2];
		spawn_position = new float[2];
		position[0] = x;
		position[1] = y;
		spawn_position[0] = x;
		spawn_position[1] = y;
		
		current_slide_sound = 0;
		item_counter = 0;
		
		clock_playing = 0;
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
		jump_mod = 1;
		g_mod = 1;
		slide_l = false;
		slide_r = false;
	}
	
	public void update(float delta,int map[][],int width,int height,float master_volume){
		float vwidth = Gdx.graphics.getWidth();
		float vheight = Gdx.graphics.getHeight();
		float wscale = vwidth/800f;
		float hscale = vheight/600f;
		
		elapsed_time += delta * time_mod;
		
		//I Know this part looks horrible
		//But that's the way i started it :(
		
		int x1 = ((int)(position[0]+10)/64);
		int x2 = ((int)(position[0]+54)/64);
		int xr = ((int)(position[0]+64)/64);
		int xl = ((int)(position[0])/64);
		int xl2 = ((int)(position[0]+16)/64);
		int xr2 = ((int)(position[0]+48)/64);
		
		int y = ((int)(position[1]+4)/64);
		int y2 = ((int)(position[1]+48)/64);
		int y1 = ((int)(position[1]+12)/64);
		int yu = ((int)(position[1]+54)/64);

		if(y > map.length-1) y = map.length-1;
		if(y2 > map.length-1) y2 = map.length-1;
		if(y1 > map.length-1) y1 = map.length-1;
		if(yu > map.length-1) yu = map.length-1;
				
		
		if(!particles_on){
				if(particlefx.isActive())particlefx.stop();
		}		
		else{			
			if(particlefx.isActive()){
				particlefx.update(delta, camera,time_mod);
				if(!slide_l && !slide_r){
					if(particlefx.getNum() == 0)particlefx.stop();
					particlefx.canCreate(false);
				}
			}
		}
		
		if(map[y][x1] < 0 && map[y][x2] < 0)grounded = false;
		else grounded = true;
		
		if(grounded){
			jump_count = 0;
			g_mod = 1;
			slide_l = false;
			slide_r = false;
			if(System.currentTimeMillis() - current_slide_sound > 500)
				slide_sound.stop();
			current_slide_sound = 0;
			speed_y = 0;
		}	
		else{
			if(slide_l || slide_r){
				if(particles_on){
					if(!particlefx.isActive() || (particlefx.isActive()&&!particlefx.getCan())){
						particlefx.start(55f, 90, 100,60);
					}
					if(slide_r)particlefx.setOrigin(position[0]+46, position[1]);
					else particlefx.setOrigin(position[0], position[1]);
				}

				if(current_slide_sound == 0){
					slide_sound.play(0.8f*master_volume);
					current_slide_sound = System.currentTimeMillis();
				}
				if(System.currentTimeMillis() - current_slide_sound > 650)
					current_slide_sound = 0;
				speed_y = g_mod;
			}
			else if(speed_y < 7.5f) speed_y += delta*14.75f*time_mod;
		}
		
		if(slow_time){
			timer += delta*1.5f;
			if(timer > 5){
				clock_sound.setLooping(clock_playing, false);
				clock_sound.stop(clock_playing);
				clock_playing = 0;
				
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
			timer += delta*0.8f;
			smoke_elapsed += delta;
			if(timer < 5)
				current_gauge = 4 - (int)timer;
			if(timer > 5){
				stop_time = false;
				timer = 0;
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && !slow_time && !stop_time && !touching){
			slow_time = true;
			time_mod = 0.5f;
			timer = delta;
			clock_playing = clock_sound.play(0.9f*master_volume);
			clock_sound.setLooping(clock_playing,true);
		}
		
		if(Gdx.input.isTouched() && !slow_time && !stop_time){
			touching = true;
			smoke_elapsed = 0;
			
			teleport_pos[0] = position[0];
			teleport_pos[1] = position[1];
			
			int tx = (int)((camera.position.x*wscale-vwidth/2+Gdx.input.getX())/(64*wscale));
			int ty =  (int)((camera.position.y*hscale-vheight/2+(vheight-Gdx.input.getY()))/(64*hscale));
		
			if(tx > map[0].length-1)tx = map[0].length-1;
			if(ty > map.length-1)ty = map.length-1;
			
			int dx = (x1-tx)*64;
			int dy =  (y1-ty)*64;
			
			boolean naopode = true;
			if(dx != 0 && dy != 0) naopode = dx*dx + dy*dy > 36100;
			else naopode = dx*dx > 40000 || dy*dy > 25600;
			
			if(map[ty][tx] < 0 &&!naopode){
					teleport_pos[0] = tx*64;
					teleport_pos[1] = ty*64;
			}
			else{
				touching = false;
				teleport_pos[0] = 0;
				teleport_pos[1] = 0;
			}
		}
		
		if(!Gdx.input.isTouched()&& !slow_time && !stop_time && touching){
			teleport_sound.play(0.5f*master_volume);
			stop_time = true;
			touching = false;
			timer = delta;
			current_gauge = 4;
			float tx = position[0];
			float ty = position[1];
			position[0] = teleport_pos[0];
			position[1] = teleport_pos[1];
			slide_l = false;
			slide_r = false;
			current_slide_sound = 0;
			slide_sound.stop();
			jump_count = 0;
			teleport_pos[0] = tx;
			teleport_pos[1] = ty;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			facing_right = true;
			slide_l = false;
			
			if(speed_x < 4.3f)
				speed_x += delta*6.6f*time_mod;
			if(speed_x < 0)
				speed_x += delta*4.6f*time_mod;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
			
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.A)){
			facing_right = false;
			slide_r = false;
			
			if(speed_x > -4.3f)
				speed_x -= delta*6.6f*time_mod;
			if(speed_x > 0)
				speed_x -= delta*4.6f*time_mod;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else{
			if(speed_x < 0.25f && speed_x > -0.25f)speed_x = 0;
			if(speed_x > 0.25f)speed_x -= delta*8*time_mod;
			if(speed_x < -0.25f)speed_x += delta*8*time_mod;
			if(current == WALK){
				elapsed_time = 0f;
				current = IDLE;
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.W) && jump_count < 2){
			jump_sound.play(0.2f*master_volume);
			
			if(!grounded){jump_count = 2;speed_y = -5.4f*jump_mod;}
			else {jump_count = 1;speed_y = -8*jump_mod;}
			grounded = false;
			
			if(slide_l){
				speed_x += 1f;
				position[0] += 4;
				slide_l = false;
				current_slide_sound = 0;
				slide_sound.stop();
				
			}
			if(slide_r){
				speed_x -= 1f;
				position[0] -= 4;
				slide_r = false;
				slide_sound.stop();
				current_slide_sound = 0;
			}
		}
		
		
		if(speed_x > 0){
			if((map[y1][xr] > -1 || map[y2][xr] > -1)){
				if(grounded){
					speed_x = -0.5f;
				}
				else{
					speed_x = 0f;
					if(speed_y > 0.5f){
						slide_r = true;
						slide_l = false;
						jump_count = 0;
					}
				}
			}
			if(map[y1][xr] < 0 && map[y2][xr] < 0 && slide_r){
				slide_r = false;
				current_slide_sound = 0;
				slide_sound.stop();
			}
		}
		if(speed_x < 0){
			if(map[y1][xl] > -1 || map[y2][xl] > -1){
				if(grounded){
					speed_x = 0.5f;
				}
				else{
					speed_x = 0f;
					if(speed_y > 0.5f){
						slide_l = true;
						slide_r = false;
						jump_count = 0;
					}
				}
			}
			if(map[y1][xl] < 0 && map[y2][xl] < 0 && slide_l){
				slide_l = false;
				current_slide_sound = 0;
				slide_sound.stop();
			}
		}
		
		if(speed_x == 0 && ((map[y1][xl] < 0 &&
				map[y2][xl] < 0 && slide_l)||(map[y1][xr] < 0 && map[y2][xr] < 0 && slide_r))){
			slide_r = false;
			slide_l = false;
			current_slide_sound = 0;
			slide_sound.stop();		
		}
		
		if((map[yu][xl2] > -1 || map[yu][xr2] > -1) && speed_y < 0){
			speed_y = 0;
			slide_r = false;
			slide_l = false;
			current_slide_sound = 0;
			slide_sound.stop();
		}
		
		
		if(time_mod == 1)
			position[0] += speed_x*time_mod;
		else if(time_mod == 0.5f)
			position[0] += speed_x*0.75f;
		
		position[1] -= speed_y*time_mod;
		
		if(position[0] < 0)
			position[0] = 0;
		if(position[0] > width-72)
			position[0] = width-72;
		
		
	}
	
	public void die(float master_volume){
		death_sound.play(master_volume);
		clock_sound.stop();
		clock_playing = 0;
		teleport_sound.stop();
		current_slide_sound = 0;
		slide_sound.stop();
		int temp = death_counter;
		init(spawn_position[0],spawn_position[1]);
		death_counter = temp + 1;
		camera.translate(camera_start_pos[0] - camera.position.x,camera_start_pos[1] - camera.position.y);
	}
	
	public void draw(SpriteBatch batch){
		if(particles_on){
			if(particlefx.isActive())particlefx.draw(batch);
		}
			
		
		TextureRegion frame;
		if(!slide_l && !slide_r){
			frame = animation[current].getKeyFrame(elapsed_time,true);
			if(facing_right == frame.isFlipX())
				frame.flip(true, false);
		}else{
			frame = wallslide;
			if(facing_right == frame.isFlipX())
				frame.flip(true, false);
		}
		
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
	
	public Rectangle rect(){
		return new Rectangle(position[0]+2,position[1]+2,60,60);
	}
	
	public void dispose(){
		camera = null;
		smoke_bomb = null;
		animation = null;
		wallslide = null;;
		gauge = null;
		
		particlefx.dispose();
		jump_sound.dispose();
		slide_sound.dispose();
		clock_sound.dispose();
		teleport_sound.dispose();
		death_sound.dispose();
		
		gauge_texture.dispose();
		wallslide_texture.dispose();
		for(int i = 0; i < walk_texture.length; i++)walk_texture[i].dispose();
		walk_texture = null;
		for(int i = 0; i < smokebomb_texture.length; i++)smokebomb_texture[i].dispose();
		smokebomb_texture = null;
		for(int i = 0; i < idle_texture.length; i++)idle_texture[i].dispose();
		idle_texture = null;
	}
	
	
}




