package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.paulogaspar.ninja.actors.Ninja;

public class DataManager {
	
	private Preferences cfgprefs;
	private Preferences stageprefs;
	private Preferences aprefs;
	
	public DataManager(){
		cfgprefs = Gdx.app.getPreferences("config");
		stageprefs = Gdx.app.getPreferences("stage");
		aprefs = Gdx.app.getPreferences("achievements");
	}
	
	public void clearAll(){
		cfgprefs.clear();
		stageprefs.clear();
		aprefs.clear();
	}
	
	public void savePoints(String stage_id,int points){
		stageprefs.putInteger(stage_id, points);
		stageprefs.flush();
	}
	public int getPoints(String stage_id){
		return stageprefs.getInteger(stage_id, -1);
	}
	
	public boolean getAchievement(String a){
		return aprefs.getBoolean(a);
	}
	
	public boolean markAchievement(String a,Ninja player){
		if(!getAchievement(a)){
			player.master_many_hit.play(player.master_volume*0.8f);
			aprefs.putBoolean(a, true);
			aprefs.flush();
			return true;
		}
		return false;
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
