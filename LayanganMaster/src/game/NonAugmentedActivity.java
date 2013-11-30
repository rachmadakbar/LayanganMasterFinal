package game;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import helper.Player;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Display;
import android.view.Menu;

import game.*;

public class NonAugmentedActivity extends SimpleBaseGameActivity implements SensorEventListener {
	// ===========================================================
		// Constants
		// ===========================================================

		private static int CAMERA_WIDTH;
		private static int CAMERA_HEIGHT;

		// ===========================================================
		// Fields
		// ===========================================================

		private ITextureRegion mKiteTextureRegion, mBoxTextureRegion, mCoinScoreTextureRegion, 
		mButtonUpTextureRegion, mButtonDownTextureRegion,  mBackgroundTextureRegion;
		private Sprite kite, background, coinScore, powerBox1, powerBox2, powerBox3, buttonUp, buttonDown, fire, fire2, fire3, 
		water, water2, water3, shield, shield2, shield3;
		private int accellerometerSpeedX;
		private int accellerometerSpeedY;
		private SensorManager sensorManager;
		private float centerX;
		private float centerY;
		private Camera camera;
		private Scene scene;
		private BitmapTextureAtlas mBitmapTextureAtlas, mItemBitmapTextureAtlas;
		//static int HPGame = game.Menu.player.HP;

		/*buat sprite burung*/
		private BuildableBitmapTextureAtlas mBirdBitmapTextureAtlas;
		private TiledTextureRegion mBirdTextureRegionRight;
		private TiledTextureRegion mBirdTextureRegionLeft;
		
		/*untuk sprite coin*/
		private ITextureRegion mCoinTextureRegion;
		private LinkedList CoinLL;
		private LinkedList CoinsToBeAdded;
		int hitCount = 0;
		//static int hitCount = game.Menu.player.coin;
		
		/*untuk sprite power item*/
		private ITextureRegion mFireTextureRegion;
		private ITextureRegion mWaterTextureRegion;
		private ITextureRegion mShieldTextureRegion;
		private LinkedList FireLL;
		private LinkedList FireToBeAdded;
		private LinkedList WaterLL;
		private LinkedList WaterToBeAdded;
		private LinkedList ShieldLL;
		private LinkedList ShieldToBeAdded;
		
		private String box1 = "";
		private String box2 = "";
		private String box3 = "";
		private int pointer = 1;
		
		private int healthPoin = 1000;
		
		private LinkedList BirdLLRight;
		private LinkedList BirdLLLeft;
		private LinkedList BirdToBeAddedRight;
		private LinkedList BirdToBeAddedLeft;
		
		/*untuk text perolehan coin dan HP*/
		private Font mFont;
		private Text scoreText, healthPoinText;
		
		/*HUD game*/
		private HUD gameHUD;
		
