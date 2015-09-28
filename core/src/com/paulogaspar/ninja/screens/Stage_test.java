package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.tools.TileMap;

public class Stage_test implements Screen {
	
	private SpriteBatch batch;
	private MyGame game;
	
	private OrthographicCamera camera;
	
	private TileMap tilemap;
	
	public Stage_test(MyGame game) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		
		tilemap = new TileMap();
		
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
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
		
		//on end
		//dispose();
		
	}
	
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tilemap.draw(batch);
		//batch.draw(img, 0, 0);
		
		
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
		tilemap.dispose();
	}

}
