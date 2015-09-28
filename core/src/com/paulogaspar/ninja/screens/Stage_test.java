package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	public Stage_test(MyGame game) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		
		font = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
		
		tilemap = new TileMap();
		player = new Ninja(camera);
		
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
		
		if(Gdx.input.isKeyPressed(Input.Keys.L) && camera.position.x - 400 < tilemap.width-808)
			camera.translate(350*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.J) && camera.position.x - 400 > 8)
			camera.translate(-350*delta, 0);
		if(Gdx.input.isKeyPressed(Input.Keys.I) && camera.position.y - 300 < tilemap.height-608)
			camera.translate(0,350*delta);
		if(Gdx.input.isKeyPressed(Input.Keys.K) && camera.position.y - 300 > 8)
			camera.translate(0,-350*delta);
		
		player.update(delta,tilemap.map,tilemap.width,tilemap.height);
		if(camera.position.x + 400 - player.position[0] < 360 && camera.position.x - 400 < tilemap.width-808 && player.speed_x > 0)
			camera.translate(player.speed_x,0);
		if(player.position[0] - (camera.position.x - 400) < 300 && player.speed_x < 0 && camera.position.x - 400 > 8)
			camera.translate(player.speed_x, 0);
		//&& camera.position.x - 400 > 8
		
		//on end
		//this.dispose();
		
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
		font.dispose();
		tilemap.dispose();
	}

}
