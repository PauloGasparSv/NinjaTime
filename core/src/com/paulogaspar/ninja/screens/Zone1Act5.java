package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Item;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.Message;
import com.paulogaspar.ninja.tools.TileMap;

public class Zone1Act5 extends Stage implements Screen{
	public Zone1Act5(MyGame game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		batch = new SpriteBatch();
		ninja_star = new Texture(Gdx.files.internal("Misc/spr_star_0.png"));
		cannonD = new Texture(Gdx.files.internal("Misc/spr_cannondown_0.png"));
		cannonL = new Texture(Gdx.files.internal("Misc/spr_cannonright_0.png"));
		cannonR = new Texture(Gdx.files.internal("Misc/spr_cannonleft_0.png"));
		cannonBall = new Texture(Gdx.files.internal("Misc/spr_smoke_0.png"));
		master_texture = new Texture[2];
		master_texture[0] = new Texture(Gdx.files.internal("Sensei/spr_boss_0.png"));
		master_texture[1] = new Texture(Gdx.files.internal("Sensei/spr_boss_1.png"));
		item_texture = new Texture[4];
		item_texture[0] = new Texture(Gdx.files.internal("Riceball/sushi1.png"));
		item_texture[1] = new Texture(Gdx.files.internal("Riceball/sushi2.png"));
		item_texture[2] = new Texture(Gdx.files.internal("Riceball/sushi3.png"));
		item_texture[3] = new Texture(Gdx.files.internal("Riceball/sushi4.png"));
		font_32 = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
		font_16 = new BitmapFont(Gdx.files.internal("Misc/font_16.fnt"),Gdx.files.internal("Misc/font_16.png"),false);
		main_theme = Gdx.audio.newMusic(Gdx.files.internal("Music/main_theme.mp3"));
		bomb_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/8bit_bomb_explosion.wav"));
		item_sound = Gdx.audio.newSound(Gdx.files.internal("Sfx/Collect_Point_00.mp3"));
		player = new Ninja(camera,80,400);
		init();
		player.camera_start_pos[0] = camera.position.x;
		player.camera_start_pos[1] = camera.position.y;
	}
	public Zone1Act5(MyGame game,Ninja player,Texture master_texture[], Texture item_texture[],Texture cannonD,Texture cannonR,
			Texture cannonL,Texture cannonBall, Texture ninja_star, BitmapFont font_32,BitmapFont font_16, Music main_theme,
			Sound bomb_sound,Sound item_sound){
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		this.player = player;
		this.game = game;
		this.ninja_star = ninja_star;
		this.cannonD = cannonD;
		this.cannonL = cannonL;
		this.cannonR = cannonR;
		this.cannonBall = cannonBall;
		this.master_texture = master_texture;
		this.item_texture = item_texture;
		this.font_32 = font_32;
		this.font_16 = font_16;
		this.main_theme = main_theme;
	
		this.bomb_sound = bomb_sound;
		this.item_sound = item_sound;
		init();
	}
	public void init(){
		camera.translate(400-camera.position.x,650-camera.position.y);
		player.init(450,640,camera);

		while(!main_theme.isPlaying()){
			main_theme.play();
			main_theme.setVolume(player.master_volume);
			main_theme.setLooping(true);	
		}

		
		tilemap = new TileMap("zone1_act5.mapa");
		options = false;
		volume = false;
		current_option = 0;
		item_counter = 0;
		can_control = true;
		next_stage = false;
		transition_angle = 0f;
		changed_screen = false;
		stage_transition_alpha = 1;
		item_alpha = 0f;
		
		disposed = false;
		menu_press = false;
		down_press = false;
		up_press = false;
		ok_press = false;
		cancel_press = false;
		
		tilemap.edit_mode = false;
	
		next_stage_door = new Rectangle(3530,710,100,200);

		cannons = new Cannon[0];
		itens = new Item[3];
		masters = new Master[2];
	
		
		//CREATE CANNONS AND ITENS
		
		String message1[] = {"Great, a test of trust now", "Your handsome master tells you to...","Jump to the right as much as you can.",
				"Now, you'll die if you fall","So i must remind you of one thing...","Use your powers to go further...","If you don't understand try waiting.",
				"Who knows right, maybe god will...","show some floating text to help.","Hehe"};
		masters[0] = new Master(master_texture, 286, 634, message1, "JUMP YO!",40, false);
		
		String message2[] = {"HA, so you would actually die...","Well guess it was a good idea to put...","This floor here... hehe",
				"Remember to use your powers!","Use the shadows to teleport there.","And go left for a treat.",
				"See, I'm not that bad...","Go now"};
		masters[1] = new Master(master_texture, 1160, 120, message2, "GET OUT OF MY FLOOR!",40, false);
		
		itens[0] = new Item(item_texture,75,130,item_sound,item_sound);
		itens[1] = new Item(item_texture,2545,190,item_sound,item_sound);
		itens[2] = new Item(item_texture,3520,305,item_sound,item_sound);
		
		num_itens = itens.length;
		
		gamepad = null;
		try{
		gamepad = Controllers.getControllers().get(0);
		}catch(Exception e){}
		
		if(player.gamepad == null && gamepad != null){
			player.gamepad = gamepad;
		}
		if(player.gamepad != null && gamepad == null){
			player.gamepad = null;
		}
		
		
		messages = new Message[3];
		if(player.gamepad != null){
			messages[0] = new Message(360,825,"Use RB or LB to teleport",6);
			messages[1] = new Message(315,795,"Point with the stick or d-pad ",6);
			messages[2] = new Message(322,765,"to change teleport direction",6);
		}
		else{
			messages[0] = new Message(402,825,"Use C to teleport",6);
			messages[1] = new Message(315,795,"Point with the arrow keys to",6);
			messages[2] = new Message(340,765,"change teleport direction",6);
		}
		
		start_time = System.currentTimeMillis();
		timer = 0;
	}
	
		
	private void update(float delta){
		
		Gdx.graphics.setTitle("Ninja Time Fps: "+Gdx.graphics.getFramesPerSecond());
		tilemap.update(camera, player,delta, player.master_volume);

		vwidth = Gdx.graphics.getWidth();
		vheight = Gdx.graphics.getHeight();
		float wscale = vwidth/800f;
		float hscale = vheight/600f;
		
		if(item_counter != player.item_counter){
			item_counter = player.item_counter;
			item_alpha = 0;
		}
		if(item_counter != 0 && item_alpha < 1){
			item_alpha += delta * 0.5f;
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.F1))tilemap.edit_mode = !tilemap.edit_mode;
		
