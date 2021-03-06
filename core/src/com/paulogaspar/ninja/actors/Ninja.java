package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.tools.DataManager;
import com.paulogaspar.ninja.tools.Key_config;
import com.paulogaspar.ninja.tools.Particle;

public class Ninja {
	private final int IDLE = 0, WALK = 1;
		
	private int current;
	private int jump_count;
	private int current_gauge;
	public int item_counter;
	public int death_counter;
	private int rotation_wise;
	
	public float position[];
	public float spawn_position[];
	private float teleport_pos[];
	public float camera_start_pos[];
	private float a_pos[];
	
	private float elapsed_time;
	private float smoke_elapsed;
	public float speed_y;
	public float speed_x;
	public float time_mod;
	private float timer;
	public float jump_mod;
	public float g_mod;
	public float master_volume;
	private float death_alpha;
	private float rotation;
	
	private boolean spin;
	boolean facing_right;
	private boolean stop_time;
	private boolean slow_time;
	public boolean slide_l,slide_r;	
	private boolean grounded;
	public boolean particles_on;
	private boolean pressing_c;
	private boolean y_press;
	private boolean r_press;
	private boolean l_press;
	public boolean interact_press;
	private boolean death_anim;
	private boolean show_a;
	private boolean big_head;
	private boolean edu_head;
	public boolean hat;
	
	private boolean speed_code;
	
	private String curr_a;
	
	private long current_slide_sound;
	public long clock_playing;	
	private long a_timer;
	
	//DELETE REFERENCE
	public Controller gamepad;
	public OrthographicCamera camera;
	
	private Animation smoke_bomb;
	private Animation animation[];
	
	private TextureRegion wallslide;
	private TextureRegion gauge[];
	private TextureRegion head[];
	
	//DISPOSE	
	public Shurikens shuriken;
	private Sound jump_sound;
	private Sound slide_sound;
	private Sound clock_sound;
	private Sound teleport_sound;
	private Sound death_sound;
	public Sound enemy_kill;
	public Sound star_cannon;
	public Sound master_hit;
	public Sound master_many_hit;
	
	private Texture gauge_texture;
	public Texture wallslide_texture;
	private Texture walk_texture[];
	private Texture idle_texture[];
	public Texture smokebomb_texture[];
	private Texture secret[];
	private Texture secret2;
	public Texture jump_texture;
	public Texture white_box;
	private Texture hat_texture;
	private TextureRegion hat_region;
	private Particle particlefx;
	
	public Ninja(OrthographicCamera camera,float x, float y){
		DataManager dm = new DataManager();
		hat = dm.getAchievement("hat");
		particles_on = dm.getParticles();
		master_volume = dm.getVolume();		
		this.camera = camera;
		gamepad = null;
		camera_start_pos = new float[2];
		
		wallslide_texture = new Texture(Gdx.files.internal("Ninja/wallslide.png"));
		wallslide = new TextureRegion(wallslide_texture);
		white_box = new Texture(Gdx.files.internal("Misc/box.jpg"));
		
		secret2 = new Texture(Gdx.files.internal("Misc/edu.png"));
		
		hat_texture = new Texture(Gdx.files.internal("Sensei/spr_boss_2.png"));
		hat_region = new TextureRegion(hat_texture,0,0,16,9);
		
		animation =  new Animation[2];

		speed_code = false;
		
		walk_texture = new Texture[4];
		
		master_hit = Gdx.audio.newSound(Gdx.files.internal("Sfx/Collect_Point_01.mp3"));
		master_many_hit = Gdx.audio.newSound(Gdx.files.internal("Sfx/Jingle_Win_00.mp3"));
		
		TextureRegion[] temp = new TextureRegion[4];
		for(int i = 1; i < 5; i++){
			walk_texture[i-1] = new Texture(Gdx.files.internal("Ninja/run"+i+".png"));
			temp[i-1] = new TextureRegion(walk_texture[i-1]);
		}
		animation[WALK] = new Animation(0.25f,temp);
		
		secret = new Texture[2];
		secret[0] = new Texture(Gdx.files.internal("Misc/juliano1.png"));
		secret[1] = new Texture(Gdx.files.internal("Misc/juliano2.png"));
		head = new TextureRegion[2];
		head[0] = new TextureRegion(secret[0]);
		head[1] = new TextureRegion(secret[1]);
		
		jump_texture = new Texture(Gdx.files.internal("Ninja/jump.png"));
		star_cannon = Gdx.audio.newSound(Gdx.files.internal("Sfx/Explosion_04.mp3"));
		idle_texture = new Texture[2];
		temp = new TextureRegion[2];
		idle_texture[0] = new Texture(Gdx.files.internal("Ninja/idle1.png"));
		idle_texture[1] = new Texture(Gdx.files.internal("Ninja/idle2.png"));
		temp[0] = new TextureRegion(idle_texture[0]);
		temp[1] = new TextureRegion(idle_texture[1]);
		animation[IDLE] = new Animation(0.75f,temp);
		
		enemy_kill = Gdx.audio.newSound(Gdx.files.internal("Sfx/vosh.mp3"));
		big_head = false;
		
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
		
		
		shuriken = new Shurikens();
		
		
		particlefx = new Particle(20, smokebomb_texture[0],2.2f);		
		init(x,y);
		camera_start_pos[0] = camera.position.x;
		camera_start_pos[1] = camera.position.y;
	
		
	}
	
