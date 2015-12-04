package com.paulogaspar.ninja.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.paulogaspar.ninja.actors.Ninja;

public class DataManager {
	
	private Preferences cfgprefs;
	private Preferences stageprefs;
	private Preferences aprefs;
	private Preferences keys;
	
	public DataManager(){
		cfgprefs = Gdx.app.getPreferences("config");
		stageprefs = Gdx.app.getPreferences("stage");
		aprefs = Gdx.app.getPreferences("achievements");
		keys = Gdx.app.getPreferences("keys");
	}
	
	public void setControls(){
		if(keys.getBoolean("changed") == true){
			Key_config.SHOOT_BUTTON = keys.getInteger("shootb");
			Key_config.JUMP_BUTTON = keys.getInteger("jumpb");
			Key_config.TELEPORT_BUTTON = keys.getInteger("teleportb");
			Key_config.TIME_BUTTON = keys.getInteger("timeb");
			Key_config.MENU_BUTTON = keys.getInteger("menub");
			
			
			Key_config.SHOOT_KEY =  keys.getInteger("shootk");
			Key_config.JUMP_KEY =  keys.getInteger("jumpk");
			Key_config.TELEPORT_KEY =  keys.getInteger("teleportk");
			Key_config.TIME_KEY =  keys.getInteger("timek");
			Key_config.MENU_KEY =  keys.getInteger("menuk");
			Key_config.UP_KEY =  keys.getInteger("upk");
			Key_config.DOWN_KEY =  keys.getInteger("downk");
			Key_config.LEFT_KEY =  keys.getInteger("leftk");
			Key_config.RIGHT_KEY =  keys.getInteger("rightk");
			Key_config.INTERACT_KEY = keys.getInteger("interactk");
		}
		else{
			Key_config.SHOOT_BUTTON = 3;
			Key_config.JUMP_BUTTON = 2;
			Key_config.TELEPORT_BUTTON = 5;
			Key_config.TIME_BUTTON = 7;
			Key_config.MENU_BUTTON = 9;
			
			Key_config.SHOOT_KEY = Input.Keys.X;
			Key_config.JUMP_KEY = Input.Keys.Z;
			Key_config.TELEPORT_KEY = Input.Keys.C;
			Key_config.TIME_KEY = Input.Keys.S;
			Key_config.MENU_KEY = Input.Keys.ESCAPE;
			Key_config.UP_KEY = Input.Keys.UP;
			Key_config.DOWN_KEY = Input.Keys.DOWN;
			Key_config.LEFT_KEY = Input.Keys.LEFT;
			Key_config.RIGHT_KEY = Input.Keys.RIGHT;
			Key_config.INTERACT_KEY = Input.Keys.UP;
			
		}
	}
	public void saveKeys(){
		keys.putBoolean("changed", true);
		
		keys.putInteger("shootb",Key_config.SHOOT_BUTTON);
		keys.putInteger("jumpb",Key_config.JUMP_BUTTON);
		keys.putInteger("teleportb",Key_config.TELEPORT_BUTTON);
		keys.putInteger("timeb",Key_config.TIME_BUTTON);
		keys.putInteger("menub",Key_config.MENU_BUTTON);
		
		keys.putInteger("shootk",Key_config.SHOOT_KEY);
		keys.putInteger("jumpk",Key_config.JUMP_KEY );
		keys.putInteger("teleportk",Key_config.TELEPORT_KEY);
		keys.putInteger("timek",Key_config.TIME_KEY);
		keys.putInteger("menuk",Key_config.MENU_KEY);
		keys.putInteger("upk",Key_config.UP_KEY );
		keys.putInteger("downk",Key_config.DOWN_KEY);
		keys.putInteger("leftk",Key_config.LEFT_KEY);
		keys.putInteger("rightk",Key_config.RIGHT_KEY);
		keys.putInteger("interactk",Key_config.INTERACT_KEY);
		
		keys.flush();
		
	}
	
	public void clearAll(){
		cfgprefs.clear();
		stageprefs.clear();
		aprefs.clear();
		keys.clear();
		cfgprefs.flush();
		stageprefs.flush();
		aprefs.flush();
		keys.flush();
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
