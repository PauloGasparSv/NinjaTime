package com.paulogaspar.ninja.screens;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Item;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.TileMap;

public class Stage_test implements Screen {
	
	private int current_option;
	private int item_counter;
	private int vwidth;
	private int vheight;
	//private int checkpoint;
	
	private float item_alpha;
	private float master_volume;

	
	private boolean options;
	private boolean volume;

	private OrthographicCamera camera;
	
	//DELETE REFERENCEf
	private MyGame game;

	//DISPOSE
	private SpriteBatch batch;
	
	private TileMap tilemap;
	
	private Ninja player;
	
	private Cannon cannons[];

	private Master masters[];
	
	private Item itens[];
	
	private BitmapFont font_32,font_16;
	
	private Texture master_texture[];
	private Texture item_texture[];
	private Texture cannonD,cannonR,cannonL,cannonBall;
	private Texture ninja_star;
	
	private Sound bomb_sound;
	private Sound item_sound;
	private Sound item3_sound;
	
	private Music main_theme;
	
	
	
	public Stage_test(MyGame game,float master_volume) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		camera.translate(0, 192);
		
		this.master_volume = master_volume;
		
		
		ninja_star = new Texture(Gdx.files.internal("Misc/spr_star_0.png"));
		font_32 = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
		font_16 = new BitmapFont(Gdx.files.internal("Misc/font_16.fnt"),Gdx.files.internal("Misc/font_16.png"),false);
		cannonD = new Texture(Gdx.files.internal("Misc/spr_cannondown_0.png"));
		cannonL = new Texture(Gdx.files.internal("Misc/spr_cannonright_0.png"));
		cannonR = new Texture(Gdx.files.internal("Misc/spr_cannonleft_0.png"));
		cannonBall = new Texture(Gdx.files.internal("Misc/spr_smoke_0.png"));
		
		main_theme = Gdx.audio.newMusic(Gdx.files.internal("Music/main_theme.mp3"));
		
		tilemap = new TileMap("mapa.mapa");
		player = new Ninja(camera,80,480);
		
		bomb_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/8bit_bomb_explosion.wav"));
		item_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/Collect_Point_00.mp3"));
		item3_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/Jingle_Achievement_01.mp3"));
		
		cannons = new Cannon[3];
		cannons[2] = new Cannon(cannonD, cannonBall, 2048, 384, Cannon.RIGHT,Cannon.LEFT_RIGHT,500,bomb_sound);
		cannons[1] = new Cannon(cannonD, cannonBall, 2176, 256, Cannon.UP,Cannon.DOWN_UP,900,bomb_sound);
		cannons[0] = new Cannon(cannonD, cannonBall, 2176, 896, Cannon.RIGHT,Cannon.LEFT_RIGHT,200,bomb_sound);

		
		master_texture = new Texture[2];
		master_texture[0] = new Texture(Gdx.files.internal("Sensei/spr_boss_0.png"));
		master_texture[1] = new Texture(Gdx.files.internal("Sensei/spr_boss_1.png"));
		
		item_texture = new Texture[4];
		item_texture[0] = new Texture(Gdx.files.internal("Riceball/sushi1.png"));
		item_texture[1] = new Texture(Gdx.files.internal("Riceball/sushi2.png"));
		item_texture[2] = new Texture(Gdx.files.internal("Riceball/sushi3.png"));
		item_texture[3] = new Texture(Gdx.files.internal("Riceball/sushi4.png"));
		
		masters = new Master[4];	
		
		//I create multiple strins so i don't have to edit every single argument everytime i do a message=new string[something]
		String message[] = {"The training you must continue",
				"Jump to find the strength within you!",
				"Or don't...",
				"Enough said, excelsior!"};
		String message2[] = {"Now a harder task you must complete",
				"Leap to the top without defeat",
				"...",
				"Just avoid the cannon balls"};
		String message3[] = {"Now you must avoid the fall",
				"Press with magic through the wall",
				"...",
				"Yeah, it was pretty bad",
				"What i meant was:",
				"Go to the ledge and...",
				"Click on the other side",
				"Such magic"};
		String message4[] = {"Like a true ninja",
				"What! Where is my lunch!?",
				"You cheating bastard"};
		masters[0] = new Master(master_texture, 605, 446, message,"You can go now... please",false);
		masters[1] = new Master(master_texture, 1600, 505, message2,"Just fall off the ledge",false);
		masters[2] = new Master(master_texture, 1840, 1088, message3,"Or you can press space forever, who cares",false);
		masters[3] = new Master(master_texture, 440, 1020, message4,"GO TO THE NEXT STAGE DAMMIT!",true);
		masters[3].changeTextColor();
	
