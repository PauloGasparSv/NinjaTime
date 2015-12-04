package com.paulogaspar.ninja.screens;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

public class Main_menu extends Stage implements Screen{

	private int current;
	private boolean pressing_down;
	private boolean pressing_up;
	private boolean ok;
	
	public Main_menu(MyGame game) {
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
	public void init(){
		current = 0;
		camera.translate(410-camera.position.x,335-camera.position.y);
		
		pressing_down = false;
		pressing_up = false;
		
		tilemap = new TileMap("menu.mapa");
		
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
		
		
		
		next_stage_door = new Rectangle(3595,125,110,160);

		cannons = new Cannon[0];
		itens = new Item[0];
		masters = new Master[0];
		
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
		goons  = new Enemy[0];
		
		
		messages = new Message[0];
		
	}
	
		
	private void update(float delta){
		tilemap.update(camera, player,delta, player.master_volume);
		
		if(!next_stage){
			
			if(gamepad == null){
				if(set_keys){
					if(Key_config.setKey(batch, font_16, player.white_box,gamepad,camera))set_keys = false;	
				}
				else{
					if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
						current ++;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
						current --;
					}
					if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)||Gdx.input.isKeyJustPressed(Key_config.JUMP_KEY)){
						if(current == 0){
							next_stage = true;
							return;
						}
						if(current == 1){
							set_keys = true;
						}
						if(current == 2){
							int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
							if(a == JOptionPane.YES_OPTION){
								Gdx.app.exit();
								dispose();
								return;
								
							}
						}
					}
				}
				if(current < 0)current = 0;
				if(current > 2)current = 2;
			}
			else{
				if(set_buttons){
					if(Key_config.setButton(batch, font_16, player.white_box, gamepad, camera))set_buttons = false;;
				}
				else{
					if(gamepad.getPov(0) != PovDirection.north)pressing_up = false;
					if(gamepad.getPov(0) != PovDirection.south)pressing_down = false;
					if(gamepad.getPov(0) == PovDirection.south && !pressing_down){
						current ++;
						pressing_down = true;
					}
					if(gamepad.getPov(0) == PovDirection.north && !pressing_up){
						current --;
						pressing_up = true;
					}
					
					if(!ok && (gamepad.getButton(Key_config.MENU_BUTTON) || gamepad.getButton(Key_config.JUMP_BUTTON))){
						if(current == 0){
							next_stage = true;
							return;
						}
						if(current == 1){
							set_buttons = true;
						}
						if(current == 2){
							int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
							if(a == JOptionPane.YES_OPTION){
								Gdx.app.exit();
								dispose();
								return;
								
							}
						}
						ok = true;
					}
					if(!(gamepad.getButton(Key_config.MENU_BUTTON) || gamepad.getButton(Key_config.JUMP_BUTTON)))ok = false;
					if(current < 0)current = 0;
					if(current > 2)current = 2;
				}
			}
			
			if(!tilemap.edit_mode){
				float x = 0;
				float y = 0;
			
				if(camera.position.x + x > tilemap.width-408)x += tilemap.width - 408 - camera.position.x - x;
				else if(camera.position.x + x < 408) x+= 408 - camera.position.x - x;
				if(camera.position.y + y > tilemap.height-308)y += tilemap.height-308-camera.position.y - y;
				else if(camera.position.y + y < 308) y += 308 - camera.position.y - y;
			
				camera.translate(x,y);
				if(x!= 0 || y != 0)camera.update();
			}
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
		System.out.println(camera.position);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		
		tilemap.drawNoPlayer(batch,camera.position.x - 400,camera.position.y-300,camera);
		
		if(!set_keys && !set_buttons){
			font_32.draw(batch,"NINJA TIME",254,520);
			if(current == 0)font_32.draw(batch,"NEW GAME",290,380);
			else font_16.draw(batch,"NEW GAME",350,360);
			if(current == 1)font_32.draw(batch,"SET CONTROLS" , 240, 300);
			else font_16.draw(batch,"SET CONTROLS" , 325, 270);
			if(current == 2)font_32.draw(batch,"EXIT" , 354, 210);
			else font_16.draw(batch,"EXIT" , 390, 180);
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
