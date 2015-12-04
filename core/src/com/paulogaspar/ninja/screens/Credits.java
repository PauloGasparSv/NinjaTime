package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.paulogaspar.ninja.actors.Enemy;
import com.paulogaspar.ninja.actors.Item;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.Key_config;
import com.paulogaspar.ninja.tools.Message;
import com.paulogaspar.ninja.tools.TileMap;

public class Credits extends Stage implements Screen{
	
	private float speed;
	private float pos;
	
	private float ppos;
	private float mpos;
	private float pspeedy;
	private float mspeedy;
	
	public Credits(MyGame game,Ninja player,Texture master_texture[], Texture item_texture[],Texture cannonD,Texture cannonR,
			Texture cannonL,Texture cannonBall, Texture ninja_star, BitmapFont font_32,BitmapFont font_16, Music m,
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
		this.bomb_sound = bomb_sound;
		this.item_sound = item_sound;
		
		
		while(m.isPlaying()){
			m.stop();
		}		
		m.dispose();

		try{
			main_theme = Gdx.audio.newMusic(Gdx.files.internal("Music/end.mp3"));
		}catch(Exception e){
			System.out.println("Could not load boss theme");
		}
		
		while(!main_theme.isPlaying()){
			try{
				main_theme.play();
				main_theme.setVolume(player.master_volume);
				main_theme.setLooping(true);
			}
			catch(Exception e){
				main_theme.stop();
			}
		}
		init();
	}
	public void init(){
		camera.translate(-camera.position.x,-camera.position.y);
	
		tilemap = new TileMap("zone1_act1.mapa");
		
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
		
		pos = -300;
		
		//pos = 1650;
		
		speed = 1;
		
		next_stage_door = new Rectangle(3595,125,110,160);

		cannons = new Cannon[0];
		itens = new Item[0];
		masters = new Master[0];
	
		ppos = -2600;
		mpos = -2600;
		pspeedy = 0;
		mspeedy = 0;
		//CREATE CANNONS AND ITENS
		
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
		

		goons  = new Enemy[0];
		
		
		messages = new Message[0];
		
	}
	
		
	private void update(float delta){
		if(gamepad != null){
			if(gamepad.getButton(Key_config.MENU_BUTTON))next_stage = true;
			if(gamepad.getPov(0) == PovDirection.south)speed = 3;
			else speed = 1;
		}
		else{
			if(Gdx.input.isKeyJustPressed(Key_config.MENU_BUTTON))next_stage = true;
			if(Gdx.input.isKeyPressed(Key_config.DOWN_KEY))speed = 3;
			else speed=1;
		}
		
		if(pos < 2650)
			pos += speed * delta * 100;
		else{
			pos = 2650;
			if(pspeedy <= 0 && ppos <= -2600){
				pspeedy = 8;
			}
			if(mspeedy <= 0 && mpos <= -2600){
				mspeedy = 6;
			}
			mspeedy -= delta * 10f;
			pspeedy -= delta * 12f;
			ppos += pspeedy;
			mpos += mspeedy;
		}
		
		if(next_stage){
			//CHANGE THIS PART IF THE VOLUME IS ALREADY DOWN! JUST PUT A *player.master_volume
			stage_transition_alpha += delta*0.75f;
			transition_angle -= 0.2f*delta;
			if(camera.zoom > 0.04)camera.zoom += transition_angle*0.05f;
			camera.rotate(transition_angle*0.75f);
			//camera.update();
			if(camera.zoom < 0)camera.zoom = 0.01f;
			if(stage_transition_alpha > 1){
				stage_transition_alpha = 1;
				main_theme.stop();
				game.setScreen(new Zone1Act1(game, player, master_texture, item_texture, cannonD,
						cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound,
						item_sound));
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
	private void draw(){
		if(disposed)return;
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		font_32.draw(batch, "CREDITS", 300, pos+200);
		font_32.draw(batch, "Created by Paulo Gaspar", 30, pos);
		
		font_32.draw(batch, "ART", 350, pos-400);
		
		batch.draw(player.jump_texture,40,pos-540,64,64);
		font_16.draw(batch, "Ninja Sprites by Kenney", 130, pos-520);

		batch.draw(master_texture[0],40,pos-640,64,64);
		font_16.draw(batch, "Tiles, sensei and itens by LittleSadNerd", 130, pos-620);
		
		batch.draw(player.shuriken.spr_star,40,pos-755,64,64);
		font_16.draw(batch, "Go check their work! Links at website!", 130, pos-720);
		
		font_32.draw(batch, "SOUND EFFECTS", 180, pos-850);
		
		batch.draw(tilemap.tiles[23],40,pos-1005,64,64);
		font_16.draw(batch, "Main theme, Boss theme and Credits", 130, pos-970);
		font_16.draw(batch, "Theme by DeadEarth", 130, pos-1000);
		
		batch.draw(item_texture[0],40,pos-1105,64,64);
		font_16.draw(batch, "Itens and victory jingle sounds", 130, pos-1070);
		font_16.draw(batch, "by Little Robot Sound Factory", 130, pos-1100);
		
		batch.draw(cannonD,40,pos-1205,64,64);
		font_16.draw(batch, "Cannon ball sound by Rust Ltd", 130, pos-1170);
	
		
		batch.draw(tilemap.tiles[29],40,pos-1305,64,64);
		font_16.draw(batch, "Jump sound by Dklon", 130, pos-1270);
		
		batch.draw(cannonBall,40,pos-1410,64,64);
		font_16.draw(batch, "Clock sound by AntumDeluge", 130, pos-1370);

		batch.draw(player.smokebomb_texture[0],40,pos-1490,64,64);
		font_16.draw(batch, "Wall sliding sound by Bart Kelsey", 130, pos-1470);
		

		font_32.draw(batch, "PROGRAMMER", 220, pos-1600);
		font_16.draw(batch, "All programmed and created by Paulo Gaspar Sena", 20, pos-1700);
		font_16.draw(batch, "do Vale. All art and audio was found on", 20, pos-1730);
		font_16.draw(batch, "www.opengameart.org under different licenses.", 20, pos-1760);
		font_16.draw(batch, "Most were avaliable under CC or were pulic", 20, pos-1790);
		font_16.draw(batch, "domain assets.", 20, pos-1820);
		
		
		font_32.draw(batch, "THANKS FOR PLAYING", 100,pos-2100);

		batch.draw(player.jump_texture,300,ppos+pos ,64,64);
		batch.draw(master_texture[0],400,mpos+pos ,64,64);
		
		if(stage_transition_alpha > 0){
			batch.setColor(new Color(0,0,0,stage_transition_alpha));
			batch.draw(tilemap.tiles[0],-50, -50, 1200,700);
			batch.setColor(Color.WHITE);
		}
		
		batch.end();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.61f, 0.64f, 0.5f, 1);
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
