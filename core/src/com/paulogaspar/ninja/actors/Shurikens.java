package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Shurikens {
	private Texture spr_star;
	
	private float position[][];
	private int speedx[];
	private float rotation[];
	
	private int current;
	private int sentinela;
	
	private long timer;
	
	private boolean pressing;
	
	public Shurikens(){
		spr_star = new Texture(Gdx.files.internal("Misc/spr_star_0.png"));
		position = new float[3][2];
		speedx = new int[3];
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
	}
	
	public void update_controller(Ninja player, Controller gamepad, float delta, int map[][]){
		if(gamepad.getButton(3) && !pressing){
			pressing = true;
			timer = System.currentTimeMillis();
			if(current < 3){
				if(player.facing_right){
					position[sentinela][0] = player.position[0] + 32;
					speedx[sentinela] = 1;
				}
				else{
					position[sentinela][0] = player.position[0]+12;
					speedx[sentinela] = -1;
				}
				position[sentinela][1] = player.position[1] + 12;	
				current++;
				for(int i = 0; i < 3; i++)if(speedx[i] == 0)sentinela = i;
			}
		}
		if(pressing&& !gamepad.getButton(3) && System.currentTimeMillis() - timer > 500)pressing = false;
		
		for(int i = 0; i < 3; i++){
			if(speedx[i] != 0){
				position[i][0] += speedx[i] * delta*400;
				rotation[i] -=  speedx[i]*delta*460;
				
				if(position[i][0] > player.camera.position.x + 600 || position[i][0] < player.camera.position.x - 600){
					sentinela = i;
					position[i][0] = -100;
					position[i][1] = -100;
					rotation[i] = 0;
					speedx[i] = 0;
					current --;
				}
				
			}
		}
		
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
