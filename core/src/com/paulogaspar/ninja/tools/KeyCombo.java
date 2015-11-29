package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;

public class KeyCombo {

	String combo;
	String current;
	int len;
	int maxlen;
	
	private boolean done;
	private long timer;
	private boolean up,down,left,right,x,y,b,a;
	
	public KeyCombo(String combo){
		this.combo = combo;
		up = down = left = right = x = y = b = a = false;
		maxlen = combo.length();
		len = 0;
		done = false;
	}
	
	public boolean update(Controller c){
		System.out.println(current);
		if(done)return true;
		if(timer != -1 && System.currentTimeMillis() - timer > 500){
			current = "";
			timer = -1;
			len = 0;
		}
		if(len == maxlen && current.equals(combo)){
			done = true;
			return true;
		}
		if(c == null){
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
				current += "u";
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
				current += "d";
				len ++;
				timer = System.currentTimeMillis();
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
				current += "l";
				len ++;
				timer = System.currentTimeMillis();
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
				current += "r";
				timer = System.currentTimeMillis();
				len ++;
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
				timer = System.currentTimeMillis();
				current += "b";
				len ++;
			}
			else if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
				current += "a";
				timer = System.currentTimeMillis();
				len ++;
			}			
		}else{
			
		}
		return false;
	}
	
}