		if(tilemap.edit_mode){
			options = false;
			volume = false;
		}
		else if(!next_stage){
			if(gamepad == null)updateMenuKeyboard(delta);
			else updateMenuGamepad(delta);
		}
		if(options || volume)start_time = System.currentTimeMillis();
		if(!options && !volume){
			timer += System.currentTimeMillis() - start_time;
			start_time =System.currentTimeMillis();
			
			if(can_control){
				player.update(delta,tilemap.map,tilemap.width,tilemap.height);		
				for(Cannon c:cannons)c.update(delta, camera,player,player.master_volume);
				for(Master m:masters)m.update(delta, camera, player);
				for(Item i:itens)i.update(player, delta,player.master_volume);
				for(Message m:messages)m.update(delta, player);

			}
			if(!tilemap.edit_mode){
				float x = 0;
				float y = 0;
				
				if(player.position[1] > camera.position.y+50  && camera.position.y - 300 < tilemap.height-608)
					y += player.position[1] - camera.position.y -50;
				else if(player.position[1] < camera.position.y-150  && camera.position.y - 300 > 8)
					y += player.position[1] - camera.position.y + 150;
				if(player.position[0] > camera.position.x + 40 && camera.position.x - 400 < tilemap.width-808)
					x += player.position[0] - camera.position.x - 40;
				else if(player.position[0] < camera.position.x -100 && camera.position.x - 400 > 8)
					x += player.position[0] - camera.position.x + 100;
			
				if(camera.position.x + x > tilemap.width-408)x += tilemap.width - 408 - camera.position.x - x;
				else if(camera.position.x + x < 408) x+= 408 - camera.position.x - x;
				if(camera.position.y + y > tilemap.height-308)y += tilemap.height-308-camera.position.y - y;
				else if(camera.position.y + y < 308) y += 308 - camera.position.y - y;
			
				camera.translate(x,y);
				if(x!= 0 || y != 0)camera.update();
			}
			else{
				if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
					player.position[0] = (Gdx.input.getX() + camera.position.x*wscale - vwidth/2)/wscale;
					player.position[1] = ((vheight-Gdx.input.getY()) + camera.position.y*hscale - vheight/2)/hscale;
				}		
			}
			
