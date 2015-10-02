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
			System.out.println("X: "+(int)mx+" Y: "+(int)my);
			
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
	//21 -> -3
	private void saveMap(){
		String output = num_tiles[0]+" "+num_tiles[1]+"\n";
		for(int line = 0; line < num_tiles[0]; line++){
			for(int col = 0; col < num_tiles[1]; col++){
				int c = map[line][col];
				if(c == 23)
					output += -2;
				else if(c == 21)
					output += -3;
				else if(c == 20)
					output += -4;
				else if(c == 19)
					output += -5;
				else if(c == 25)
					output += -6;
				else if(c == 26)
					output += -7;
				else if(c == 16)
					output += -8;
				else if(c == 17)
					output += -9;
				else if(c == 22)
					output += -10;
				else
					output += map[line][col];
				if(col != num_tiles[1])
					output+=" ";
			}
			if(line != num_tiles[0]-1)
				output += "\n";
		}
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
				else if(map[line][col] == -3)
					batch.draw(tiles[21], col*64,line*64,64,64);
				else if(map[line][col] == -4)
					batch.draw(tiles[20], col*64,line*64,64,64);
				else if(map[line][col] == -5)
					batch.draw(tiles[19], col*64,line*64,64,64);
				else if(map[line][col] == -6)
					batch.draw(tiles[25], col*64,line*64,64,64);
				else if(map[line][col] == -7)
					batch.draw(tiles[26], col*64,line*64,64,64);
				else if(map[line][col] == -8)
					batch.draw(tiles[16], col*64,line*64,64,64);
				else if(map[line][col] == -9)
					batch.draw(tiles[17], col*64,line*64,64,64);
				else if(map[line][col] == -10)
					batch.draw(tiles[22], col*64,line*64,64,64);
			}
		}
		
	}
	
	public void dispose(){
		tileset.dispose();
	}
	

}
