package com.paulogaspar.ninja.screens;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Item;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.TileMap;

public class Zone1Act4 implements Screen{
	private int current_option;
	private int item_counter;
	private int vwidth;
	private int vheight;
	private int num_itens;
	
	private long start_time;
	private long timer;
	
	private float item_alpha;
	private float master_volume;
	private float stage_transition_alpha;
	private float transition_angle;
	
	private boolean next_stage;
	private boolean options;
	private boolean volume;
	private boolean can_control;
	private boolean changed_screen;
	private boolean menu_press;
	private boolean down_press;
	private boolean up_press;
	private boolean ok_press;
	private boolean cancel_press;

	
	private Rectangle next_stage_door;
	
	private OrthographicCamera camera;
	
	//DELETE REFERENCEf
	private MyGame game;

	private Controller gamepad;
	
	//DISPOSE
	private SpriteBatch batch;
	
	private TileMap tilemap;
	
	private Ninja player;
	
	private Cannon cannons[];
	
	private Master masters[];
	
	private Item itens[];
	
	private BitmapFont font_32,font_16;
	
	private Texture master_texture[];
	private Texture item_texture[];
	private Texture cannonD,cannonR,cannonL,cannonBall;
	private Texture ninja_star;
		
	private Sound bomb_sound;
	private Sound item_sound;
		
	private Music main_theme;
	
