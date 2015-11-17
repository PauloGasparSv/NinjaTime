package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Shurikens {
	private Texture spr_star;
	
	private float position[][];
	private int speedx[];
	private int speedy[];
	private float rotation[];
	
	private int current;
	private int sentinela;
	
	private long timer;
	
	private boolean pressing;
	
	public Shurikens(){
		spr_star = new Texture(Gdx.files.internal("Misc/spr_star_0.png"));
		position = new float[3][2];
		speedx = new int[3];
		speedy = new int[3];
		rotation = new float[3];
		init();
	}
	
	public void init(){
		sentinela = 0;
		current = 0;
		timer= 0 ;
		pressing = false;
		position[0][0] = -100;
		position[0][1] = -100;
		position[1][0] = -100;
		position[1][1] = -100;
		position[2][0] = -100;
		position[2][1] = -100;
		speedx[0] = 0;
		rotation[0] = 0;
		speedx[1] = 0;
		rotation[1] = 0;
		speedx[2] = 0;
		rotation[2] = 0;
		speedy[0] = 0;
		speedy[1] = 0;
		speedy[2] = 0;
	}
	
	private void update(Ninja player, Controller gamepad, float delta, int map[][]){
		for(int i = 0; i < 3; i++){
			if(speedx[i] != 0){
				position[i][0] += speedx[i] * delta*400;
				position[i][1] += speedy[i] * delta*200;
				rotation[i] -=  speedx[i]*delta*460;
				
				if(position[i][0] > player.camera.position.x + 600 || position[i][0] < player.camera.position.x - 600){
					sentinela = i;
					position[i][0] = -100;
					position[i][1] = -100;
					rotation[i] = 0;
					speedx[i] = 0;
					speedy[i] = 0;
					current --;
				}
				
			}
		}
	}
	
	

	public void update_keyboard(Ninja player, Controller gamepad, float delta, int map[][]){
		if(Gdx.input.isKeyPressed(Input.Keys.X) && !pressing){
			pressing = true;
			timer = System.currentTimeMillis();
			if(current < 3){
				if(player.facing_right){
					position[sentinela][0] = player.position[0] + 32;
					speedx[sentinela] = 1;
					if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))speedy[sentinela] = 1;
					if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))speedy[sentinela] = -1;
				}
				else{
					position[sentinela][0] = player.position[0]+12;
					speedx[sentinela] = -1;
					if(Gdx.input.isKeyPressed(Input.Keys.UP) && Gdx.input.isKeyPressed(Input.Keys.LEFT))speedy[sentinela] = 1;
					if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.LEFT))speedy[sentinela] = -1;
				}
				position[sentinela][1] = player.position[1] + 12;	
				current++;
				for(int i = 0; i < 3; i++)if(speedx[i] == 0)sentinela = i;
			}
		}
		if(pressing&& !Gdx.input.isKeyPressed(Input.Keys.X)  && System.currentTimeMillis() - timer > 400)pressing = false;
		update(player,gamepad,delta,map);	
	}
	
	
	public void update_controller(Ninja player, Controller gamepad, float delta, int map[][]){
		if(gamepad.getButton(3) && !pressing){
			pressing = true;
			timer = System.currentTimeMillis();
			if(current < 3){
				if(player.facing_right){
					position[sentinela][0] = player.position[0] + 32;
					speedx[sentinela] = 1;
					if(gamepad.getAxis(0) > 0.2f && gamepad.getAxis(1)<-0.4f || gamepad.getPov(0) == PovDirection.northEast)speedy[sentinela] = 1;
					if(gamepad.getAxis(0) > 0.2f && gamepad.getAxis(1) > 0.4f || gamepad.getPov(0) == PovDirection.southEast)speedy[sentinela] = -1;
				}
				else{
					position[sentinela][0] = player.position[0]+12;
					speedx[sentinela] = -1;
					if(gamepad.getAxis(0) < -0.2f && gamepad.getAxis(1)<-0.4f|| gamepad.getPov(0) == PovDirection.northWest)speedy[sentinela] = 1;
					if(gamepad.getAxis(0) < -0.2f && gamepad.getAxis(1) > 0.4f|| gamepad.getPov(0) == PovDirection.southWest)speedy[sentinela] = -1;
				}
				position[sentinela][1] = player.position[1] + 12;	
				current++;
				for(int i = 0; i < 3; i++)if(speedx[i] == 0)sentinela = i;
			}
		}
		if(pressing&& !gamepad.getButton(3) && System.currentTimeMillis() - timer > 400)pressing = false;
		update(player,gamepad,delta,map);	
	}
	
	
	public void draw(SpriteBatch batch){
		for(int i = 0; i < 3; i++){
			if(speedx[i] != 0){
				batch.draw(spr_star,position[i][0],position[i][1],20,20,40,40,1,1,rotation[i],0,0,16,16,false,false);
			}
		}
		
	}
	
	public void dispose(){
		spr_star.dispose();
	}
	
}
