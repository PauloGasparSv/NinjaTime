package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.paulogaspar.ninja.actors.Ninja;

public class DataManager {
	
	private Preferences cfgprefs;
	private Preferences stageprefs;
	
	public DataManager(){
		cfgprefs = Gdx.app.getPreferences("config");
		stageprefs = Gdx.app.getPreferences("stage");	
	}
	
	public void clearAll(){
		cfgprefs.clear();
		stageprefs.clear();
	}
	
	public void savePoints(String stage_id,int points){
		stageprefs.putInteger(stage_id, points);
		stageprefs.flush();
	}
	public int getPoints(String stage_id){
		return stageprefs.getInteger(stage_id, -1);
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
