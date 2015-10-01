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
	
	private Texture cannonD,cannonR,cannonL;
	
	public Stage_test(MyGame game) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		camera.translate(0, 192);
		
		font = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
			
		tilemap = new TileMap();
		player = new Ninja(camera);
	
		num_death_blocks = 2;
		death_blocks = new Rectangle[2];
		death_blocks[0] = new Rectangle(128,216,458,64);
		death_blocks[1] = new Rectangle(0,40,tilemap.width,0);
		
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
		
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && camera.position.x - 400 < tilemap.width-808)
			camera.translate(350*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && camera.position.x - 400 > 8)
			camera.translate(-350*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.UP) && camera.position.y - 300 < tilemap.height-608)
			camera.translate(0,350*delta);
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && camera.position.y - 300 > 8)
			camera.translate(0,-350*delta);
		
		
		tilemap.edit(camera);
		for(int i = 0; i < num_death_blocks; i++)
			if(new Rectangle(player.position[0]+2,player.position[1]+2,60,60).overlaps(death_blocks[i]))
				player.die();
		
		
		
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
		font.dispose();
		tilemap.dispose();
	}

}