	public Zone1Act4(MyGame game,float volume) {
		master_volume = volume;
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		batch = new SpriteBatch();
		ninja_star = new Texture(Gdx.files.internal("Misc/spr_star_0.png"));
		cannonD = new Texture(Gdx.files.internal("Misc/spr_cannondown_0.png"));
		cannonL = new Texture(Gdx.files.internal("Misc/spr_cannonright_0.png"));
		cannonR = new Texture(Gdx.files.internal("Misc/spr_cannonleft_0.png"));
		cannonBall = new Texture(Gdx.files.internal("Misc/spr_smoke_0.png"));
		master_texture = new Texture[2];
		master_texture[0] = new Texture(Gdx.files.internal("Sensei/spr_boss_0.png"));
		master_texture[1] = new Texture(Gdx.files.internal("Sensei/spr_boss_1.png"));
		item_texture = new Texture[4];
		item_texture[0] = new Texture(Gdx.files.internal("Riceball/sushi1.png"));
		item_texture[1] = new Texture(Gdx.files.internal("Riceball/sushi2.png"));
		item_texture[2] = new Texture(Gdx.files.internal("Riceball/sushi3.png"));
		item_texture[3] = new Texture(Gdx.files.internal("Riceball/sushi4.png"));
		font_32 = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
		font_16 = new BitmapFont(Gdx.files.internal("Misc/font_16.fnt"),Gdx.files.internal("Misc/font_16.png"),false);
		main_theme = Gdx.audio.newMusic(Gdx.files.internal("Music/main_theme.mp3"));
		bomb_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/8bit_bomb_explosion.wav"));
		item_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/Collect_Point_00.mp3"));
		player = new Ninja(camera,80,400);
		init();
		player.camera_start_pos[0] = camera.position.x;
		player.camera_start_pos[1] = camera.position.y;
	}
	public Zone1Act4(MyGame game,Ninja player,float volume,Texture master_texture[], Texture item_texture[],Texture cannonD,Texture cannonR,
			Texture cannonL,Texture cannonBall, Texture ninja_star, BitmapFont font_32,BitmapFont font_16, Music main_theme,
			Sound bomb_sound,Sound item_sound){
		master_volume = volume;
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		this.player = player;
		player.init(80,400,camera);
		this.game = game;
		this.ninja_star = ninja_star;
		this.cannonD = cannonD;
		this.cannonL = cannonL;
		this.cannonR = cannonR;
		this.cannonBall = cannonBall;
		this.master_texture = master_texture;
		this.item_texture = item_texture;
		this.font_32 = font_32;
		this.font_16 = font_16;
		this.main_theme = main_theme;
	
		this.bomb_sound = bomb_sound;
		this.item_sound = item_sound;
		init();
	}
	public void init(){
		main_theme.setVolume(master_volume);
		main_theme.play();
		main_theme.setLooping(true);
		tilemap = new TileMap("zone1_act4.mapa");
		options = false;
		volume = false;
		current_option = 0;
		item_counter = 0;
		can_control = true;
		next_stage = false;
		transition_angle = 0f;
		changed_screen = false;
		stage_transition_alpha = 1;
		item_alpha = 0f;
		
		menu_press = false;
		down_press = false;
		up_press = false;
		ok_press = false;
		cancel_press = false;
		
		tilemap.edit_mode = false;
	
		next_stage_door = new Rectangle(2035,334,180,150);

		cannons = new Cannon[12];
		itens = new Item[2];
		masters = new Master[2];
		
		cannons[1] = new Cannon(cannonD, cannonBall, 13*64, 2*64,476, Cannon.UP, Cannon.DOWN_UP, 400, bomb_sound);
		cannons[3] = new Cannon(cannonD, cannonBall, 17*64, 2*64,476, Cannon.UP, Cannon.DOWN_UP, 400, bomb_sound);
		cannons[4] = new Cannon(cannonD, cannonBall, 20*64, 2*64,476, Cannon.UP, Cannon.DOWN_UP, 400, bomb_sound);
		cannons[5] = new Cannon(cannonD, cannonBall, 21*64, 2*64,476, Cannon.UP, Cannon.DOWN_UP, 400, bomb_sound);
		cannons[7] = new Cannon(cannonD, cannonBall, 25*64, 2*64,476, Cannon.UP, Cannon.DOWN_UP, 300, bomb_sound);
		cannons[8] = new Cannon(cannonD, cannonBall, 26*64, 2*64,476, Cannon.UP, Cannon.DOWN_UP, 300, bomb_sound);
		cannons[0] = new Cannon(cannonD, cannonBall, 10*64, 8*64,156, Cannon.DOWN, Cannon.UP_DOWN, 400, bomb_sound);
		cannons[2] = new Cannon(cannonD, cannonBall, 15*64, 8*64,156, Cannon.DOWN, Cannon.UP_DOWN, 400, bomb_sound);		
		cannons[6] = new Cannon(cannonD, cannonBall, 23*64, 8*64,156, Cannon.DOWN, Cannon.UP_DOWN, 300, bomb_sound);
		cannons[9] = new Cannon(cannonD, cannonBall, 27*64, 8*64,156, Cannon.DOWN, Cannon.UP_DOWN, 500, bomb_sound);
		cannons[10] = new Cannon(cannonD, cannonBall, 28*64, 8*64,156, Cannon.DOWN, Cannon.UP_DOWN, 500, bomb_sound);
		cannons[11] = new Cannon(cannonD,cannonBall,37*64,5*64,0,Cannon.LEFT,Cannon.RIGHT_LEFT,500,bomb_sound);

		itens[0] = new Item(item_texture,1314,132,item_sound,item_sound);
		itens[1] = new Item(item_texture,1705,370,item_sound,item_sound);
		
		num_itens = itens.length;
		
		gamepad = null;
		try{
		gamepad = Controllers.getControllers().get(0);
		}catch(Exception e){}
		
		if(player.gamepad == null && gamepad != null){
			player.gamepad = gamepad;
		}
		if(player.gamepad != null && gamepad == null){
			player.gamepad = null;
		}
		
		String message1[] = {"Time to really test you","Just take your time here","If you have any problems","Just remember our training...",
				"and use your ninja reflexes!"};
		masters[0] = new Master(master_texture, 400, 188, message1, "PRESS X DUDE!",40, false);
		
		String message2[] = {"WAIT!","Slowly"};
		masters[1] = new Master(master_texture, 1912, 188, message2, "Hue",40, false);
		
		start_time = System.currentTimeMillis();
		timer = 0;
	}
	
		
	private void update(float delta){
		
		Gdx.graphics.setTitle("Ninja Time Fps: "+Gdx.graphics.getFramesPerSecond());
		tilemap.update(camera, player,delta, master_volume);

		vwidth = Gdx.graphics.getWidth();
		vheight = Gdx.graphics.getHeight();
		float wscale = vwidth/800f;
		float hscale = vheight/600f;
		
		if(item_counter != player.item_counter){
			item_counter = player.item_counter;
			item_alpha = 0;
		}
		if(item_counter != 0 && item_alpha < 1){
			item_alpha += delta * 0.5f;
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1))tilemap.edit_mode = !tilemap.edit_mode;
		
