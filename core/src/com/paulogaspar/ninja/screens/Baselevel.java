package com.paulogaspar.ninja.screens;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Item;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.TileMap;

public class Baselevel implements Screen{
	
	private int current_option;
	private int item_counter;
	private int vwidth;
	private int vheight;
	
	private long start_time;
	
	private float master_volume;
	private float stage_transition_alpha;
	private float transition_angle;

	
	private boolean next_stage;
	private boolean options;
	private boolean volume;
	private boolean can_control;
	private boolean changed_screen;


	
	private OrthographicCamera camera;
	
	//DELETE REFERENCEf
	private MyGame game;
	
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
	
	public Baselevel(MyGame game,float volume) {
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
		player = new Ninja(camera,80,140);
		init();
	}
	public Baselevel(MyGame game,Ninja player,float volume,Texture master_texture[], Texture item_texture[],Texture cannonD,Texture cannonR,
			Texture cannonL,Texture cannonBall, Texture ninja_star, BitmapFont font_32,BitmapFont font_16, Music main_theme,
			Sound bomb_sound,Sound item_sound){
		master_volume = volume;
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		this.player = player;
		player.init(80, 140);
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
		//camera.translate(0, 0);
		main_theme.play();
		main_theme.setLooping(true);
		tilemap = new TileMap("zone1_act1.mapa");
		options = false;
		volume = false;
		current_option = 0;
		item_counter = 0;
		can_control = true;
		next_stage = false;
		cannons = new Cannon[0];
		itens = new Item[0];
		masters = new Master[0];
		changed_screen = false;
		transition_angle = 0f;
		stage_transition_alpha = 1;
	}
	
		
	private void update(float delta){
		Gdx.graphics.setTitle("Ninja Time Fps: "+Gdx.graphics.getFramesPerSecond());
		camera.update();
		tilemap.update(camera, player,delta, master_volume);

		vwidth = Gdx.graphics.getWidth();
		vheight = Gdx.graphics.getHeight();
		float wscale = vwidth/800f;
		float hscale = vheight/600f;
		
		if(tilemap.edit_mode){
			options = false;
			volume = false;
		}
		else if(!next_stage){
			updateMenu(delta);
		}
		if(!options && !volume){
			if(can_control)player.update(delta,tilemap.map,tilemap.width,tilemap.height,master_volume);		
			for(Cannon c:cannons)c.update(delta, camera,player,master_volume);
			for(Master m:masters)m.update(delta, camera, player);
			for(Item i:itens)i.update(player, delta,master_volume);
			
			if(!tilemap.edit_mode){
				if(player.position[1] > camera.position.y+50  && camera.position.y - 300 < tilemap.height-608)
					camera.translate(0, player.position[1] - camera.position.y -50);
				if(player.position[1] < camera.position.y-150  && camera.position.y - 300 > 8)
					camera.translate(0, player.position[1] - camera.position.y+150);
				if(player.position[0] > camera.position.x + 40 && camera.position.x - 400 < tilemap.width-808)
					camera.translate(player.position[0] - camera.position.x - 40,0);
				if(player.position[0] < camera.position.x -100 && camera.position.x - 400 > 8)
					camera.translate(player.position[0] - camera.position.x + 100, 0);
			}
			else{
				if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
					player.position[0] = (Gdx.input.getX() + camera.position.x*wscale - vwidth/2)/wscale;
					player.position[1] = ((vheight-Gdx.input.getY()) + camera.position.y*hscale - vheight/2)/hscale;
				}		
			}
			
			if(next_stage){
				//CHANGE THIS PART IF THE VOLUME IS ALREADY DOWN! JUST PUT A *master_volume
				stage_transition_alpha += delta*0.5f;
				transition_angle -= 0.2f*delta;
				camera.zoom += transition_angle*0.05f;
				camera.rotate(transition_angle*0.75f);
				main_theme.setVolume((1-stage_transition_alpha)*master_volume);
				if(camera.zoom < 0)camera.zoom = 0.01f;
				if(stage_transition_alpha > 1){
					stage_transition_alpha = 1;
					main_theme.stop();
					//game.setScreen(new Zone());
					//minorDipose(); or dipose();
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
		for(int i = 0; i < cannons.length; i++)cannons[i].dispose();
		for(int i = 0; i < masters.length; i++)masters[i].dispose();
		for(int i = 0; i < itens.length; i++)itens[i].dispose();
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
		player.init(0,0);
	}
	

	private void updateMenu(float delta){
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !volume){
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
