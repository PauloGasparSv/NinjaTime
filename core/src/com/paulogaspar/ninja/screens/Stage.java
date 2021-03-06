package com.paulogaspar.ninja.screens;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
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

public class Stage {
	protected int current_option;
	protected int item_counter;
	protected int num_itens;
	protected int vwidth;
	protected int vheight;
	
	protected float stage_transition_alpha;
	protected float transition_angle;
	protected float item_alpha;
	
	protected long start_time;
	protected long timer;

	protected boolean next_stage;
	protected boolean options;
	protected boolean volume;
	protected boolean can_control;
	protected boolean changed_screen;
	protected boolean menu_press;
	protected boolean down_press;
	protected boolean up_press;
	protected boolean ok_press;
	protected boolean cancel_press;
	protected boolean disposed;
	protected boolean set_keys = false;
	protected boolean set_buttons = false;

	protected OrthographicCamera camera;
	
	protected Rectangle next_stage_door;
	
	//DELETE REFERENCEf
	protected MyGame game;
	protected Controller gamepad;
	protected Message messages[];
	protected Enemy goons[];
	
	//DISPOSE
	protected SpriteBatch batch;
	
	protected TileMap tilemap;

	protected Ninja player;
		
	protected Cannon cannons[];

	protected Master masters[];
		
	protected Item itens[];
		
	protected BitmapFont font_32,font_16;
		
	protected Texture master_texture[];
	protected Texture item_texture[];
	protected Texture cannonD,cannonR,cannonL,cannonBall;
	protected Texture ninja_star;
		
	protected Sound bomb_sound;
	protected Sound item_sound;
	
	protected Music main_theme;

	public void minorDipose(){
		game = null;
		batch.dispose();
		tilemap.dispose();
		if(cannons!=null)
		for(int i = 0; i < cannons.length; i++)cannons[i].dispose();
		if(masters!=null)
		for(int i = 0; i < masters.length; i++)masters[i].dispose();
		if(itens!=null)
		for(int i = 0; i < itens.length; i++)itens[i].dispose();
		if(goons!=null)
		for(int i = 0; i < goons.length;i++)goons[i].dispose();
	}
	

