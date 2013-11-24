package helper;

import org.andengine.util.debug.Debug;

import android.content.Context;

public class Player{
	
	public int HP, exp, attack, defense, speed, coin, level, kite;
	public boolean isSound, isCamera, isDebt, isMaster, isLightning, buyGreen, buyPurple, buyBlue, isNew;
	LayanganMasterDBAdapter db;
	String username = "rabbit";
	
	public float scale, paddingY;
	
	public Player(Context ctx){
		db = new LayanganMasterDBAdapter(ctx);
		db.open();
		isNew = db.isNew(username); 
		if(isNew){
			db.insertUser(username, 0, 1, 10, 10, 10, 1000, 1, 1, 10000);
			db.insertBengkel("purple", 0);
			db.insertBengkel("green", 0);
			db.insertBengkel("blue", 0);
			db.insertAchievement("debt", 0);
			db.insertAchievement("master", 0);
			db.insertAchievement("lightning", 0);
			Debug.e("achievement", db.getAchievementCount()+"");
		}
		
		HP = db.getHP(username);
		exp = db.getEXP(username);
		attack = db.getAttack(username);
		defense = db.getDefense(username);
		speed = db.getSpeed(username);
		coin = db.getCoin(username);
		level = db.getLevel(username);
		isSound = db.isSound(username);
		isCamera = db.isCamera(username);
		isDebt = db.getAchievement("debt");
		isMaster = db.getAchievement("master");
		isLightning = db.getAchievement("lightning");
		buyGreen = db.getBengkel("green");
		buyPurple =db.getBengkel("purple");
		buyBlue = db.getBengkel("blue");
		db.close();
	}
	
	public void updatePower(int speed, int defense, int attack, int HP){
		this.HP = HP;
		this.speed = speed;
		this.attack = attack;
		this.defense = defense;
		db.open();
		db.updatePower(username, speed, defense, attack, HP);
		db.close();
	}
	
	public void updateExp(int newExp){
		db.open();
		db.updateEXP(username, exp);
		if(exp < 100 && newExp >= 100){
			this.level = 2;
			db.updateLevel(username, 2);
		}else if (exp < 250 && newExp >= 600){
			this.level = 3;
			db.updateLevel(username, 3);
			db.updateAchievement("master", 1);
			isMaster = true;
		}
		this.exp = newExp;
		db.close();	
	}
	
	public void updateCoin(int newCoin){
		coin = newCoin;
		db.open();
		db.updateCoin(username, newCoin);
		db.close();
	}
	
	public void beli(String name){
		
		if(name.equals("blue")) buyBlue = true;
		else if(name.equals("purple")) buyPurple = true;
		else if(name.equals("green")) buyGreen = true;
		
		db.open();
		db.updateBengkel(name, 1);
		
		if(buyBlue && buyPurple && buyGreen){
			db.updateAchievement("debt", 1);
			isDebt = true;
		}
		
		db.close();
	}
	
	public void checkLightning(int speed){
		if(speed == 30){
			db.open();
			db.updateAchievement("lightning", 1);
			db.close();
			isLightning = true;
		}
	}
	
	public void setSound(int sound){
		db.open();
		db.updateSound(username, sound);
		db.close();
		if(sound == 1) isSound = true;
		else isSound = false;
	}
	
	public void setCamera(int camera){
		db.open();
		db.updateCamera(username, camera);
		db.close();
		if(camera == 1) isCamera = true;
		else isCamera = false;
	}
	
	public void reset(){
		db.dropDatabase();
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
	
	public void setPaddingY(float paddingY){
		this.paddingY = paddingY;
	}
	
	public float getCameraHeight(double ratio){
		return  (float) (240 * scale*ratio + paddingY);
	}
	
	public void setKite(int i){
		kite = i;
	}
}
