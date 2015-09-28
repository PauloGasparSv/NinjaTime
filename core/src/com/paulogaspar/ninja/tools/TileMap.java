package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileMap {
	
	private Texture tileset;
	private TextureRegion tiles[];
	
	private int num_tiles[];
	
	public int width;
	public int height;
	
	public int map[][] = {
			{10,3,4,4,4,4,4,4,2,3,4,4,4,4,4},
			{10,5,23,23,23,23,23,23,10,5,-1,-1,-1,-1,-1},
			{0,1,-1,-1,-1,-1,-1,-1,0,1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,28,-1,-1,-1,-1,28,-1,-1,-1},
			{-1,-1,-1,28,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
			{7,-1,-1,6,11,11,11,11,7,-1,-1,6,7,-1,-1},
			{5,-1,-1,10,30,30,30,30,5,-1,-1,10,5,-1,-1}
			};
	
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
		
		num_tiles = new int[2];
		num_tiles[0] = map[0].length;
		num_tiles[1] = map.length;
		width = num_tiles[0]*64;
		height = num_tiles[1]*64;
		
	}
	
	public void draw(SpriteBatch batch,float camera_x,float camera_y){
		int begin_x = (int)(camera_x-20)/64;
		if(begin_x < 0)
			begin_x = 0;
		int end_x = (int)(camera_x+870)/64;
		if(end_x > num_tiles[0])
			end_x = num_tiles[0];
		int begin_y = (int)(camera_y-20)/64;
		if(begin_y < 0)
			begin_y = 0;
		int end_y = (int)(camera_y+670)/64;
		if(end_y > num_tiles[1])
			end_y = num_tiles[1];
		
		for(int line = begin_y; line < end_y; line++)
			for(int col = begin_x; col < end_x; col++)
				if(map[line][col] != -1)
					batch.draw(tiles[map[line][col]], col*64,line*64,64,64);
		
	}
	
	public void dispose(){
		tileset.dispose();
	}
	

}