		if(tilemap.edit_mode){
			options = false;
			volume = false;
		}
		else if(!next_stage){
			if(gamepad == null)updateMenuKeyboard(delta);
			else updateMenuGamepad(delta);
		}
		if(options || volume)start_time = System.currentTimeMillis();
		if(!options && !volume){
			timer += System.currentTimeMillis() - start_time;
			start_time =System.currentTimeMillis();
			
			if(can_control){
				player.update(delta,tilemap.map,tilemap.width,tilemap.height,master_volume);		
				for(Cannon c:cannons)c.update(delta, camera,player,master_volume);
				for(Master m:masters)m.update(delta, camera, player);
				for(Item i:itens)i.update(player, delta,master_volume);
			}
			if(!tilemap.edit_mode){
				float x = 0;
				float y = 0;
				
				if(player.position[1] > camera.position.y+50  && camera.position.y - 300 < tilemap.height-608)
					y += player.position[1] - camera.position.y -50;
				else if(player.position[1] < camera.position.y-150  && camera.position.y - 300 > 8)
					y += player.position[1] - camera.position.y + 150;
				if(player.position[0] > camera.position.x + 40 && camera.position.x - 400 < tilemap.width-808)
					x += player.position[0] - camera.position.x - 40;
				else if(player.position[0] < camera.position.x -100 && camera.position.x - 400 > 8)
					x += player.position[0] - camera.position.x + 100;
			
				if(camera.position.x + x > tilemap.width-408)x += tilemap.width - 408 - camera.position.x - x;
				else if(camera.position.x + x < 408) x+= 408 - camera.position.x - x;
				if(camera.position.y + y > tilemap.height-308)y += tilemap.height-308-camera.position.y - y;
				else if(camera.position.y + y < 308) y += 308 - camera.position.y - y;
			
				camera.translate(x,y);
				if(x!= 0 || y != 0)camera.update();
			}
			else{
				if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
					player.position[0] = (Gdx.input.getX() + camera.position.x*wscale - vwidth/2)/wscale;
					player.position[1] = ((vheight-Gdx.input.getY()) + camera.position.y*hscale - vheight/2)/hscale;
				}		
			}
			
			if(player.interact_press&&!next_stage&&player.rect().overlaps(next_stage_door)){
				can_control = false;
				next_stage = true;
			}
			
