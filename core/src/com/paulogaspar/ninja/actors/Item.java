package com.paulogaspar.ninja.actors;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Item {
	
	private boolean active;
	private boolean taken;

	private float position[];
	private float elapsed;
			
	//DELETE REFERENCE
	private Sound sound;
	private Sound sound2;
	private Animation animation;
	
	public Item(Texture master[],float posX,float posY, Sound sound,Sound sound2){
		
		TextureRegion a[] = new TextureRegion[2];
		a[0] = new TextureRegion(master[0]);
		a[1] = new TextureRegion(master[1]);
		animation = new Animation(1f,a);
		elapsed = 0f;
		this.sound = sound;
		this.sound2 = sound2;
		position = new float[2];
		position[0] = posX;
		position[1] = posY;
		active = false;
		taken  = false;
	}
	
	public void update(Ninja player,float delta,float volume){
		//FOR THIS ONE I WILL DO ANOTHER CODE FOR ACTIVATING
		//USING THE DIST BEETWEEN IT AND THE PLAYER ONLY
		if(player.item_counter == 0 && taken)taken = false;
		
		float dx = player.position[0] - position[0];
		float dy = player.position[1] - position[1];
		
		if(dx*dx < 620000 && dy*dy < 340000){
			active = true;
		}
		else{
			active = false;
			elapsed = 0;
		}
		
		if(!taken && active){
			elapsed += delta;
			if(new Rectangle(position[0]+10,position[1]+10,44,44).overlaps(player.rect())){
				taken = true;
				if(player.item_counter < 2)	sound.play(volume);
				else sound.play(volume);
				player.item_counter++;
			}
		}
		
		
	}
	public void draw(SpriteBatch batch){
		if(!taken && active){
			batch.draw(animation.getKeyFrame(elapsed,true),position[0],position[1],64,64);
		}
	}
	
	public void dispose(){
		animation = null;
		sound = null;
		sound2 = null;
	}

}
