package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.paulogaspar.ninja.actors.Ninja;

public class DataManager {
	
	private Preferences cfgprefs;
	
	public DataManager(){
		cfgprefs = Gdx.app.getPreferences("config");
		
		
	}
	
	public void changeConfig(Ninja player){
		cfgprefs.putBoolean("particles_on", player.particles_on);
		cfgprefs.putFloat("master_volume", player.master_volume);
		cfgprefs.flush();
	}
	
	public float getVolume(){
		return cfgprefs.getFloat("master_volume", 1f);
	}
	public boolean getParticles(){
		return cfgprefs.getBoolean("particles_on", true);
	}
	
	public void dispose(){
		cfgprefs.flush();
	}
	
}