			if(player.interact_press&&!next_stage&&player.rect().overlaps(next_stage_door)){
				can_control = false;
				next_stage = true;
			}
			
			if(next_stage){
				//CHANGE THIS PART IF THE VOLUME IS ALREADY DOWN! JUST PUT A *player.master_volume
				stage_transition_alpha += delta*0.75f;
				transition_angle -= 0.2f*delta;
				if(camera.zoom > 0.04)camera.zoom += transition_angle*0.05f;
				camera.rotate(transition_angle*0.75f);
				camera.update();
				main_theme.setVolume((1-stage_transition_alpha)*player.master_volume);
				if(camera.zoom < 0)camera.zoom = 0.01f;
				if(stage_transition_alpha > 1){
					stage_transition_alpha = 1;
					main_theme.stop();
					game.setScreen(new Points_state(game, player, player.master_volume, master_texture, item_texture, cannonD, 
							cannonR, cannonL, cannonBall, ninja_star, font_32, font_16, main_theme, bomb_sound, 
							item_sound,player.death_counter,4,item_counter,num_itens,timer,45000 ,true,5,"Get ready for a hard one!"));
					minorDipose();
					changed_screen = true;
					return;
				}
			}
			else if(stage_transition_alpha > 0 ){
				stage_transition_alpha -= delta * 0.5f;
				if(stage_transition_alpha < 0)stage_transition_alpha = 0;
				main_theme.setVolume((1-stage_transition_alpha)*player.master_volume);
			}
			else if(player.clock_playing != -1){
				main_theme.setVolume(player.master_volume*0.2f);
			}
			else 
				main_theme.setVolume(player.master_volume);
		}
	}
	private void draw(){
		if(disposed)return;
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		if(!set_buttons && !set_keys){	
			tilemap.draw(batch,camera.position.x - 400,camera.position.y-300,camera);
			
			for(Cannon c:cannons)c.draw(batch);
			for(Master m:masters)m.draw(batch,font_16);
			for(Item i:itens)i.draw(batch);
			for(Message m:messages)m.draw(batch, font_16, player);
			player.draw(batch,font_16);
		}
		
		
	
			
		
		if(item_counter == 0){
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}
		else if(item_counter == 1){
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}	
		else if(item_counter == 2) {
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.BLACK);
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}else{
			batch.draw(item_texture[0],camera.position.x - 360,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.draw(item_texture[0],camera.position.x - 310,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(new Color(1, 1, 1, item_alpha));
			batch.draw(item_texture[0],camera.position.x - 260,camera.position.y+180,96,96,0,0,32,32,false,false);
			batch.setColor(Color.WHITE);
		}
		
		
		font_32.draw(batch,""+timer/1000 ,camera.position.x+300,camera.position.y+270);

		
		if(stage_transition_alpha > 0){
			batch.setColor(new Color(0,0,0,stage_transition_alpha));
			batch.draw(tilemap.tiles[0],camera.position.x - 450, camera.position.y -350, 1200,700);
			batch.setColor(Color.WHITE);
		}
		
		drawMenu();

		
		batch.end();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.51f, 0.54f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		if(changed_screen)return;
		draw();
	}
	
	
	@Override
	public void dispose() {
		disposed = true;
		minorDipose();
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
		main_theme.dispose();
		player.dispose();

	}

	
	@Override
	public void show() {
		
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
}
