package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileMap {
	
	private Texture tileset;
	
	public TileMap(){
		tileset = new Texture(Gdx.files.internal("tileset.png"));
		
	}
	
	public void draw(SpriteBatch batch){
		batch.draw(tileset, 0, 0);
	}
	

}