	public void init(float x, float y){
		rotation = 0;
		spin = false;
		a_pos = new float[2];
		a_pos[0] = 0;
		a_pos[1] = 0;
		death_counter = 0;
		shuriken.init();
		y_press = false;
		death_anim = false;
		death_alpha = 1;
		l_press = false;
		r_press = false;
		particlefx = new Particle(14, smokebomb_texture[0],2.2f);
		position = new float[2];
		teleport_pos = new float[2];
		spawn_position = new float[2];
		position[0] = x;
		position[1] = y;
		spawn_position[0] = x;
		spawn_position[1] = y;
		pressing_c = false;
		
		current_slide_sound = 0;
		item_counter = 0;
		
		clock_playing = -1;
		teleport_pos[0] = 0;
		teleport_pos[1] = 0;
		
		elapsed_time = 0f;
		
		facing_right = true;
		
		current = 0;
		
		rotation_wise = 0;

		speed_x = 0;
		speed_y = 0;
		
		
		
		jump_count = 0;
		
		grounded = false;
		
		slow_time = false;
		stop_time = false;
		time_mod = 1;
		
		timer = 0;
		current_gauge = 0;
		smoke_elapsed = 0;
		jump_mod = 1;
		g_mod = 1;
		slide_l = false;
		slide_r = false;
	}
	
	public void setButtons(Controller gamepad){
		
	}
	
	public void init(float x, float y,OrthographicCamera camera){
		death_counter = 0;
		shuriken.init();
		
		show_a = false;
		curr_a = "";
		a_timer = 0;
		
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
		
		clock_playing = -1;
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
		
		timer = 0;
		current_gauge = 0;
		smoke_elapsed = 0;
		jump_mod = 1;
		g_mod = 1;
		slide_l = false;
		slide_r = false;
	}
	