		private boolean upIsTouchedFlag = false;
		private boolean downIsTouchedFlag = false;
			
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Display display = getWindowManager().getDefaultDisplay();
		CAMERA_WIDTH = display.getWidth();
		CAMERA_HEIGHT = display.getHeight();
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true)
					.setNeedsSound(true);
		
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("asset/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				CAMERA_WIDTH, CAMERA_HEIGHT, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mItemBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),
				512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			
		
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "sky.jpg", 0,
						0);
		this.mKiteTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "kite1.png", 0,
						0); 
		
		this.mCoinTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "coin.png", 50,
						0);
		
		this.mFireTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "fire.png", 90,
						0);
		
		this.mWaterTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "water.png", 130,
						0);
		
		this.mShieldTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "shield.png", 180,
						0);
		
		this.mBoxTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "kotak.png", 0,
						100);
		
		this.mCoinScoreTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "coin2.png", 300,
						0);
		
		this.mButtonUpTextureRegion= BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "ulur.png", 150,
						100);
		
		this.mButtonDownTextureRegion= BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mItemBitmapTextureAtlas, this, "tarik.png", 50,
						100);
		
		
		this.mItemBitmapTextureAtlas.load();
		
		this.mBirdBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		this.mBirdTextureRegionRight = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBirdBitmapTextureAtlas, this, "bird1.png", 4, 1);
		this.mBirdTextureRegionLeft = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBirdBitmapTextureAtlas, this, "bird2.png", 4, 1);

		try {
			this.mBirdBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.mBitmapTextureAtlas.load();
			this.mBirdBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		FontFactory.setAssetBasePath("font/");

		mFont = FontFactory.createFromAsset(mEngine.getFontManager(),
		            mEngine.getTextureManager(), 256, 256, TextureOptions.BILINEAR,
		            this.getAssets(), "RAVIE.TTF", 20f, true,
		            Color.WHITE_ABGR_PACKED_INT);
		mFont.load();
		
	}

	@Override
	protected Scene onCreateScene() {
		sensorManager = (SensorManager) this
				.getSystemService(this.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				sensorManager.SENSOR_DELAY_GAME);
		
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {
			public void onUpdate(float pSecondsElapsed) {
				updateKitePosition();
			}

			public void reset() {
				// TODO Auto-generated method stub
			}
		});
		
		centerX = CAMERA_WIDTH/2f;
		centerY = CAMERA_HEIGHT/2f;
		
		scene = new Scene();
		
		background = new Sprite(0, 0, mBackgroundTextureRegion,
				getVertexBufferObjectManager());
		background.setSize(CAMERA_WIDTH, CAMERA_HEIGHT);
		scene.attachChild(background);
		
		kite = new Sprite(this.centerX, centerY, mKiteTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(kite);
		
		coinScore = new Sprite(this.CAMERA_WIDTH - 100, 10, mCoinScoreTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(coinScore);
		
		
		
		
		gameHUD = new HUD();
		
		/*inisialisasi linked list sprite coin + power item*/
		this.CoinLL = new LinkedList<Sprite>();
		this.CoinsToBeAdded = new LinkedList<Sprite>();
		this.FireLL = new LinkedList<Sprite>(); 
		this.FireToBeAdded = new LinkedList<Sprite>();
		this.WaterLL = new LinkedList<Sprite>();
		this.WaterToBeAdded = new LinkedList<Sprite>();
		this.ShieldLL = new LinkedList<Sprite>();
		this.ShieldToBeAdded = new LinkedList<Sprite>();
		this.BirdLLRight = new LinkedList<Sprite>();
		this.BirdLLLeft = new LinkedList<Sprite>();
		this.BirdToBeAddedLeft = new LinkedList<Sprite>();
		this.BirdToBeAddedRight = new LinkedList<Sprite>();
		
		createCoinSpriteTimeHandler();
		scene.registerUpdateHandler(coinHandler);
		
		createPowerSpriteTimeHandler();
		scene.registerUpdateHandler(powerHandler);
		
		createBirdSpriteTimeHandler();
		scene.registerUpdateHandler(birdHandler);
		
		createControllers();
		
		powerBox1 = new Sprite((CAMERA_WIDTH/2) - mBoxTextureRegion.getWidth() - 30, CAMERA_HEIGHT - mBoxTextureRegion.getHeight()-10, mBoxTextureRegion, this.getVertexBufferObjectManager() );
		scene.attachChild(powerBox1);
		
		powerBox2 = new Sprite((CAMERA_WIDTH/2)+ 5 - 30, CAMERA_HEIGHT - mBoxTextureRegion.getHeight()-10, mBoxTextureRegion, this.getVertexBufferObjectManager() );
		scene.attachChild(powerBox2);
		
		powerBox3 = new Sprite((CAMERA_WIDTH/2) + mBoxTextureRegion.getWidth()+ 10 - 30, CAMERA_HEIGHT - mBoxTextureRegion.getHeight()-10, mBoxTextureRegion, this.getVertexBufferObjectManager() );
		scene.attachChild(powerBox3);
		
		scoreText = new Text(CAMERA_WIDTH - 50, 10, mFont, "                    ", getVertexBufferObjectManager());
		scene.attachChild(scoreText);
		
		healthPoinText = new Text(10, 10, mFont, "Health Poins: 1000    ", getVertexBufferObjectManager());
		scene.attachChild(healthPoinText);
				
		fire = new Sprite(powerBox1.getX() + 8, powerBox1.getY() - 50, mFireTextureRegion, this.getVertexBufferObjectManager());
		fire2 = new Sprite(powerBox1.getX() + 8, powerBox1.getY() - 90, mFireTextureRegion, this.getVertexBufferObjectManager());
		fire3 = new Sprite(powerBox1.getX() + 8, powerBox1.getY() - 130, mFireTextureRegion, this.getVertexBufferObjectManager());
		water = new Sprite(powerBox2.getX() + 8, powerBox2.getY() - 50, mWaterTextureRegion, this.getVertexBufferObjectManager());
		water2 = new Sprite(powerBox2.getX() + 8, powerBox2.getY() - 90, mWaterTextureRegion, this.getVertexBufferObjectManager());
		water3 = new Sprite(powerBox2.getX() + 8, powerBox2.getY() - 130, mWaterTextureRegion, this.getVertexBufferObjectManager());
		shield = new Sprite(powerBox3.getX() + 8, powerBox3.getY() - 50, mShieldTextureRegion, this.getVertexBufferObjectManager());
		shield2 = new Sprite(powerBox3.getX() + 8, powerBox3.getY() - 90, mShieldTextureRegion, this.getVertexBufferObjectManager());
		shield3 = new Sprite(powerBox3.getX() + 8, powerBox3.getY() - 130, mShieldTextureRegion, this.getVertexBufferObjectManager());
		
		return scene;
		
	}
	
	
	
	// ===========================================================
	// Methods
	// ===========================================================
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				accellerometerSpeedX = (int) event.values[1];
				accellerometerSpeedY = (int) event.values[0];
				break;
			}
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//
	}
	
	private void updateKitePosition() {
		if ((accellerometerSpeedX != 0) || (accellerometerSpeedY != 0)) {
			// Set the Boundary limits
			int tL = 0;
			int lL = 0;
			int rL = CAMERA_WIDTH - (int) kite.getWidth();
			int bL = CAMERA_HEIGHT - (int) kite.getHeight();

			// Calculate New X,Y Coordinates within Limits
			if (centerX >= lL)
				centerX += accellerometerSpeedX;
			else
				centerX = lL;
			if (centerX <= rL)
				centerX += accellerometerSpeedX;
			else
				centerX = rL;
			/*
			if (centerY >= tL)
				centerY += accellerometerSpeedY;
			else
				centerY = tL;
			if (centerY <= bL)
				centerY += accellerometerSpeedY;
			else
				centerY = bL;
			 */
			// Double Check That New X,Y Coordinates are within Limits
			if (centerX < lL)
				centerX = lL;
			else if (centerX > rL)
				centerX = rL;
			/*
			if (centerY < tL)
				centerY = tL;
			else if (centerY > bL)
				centerY = bL;
			*/
			kite.setPosition(centerX, centerY);
		}
	}
	
	private void createCoinSpriteTimeHandler(){
		TimerHandler coinTimerHandler;
	    float mEffectSpawnDelay = 1f;
	 
	    coinTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addCoin();
					}
				});

		getEngine().registerUpdateHandler(coinTimerHandler);
	}
	
	private void addCoin(){
		Random rand = new Random();

		float y = camera.getHeight() + mCoinTextureRegion.getHeight();
		float minX = mCoinTextureRegion.getWidth();
		float maxX = camera.getWidth() - mCoinTextureRegion.getWidth();
		float rangeX = maxX - minX;
		float x = rand.nextFloat() * rangeX + minX;

		Sprite coin = new Sprite(x, y, this.mCoinTextureRegion.deepCopy(),
				this.getVertexBufferObjectManager());
		this.scene.attachChild(coin);

		// Coin duration
		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;

		MoveYModifier mod = new MoveYModifier(actualDuration,
				-coin.getHeight(), coin.getY());

		coin.registerEntityModifier(mod.deepCopy());
		this.CoinsToBeAdded.add(coin);
	}
	
	private void createPowerSpriteTimeHandler(){
		TimerHandler powerTimerHandler;
	    float mEffectSpawnDelay = 3f;
	 
	    powerTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addPower();
					}
				});

		getEngine().registerUpdateHandler(powerTimerHandler);
	}
	
	public void addPower(){
		Random rand = new Random();
		int tmp = rand.nextInt(2);
		if (tmp == 1 ){
			int type = rand.nextInt(3);
			if (type == 0){
				float y = camera.getHeight() + mFireTextureRegion.getHeight();
				float minX = mFireTextureRegion.getWidth();
				float maxX = camera.getWidth() - mFireTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Fire = new Sprite(x, y, this.mFireTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());

				int minDurationFire = 3;
				int maxDurationFire = 6;
				int rangeDurationFire = maxDurationFire - minDurationFire;
				int actualDurationFire = rand.nextInt(rangeDurationFire)
						+ minDurationFire;

				MoveYModifier modFire = new MoveYModifier(actualDurationFire,
						-Fire.getHeight(), Fire.getY());
				Fire.registerEntityModifier(modFire.deepCopy());

				this.scene.attachChild(Fire);
				this.FireToBeAdded.add(Fire);
			}
			else if (type == 1){
				float y = camera.getHeight() + mWaterTextureRegion.getHeight();
				float minX = mWaterTextureRegion.getWidth();
				float maxX = camera.getWidth() - mWaterTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Water = new Sprite(x, y, this.mWaterTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());

				int minDurationWater = 3;
				int maxDurationWater = 6;
				int rangeDurationWater = maxDurationWater - minDurationWater;
				int actualDurationWater = rand.nextInt(rangeDurationWater)
						+ minDurationWater;

				MoveYModifier modWater = new MoveYModifier(actualDurationWater,
						-Water.getHeight(), Water.getY());
				Water.registerEntityModifier(modWater.deepCopy());

				this.scene.attachChild(Water);
				this.WaterToBeAdded.add(Water);
			}
			else if (type == 2){
				float y = camera.getHeight() + mShieldTextureRegion.getHeight();
				float minX = mShieldTextureRegion.getWidth();
				float maxX = camera.getWidth() - mShieldTextureRegion.getWidth();
				float rangeX = maxX - minX;
				float x = rand.nextFloat() * rangeX + minX;

				Sprite Shield = new Sprite(x, y, this.mShieldTextureRegion.deepCopy(),
						this.getVertexBufferObjectManager());

				int minDurationShield = 3;
				int maxDurationShield = 6;
				int rangeDurationShield = maxDurationShield - minDurationShield;
				int actualDurationShield = rand.nextInt(rangeDurationShield)
						+ minDurationShield;

				MoveYModifier modShield = new MoveYModifier(actualDurationShield,
						-Shield.getHeight(), Shield.getY());
				Shield.registerEntityModifier(modShield.deepCopy());

				this.scene.attachChild(Shield);
				this.ShieldToBeAdded.add(Shield);
			}
		}
	}
	
	private void createBirdSpriteTimeHandler(){
		TimerHandler birdTimerHandler;
	    float mEffectSpawnDelay = 3f;
	 
	    birdTimerHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {

					public void onTimePassed(TimerHandler pTimerHandler) {
						addBird();
					}
				});

		getEngine().registerUpdateHandler(birdTimerHandler);
	}
	
	public void addBird(){
		Random rand = new Random();
		float x = camera.getWidth() + mBirdTextureRegionRight.getWidth();
		float minY = mBirdTextureRegionRight.getHeight();
		float maxY = camera.getHeight() - mBirdTextureRegionRight.getHeight();
		float rangeY = maxY - minY;
		float y = rand.nextFloat() * rangeY + minY;
		
		float a = camera.getWidth() + mBirdTextureRegionRight.getWidth();
		float minB = mBirdTextureRegionRight.getHeight();
		float maxB = camera.getHeight() - mBirdTextureRegionRight.getHeight();
		float rangeB = maxB - minB;
		float b = rand.nextFloat() * rangeB + minB;
		
		final AnimatedSprite birdRight = new AnimatedSprite(x, y, this.mBirdTextureRegionRight.deepCopy(), this.getVertexBufferObjectManager());
		birdRight.animate(100);
		
		final AnimatedSprite birdLeft = new AnimatedSprite(a, b, this.mBirdTextureRegionLeft.deepCopy(), this.getVertexBufferObjectManager());
		birdLeft.animate(100);
		
		
		int minDuration = 3;
		int maxDuration = 6;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = rand.nextInt(rangeDuration) + minDuration;
		
		int minLeft = 3;
		int maxLeft = 5;
		int rangeLeft = maxLeft - minLeft;
		int actualLeft = rand.nextInt(rangeLeft) + minLeft;
		
		MoveXModifier modRight = new MoveXModifier(actualDuration,
				-birdRight.getWidth(), birdRight.getX());
		
		MoveXModifier modLeft = new MoveXModifier(actualLeft, 
				birdLeft.getX(), -birdLeft.getWidth());
		
		birdRight.registerEntityModifier(modRight.deepCopy());
		birdLeft.registerEntityModifier(modLeft.deepCopy());
		this.BirdToBeAddedRight.add(birdRight);
		this.BirdToBeAddedLeft.add(birdLeft);
		this.scene.attachChild(birdRight);
		this.scene.attachChild(birdLeft);
	}
	
	IUpdateHandler coinHandler = new IUpdateHandler (){
		public void reset() {
		}
		public void onUpdate(final float pSecondsElapsed) {
			Iterator<Sprite> coin = CoinLL.iterator();
			Sprite _coin;
			boolean hit = false;

			// iterating over the targets
			while (coin.hasNext()) {
				_coin = coin.next();

				if (_coin.collidesWith(kite)) {
					hit = true;
				}

				if (hit) {
					removeSprite(_coin, coin);
					hitCount++;
					scoreText.setText(String.valueOf(hitCount));
					hit = false;
					//Debug.e("jumlah hitCount", String.valueOf(hitCount));
					
				}
			}
			CoinLL.addAll(CoinsToBeAdded);
			CoinsToBeAdded.clear();	
		}
	};
	
	IUpdateHandler powerHandler = new IUpdateHandler() {
		
		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			Iterator<Sprite> fire = FireLL.iterator();
			Iterator<Sprite> water = WaterLL.iterator();
			Iterator<Sprite> shield = ShieldLL.iterator();
			Sprite _fire;
			Sprite _water;
			Sprite _shield;
			boolean hitFire = false;
			boolean hitWater = false;
			boolean hitShield = false;
			
			while (fire.hasNext()){
				_fire = fire.next();

				if (_fire.collidesWith(kite)) {
					hitFire = true;
				}

				if (hitFire) {
//					_fire.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
					removeSprite(_fire, fire);
					fireAddToBox();
					hitFire = false;
				}
				
			}
			
			while (water.hasNext()){
				_water = water.next();

				if (_water.collidesWith(kite)) {
					hitWater = true;
				}

				if (hitWater) {
					removeSprite(_water, water);
					waterAddToBox();
					hitWater = false;
				}
			}
			
			while (shield.hasNext()){
				_shield = shield.next();

				if (_shield.collidesWith(kite)) {
					hitShield = true;
				}

				if (hitShield) {
					removeSprite(_shield, shield);
					shieldAddToBox();
					hitShield = false;			
				}
			}
			
			FireLL.addAll(FireToBeAdded);
			FireToBeAdded.clear();
			
			WaterLL.addAll(WaterToBeAdded);
			WaterToBeAdded.clear();
			
			ShieldLL.addAll(ShieldToBeAdded);
			ShieldToBeAdded.clear();
		}
	};
	
	IUpdateHandler birdHandler = new IUpdateHandler (){
		public void reset() {
		}
		public void onUpdate(final float pSecondsElapsed) {
			Iterator<Sprite> birdRight = BirdLLRight.iterator();
			Iterator<Sprite> birdLeft = BirdLLLeft.iterator();
			Sprite _bird;
			boolean hit = false;

			// iterating over the targets
			while (birdRight.hasNext()) {
				_bird = birdRight.next();

				if (_bird.collidesWith(kite)) {
					hit = true;
				}

				if (hit) {
					removeSprite(_bird, birdRight);
					healthPoin = healthPoin - 100;
					healthPoinText.setText("Health Poin: "+ String.valueOf(healthPoin));
					hit = false;
					
					
				}
			}
			// iterating over the targets
			while (birdLeft.hasNext()) {
				_bird = birdLeft.next();
					if (_bird.collidesWith(kite)) {
						hit = true;
					}
					if (hit) {
						removeSprite(_bird, birdLeft);
						healthPoin = healthPoin - 100;
						healthPoinText.setText("Health Poin: "+ String.valueOf(healthPoin));
						hit = false;
													
					}
			}
			
			BirdLLRight.addAll(BirdToBeAddedRight);
			BirdLLLeft.addAll(BirdToBeAddedLeft);
			BirdToBeAddedRight.clear();	
			BirdToBeAddedLeft.clear();
		}
	};
	
	/* safely detach the sprite from the scene and remove it from the iterator */
	public void removeSprite(final Sprite _sprite, Iterator<Sprite> it) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				scene.detachChild(_sprite);
			}
		});
		it.remove();
	}
	
	
	
