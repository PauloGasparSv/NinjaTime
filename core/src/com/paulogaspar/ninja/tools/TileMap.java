package com.paulogaspar.ninja.tools;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileMap {
	
	private Texture tileset;
	private TextureRegion tiles[];
	
	private int num_tiles[];
	
	public int width;
	public int height;
	
	public int map[][];
	public boolean edit_mode;
	private int ribbon;
	
	public TileMap(){
		FileHandle file = Gdx.files.local("Maps/mapa.mapa");
		String text = file.readString();
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(text);
	
		num_tiles = new int[2];
		String t = sc.nextLine();
		num_tiles[0] = Integer.parseInt(t.split(" ")[0]);
		num_tiles[1] = Integer.parseInt(t.split(" ")[1]);
		map = new int[num_tiles[0]][num_tiles[1]];
		for(int line = 0; line < num_tiles[0]; line++){
			System.out.println(num_tiles[0]);
			System.out.println(line);
			String temp[] = sc.nextLine().split(" ");
			
			for(int col = 0; col < num_tiles[1]; col++){
				map[line][col] = Integer.parseInt(temp[col]);
			}
		}
		
		
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
		
		width = num_tiles[1]*64;
		height = num_tiles[0]*64;
		edit_mode = true;
		ribbon = -1;
	}
	
	public void edit(OrthographicCamera camera){
		if(edit_mode){
			if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)){
				saveMap();
				return;
			}
			
			
			float mx = Gdx.input.getX();
			mx += camera.position.x-400;
			mx= (int)mx/64;
			float my = 600 - Gdx.input.getY();
			my += camera.position.y - 300;
			my=(int)my/64;
			if(Gdx.input.isKeyJustPressed(Input.Keys.C))
				ribbon = map[(int)my][(int)mx];
			if(Gdx.input.isKeyPressed(Input.Keys.V))
				map[(int)my][(int)mx] = ribbon;
			if(Gdx.input.isKeyJustPressed(Input.Keys.X))
				map[(int)my][(int)mx] = -1;
			if(Gdx.input.isKeyJustPressed(Input.Keys.Z))
				map[(int)my][(int)mx]++;
			if(map[(int)my][(int)mx]>30)
				map[(int)my][(int)mx] = 0;
		}
	}
	
	private void saveMap(){
		String output = num_tiles[0]+" "+num_tiles[1]+"\n";
		for(int line = 0; line < num_tiles[0]; line++){
			for(int col = 0; col < num_tiles[1]; col++){
				output += map[line][col] != 23?map[line][col]:-2;
				if(col != num_tiles[1])
					output+=" ";
			}
			if(line != num_tiles[0]-1)
				output += "\n";
		}
		System.out.println("OK");
		FileHandle file = Gdx.files.external("mapa.mapa");
		file.writeString(output,false);
	}
	
	public void draw(SpriteBatch batch,float camera_x,float camera_y){
		int begin_x = (int)(camera_x-20)/64;
		if(begin_x < 0)
			begin_x = 0;
		int end_x = (int)(camera_x+870)/64;
		if(end_x > num_tiles[1]-1)
			end_x = num_tiles[1]-1;
		int begin_y = (int)(camera_y-20)/64;
		if(begin_y < 0)
			begin_y = 0;
		int end_y = (int)(camera_y+670)/64;
		if(end_y > num_tiles[0]-1)
			end_y = num_tiles[0]-1;
		
		
		for(int line = begin_y; line <= end_y; line++){
			for(int col = begin_x; col <= end_x; col++){
				if(map[line][col] >= 0)
					batch.draw(tiles[map[line][col]], col*64,line*64,64,64);
				else if(map[line][col] == -2)
					batch.draw(tiles[23], col*64,line*64,64,64);
			}
		}
		
	}
	
	public void dispose(){
		tileset.dispose();
	}
	

}
