package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

public class KeyCombo {

	String combo;
	String current;
	int len;
	int maxlen;
	
	private boolean done;
	private boolean playonce;
	private long timer;
	private boolean up,down,left,right,b,x,downl,downr;
	
	public KeyCombo(String combo){
		this.combo = combo;
		up = down = left = right = this.b  = x = downr = downl = false;
		maxlen = combo.length();
		len = 0;
		playonce = true;
		current = "";
		done = false;
		timer = -1;
	}
	
	public boolean atLeastContains(String a){
		boolean b  = current.contains(a);
		if(b){
			current = "";
			up = down = left = right = this.b = x = downr = downl = false;
			len = 0;
			timer = -1;
		}
		return b; 
	}
	
	public void setPlayOnce(boolean p){
		playonce = p;
	}
	
	public boolean update(Controller c){
		System.out.println(current);
		if(done && playonce)return true;
		if(timer != -1 && System.currentTimeMillis() - timer > 500){
			current = "";
			timer = -1;
			len = 0;
		}
		if(len == maxlen && current.equals(combo)){
			done = true;
			if(!playonce){
				current = "";
				up = down = left = right = this.b = x = downr = downl = false;
				len = 0;
				timer = -1;
			}
			return true;
		}
		if(c == null){
			if(!Gdx.input.isKeyPressed(Key_config.DOWN_KEY)&& !Gdx.input.isKeyJustPressed(Key_config.RIGHT_KEY))down = false;
			if(!Gdx.input.isKeyPressed(Key_config.DOWN_KEY)&& !Gdx.input.isKeyJustPressed(Key_config.LEFT_KEY))down = false;
			if(!Gdx.input.isKeyPressed(Key_config.RIGHT_KEY))right = false;
			if(!Gdx.input.isKeyPressed(Key_config.LEFT_KEY))left = false;
			
			if(Gdx.input.isKeyPressed(Key_config.DOWN_KEY) && Gdx.input.isKeyPressed(Key_config.RIGHT_KEY) && !down){
				current += "1";
				len ++;
				down = true;
				timer = System.currentTimeMillis();
			}
			if(Gdx.input.isKeyPressed(Key_config.DOWN_KEY) && Gdx.input.isKeyPressed(Key_config.LEFT_KEY) && !down){
				current += "1";
				len ++;
				down = true;
				timer = System.currentTimeMillis();
			}
			
			if(Gdx.input.isKeyJustPressed(Key_config.UP_KEY)){
				current += "u";
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(Gdx.input.isKeyJustPressed(Key_config.DOWN_KEY) && !down){
				current += "d";
				len ++;
				timer = System.currentTimeMillis();
			}
			else if(Gdx.input.isKeyPressed(Key_config.LEFT_KEY) && !down && !left){
				current += "l";
				left = true;
				len ++;
				timer = System.currentTimeMillis();
			}
			else if(Gdx.input.isKeyPressed(Key_config.RIGHT_KEY) && !down && !right){
				current += "r";
				timer = System.currentTimeMillis();
				right = true;
				len ++;
			}
			else if(Gdx.input.isKeyJustPressed(Key_config.JUMP_KEY)){
				timer = System.currentTimeMillis();
				current += "b";
				len ++;
			}
			else if(Gdx.input.isKeyJustPressed(Key_config.SHOOT_KEY)){
				timer = System.currentTimeMillis();
				current += "a";
				len ++;
			}
		}else{
			if(c.getPov(0) == PovDirection.southEast&& !downr){
				current += "1";
				downr = true;
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(downr && c.getPov(0) != PovDirection.southEast)downr = false;
			if(c.getPov(0) == PovDirection.southWest&& !downl){
				current += "1";
				downl = true;
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(downl && c.getPov(0) != PovDirection.southWest)downl = false;
			
			if(c.getPov(0) == PovDirection.north && !up){
				current += "u";
				up = true;
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(up && c.getPov(0) != PovDirection.north)up = false;
			
			if(c.getPov(0) == PovDirection.south && !down){
				current += "d";
				down = true;
				len ++;
				timer = System.currentTimeMillis();
			}
			else if(down && c.getPov(0) != PovDirection.south)down = false;
			
			if(c.getPov(0) == PovDirection.west&& !left){
				current += "l";
				left = true;
				len ++;
				timer = System.currentTimeMillis();
			}
			else if(left && c.getPov(0) != PovDirection.west)left = false;
			
			if(c.getPov(0) == PovDirection.east && !right){
				current += "r";
				right = true;
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(right&&c.getPov(0) != PovDirection.east)right = false;
			
			if(!b && c.getButton(Key_config.JUMP_BUTTON)){
				timer = System.currentTimeMillis();
				b = true;
				current += "b";
				len ++;
			}
			else if(!c.getButton(Key_config.JUMP_BUTTON) && b)b = false;
			
			if(c.getButton(Key_config.SHOOT_BUTTON) && !x){
				current += "a";
				x = true;
				timer = System.currentTimeMillis();
				len ++;
			}else if(x && !c.getButton(Key_config.SHOOT_BUTTON))x = false;
			
			
		}
		return false;
	}
	
}
