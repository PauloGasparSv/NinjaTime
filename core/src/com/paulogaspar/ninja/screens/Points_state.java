package com.paulogaspar.ninja.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
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
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.DataManager;
import com.paulogaspar.ninja.tools.TileMap;

public class Points_state implements Screen{
	
	private int death_counter;
	private int max_death;
	private int item_counter;
	private int max_item;
	private int grade;
	private int next_stage_id;
	
	
	private float master_volume;
	private float stage_transition_alpha;
	//private float transition_angle;
	private float x[];
	private float y[];
	private float dx[];
	private float sx[];
	private float sy[];
	
	private long time;
	private long max_time;
	
	private boolean next_stage;
	private boolean changed_screen;
	private boolean has_master;
	private boolean ended;
	
	private String[] message;

	
	private OrthographicCamera camera;
	
	//DELETE REFERENCEf
	private MyGame game;
	
	//DISPOSE
	private SpriteBatch batch;
	
	private TileMap tilemap;
	
	private Master master;
	
	private Ninja player;
	
	private BitmapFont font_32,font_16;
	
	private Texture master_texture[];
	private Texture item_texture[];
	private Texture cannonD,cannonR,cannonL,cannonBall;
	private Texture ninja_star;
	
		
	private Sound bomb_sound;
	private Sound item_sound;
	private Sound jingle;
	
	private Music main_theme;
	
	private DataManager dm;
	
	public Points_state(MyGame game,Ninja player,float volume,Texture master_texture[], Texture item_texture[],Texture cannonD,Texture cannonR,
			Texture cannonL,Texture cannonBall, Texture ninja_star, BitmapFont font_32,BitmapFont font_16, Music main_theme,
			Sound bomb_sound,Sound item_sound,int death_counter,int max_death, int item_counter, int max_item,long time,long max_time,
			boolean has_master,int next_stage_id,String message){
		master_volume = volume;
		this.next_stage_id = next_stage_id;
		batch = new SpriteBatch();
		
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
		this.death_counter = death_counter;
		this.max_death = max_death;
		this.item_counter = item_counter;
		this.max_item = max_item;
		this.has_master = has_master;
		this.message = new String[1];
		this.message[0] = message;
		this.time = time;
		this.max_time = max_time;
		jingle = Gdx.audio.newSound(Gdx.files.internal("Sfx/Jingle_Achievement_01.mp3"));
		
		init();
	}
	public void init(){
		dm = new DataManager();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		camera.translate(24, 80);
		//main_theme.play();
		//main_theme.setLooping(true);
		tilemap = new TileMap("points.mapa");
		next_stage = false;
		changed_screen = false;
		//transition_angle = 0f;
		stage_transition_alpha = 1;
		if(has_master){
			master = new Master(master_texture, 400, 124, message, "", 50, false);
			master.current_message = 0;
		}
		
		if(item_counter > 0)grade ++;
		if(item_counter == max_item)grade ++;
		if(death_counter < max_death) grade ++;
		if(death_counter == 0)grade ++;
		if(time < max_time) grade ++;
		
		x = new float[grade];
		y = new float[grade];
		dx = new float[grade];
		sx = new float[grade];
		sy = new float [grade];
		Random rand = new Random();
		for(int i = 0; i < grade; i++){
			int lr = rand.nextInt(2);
			int ud = rand.nextInt(2);
			int  decoy = rand.nextInt(100);
			if(lr == 0)	x[i] = -40-decoy-250;
			else x[i] = 800+decoy+250;		
			if(ud == 0)y[i] = -40-decoy-250;
			else y[i] = 600 + decoy+250;
			dx[i] = 200 + 90*i;
			sx[i] = dx[i] - x[i];
			sy[i] = 240 - y[i];
			if(sx[i] > 0)sx[i] -= i*20;
			if(sx[i] < 0)sx[i] += i*20;
			if(sy[i] > 0)sy[i] -= i*20;
			if(sy[i] < 0)sy[i] += i*20;
		}
		
	}
	
