package com.paulogaspar.ninja;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.screens.*;

public class MyGame extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//setScreen(new Stage_test(this,1f));
		
		setScreen(new Zone1Act4(this,0f));
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