	public void update(float delta,int map[][],int width,int height){
		if(!death_anim){
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
			
			if(spin){
				rotation += rotation_wise*-600*delta*time_mod;
				if((rotation_wise < 0 &&rotation > 360)||(rotation_wise > 0 &&rotation < -360)){
					rotation = 0;
					spin = false;
				}					
			}
			
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
				timer += delta*1.75f;
				if(timer > 5){
					clock_sound.stop();
					clock_playing = -1;
					
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
				timer += delta*2f;
				smoke_elapsed += delta;
				if(timer < 5)
					current_gauge = 4 - (int)timer;
				if(timer > 5){
					stop_time = false;
					timer = 0;
				}
			}
			
			if(gamepad == null)keyboardcontrol(delta,master_volume,map);
			else gamepadcontrol(delta,master_volume,map);
			
			if(speed_x > 0){
				if((map[y1][xr] > -1 || map[y2][xr] > -1)){
					if(grounded){
						speed_x = -0.5f;
					}
					else{
						speed_x = 0f;
						if(!spin && speed_y > 0.5f){
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
						if(!spin && speed_y > 0.5f){
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
			
			if(!speed_code){
				if(time_mod == 1)
					position[0] += speed_x*time_mod*1.1f;
				else if(time_mod == 0.5f)
					position[0] += speed_x*0.75f;
			}else{
				if(time_mod == 1)
					position[0] += speed_x*2f*time_mod;
				else if(time_mod == 0.5f)
					position[0] += speed_x*1.5f*0.75f;
			}
			position[1] -= speed_y*time_mod;
			
			if(position[0] < 0)
				position[0] = 0;
			if(position[0] > width-72)
				position[0] = width-72;
		}else{
			death_alpha -= delta*2.5f;
			if(death_alpha < 0){
				death_alpha = 0;
				death_anim = false;
				death_sound.play(master_volume);
				clock_sound.stop();
				clock_playing = -1;
				teleport_sound.stop();
				current_slide_sound = 0;
				slide_sound.stop();
				int temp = death_counter;
				init(spawn_position[0],spawn_position[1]);
				death_counter = temp + 1;
				if(death_counter > 99){
					DataManager dm = new DataManager();
					if(dm.markAchievement("deathlover", this))
						showAchievement("Death Lover");
				}
				camera.translate(camera_start_pos[0] - camera.position.x,camera_start_pos[1] - camera.position.y);
			}
		}
		
		if(show_a){
			if(a_pos[1] < 0 && System.currentTimeMillis() - a_timer < 1000){
				a_pos[1] += delta*100;
			}
			if(a_pos[1] > -200 && System.currentTimeMillis() - a_timer > 3000 &&a_pos[1] > -200 && System.currentTimeMillis() - a_timer < 4000 ){
				a_pos[1] -= delta*100;
			}
			if(System.currentTimeMillis() - a_timer > 4000){
				show_a = false;
				curr_a = "";
				a_timer = 0;
			}
		}
		
	}
	
	private void gamepadcontrol(float delta,float master_volume,int [][]map){
		
		if(gamepad.getPov(0) == PovDirection.north )interact_press = true;
		else interact_press = false;

		if(gamepad.getButton(Key_config.TIME_BUTTON) && !slow_time && !stop_time){
			slow_time = true;
			time_mod = 0.5f;
			timer = delta;
			clock_playing = clock_sound.play(master_volume);
			clock_sound.setLooping(clock_playing,true);
		}
		if(gamepad.getButton(Key_config.TELEPORT_BUTTON)  && !slow_time && !stop_time && !slide_l && !slide_r && current_gauge == 0){
			pressing_c = true;
						
			float tx = position[0]+32;
			tx += facing_right ? 162:-162;
			
			float ty = position[1]+12;
			if(gamepad.getAxis(1) < -0.2f){
				ty += 64;
				if(!l_press && !r_press){
					ty+=64;
					tx = position[0];
				}
			}
			else if(gamepad.getAxis(1) > 0.2f){
				ty -= 26;
				if(!l_press && !r_press){
					tx = position[0];
					ty-=64;
				}
			} else if(gamepad.getPov(0) == PovDirection.south){
				ty -= 128;
				tx = position[0];
			}
			else if(gamepad.getPov(0) == PovDirection.north){
				ty += 128;
				tx = position[0];
			}
			else if(gamepad.getPov(0) == PovDirection.southEast || gamepad.getPov(0) == PovDirection.southWest)
				ty -= 26;		
			else if(gamepad.getPov(0) == PovDirection.northEast || gamepad.getPov(0) == PovDirection.northWest)
				ty += 64;		
	
			if(ty/64 > map.length-1)ty = (map.length-1)*64;
			
			
			if(ty < 0 || ty == 0)ty = position[1];			
			//int ty = 
			if(tx < 0 || tx == 0)tx = position[0];
			if(tx/64 > map[0].length-1)tx = (map[0].length-1)*64;
			
			//int dy =  (y1-ty)*64;
			
			teleport_pos[0] = tx;
			teleport_pos[1] = ty;
			
			
		}
		
		if(pressing_c){
			pressing_c = false;
			smoke_elapsed = 0;
			
			
			
			int px = (int)teleport_pos[0]/64;
			int py = (int)teleport_pos[1]/64;
			
			if(map[py][px] < 0){
				if(speed_y > 1)speed_y = 1;
				teleport_sound.play(master_volume);
				stop_time = true;
				timer = delta;
				current_gauge = 4;
				float tx = position[0];
				float ty = position[1];
				position[0] = px * 64;
				position[1] = py * 64;
				jump_count = 0;
				teleport_pos[0] = tx;
				teleport_pos[1] = ty;
			}
			else{
				teleport_pos[0] = 0;
				teleport_pos[1] = 0;
			}
			
		}
	
		if(gamepad.getAxis(0) > 0.2f || gamepad.getPov(0) == PovDirection.east || gamepad.getPov(0) == PovDirection.northEast || gamepad.getPov(0) == PovDirection.southEast){
			facing_right = true;
			slide_l = false;
			r_press = true;
			l_press = false;
			
			if(speed_x < 4.6f)speed_x += delta*6.7f*time_mod;
			else speed_x = 4.6f;
			if(speed_x < 0)speed_x += delta*4.6f*time_mod;
			
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
			
		}
		else if(gamepad.getAxis(0) < -0.2f || gamepad.getPov(0) == PovDirection.west 
				|| gamepad.getPov(0) == PovDirection.northWest || gamepad.getPov(0) == PovDirection.southWest){
			facing_right = false;
			slide_r = false;
			r_press = false;
			l_press = true;
			
			if(speed_x > -4.6f)speed_x -= delta*6.7f*time_mod;
			else speed_x = -4.6f;
			if(speed_x > 0)speed_x -= delta*4.6f*time_mod;
				
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
		}
		else{
			r_press = false;
			l_press = false;
			if(speed_x < 0.25f && speed_x > -0.25f)speed_x = 0;
			if(speed_x > 0.25f)speed_x -= delta*8*time_mod;
			if(speed_x < -0.25f)speed_x += delta*8*time_mod;
			if(current == WALK){
				elapsed_time = 0f;
				current = IDLE;
			}
		}
		
		if((gamepad.getButton(Key_config.JUMP_BUTTON)) && !y_press && jump_count < 2){
			y_press = true;
			jump_sound.play(0.2f*master_volume);
			
			if(jump_count == 1 && speed_y < -5.3){
				spin = true;
				if(facing_right)rotation_wise = 1;
				else rotation_wise = -1;
			}
			
			if(!grounded){jump_count = 2;speed_y = -5.4f*jump_mod;}
			else {jump_count = 1;speed_y = -8*jump_mod;}
			grounded = false;
			
			if(slide_l){
				speed_x += 1.5f;
				position[0] += 8;
				slide_l = false;
				current_slide_sound = 0;
				slide_sound.stop();
				
			}
			else if(slide_r){
				speed_x -= 1.5f;
				position[0] -= 8;
				slide_r = false;
				slide_sound.stop();
				current_slide_sound = 0;
			}
		}
		if(y_press && !(gamepad.getButton(Key_config.JUMP_BUTTON)))
			y_press = false;
		
		if(!edu_head)
			shuriken.update_controller(this, gamepad, delta, map);
		
	}
	
	
	private void keyboardcontrol(float delta,float master_volume,int map[][]){
		if(Gdx.input.isKeyPressed(Key_config.INTERACT_KEY))interact_press = true;
		else interact_press = false;
		
		if(Gdx.input.isKeyJustPressed(Key_config.TIME_KEY) && !slow_time && !stop_time){
			slow_time = true;
			time_mod = 0.5f;
			timer = delta;
			clock_playing = clock_sound.play(master_volume);
			clock_sound.setLooping(clock_playing,true);
		}
		
		if(Gdx.input.isKeyPressed(Key_config.TELEPORT_KEY) && !slow_time && !stop_time && !slide_l && !slide_r && current_gauge == 0){
			pressing_c = true;
						
			float tx = position[0];
			tx += facing_right ? 128:-128;
			
			float ty = position[1]+12;
			if(Gdx.input.isKeyPressed(Key_config.UP_KEY)){
				ty += 64;
				if(!Gdx.input.isKeyPressed(Key_config.LEFT_KEY) && !Gdx.input.isKeyPressed(Key_config.RIGHT_KEY)){
					ty+=64;
					tx = position[0];
				}
			}
			if(Gdx.input.isKeyPressed(Key_config.DOWN_KEY)){
				ty -= 26;
				if(!Gdx.input.isKeyPressed(Key_config.LEFT_KEY) && !Gdx.input.isKeyPressed(Key_config.RIGHT_KEY)){
					tx = position[0];
					ty-=64;
				}
			}
			
	
			if(ty/64 > map.length-1)ty = (map.length-1)*64;
			
			
			if(ty < 0 || ty == 0)ty = position[1];			
			//int ty = 
			if(tx < 0 || tx == 0)tx = position[0];
			if(tx/64 > map[0].length-1)tx = (map[0].length-1)*64;

			
			
			//int dy =  (y1-ty)*64;
			
			teleport_pos[0] = tx;
			teleport_pos[1] = ty;
			
			
		}
		
		if(pressing_c){
			pressing_c = false;
			smoke_elapsed = 0;
			
			
			
			int px = (int)teleport_pos[0]/64;
			int py = (int)teleport_pos[1]/64;
			
			if(map[py][px] < 0){
				if(speed_y > 1)speed_y = 1;
				teleport_sound.play(master_volume);
				stop_time = true;
				timer = delta;
				current_gauge = 4;
				float tx = position[0];
				float ty = position[1];
				position[0] = px * 64;
				position[1] = py * 64;
				jump_count = 0;
				teleport_pos[0] = tx;
				teleport_pos[1] = ty;
			}
			else{
				teleport_pos[0] = 0;
				teleport_pos[1] = 0;
			}
			
		}
		
		if(Gdx.input.isKeyPressed(Key_config.RIGHT_KEY)){
			facing_right = true;
			slide_l = false;
			
			if(speed_x < 4.6f)speed_x += delta*6.6f*time_mod;
			else speed_x = 4.6f;
			if(speed_x < 0)speed_x += delta*4.6f*time_mod;
			
			if(current == IDLE){
				elapsed_time = 0f;
				current = WALK;
			}
			
		}
		else if(Gdx.input.isKeyPressed(Key_config.LEFT_KEY)){
			facing_right = false;
			slide_r = false;
			
			if(speed_x > -4.6f)speed_x -= delta*6.6f*time_mod;
			else speed_x = -4.6f;
			if(speed_x > 0)speed_x -= delta*4.6f*time_mod;
				
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
		
		if(Gdx.input.isKeyJustPressed(Key_config.JUMP_KEY) && jump_count < 2){
			jump_sound.play(0.2f*master_volume);
			if(jump_count == 1 && speed_y < -5.3){
				spin = true;
				if(facing_right)rotation_wise = 1;
				else rotation_wise = -1;
			}
			
			if(!grounded){jump_count = 2;speed_y = -5.4f*jump_mod;}
			else {jump_count = 1;speed_y = -8*jump_mod;}
			grounded = false;
			
			if(slide_l){
				speed_x += 1.5f;
				position[0] += 8;
				slide_l = false;
				current_slide_sound = 0;
				slide_sound.stop();
				
			}
			else if(slide_r){
				speed_x -= 1.5f;
				position[0] -= 8;
				slide_r = false;
				slide_sound.stop();
				current_slide_sound = 0;
			}
		}
		
		
		
		shuriken.update_keyboard(this, null, delta, map);
		
	}
	
	public void die(){	
		DataManager dm = new DataManager();
		if(dm.markAchievement("firstdeath",this)){
			showAchievement("First Blood");
		}
		if(!death_anim){
			death_anim = true;
			death_alpha = 1f;
		}
	}
	
	public void showAchievement(String a){
		show_a = true;
		curr_a = a;
		a_timer = System.currentTimeMillis();
		a_pos[0] = 0;
		a_pos[1] = -50;
	}
	
	public void drawAch(SpriteBatch batch, BitmapFont font){
		if(show_a){
			batch.setColor(new Color(0.8f,0.8f,0.8f,0.9f));
			batch.draw(white_box,camera.position.x-115,camera.position.y+235-a_pos[1],200,30);
			batch.setColor(Color.WHITE);
			font.draw(batch, curr_a, camera.position.x-100, camera.position.y+250-a_pos[1]);
		}
	}
	
	public void draw(SpriteBatch batch,BitmapFont font){
		drawAch(batch, font);
		
		if(!death_anim){
			if(particles_on){
				if(particlefx.isActive())particlefx.draw(batch);
			}
			
			shuriken.draw(batch);
			
			TextureRegion frame;
			if(!slide_l && !slide_r){
				frame = animation[current].getKeyFrame(elapsed_time,true);
				if(facing_right == frame.isFlipX())
					frame.flip(true, false);
			}else{
				pressing_c = false;
				frame = wallslide;
				if(facing_right == frame.isFlipX())
					frame.flip(true, false);
			}

		
					
			
			batch.draw(frame,position[0],position[1],32,32,64,64,1,1,rotation);
			if(big_head){
				TextureRegion frame2 = (jump_count != 0 || shuriken.isThereShuriken())?head[1]:head[0];
				if((facing_right == frame2.isFlipX() && !slide_l && !slide_r) || (facing_right != frame2.isFlipX() && (slide_l || slide_r))){
					frame2.flip(true, false);
				}
				batch.draw(frame2,position[0]+(facing_right?10:-5),position[1]+15,(facing_right?22:32),15,60,82,1,1,rotation);
			}
			if(edu_head){
				TextureRegion frame2 = new TextureRegion(secret2);
				if((facing_right != frame2.isFlipX() && !slide_l && !slide_r) || (facing_right == frame2.isFlipX() && (slide_l || slide_r))){
					frame2.flip(true, false);
				}
				batch.draw(frame2,position[0]+(facing_right?10:-5),position[1]+15,(facing_right?22:32),15,60,82,1,1,rotation);
		
			}
			if(hat){
				if(edu_head){
					if(slide_r)batch.draw(hat_region,position[0]-16,position[1]+70,102,56);
					else if(slide_l)batch.draw(hat_region,position[0]-22,position[1]+70,102,56);
					else if(facing_right)batch.draw(hat_region,position[0]-10,position[1]+70+(spin?70:0),102,56);
					else batch.draw(hat_region,position[0]-31,position[1]+70+(spin?70:0),102,56);
				}
				
				else if(big_head){
					if(slide_r)batch.draw(hat_region,position[0]-16,position[1]+70,96,48);
					else if(slide_l)batch.draw(hat_region,position[0]-22,position[1]+70,96,48);
					else if(facing_right)batch.draw(hat_region,position[0]-10,position[1]+70+(spin?70:0),96,48);
					else batch.draw(hat_region,position[0]-28,position[1]+70+(spin?70:0),96,48);
				}
				else{
					if(slide_r)batch.draw(hat_region,position[0]+8,position[1]+39,64,32);
					else if(slide_l)batch.draw(hat_region,position[0]-12,position[1]+39,64,32);
					else if(facing_right)batch.draw(hat_region,position[0],position[1]+38+(spin?38:0),64,32);
					else batch.draw(hat_region,position[0]-4,position[1]+38+(spin?38:0),64,32);
				}
			}
			
			if(slow_time || stop_time)
				batch.draw(gauge[current_gauge],position[0]+5,position[1]+70);
			
			if(pressing_c){
				batch.setColor(0, 0, 0, 0.5f);
				batch.draw(frame,teleport_pos[0],teleport_pos[1]-12,64,64);
				batch.setColor(1, 1, 1, 1);
			}
			
			if(stop_time && !smoke_bomb.isAnimationFinished(smoke_elapsed)){
				batch.draw(smoke_bomb.getKeyFrame(smoke_elapsed,false),position[0],position[1],64,64);
				batch.draw(smoke_bomb.getKeyFrame(smoke_elapsed,false),teleport_pos[0],teleport_pos[1],64,64);			
			}
		}
		else{
			TextureRegion frame;
			if(!slide_l && !slide_r){
				frame = animation[current].getKeyFrame(elapsed_time,true);
				if(facing_right == frame.isFlipX())
					frame.flip(true, false);
			}else{
	
				pressing_c = false;
				frame = wallslide;
				if(facing_right == frame.isFlipX())
					frame.flip(true, false);
			}
			
			batch.setColor(1,1,1,death_alpha);
			
			batch.draw(frame,position[0],position[1],64,64);
			batch.setColor(1,1,1,1);
			
		}
	
	}

	public void edu(){
		edu_head  = true;
	}
	public void bigHead(){
		big_head = true;
	}
	public void superSpeed(){
		speed_code = true;
	}
	
	public Rectangle rect(){
		if(death_anim) return new Rectangle(-10,-10,0,0);
		return new Rectangle(position[0]+8,position[1]+2,48,54);
	}
	
	public void dispose(){
		DataManager dm = new DataManager();
		dm.changeConfig(this);
		
		camera = null;
		smoke_bomb = null;
		animation = null;
		wallslide = null;;
		gauge = null;
		
		hat_region = null;
		hat_texture.dispose();
		secret2.dispose();
		master_many_hit.dispose();
		master_hit.dispose();
		star_cannon.dispose();
		enemy_kill.dispose();
		jump_texture.dispose();
		shuriken.dispose();
		particlefx.dispose();
		jump_sound.dispose();
		white_box.dispose();
		slide_sound.dispose();
		clock_sound.dispose();
		teleport_sound.dispose();
		death_sound.dispose();
		secret[0].dispose();
		secret[1].dispose();
		
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




