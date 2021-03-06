package com.paulogaspar.ninja.tools;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.actors.Ninja;

public class TileMap {
	
	public int width;
	public int height;
	private int ribbon;
	public int map[][];
	private int num_tiles[];
		
	private float []pos_a;
	private float []pos_b;
	
	public boolean edit_mode;

	private Rectangle death_blocks[];

	//DELETE REFERENCE
	public TextureRegion tiles[];

	//DISPOSE
	private Texture tileset;
	
	public TileMap(String source){
		FileHandle file = Gdx.files.local("Maps/"+source);
		String text = file.readString();
		
		Scanner sc = new Scanner(text);
	
		num_tiles = new int[2];
		String t = sc.nextLine();
		num_tiles[0] = Integer.parseInt(t.split(" ")[0]);
		num_tiles[1] = Integer.parseInt(t.split(" ")[1]);
		map = new int[num_tiles[0]][num_tiles[1]];
		int num_death_blocks = 0;
		
		for(int line = 0; line < num_tiles[0]; line++){
			String temp[] = sc.nextLine().split(" ");
			for(int col = 0; col < num_tiles[1]; col++){
				map[line][col] = Integer.parseInt(temp[col]);
			}
		}
		sc.close();
		
		for(int line = 0; line < num_tiles[0]; line++){
			for(int col = 0; col < num_tiles[1]; col++){
				if(map[line][col] == -2){ 
					while(col + 1 < num_tiles[1] && map[line][col+1] == -2){
						col++;
					}
					num_death_blocks++;
				}
				if(map[line][col] == -8){
					while(col + 1 < num_tiles[1] && map[line][col+1] == -8){
						col++;
					}
					num_death_blocks++;
				}
			}
		}
		for(int col = 0; col < num_tiles[1]; col++){
			for(int line = 0; line < num_tiles[0]; line++){
				if(map[line][col] == -10){
					while(line + 1 < num_tiles[1] && map[line+1][col] == -10){
						line++;
					}
					num_death_blocks++;
				}
				if(map[line][col] == -9){
					while(line + 1 < num_tiles[1] && map[line+1][col] == -9){
						line++;
					}
					num_death_blocks++;
				}
			}
		}
		
		death_blocks = new Rectangle[num_death_blocks];
		num_death_blocks = 0;
		for(int line = 0; line < num_tiles[0]; line++){
			for(int col = 0; col < num_tiles[1]; col++){
				if(map[line][col] == -2){
					death_blocks[num_death_blocks] = new Rectangle(col*64+6,line*64,52,24);
					while(col + 1 < num_tiles[1] && map[line][col+1] == -2){
						col++;
						death_blocks[num_death_blocks].width += 64;
					}
					num_death_blocks++;
				}
				else if(map[line][col] == -8){
					death_blocks[num_death_blocks] = new Rectangle(col*64+6,line*64+44,52,24);
					while(col + 1 < num_tiles[1] && map[line][col+1] == -8){
						col++;
						death_blocks[num_death_blocks].width += 64;
					}
					num_death_blocks++;
				}
			}
		}
				
		for(int col = 0; col < num_tiles[1]; col++){
			for(int line = 0; line < num_tiles[0]; line++){
				if(map[line][col] == -10){
					death_blocks[num_death_blocks] = new Rectangle(col*64-1,line*64+10,22,42);
					while(line + 1 < num_tiles[1] && map[line+1][col] == -10){
						line++;
						death_blocks[num_death_blocks].height += 64;
					}
					num_death_blocks++;
				}
				else if(map[line][col] == -9){
					death_blocks[num_death_blocks] = new Rectangle(col*64+40,line*64+10,22,42);
					while(line + 1 < num_tiles[1] && map[line+1][col] == -9){
						line++;
						death_blocks[num_death_blocks].height += 64;
					}
					num_death_blocks++;
				}
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
		
		pos_a = new float[2];
		pos_b = new float[2];
		pos_a[0] = 0;
		pos_a[1] =0;
		pos_b[0] = 0;
		pos_b[1] =0;
		
		width = num_tiles[1]*64;
		height = num_tiles[0]*64;
		edit_mode = false;
		ribbon = -1;
		
	}
	
	public void update(OrthographicCamera camera,Ninja player,float delta,float master_volume){
		for(Rectangle block:death_blocks)
			if(new Rectangle(player.rect()).overlaps(block))
				player.die();
		edit(camera,delta);
	}
	
	public void edit(OrthographicCamera camera,float delta){
		if(edit_mode){
			camera.update();
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && camera.position.x - 400 < width-808)
				camera.translate(350*delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && camera.position.x - 400 > 8)
				camera.translate(-350*delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.UP) && camera.position.y - 300 < height-608)
				camera.translate(0,350*delta);
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && camera.position.y - 300 > 8)
				camera.translate(0,-350*delta);
			if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)){
				saveMap();
				return;
			}
			
			float vwidth = Gdx.graphics.getWidth();
			float vheight = Gdx.graphics.getHeight();
			float wscale = vwidth/800f;
			float hscale = vheight/600f;
			float x = (camera.position.x*wscale-vwidth/2+Gdx.input.getX());
			float y = (camera.position.y*hscale-vheight/2+(vheight-Gdx.input.getY()));
			
			
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
				if(pos_a[0] == pos_b[0] && pos_a[0] == 0){
					pos_a[0] = x;
					pos_a[1] = y;
				}else{
					pos_b[0] = x;
					pos_b[1] = y;
				}
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.L))
				pos_a[0] = pos_a[1] = pos_b[0] = pos_b[1] = 0;
			
			int mx = (int)(x/(64*wscale));
			int my =  (int)(y/(64*hscale));
			if(mx > num_tiles[1]-1)mx = num_tiles[1] -1;
			if(my > num_tiles[0]-1)my = num_tiles[0] -1;
			
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
				if(c == 23){
					output += -2;
					map[line][col] = -2;
				}
				else if(c == 21){
					output += -3;
					map[line][col] = -3;
				}
				else if(c == 20){
					output += -4;
					map[line][col] = -4;
				}
				else if(c == 19){
					output += -5;
					map[line][col] = -5;
				}
				else if(c == 25){
					output += -6;
					map[line][col] = -6;
				}
				else if(c == 26){
					output += -7;
					map[line][col] = -7;
				}
				else if(c == 16){
					output += -8;
					map[line][col] = -7;
				}
				else if(c == 17){
					output += -9;
					map[line][col] = -9;
				}
				else if(c == 22){
					output += -10;
					map[line][col] = -10;
				}
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
	
	public void draw(SpriteBatch batch,float camera_x,float camera_y,OrthographicCamera camera){
		int begin_x = (int)(camera_x-65)/64;
		if(begin_x < 0)
			begin_x = 0;
		int end_x = (int)(camera_x+870)/64;
		if(end_x > num_tiles[1]-1)
			end_x = num_tiles[1]-1;
		int begin_y = (int)(camera_y-70)/64;
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
	
	public void drawNoPlayer(SpriteBatch batch,float camera_x,float camera_y,OrthographicCamera camera){
		int begin_x = 0;
		int end_x = num_tiles[1]-1;
		int begin_y = 0;
		int end_y = num_tiles[0]-1;
		
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
		for(int i = 0; i < tiles.length; i++)tiles[i] = null;
		tiles = null;
		tileset.dispose();
	}
	

}
