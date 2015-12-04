package com.paulogaspar.ninja;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.screens.Main_menu;
import com.paulogaspar.ninja.tools.DataManager;
import com.paulogaspar.ninja.tools.Key_config;

public class MyGame extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//setScreen(new Stage_test(this,1f));
		DataManager dm = new DataManager();
		dm.setControls();
		Key_config.start();
	
		setScreen(new Main_menu(this));
	}

	@Override
	public void render () {
		super.render();		
	}
	
	
	@Override
	public void dispose(){
		batch.dispose();
	}
	
}
