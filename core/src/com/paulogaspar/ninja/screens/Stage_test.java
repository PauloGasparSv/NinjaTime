package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.MyGame;

public class Stage_test implements Screen {
	
	private SpriteBatch batch;
	private MyGame game;
	
	private OrthographicCamera camera;
	
	private TileMap map;
	
	public Stage_test(MyGame game) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		
		map = new TileMap();
		
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		draw();	
		
	}

	private void update(float delta){
		camera.update();
		
		//on end
		//dispose();
		
	}
	
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		map.draw(batch);
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
	}

}
