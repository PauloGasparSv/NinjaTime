package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.TileMap;

public class Stage_test implements Screen {
	
	private SpriteBatch batch;
	private MyGame game;
	
	private OrthographicCamera camera;
	
	private TileMap tilemap;
	
	private Ninja player;
	
	private BitmapFont font;
	public Rectangle death_blocks[];
	private int num_death_blocks;
	
	private Texture cannonD,cannonR,cannonL,cannonBall;
	private Cannon cannons[];
	private int num_cannons;
	
	public Stage_test(MyGame game) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		camera.translate(0, 192);
		
		font = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
		cannonD = new Texture(Gdx.files.internal("Misc/spr_cannondown_0.png"));
		cannonL = new Texture(Gdx.files.internal("Misc/spr_cannonright_0.png"));
		cannonR = new Texture(Gdx.files.internal("Misc/spr_cannonleft_0.png"));
		cannonBall = new Texture(Gdx.files.internal("Misc/spr_smoke_0.png"));
		
		
		tilemap = new TileMap();
		player = new Ninja(camera);
	
		num_death_blocks = 2;
		death_blocks = new Rectangle[num_death_blocks];
		death_blocks[0] = new Rectangle(128,216,458,64);
		death_blocks[1] = new Rectangle(0,40,tilemap.width,0);
		
		num_cannons = 3;
		cannons = new Cannon[num_cannons];
		cannons[2] = new Cannon(cannonD, cannonBall, 2048, 384, Cannon.RIGHT,Cannon.LEFT_RIGHT,500);
		cannons[1] = new Cannon(cannonD, cannonBall, 2176, 256, Cannon.UP,Cannon.DOWN_UP,900);
		cannons[0] = new Cannon(cannonD, cannonBall, 2176, 896, Cannon.RIGHT,Cannon.LEFT_RIGHT,200);

		//2176 896
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.51f, 0.54f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		draw();
		
	}

	private void update(float delta){
		camera.update();
		
	
		
		tilemap.edit(camera);
		for(int i = 0; i < num_cannons; i++){
			if(i < num_death_blocks && new Rectangle(player.rect()).overlaps(death_blocks[i]))
				player.die();
			if(i < num_cannons)
				cannons[i].update(delta, camera,player);
		}
		
		player.update(delta,tilemap.map,tilemap.width,tilemap.height);
		
		if(!tilemap.edit_mode){
			if(player.position[1] > camera.position.y+50  && camera.position.y - 300 < tilemap.height-608)
				camera.translate(0, player.position[1] - camera.position.y -50);
			if(player.position[1] < camera.position.y-150  && camera.position.y - 300 > 8)
				camera.translate(0, player.position[1] - camera.position.y+150);
			if(player.position[0] > camera.position.x + 40 && camera.position.x - 400 < tilemap.width-808)
				camera.translate(player.position[0] - camera.position.x - 40,0);
			if(player.position[0] < camera.position.x -100 && camera.position.x - 400 > 8)
				camera.translate(player.position[0] - camera.position.x + 100, 0);
		}
		else{
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && camera.position.x - 400 < tilemap.width-808)
				camera.translate(350*delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && camera.position.x - 400 > 8)
				camera.translate(-350*delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.UP) && camera.position.y - 300 < tilemap.height-608)
				camera.translate(0,350*delta);
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && camera.position.y - 300 > 8)
				camera.translate(0,-350*delta);
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
				player.position[0] = Gdx.input.getX() + camera.position.x - 400;
				player.position[1] = (600-Gdx.input.getY()) + camera.position.y - 300;
			}
				
		}
		//on end
		//this.dispose();
		
		
		//DEATH
		
		
		
	}
	
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tilemap.draw(batch,camera.position.x - 400,camera.position.y-300);
		for(Cannon c:cannons)
			c.draw(batch);
		player.draw(batch);;
	
	
		//font.draw(batch,i , 20, 400);
		
		batch.end();
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

	@Override
	public void dispose() {
		batch = null;
		game = null;
		cannonD.dispose();
		cannonR.dispose();
		cannonL.dispose();
		cannonBall.dispose();
		font.dispose();
		tilemap.dispose();
	}

}