		itens = new Item[3];
		itens[0] = new Item(item_texture, 1600, 190,item_sound,item3_sound);
		itens[1] = new Item(item_texture, 0, 1220,item_sound,item3_sound);
		itens[2] = new Item(item_texture, 2810, 74,item_sound,item3_sound);

		

		
		
		//main_theme.play();
		main_theme.setVolume(master_volume);
		main_theme.setLooping(true);
		options = false;
		current_option = 0;
		volume = false;
		item_counter = 0;
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.51f, 0.54f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		draw();
		
	}

	private void update(float delta){
		camera.update();
		tilemap.update(camera,player,delta,master_volume);
		
		vwidth = Gdx.graphics.getWidth();
		vheight = Gdx.graphics.getHeight();
		float wscale = vwidth/800f;
		float hscale = vheight/600f;
		
		
		Gdx.graphics.setTitle("Ninja Time Fps: "+Gdx.graphics.getFramesPerSecond());
		
		if(item_counter != player.item_counter){
			item_counter = player.item_counter;
			item_alpha = 0;
		}
		if(item_counter != 0 && item_alpha < 1){
			item_alpha += delta * 0.5f;
		}
		
		
		if(tilemap.edit_mode){
			options = false;
			volume = false;
		}
		else{
			updateMenu(delta);
		}
		if(!options && !volume){
			if(!tilemap.edit_mode){
				player.update(delta,tilemap.map,tilemap.width,tilemap.height,master_volume);		
			}
			for(Cannon c:cannons)c.update(delta, camera,player,master_volume);
			for(Master m:masters)m.update(delta, camera, player);
			for(Item i:itens)i.update(player, delta,master_volume);
			
			if(!tilemap.edit_mode){
				if(player.position[1] > camera.position.y+50  && camera.position.y - 300 < tilemap.height-608)
					camera.translate(0, player.position[1] - camera.position.y -50);
				if(player.position[1] < camera.position.y-150  && camera.position.y - 300 > 8)
					camera.translate(0, player.position[1] - camera.position.y+150);
				if(player.position[0] > camera.position.x + 40 && camera.position.x - 400 < tilemap.width-808)
					camera.translate(player.position[0] - camera.position.x - 40,0);
				if(player.position[0] < camera.position.x -100 && camera.position.x - 400 > 8)
					camera.translate(player.position[0] - camera.position.x + 100, 0);
			}
			else{
				if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
					player.position[0] = (Gdx.input.getX() + camera.position.x*wscale - vwidth/2)/wscale;
					player.position[1] = ((vheight-Gdx.input.getY()) + camera.position.y*hscale - vheight/2)/hscale;
				}
					
			}
		}		
		
	}
	
	private void updateMenu(float delta){
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !volume){
			options = !options;
		}
		if(options){
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S))
				current_option++;
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
				current_option--;
			if(current_option > 3) current_option = 3;
			if(current_option < 0) current_option = 0;
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
					Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
				if(current_option == 0){
					options = false;
					current_option = 0;					

				}
				if(current_option == 1){
					options = false;
					volume = true;
					current_option = 0;					
				}
				if(current_option == 2){
					player.particles_on = !player.particles_on;
				}
				if(current_option == 3){
					int a = JOptionPane.showConfirmDialog(null, "Are you sure you wanna quit?");
					if(a == JOptionPane.YES_OPTION){
						Gdx.app.exit();
						return;
						
					}
				}
			}
			
		}
		if(volume){
			if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
				volume = false;
				options = true;
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S))
				current_option++;
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W))
				current_option--;
			if(current_option > 2) current_option = 2;
			if(current_option < 0) current_option = 0;
			if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
					Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
				if(current_option == 1){
					master_volume = 0;
					main_theme.setVolume(master_volume);

				}
				if(current_option == 2){
					options = true;
					volume = false;
					current_option = 0;		
				}			
			}
			if(current_option == 0){
				if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D))&&master_volume <= 1)
					master_volume += delta * 0.4f;
				if((Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A))&&master_volume >= 0)
					master_volume -= delta * 0.4f;
				if(master_volume > 1) master_volume = 1;
				if(master_volume < 0) master_volume = 0;
				main_theme.setVolume(master_volume);
			}
			
			
		}
	}
	private void drawMenu(){
		if(options){
			font_32.draw(batch, "Options", camera.position.x-110, camera.position.y+265);
			batch.draw(ninja_star,camera.position.x - 260, camera.position.y+90-68*current_option,64,64);
			if(current_option == 0)
				font_32.draw(batch,"RESUME",camera.position.x-96, camera.position.y+150);
			else
				font_16.draw(batch,"RESUME",camera.position.x-50, camera.position.y+125);
			if(current_option == 1)
				font_32.draw(batch,"VOLUME",camera.position.x-100, camera.position.y+85);
			else
				font_16.draw(batch,"VOLUME",camera.position.x-45, camera.position.y+55);
			if(current_option == 2)
				font_32.draw(batch,"PARTICLES "+(player.particles_on?"ON":"OFF"),camera.position.x-180, camera.position.y+15);
			else
				font_16.draw(batch,"PARTICLES "+(player.particles_on?"ON":"OFF"),camera.position.x-100, camera.position.y-10);
			if(current_option == 3)
				font_32.draw(batch,"QUIT GAME",camera.position.x-140, camera.position.y-50);
			else
				font_16.draw(batch,"QUIT GAME",camera.position.x-70, camera.position.y-75);				
		}
		if(volume){
			font_32.draw(batch, "Volume", camera.position.x-98, camera.position.y+265);
			batch.draw(ninja_star,camera.position.x - 360, camera.position.y+90-100*current_option,64,64);
			
			font_16.draw(batch,"MIN ------------------------ MAX",camera.position.x-250, camera.position.y+120);
			font_32.draw(batch,"|",camera.position.x-190+master_volume*375, camera.position.y+142);
			if(current_option == 1)
				font_32.draw(batch,"MUTE",camera.position.x-75, camera.position.y+50);
			else
				font_16.draw(batch,"MUTE",camera.position.x-40, camera.position.y+25);
			if(current_option == 2)
				font_32.draw(batch,"GO BACK",camera.position.x-120, camera.position.y-50);
			else
				font_16.draw(batch,"GO BACK",camera.position.x-60, camera.position.y-75);
		}
	}
	
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tilemap.draw(batch,camera.position.x - 400,camera.position.y-300,camera);
		for(Cannon c:cannons)c.draw(batch);
		for(Master m:masters)m.draw(batch,font_16);
		for(Item i:itens)i.draw(batch);
		player.draw(batch);
		
		drawMenu();
		
		//font.draw(batch,i , 20, 400);
		if(item_counter != 0){
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 370 + (item_counter-1)*50,camera.position.y+180,96,96,0,0,32,32,false,false);
		}
		batch.setColor(Color.BLACK);
		for(int i = item_counter; i < 3; i++)
			batch.draw(item_texture[0],camera.position.x - 370 + i*50,camera.position.y+180,96,96,0,0,32,32,false,false);
		batch.setColor(Color.WHITE);
		for(int i = 0; i < item_counter-1; i++)
			batch.draw(item_texture[0],camera.position.x - 370 + i*50,camera.position.y+180,96,96,0,0,32,32,false,false);
		
		
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
		minorDipose();
		for(int i = 0; i < cannons.length; i++)cannons[i].dispose();
		for(int i = 0; i < masters.length; i++)masters[i].dispose();
		for(int i = 0; i < itens.length; i++)itens[i].dispose();
		font_32.dispose();
		font_16.dispose();
		for(int i = 0; i < master_texture.length; i++)master_texture[i].dispose();
		for(int i = 0; i < item_texture.length; i++)item_texture[i].dispose();
		cannonD.dispose();
		cannonR.dispose();
		cannonL.dispose();
		cannonBall.dispose();
		ninja_star.dispose();
		bomb_sound.dispose();
		item_sound.dispose();
		item3_sound.dispose();
		main_theme.dispose();
	}
	
	public void minorDipose(){
		game = null;
		batch.dispose();
		tilemap.dispose();
		player.init(0,0);
	}
	
}