	private void setScreen(){
		//dm.savePoints(""+(next_stage_id),grade);
		switch(next_stage_id){
			case 2:
				game.setScreen(new Zone1Act3(game, player, master_texture, item_texture, cannonD,
						cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound,
						item_sound));
				break;
			case 3:
				game.setScreen(new Zone1Act4(game, player, master_texture, item_texture, cannonD,
						cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound,
						item_sound));
				break;
			case 4:
				game.setScreen(new Zone1Act4(game, player, master_texture, item_texture, cannonD,
						cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound,
						item_sound));
				break;
		
		}
	}
	
		
	private void update(float delta){
		Gdx.graphics.setTitle("Ninja Time Fps: "+Gdx.graphics.getFramesPerSecond());
		camera.update();
		tilemap.update(camera, player,delta, master_volume);
		player.update(delta,tilemap.map, tilemap.width, tilemap.height);
		
		if(has_master){
			master.update(delta, camera, player);
		}
		
		if(player.interact_press&&!next_stage){
			next_stage = true;
		}
		if(!ended){
			for(int i = 0; i < grade; i++){
				x[i] += sx[i] * delta*0.65f;
				y[i] += sy[i] * delta*0.65f;
				if((y[i] > 220 && y[i] < 260 && x[i] > dx[i] - 20 && x[i] < dx[i] + 20) || (sx[i] > 0 && x[i] > dx[i]+20) || (sx[i] < 0 && x[i] < dx[i]-20)){
					sx[i] = 0;
					sy[i] = 0;
					x[i] = dx[i];
					y[i] = 240;
					boolean f = true;
					for(int z = 0; z < grade; z++){
						if(x[z] != dx[z])f = false;
					}
					ended = f;
					if(ended){
						if(grade == 5)jingle.play(player.master_volume);
						break;
					}
				}
				
			}
		}
		
		
		if(next_stage){
			//CHANGE THIS PART IF THE VOLUME IS ALREADY DOWN! JUST PUT A *master_volume
			stage_transition_alpha += delta*0.9f;
			//transition_angle -= 0.2f*delta;
			//camera.zoom += transition_angle*0.05f;
			//camera.rotate(transition_angle*0.75f);
			
			if(camera.zoom < 0)camera.zoom = 0.01f;
			if(stage_transition_alpha > 1){
				stage_transition_alpha = 1;
				
				setScreen();
				//game.setScreen(new Zone());
				minorDipose();
				changed_screen = true;
				return;
			}
		}
		else if(stage_transition_alpha > 0 ){
			stage_transition_alpha -= delta * 0.5f;
			if(stage_transition_alpha < 0){stage_transition_alpha = 0;}
			
		}
		
	}
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tilemap.draw(batch,camera.position.x - 400,camera.position.y-300,camera);
		
		if(has_master)master.draw(batch, font_16);
		
		font_32.draw(batch, "Special Itens:	 "+item_counter+"/"+max_item, camera.position.x-265, camera.position.y+200);
		font_32.draw(batch, "Total deaths: "+death_counter, camera.position.x-265, camera.position.y+120);
		font_32.draw(batch, "Time: "+time/1000+" seconds", camera.position.x-265, camera.position.y+40);

		for(int i = 0; i < grade; i++){
			batch.draw(ninja_star,x[i],y[i],40,40,80,80,1,1,2*(dx[i]-x[i]),0,0,16,16,false,false);
		}
		
		if(stage_transition_alpha > 0){
			batch.setColor(new Color(0,0,0,stage_transition_alpha));
			batch.draw(tilemap.tiles[0],camera.position.x - 450, camera.position.y -350, 1200,700);
			batch.setColor(Color.WHITE);
		}
		
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
		master.dispose();
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
		jingle.dispose();
		batch.dispose();
		tilemap.dispose();
		master.dispose();
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