//	public void createCoinScoreHUD(){
//		scoreText = new Text(CAMERA_WIDTH - 50, 0, this.mFont, "0123456789",
//				new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager()); 
//		scoreText.setSkewCenter(0, 0);  
//		scoreText.setText("0");
//		
//		gameHUD.attachChild(scoreText);
//		camera.setHUD(gameHUD);
//	}
	
	TimerHandler tUp;
	TimerHandler tDown;
	
	/*Button Controller*/
	private void createControllers(){
			
		final int tL = 20;
		final int bL = CAMERA_HEIGHT - (int) kite.getHeight() - 50;
		
		/*Handler untuk button*/
		tUp = new TimerHandler(0.01f, true,
				new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				if (centerY <= tL){
					centerY = tL;
				}   
				else{
	    			centerY -= 2;
	            	kite.setPosition(centerX, centerY);	
	            }
			}
		});
		
		// our button
		buttonUp = new ButtonSprite(this.CAMERA_WIDTH - mButtonUpTextureRegion.getWidth()-10, CAMERA_HEIGHT - mButtonUpTextureRegion.getHeight()-10, mButtonUpTextureRegion, this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if (pSceneTouchEvent.isActionDown()){
					mEngine.registerUpdateHandler(tUp);

				}
				else if (pSceneTouchEvent.isActionUp()){
					mEngine.unregisterUpdateHandler(tUp);
					
				}
				return true;
			}
		};
		
		
		/*Handler untuk button down*/
		tDown = new TimerHandler(0.01f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						if (centerY >= bL){
							centerY = bL;
						}
						else{
			    			centerY += 2;
			            	kite.setPosition(centerX, centerY);
			            }
					}
				});
		
		// our button
		buttonDown = new ButtonSprite(10, CAMERA_HEIGHT - mButtonDownTextureRegion.getHeight()-10, mButtonDownTextureRegion, this.getVertexBufferObjectManager()){
			
			@Override
	 		public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY){
				if (pSceneTouchEvent.isActionDown()){
					mEngine.registerUpdateHandler(tDown);
				}
				if (pSceneTouchEvent.isActionUp()){
					mEngine.unregisterUpdateHandler(tDown);
				}
				return true;
			}	
		};
		
	    gameHUD.registerTouchArea(buttonUp);
	    gameHUD.registerTouchArea(buttonDown);
	    gameHUD.attachChild(buttonUp);
	    gameHUD.attachChild(buttonDown);
	    camera.setHUD(gameHUD);
	}
	
	public void fireAddToBox(){
		if (pointer > 3 ){
			pointer = 1;
		}
		if (pointer == 1){
			if (box1 ==""){
				fire.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(fire);
			}else if (box1 == "water"){
				scene.detachChild(water);
				fire.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(fire);
			}else if (box1 == "shield"){
				scene.detachChild(shield);
				fire.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(fire);
			}
			box1 = "fire";
			++pointer;
		}
		else if (pointer == 2){
			if (box2 ==""){
				fire2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(fire2);
			}else if (box2 == "water"){
				scene.detachChild(water2);
				fire2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(fire2);
			}else if (box2 == "shield"){
				scene.detachChild(shield2);
				fire2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(fire2);
			}
			box2 = "fire";
			++pointer;
		}
		else if (pointer == 3){
			if (box3 ==""){
				fire3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(fire3);
			}else if (box3 == "water"){
				scene.detachChild(water3);
				fire3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(fire3);
			}else if (box3 == "shield"){
				scene.detachChild(shield3);
				fire3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(fire3);
			}
			box3 = "fire";
			++pointer;
		}	
	}
	
	public void waterAddToBox(){
		if (pointer > 3 ){
			pointer = 1;
		}
		if (pointer == 1){
			if (box1 ==""){
				water.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(water);
			}else if (box1 == "fire"){
				scene.detachChild(fire);
				water.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(water);
			}else if (box1 == "shield"){
				scene.detachChild(shield);
				water.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(water);
			}
			box1 = "water";
			++pointer;
		}
		else if (pointer == 2){
			if (box2 ==""){
				water2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(water2);
			}else if (box2 == "fire"){
				scene.detachChild(fire2);
				water2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(water2);
			}else if (box2 == "shield"){
				scene.detachChild(shield2);
				water2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(water2);
			}
			box2 = "water";
			++pointer;
		}
		else if (pointer == 3){
			if (box3 ==""){
				water3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(water3);
			}else if (box3 == "fire"){
				scene.detachChild(fire3);
				water3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(water3);
			}else if (box3 == "shield"){
				scene.detachChild(shield3);
				water3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(water3);
			}
			box3 = "water";
			++pointer;
		}	
	}
	
	public void shieldAddToBox(){
		if (pointer > 3 ){
			pointer = 1;
		}
		if (pointer == 1){
			if (box1 ==""){
				shield.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(shield);
			}else if (box1 == "fire"){
				scene.detachChild(fire);
				shield.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(shield);
			}else if (box1 == "water"){
				scene.detachChild(water);
				shield.setPosition(powerBox1.getX() + 8, powerBox1.getY() + 10);
				scene.attachChild(shield);
			}
			box1 = "shield";
			++pointer;
		}
		else if (pointer == 2){
			if (box2 ==""){
				shield2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(shield2);
			}else if (box2 == "fire"){
				scene.detachChild(fire2);
				shield2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(shield2);
			}else if (box2 == "water"){
				scene.detachChild(water2);
				shield2.setPosition(powerBox2.getX() + 8, powerBox2.getY() + 10);
				scene.attachChild(shield2);
			}
			box2 = "shield";
			++pointer;
		}
		else if (pointer == 3){
			if (box3 ==""){
				shield3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(shield3);
			}else if (box3 == "fire"){
				scene.detachChild(fire3);
				shield3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(shield3);
			}else if (box3 == "water"){
				scene.detachChild(water3);
				shield3.setPosition(powerBox3.getX() + 8, powerBox3.getY() + 10);
				scene.attachChild(shield3);
			}
			box3 = "shield";
			++pointer;
		}	
	}
	
	
	@Override
	protected void onDestroy()
	{
	    super.onDestroy();
	        
	    if (this.isGameLoaded())
	    {
	        System.exit(0);    
	    }
	}

}
