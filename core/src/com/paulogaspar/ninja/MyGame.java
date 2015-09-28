package com.paulogaspar.ninja;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.screens.Stage_test;

public class MyGame extends Game {
	public SpriteBatch batch;
	private Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		set_screen(0);
	}

	@Override
	public void render () {
		super.render();		
	}
	
	public void set_screen(int screen){
		switch(screen){
			case 0:
				this.setScreen(new Stage_test(this));
				break;
		
		}
		
	}
	
	@Override
	public void dispose(){
		batch.dispose();
		img.dispose();
		
	}
	
}