			if(next_stage){
				//CHANGE THIS PART IF THE VOLUME IS ALREADY DOWN! JUST PUT A *master_volume
				stage_transition_alpha += delta*0.75f;
				transition_angle -= 0.2f*delta;
				if(camera.zoom > 0.04)camera.zoom += transition_angle*0.05f;
				camera.rotate(transition_angle*0.75f);
				camera.update();
				main_theme.setVolume((1-stage_transition_alpha)*master_volume);
				if(camera.zoom < 0)camera.zoom = 0.01f;
				if(stage_transition_alpha > 1){
					stage_transition_alpha = 1;
					main_theme.stop();
					game.setScreen(new Points_state(game, player, master_volume, master_texture, item_texture, cannonD, 
							cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound, 
							item_sound,player.death_counter,4,item_counter,num_itens,timer,90000 ,true,2,"Keep going"));
					minorDipose();
					changed_screen = true;
					return;
				}
			}
			else if(stage_transition_alpha > 0 ){
				stage_transition_alpha -= delta * 0.5f;
				if(stage_transition_alpha < 0)stage_transition_alpha = 0;
				main_theme.setVolume((1-stage_transition_alpha)*master_volume);
			}
			
		}
	}
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tilemap.draw(batch,camera.position.x - 400,camera.position.y-300,camera);
		
		for(Cannon c:cannons)c.draw(batch);
		for(Master m:masters)m.draw(batch,font_16);
		for(Item i:itens)i.draw(batch);
		
		player.draw(batch);
	
			
		
		if(item_counter == 0){
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}
		else if(item_counter == 1){
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}	
		else {
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}	
		
		
		font_32.draw(batch,""+timer/1000 ,camera.position.x+300,camera.position.y+270);

		
		if(stage_transition_alpha > 0){
			batch.setColor(new Color(0,0,0,stage_transition_alpha));
			batch.draw(tilemap.tiles[0],camera.position.x - 450, camera.position.y -350, 1200,700);
			batch.setColor(Color.WHITE);
		}
		
		drawMenu();

		
		batch.end();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.51f, 0.54f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		if(changed_screen)return;
		draw();
	}
	
	
	@Override
	public void dispose() {
		minorDipose();
		font_32.dispose();
		font_16.dispose();
		for(int i = 0; i < master_texture.length; i++)master_texture[i].dispose();
		for(int i = 0; i < item_texture.length; i++)item_texture[i].dispose();
		cannonD.dispose();
		cannonR.dispose();
		cannonL.dispose();
		cannonBall.dispose();
		ninja_star.dispose();
		bomb_sound.dispose();
		item_sound.dispose();
		main_theme.dispose();
	}
	public void minorDipose(){
		game = null;
		batch.dispose();
		tilemap.dispose();
		for(int i = 0; i < cannons.length; i++)cannons[i].dispose();
		for(int i = 0; i < masters.length; i++)masters[i].dispose();
		for(int i = 0; i < itens.length; i++)itens[i].dispose();
	}
	



	private void updateMenuKeyboard(float delta){
		if((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) && !volume){
			options = !options;
		}
		if(options){
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S))
				current_option++;
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
				current_option--;
			if(current_option > 3) current_option = 3;
			if(current_option < 0) current_option = 0;
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
					Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
				if(current_option == 0){
					options = false;
					current_option = 0;					

				}
				if(current_option == 1){
					options = false;
					volume = true;
					current_option = 0;					
				}
				if(current_option == 2){
					player.particles_on = !player.particles_on;
				}
				if(current_option == 3){
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
					if(a == JOptionPane.YES_OPTION){
						Gdx.app.exit();
						return;
						
					}
				}
			}
			
		}
		if(volume){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
				volume = false;
				options = true;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S))
				current_option++;
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
				current_option--;
			if(current_option > 2) current_option = 2;
			if(current_option < 0) current_option = 0;
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
					Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
				if(current_option == 1){
					master_volume = 0;
					main_theme.setVolume(master_volume);

				}
				if(current_option == 2){
					options = true;
					volume = false;
					current_option = 0;		
				}			
			}
			if(current_option == 0){
				if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D))&&master_volume <= 1)
					master_volume += delta * 0.4f;
				if((Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A))&&master_volume >= 0)
					master_volume -= delta * 0.4f;
				if(master_volume > 1) master_volume = 1;
				if(master_volume < 0) master_volume = 0;
				main_theme.setVolume(master_volume);
			}
			
			
		}
	}
	
	private void updateMenuGamepad(float delta){
		if((gamepad.getButton(9) || gamepad.getButton(8)) && !volume && !menu_press){
			options = !options;
			menu_press = true;
		}
		if(menu_press && !gamepad.getButton(9) && !gamepad.getButton(8))menu_press = false;
		if(options || volume){
			if(ok_press && !gamepad.getButton(2))ok_press = false;
			if(up_press &&!(gamepad.getAxis(1) < -0.2f || gamepad.getPov(0) == PovDirection.north)) up_press = false;
			if(down_press &&!(gamepad.getAxis(1) > 0.2f || gamepad.getPov(0) == PovDirection.south)) down_press = false;
			if(!gamepad.getButton(3) && cancel_press)cancel_press = false;
		}
		if(options){
			
			if((gamepad.getAxis(1) > 0.2f || gamepad.getPov(0) == PovDirection.south) && !down_press){
				current_option++;
				down_press = true;
			}
			else if((gamepad.getAxis(1) < -0.2f || gamepad.getPov(0) == PovDirection.north) && !up_press){
				current_option--;
				up_press = true;
			}
			
			
			
			if(current_option > 3) current_option = 3;
			if(current_option < 0) current_option = 0;
			
			if(gamepad.getButton(2) && !ok_press){
				ok_press = true;
				if(current_option == 0){
					options = false;
					current_option = 0;					

				}
				if(current_option == 1){
					options = false;
					volume = true;
					current_option = 0;					
				}
				if(current_option == 2){
					player.particles_on = !player.particles_on;
				}
				if(current_option == 3){
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
					if(a == JOptionPane.YES_OPTION){
						Gdx.app.exit();
						return;
						
					}
				}
			}
			
		}
		else if(volume){
			if(gamepad.getButton(3) && !cancel_press){
				volume = false;
				options = true;
				cancel_press = true;
			}
			
			if((gamepad.getAxis(1) > 0.2f || gamepad.getPov(0) == PovDirection.south) && !down_press){
				current_option++;
				down_press = true;
			}
			else if((gamepad.getAxis(1) < -0.2f || gamepad.getPov(0) == PovDirection.north) && !up_press){
				current_option--;
				up_press = true;
			}
			
			if(current_option > 2) current_option = 2;
			if(current_option < 0) current_option = 0;
			
			if(gamepad.getButton(2) && !ok_press){
				ok_press = true;
				if(current_option == 1){
					master_volume = 0;
					main_theme.setVolume(master_volume);

				}
				if(current_option == 2){
					options = true;
					volume = false;
					current_option = 0;		
				}			
			}
			if(current_option == 0){
				if((gamepad.getPov(0) == PovDirection.east || gamepad.getAxis(0) > 0.2f)&& master_volume <= 1)
					master_volume += delta * 0.4f;
				if((gamepad.getPov(0) == PovDirection.west || gamepad.getAxis(0) < -0.2f)&&master_volume >= 0)
					master_volume -= delta * 0.4f;
				if(master_volume > 1) master_volume = 1;
				if(master_volume < 0) master_volume = 0;
				main_theme.setVolume(master_volume);
			}
			
			
		}
	}
	
	
	private void drawMenu(){
		if(options || volume){
			batch.setColor(new Color(0,0,0,0.6f));
			batch.draw(tilemap.tiles[10],camera.position.x-420,camera.position.y-320,840,640);
			batch.setColor(Color.WHITE);
		}
		if(options){
			font_32.draw(batch, "Options", camera.position.x-110, camera.position.y+215);
			batch.draw(ninja_star,camera.position.x - 260, camera.position.y+40-68*current_option,64,64);
			if(current_option == 0)
				font_32.draw(batch,"RESUME",camera.position.x-96, camera.position.y+100);
			else
				font_16.draw(batch,"RESUME",camera.position.x-50, camera.position.y+75);
			if(current_option == 1)
				font_32.draw(batch,"VOLUME",camera.position.x-100, camera.position.y+35);
			else
				font_16.draw(batch,"VOLUME",camera.position.x-45, camera.position.y+5);
			if(current_option == 2)
				font_32.draw(batch,"PARTICLES "+(player.particles_on?"ON":"OFF"),camera.position.x-180, camera.position.y-35);
			else
				font_16.draw(batch,"PARTICLES "+(player.particles_on?"ON":"OFF"),camera.position.x-100, camera.position.y-60);
			if(current_option == 3)
				font_32.draw(batch,"QUIT GAME",camera.position.x-140, camera.position.y-100);
			else
				font_16.draw(batch,"QUIT GAME",camera.position.x-70, camera.position.y-125);				
		}
		if(volume){
			font_32.draw(batch, "Volume", camera.position.x-98, camera.position.y+215);
			batch.draw(ninja_star,camera.position.x - 360, camera.position.y+40-100*current_option,64,64);
			
			font_16.draw(batch,"MIN ------------------------ MAX",camera.position.x-250, camera.position.y+70);
			font_32.draw(batch,"|",camera.position.x-190+master_volume*375, camera.position.y+92);
			if(current_option == 1)
				font_32.draw(batch,"MUTE",camera.position.x-75, camera.position.y);
			else
				font_16.draw(batch,"MUTE",camera.position.x-40, camera.position.y-25);
			if(current_option == 2)
				font_32.draw(batch,"GO BACK",camera.position.x-120, camera.position.y-100);
			else
				font_16.draw(batch,"GO BACK",camera.position.x-60, camera.position.y-125);
		}
	}
	
	
	@Override
	public void show() {
		
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}
}
