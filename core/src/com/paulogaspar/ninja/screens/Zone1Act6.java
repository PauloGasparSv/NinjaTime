package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Enemy;
import com.paulogaspar.ninja.actors.Item;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.DataManager;
import com.paulogaspar.ninja.tools.Message;
import com.paulogaspar.ninja.tools.TileMap;

public class Zone1Act6 extends Stage implements Screen{
	
	boolean noShuriken;
	
	public Zone1Act6(MyGame game) {
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
	public Zone1Act6(MyGame game,Ninja player,Texture master_texture[], Texture item_texture[],Texture cannonD,Texture cannonR,
			Texture cannonL,Texture cannonBall, Texture ninja_star, BitmapFont font_32,BitmapFont font_16, Music main_theme,
			Sound bomb_sound,Sound item_sound){
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		this.player = player;
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
		camera.translate(460-camera.position.x,300-camera.position.y);
		//camera.translate(3360-camera.position.x,300-camera.position.y);
		player.init(360,180,camera);

		while(!main_theme.isPlaying()){
			main_theme.play();
			main_theme.setVolume(player.master_volume);
			main_theme.setLooping(true);	
		}

		
		tilemap = new TileMap("zone1_act6.mapa");
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
		
		disposed = false;
		menu_press = false;
		down_press = false;
		up_press = false;
		ok_press = false;
		cancel_press = false;
		
		noShuriken = true;
	
		next_stage_door = new Rectangle(3595,125,110,160);

		cannons = new Cannon[0];
		itens = new Item[3];
		masters = new Master[1];
	
		
		//CREATE CANNONS AND ITENS
		
		String message1[] = {"Some of your fellow students...","Are here to help you fail the test",
				"So be carefull!","If you get near them they will...","Attack you and you will fail",
				"Of course I told them nothing...","About you hitting back","haha I'm great","Remember one thing...",
				"It is supposed to be hard!"};
		masters[0] = new Master(master_texture, 506, 126, message1, "JUMP YO!",40, false);
		
	
		itens[0] = new Item(item_texture,60,892,item_sound,item_sound);
		itens[1] = new Item(item_texture,2545,190,item_sound,item_sound);
		itens[2] = new Item(item_texture,3400,135,item_sound,item_sound);
		
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
		
		tilemap.edit_mode = false;

		goons  = new Enemy[14];
		goons[0] = new Enemy(680, 130, 100, 0, player);
		goons[1] = new Enemy(1170, 130, 190, 0, player);
		goons[2] = new Enemy(1500, 130, -190, 0, player);
		goons[3] = new Enemy(1792, 410, 190, 1, player);
		goons[4] = new Enemy(1980, 185, -190, 1, player);
		goons[5] = new Enemy(2396, 130, 140, 0, player);
		goons[6] = new Enemy(2645, 130, -140, 0, player);
		goons[7] = new Enemy(2520, 194, 0, 0, player);
		goons[8] = new Enemy(3580, 325, -190, 1, player);

		goons[10] = new Enemy(3040, 258, 0, 0, player);
		goons[11] = new Enemy(3130, 322, 0, 0, player);
		goons[12] = new Enemy(3193, 386, 0, 0, player);
		goons[13] = new Enemy(3323, 514, 0, 0, player);
		goons[9] = new Enemy(3386, 578, 0, 0, player);

		
		goons[2].stayActive(true);
		goons[6].stayActive(true);
		goons[7].stayActive(true);
		
		messages = new Message[3];
		if(player.gamepad != null){
			messages[0] = new Message(350,325,"Use X to shoot ninja stars",8);
			messages[1] = new Message(325,295,"Point with the stick or d-pad ",8);
			messages[2] = new Message(332,265,"to change teleport direction",8);
		}
		else{
			messages[0] = new Message(350,325,"Use X to shoot ninja stars",8);
			messages[1] = new Message(315,295,"Point with the arrow keys to",8);
			messages[2] = new Message(340,265,"change teleport direction",8);
		}
		
		start_time = System.currentTimeMillis();
		timer = 0;
	}
	
		
	private void update(float delta){
		
		Gdx.graphics.setTitle("Ninja Time Fps: "+Gdx.graphics.getFramesPerSecond());
		tilemap.update(camera, player,delta, player.master_volume);

		if(player.shuriken.isThereShuriken())noShuriken = false;
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
				player.update(delta,tilemap.map,tilemap.width,tilemap.height);		
				for(Cannon c:cannons)c.update(delta, camera,player,player.master_volume);
				for(Master m:masters)m.update(delta, camera, player);
				for(Item i:itens)i.update(player, delta,player.master_volume);
				for(Message m:messages)m.update(delta, player);
				for(int e = 0; e < goons.length; e++){
					goons[e].update(delta, player, tilemap.map);
					if(goons[e].isAlive()){
						if(e == 1 && goons[e].isActive())goons[2].activate(true);
						if(e == 1 && !goons[e].isActive()) goons[2].activate(false);
						if(e == 5 && goons[e].isActive()){goons[6].activate(true);goons[7].activate(true);}
						if(e == 5 && !goons[e].isActive()){ goons[6].activate(false);goons[7].activate(false);}
					}
				}
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
			if(player.rect().overlaps(next_stage_door)){
				if(noShuriken){
					DataManager dm = new DataManager();
					if(dm.markAchievement("noshuriken", player))player.showAchievement("Peace Maker");
				}
				if(player.interact_press&&!next_stage){
					can_control = false;
					next_stage = true;
				}
			}
			
			if(next_stage){
				//CHANGE THIS PART IF THE VOLUME IS ALREADY DOWN! JUST PUT A *player.master_volume
				stage_transition_alpha += delta*0.75f;
				transition_angle -= 0.2f*delta;
				if(camera.zoom > 0.04)camera.zoom += transition_angle*0.05f;
				camera.rotate(transition_angle*0.75f);
				camera.update();
				main_theme.setVolume((1-stage_transition_alpha)*player.master_volume);
				if(camera.zoom < 0)camera.zoom = 0.01f;
				if(stage_transition_alpha > 1){
					stage_transition_alpha = 1;
					main_theme.stop();
					game.setScreen(new Points_state(game, player, player.master_volume, master_texture, item_texture, cannonD, 
							cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound, 
							item_sound,player.death_counter,4,item_counter,num_itens,timer,80000 ,true,6,"Almost there!"));
					minorDipose();
					changed_screen = true;
					return;
				}
			}
			else if(stage_transition_alpha > 0 ){
				stage_transition_alpha -= delta * 0.5f;
				if(stage_transition_alpha < 0)stage_transition_alpha = 0;
				main_theme.setVolume((1-stage_transition_alpha)*player.master_volume);
			}
			else if(player.clock_playing != -1){
				main_theme.setVolume(player.master_volume*0.2f);
			}
			else 
				main_theme.setVolume(player.master_volume);
		}
	}
	private void draw(){
		if(disposed)return;
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		
		if(!set_buttons && !set_keys){
			tilemap.draw(batch,camera.position.x - 400,camera.position.y-300,camera);
			
			for(Cannon c:cannons)c.draw(batch);
			for(Master m:masters)m.draw(batch,font_16);
			for(Item i:itens)i.draw(batch);
			for(Message m:messages)m.draw(batch, font_16, player);
			for(Enemy e:goons)e.draw(batch, player);			
			player.draw(batch,font_16);
		}
			
		if(player.position[0] > 2800){
			batch.setColor(new Color(1,1,1,0.97f));
			batch.draw(tilemap.tiles[30],3392,128,64,64);
			batch.draw(tilemap.tiles[30],3456,128,64,64);
			batch.draw(tilemap.tiles[5],3520,128,64,64);
			batch.setColor(Color.WHITE);
		}
		if(item_counter == 0){
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}
		else if(item_counter == 1){
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}	
		else if(item_counter == 2) {
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}else{
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
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
		disposed = true;
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
		player.dispose();

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