	protected void updateMenuKeyboard(float delta){
		if(!set_keys && (Gdx.input.isKeyJustPressed(Key_config.MENU_KEY)) && !volume){
			options = !options;
		}
		if(options){
			if(set_keys){
				if(Key_config.setKey(batch, font_16, player.white_box,gamepad,camera))set_keys = false;
			}
			else{
				if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN))
					current_option++;
				if(Gdx.input.isKeyJustPressed(Input.Keys.UP) ||  Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP))
					current_option--;
				if(current_option > 5) current_option = 5;
				if(current_option < 0) current_option = 0;
			}
			if(!set_keys && Gdx.input.isKeyJustPressed(Key_config.JUMP_KEY)){
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
					set_keys = true;
				}
				if(current_option == 3){
					player.particles_on = !player.particles_on;
				}
				if(current_option == 4){
					options = false;
					current_option = 0;	
					init();
				}
				if(current_option == 5){
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
					if(a == JOptionPane.YES_OPTION){
						Gdx.app.exit();
						dispose();
						return;
						
					}
				}
			}
			
		}
		if(volume){
			if(Gdx.input.isKeyJustPressed(Key_config.MENU_KEY) || Gdx.input.isKeyJustPressed(Key_config.SHOOT_KEY)){
				volume = false;
				options = true;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)||  Gdx.input.isKeyJustPressed(Input.Keys.PAGE_DOWN))
				current_option++;
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)|| Gdx.input.isKeyJustPressed(Input.Keys.PAGE_UP))
				current_option--;
			if(current_option > 2) current_option = 2;
			if(current_option < 0) current_option = 0;
			if(Gdx.input.isKeyJustPressed(Key_config.JUMP_KEY)){
				if(current_option == 1){
					player.master_volume = 0;
					main_theme.setVolume(player.master_volume);

				}
				if(current_option == 2){
					options = true;
					volume = false;
					current_option = 0;		
				}			
			}
			if(current_option == 0){
				if((Gdx.input.isKeyPressed(Input.Keys.RIGHT))&&player.master_volume <= 1)
					player.master_volume += delta * 0.4f;
				if((Gdx.input.isKeyPressed(Input.Keys.LEFT))&&player.master_volume >= 0)
					player.master_volume -= delta * 0.4f;
				if(player.master_volume > 1) player.master_volume = 1;
				if(player.master_volume < 0) player.master_volume = 0;
				main_theme.setVolume(player.master_volume);
			}
			
			
		}
	}
	
	protected void updateMenuGamepad(float delta){
		if((gamepad.getButton(Key_config.MENU_BUTTON)) && !volume && !menu_press && !set_buttons){
			options = !options;
			menu_press = true;
		}
		if(menu_press && !gamepad.getButton(Key_config.MENU_BUTTON))menu_press = false;
		if(!set_buttons && (options || volume)){
			if(ok_press && !gamepad.getButton(Key_config.JUMP_BUTTON))ok_press = false;
			if(up_press &&!(gamepad.getAxis(1) < -0.2f || gamepad.getPov(0) == PovDirection.north)) up_press = false;
			if(down_press &&!(gamepad.getAxis(1) > 0.2f || gamepad.getPov(0) == PovDirection.south)) down_press = false;
			if(!gamepad.getButton(Key_config.SHOOT_BUTTON) && cancel_press)cancel_press = false;
		}
		if(options){
			if(set_buttons){
				if(Key_config.setButton(batch, font_16, player.white_box,gamepad,camera))set_buttons = false;
			}
			else{
				if((gamepad.getAxis(1) > 0.2f || gamepad.getPov(0) == PovDirection.south) && !down_press){
					current_option++;
					down_press = true;
				}
				else if((gamepad.getAxis(1) < -0.2f || gamepad.getPov(0) == PovDirection.north) && !up_press){
					current_option--;
					up_press = true;
				}
			}
			
			
			
			if(current_option > 5) current_option = 5;
			if(current_option < 0) current_option = 0;
			
			if(!set_buttons && gamepad.getButton(Key_config.JUMP_BUTTON) && !ok_press){
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
					set_buttons = true;
				}
				if(current_option == 3){
					player.particles_on = !player.particles_on;
				}
				if(current_option == 4){
					options = false;
					current_option = 0;	
					init();
				}
				if(current_option == 5){
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
					if(a == JOptionPane.YES_OPTION){
						Gdx.app.exit();
						dispose();
						return;
						
					}
				}
			}
			
		}
		else if(volume){
			if(gamepad.getButton(Key_config.SHOOT_BUTTON) && !cancel_press){
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
			
			if(gamepad.getButton(Key_config.JUMP_BUTTON) && !ok_press){
				ok_press = true;
				if(current_option == 1){
					player.master_volume = 0;
					main_theme.setVolume(player.master_volume);

				}
				if(current_option == 2){
					options = true;
					volume = false;
					current_option = 0;		
				}			
			}
			if(current_option == 0){
				if((gamepad.getPov(0) == PovDirection.east || gamepad.getAxis(0) > 0.2f)&& player.master_volume <= 1)
					player.master_volume += delta * 0.4f;
				if((gamepad.getPov(0) == PovDirection.west || gamepad.getAxis(0) < -0.2f)&&player.master_volume >= 0)
					player.master_volume -= delta * 0.4f;
				if(player.master_volume > 1) player.master_volume = 1;
				if(player.master_volume < 0) player.master_volume = 0;
				main_theme.setVolume(player.master_volume);
			}
			
			
		}
	}
	
	
	protected void drawMenu(){
		if(options || volume){
			batch.setColor(new Color(0,0,0,0.6f));
			batch.draw(tilemap.tiles[10],camera.position.x-420,camera.position.y-320,840,640);
			batch.setColor(Color.WHITE);
		}
		if(options){
			if(!set_buttons && !set_keys){
				font_32.draw(batch, "Options", camera.position.x-110, camera.position.y+250);
				batch.draw(ninja_star,camera.position.x - 260, camera.position.y+88-54*current_option,64,64);
				
				if(current_option == 0)
					font_32.draw(batch,"RESUME",camera.position.x-96, camera.position.y+150);
				else
					font_16.draw(batch,"RESUME",camera.position.x-50, camera.position.y+125); 
				if(current_option == 1)
					font_32.draw(batch,"VOLUME",camera.position.x-100, camera.position.y+95);
				else
					font_16.draw(batch,"VOLUME",camera.position.x-45, camera.position.y+70);
				//55 AND 60
				if(current_option == 2)
					font_32.draw(batch,"SET CONTROLS",camera.position.x-180, camera.position.y+40);
				else
					font_16.draw(batch,"SET CONTROLS",camera.position.x-100, camera.position.y+15);
				
				
				if(current_option == 3)
					font_32.draw(batch,"PARTICLES "+(player.particles_on?"ON":"OFF"),camera.position.x-180, camera.position.y-15);
				else
					font_16.draw(batch,"PARTICLES "+(player.particles_on?"ON":"OFF"),camera.position.x-100	, camera.position.y-40);
				
				if(current_option == 4)
					font_32.draw(batch,"RETRY STAGE",camera.position.x-165, camera.position.y-70);
				else
					font_16.draw(batch,"RETRY STAGE",camera.position.x-95, camera.position.y-95);	
				if(current_option == 5)
					font_32.draw(batch,"QUIT GAME",camera.position.x-140, camera.position.y-125);
				else
					font_16.draw(batch,"QUIT GAME",camera.position.x-70, camera.position.y-150);
			}
		}
		if(volume){
			font_32.draw(batch, "Volume", camera.position.x-98, camera.position.y+215);
			batch.draw(ninja_star,camera.position.x - 360, camera.position.y+40-100*current_option,64,64);
			
			font_16.draw(batch,"MIN ------------------------ MAX",camera.position.x-250, camera.position.y+70);
			font_32.draw(batch,"|",camera.position.x-190+player.master_volume*375, camera.position.y+92);
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
	
	public void dispose(){
		
	}
	protected void init(){
		
	}
	

}
