package com.paulogaspar.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paulogaspar.ninja.MyGame;
import com.paulogaspar.ninja.actors.Cannon;
import com.paulogaspar.ninja.actors.Master;
import com.paulogaspar.ninja.actors.Ninja;
import com.paulogaspar.ninja.tools.TileMap;

public class Stage_test implements Screen {
	
	private SpriteBatch batch;
	private MyGame game;
	
	private OrthographicCamera camera;
	
	private TileMap tilemap;
	
	private Ninja player;
	
	private BitmapFont font_32,font_16;
	public Rectangle death_blocks[];
	
	private Texture cannonD,cannonR,cannonL,cannonBall;
	private Cannon cannons[];
	
	private Texture master_texture[];
	private Master masters[];
	
	
	public Stage_test(MyGame game) {
		this.game = game;
		batch = this.game.batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false,800,600);
		camera.translate(0, 192);
		
		font_32 = new BitmapFont(Gdx.files.internal("Misc/font.fnt"),Gdx.files.internal("Misc/font.png"),false);
		font_16 = new BitmapFont(Gdx.files.internal("Misc/font_16.fnt"),Gdx.files.internal("Misc/font_16.png"),false);
		cannonD = new Texture(Gdx.files.internal("Misc/spr_cannondown_0.png"));
		cannonL = new Texture(Gdx.files.internal("Misc/spr_cannonright_0.png"));
		cannonR = new Texture(Gdx.files.internal("Misc/spr_cannonleft_0.png"));
		cannonBall = new Texture(Gdx.files.internal("Misc/spr_smoke_0.png"));
		
		
		tilemap = new TileMap();
		player = new Ninja(camera);
	
		death_blocks = new Rectangle[7];
		death_blocks[0] = new Rectangle(128,216,458,64);
		death_blocks[1] = new Rectangle(0,40,tilemap.width,0);
		death_blocks[2] = new Rectangle(778,960,52,28);
		death_blocks[3] = new Rectangle(64,968,24,176);
		death_blocks[4] = new Rectangle(64,840,24,52);
		death_blocks[5] = new Rectangle(170,840,24,112);
		death_blocks[6] = new Rectangle(1480,1024+36,52,28);
		//X + 10, WIDTH - 14, IF ON CEEILING Y += 36 X - = 2
		//Y + 8, HEIGHT - 16, IF ON RIGHT SIDE X += 42
		//23 16
		cannons = new Cannon[3];
		cannons[2] = new Cannon(cannonD, cannonBall, 2048, 384, Cannon.RIGHT,Cannon.LEFT_RIGHT,500);
		cannons[1] = new Cannon(cannonD, cannonBall, 2176, 256, Cannon.UP,Cannon.DOWN_UP,900);
		cannons[0] = new Cannon(cannonD, cannonBall, 2176, 896, Cannon.RIGHT,Cannon.LEFT_RIGHT,200);

		
		master_texture = new Texture[2];
		master_texture[0] = new Texture(Gdx.files.internal("Sensei/spr_boss_0.png"));
		master_texture[1] = new Texture(Gdx.files.internal("Sensei/spr_boss_1.png"));
		
		masters = new Master[4];
		
		//I create multiple strins so i don't have to edit every single argument everytime i do a message=new string[something]
		String message[] = {"The training you must continue",
				"Jump to find the strength within you!",
				"Or don't...",
				"Enough said, excelsior!"};
		masters[0] = new Master(master_texture, 605, 446, message);
		String message2[] = {"Now a harder task you must complete",
				"Leap to the top without defeat",
				"...",
				"Just avoid the cannon balls"};
		masters[1] = new Master(master_texture, 1740, 446, message2);
		String message3[] = {"Now you must avoid the fall",
				"Press with magic through the wall",
				"...",
				"Yeah, it was pretty bad",
				"What i meant was:",
				"Go to the ledge and...",
				"Click on the other side",
				"Such magic"};
		masters[2] = new Master(master_texture, 1840, 1088, message3);
		String message4[] = {"Finisho"};
		masters[3] = new Master(master_texture, 440, 1020, message4);
		masters[3].changeTextColor();
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
		
		tilemap.edit(camera);
		for(Rectangle block:death_blocks)
			if(new Rectangle(player.rect()).overlaps(block))
				player.die();
		for(Cannon c:cannons)
			c.update(delta, camera,player);
		for(Master m:masters)
			m.update(delta, camera, player);
		
		
		player.update(delta,tilemap.map,tilemap.width,tilemap.height);
		
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
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && camera.position.x - 400 < tilemap.width-808)
				camera.translate(350*delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && camera.position.x - 400 > 8)
				camera.translate(-350*delta, 0);
			if(Gdx.input.isKeyPressed(Input.Keys.UP) && camera.position.y - 300 < tilemap.height-608)
				camera.translate(0,350*delta);
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && camera.position.y - 300 > 8)
				camera.translate(0,-350*delta);
			
			if(Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)){
				player.position[0] = Gdx.input.getX() + camera.position.x - 400;
				player.position[1] = (600-Gdx.input.getY()) + camera.position.y - 300;
			}
				
		}
		//on end
		//this.dispose();
		
		
		//DEATH
		
		
		
	}
	
	private void draw(){
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		tilemap.draw(batch,camera.position.x - 400,camera.position.y-300);
		for(Cannon c:cannons)
			c.draw(batch);
		for(Master m:masters)
			m.draw(batch,font_16);
		player.draw(batch);;
	
	
		//font.draw(batch,i , 20, 400);
		
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
		batch = null;
		game = null;
		master_texture[0].dispose();
		master_texture[1].dispose();
		cannonD.dispose();
		cannonR.dispose();
		cannonL.dispose();
		cannonBall.dispose();
		font_16.dispose();
		font_32.dispose();
		tilemap.dispose();
	}

}
