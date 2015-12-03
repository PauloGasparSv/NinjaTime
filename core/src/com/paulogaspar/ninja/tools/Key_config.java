package com.paulogaspar.ninja.tools;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Key_config {

	public static int JUMP_BUTTON;
	public static int SHOOT_BUTTON;
	public static int TELEPORT_BUTTON;
	public static int TIME_BUTTON;
	public static int MENU_BUTTON;
	
	public static int JUMP_KEY;
	public static int SHOOT_KEY;
	public static int TELEPORT_KEY;
	public static int TIME_KEY;
	public static int MENU_KEY;
	public static int UP_KEY;
	public static int DOWN_KEY;
	public static int LEFT_KEY;
	public static int RIGHT_KEY;
	public static int INTERACT_KEY;
	
	
	private static boolean jump = false;
	private static boolean shoot = false;
	private static boolean time = false;
	private static boolean menu = false;
	private static boolean teleport = false;
	private static boolean up = false;
	private static boolean down = false;
	private static boolean left = false;
	private static boolean right = false;
	
	
	private static boolean [] buttons;
	
	public static void start(){
		jump = shoot = time = menu = teleport = up = down = left = right = false;
		buttons = new boolean[144];
		for(int i = 0; i < 144; i++)buttons[i] = true;
	}
	
	public static boolean setKey(SpriteBatch batch, BitmapFont font, Texture block,Controller gamepad,OrthographicCamera camera){
		int i = -1;
		System.out.println("HELLO");
		batch.begin();
		
		
		batch.draw(block,camera.position.x-200,camera.position.y+12,420,50);

		i = getKey();
		
		if(!jump){
			font.draw(batch, "PRESS THE JUMP BUTTON", camera.position.x-158, camera.position.y+40);
			if(i != -1){
				JUMP_KEY = i;
				jump = true;
			}	
		}
		else if(!shoot){
			font.draw(batch, "PRESS THE SHOOT BUTTON", camera.position.x-162, camera.position.y+40);
			if(i != -1 && i != JUMP_KEY){
				SHOOT_KEY = i;
				shoot = true;
			}
		}
		else if(!teleport ){
			font.draw(batch, "PRESS THE TELEPORT BUTTON", camera.position.x-188, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY){
				TELEPORT_KEY = i;
				teleport = true;
			}
		}
		else if(!time){
			font.draw(batch, "PRESS THE SLOW TIME BUTTON", camera.position.x-195, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY && i != TELEPORT_KEY){
				TIME_KEY = i;
				time = true;
			}
		}
		else if(!menu){
			font.draw(batch, "PRESS THE MENU BUTTON", camera.position.x-158, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY && i != TELEPORT_KEY && i != TIME_KEY){
				MENU_KEY = i;
				menu = true;
			}
		}
		else if(!up){
			font.draw(batch, "PRESS THE UP BUTTON", camera.position.x-152, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY && i != TELEPORT_KEY && i != TIME_KEY && i!=MENU_KEY){
				UP_KEY = i;
				up = true;
			}
		}
		else if(!down){
			font.draw(batch, "PRESS THE DOWN BUTTON", camera.position.x-158, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY && i != TELEPORT_KEY && i != TIME_KEY && i!=MENU_KEY&&
					i!= UP_KEY){
				DOWN_KEY = i;
				down = true;
			}
		}
		else if(!left){
			font.draw(batch, "PRESS THE LEFT BUTTON", camera.position.x-158, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY && i != TELEPORT_KEY && i != TIME_KEY && i!=MENU_KEY&&
					i!= UP_KEY && i!= DOWN_KEY){
				LEFT_KEY = i;
				left = true;
			}
		}
		else if(!right){
			font.draw(batch, "PRESS THE RIGHT BUTTON", camera.position.x-158, camera.position.y+40);

			if(i != -1 && i != JUMP_KEY && i != SHOOT_KEY && i != TELEPORT_KEY && i != TIME_KEY && i!=MENU_KEY&&
					i!= UP_KEY && i!= DOWN_KEY && i!= LEFT_KEY){
				RIGHT_KEY = i;
				right = true;
			}
		}
		
		
		batch.end();
		if(jump && shoot && time && menu && teleport && up && down && left && right){
			start();
			DataManager dm = new DataManager();
			dm.saveKeys();
			return true;
		}
		return false;
	}
	public static boolean setButton(SpriteBatch batch, BitmapFont font, Texture block,Controller gamepad,OrthographicCamera camera){
		int i = -1;
			
		batch.begin();
		
		batch.draw(block,camera.position.x-200,camera.position.y+12,420,50);

		i = getButton(gamepad);
		
		if(!jump){
			font.draw(batch, "PRESS THE JUMP BUTTON", camera.position.x-158, camera.position.y+40);
			if(i != -1){
				JUMP_BUTTON = i;
				jump = true;
			}	
		}
		else if(!shoot){
			font.draw(batch, "PRESS THE SHOOT BUTTON", camera.position.x-162, camera.position.y+40);
			if(i != -1 && i != JUMP_BUTTON){
				SHOOT_BUTTON = i;
				shoot = true;
			}
		}
		else if(!teleport ){
			font.draw(batch, "PRESS THE TELEPORT BUTTON", camera.position.x-188, camera.position.y+40);

			if(i != -1 && i != JUMP_BUTTON && i != SHOOT_BUTTON){
				TELEPORT_BUTTON = i;
				teleport = true;
			}
		}
		else if(!time){
			font.draw(batch, "PRESS THE SLOW TIME BUTTON", camera.position.x-195, camera.position.y+40);

			if(i != -1 && i != JUMP_BUTTON && i != SHOOT_BUTTON && i != TELEPORT_BUTTON){
				TIME_BUTTON = i;
				time = true;
			}
		}
		else if(!menu){
			font.draw(batch, "PRESS THE MENU BUTTON", camera.position.x-158, camera.position.y+40);

			if(i != -1 && i != JUMP_BUTTON && i != SHOOT_BUTTON && i != TELEPORT_BUTTON && i != TIME_BUTTON){
				MENU_BUTTON = i;
				menu = true;
			}
		}
		
		batch.end();
		if(jump && shoot && time && menu && teleport){
			start();
			DataManager dm = new DataManager();
			dm.saveKeys();
			return true;
		}
		return false;
	}
	
	public static void delay(long a){
		long b = System.currentTimeMillis();
		while(System.currentTimeMillis() - b < a){
			
		}
		return; 
	}
	
	public static int getButton(Controller gamepad){
		for(int i = 0; i < 14; i++){
			if(!gamepad.getButton(i)){
				buttons[i] = false;
			}
			if(!buttons[i] && gamepad.getButton(i)){
				buttons[i] = true;
				return i;
			}
		
		}		
		return -1;
	}
	public static int getKey(){
		for(int i = 0; i < 144; i++){
			if(!Gdx.input.isKeyPressed(i)){
				buttons[i] = false;
			}
			if(!buttons[i] && Gdx.input.isKeyJustPressed(i)){
				buttons[i] = true;
				return i;
			}
		
		}		
		return -1;
	}

	public static void printKeys(){

	}
	
	public static void printButtons(){
		JOptionPane.showMessageDialog(null, "JUMP: "+JUMP_BUTTON+"\nSHOOT: "+SHOOT_BUTTON+"\nTELEPORT: "+TELEPORT_BUTTON+
				"\nTIME: "+TIME_BUTTON+"\nMENU: "+MENU_BUTTON);
	}
	
}

