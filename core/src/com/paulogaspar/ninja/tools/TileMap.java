package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileMap {
	
	private Texture tileset;
	private TextureRegion tiles[];
	
	private int num_tiles[] = {15,10};
	
	public int width = num_tiles[0]*64;
	public int height = num_tiles[1]*64;
	
	private int map[][] = {
			{10,3,4,4,4,4,4,4,2,5,-1,-1,-1,-1,-1},
			{10,5,23,23,23,23,23,23,10,5,-1,-1,-1,-1,-1},
			{0,1,-1,-1,-1,-1,-1,-1,0,1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,28,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,28,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{6,7,-1,-1,6,7,-1,-1,6,7,-1,-1,6,7,-1},
			{10,5,-1,-1,10,5,-1,-1,10,5,-1,-1,10,5,-1}};
	
	public TileMap(){
		tileset = new Texture(Gdx.files.internal("tileset.png"));
		tiles = new TextureRegion[31];
		
		int current = 0;
		for(int line = 0; line < 5; line++){
			for(int column = 0; column < 6; column++){
				tiles[current] = new TextureRegion(tileset, column*16, line*16, 16, 16);
				current++;
			}
		}
		tiles[30] = new TextureRegion(tileset,96,16,16,16);
		
	}
	
	public void draw(SpriteBatch batch){
		for(int line = 0; line < num_tiles[1]; line++)
			for(int col = 0; col < num_tiles[0]; col++)
				try{
				batch.draw(tiles[map[line][col]], col*64,line*64,64,64);
				}catch(Exception e){}
	}
	
	public void dispose(){
		tileset.dispose();
	}
	

}
